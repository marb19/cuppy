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
package de.hybris.platform.chinesepaymentservices.checkout.strategies.impl;

import de.hybris.platform.chinesepaymentservices.checkout.strategies.ChinesePaymentServicesStrategy;
import de.hybris.platform.chinesepaymentservices.payment.ChinesePaymentService;
import de.hybris.platform.core.Registry;

import org.springframework.context.ApplicationContext;


public class DefaultChinesePaymentServicesStrategy implements ChinesePaymentServicesStrategy
{
	protected ApplicationContext getApplicationContext()
	{
		return Registry.getApplicationContext();
	}

	@Override
	public ChinesePaymentService getPaymentService(final String paymentService)
	{
		return (ChinesePaymentService) getApplicationContext().getBean(paymentService);
	}

}
