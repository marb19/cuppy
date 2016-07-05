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
package com.sap.hybris.sapcustomerb2b.outbound;

import de.hybris.platform.b2bacceleratorservices.company.impl.DefaultCompanyB2BCommerceService;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.impl.UniqueAttributesInterceptor;
import org.apache.log4j.Logger;

public class DefaultCompanyB2BSapCommerceService extends DefaultCompanyB2BCommerceService {
	private static final Logger LOG = Logger.getLogger(DefaultCompanyB2BSapCommerceService.class);
	DefaultB2BSapCustomerAccountService b2BSAPCustomerAccountService ;

	@Override
	public <T extends ItemModel> void saveModel(final T model) 
	{
		try
		{
			getB2BSAPCustomerAccountService().register((CustomerModel)model, null);
		}
		catch (final ModelSavingException e)
		{
			if (e.getCause() instanceof InterceptorException
					&& ((InterceptorException) e.getCause()).getInterceptor().getClass().equals(UniqueAttributesInterceptor.class))
			{
				LOG.error("Could not save the model (B2B Contact).");	
			}			
		} catch (DuplicateUidException e) {
			LOG.error("The uid of the model being stored already exists, could not save.");			
		} 
	}

	public DefaultB2BSapCustomerAccountService getB2BSAPCustomerAccountService() {
		return b2BSAPCustomerAccountService;
	}

	public void setB2BSAPCustomerAccountService(
			DefaultB2BSapCustomerAccountService b2bsapCustomerAccountService) {
		b2BSAPCustomerAccountService = b2bsapCustomerAccountService;
	}

	
	
	
	
	
}
