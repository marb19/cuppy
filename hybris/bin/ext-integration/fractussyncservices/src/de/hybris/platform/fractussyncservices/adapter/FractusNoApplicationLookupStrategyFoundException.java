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
package de.hybris.platform.fractussyncservices.adapter;

import de.hybris.platform.servicelayer.exceptions.BusinessException;


public class FractusNoApplicationLookupStrategyFoundException extends BusinessException
{
	public FractusNoApplicationLookupStrategyFoundException(String message)
	{
		super(message);
	}

	public FractusNoApplicationLookupStrategyFoundException(Throwable cause)
	{
		super(cause);
	}

	public FractusNoApplicationLookupStrategyFoundException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
