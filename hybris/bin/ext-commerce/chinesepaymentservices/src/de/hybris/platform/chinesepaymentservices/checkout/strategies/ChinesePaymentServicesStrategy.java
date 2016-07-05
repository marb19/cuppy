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
package de.hybris.platform.chinesepaymentservices.checkout.strategies;

import de.hybris.platform.chinesepaymentservices.payment.ChinesePaymentService;


/**
 * The Strategy of ChinesePaymentServices
 */
public interface ChinesePaymentServicesStrategy
{
	/**
	 * Getting the PaymentService
	 *
	 * @param paymentService
	 *           the id of the paymentService
	 * @return ChinesePaymentService
	 */
	ChinesePaymentService getPaymentService(String paymentService);
}
