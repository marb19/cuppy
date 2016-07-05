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
package de.hybris.platform.warehousingfacade.stocklevel;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousingfacade.product.data.StockLevelData;


/**
 * Warehousing facade exposing CRUD operations on {@link de.hybris.platform.ordersplitting.model.WarehouseModel}
 */
public interface WarehousingStockLevelFacade
{
	/**
	 * API to get the stocklevels for the {@link WarehouseModel#CODE}
	 *
 	 * @param code
	 *           the code of warehouse to search
	 * @param pageableData
	 *           pageable object that contains info on the number or pages and how many items in each page in addition
	 *           the sorting info
	 * @return list of stocklevels that complies with above conditions
	 */
	SearchPageData<StockLevelData> getStockLevelsForWarehouseCode(String code, PageableData pageableData);

	/**
	 * API to create a {@link de.hybris.platform.ordersplitting.model.StockLevelModel}
	 *
	 * @param stockLevelData
	 * 		the {@link StockLevelData} to create {@link de.hybris.platform.ordersplitting.model.StockLevelModel} in the system
	 * @return the {@link StockLevelData} converted from the newly created {@link de.hybris.platform.ordersplitting.model.StockLevelModel}
	 */
	StockLevelData createStockLevel(StockLevelData stockLevelData);
}
