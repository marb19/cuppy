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
package de.hybris.platform.sap.sapinvoiceaddon.document.dao;

import de.hybris.platform.sap.sapinvoiceaddon.model.SapB2BDocumentModel;


/**
 *
 */
public interface B2BInvoiceDao
{

	/**
	 * Retrieves SapB2BDocumentModel for given invoiceDocumentNumber.
	 *
	 * @param invoiceDocumentNumber
	 *           the invoice number
	 * @return SapB2BDocumentModel object if found, null otherwise
	 */
	public abstract SapB2BDocumentModel findInvoiceByDocumentNumber(String invoiceDocumentNumber);
}