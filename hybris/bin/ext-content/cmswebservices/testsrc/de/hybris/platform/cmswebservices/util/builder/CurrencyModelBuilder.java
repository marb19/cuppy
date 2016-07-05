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
package de.hybris.platform.cmswebservices.util.builder;

import de.hybris.platform.core.model.c2l.CurrencyModel;


public class CurrencyModelBuilder
{

	private final CurrencyModel model;

	private CurrencyModelBuilder()
	{
		model = new CurrencyModel();
	}

	private CurrencyModelBuilder(CurrencyModel model)
	{
		this.model = model;
	}

	private CurrencyModel getModel()
	{
		return this.model;
	}

	public static CurrencyModelBuilder aModel()
	{
		return new CurrencyModelBuilder();
	}

	public static CurrencyModelBuilder fromModel(CurrencyModel model)
	{
		return new CurrencyModelBuilder(model);
	}

	public CurrencyModel build()
	{
		return this.getModel();
	}

	public CurrencyModelBuilder withActive(Boolean active)
	{
		getModel().setActive(active);
		return this;
	}

	public CurrencyModelBuilder withIsocode(String isocode)
	{
		getModel().setIsocode(isocode);
		return this;
	}

	public CurrencyModelBuilder withSymbol(String symbol)
	{
		getModel().setSymbol(symbol);
		return this;
	}
}