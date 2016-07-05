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
package de.hybris.platform.warehousingfacade.order.impl;


import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.commercefacades.order.data.ConsignmentData;
import de.hybris.platform.commercefacades.order.data.ConsignmentEntryData;
import de.hybris.platform.commerceservices.model.PickUpDeliveryModeModel;
import de.hybris.platform.commerceservices.search.dao.PagedGenericDao;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.ordermanagementfacade.OmsBaseFacade;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;
import de.hybris.platform.warehousing.shipping.service.WarehousingShippingService;
import de.hybris.platform.warehousingfacade.order.WarehousingConsignmentFacade;
import de.hybris.platform.ordermanagementfacade.search.dao.impl.SearchByStatusPagedGenericDao;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateIfSingleResult;


/**
 * Default implementation of {@link WarehousingConsignmentFacade}.
 */
public class DefaultWarehousingConsignmentFacade extends OmsBaseFacade implements WarehousingConsignmentFacade
{
	private Converter<ConsignmentModel, ConsignmentData> consignmentConverter;
	private Converter<ConsignmentEntryModel, ConsignmentEntryData> consignmentEntryConverter;
	private PagedGenericDao<ConsignmentModel> consignmentPagedGenericDao;
	private GenericDao<ConsignmentModel> consignmentGenericDao;
	private PagedGenericDao<ConsignmentModel> consignmentEntryPagedDao;
	private SearchByStatusPagedGenericDao<ConsignmentModel> consignmentSearchByStatusPagedDao;
	private EnumerationService enumerationService;
	private WarehousingShippingService warehousingShippingService;

	@Override
	public SearchPageData<ConsignmentData> getConsignments(final PageableData pageableData)
	{
		return convertSearchPageData(getConsignmentPagedGenericDao().find(pageableData), getConsignmentConverter());
	}

	@Override
	public SearchPageData<ConsignmentData> getConsignmentsByStatuses(final PageableData pageableData,
			final Set<ConsignmentStatus> consignmentStatusSet)
	{
		final Map<String, Object> params = new HashMap<>();
		params.put(ConsignmentModel.STATUS, consignmentStatusSet);
		return convertSearchPageData(getConsignmentSearchByStatusPagedDao().find(params, pageableData), getConsignmentConverter());
	}

	@Override
	public SearchPageData<ConsignmentEntryData> getConsignmentEntriesForConsignmentCode(final String code,
			final PageableData pageableData)
	{
		final ConsignmentModel consignment = getConsignmentModelForCode(code);

		final Map<String, ConsignmentModel> consignmentEntryParams = new HashMap<>();
		consignmentEntryParams.put(ConsignmentEntryModel.CONSIGNMENT, consignment);
		return convertSearchPageData(getConsignmentEntryPagedDao().find(consignmentEntryParams, pageableData),
				getConsignmentEntryConverter());
	}

	@Override
	public ConsignmentData getConsignmentForCode(final String code)
	{
		return getConsignmentConverter().convert(getConsignmentModelForCode(code));
	}

	@Override
	public List<ConsignmentStatus> getConsignmentStatuses()
	{
		return getEnumerationService().getEnumerationValues(ConsignmentStatus._TYPECODE);
	}

	@Override
	public void confirmShipConsignment(final String code)
	{
		Assert.notNull(code, "Code cannot be null for the consignment");
		Assert.isTrue(isConsignmentConfirmable(code), String.format("Confirmation is not possible for Consignment with the code: [%s].", code));

		final ConsignmentModel consignmentModel = getConsignmentModelForCode(code);
		Assert.isTrue(!(consignmentModel.getDeliveryMode() instanceof PickUpDeliveryModeModel),
				String.format("Shipping is not allowed for Pick up Consignment with the code: [%s].", consignmentModel.getCode()));

		getWarehousingShippingService().confirmShipConsignment(consignmentModel);
	}

