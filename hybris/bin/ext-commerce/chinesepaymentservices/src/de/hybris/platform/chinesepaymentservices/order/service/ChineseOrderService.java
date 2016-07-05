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
package de.hybris.platform.chinesepaymentservices.order.service;

/**
 * The service of ChineseOrder
 */
public interface ChineseOrderService
{
	/**
	 * Cancel the order
	 *
	 * @param code
	 *           The code of the order
	 */
	void cancelOrder(final String code);

	/**
	 * Mark the order as paid
	 *
	 * @param orderCode
	 *           The code of the order
	 */
	void markOrderAsPaid(final String orderCode);
}
