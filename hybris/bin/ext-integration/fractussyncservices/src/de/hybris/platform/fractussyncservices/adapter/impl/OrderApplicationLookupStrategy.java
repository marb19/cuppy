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


import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.jalo.order.Order;
import org.apache.commons.lang.StringUtils;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.yaasconfiguration.model.YaasApplicationModel;

import java.util.Set;


public class OrderApplicationLookupStrategy extends AbstractFractusApplicationLookupStrategy
{

	@Override
	public String getTypeCode()
	{
		return OrderModel._TYPECODE;
	}

	@Override
	public String lookup(Item item)
	{
		if (item instanceof Order)
		{
			OrderModel orderModel = getModelService().get(item);

			Set<YaasApplicationModel> applications = getApplications(orderModel.getSite());

			return applications.isEmpty() ? StringUtils.EMPTY : applications.iterator().next().getIdentifier();
		}

		return StringUtils.EMPTY;
	}
}
