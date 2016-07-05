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

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.ordersplitting.jalo.StockLevel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.yaasconfiguration.model.YaasApplicationModel;


public class StockLevelApplicationLookupStrategy extends AbstractFractusApplicationLookupStrategy
{
	@Override
	public String getTypeCode()
	{
		return StockLevelModel._TYPECODE;
	}


	@Override
	public String lookup(Item item)
	{
		if (item instanceof StockLevel)
		{
			StockLevelModel stockLevelModel = getModelService().get(item);
			ProductModel productModel = stockLevelModel.getProduct();

			Set<YaasApplicationModel> applications = getApplications(productModel);
			return applications.isEmpty() ? StringUtils.EMPTY : applications.iterator().next().getIdentifier();
		}
		return StringUtils.EMPTY;
	}

}
