/*
 *  
 * [y] hybris Platform
 *  
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *  
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *  
 */
package de.hybris.platform.warehousing.util;

import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ordercancel.OrderCancelEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * This is a Sourcing order builder implementation of the Builder interface
 */

public class CancellationEntryBuilder
{
	public static CancellationEntryBuilder aCancellation()
	{
		return new CancellationEntryBuilder();
	}

	public List<OrderCancelEntry> build(Map<AbstractOrderEntryModel, Long> cancellationEntryInfo, CancelReason cancelReason)
	{
		List<OrderCancelEntry> cancellationEntryCollection = new ArrayList<OrderCancelEntry>();
		cancellationEntryInfo.entrySet().stream().forEach(
				cancellation ->
				{
					OrderCancelEntry cancellationEntry = new OrderCancelEntry(cancellation.getKey(), cancellation.getValue(), cancelReason.toString());
					cancellationEntryCollection.add(cancellationEntry);
					//TODO OMSE-1099
				}
		);
		return cancellationEntryCollection;
	}
}
