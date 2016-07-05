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
package de.hybris.platform.sap.sapinvoicebol.backend;

import de.hybris.platform.sap.core.bol.backend.BackendBusinessObject;
import de.hybris.platform.sap.core.jco.exceptions.BackendException;


/**
 *
 */
public interface SapInvoiceBackend extends BackendBusinessObject
{
	/**
	 * get the invoice in byte format
	 *
	 * @param billingDocNumber
	 * @return byte array
	 * @throws BackendException
	 */
	public byte[] getInvoiceInByte(final String billingDocNumber) throws BackendException;
}