	@Override
	public void confirmPickupConsignment(final String code)
	{
		Assert.notNull(code, "Code cannot be null for the consignment");
		Assert.isTrue(isConsignmentConfirmable(code), String.format("Confirmation is not possible for Consignment with the code: [%s].", code));

		final ConsignmentModel consignmentModel = getConsignmentModelForCode(code);
		Assert.isTrue((consignmentModel.getDeliveryMode() instanceof PickUpDeliveryModeModel),
				String.format("Pick up is not allowed for Shipping Consignment with the code: [%s].", consignmentModel.getCode()));

		getWarehousingShippingService().confirmPickupConsignment(consignmentModel);
	}

	@Override
	public boolean isConsignmentConfirmable(final String code)
	{
		final ConsignmentModel consignmentModel = getConsignmentModelForCode(code);
		return getWarehousingShippingService().isConsignmentConfirmable(consignmentModel);
	}

	/**
	 * Finds {@link ConsignmentModel} for the given {@link ConsignmentModel#CODE}
	 *
	 * @param code
	 * 		the consignment's code
	 * @return the requested consignment for the given code
	 */
	protected ConsignmentModel getConsignmentModelForCode(final String code)
	{
		final Map<String, Object> params = new HashMap<>();
		params.put(ConsignmentModel.CODE, code);

		List<ConsignmentModel> consignments = getConsignmentGenericDao().find(params);
		validateIfSingleResult(consignments, String.format("Could not find Consignment with code: [%s].", code),
				String.format("Multiple results found for Consignment with code: [%s].", code));

		return consignments.get(0);
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

	protected GenericDao<ConsignmentModel> getConsignmentGenericDao()
	{
		return consignmentGenericDao;
	}

	@Required
	public void setConsignmentGenericDao(final GenericDao<ConsignmentModel> consignmentGenericDao)
	{
		this.consignmentGenericDao = consignmentGenericDao;
	}

	@Required
	public void setConsignmentPagedGenericDao(final PagedGenericDao<ConsignmentModel> consignmentPagedGenericDao)
	{
		this.consignmentPagedGenericDao = consignmentPagedGenericDao;
	}

	protected PagedGenericDao<ConsignmentModel> getConsignmentPagedGenericDao()
	{
		return consignmentPagedGenericDao;
	}

	@Required
	public void setConsignmentConverter(final Converter<ConsignmentModel, ConsignmentData> consignmentConverter)
	{
		this.consignmentConverter = consignmentConverter;
	}

	protected Converter<ConsignmentModel, ConsignmentData> getConsignmentConverter()
	{
		return consignmentConverter;
	}

	@Required
	public void setConsignmentSearchByStatusPagedDao(
			final SearchByStatusPagedGenericDao<ConsignmentModel> consignmentSearchByStatusPagedDao)
	{
		this.consignmentSearchByStatusPagedDao = consignmentSearchByStatusPagedDao;
	}

	protected SearchByStatusPagedGenericDao<ConsignmentModel> getConsignmentSearchByStatusPagedDao()
	{
		return consignmentSearchByStatusPagedDao;
	}

	@Required
	public void setConsignmentEntryPagedDao(final PagedGenericDao consignmentEntryPagedDao)
	{
		this.consignmentEntryPagedDao = consignmentEntryPagedDao;
	}

	protected PagedGenericDao getConsignmentEntryPagedDao()
	{
		return consignmentEntryPagedDao;
	}

	@Required
	public void setConsignmentEntryConverter(final Converter<ConsignmentEntryModel, ConsignmentEntryData> consignmentEntryConverter)
	{
		this.consignmentEntryConverter = consignmentEntryConverter;
	}

	protected Converter<ConsignmentEntryModel, ConsignmentEntryData> getConsignmentEntryConverter()
	{
		return consignmentEntryConverter;
	}

	@Required
	public void setWarehousingShippingService( final WarehousingShippingService warehousingShippingService)
	{
		this.warehousingShippingService = warehousingShippingService;
	}

	protected WarehousingShippingService getWarehousingShippingService()
	{
		return warehousingShippingService;
	}
}
