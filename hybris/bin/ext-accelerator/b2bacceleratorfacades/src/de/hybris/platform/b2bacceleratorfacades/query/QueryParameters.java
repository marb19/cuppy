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
package de.hybris.platform.b2bacceleratorfacades.query;

import java.util.HashMap;
import java.util.Map;


/**
 * Simple Builder utility class which makes the parameters construction easier
 */
public final class QueryParameters
{

	final private Map<String, String> parameters;

	private QueryParameters(final String name, final String value)
	{
		this.parameters = new HashMap<>();
		this.parameters.put(name, value);
	}

	public static QueryParameters with(final String name, final String value)
	{
		return new QueryParameters(name, value);
	}

	public QueryParameters and(final String name, final String value)
	{
		this.parameters.put(name, value);
		return this;
	}

	public Map buildMap()
	{
		return this.parameters;
	}
}
