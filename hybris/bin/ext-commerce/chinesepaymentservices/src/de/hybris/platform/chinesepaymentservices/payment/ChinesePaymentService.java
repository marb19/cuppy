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
package de.hybris.platform.chinesepaymentservices.payment;

import de.hybris.platform.chinesepaymentservices.model.ChinesePaymentInfoModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.payment.PaymentService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * The interface to be implemented by the 3rd part payment service provider
 */
public interface ChinesePaymentService extends PaymentService
{
	/**
	 * Handling the Asyn-response of the 3rd part payment service provider server
	 *
	 * @param request
	 *           The HttpServletRequest
	 * @param response
	 *           The HttpServletResponse
	 */
	void handleAsynResponse(final HttpServletRequest request, final HttpServletResponse response);

	/**
	 * Handling the Sync-response of the 3rd part payment service provider server
	 *
	 * @param request
	 *           The HttpServletRequest
	 * @param response
	 *           The HttpServletResponse
	 * @return String the code of the order
	 */
	String handleSyncResponse(final HttpServletRequest request, final HttpServletResponse response);

	/**
	 * Canceling the payment with the 3rd part payment service provider server
	 *
	 * @param orderCode
	 *           The code of the order
	 * @return boolean
	 */
	boolean cancelPayment(final String orderCode);

	/**
	 * Getting the PaymentRequestUrl to be send to the 3rd part payment service provider server
	 *
	 * @param orderCode
	 *           The code of the order
	 * @return String
	 */
	String getPaymentRequestUrl(final String orderCode);

	/**
	 * Synchronizing the PaymentStatus with the 3rd part payment service provider server
	 *
	 * @param orderCode
	 *           The code of the order
	 */
	void syncPaymentStatus(final String orderCode);

	/**
	 * Saving the PaymentInfo
	 *
	 * @param cartModel
	 *           The current cart
	 * @param chinesePaymentInfoModel
	 *           The ChinesePaymentInfo of the cart
	 * @return boolean
	 */
	boolean setPaymentInfo(final CartModel cartModel, final ChinesePaymentInfoModel chinesePaymentInfoModel);

	/**
	 * Getting the Logo fo the 3rd part payment service providers
	 *
	 * @return String
	 */
	String getPspLogoUrl();
}
