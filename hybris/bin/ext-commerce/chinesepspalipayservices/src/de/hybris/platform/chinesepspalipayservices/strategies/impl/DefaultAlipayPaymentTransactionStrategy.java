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
package de.hybris.platform.chinesepspalipayservices.strategies.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.chinesepspalipayservices.constants.PaymentConstants;
import de.hybris.platform.chinesepspalipayservices.dao.AlipayPaymentTransactionDao;
import de.hybris.platform.chinesepspalipayservices.dao.AlipayPaymentTransactionEntryDao;
import de.hybris.platform.chinesepspalipayservices.data.AlipayDirectPayRequestData;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRawCancelPaymentResult;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRawDirectPayErrorInfo;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRawDirectPayNotification;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRawPaymentStatus;
import de.hybris.platform.chinesepspalipayservices.enums.AlipayPaymethod;
import de.hybris.platform.chinesepspalipayservices.strategies.AlipayPaymentTransactionStrategy;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.dto.TransactionStatusDetails;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.AlipayPaymentTransactionEntryModel;
import de.hybris.platform.payment.model.AlipayPaymentTransactionModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public class DefaultAlipayPaymentTransactionStrategy implements AlipayPaymentTransactionStrategy
{
	private final Logger LOG = Logger.getLogger(DefaultAlipayPaymentTransactionStrategy.class.getName());
	private ModelService modelService;
	private KeyGenerator paymentTransactionKeyGenerator;
	private AlipayPaymentTransactionEntryDao alipayPaymentTransactionEntryDao;
	private AlipayPaymentTransactionDao alipayPaymentTransactionDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void createForNewRequest(final OrderModel orderModel, final AlipayDirectPayRequestData directPayRequestData,
			final String requestUrl)
	{

		final AlipayPaymentTransactionModel alipayPaymentTransactionModel = createTransacionForNewRequest(orderModel,
				directPayRequestData, requestUrl);

		createTransactionEntryForNewRequest(orderModel, alipayPaymentTransactionModel);
	}

	protected final AlipayPaymentTransactionModel createTransacionForNewRequest(final OrderModel orderModel,
			final AlipayDirectPayRequestData directPayRequestData, final String requestUrl)
	{
		final AlipayPaymentTransactionModel alipayPaymentTransactionModel = (AlipayPaymentTransactionModel) modelService
				.create(AlipayPaymentTransactionModel.class);
		alipayPaymentTransactionModel.setOrder(orderModel);
		alipayPaymentTransactionModel.setCode(orderModel.getCode());
		alipayPaymentTransactionModel.setPaymentUrl(requestUrl);
		alipayPaymentTransactionModel.setPaymethod(AlipayPaymethod.DIRECTPAY);
		alipayPaymentTransactionModel.setRequestId(directPayRequestData.getOutTradeNo());
		alipayPaymentTransactionModel.setPaymentProvider(PaymentConstants.Basic.PAYMENT_PROVIDER);
		alipayPaymentTransactionModel.setTransactionInitiateDate(new Date());
		modelService.save(alipayPaymentTransactionModel);
		return alipayPaymentTransactionModel;
	}



	protected AlipayPaymentTransactionEntryModel createTransactionEntryForNewRequest(final OrderModel orderModel,
			final AlipayPaymentTransactionModel alipayPaymentTransactionModel)
	{
		final AlipayPaymentTransactionEntryModel entry = (AlipayPaymentTransactionEntryModel) modelService
				.create(AlipayPaymentTransactionEntryModel.class);
		entry.setAmount(BigDecimal.valueOf(orderModel.getTotalPrice().doubleValue()));
		if (orderModel.getCurrency() != null)
		{
			entry.setCurrency(orderModel.getCurrency());
		}
		entry.setType(PaymentTransactionType.REQUEST);
		entry.setTime(new Date());
		entry.setPaymentTransaction(alipayPaymentTransactionModel);
		entry.setRequestId(alipayPaymentTransactionModel.getRequestId());
		entry.setTransactionStatus(TransactionStatus.ACCEPTED.name());
		entry.setTransactionStatusDetails(TransactionStatusDetails.SUCCESFULL.name());
		entry.setCode(String.valueOf(paymentTransactionKeyGenerator.generate()));
		modelService.save(entry);
		return entry;
	}

	@Override
	public AlipayPaymentTransactionEntryModel saveForStatusCheck(final OrderModel orderModel,
			final AlipayRawPaymentStatus alipayRawPaymentStatus)
	{

		validateParameterNotNull(orderModel, "The given order is null!");
		validateParameterNotNull(alipayRawPaymentStatus, "The given status data is null!");
		final String error = alipayRawPaymentStatus.getError();
		if (error == null && alipayRawPaymentStatus.getTradeNo() != null)
		{
			final TransactionStatus status = PaymentConstants.TransactionStatusMap.AlipayToHybris
					.get(alipayRawPaymentStatus.getTradeStatus());
			if (status == null)
			{
				return null;
			}

			final AlipayPaymentTransactionModel alipayPaymentTransaction = getPaymentTransactionToUpdate(orderModel, status,
					alipayRawPaymentStatus.getTradeNo());

			if (alipayPaymentTransaction != null)
			{
				final AlipayPaymentTransactionEntryModel alipayPaymentTransactionEntry = (AlipayPaymentTransactionEntryModel) modelService
						.create(AlipayPaymentTransactionEntryModel.class);

				setEntryByTransaction(alipayPaymentTransaction, alipayPaymentTransactionEntry);
				setEntryByRawPaymentStatus(alipayRawPaymentStatus, alipayPaymentTransactionEntry);

				alipayPaymentTransactionEntry.setType(PaymentTransactionType.CAPTURE);
				alipayPaymentTransactionEntry.setTransactionStatus(status.name());
				alipayPaymentTransactionEntry.setTransactionStatusDetails("Trade Status" + alipayRawPaymentStatus.getTradeStatus());
				modelService.save(alipayPaymentTransactionEntry);
				modelService.refresh(alipayPaymentTransaction);

				return alipayPaymentTransactionEntry;
			}
		}

		return null;

	}


	@Override
	public boolean checkCaptureTransactionEntry(final OrderModel orderModel, final TransactionStatus status)
	{
		final AlipayPaymentTransactionModel returnTransaction = getPaymentTransactionWithCaptureEntry(orderModel, status);
		if (returnTransaction == null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}


	@Override
	public void updateForCancelPayment(final OrderModel orderModel,
			final AlipayRawCancelPaymentResult alipayRawCancelPaymentResult)
	{
		validateParameterNotNull(orderModel, "The given order is null!");
		validateParameterNotNull(alipayRawCancelPaymentResult, "The given closeTradeResponseData is null!");


		for (final PaymentTransactionModel transaction : orderModel.getPaymentTransactions())
		{
			if (transaction instanceof AlipayPaymentTransactionModel)
			{
				final AlipayPaymentTransactionEntryModel entry = (AlipayPaymentTransactionEntryModel) modelService
						.create(AlipayPaymentTransactionEntryModel.class);
				entry.setCode(String.valueOf(paymentTransactionKeyGenerator.generate()));
				entry.setType(PaymentTransactionType.CANCEL);
				entry.setTime(new Date());
				entry.setPaymentTransaction(transaction);
				if (alipayRawCancelPaymentResult.getError() == null)
				{
					entry.setTransactionStatus(TransactionStatus.ACCEPTED.name());
				}
				else
				{
					entry.setTransactionStatus(TransactionStatus.ERROR.name());
					entry.setTransactionStatusDetails(alipayRawCancelPaymentResult.getError());
				}

				modelService.save(entry);
			}
		}

	}

	@Override
	public void updateForNotification(final OrderModel orderModel,
			final AlipayRawDirectPayNotification directPayNotifyResponseData)
	{
		validateParameterNotNull(orderModel, "The given order is null!");
		validateParameterNotNull(directPayNotifyResponseData, "The given notifyData is null!");

		if (directPayNotifyResponseData.getTradeNo() != null)
		{
			final TransactionStatus status = PaymentConstants.TransactionStatusMap.AlipayToHybris
					.get(directPayNotifyResponseData.getTradeStatus());
			final AlipayPaymentTransactionModel alipayPaymentTransaction = getPaymentTransactionToUpdate(orderModel, status,
					directPayNotifyResponseData.getTradeNo());

			if (null == alipayPaymentTransaction)
			{
				LOG.warn("no AlipayPaymentTransaction found");
				return;
			}

			alipayPaymentTransaction.setAlipayCode(directPayNotifyResponseData.getTradeNo());
			modelService.save(alipayPaymentTransaction);
			final AlipayPaymentTransactionEntryModel alipayPaymentTransactionEntry = (AlipayPaymentTransactionEntryModel) modelService
					.create(AlipayPaymentTransactionEntryModel.class);
			setEntryByTransaction(alipayPaymentTransaction, alipayPaymentTransactionEntry);
			setEntryByNotification(directPayNotifyResponseData, alipayPaymentTransactionEntry);
			modelService.save(alipayPaymentTransactionEntry);
		}
	}


	@Override
	public void updateForError(final OrderModel orderModel, final AlipayRawDirectPayErrorInfo alipayRawDirectPayErrorInfo)
	{
		final AlipayPaymentTransactionModel alipayPaymentTransaction = alipayPaymentTransactionDao
				.findTransactionByLatestRequestEntry(orderModel, true);
		if (null == alipayPaymentTransaction)
		{
			LOG.warn("no AlipayPaymentTransaction found");
			return;
		}

		final AlipayPaymentTransactionEntryModel entry = (AlipayPaymentTransactionEntryModel) modelService
				.create(AlipayPaymentTransactionEntryModel.class);
		entry.setType(PaymentTransactionType.CAPTURE);
		entry.setTransactionStatus(TransactionStatus.ERROR.name());
		entry.setTransactionStatusDetails("Error Code" + alipayRawDirectPayErrorInfo.getErrorCode());
		entry.setPayerEmail(alipayRawDirectPayErrorInfo.getBuyerEmail());
		entry.setPayerId(alipayRawDirectPayErrorInfo.getBuyerId());
		entry.setPaymentTransaction(alipayPaymentTransaction);
		entry.setCode(String.valueOf(paymentTransactionKeyGenerator.generate()));
		modelService.save(entry);
		modelService.refresh(alipayPaymentTransaction);

	}

	protected AlipayPaymentTransactionModel getPaymentTransactionWithCaptureEntry(final OrderModel orderModel,
			final TransactionStatus status)
	{
		AlipayPaymentTransactionModel returnTransaction = null;
		for (final PaymentTransactionModel transaction : orderModel.getPaymentTransactions())
		{
			if (transaction instanceof AlipayPaymentTransactionModel)
			{
				final AlipayPaymentTransactionModel alipayTransaction = ((AlipayPaymentTransactionModel) transaction);
				if (alipayTransaction.getAlipayCode() != null)
				{
					final AlipayPaymentTransactionEntryModel alipayTransactionEntry = alipayPaymentTransactionEntryDao
							.findPaymentTransactionEntryByTypeAndStatus(PaymentTransactionType.CAPTURE, status, alipayTransaction);
					if (alipayTransactionEntry != null)
					{
						returnTransaction = alipayTransaction;
						break;
					}
					else
					{
						returnTransaction = null;
					}
				}
			}
		}

		return returnTransaction;
	}


	protected AlipayPaymentTransactionModel getPaymentTransactionToUpdate(final OrderModel orderModel,
			final TransactionStatus status, final String alipayCode)
	{
		AlipayPaymentTransactionModel alipayPaymentTransaction = getPaymentTransactionWithCaptureEntry(orderModel, status);
		if (alipayPaymentTransaction != null)
		{
			return null;
		}

		alipayPaymentTransaction = alipayPaymentTransactionDao.findTransactionByAlipayCode(alipayCode);
		if (alipayPaymentTransaction != null)
		{
			return alipayPaymentTransaction;
		}

		alipayPaymentTransaction = alipayPaymentTransactionDao.findTransactionByLatestRequestEntry(orderModel, true);
		if (alipayPaymentTransaction != null)
		{
			return alipayPaymentTransaction;
		}

		alipayPaymentTransaction = alipayPaymentTransactionDao.findTransactionByLatestRequestEntry(orderModel, false);
		if (alipayPaymentTransaction != null)
		{
			return alipayPaymentTransaction;
		}

		return alipayPaymentTransaction;
	}

	protected void setEntryByTransaction(final AlipayPaymentTransactionModel alipayPaymentTransaction,
			final AlipayPaymentTransactionEntryModel alipayPaymentTransactionEntry)
	{
		alipayPaymentTransactionEntry.setRequestId(alipayPaymentTransaction.getRequestId());
		alipayPaymentTransactionEntry.setPaymentTransaction(alipayPaymentTransaction);
	}

	protected void setEntryByRawPaymentStatus(final AlipayRawPaymentStatus alipayRawPaymentStatus,
			final AlipayPaymentTransactionEntryModel alipayPaymentTransactionEntry)
	{
		alipayPaymentTransactionEntry.setAdjustedAmount(Double.valueOf(alipayRawPaymentStatus.getTotalFee()));
		alipayPaymentTransactionEntry.setPayerEmail(alipayRawPaymentStatus.getBuyerEmail());
		alipayPaymentTransactionEntry.setPayerId(alipayRawPaymentStatus.getBuyerId());
		if (alipayRawPaymentStatus.getFlagTradeLocked() != null)
		{
			alipayPaymentTransactionEntry
					.setLocked(alipayRawPaymentStatus.getFlagTradeLocked().trim().equals("0") ? Boolean.TRUE : Boolean.FALSE);
		}
		if (alipayRawPaymentStatus.getUseCoupon() != null)
		{
			alipayPaymentTransactionEntry
					.setCouponUsed(alipayRawPaymentStatus.getUseCoupon().trim().equals("T") ? Boolean.TRUE : Boolean.FALSE);
		}
		alipayPaymentTransactionEntry.setSellerFee(Double.valueOf(alipayRawPaymentStatus.getToSellerFee()));
		alipayPaymentTransactionEntry.setSupplementaryStatus(alipayRawPaymentStatus.getAdditionalTradeStatus());
		alipayPaymentTransactionEntry.setTime(new Date());
		alipayPaymentTransactionEntry.setCode(String.valueOf(paymentTransactionKeyGenerator.generate()));
	}

	protected void setEntryByNotification(final AlipayRawDirectPayNotification directPayNotifyResponseData,
			final AlipayPaymentTransactionEntryModel alipayPaymentTransactionEntry)
	{
		alipayPaymentTransactionEntry.setTime(new Date());
		if (directPayNotifyResponseData.getUseCoupon() != null)
		{
			alipayPaymentTransactionEntry
					.setCouponUsed(directPayNotifyResponseData.getUseCoupon().trim().equals("T") ? Boolean.TRUE : Boolean.FALSE);
		}
		alipayPaymentTransactionEntry.setPayerEmail(directPayNotifyResponseData.getBuyerEmail());
		alipayPaymentTransactionEntry.setPayerId(directPayNotifyResponseData.getBuyerId());
		alipayPaymentTransactionEntry.setAdjustedAmount(Double.valueOf(directPayNotifyResponseData.getTotalFee()));
		alipayPaymentTransactionEntry.setType(PaymentTransactionType.CAPTURE);

		final String tradeStatus = directPayNotifyResponseData.getTradeStatus().trim();
		alipayPaymentTransactionEntry
				.setTransactionStatus(PaymentConstants.TransactionStatusMap.AlipayToHybris.get(tradeStatus).name());
		alipayPaymentTransactionEntry.setTransactionStatusDetails("Trade Status:" + tradeStatus);
		alipayPaymentTransactionEntry.setCode(String.valueOf(paymentTransactionKeyGenerator.generate()));
	}


	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Required
	public void setPaymentTransactionKeyGenerator(final KeyGenerator paymentTransactionKeyGenerator)
	{
		this.paymentTransactionKeyGenerator = paymentTransactionKeyGenerator;
	}


	@Required
	public void setAlipayPaymentTransactionEntryDao(final AlipayPaymentTransactionEntryDao alipayPaymentTransactionEntryDao)
	{
		this.alipayPaymentTransactionEntryDao = alipayPaymentTransactionEntryDao;
	}

	@Required
	public void setAlipayPaymentTransactionDao(final AlipayPaymentTransactionDao alipayPaymentTransactionDao)
	{
		this.alipayPaymentTransactionDao = alipayPaymentTransactionDao;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected KeyGenerator getPaymentTransactionKeyGenerator()
	{
		return paymentTransactionKeyGenerator;
	}

	protected AlipayPaymentTransactionEntryDao getAlipayPaymentTransactionEntryDao()
	{
		return alipayPaymentTransactionEntryDao;
	}

	protected AlipayPaymentTransactionDao getAlipayPaymentTransactionDao()
	{
		return alipayPaymentTransactionDao;
	}



}
