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
package de.hybris.platform.sap.sapcreditcheck.backend;

import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.sap.core.bol.backend.BackendBusinessObject;
import de.hybris.platform.sap.core.jco.exceptions.BackendException;


/**
 *
 */
public interface SapCreditCheckBackend extends BackendBusinessObject
{
	/**
	 * checks if status of order is credit blocked
	 * @param orderCode
	 * @return a boolean flag indicating if the order is credit blocked
	 * @throws BackendException
	 */
	public boolean checkOrderCreditBlocked(final String orderCode) throws BackendException;

	/**
	 * 
	 * @param orderData
	 * @param soldTo
	 * @return
	 * @throws BackendException
	 */
	boolean checkCreditLimitExceeded(AbstractOrderData orderData, String soldTo)
			throws BackendException;
}
