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
 */

package de.hybris.platform.fractussyncservices.adapter.impl;


import java.util.Set;

import org.apache.commons.lang.StringUtils;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.ordersplitting.jalo.Consignment;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.yaasconfiguration.model.YaasApplicationModel;


public class ConsignmentApplicationLookupStrategy extends AbstractFractusApplicationLookupStrategy
{

	@Override
	public String getTypeCode()
	{
		return ConsignmentModel._TYPECODE;
	}

	@Override
	public String lookup(Item item)
	{
		if (item instanceof Consignment)
		{
			ConsignmentModel consignmentModel = getModelService().get(item);

			Set<YaasApplicationModel> applications = getApplications(consignmentModel.getOrder().getSite());

			return applications.isEmpty() ? StringUtils.EMPTY : applications.iterator().next().getIdentifier();
		}

		return StringUtils.EMPTY;
	}
}
