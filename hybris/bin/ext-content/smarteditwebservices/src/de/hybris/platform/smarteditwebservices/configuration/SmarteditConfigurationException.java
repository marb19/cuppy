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
package de.hybris.platform.smarteditwebservices.configuration;


/**
 * Thrown when there is a problem while calling any of the SmarteditConfigurationFacade methods
 */
public class SmarteditConfigurationException extends RuntimeException
{

	public SmarteditConfigurationException(final String message)
	{
		super(message);
	}

	public SmarteditConfigurationException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public SmarteditConfigurationException(final Throwable cause)
	{
		super(cause);
	}
}
