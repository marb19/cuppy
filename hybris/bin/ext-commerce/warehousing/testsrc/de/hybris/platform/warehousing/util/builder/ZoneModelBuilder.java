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

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.deliveryzone.model.ZoneModel;

import com.google.common.collect.Sets;


public class ZoneModelBuilder
{
	private final ZoneModel model;

	private ZoneModelBuilder()
	{
		model = new ZoneModel();
	}

	private ZoneModel getModel()
	{
		return this.model;
	}

	public static ZoneModelBuilder aModel()
	{
		return new ZoneModelBuilder();
	}

	public ZoneModel build()
	{
		return getModel();
	}

	public ZoneModelBuilder withCode(final String code)
	{
		getModel().setCode(code);
		return this;
	}

	public ZoneModelBuilder withCountries(final CountryModel... countries)
	{
		getModel().setCountries(Sets.newHashSet(countries));
		return this;
	}

}
