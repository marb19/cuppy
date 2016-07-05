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
package de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus.exception;

public class UnknownEcpStatusException extends Exception
{
	public UnknownEcpStatusException(final String message)
	{
		super(message);
	}

	public UnknownEcpStatusException(final Throwable cause)
	{
		super(cause);
	}

	public UnknownEcpStatusException(final String message, final Throwable cause)
	{
		super(message, cause);
	}
}
