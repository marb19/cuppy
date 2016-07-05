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
package de.hybris.platform.sap.sapinvoiceservices.services.impl;

import de.hybris.platform.sap.core.configuration.SAPConfigurationService;
import de.hybris.platform.sap.core.jco.exceptions.BackendException;
import de.hybris.platform.sap.sapinvoiceservices.services.InvoiceService;
import de.hybris.platform.sap.sapinvoiceservices.services.SapInvoiceBOFactory;

import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 *
 */
public class InvoiceServiceImpl implements InvoiceService
{

	private SapInvoiceBOFactory sapInvoiceBOFactory;

	@Override
	public byte[] getPDFData(final String billDocumentNumber) throws BackendException
	{
		return getSapInvoiceBOFactory().getSapInvoiceBO().getPDF(billDocumentNumber);
	}

	/**
	 * @return the sapInvoiceBOFactory
	 */
	public SapInvoiceBOFactory getSapInvoiceBOFactory()
	{
		return sapInvoiceBOFactory;
	}

	/**
	 * @param sapInvoiceBOFactory
	 *           the sapInvoiceBOFactory to set
	 */
	@Required
	public void setSapInvoiceBOFactory(final SapInvoiceBOFactory sapInvoiceBOFactory)
	{
		this.sapInvoiceBOFactory = sapInvoiceBOFactory;
	}

}
