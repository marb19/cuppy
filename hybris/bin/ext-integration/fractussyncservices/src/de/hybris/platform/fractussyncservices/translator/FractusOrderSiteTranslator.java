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
 */

package de.hybris.platform.fractussyncservices.translator;

import de.hybris.platform.core.Registry;
import de.hybris.platform.fractussyncservices.adapter.FractusSpecialValueImportAdapter;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.SpecialColumnDescriptor;
import de.hybris.platform.impex.jalo.translators.AbstractSpecialValueTranslator;
import de.hybris.platform.jalo.Item;


public class FractusOrderSiteTranslator extends AbstractSpecialValueTranslator
{

	private FractusSpecialValueImportAdapter fractusOrderSiteAdapter;

	@Override
	public void init(SpecialColumnDescriptor columnDescriptor) throws HeaderValidationException
	{
		fractusOrderSiteAdapter = (FractusSpecialValueImportAdapter) Registry.getApplicationContext().getBean("fractusOrderSiteAdapter");
	}

	@Override
	public void performImport(String cellValue, Item processedItem) throws ImpExException
	{
		getFractusOrderSiteAdapter().performImport(cellValue, processedItem);
	}

	public FractusSpecialValueImportAdapter getFractusOrderSiteAdapter()
	{
		return fractusOrderSiteAdapter;
	}
}
