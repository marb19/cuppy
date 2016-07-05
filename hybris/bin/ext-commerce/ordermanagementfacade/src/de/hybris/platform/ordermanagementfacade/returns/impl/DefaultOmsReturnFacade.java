/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.ordermanagementfacade.returns.impl;


import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commerceservices.event.CreateReturnEvent;
import de.hybris.platform.commerceservices.impersonation.ImpersonationContext;
import de.hybris.platform.commerceservices.impersonation.ImpersonationService;
import de.hybris.platform.commerceservices.search.dao.PagedGenericDao;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.ordermanagementfacade.OmsBaseFacade;
import de.hybris.platform.ordermanagementfacade.returns.OmsReturnFacade;
import de.hybris.platform.ordermanagementfacade.returns.data.ReturnRequestData;
import de.hybris.platform.ordermanagementfacade.returns.data.ReturnEntryData;
import de.hybris.platform.refund.RefundService;
import de.hybris.platform.returns.OrderReturnRecordsHandlerException;
import de.hybris.platform.returns.ReturnService;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Objects;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateIfSingleResult;


/**
 * order management default Return Facade implementation for the {@link de.hybris.platform.ordermanagementfacade.returns.OmsReturnFacade}
 */
public class DefaultOmsReturnFacade extends OmsBaseFacade implements OmsReturnFacade
{
	private Converter<ReturnRequestModel, ReturnRequestData> returnConverter;
	private Converter<ReturnEntryModel, ReturnEntryData> returnEntryConverter;
	private PagedGenericDao<ReturnRequestModel> returnPagedGenericDao;
	private PagedGenericDao<ReturnEntryModel> returnEntryPagedGenericDao;
	private GenericDao<ReturnRequestModel> returnGenericDao;
	private GenericDao<OrderModel> orderGenericDao;
	private EnumerationService enumerationService;
	private ImpersonationService impersonationService;
	private ReturnService returnService;
	private RefundService refundService;
	private OrderService orderService;
	private EventService eventService;

	protected static final Logger LOGGER = LoggerFactory.getLogger(DefaultOmsReturnFacade.class);

	@Override
	public SearchPageData<ReturnRequestData> getReturns(PageableData pageableData)
	{
		SearchPageData<ReturnRequestModel> returnSearchPageData = getReturnPagedGenericDao().find(pageableData);
		return convertSearchPageData(returnSearchPageData, getReturnConverter());
	}


	@Override
	public SearchPageData<ReturnRequestData> getReturnsByStatuses(PageableData pageableData, Set<ReturnStatus> returnStatusSet)
	{
		final Map<String, Object> params = new HashMap<>();
		params.put(ReturnRequestModel.STATUS, returnStatusSet);
		return convertSearchPageData(getReturnPagedGenericDao().find(params, pageableData), getReturnConverter());
	}

	@Override
	public ReturnRequestData getReturnForReturnCode(String code)
	{
		return getReturnConverter().convert(getReturnRequestModelForCode(code));
	}

	@Override
	public List<ReturnStatus> getReturnStatuses()
	{
		return getEnumerationService().getEnumerationValues(ReturnStatus._TYPECODE);
	}

	@Override
	public SearchPageData<ReturnEntryData> getReturnEntriesForReturnCode(String code, PageableData pageableData)
	{
		final ReturnRequestModel returnReq = getReturnRequestModelForCode(code);

		final Map<String, ReturnRequestModel> returnEntryParams = new HashMap<>();
		returnEntryParams.put(ReturnEntryModel.RETURNREQUEST, returnReq);
		return convertSearchPageData(getReturnEntryPagedGenericDao().find(returnEntryParams, pageableData),
				getReturnEntryConverter());
	}

	@Override
	public ReturnRequestData createReturnRequest(final ReturnRequestData returnRequestData)
	{
		validateReturnRequestData(returnRequestData);
		final String orderCode = returnRequestData.getOrder().getCode();
		final Map<String, String> orderParams = new HashMap<>();
		orderParams.put(OrderModel.CODE, orderCode);
		final List<OrderModel> orders = discardOrderSnapshot(getOrderGenericDao().find(orderParams));

		validateIfSingleResult(orders, String.format("Could not find Order with code: [%s].", orderCode),
				String.format("Multiple results found for Order with code: [%s].", orderCode));
		final OrderModel order = orders.get(0);

		final ImpersonationContext context = new ImpersonationContext();
		context.setSite(order.getSite());
		final ReturnRequestModel returnRequestModel = getImpersonationService().executeInContext(context, () -> createReturnRequestInContext(order, returnRequestData));

		return getReturnConverter().convert(returnRequestModel);
	}

