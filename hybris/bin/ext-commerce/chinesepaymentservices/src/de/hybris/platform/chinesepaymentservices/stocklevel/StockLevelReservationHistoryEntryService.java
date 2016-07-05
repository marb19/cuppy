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
package de.hybris.platform.chinesepaymentservices.stocklevel;

import de.hybris.platform.chinesepaymentservices.model.StockLevelReservationHistoryEntryModel;

import java.util.List;


/**
 * The service of StockLevelReservationHistoryEntry
 */
public interface StockLevelReservationHistoryEntryService
{
	/**
	 * Getting the StockLevelReservationHistoryEntry by the code of the order
	 *
	 * @param orderCode
	 *           The code of the order
	 * @return List<StockLevelReservationHistoryEntryModel>
	 */
	List<StockLevelReservationHistoryEntryModel> getStockLevelReservationHistoryEntryByOrderCode(String orderCode);
}
