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
package de.hybris.platform.sap.sapproductavailability.businessobject.impl;

import de.hybris.platform.sap.sapproductavailability.businessobject.SapProductAvailability;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Immutable Object
 * 
 * 
 */
public class SapProductAvailabilityImpl implements SapProductAvailability
{

	private final Long currentStockLevel;

	private final Map<String, Map<Date, Long>> futureAvailability;

	/**
	 * @param currentStockLevel
	 * @param futureAvailability
	 */
	public SapProductAvailabilityImpl(final Long currentStockLevel, final Map<String, Map<Date, Long>> futureAvailability)
	{
		this.currentStockLevel = Long.valueOf(currentStockLevel.longValue());
		this.futureAvailability = new HashMap<String, Map<Date, Long>>(futureAvailability);
	}

	@Override
	public Long getCurrentStockLevel()
	{
		return Long.valueOf(this.currentStockLevel.longValue());
	}

	@Override
	public Map<String, Map<Date, Long>> getFutureAvailability()
	{
		return new HashMap<String, Map<Date, Long>>(this.futureAvailability);
	}

}
