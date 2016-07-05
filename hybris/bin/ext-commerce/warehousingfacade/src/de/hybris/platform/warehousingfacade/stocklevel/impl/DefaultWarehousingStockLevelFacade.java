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
package de.hybris.platform.warehousingfacade.stocklevel.impl;

import de.hybris.platform.commerceservices.search.dao.PagedGenericDao;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.ordermanagementfacade.OmsBaseFacade;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.warehousing.atp.services.WarehouseStockService;
import de.hybris.platform.warehousingfacade.product.data.StockLevelData;
import de.hybris.platform.warehousingfacade.stocklevel.WarehousingStockLevelFacade;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;


/**
 * Default implementation of {@link WarehousingStockLevelFacade}
 */
public class DefaultWarehousingStockLevelFacade extends OmsBaseFacade implements WarehousingStockLevelFacade
{
	private PagedGenericDao<StockLevelModel> stockLevelPagedGenericDao;
	private Converter<StockLevelModel, StockLevelData> stockLevelConverter;
	private WarehouseService warehouseService;
	private WarehouseStockService warehouseStockService;

	@Override
	public SearchPageData<StockLevelData> getStockLevelsForWarehouseCode(final String code,
			final PageableData pageableData)
	{
		final Map<String, WarehouseModel> warehouseParams = new HashMap<>();
		warehouseParams.put(StockLevelModel.WAREHOUSE, getWarehouseService().getWarehouseForCode(code));

		SearchPageData<StockLevelModel> stockLevelModelSearchPageData = getStockLevelPagedGenericDao().find(warehouseParams,pageableData);
		return convertSearchPageData(stockLevelModelSearchPageData,getStockLevelConverter());
	}

	@Override
	public StockLevelData createStockLevel(final StockLevelData stockLevelData)
	{
		validateStockLevelData(stockLevelData);
		final String warehouseCode = stockLevelData.getWarehouse().getCode();
		final WarehouseModel warehouse = getWarehouseService().getWarehouseForCode(warehouseCode);
		ServicesUtil.validateParameterNotNull(warehouse, String.format("No Warehouse found for the code: [%s]", warehouseCode));

		final StockLevelModel stockLevel = getWarehouseStockService()
				.createStockLevel(stockLevelData.getProductCode(), warehouse, stockLevelData.getInitialQuantityOnHand(), stockLevelData.getInStockStatus(),
						stockLevelData.getReleaseDate(),stockLevelData.getBin());
		return getStockLevelConverter().convert(stockLevel);

	}

	/**
	 * Validates for null check and mandatory fields in stockLevelData
	 *
	 * @param stockLevelData
	 * 		stockLevelData to be validated
	 */
	protected void validateStockLevelData(final StockLevelData stockLevelData)
	{
		Assert.notNull(stockLevelData.getProductCode(), "ProductCode cannot be null for StockLevel");
		Assert.notNull(stockLevelData.getWarehouse(), "Warehouse cannot be null for StockLevel");
		Assert.isTrue(stockLevelData.getInitialQuantityOnHand() != null && stockLevelData.getInitialQuantityOnHand() > 0,
				"initialQuantityOnHand cannot be null or negative for StockLevel");
	}

	protected PagedGenericDao<StockLevelModel> getStockLevelPagedGenericDao()
	{
		return stockLevelPagedGenericDao;
	}

	@Required
	public void setStockLevelPagedGenericDao(final PagedGenericDao<StockLevelModel> stockLevelPagedGenericDao)
	{
		this.stockLevelPagedGenericDao = stockLevelPagedGenericDao;
	}

	protected Converter<StockLevelModel, StockLevelData> getStockLevelConverter()
	{
		return stockLevelConverter;
	}

	@Required
	public void setStockLevelConverter(final Converter<StockLevelModel, StockLevelData> stockLevelConverter)
	{
		this.stockLevelConverter = stockLevelConverter;
	}

	protected WarehouseService getWarehouseService()
	{
		return warehouseService;
	}

	@Required
	public void setWarehouseService(final WarehouseService warehouseService)
	{
		this.warehouseService = warehouseService;
	}

	protected WarehouseStockService getWarehouseStockService()
	{
		return warehouseStockService;
	}

	@Required
	public void setWarehouseStockService(final WarehouseStockService warehouseStockService)
	{
		this.warehouseStockService = warehouseStockService;
	}
}
