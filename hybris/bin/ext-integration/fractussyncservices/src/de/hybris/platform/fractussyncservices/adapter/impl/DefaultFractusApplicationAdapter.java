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
package de.hybris.platform.fractussyncservices.adapter.impl;


import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.fractussyncservices.adapter.FractusApplicationAdapter;
import de.hybris.platform.fractussyncservices.adapter.FractusApplicationLookupStrategy;
import de.hybris.platform.fractussyncservices.adapter.FractusNoApplicationLookupStrategyFoundException;
import de.hybris.platform.jalo.Item;


public class DefaultFractusApplicationAdapter implements FractusApplicationAdapter
{

	private final static Logger LOG = Logger.getLogger(DefaultFractusApplicationAdapter.class);

	private List<FractusApplicationLookupStrategy> lookupStrategies;

	@Override
	public String getApplicationId(Item item) throws FractusNoApplicationLookupStrategyFoundException
	{
		String code = item.getComposedType().getCode();

		FractusApplicationLookupStrategy strategy = getStrategy(code);
		if (strategy == null)
		{
			throw new FractusNoApplicationLookupStrategyFoundException("No lookup strategy found for type code " + code);
		}
		else
		{
			return strategy.lookup(item);
		}
	}

	protected FractusApplicationLookupStrategy getStrategy(String typeCode)
	{
		return getLookupStrategies().stream().filter(strategy -> typeCode.equals(strategy.getTypeCode())).findFirst().orElse(null);
	}

	protected List<FractusApplicationLookupStrategy> getLookupStrategies()
	{
		return lookupStrategies;
	}

	@Required
	public void setLookupStrategies(List<FractusApplicationLookupStrategy> lookupStrategies)
	{
		this.lookupStrategies = lookupStrategies;
	}

}
