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

import de.hybris.platform.sap.core.common.util.GenericFactory;
import de.hybris.platform.sap.sapinvoicebol.businessobject.SapInvoiceBO;
import de.hybris.platform.sap.sapinvoiceservices.constants.SapinvoiceservicesConstants;
import de.hybris.platform.sap.sapinvoiceservices.services.SapInvoiceBOFactory;

import org.springframework.beans.factory.annotation.Required;


/**
 *
 */

public class SapInvoiceBOFactoryImpl implements SapInvoiceBOFactory
{
	private GenericFactory genericFactory;

	/**
	 *
	 * @return the genericFactory
	 */
	public GenericFactory getGenericFactory()
	{
		return genericFactory;
	}

	/**
	 *
	 * @param genericFactory
	 */
	@Required
	public void setGenericFactory(final GenericFactory genericFactory)
	{
		this.genericFactory = genericFactory;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.accountsummaryaddon.document.service.SapInvoiceBOFactory#getSapInvoiceBO()
	 */
	@Override
	public SapInvoiceBO getSapInvoiceBO()
	{
		return (SapInvoiceBO) genericFactory.getBean(SapinvoiceservicesConstants.SAP_INVOICE_BO);

	}

}
