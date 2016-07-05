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
package de.hybris.platform.yacceleratorfractusfulfilmentprocess.order;

import de.hybris.platform.servicelayer.exceptions.BusinessException;


/**
 * Thrown when place fractus order is not possible.
 */
public class FractusOrderPlaceException extends BusinessException
{

	public FractusOrderPlaceException(String message)
	{
		super(message);
	}

	public FractusOrderPlaceException(Throwable cause)
	{
		super(cause);
	}

	public FractusOrderPlaceException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
