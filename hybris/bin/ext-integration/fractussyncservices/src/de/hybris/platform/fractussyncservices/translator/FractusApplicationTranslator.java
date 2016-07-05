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
package de.hybris.platform.fractussyncservices.translator;

import de.hybris.platform.core.Registry;
import de.hybris.platform.fractussyncservices.adapter.FractusApplicationAdapter;
import de.hybris.platform.fractussyncservices.adapter.FractusNoApplicationLookupStrategyFoundException;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.SpecialColumnDescriptor;
import de.hybris.platform.impex.jalo.translators.AbstractSpecialValueTranslator;
import de.hybris.platform.jalo.Item;


public class FractusApplicationTranslator extends AbstractSpecialValueTranslator
{

	private FractusApplicationAdapter applicationAdapter;

	@Override
	public void init(SpecialColumnDescriptor specialColumnDescriptor) throws HeaderValidationException
	{
		applicationAdapter = (FractusApplicationAdapter) Registry.getApplicationContext().getBean("fractusApplicationAdapter");
	}

	@Override
	public String performExport(final Item item) throws ImpExException
	{
		try
		{
			return getApplicationAdapter().getApplicationId(item);
		}
		catch (FractusNoApplicationLookupStrategyFoundException e)
		{
			throw new ImpExException(e);
		}
	}

	public FractusApplicationAdapter getApplicationAdapter()
	{
		return applicationAdapter;
	}

	public void setApplicationAdapter(FractusApplicationAdapter applicationAdapter)
	{
		this.applicationAdapter = applicationAdapter;
	}
}
