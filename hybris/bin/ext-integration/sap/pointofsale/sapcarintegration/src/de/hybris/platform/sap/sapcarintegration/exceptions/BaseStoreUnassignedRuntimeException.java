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
package de.hybris.platform.sap.sapcarintegration.exceptions;

public class BaseStoreUnassignedRuntimeException extends RuntimeException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BaseStoreUnassignedRuntimeException(String msg, Exception e)
	{
		super(msg, e);
	}
	
	public BaseStoreUnassignedRuntimeException(String msg)
	{
		super(msg);
	}
}
