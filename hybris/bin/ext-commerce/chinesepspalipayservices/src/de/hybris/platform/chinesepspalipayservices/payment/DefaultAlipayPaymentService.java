 /*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 */
package de.hybris.platform.chinesepspalipayservices.payment;

import de.hybris.platform.chinesepaymentservices.enums.ServiceType;
import de.hybris.platform.chinesepaymentservices.model.ChinesePaymentInfoModel;
import de.hybris.platform.chinesepaymentservices.order.service.ChineseOrderService;
import de.hybris.platform.chinesepaymentservices.payment.ChinesePaymentService;
import de.hybris.platform.chinesepspalipayservices.alipay.AlipayConfiguration;
import de.hybris.platform.chinesepspalipayservices.constants.PaymentConstants;
import de.hybris.platform.chinesepspalipayservices.data.AlipayCancelPaymentRequestData;
import de.hybris.platform.chinesepspalipayservices.data.AlipayDirectPayRequestData;
import de.hybris.platform.chinesepspalipayservices.data.AlipayPaymentStatusRequestData;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRawCancelPaymentResult;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRawDirectPayErrorInfo;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRawDirectPayNotification;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRawDirectPayReturnInfo;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRawPaymentStatus;
import de.hybris.platform.chinesepspalipayservices.order.AlipayOrderService;
import de.hybris.platform.chinesepspalipayservices.strategies.AlipayCreateRequestStrategy;
import de.hybris.platform.chinesepspalipayservices.strategies.AlipayPaymentInfoStrategy;
import de.hybris.platform.chinesepspalipayservices.strategies.AlipayPaymentTransactionStrategy;
import de.hybris.platform.chinesepspalipayservices.strategies.AlipayResponseValidationStrategy;
import de.hybris.platform.commerceservices.order.CommerceCheckoutService;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.core.enums.PaymentStatus;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.impl.DefaultPaymentServiceImpl;
import de.hybris.platform.payment.model.AlipayPaymentTransactionEntryModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


public class DefaultAlipayPaymentService extends DefaultPaymentServiceImpl implements ChinesePaymentService
{
	private static final Logger LOG = Logger.getLogger(DefaultAlipayPaymentService.class.getName());
	private AlipayPaymentInfoStrategy alipayPaymentInfoStrategy;
	private CommerceCheckoutService commerceCheckoutService;
	private AlipayOrderService alipayOrderService;
	private AlipayCreateRequestStrategy alipayCreateRequestStrategy;
	private AlipayPaymentTransactionStrategy alipayPaymentTransactionStrategy;
	private AlipayResponseValidationStrategy alipayResponseValidationStrategy;
	private MediaService mediaService;
	private ChineseOrderService chineseOrderService;
	private AlipayConfiguration alipayConfiguration;
	private ModelService modelService;

	@Override
	public void handleAsynResponse(final HttpServletRequest request, final HttpServletResponse response)
	{
		final Map<String, String> unifyResponseMap = unifyRequestParameterValue(request.getParameterMap());

		if (request.getRequestURL().toString().contains(PaymentConstants.Controller.DIRECT_AND_EXPRESS_NOTIFY_URL))
		{
			final boolean verify = alipayResponseValidationStrategy.validateResponse(unifyResponseMap);
			if (!verify)
			{
				LOG.warn("Invalid notify from Alipay");
				return;
			}
			try
			{
				handleNotification(unifyResponseMap, response);
			}
			catch (final IOException e)
			{
				LOG.error("Handle notification for alipay failed", e);
			}
		}
		else if (request.getRequestURL().toString().contains(PaymentConstants.Controller.ERROR_NOTIFY_URL))
		{
			handleErrorResponse(unifyResponseMap);
		}
	}

	@Override
	public String handleSyncResponse(final HttpServletRequest request, final HttpServletResponse response)
	{
		final Map<String, String> unifyResponseMap = unifyRequestParameterValue(request.getParameterMap());
		if (request.getRequestURL().toString().contains(PaymentConstants.Controller.DIRECT_AND_EXPRESS_RETURN_URL))
		{

			final boolean verify = alipayResponseValidationStrategy.validateResponse(unifyResponseMap);

			if (!verify)
			{
				LOG.warn("Invalid return from Alipay");
			}
			// TODO check if business process is right
			try
			{
				return handleReturnInfo(unifyResponseMap);
			}
			catch (final IOException e)
			{
				LOG.error("Handle returnInfo for alipay failed", e);
			}

		}
		return null;

	}

