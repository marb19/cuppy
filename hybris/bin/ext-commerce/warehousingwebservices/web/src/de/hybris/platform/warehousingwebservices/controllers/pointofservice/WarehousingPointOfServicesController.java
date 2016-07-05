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
package de.hybris.platform.warehousingwebservices.controllers.pointofservice;


import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commercewebservicescommons.dto.store.PointOfServiceWsDTO;
import de.hybris.platform.warehousingwebservices.controllers.WarehousingBaseController;
import de.hybris.platform.warehousingwebservices.dto.store.WarehouseSearchPageWsDto;
import de.hybris.platform.warehousingfacade.pointofservice.WarehousingPointOfServiceFacade;
import de.hybris.platform.warehousingfacade.storelocator.data.WarehouseData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


/**
 * WebResource exposing {@link de.hybris.platform.warehousingfacade.pointofservice.WarehousingPointOfServiceFacade}
 * http://host:port/warehousingwebservices/pointofservices
 */
@Controller
@RequestMapping(value = "/pointofservices")
public class WarehousingPointOfServicesController extends WarehousingBaseController
{
	@Resource
	private WarehousingPointOfServiceFacade warehousingPointOfServiceFacade;

	/**
	 * Request to get a point of service by name
	 *
	 * @param name
	 * 		the name of the requested point of service
	 * @param fields
	 * 		defaulted to DEFAULT but can be FULL or BASIC
	 * @return the point of service
	 */
	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	@ResponseBody
	public PointOfServiceWsDTO getPointOfServiceByName(@PathVariable final String name,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final PointOfServiceData pos = warehousingPointOfServiceFacade.getPointOfServiceByName(name);
		return dataMapper.map(pos, PointOfServiceWsDTO.class, fields);
	}

	/**
	 * Request to get all warehouses for the given {@link de.hybris.platform.storelocator.model.PointOfServiceModel} in
	 * the system
	 *
	 * @param pointOfService
	 * 		the point of service for which to get the warehouses
	 * @param fields
	 * 		defaulted to DEFAULT but can be FULL or BASIC
	 * @param currentPage
	 * 		number of the current page
	 * @param pageSize
	 * 		number of items in a page
	 * @param sort
	 * 		sorting the results ascending or descending
	 * @return list of warehouses
	 */
	@RequestMapping(value = "/{pointOfService}/warehouses", method = RequestMethod.GET)
	@ResponseBody
	public WarehouseSearchPageWsDto getWarehousesForPointOfService(@PathVariable final String pointOfService,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,
			@RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@RequestParam(required = false, defaultValue = DEFAULT_SORT) final String sort)
	{
		final PageableData pageableData = createPageable(currentPage, pageSize, sort);
		final SearchPageData<WarehouseData> warehouses = warehousingPointOfServiceFacade
				.getWarehousesForPointOfService(pageableData,
						pointOfService);

		return dataMapper.map(warehouses, WarehouseSearchPageWsDto.class, fields);
	}

}
