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
package de.hybris.platform.warehousingfacade.pointofservice.impl;

import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.search.dao.PagedGenericDao;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.ordermanagementfacade.OmsBaseFacade;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.warehousingfacade.pointofservice.WarehousingPointOfServiceFacade;
import de.hybris.platform.warehousingfacade.storelocator.data.WarehouseData;
import org.springframework.beans.factory.annotation.Required;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Default implementation of {@link de.hybris.platform.warehousingfacade.pointofservice.WarehousingPointOfServiceFacade}
 */
public class DefaultWarehousingPointOfServiceFacade extends OmsBaseFacade implements WarehousingPointOfServiceFacade
{
	private Converter<PointOfServiceModel, PointOfServiceData> pointOfServiceConverter;
	private PagedGenericDao<WarehouseModel> warehousesByPointOfServicePagedDao;
	private GenericDao<PointOfServiceModel> pointOfServiceGenericDao;
	private Converter<WarehouseModel, WarehouseData> warehouseConverter;

	@Override
	public PointOfServiceData getPointOfServiceByName(final String name)
	{
		return getPointOfServiceConverter().convert(getPointOfServiceModelByName(name));
	}

	@Override
	public SearchPageData<WarehouseData> getWarehousesForPointOfService(final PageableData pageableData, final String name)
	{
		final PointOfServiceModel pointOfService = getPointOfServiceModelByName(name);

		final Map<String, PointOfServiceModel> params = new HashMap<>();
		params.put(WarehouseModel.POINTSOFSERVICE, pointOfService);

		final SearchPageData<WarehouseModel> warehouseSearchPage = getWarehousesByPointOfServicePagedDao()
				.find(params, pageableData);
		return convertSearchPageData(warehouseSearchPage, getWarehouseConverter());
	}

	/**
	 * Finds {@link PointOfServiceModel} for the given {@link PointOfServiceModel#NAME}
	 *
	 * @param name
	 * 		the pointOfServiceModel's name
	 * @return the requested pointOfService for the given code
	 */
	protected PointOfServiceModel getPointOfServiceModelByName(final String name)
	{
		final Map<String, String> params = new HashMap<>();
		params.put(PointOfServiceModel.NAME, name);

		final List<PointOfServiceModel> resultSet = getPointOfServiceGenericDao().find(params);

		if (resultSet.isEmpty())
		{
			throw new ModelNotFoundException(String.format("Could not find point of service with name: [%s].", name));
		}
		else if (resultSet.size() > 1)
		{
			throw new AmbiguousIdentifierException(
					String.format("Multiple results found for point of service with name: [%s].", name));
		}
		return resultSet.get(0);
	}

	protected Converter<WarehouseModel, WarehouseData> getWarehouseConverter()
	{
		return warehouseConverter;
	}

	@Required
	public void setWarehouseConverter(final Converter<WarehouseModel, WarehouseData> warehouseConverter)
	{
		this.warehouseConverter = warehouseConverter;
	}

	protected Converter<PointOfServiceModel, PointOfServiceData> getPointOfServiceConverter()
	{
		return pointOfServiceConverter;
	}

	@Required
	public void setPointOfServiceConverter(final Converter<PointOfServiceModel, PointOfServiceData> pointOfServiceConverter)
	{
		this.pointOfServiceConverter = pointOfServiceConverter;
	}

	protected GenericDao<PointOfServiceModel> getPointOfServiceGenericDao()
	{
		return pointOfServiceGenericDao;
	}

	@Required
	public void setPointOfServiceGenericDao(final GenericDao<PointOfServiceModel> pointOfServiceGenericDao)
	{
		this.pointOfServiceGenericDao = pointOfServiceGenericDao;
	}

	protected PagedGenericDao<WarehouseModel> getWarehousesByPointOfServicePagedDao()
	{
		return warehousesByPointOfServicePagedDao;
	}

	@Required
	public void setWarehousesByPointOfServicePagedDao(
			final PagedGenericDao<WarehouseModel> warehousesByPointOfServicePagedDao)
	{
		this.warehousesByPointOfServicePagedDao = warehousesByPointOfServicePagedDao;
	}

}
