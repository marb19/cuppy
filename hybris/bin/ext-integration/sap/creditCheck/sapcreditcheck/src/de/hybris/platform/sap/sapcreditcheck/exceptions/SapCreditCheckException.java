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
package de.hybris.platform.sap.sapcreditcheck.exceptions;

/**
 * 
 */
public class SapCreditCheckException extends RuntimeException 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6915031997744276220L;

	/**
	 * @param exp
	 */
	public SapCreditCheckException(final Exception exp)
	{
		super(exp);
	}
}
