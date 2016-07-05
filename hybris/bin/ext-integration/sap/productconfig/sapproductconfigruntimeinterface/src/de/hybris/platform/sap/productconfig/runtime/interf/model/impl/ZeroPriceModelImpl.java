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
package de.hybris.platform.sap.productconfig.runtime.interf.model.impl;

import java.math.BigDecimal;


/**
 * Immutable Object
 *
 */
public class ZeroPriceModelImpl extends PriceModelImpl
{

	@Override
	public void setCurrency(final String currency)
	{
		throw new IllegalArgumentException("ZeroPriceModelImpl is immutable");
	}

	@Override
	public void setPriceValue(final BigDecimal priceValue)
	{
		throw new IllegalArgumentException("ZeroPriceModelImpl is immutable");
	}

	@Override
	public String getCurrency()
	{
		return "";
	}

	@Override
	public BigDecimal getPriceValue()
	{
		return BigDecimal.ZERO;
	}
}
