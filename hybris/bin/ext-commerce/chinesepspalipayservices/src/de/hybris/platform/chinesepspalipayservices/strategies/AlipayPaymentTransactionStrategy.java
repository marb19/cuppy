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
package de.hybris.platform.chinesepspalipayservices.strategies;


import de.hybris.platform.chinesepspalipayservices.data.AlipayDirectPayRequestData;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRawCancelPaymentResult;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRawDirectPayErrorInfo;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRawDirectPayNotification;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRawPaymentStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.model.AlipayPaymentTransactionEntryModel;


public interface AlipayPaymentTransactionStrategy
{

	/**
	 * Save new transaction with entry for some order once new direct_pay request is issued.
	 *
	 * @param orderModel
	 *           order launching direct_pay
	 * @param directPayRequestData
	 *           Request data needed for launching direct_pay
	 * @param requestUrl
	 *           accessible URL
	 */
	void createForNewRequest(OrderModel orderModel, AlipayDirectPayRequestData directPayRequestData, String requestUrl);


	/**
	 *
	 * Save alipayPaymentTransactionEntry once payment status check (Alipay's check trade) is completed
	 *
	 * @param orderModel
	 *           order launching check trade
	 * @param checkTradeResponseData
	 *           Data needed for launching check trade
	 */
	AlipayPaymentTransactionEntryModel saveForStatusCheck(OrderModel orderModel, AlipayRawPaymentStatus checkTradeResponseData);

	/**
	 * Update alipayPaymentTransaction and entry once notify data from alipay is received.
	 *
	 * @param orderModel
	 *           Order handled by the notify data {@link OrderModel}
	 * @param directPayNotifyResponseData
	 *           Notify data from alipay {@link AlipayRawDirectPayNotification}
	 */
	void updateForNotification(OrderModel orderModel, AlipayRawDirectPayNotification directPayNotifyResponseData);

	/**
	 * Update alipayPaymentTransaction and entry once cancel trade is launched
	 *
	 * @param orderModel
	 *           order launching cancel trade
	 * @param alipayRawCancelPaymentResult
	 *           Response data from alipay after canceling trade {@link AlipayRawCancelPaymentResult}
	 */
	void updateForCancelPayment(OrderModel orderModel, final AlipayRawCancelPaymentResult alipayRawCancelPaymentResult);

	/**
	 * Update alipayPaymentTransaction and entry once error data from alipay is received.
	 *
	 * @param orderModel
	 *           Order handled by the error data
	 * @param aipayRawDirectPayErrorInfo
	 *           Error data from alipay {@link AlipayRawDirectPayErrorInfo}
	 */
	void updateForError(OrderModel orderModel, AlipayRawDirectPayErrorInfo aipayRawDirectPayErrorInfo);

	/**
	 * Whether the alipayPaymentTransaction under a order which has CAPTURED entry exists
	 *
	 * @param orderModel
	 *           Order needed to check
	 *
	 * @param status
	 *           transaction status {@link TransactionStatus}
	 * @return if false, the transaction exists.Otherwise,not exist
	 */
	boolean checkCaptureTransactionEntry(final OrderModel orderModel, final TransactionStatus status);

}
