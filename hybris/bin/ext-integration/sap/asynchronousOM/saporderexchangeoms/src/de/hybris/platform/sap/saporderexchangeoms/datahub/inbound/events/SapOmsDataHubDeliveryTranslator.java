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
package de.hybris.platform.sap.saporderexchangeoms.datahub.inbound.events;


import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.sap.orderexchange.constants.DataHubInboundConstants;
import de.hybris.platform.sap.orderexchange.inbound.events.DataHubTranslator;
import de.hybris.platform.sap.saporderexchangeoms.datahub.inbound.SapDataHubInboundHelper;



/**
 * This class includes the translator for delivery that updates consignments statuses.
 */
public class SapOmsDataHubDeliveryTranslator extends DataHubTranslator<SapDataHubInboundHelper>
{
	@SuppressWarnings("javadoc")
	public static final String HELPER_BEAN = "sapDataHubInboundHelper";

	@SuppressWarnings("javadoc")
	public SapOmsDataHubDeliveryTranslator()
	{
		super(HELPER_BEAN);
	}

	@Override
	public void performImport(final String delivInfo, final Item processedItem) throws ImpExException
	{
		String orderCode = null;

		try
		{
			orderCode = processedItem.getAttribute(DataHubInboundConstants.CODE).toString();
		}
		catch (final JaloSecurityException | JaloInvalidParameterException e)
		{
			throw new ImpExException(e);
		}

		if (delivInfo != null && !delivInfo.equals(DataHubInboundConstants.IGNORE))
		{
			final String entryNumber = getInboundHelper().determineEntryNumber(delivInfo);
			final String quantity = getInboundHelper().determineQuantity(delivInfo);

			if (Long.parseLong(quantity) > 0)
			{
				getInboundHelper().processDeliveryNotification(orderCode, entryNumber);
			}

		}
	}
}
