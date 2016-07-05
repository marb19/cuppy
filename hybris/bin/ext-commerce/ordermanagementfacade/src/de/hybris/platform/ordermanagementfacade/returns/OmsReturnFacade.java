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
package de.hybris.platform.ordermanagementfacade.returns;

import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.ordermanagementfacade.returns.data.ReturnRequestData;
import de.hybris.platform.ordermanagementfacade.returns.data.ReturnEntryData;

import java.util.List;
import java.util.Set;


public interface OmsReturnFacade
{
	/**
	 * API to get all returns in the system
	 *
	 * @param pageableData
	 * 		pageable object that contains info on the number or pages and how many items in each page
	 * 		in addition the sorting info
	 * @return SearchPageData that contains a list of returns
	 */
	SearchPageData<ReturnRequestData> getReturns(PageableData pageableData);

	/**
	 * API to get all returns in the system that have certain status(s)
	 *
	 * @param pageableData
	 * 		pageable object that contains info on the number or pages and how many items in each page
	 * 		in addition the sorting info
	 * @param returnStatusSet
	 * 		set of return status(s) in which we want to get list of returns for.
	 * @return SearchPageData that contains a list of returns that complies with passed return status(es)
	 */
	SearchPageData<ReturnRequestData> getReturnsByStatuses(PageableData pageableData, Set<ReturnStatus> returnStatusSet);

	/**
	 * API to get all return statuses
	 *
	 * @return a list of {@link de.hybris.platform.basecommerce.enums.ReturnStatus}
	 */
	List<ReturnStatus> getReturnStatuses();

	/**
	 * API to get returnEntries for the given {@link de.hybris.platform.returns.model.ReturnRequestModel#CODE}
	 *
	 * @param code
	 * 		the return's code
	 * @param pageableData
	 * 		pageable object that contains info on the number or pages and how many items in each page in addition
	 * 		the sorting info
	 * @return SearchPageData that contains a list of the returnEntries for the given order
	 */
	SearchPageData<ReturnEntryData> getReturnEntriesForReturnCode(String code, PageableData pageableData);

	/**
	 * API to create a {@link de.hybris.platform.returns.model.ReturnRequestModel}
	 *
	 * @param returnRequestData
	 * 		the {@link ReturnRequestData} to create {@link de.hybris.platform.returns.model.ReturnRequestModel}
	 * @return the {@link ReturnRequestData} converted from the newly created {@link de.hybris.platform.returns.model.ReturnRequestModel}
	 */
	ReturnRequestData createReturnRequest(ReturnRequestData returnRequestData);

	/**
	 * API to get a {@link ReturnRequestData} by it's code
	 *
	 * @param code
	 * 		the returnRequest's code
	 * @return the returnRequest
	 */
	ReturnRequestData getReturnForReturnCode(String code);
}
