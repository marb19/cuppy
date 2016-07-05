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
package de.hybris.platform.cmswebservices.util.models;

import de.hybris.platform.cmswebservices.util.builder.CurrencyModelBuilder;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.i18n.daos.CurrencyDao;


public class CurrencyModelMother extends AbstractModelMother<CurrencyModel>
{

	public static final String CODE_USD = "USD";
	private static final String SYMBOL_USD = "$";

	private CurrencyDao currencyDao;

	protected CurrencyModel defaultCurrency()
	{
		return CurrencyModelBuilder.aModel().withActive(Boolean.TRUE).build();
	}

	public CurrencyModel createUSDollar()
	{
		return getFromCollectionOrSaveAndReturn(() -> getCurrencyDao().findCurrenciesByCode(CODE_USD),
				() -> CurrencyModelBuilder.fromModel(defaultCurrency()).withIsocode(CODE_USD).withSymbol(SYMBOL_USD).build());
	}

	public CurrencyDao getCurrencyDao()
	{
		return currencyDao;
	}

	public void setCurrencyDao(final CurrencyDao currencyDao)
	{
		this.currencyDao = currencyDao;
	}


}
