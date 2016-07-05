/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 */
package de.hybris.platform.chinesetaxinvoiceservices.services;

import de.hybris.platform.chinesetaxinvoicefacades.data.TaxInvoiceData;
import de.hybris.platform.chinesetaxinvoiceservices.model.TaxInvoiceModel;


public interface TaxInvoiceService
{

	/**
	 * Query TaxInvoiceModel for PK.
	 *
	 * @param code
	 *           PK of TaxInvoice
	 * @return TaxInvoiceModel
	 */
	TaxInvoiceModel getTaxInvoiceForCode(String code);

	/**
	 * To create a TaxInvoiceModel use a TaxInvoiceData
	 *
	 * @param data
	 *           A DTO instance of TaxInvoiceData.
	 * @return TaxInvoiceModel
	 */
	TaxInvoiceModel createTaxInvoice(TaxInvoiceData data);
}
