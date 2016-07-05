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
package de.hybris.platform.warehousingfacade.warehouse.impl;


import de.hybris.platform.ordermanagementfacade.OmsBaseFacade;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.warehousingfacade.storelocator.data.WarehouseData;
import de.hybris.platform.warehousingfacade.warehouse.WarehousingWarehouseFacade;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of {@link WarehousingWarehouseFacade}
 */
public class DefaultWarehousingWarehouseFacade extends OmsBaseFacade implements WarehousingWarehouseFacade
{
	private Converter<WarehouseModel, WarehouseData> warehouseConverter;
	private WarehouseService warehouseService;

	@Override
	public WarehouseData getWarehouseForCode(final String code)
	{
		return getWarehouseConverter().convert(getWarehouseService().getWarehouseForCode(code));
	}


	protected Converter<WarehouseModel, WarehouseData> getWarehouseConverter()
	{
		return warehouseConverter;
	}

	@Required
	public void setWarehouseConverter(final Converter<WarehouseModel, WarehouseData> warehouseConverter)
	{
		this.warehouseConverter = warehouseConverter;
	}

	protected WarehouseService getWarehouseService()
	{
		return warehouseService;
	}

	@Required
	public void setWarehouseService(WarehouseService warehouseService)
	{
		this.warehouseService = warehouseService;
	}

}
