 /*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 */
package de.hybris.platform.chinesepspalipayservices.dao.impl;

import de.hybris.platform.chinesepspalipayservices.dao.AlipayOrderDao;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Required;


public class DefaultAlipayOrderDao implements AlipayOrderDao
{

	private static final String FIND_ORDERS_BY_CODE_STORE_QUERY = "SELECT {" + OrderModel.PK + "}, {" + OrderModel.CREATIONTIME
			+ "}, {" + OrderModel.CODE + "} FROM {" + OrderModel._TYPECODE + "} WHERE {" + OrderModel.CODE + "} = ?code AND {"
			+ OrderModel.VERSIONID + "} IS NULL";

	private static final String ORDER_CODE = "code";
	private FlexibleSearchService searchService;

	@Override
	public OrderModel findOrderByCode(final String code)
	{
		Assert.assertNotNull(code);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_ORDERS_BY_CODE_STORE_QUERY);
		query.addQueryParameter(ORDER_CODE, code);
		return getSearchService().searchUnique(query);
	}


	@Required
	public void setSearchService(final FlexibleSearchService searchService)
	{
		this.searchService = searchService;
	}

	protected FlexibleSearchService getSearchService()
	{
		return searchService;
	}
}

