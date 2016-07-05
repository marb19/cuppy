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
package de.hybris.platform.chinesetaxinvoicefacades.facades;

import de.hybris.platform.acceleratorfacades.order.AcceleratorCheckoutFacade;
import de.hybris.platform.chinesetaxinvoicefacades.data.TaxInvoiceData;


public interface TaxInvoiceCheckoutFacade extends AcceleratorCheckoutFacade
{

	/**
	 * Save TaxInvoice in AbstractOrderModel.
	 *
	 * @param data
	 *           TaxInvoiceData.
	 * @return
	 */
	boolean setTaxInvoice(TaxInvoiceData data);

	/**
	 * Remove a TaxInvoiceModel for PK.
	 *
	 * @param code
	 *           PK.
	 * @return
	 */
	boolean removeTaxInvoice(String code);

	/**
	 * Check if the current CartModel has an TaxInvoice.
	 *
	 * @return
	 */
	boolean hasTaxInvoice();
}
