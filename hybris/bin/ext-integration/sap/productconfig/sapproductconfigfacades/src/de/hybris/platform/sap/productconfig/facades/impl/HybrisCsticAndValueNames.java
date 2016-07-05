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
package de.hybris.platform.sap.productconfig.facades.impl;

import java.util.Collections;
import java.util.Map;


/**
 * Immutable Object
 *
 */
public class HybrisCsticAndValueNames
{

	private final String code;
	private final String name;
	private final String description;
	private final Map<String, String> valueNames;

	private static final String NULL_CODE = "";

	public static final HybrisCsticAndValueNames NULL_OBJ = new HybrisCsticAndValueNames(NULL_CODE, null, null,
			Collections.emptyMap());


	public String getName()
	{
		return name;
	}

	public HybrisCsticAndValueNames(final String code, final String name, final String description,
			final Map<String, String> valueNames)
	{
		super();
		this.code = code;
		this.name = name;
		this.description = description;
		this.valueNames = Collections.unmodifiableMap(valueNames);
	}


	public String getDescription()
	{
		return description;
	}

	public Map<String, String> getValueNames()
	{
		return valueNames;
	}

	@Override
	public int hashCode()
	{
		return code.hashCode();
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final HybrisCsticAndValueNames other = (HybrisCsticAndValueNames) obj;
		if (code == null)
		{
			if (other.code != null)
			{
				return false;
			}
		}
		else if (!code.equals(other.code))
		{
			return false;
		}
		return true;
	}
}
