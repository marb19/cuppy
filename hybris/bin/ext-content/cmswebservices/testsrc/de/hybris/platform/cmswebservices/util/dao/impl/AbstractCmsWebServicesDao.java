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
package de.hybris.platform.cmswebservices.util.dao.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cmswebservices.util.dao.CmsWebServicesDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import org.springframework.beans.factory.annotation.Required;


public abstract class AbstractCmsWebServicesDao<T> implements CmsWebServicesDao<T>
{

	private FlexibleSearchService flexibleSearchService;

	protected abstract String getQuery();

	@Override
	public T getByUidAndCatalogVersion(String uid, CatalogVersionModel catalogVersion)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(getQuery());
		query.addQueryParameter("uid", uid);
		query.addQueryParameter("catalogVersion", catalogVersion);
		return getFlexibleSearchService().searchUnique(query);
	}

	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	@Required
	public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

}
