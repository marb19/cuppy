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
package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.catalog.daos.CatalogDao;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.warehousing.util.builder.CatalogModelBuilder;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Required;


public class Catalogs extends AbstractItems<CatalogModel>
{
	public static final String ID_PRIMARY = "primary";

	private CatalogDao catalogDao;

	public CatalogModel Primary()
	{
		return getOrSaveAndReturn(() -> getCatalogDao().findCatalogById(ID_PRIMARY), 
				() -> CatalogModelBuilder.aModel() 
						.withDefaultCatalog(Boolean.TRUE) 
						.withId(ID_PRIMARY) 
						.withName(ID_PRIMARY, Locale.ENGLISH) 
						.build());
	}

	public CatalogDao getCatalogDao()
	{
		return catalogDao;
	}

	@Required
	public void setCatalogDao(final CatalogDao catalogDao)
	{
		this.catalogDao = catalogDao;
	}
}
