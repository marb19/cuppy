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
package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;


public class CatalogVersionModelBuilder
{
	private final CatalogVersionModel model;

	private CatalogVersionModelBuilder()
	{
		model = new CatalogVersionModel();
	}

	private CatalogVersionModel getModel()
	{
		return this.model;
	}

	public static CatalogVersionModelBuilder aModel()
	{
		return new CatalogVersionModelBuilder();
	}

	public CatalogVersionModel build()
	{
		return getModel();
	}

	public CatalogVersionModelBuilder withCatalog(final CatalogModel catalog)
	{
		getModel().setCatalog(catalog);
		return this;
	}

	public CatalogVersionModelBuilder withVersion(final String version)
	{
		getModel().setVersion(version);
		return this;
	}

	public CatalogVersionModelBuilder withActive(final Boolean active)
	{
		getModel().setActive(active);
		return this;
	}

	public CatalogVersionModelBuilder withDefaultCurrency(final CurrencyModel defaultCurrency)
	{
		getModel().setDefaultCurrency(defaultCurrency);
		return this;
	}

}
