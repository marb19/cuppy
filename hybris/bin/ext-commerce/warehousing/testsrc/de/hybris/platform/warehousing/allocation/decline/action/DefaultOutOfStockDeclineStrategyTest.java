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
package de.hybris.platform.warehousing.allocation.decline.action;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.warehousing.allocation.decline.action.impl.DefaultOutOfStockDeclineStrategy;
import de.hybris.platform.warehousing.atp.services.WarehouseStockService;
import de.hybris.platform.warehousing.data.allocation.DeclineEntry;
import de.hybris.platform.warehousing.inventoryevent.service.InventoryEventService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultOutOfStockDeclineStrategyTest
{

	@Mock
	private ConsignmentEntryModel consignmentEntryModel;
	@Mock
	private ConsignmentModel consignmentModel;
	@Mock
	private OrderEntryModel orderEntryModel;
	@Mock
	private ProductModel productModel;
	@Mock
	private WarehouseModel warehouseModel;
	@Mock
	private StockLevelModel stockLevelModel;
	@Mock
	private DeclineEntry declineEntry;
	@Mock
	private InventoryEventService inventoryEventService;
	@Mock
	private WarehouseStockService warehouseStockService;

	@InjectMocks
	private DefaultOutOfStockDeclineStrategy defaultOutOfStockStrategy;


	@Test
	public void shouldExecute()
	{
		//given
		Mockito.when(declineEntry.getConsignmentEntry()).thenReturn(consignmentEntryModel);
		Mockito.when(consignmentEntryModel.getOrderEntry()).thenReturn(orderEntryModel);
		Mockito.when(orderEntryModel.getProduct()).thenReturn(productModel);
		Mockito.when(consignmentEntryModel.getConsignment()).thenReturn(consignmentModel);
		Mockito.when(consignmentModel.getWarehouse()).thenReturn(warehouseModel);
		Mockito.when(warehouseStockService.getStockLevelForProductCodeAndWarehouse(productModel.getCode(), warehouseModel)).thenReturn(1L);

		// when
		defaultOutOfStockStrategy.execute(declineEntry);

		// Then
		Mockito.verify(inventoryEventService).createShrinkageEvent(consignmentEntryModel, 1L);


	}
}
