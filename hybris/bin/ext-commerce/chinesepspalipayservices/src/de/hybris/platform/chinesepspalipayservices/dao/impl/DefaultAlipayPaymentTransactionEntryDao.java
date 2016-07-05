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
package de.hybris.platform.chinesepspalipayservices.dao.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.chinesepspalipayservices.dao.AlipayPaymentTransactionEntryDao;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.AlipayPaymentTransactionEntryModel;
import de.hybris.platform.payment.model.AlipayPaymentTransactionModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


public class DefaultAlipayPaymentTransactionEntryDao extends DefaultGenericDao<AlipayPaymentTransactionEntryModel>
		implements AlipayPaymentTransactionEntryDao
{
	Logger LOG = Logger.getLogger(DefaultAlipayPaymentTransactionDao.class);

	public DefaultAlipayPaymentTransactionEntryDao()
	{
		super(AlipayPaymentTransactionEntryModel._TYPECODE);
	}


	@Override
	public AlipayPaymentTransactionEntryModel findPaymentTransactionEntryByTypeAndStatus(final PaymentTransactionType capture,
			final TransactionStatus status, final AlipayPaymentTransactionModel alipayPaymentTransaction)
	{
		validateParameterNotNull(capture, "PaymentTransactionType cannot be null");
		validateParameterNotNull(status, "status cannot be null");
		validateParameterNotNull(alipayPaymentTransaction, "AlipayPaymentTransaction cannot be null");

		AlipayPaymentTransactionEntryModel entry = null;
		final Map<String, Object> params = new HashMap<>();


		params.put(AlipayPaymentTransactionEntryModel.TRANSACTIONSTATUS, status.name());
		params.put(AlipayPaymentTransactionEntryModel.TYPE, capture);
		params.put(AlipayPaymentTransactionEntryModel.PAYMENTTRANSACTION, alipayPaymentTransaction);
		final List<AlipayPaymentTransactionEntryModel> entries = find(params);

		if (entries == null || entries.isEmpty())
		{
			LOG.error("There is no Alipay payment transaction entry with type: " + capture + " and status: " + status);
		}
		else if (entries.size() > 1)
		{
			LOG.error("More than one Alipay payment transaction entries found for type: " + capture + " and status: " + status);
		}
		else
		{
			entry = entries.get(0);
		}

		return entry;
	}

}