	@Override
	public boolean cancelPayment(final String orderCode)
	{
		final OrderModel orderModel = getOrderModelByCode(orderCode);

		final AlipayCancelPaymentRequestData alipayCancelPaymentRequestData = new AlipayCancelPaymentRequestData();

		if (orderModel.getPaymentStatus().equals(PaymentStatus.PAID))
		{
			return false;
		}
		setAlipayCancelPaymentRequestDataByOrder(orderModel, alipayCancelPaymentRequestData);
		try
		{

			final boolean isCaputreNotExist = alipayPaymentTransactionStrategy.checkCaptureTransactionEntry(orderModel,
					TransactionStatus.ACCEPTED);

			if (isCaputreNotExist)
			{
				final AlipayRawCancelPaymentResult alipayRawCancelPaymentResult = alipayCreateRequestStrategy
						.submitCancelPaymentRequest(alipayCancelPaymentRequestData);

				alipayPaymentTransactionStrategy.updateForCancelPayment(orderModel, alipayRawCancelPaymentResult);

				if (alipayRawCancelPaymentResult.getError() == null)
				{
					return true;
				}

			}
			else
			{
				orderModel.setPaymentStatus(PaymentStatus.PAID);
			}
		}
		catch (final Exception e)
		{
			LOG.error("Cancel trade:" + orderCode + " fails", e);
		}
		return false;
	}

	@Override
	public String getPaymentRequestUrl(final String orderCode)
	{
		final OrderModel orderModel = alipayOrderService.getOrderByCode(orderCode);

		final AlipayDirectPayRequestData alipayDirectPayRequestData = new AlipayDirectPayRequestData();

		setAlipayDirectPayRequestDataByOrder(orderModel, alipayDirectPayRequestData);

		try
		{
			final String directPayUrl = alipayCreateRequestStrategy.createDirectPayUrl(alipayDirectPayRequestData);

			alipayPaymentTransactionStrategy.createForNewRequest(orderModel, alipayDirectPayRequestData, directPayUrl);

			return directPayUrl;
		}
		catch (final Exception e)
		{
			LOG.error("Create alipay direct pay url fails", e);

			return null;
		}

	}

