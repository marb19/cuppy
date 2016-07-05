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
package de.hybris.platform.b2b.util;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;


public class B2BCommerceTestUtils
{
	private static final int PAGE_SIZE = 5;

	public static PageableData createPageableData(final int pageNumber, final int pageSize, final String sortCode)
	{
		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(pageNumber);
		pageableData.setSort(sortCode);
		pageableData.setPageSize(pageSize);
		return pageableData;
	}

	public static PageableData createPageableData()
	{
		return createPageableData(0, PAGE_SIZE, null);
	}
}
