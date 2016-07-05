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
import de.hybris.platform.fractussyncservices.adapter.FractusValueImportAdapter;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.impex.jalo.translators.AbstractValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;


public class FractusOrderEntryProductTranslator extends AbstractValueTranslator
{

	private FractusValueImportAdapter fractusOrderEntryProductAdapter;

	@Override
	public String exportValue(Object o) throws JaloInvalidParameterException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Object importValue(String cellValue, Item item)
	{
		return getFractusOrderEntryProductAdapter().performImport(cellValue);
	}

	@Override
	public void init(StandardColumnDescriptor descriptor)
	{
		super.init(descriptor);
		fractusOrderEntryProductAdapter = (FractusValueImportAdapter) Registry.getApplicationContext().getBean(
				"fractusOrderEntryProductAdapter");
	}

	public FractusValueImportAdapter getFractusOrderEntryProductAdapter()
	{
		return fractusOrderEntryProductAdapter;
	}
}
