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
package de.hybris.platform.sap.sapinvoiceaddon.constants;

import de.hybris.platform.sap.sapinvoiceaddon.constants.GeneratedSapinvoiceaddonConstants;

/**
 * Global class for all Sapinvoiceaddon constants. You can add global constants for your extension into this class.
 */
public final class SapinvoiceaddonConstants extends GeneratedSapinvoiceaddonConstants
{
	public static final String EXTENSIONNAME = "sapinvoiceaddon";
	public static final String SAP_INVOICE_BO = "sapInvoiceBO";
	public static final String MYCOMPANY_INVOICE_DETAILS_PAGE = "invoice";
	
	public static final String REDIRECT_PREFIX = "redirect:";
	public static final String INVOICE_CODE_PATH_VARIABLE_PATTERN = "{invoiceCode:.*}";
	public static final String REDIRECT_TO_DOCUMENT_LIST_PAGE = REDIRECT_PREFIX
			+ "addon:/accountsummaryaddon/pages/documents";
	public static final String ACCOUNT_STATUS_DOCUMENTS_PATH = "/my-company/organization-management/accountstatus/details?unit=%s";
	public static final String ACCOUNT_STATUS_DOCUMENTS_UNIT_PATH = "/my-company/organization-management/accountsummary-unit/details?unit=%s";
	public static final String TEXT_COMPANY_ACCOUNTSUMMARY = "text.company.accountsummary";
	public static final String TEXT_COMPANY_ACCOUNTSUMMARY_DETAILS = "text.company.accountsummary.details";
	public static final String ACCOUNT_STATUS_PATH = "/my-company/organization-management/accountstatus/";
	public static final String ACCOUNT_STATUS_PATH_UNIT = "/my-company/organization-management/accountsummary-unit/";
	public static final String ACCOUNT_STATUS_UI_VERSION = "commerceservices.default.desktop.ui.experience";
	public static final String ACCOUNT_STATUS_DESKTOP_STRING = "desktop";
	public static final String ACCOUNT_STATUS_RESPONSIVE_STRING = "responsive";
	
	public static final String MY_COMPANY_URL = "/my-company";
	public static final String MY_COMPANY_MESSAGE_KEY = "header.link.company";

	
	

	
	public static final String ACCOUNT_INVOICE_DETAILS="text.company.accountsummary.invoice.details";



	private SapinvoiceaddonConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension
}
