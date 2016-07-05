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
package de.hybris.platform.ordermanagementwebservices.controllers.returns;

import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.ordermanagementwebservices.controllers.OmsBaseController;
import de.hybris.platform.ordermanagementwebservices.dto.returns.ReturnEntrySearchPageWsDTO;
import de.hybris.platform.ordermanagementwebservices.dto.returns.ReturnRequestWsDTO;
import de.hybris.platform.ordermanagementwebservices.dto.returns.ReturnSearchPageWsDTO;
import de.hybris.platform.ordermanagementwebservices.dto.returns.ReturnStatusListWsDTO;
import de.hybris.platform.ordermanagementfacade.constants.OrdermanagementfacadeConstants;
import de.hybris.platform.ordermanagementfacade.order.data.ReturnStatusDataList;
import de.hybris.platform.ordermanagementfacade.returns.OmsReturnFacade;
import de.hybris.platform.ordermanagementfacade.returns.data.ReturnEntryData;
import de.hybris.platform.ordermanagementfacade.returns.data.ReturnRequestData;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * WebResource exposing {@link de.hybris.platform.ordermanagementfacade.returns.OmsReturnFacade}
 * http://host:port/ordermanagementwebservices/returns
 */
@Controller
@RequestMapping(value = "/returns")
public class OmsReturnsController extends OmsBaseController
{
	@Resource
	private OmsReturnFacade omsReturnFacade;

	/**
	 * Request to get paged returns in the system
	 *
	 * @param fields
	 * 		defaulted to DEFAULT but can be FULL or BASIC
	 * @param currentPage
	 * 		number of the current page
	 * @param pageSize
	 * 		number of items in a page
	 * @param sort
	 * 		sorting the results ascending or descending
	 * @return list of returns
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ReturnSearchPageWsDTO getReturns(@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,
			@RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@RequestParam(required = false, defaultValue = DEFAULT_SORT) final String sort)
	{

		final PageableData pageableData = createPageable(currentPage, pageSize, sort);
		final SearchPageData<ReturnRequestData> returns = omsReturnFacade.getReturns(pageableData);
		return dataMapper.map(returns, ReturnSearchPageWsDTO.class, fields);
	}

	/**
	 * Request to get paged returns with certain return status(s)
	 *
	 * @param returnStatuses
	 * 		a list of valid return statuses separated by ","
	 * @param fields
	 * 		defaulted to DEFAULT but can be FULL or BASIC
	 * @param currentPage
	 * 		number of the current page
	 * @param pageSize
	 * 		number of items in a page
	 * @param sort
	 * 		sorting the results ascending or descending
	 * @return list of returns that complies with conditions above
	 * @throws de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException
	 * 		in case of passing a wrong return status validation exception will be thrown
	 */
	@RequestMapping(value = "status/{returnStatuses}", method = RequestMethod.GET)
	@ResponseBody
	public ReturnSearchPageWsDTO getReturnsByStatus(@PathVariable final String returnStatuses,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,
			@RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@RequestParam(required = false, defaultValue = DEFAULT_SORT) final String sort) throws WebserviceValidationException
	{
		final Set<ReturnStatus> statusSet = extractReturnStatuses(returnStatuses);
		final PageableData pageableData = createPageable(currentPage, pageSize, sort);
		final SearchPageData<ReturnRequestData> returns = omsReturnFacade.getReturnsByStatuses(pageableData, statusSet);
		return dataMapper.map(returns, ReturnSearchPageWsDTO.class, fields);
	}

	/**
	 * Request to get ReturnRequest by its code
	 *
	 * @param code
	 * 		the code of the requested return
	 * @param fields
	 * 		defaulted to DEFAULT but can be FULL or BASIC
	 * @return the requested returnRequest that complies with conditions above
	 */
	@RequestMapping(value = "{code}", method = RequestMethod.GET)
	@ResponseBody
	public ReturnRequestWsDTO getReturnForReturnCode(@PathVariable final String code,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final ReturnRequestData returns = omsReturnFacade.getReturnForReturnCode(code);
		return dataMapper.map(returns, ReturnRequestWsDTO.class, fields);
	}


	/**
	 * Request to get all {@link ReturnStatus} in the system
	 *
	 * @return list of return statuses
	 */
	@RequestMapping(value = "/statuses", method = RequestMethod.GET)
	@ResponseBody
	public ReturnStatusListWsDTO getReturnStatuses()
	{
		final List<ReturnStatus> returnStatuses = omsReturnFacade.getReturnStatuses();
		final ReturnStatusDataList returnStatusList = new ReturnStatusDataList();
		returnStatusList.setStatuses(returnStatuses);
		return dataMapper.map(returnStatusList, ReturnStatusListWsDTO.class);
	}

	/**
	 * Request to get returnEntries for the given {@link de.hybris.platform.returns.model.ReturnRequestModel#CODE}
	 *
	 * @param code
	 * 		return's code for the requested return entries
	 * @param fields
	 * 		defaulted to DEFAULT but can be FULL or BASIC
	 * @param currentPage
	 * 		number of the current page
	 * @param pageSize
	 * 		number of items in a page
	 * @param sort
	 * 		sorting the results ascending or descending
	 * @return the list of returnEntries fulfilling the above conditions
	 */
	@RequestMapping(value = "/{code}/entries", method = RequestMethod.GET)
	@ResponseBody
	public ReturnEntrySearchPageWsDTO getReturnEntriesForOrderCode(@PathVariable final String code,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,
			@RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@RequestParam(required = false, defaultValue = DEFAULT_SORT) final String sort)
	{
		final PageableData pageableData = createPageable(currentPage, pageSize, sort);
		final SearchPageData<ReturnEntryData> returnEntrySearchPageData = omsReturnFacade.getReturnEntriesForReturnCode(code,
				pageableData);
		return dataMapper.map(returnEntrySearchPageData, ReturnEntrySearchPageWsDTO.class, fields);
	}

	protected Set<ReturnStatus> extractReturnStatuses(final String statuses)
	{
		final String statusesStrings[] = statuses.split(OrdermanagementfacadeConstants.OPTIONS_SEPARATOR);

		final Set<ReturnStatus> statusesEnum = new HashSet<>();
		try
		{
			for (final String status : statusesStrings)
			{
				statusesEnum.add(ReturnStatus.valueOf(status));
			}
		}
		catch (final IllegalArgumentException e)
		{
			throw new WebserviceValidationException(e.getMessage());
		}
		return statusesEnum;
	}

	/**
	 * Request to create return in the system
	 *
	 * @param fields
	 * 		defaulted to DEFAULT but can be FULL or BASIC
	 * @param returnRequestWsDTO
	 * 		object representing {@link ReturnRequestWsDTO}
	 * @return created return
	 */
	@RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
			consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public ReturnRequestWsDTO createReturnRequest(@RequestBody final ReturnRequestWsDTO returnRequestWsDTO,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final ReturnRequestData returnRequestData = dataMapper.map(returnRequestWsDTO, ReturnRequestData.class);
		final ReturnRequestData createdReturnRequestData = omsReturnFacade.createReturnRequest(returnRequestData);

		return dataMapper.map(createdReturnRequestData, ReturnRequestWsDTO.class, fields);
	}

}
