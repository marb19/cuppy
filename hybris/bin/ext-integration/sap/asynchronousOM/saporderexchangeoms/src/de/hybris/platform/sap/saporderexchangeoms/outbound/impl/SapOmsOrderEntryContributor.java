/*
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
 *
 */
package de.hybris.platform.sap.saporderexchangeoms.outbound.impl;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.sap.orderexchange.constants.OrderCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.OrderEntryCsvColumns;
import de.hybris.platform.sap.orderexchange.outbound.impl.DefaultOrderEntryContributor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;


/**
 * DefaultSapOmsOrderEntryContributor
 *
 */
public class SapOmsOrderEntryContributor extends DefaultOrderEntryContributor
{
	private final static Logger LOG = Logger.getLogger(SapOmsOrderEntryContributor.class);

	@Override
	public Set<String> getColumns()
	{
		final Set<String> columns = super.getColumns();
		columns.addAll(Arrays.asList(OrderEntryCsvColumns.WAREHOUSE, OrderEntryCsvColumns.EXPECTED_SHIPPING_DATE));
		return columns;
	}

	@Override
	public List<Map<String, Object>> createRows(final OrderModel order)
	{

		final List<Map<String, Object>> result = new ArrayList<>();
		final List<AbstractOrderEntryModel> entries = order.getEntries();


		for (final AbstractOrderEntryModel entry : entries)
		{

			for (final ConsignmentEntryModel consignmentEntry : entry.getConsignmentEntries())
			{
				if (!consignmentEntry.getConsignment().getSapConsignmentProcesses().isEmpty())
				{

					final Map<String, Object> row = new HashMap<>();

					row.put(OrderCsvColumns.ORDER_ID, order.getCode());
					row.put(OrderEntryCsvColumns.ENTRY_NUMBER, Integer.valueOf(consignmentEntry.getSapOrderEntryRowNumber() - 1));
					row.put(OrderEntryCsvColumns.QUANTITY, consignmentEntry.getQuantity());
					row.put(OrderEntryCsvColumns.PRODUCT_CODE, consignmentEntry.getOrderEntry().getProduct().getCode());
					row.put(OrderEntryCsvColumns.WAREHOUSE, consignmentEntry.getConsignment().getWarehouse().getCode());
					row.put(OrderEntryCsvColumns.EXPECTED_SHIPPING_DATE, consignmentEntry.getConsignment().getShippingDate());
					row.put(OrderEntryCsvColumns.EXTERNAL_PRODUCT_CONFIGURATION,
							getProductConfigurationData(consignmentEntry.getOrderEntry()));

					final UnitModel unit = consignmentEntry.getOrderEntry().getUnit();
					if (unit != null)
					{
						row.put(OrderEntryCsvColumns.ENTRY_UNIT_CODE, unit.getCode());
					}
					else
					{
						LOG.warn(String.format("Could not determine unit code for product %s as entry %d of order %s", consignmentEntry
								.getOrderEntry().getProduct().getCode(), consignmentEntry.getOrderEntry().getEntryNumber(),
								order.getCode()));
					}

					String shortText = determineItemShortText(consignmentEntry.getOrderEntry(), order.getLanguage().getIsocode());

					if (shortText.isEmpty())
					{
						final List<LanguageModel> fallbackLanguages = order.getLanguage().getFallbackLanguages();
						if (!fallbackLanguages.isEmpty())
						{
							shortText = determineItemShortText(consignmentEntry.getOrderEntry(), fallbackLanguages.get(0).getIsocode());
						}
					}

					row.put(OrderEntryCsvColumns.PRODUCT_NAME, shortText);

					result.add(row);

				}

			}

		}

		return result;

	}

}
