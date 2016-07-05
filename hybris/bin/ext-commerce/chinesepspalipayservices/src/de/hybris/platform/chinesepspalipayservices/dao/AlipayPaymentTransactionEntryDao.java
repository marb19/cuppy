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
package de.hybris.platform.chinesepspalipayservices.dao;

import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.AlipayPaymentTransactionEntryModel;
import de.hybris.platform.payment.model.AlipayPaymentTransactionModel;


public interface AlipayPaymentTransactionEntryDao
{

	/**
	 * Get AlipayPaymentTransactionEntry by the given PaymentTransactionEntry type, transaction status and
	 * AlipayPaymentTransaction.
	 *
	 * @param capture
	 *           PaymentTransactionEntry type: CAPTURE,CANCEL and so on
	 * @param status
	 *           PaymentTransactionStatus: ACCEPTED,REJECTED and so on
	 * @param alipayPaymentTransaction
	 * @return AlipayPaymentTransactionEntryModel if found and null otherwise
	 */
	public AlipayPaymentTransactionEntryModel findPaymentTransactionEntryByTypeAndStatus(final PaymentTransactionType capture,
			final TransactionStatus status, final AlipayPaymentTransactionModel alipayPaymentTransaction);
}