	/**
	 * Creates {@link ReturnRequestModel} in the {@link ImpersonationContext}
	 * @param order
	 * 		the {@link OrderModel} for which returnRequest needs to be created
	 * @param returnRequestData
	 * 		the {@link ReturnRequestData} containing required data to create {@link ReturnRequestModel}
	 * @return the newly created {@link ReturnRequestModel}
	 */
	protected ReturnRequestModel createReturnRequestInContext(final OrderModel order, final ReturnRequestData returnRequestData)
	{
		final ReturnRequestModel returnRequest = getReturnService().createReturnRequest(order);
		returnRequest.setRefundDeliveryCost(canRefundDeliveryCost(order.getCode(), returnRequestData.getRefundDeliveryCost()));
		getModelService().save(returnRequest);

		returnRequestData.getEntries().forEach(returnEntryData ->
		{
			final AbstractOrderEntryModel orderEntry = getOrderService()
					.getEntryForNumber(order, returnEntryData.getOrderEntry().getEntryNumber());
			Assert.notNull(orderEntry, "OrderEntry can not be null for the requested RefundEntry");

			final RefundEntryModel refundEntryToBeCreated = getReturnService().createRefund(returnRequest,
					orderEntry, returnEntryData.getNotes(), returnEntryData.getExpectedQuantity(),
					returnEntryData.getAction(), returnEntryData.getRefundReason());
			refundEntryToBeCreated.setAmount(returnEntryData.getRefundAmount() != null ?
					returnEntryData.getRefundAmount() :
					calculateRefundEntryAmount(orderEntry.getBasePrice(), returnEntryData.getExpectedQuantity()));
			getModelService().save(refundEntryToBeCreated);
		});

		try
		{
			getRefundService().apply(returnRequest.getOrder(), returnRequest);
		}
		catch (final OrderReturnRecordsHandlerException e)
		{
			LOGGER.info("Return record already in progress for Order: " + order.getCode());
		}
		catch (final IllegalStateException ise)
		{
			LOGGER.info("Order " + order.getCode() + " Return record already in progress");
		}
		final CreateReturnEvent createReturnEvent = new CreateReturnEvent();
		createReturnEvent.setReturnRequest(returnRequest);
		getEventService().publishEvent(createReturnEvent);

		return returnRequest;
	}

	/**
	 * Validates for null check and mandatory fields in returnRequestData
	 *
	 * @param returnRequestData
	 * 		returnRequest to be validated
	 */
	protected void validateReturnRequestData(final ReturnRequestData returnRequestData)
	{
		Assert.notNull(returnRequestData, "ReturnRequestData can not be null");
		Assert.notNull(returnRequestData.getOrder(), "Order can not be null");
		Assert.isTrue(Objects.nonNull(returnRequestData.getEntries()) && CollectionUtils.isNotEmpty(returnRequestData.getEntries()),
				"Return entries can not be null or empty");
		Assert.isTrue(canRefundDeliveryCost(returnRequestData.getOrder().getCode(),returnRequestData.getRefundDeliveryCost()),
				String.format("Cannot refund delivery cost twice. Delivery cost already refunded for this order: [%s].", returnRequestData.getOrder().getCode()));
		returnRequestData.getEntries().forEach(entry -> validateReturnEntryData(entry));
	}

	/**
	 * Validates for null check and mandatory fields in returnEntryData
	 *
	 * @param returnEntryData
	 * 		returnEntry to be validated
	 */
	protected void validateReturnEntryData(final ReturnEntryData returnEntryData)
	{
		Assert.notNull(returnEntryData.getExpectedQuantity(), "Expected Quantity can not be null");
		Assert.notNull(returnEntryData.getAction(), "ReturnAction cannot be null for ReturnEntry");
		Assert.notNull(returnEntryData.getRefundReason(), "RefundReason cannot be null for ReturnEntry");
		validateOrderEntryForReturnEntry(returnEntryData.getOrderEntry());
	}

	/**
	 * Validates for null check and mandatory fields in returnEntryData
	 *
	 * @param orderEntry
	 * 		orderEntry to be validated
	 */
	protected void validateOrderEntryForReturnEntry(final OrderEntryData orderEntry)
	{
		Assert.notNull(orderEntry, "Order Entry can not be null for RefundEntry");
		Assert.notNull(orderEntry.getEntryNumber(), "Entry number cannot be null for the order Entry");
	}

	/**
	 * Evaluates refundAmount for the {@link RefundEntryModel} to be created
	 *
	 * @param basePrice
	 * 		the basePrice for the product to be refunded
	 * @param expectedQuantity
	 * 		expectedQuantity for the product to be refunded
	 * @return the amount in BigDecimal to be refunded for the {@link RefundEntryModel}
	 */
	protected BigDecimal calculateRefundEntryAmount(final Double basePrice, final Long expectedQuantity)
	{
		return new BigDecimal(basePrice * expectedQuantity);
	}

