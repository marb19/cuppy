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
package de.hybris.platform.ycommercewebservices.v2.helper;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.webservicescommons.mapping.DataMapper;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;


@Component
public abstract class AbstractHelper
{
	@Resource(name = "dataMapper")
	private DataMapper dataMapper;

	protected PageableData createPageableData(final int currentPage, final int pageSize, final String sort)
	{
		final PageableData pageable = new PageableData();
		pageable.setCurrentPage(currentPage);
		pageable.setPageSize(pageSize);
		pageable.setSort(sort);
		return pageable;
	}

	protected DataMapper getDataMapper()
	{
		return dataMapper;
	}

	protected void setDataMapper(final DataMapper dataMapper)
	{
		this.dataMapper = dataMapper;
	}
}
