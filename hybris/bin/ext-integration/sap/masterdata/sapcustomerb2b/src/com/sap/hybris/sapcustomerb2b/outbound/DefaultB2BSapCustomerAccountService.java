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

import de.hybris.platform.b2bacceleratorservices.customer.impl.DefaultB2BCustomerAccountService;
import de.hybris.platform.commerceservices.customer.impl.DefaultCustomerAccountService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;

/**
 * override the generateContactId method of class
 * {@link DefaultCustomerAccountService} and use the sapContactIdGenerator to
 * generate a contact ID
 */
public class DefaultB2BSapCustomerAccountService extends DefaultB2BCustomerAccountService {

	private PersistentKeyGenerator sapContactIdGenerator;
	
	/**
	 * Generates with sapCustomerIdGenerator a customer ID during registration
	 * 
	 * @param customerModel
	 */
	@Override
	protected void generateCustomerId(final CustomerModel customerModel)
	{
			// No implementation provided to avoid default customer id generation.
	}

	/**
	 * Generates with sapContactIdGenerator a contact ID during registration
	 * 
	 * @param customerModel
	 */
	protected void generateContactId(final CustomerModel customerModel) {
		customerModel.setSapContactID((String) getSapContactIdGenerator().generate());
	}

	/**
	 * Returns the Generator instance
	 * 
	 * @return sapContactIdGenerator
	 */
	public PersistentKeyGenerator getSapContactIdGenerator() {
		return sapContactIdGenerator;
	}

	/**
	 * Sets the Contact ID Generator
	 * 
	 * @param sapContactIdGenerator
	 */
	public void setSapContactIdGenerator(final PersistentKeyGenerator sapContactIdGenerator) {
		this.sapContactIdGenerator = sapContactIdGenerator;
	}

}
