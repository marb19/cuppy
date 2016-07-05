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
package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.warehousing.model.AllocationEventModel;


public class AllocationEventModelBuilder
{
	private final AllocationEventModel model;

	private AllocationEventModelBuilder()
	{
		model = new AllocationEventModel();
	}

	private AllocationEventModel getModel()
	{
		return this.model;
	}

	public static AllocationEventModelBuilder aModel()
	{
		return new AllocationEventModelBuilder();
	}

	public AllocationEventModel build()
	{
		return getModel();
	}

	public AllocationEventModelBuilder withConsignmentEntry(final ConsignmentEntryModel consignmentEntry)
	{
		getModel().setConsignmentEntry(consignmentEntry);
		return this;
	}

	public AllocationEventModelBuilder withStockLevel(final StockLevelModel stockLevel)
	{
		getModel().setStockLevel(stockLevel);
		return this;
	}

	public AllocationEventModelBuilder withQuantity(final long quantity)
	{
		getModel().setQuantity(quantity);
		return this;
	}
}
