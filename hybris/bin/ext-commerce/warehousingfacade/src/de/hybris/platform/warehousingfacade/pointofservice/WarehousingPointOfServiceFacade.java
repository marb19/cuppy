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
package de.hybris.platform.warehousingfacade.pointofservice;

import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.warehousingfacade.storelocator.data.WarehouseData;





/**
 * Warehousing facade exposing CRUD operations on {@link de.hybris.platform.storelocator.model.PointOfServiceModel}
 */
public interface WarehousingPointOfServiceFacade
{
	/**
	 * API to get a point of service by name
	 *
	 * @param name the point of service's name
	 * @return the point of service
	 */
	PointOfServiceData getPointOfServiceByName(String name);

	/**
	 * API to get all {@link de.hybris.platform.ordersplitting.model.WarehouseModel} in the system, for the given
	 * {@link de.hybris.platform.storelocator.model.PointOfServiceModel#NAME}
	 *
	 * @param pageableData pageable object that contains info on the number or pages and how many items in each page in addition
	 *                     the sorting info
	 * @param name         the name of the PointOfService for which warehouses are being retrieved
	 * @return the list of warehouses for the given PointOfService
	 */
	SearchPageData<WarehouseData> getWarehousesForPointOfService(PageableData pageableData, String name);
}
