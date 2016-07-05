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
package de.hybris.platform.ordermanagementwebservices.controllers;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.webservicescommons.mapping.DataMapper;

import javax.annotation.Resource;


/**
 * Abstract class containing basic controller functionality
 */
public abstract class OmsBaseController
{
	public static final String DEFAULT_FIELD_SET = "DEFAULT";
	public static final String DEFAULT_CURRENT_PAGE = "0";
	public static final String DEFAULT_PAGE_SIZE = "10";
	public static final String DEFAULT_SORT = "asc";

	@Resource(name = "dataMapper")
	protected DataMapper dataMapper;

	/**
	 * Creates a pageableData with provided page, pageSize and sort
	 *
	 * @param page
	 *           current page number
	 * @param pageSize
	 *           number of items in a page
	 * @param sort
	 *           sorting the results ascending or descending
	 * @return a pageableData
	 */
	protected PageableData createPageable(final int page, final int pageSize, final String sort)
	{
		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(page);
		pageableData.setPageSize(pageSize);
		pageableData.setSort(sort);
		return pageableData;
	}
}
