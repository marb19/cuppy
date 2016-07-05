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
package de.hybris.platform.warehousing.sourcing.context.populator.impl;

import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.atp.services.WarehouseStockService;
import de.hybris.platform.warehousing.data.sourcing.SourcingContext;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;

import static org.mockito.Mockito.spy;


@RunWith(MockitoJUnitRunner.class)
public class AvailabilitySourcingLocationPopulatorTest
{
	private static final ProductModel PRODUCT_1 = new ProductModel();
	private static final ProductModel PRODUCT_2 = new ProductModel();
	private static final Long QUANTITY_1 = 4L;
	private static final Long QUANTITY_2 = 9L;

	@InjectMocks
	private final AvailabilitySourcingLocationPopulator populator = new AvailabilitySourcingLocationPopulator();
	private WarehouseModel warehouse;
	private SourcingLocation location;
	@Spy
	private OrderEntryModel orderEntry1;
	@Spy
	private OrderEntryModel orderEntry2;
	@Mock
	private WarehouseStockService warehouseStockService;

	@Before
	public void setUp()
	{
		location = new SourcingLocation();
		final SourcingContext context = new SourcingContext();
		final OrderModel order = new OrderModel();

		orderEntry1.setProduct(PRODUCT_1);
		orderEntry2.setProduct(PRODUCT_2);
		order.setEntries(Lists.newArrayList(orderEntry1, orderEntry2));

		context.setOrderEntries(Lists.newArrayList(orderEntry1, orderEntry2));
		location.setContext(context);

		warehouse = new WarehouseModel();

		Mockito.when(warehouseStockService.getStockLevelForProductAndWarehouse(PRODUCT_1, warehouse)).thenReturn(QUANTITY_1);
		Mockito.when(warehouseStockService.getStockLevelForProductAndWarehouse(PRODUCT_2, warehouse)).thenReturn(QUANTITY_2);
	}

	@Test
	public void shouldPopulateAvailability()
	{
		populator.populate(warehouse, location);
		Assert.assertEquals(QUANTITY_1, location.getAvailability().get(PRODUCT_1));
		Assert.assertEquals(QUANTITY_2, location.getAvailability().get(PRODUCT_2));
	}

	@Test
	public void shouldDefaultAvailabilityToOrderEntryQuantityWhenNull()
	{
		Mockito.when(warehouseStockService.getStockLevelForProductAndWarehouse(PRODUCT_1, warehouse)).thenReturn(QUANTITY_1);
		Mockito.when(orderEntry1.getQuantityUnallocated()).thenReturn(QUANTITY_1);

		populator.populate(warehouse, location);
		Assert.assertEquals(QUANTITY_1, location.getAvailability().get(PRODUCT_1));
		Assert.assertEquals(QUANTITY_2, location.getAvailability().get(PRODUCT_2));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailPopulate_NullSource()
	{
		warehouse = null;
		populator.populate(warehouse, location);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailPopulate_NullTarget()
	{
		location = null;
		populator.populate(warehouse, location);
	}
}
