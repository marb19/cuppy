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
package de.hybris.platform.warehousingwebservices.controllers.stocklevel;


import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.warehousingwebservices.controllers.WarehousingBaseController;
import de.hybris.platform.warehousingwebservices.dto.product.StockLevelSearchPageWsDto;
import de.hybris.platform.warehousingwebservices.dto.product.StockLevelWsDto;
import de.hybris.platform.warehousingfacade.product.data.StockLevelData;
import de.hybris.platform.warehousingfacade.stocklevel.WarehousingStockLevelFacade;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.Resource;


/**
 * WebResource exposing {@link WarehousingStockLevelFacade}
 * http://host:port/warehousingwebservices/stocklevels
 */
@Controller
@RequestMapping(value = "/stocklevels")
public class WarehousingStockLevelsController extends WarehousingBaseController
{
	@Resource
	private WarehousingStockLevelFacade warehousingStockLevelFacade;
	@Resource(name = "warehousingStockLevelValidator")
	private Validator warehousingStockLevelValidator;

	/**
	 * Request to get a {@link de.hybris.platform.ordersplitting.model.StockLevelModel} for its {@value de.hybris.platform.ordersplitting.model.WarehouseModel#CODE}
	 *
	 * @param code
	 * 		the code of the requested {@link de.hybris.platform.ordersplitting.model.WarehouseModel}
	 * @param fields
	 * 		defaulted to DEFAULT but can be FULL or BASIC
	 * @param currentPage
	 * 		number of the current page
	 * @param pageSize
	 * 		number of items in a page
	 * @param sort
	 * 		sorting the results ascending or descending
	 * @return the list of stocklevels
	 */
	@RequestMapping(value = "/warehouses/{code}", method = RequestMethod.GET)
	@ResponseBody
	public StockLevelSearchPageWsDto getStockLevelsForWarehouseCode(@PathVariable final String code,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,
			@RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@RequestParam(required = false, defaultValue = DEFAULT_SORT) final String sort)
	{
		final PageableData pageableData = createPageable(currentPage, pageSize, sort);
		final SearchPageData<StockLevelData> stockLevelSearchPageData = warehousingStockLevelFacade.getStockLevelsForWarehouseCode(code,pageableData);
		return dataMapper.map(stockLevelSearchPageData, StockLevelSearchPageWsDto.class, fields);
	}

	/**
	 * Request to create a {@link de.hybris.platform.ordersplitting.model.StockLevelModel} in the system
	 *
	 * @param fields
	 * 		defaulted to DEFAULT but can be FULL or BASIC
	 * @param stockLevelWsDto
	 * 		object representing {@link StockLevelWsDto}
	 * @return created stockLevel
	 */
	@RequestMapping(method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public StockLevelWsDto createStockLevel(@RequestBody final StockLevelWsDto stockLevelWsDto,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws WebserviceValidationException
	{
		validate(stockLevelWsDto, "stockLevelWsDto", warehousingStockLevelValidator);
		final StockLevelData stockLevelData = dataMapper.map(stockLevelWsDto,StockLevelData.class);
		final StockLevelData createdStockLevelData = warehousingStockLevelFacade.createStockLevel(stockLevelData);

		return dataMapper.map(createdStockLevelData,StockLevelWsDto.class,fields);
	}
}
