/*
 *  
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
 */
package de.hybris.platform.warehousing.util.dao.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.warehousing.util.dao.WarehousingDao;

import org.springframework.beans.factory.annotation.Required;


public abstract class AbstractWarehousingDao<T> implements WarehousingDao<T>
{
	private FlexibleSearchService flexibleSearchService;

	protected abstract String getQuery();

	@Override
	public T getByCode(final String code)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(getQuery());
		query.addQueryParameter("code", code);
		return flexibleSearchService.searchUnique(query);
	}

	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

}
