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
package de.hybris.platform.sap.sapproductavailability.service.impl;

import de.hybris.platform.sap.core.common.util.GenericFactory;
import de.hybris.platform.sap.sapproductavailability.businessobject.SapProductAvailabilityBO;
import de.hybris.platform.sap.sapproductavailability.constants.SapproductavailabilityConstants;
import de.hybris.platform.sap.sapproductavailability.service.SapProductAvailabilityBOFactory;

import org.springframework.beans.factory.annotation.Required;


/**
 * 
 */
public class DefaultSapProductAvailabilityBOFactory implements SapProductAvailabilityBOFactory
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
	 * @see
	 * de.hybris.platform.sap.sapproductavailability.service.SapProductAvailabilityBOFactory#getSapProductAvailabilityBO
	 * ()
	 */
	@Override
	public SapProductAvailabilityBO getSapProductAvailabilityBO()
	{
		return (SapProductAvailabilityBO) genericFactory.getBean(SapproductavailabilityConstants.SAP_PRODUCT_AVAILABILITY_BO);
	}

}
