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
package de.hybris.platform.chinesepaymentservices.stocklevel.dao.impl;

import de.hybris.platform.chinesepaymentservices.model.StockLevelReservationHistoryEntryModel;
import de.hybris.platform.chinesepaymentservices.stocklevel.dao.StockLevelReservationHistoryEntryDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


public class ChineseStockLevelReservationHistoryEntryDao implements StockLevelReservationHistoryEntryDao
{

	private FlexibleSearchService flexibleSearchService;

	@Override
	public List<StockLevelReservationHistoryEntryModel> getStockLevelReservationHistoryEntryByOrderCode(final String orderCode)
	{
		final String fsq = "SELECT {" + StockLevelReservationHistoryEntryModel.PK + "} FROM {"
				+ StockLevelReservationHistoryEntryModel._TYPECODE + "} WHERE {" + StockLevelReservationHistoryEntryModel.ORDERCODE
				+ "}  = ?orderCode";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(fsq);
		query.addQueryParameter("orderCode", orderCode);
		final SearchResult<StockLevelReservationHistoryEntryModel> result = flexibleSearchService.search(query);
		final List<StockLevelReservationHistoryEntryModel> StockLevelReservationHistoryEntries = result.getResult();
		return StockLevelReservationHistoryEntries;
	}

	protected FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

}
