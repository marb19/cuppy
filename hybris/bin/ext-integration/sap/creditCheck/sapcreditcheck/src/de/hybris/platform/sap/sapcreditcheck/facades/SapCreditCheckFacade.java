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
package de.hybris.platform.sap.sapcreditcheck.facades;

/**
 *
 */
public interface SapCreditCheckFacade
{
	
	/**
	 * Check if the credit limit has been exceeded 
	 * @return true if credit check exceeded
	 */
	abstract boolean checkCreditLimitExceeded(String orderCode);
	 
	
	/**
	 * checks if order ir credit bocked
	 * @param orderCode
	 * @return true if order is credit blocked
	 */
	abstract boolean checkOrderCreditBlocked(String orderCode);
	
	
	
	
}
