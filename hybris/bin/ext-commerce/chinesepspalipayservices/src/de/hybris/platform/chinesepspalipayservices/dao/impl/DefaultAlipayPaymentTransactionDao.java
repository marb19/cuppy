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

import de.hybris.platform.chinesepspalipayservices.dao.AlipayPaymentTransactionDao;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.AlipayPaymentTransactionEntryModel;
import de.hybris.platform.payment.model.AlipayPaymentTransactionModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


public class DefaultAlipayPaymentTransactionDao extends DefaultGenericDao<AlipayPaymentTransactionModel>
		implements AlipayPaymentTransactionDao
{
	Logger LOG = Logger.getLogger(DefaultAlipayPaymentTransactionDao.class);
	private FlexibleSearchService searchService;

	@Required
	public void setSearchService(final FlexibleSearchService searchService)
	{
		this.searchService = searchService;
	}


	protected FlexibleSearchService getSearchService()
	{
		return searchService;
	}


	public DefaultAlipayPaymentTransactionDao()
	{
		super(AlipayPaymentTransactionModel._TYPECODE);
	}

	@Override
	public AlipayPaymentTransactionModel findTransactionByLatestRequestEntry(final OrderModel orderModel, final boolean limit)
	{
		final String order = orderModel.getPk().getLongValueAsString();

		final StringBuilder queryString = new StringBuilder();
		queryString.append("select {" + AlipayPaymentTransactionModel.PK + "} from {" + AlipayPaymentTransactionModel._TYPECODE
				+ "} where {" + AlipayPaymentTransactionModel.PK + "} in" + " ({{ select * from" + "({{ " + "select {"
				+ PaymentTransactionModel._TYPECODE + "} from {" + AlipayPaymentTransactionEntryModel._TYPECODE + " as entry join "
				+ PaymentTransactionType._TYPECODE + " as type on {entry.type}={type.pk} } where {type.code}='REQUEST'"
				+ " and {entry.paymenttransaction} in ({{" + "select {" + AlipayPaymentTransactionModel.PK + "} from {"
				+ AlipayPaymentTransactionModel._TYPECODE + " as t} where {t.order}=?order"
				+ "}} )  order by {entry.time} desc limit 1 }}) as temp }})");

		if (limit)
		{
			queryString.append(" and {pk} in ({{select {" + PaymentTransactionModel._TYPECODE + "} from {"
					+ AlipayPaymentTransactionEntryModel._TYPECODE + "} group by {" + PaymentTransactionModel._TYPECODE
					+ "}  having  count(*)=1 }})");
		}
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString.toString());

		query.addQueryParameter("order", order);

		try
		{
			final AlipayPaymentTransactionModel result = searchService.searchUnique(query);
			return result;
		}
		catch (final ModelNotFoundException e)
		{
			LOG.info("No result for the given query for :" + order);
		}
		return null;

	}


	@Override
	public AlipayPaymentTransactionModel findTransactionByAlipayCode(final String alipayCode)
	{
		final String queryString = "select {pk} from {" + AlipayPaymentTransactionModel._TYPECODE + "} where {"
				+ AlipayPaymentTransactionModel.ALIPAYCODE + "}=?alipayCode";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

		query.addQueryParameter("alipayCode", alipayCode);

		final List<AlipayPaymentTransactionModel> searchResult = searchService.<AlipayPaymentTransactionModel> search(query)
				.getResult();
		if (searchResult.size() > 0)
		{
			return searchResult.get(0);
		}

		return null;

	}


}