	/**
	 * Evaluates if deliveryCost should be refunded for the requested {@link ReturnRequestModel} to be created
	 *
	 * @param orderCode
	 * 		the orderCode's code for the requested returnRequest to be created
	 * @param isDeliveryCostRequested
	 * 		is deliveryCost requested in the request
	 * @return the boolean to indicate if deliveryCost should be refunded
	 */
	protected Boolean canRefundDeliveryCost(final String orderCode, final Boolean isDeliveryCostRequested)
	{
		if (isDeliveryCostRequested != null && isDeliveryCostRequested)
		{
			return getReturnService().getReturnRequests(orderCode).stream().noneMatch(
					returnReq -> returnReq.getRefundDeliveryCost().booleanValue()
							&& returnReq.getStatus() != ReturnStatus.CANCELED);
		}
		return Boolean.FALSE;
	}

	/**
	 * Finds {@link ReturnRequestModel} for the given {@value de.hybris.platform.returns.model.ReturnRequestModel#CODE}
	 *
	 * @param code
	 * 		the returnRequest's code
	 * @return the requested return for the given code
	 */
	protected ReturnRequestModel getReturnRequestModelForCode(final String code)
	{

		final Map<String, String> params = new HashMap<>();
		params.put(ReturnRequestModel.CODE, code);

		final List<ReturnRequestModel> resultSet = getReturnGenericDao().find(params);
		validateIfSingleResult(resultSet, String.format("Could not find ReturnRequest with code: [%s].", code),
				String.format("Multiple results found for ReturnRequest with code: [%s].", code));

		return resultSet.get(0);
	}

	@Required
	public void setReturnPagedGenericDao(PagedGenericDao<ReturnRequestModel> returnPagedGenericDao)
	{
		this.returnPagedGenericDao = returnPagedGenericDao;
	}

	protected PagedGenericDao<ReturnRequestModel> getReturnPagedGenericDao()
	{
		return returnPagedGenericDao;
	}

	@Required
	public void setReturnConverter(Converter<ReturnRequestModel, ReturnRequestData> returnConverter)
	{
		this.returnConverter = returnConverter;
	}

	protected Converter<ReturnRequestModel, ReturnRequestData> getReturnConverter()
	{
		return returnConverter;
	}


	@Required
	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	protected GenericDao<ReturnRequestModel> getReturnGenericDao()
	{
		return returnGenericDao;
	}

	@Required
	public void setReturnGenericDao(GenericDao<ReturnRequestModel> returnGenericDao)
	{
		this.returnGenericDao = returnGenericDao;
	}

	protected PagedGenericDao<ReturnEntryModel> getReturnEntryPagedGenericDao()
	{
		return returnEntryPagedGenericDao;
	}

	@Required
	public void setReturnEntryPagedGenericDao(PagedGenericDao<ReturnEntryModel> returnEntryPagedGenericDao)
	{
		this.returnEntryPagedGenericDao = returnEntryPagedGenericDao;
	}

	protected Converter<ReturnEntryModel, ReturnEntryData> getReturnEntryConverter()
	{
		return returnEntryConverter;
	}

	@Required
	public void setReturnEntryConverter(Converter<ReturnEntryModel, ReturnEntryData> returnEntryConverter)
	{
		this.returnEntryConverter = returnEntryConverter;
	}

	protected ReturnService getReturnService()
	{
		return returnService;
	}

	@Required
	public void setReturnService(final ReturnService returnService)
	{
		this.returnService = returnService;
	}

	protected GenericDao<OrderModel> getOrderGenericDao()
	{
		return orderGenericDao;
	}

	@Required
	public void setOrderGenericDao(final GenericDao<OrderModel> orderGenericDao)
	{
		this.orderGenericDao = orderGenericDao;
	}

	protected OrderService getOrderService()
	{
		return orderService;
	}

	@Required
	public void setOrderService(final OrderService orderService)
	{
		this.orderService = orderService;
	}

	protected EventService getEventService()
	{
		return eventService;
	}

	@Required
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}

	protected RefundService getRefundService()
	{
		return refundService;
	}

	@Required
	public void setRefundService(final RefundService refundService)
	{
		this.refundService = refundService;
	}

	protected ImpersonationService getImpersonationService()
	{
		return impersonationService;
	}

	@Required
	public void setImpersonationService(final ImpersonationService impersonationService)
	{
		this.impersonationService = impersonationService;
	}
}