	@Override
	public void syncPaymentStatus(final String orderCode)
	{
		final OrderModel orderModel = alipayOrderService.getOrderByCode(orderCode);
		final AlipayPaymentStatusRequestData alipayPaymentStatusRequestData = new AlipayPaymentStatusRequestData();
		setAlipayPaymentStatusRequestDataByOrder(orderModel, alipayPaymentStatusRequestData);
		try
		{
			final AlipayRawPaymentStatus alipayRawPaymentStatus = alipayCreateRequestStrategy
					.submitPaymentStatusRequest(alipayPaymentStatusRequestData);
			if (alipayRawPaymentStatus != null)
			{
				final AlipayPaymentTransactionEntryModel entry = alipayPaymentTransactionStrategy.saveForStatusCheck(orderModel,
						alipayRawPaymentStatus);

				if (entry != null && entry.getTransactionStatus().equals(TransactionStatus.ACCEPTED.name()))
				{
					orderModel.setPaymentStatus(PaymentStatus.PAID);
					modelService.save(orderModel);
					chineseOrderService.markOrderAsPaid(orderCode);
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error("Check alipay trade fails", e);
		}

	}

	@Override
	public boolean setPaymentInfo(final CartModel cartModel, final ChinesePaymentInfoModel chinesePaymentInfoModel)
	{
		cartModel.setChinesePaymentInfo(chinesePaymentInfoModel);
		alipayPaymentInfoStrategy.updatePaymentInfoForPayemntMethod(chinesePaymentInfoModel);
		final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		parameter.setPaymentInfo(chinesePaymentInfoModel);
		return commerceCheckoutService.setPaymentInfo(parameter);
	}

	@Override
	public String getPspLogoUrl()
	{
		final MediaModel alipayLog = mediaService.getMedia("/images/theme/alipay.jpg");
		return alipayLog.getURL();
	}

	/**
	 * retrieve OrderModel by order code
	 *
	 * @param orderCode
	 *           order code of OrderModel
	 * @return OrderModel
	 */
	protected OrderModel getOrderModelByCode(final String orderCode)
	{
		final OrderModel orderModel = alipayOrderService.getOrderByCode(orderCode);
		if (orderModel == null)
		{
			throw new UnknownIdentifierException(
					"Order with orderGUID " + orderCode + " not found for current user in current BaseStore");
		}

		return orderModel;

	}

	protected Map<String, String> unifyRequestParameterValue(final Map<String, String[]> params)
	{
		final Map<String, String> unifyRequestMap = new LinkedHashMap<String, String>();
		for (final String key : params.keySet())
		{
			final String[] strArray = params.get(key);
			final StringBuilder builder = new StringBuilder();
			for (final String s : strArray)
			{
				builder.append(s);
			}
			final String value = builder.toString();
			unifyRequestMap.put(key, value);
		}
		return unifyRequestMap;
	}

	protected Map<String, String> convertKey2CamelCase(final Map<String, String> snakeCaseMap)
	{
		final Map<String, String> camelCaseMap = new LinkedHashMap<String, String>();
		for (final String key : snakeCaseMap.keySet())
		{
			final String value = snakeCaseMap.get(key);
			String camelKey = WordUtils.capitalizeFully(key, new char[]
			{ '_' }).replaceAll("_", "");
			camelKey = WordUtils.uncapitalize(camelKey);
			camelCaseMap.put(camelKey, value);
		}
		return camelCaseMap;
	}

	protected void handleNotification(final Map<String, String> responseMap, final HttpServletResponse response) throws IOException
	{
		final AlipayRawDirectPayNotification alipayRawDirectPayNotification = new AlipayRawDirectPayNotification();
		final Map<String, String> camelCaseMap = convertKey2CamelCase(responseMap);
		try
		{
			BeanUtils.populate(alipayRawDirectPayNotification, camelCaseMap);
		}
		catch (IllegalAccessException | InvocationTargetException e)
		{
			LOG.error("Handle notify info from Alipay fails", e);
		}
		final String orderCode = alipayRawDirectPayNotification.getOutTradeNo();
		final OrderModel orderModel = getOrderModelByCode(orderCode);
		alipayPaymentTransactionStrategy.updateForNotification(orderModel, alipayRawDirectPayNotification);
		final String tradeStatus = PaymentConstants.TransactionStatusMap.AlipayToHybris
				.get(alipayRawDirectPayNotification.getTradeStatus()).name();

		if (tradeStatus.equals(TransactionStatus.ACCEPTED.name()))
		{
			orderModel.setPaymentStatus(PaymentStatus.PAID);
		}
		else
		{
			orderModel.setPaymentStatus(PaymentStatus.NOTPAID);
		}
		modelService.save(orderModel);
		chineseOrderService.markOrderAsPaid(orderCode);

		response.getWriter().print("success");
	}

	protected String handleReturnInfo(final Map<String, String> responseMap) throws IOException
	{
		final AlipayRawDirectPayReturnInfo alipayRawDirectPayReturnInfo = new AlipayRawDirectPayReturnInfo();
		final Map<String, String> camelCaseMap = convertKey2CamelCase(responseMap);
		try
		{
			BeanUtils.populate(alipayRawDirectPayReturnInfo, camelCaseMap);
		}
		catch (IllegalAccessException | InvocationTargetException e)
		{
			LOG.error("Handle notify info from Alipay fails", e);
		}
		final String orderCode = alipayRawDirectPayReturnInfo.getOutTradeNo();
		return orderCode;
	}

	protected void handleErrorResponse(final Map<String, String> responseMap)
	{
		final String orderCode = responseMap.get(PaymentConstants.ErrorHandler.OUT_TRADE_NO);
		final OrderModel orderModel = alipayOrderService.getOrderByCode(orderCode);

		final Map<String, String> camelCaseMap = convertKey2CamelCase(responseMap);

		final AlipayRawDirectPayErrorInfo alipayRawDirectPayErrorInfo = new AlipayRawDirectPayErrorInfo();

		try
		{
			BeanUtils.populate(alipayRawDirectPayErrorInfo, camelCaseMap);

			alipayPaymentTransactionStrategy.updateForError(orderModel, alipayRawDirectPayErrorInfo);
		}
		catch (IllegalAccessException | InvocationTargetException e)
		{
			LOG.error("Handle error notify failes", e);
		}
	}

	protected void setAlipayDirectPayRequestDataByOrder(final OrderModel source, final AlipayDirectPayRequestData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		if (source.getPaymentInfo() != null)
		{
			if (source.getPaymentInfo() instanceof ChinesePaymentInfoModel)
			{
				final ChinesePaymentInfoModel paymentInfo = (ChinesePaymentInfoModel) source.getPaymentInfo();
				final ServiceType serviceType = paymentInfo.getServiceType();
				if (serviceType.equals(ServiceType.DIRECTPAY))
				{
					target.setPaymethod(alipayConfiguration.getDirectayPaymethodName());
				}
				else
				{
					target.setPaymethod(alipayConfiguration.getExpressPaymethodName());
				}
				target.setOutTradeNo(source.getCode());
				target.setSubject(alipayConfiguration.getRequestSubject() + source.getCode());
				target.setTotalFee(alipayConfiguration.getRequestPrice(source.getTotalPrice().doubleValue()));

				if (alipayConfiguration.getRequestTimeout() != null)
				{
					target.setItBPay(alipayConfiguration.getRequestTimeout());
				}

				target.setService(alipayConfiguration.getDirectPayServiceApiName());
				target.setPartner(alipayConfiguration.getWebPartner());
				target.setInputCharset(PaymentConstants.Basic.INPUT_CHARSET);
				target.setReturnUrl(alipayConfiguration.getBasepath() + PaymentConstants.Controller.DIRECT_AND_EXPRESS_RETURN_URL);
				target.setNotifyUrl(alipayConfiguration.getBasepath() + PaymentConstants.Controller.DIRECT_AND_EXPRESS_NOTIFY_URL);
				target.setErrorNotifyUrl(alipayConfiguration.getBasepath() + PaymentConstants.Controller.ERROR_NOTIFY_URL);
				target.setSellerEmail(alipayConfiguration.getWebSellerEmail());
				target.setPaymentType(PaymentConstants.Basic.PaymentType.BUY_PRODUCT);

				long quantity = 0;
				for (final AbstractOrderEntryModel entry : source.getEntries())
				{
					quantity += entry.getQuantity().longValue();
				}

				target.setQuantity(quantity);

			}
		}
	}

	protected void setAlipayCancelPaymentRequestDataByOrder(final OrderModel source, final AlipayCancelPaymentRequestData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		target.setService(alipayConfiguration.getCloseTradeServiceApiName());
		target.setPartner(alipayConfiguration.getWebPartner());
		target.setInputCharset(PaymentConstants.Basic.INPUT_CHARSET);
		target.setOutOrderNo(source.getCode());
	}

	protected void setAlipayPaymentStatusRequestDataByOrder(final OrderModel source, final AlipayPaymentStatusRequestData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		target.setService(alipayConfiguration.getCheckTradeServiceApiName());
		target.setPartner(alipayConfiguration.getWebPartner());
		target.setInputCharset(PaymentConstants.Basic.INPUT_CHARSET);
		target.setOutTradeNo(source.getCode());
	}

	@Required
	public void setAlipayPaymentInfoStrategy(final AlipayPaymentInfoStrategy alipayPaymentInfoStrategy)
	{
		this.alipayPaymentInfoStrategy = alipayPaymentInfoStrategy;
	}

	@Required
	public void setCommerceCheckoutService(final CommerceCheckoutService commerceCheckoutService)
	{
		this.commerceCheckoutService = commerceCheckoutService;
	}

	@Required
	public void setMediaService(final MediaService mediaService)
	{
		this.mediaService = mediaService;
	}

	@Required
	public void setAlipayOrderService(final AlipayOrderService alipayOrderService)
	{
		this.alipayOrderService = alipayOrderService;
	}

	@Required
	public void setAlipayCreateRequestStrategy(final AlipayCreateRequestStrategy alipayCreateRequestStrategy)
	{
		this.alipayCreateRequestStrategy = alipayCreateRequestStrategy;
	}

	@Required
	public void setAlipayPaymentTransactionStrategy(final AlipayPaymentTransactionStrategy alipayPaymentTransactionStrategy)
	{
		this.alipayPaymentTransactionStrategy = alipayPaymentTransactionStrategy;
	}

	@Required
	public void setAlipayResponseValidationStrategy(final AlipayResponseValidationStrategy alipayResponseValidationStrategy)
	{
		this.alipayResponseValidationStrategy = alipayResponseValidationStrategy;
	}

	@Required
	public void setChineseOrderService(final ChineseOrderService chineseOrderService)
	{
		this.chineseOrderService = chineseOrderService;
	}

	@Required
	public void setAlipayConfiguration(final AlipayConfiguration alipayConfiguration)
	{
		this.alipayConfiguration = alipayConfiguration;
	}

	@Override
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected AlipayPaymentInfoStrategy getAlipayPaymentInfoStrategy()
	{
		return alipayPaymentInfoStrategy;
	}

	protected CommerceCheckoutService getCommerceCheckoutService()
	{
		return commerceCheckoutService;
	}

	protected AlipayOrderService getAlipayOrderService()
	{
		return alipayOrderService;
	}

	protected AlipayCreateRequestStrategy getAlipayCreateRequestStrategy()
	{
		return alipayCreateRequestStrategy;
	}

	protected AlipayPaymentTransactionStrategy getAlipayPaymentTransactionStrategy()
	{
		return alipayPaymentTransactionStrategy;
	}

	protected AlipayResponseValidationStrategy getAlipayResponseValidationStrategy()
	{
		return alipayResponseValidationStrategy;
	}

	protected MediaService getMediaService()
	{
		return mediaService;
	}

	protected ChineseOrderService getChineseOrderService()
	{
		return chineseOrderService;
	}

	protected AlipayConfiguration getAlipayConfiguration()
	{
		return alipayConfiguration;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

}
