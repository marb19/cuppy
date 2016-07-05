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
package de.hybris.platform.chinesetaxinvoiceaddon.controllers;



public interface ControllerConstants
{
	String ADDON_PREFIX = "addon:/chinesetaxinvoiceaddon/";

	interface Views
	{

		interface MultiStepCheckout
		{
			String CHINESE_TAX_INVOICE_PAGE = ADDON_PREFIX + "pages/checkout/multi/chineseTaxInvoicePage";
		}
	}
}
