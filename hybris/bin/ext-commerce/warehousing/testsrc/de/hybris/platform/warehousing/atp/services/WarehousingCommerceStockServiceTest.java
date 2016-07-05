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
package de.hybris.platform.warehousing.atp.services;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.stock.strategies.CommerceAvailabilityCalculationStrategy;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.warehousing.atp.services.impl.WarehousingCommerceStockService;
import de.hybris.platform.warehousing.atp.strategy.PickupWarehouseSelectionStrategy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class WarehousingCommerceStockServiceTest
{
	private static final Long ZERO = Long.valueOf(0);
	private static final Long TEN = Long.valueOf(10);

	@InjectMocks
	private final WarehousingCommerceStockService warehousingCommerceStockService = new WarehousingCommerceStockService();
	@Mock
	private StockService stockService;
	@Mock
	private CommerceAvailabilityCalculationStrategy commerceAvailabilityCalculationStrategy;
	@Mock
	private PickupWarehouseSelectionStrategy pickupWarehouseSelectionStrategy;

	private PointOfServiceModel pointOfService;
	private ProductModel product;
	private WarehouseModel warehouse;
	private List<WarehouseModel> warehouses;
	private StockLevelModel stockLevel;
	private List<StockLevelModel> stockLevels;

	@Before
	public void setUp()
	{
		warehouse = new WarehouseModel();
		warehouses = Arrays.asList(warehouse);

		pointOfService = new PointOfServiceModel();
		pointOfService.setWarehouses(warehouses);

		product = new ProductModel();
		product.setCode("TEST");

		stockLevel = new StockLevelModel();
		stockLevel.setAvailable(TEN.intValue());
		stockLevel.setProduct(product);
		stockLevel.setWarehouse(warehouse);
		stockLevels = Arrays.asList(stockLevel);
	}


	@Test(expected = IllegalArgumentException.class)
	public void shouldFail_GetStockLevelForProductAndPointOfService_NullProduct()
	{
		warehousingCommerceStockService.getStockLevelForProductAndPointOfService(null, pointOfService);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFail_GetStockLevelForProductAndPointOfService_NullPos()
	{
		warehousingCommerceStockService.getStockLevelForProductAndPointOfService(product, null);
	}

	@Test
	public void shouldGetStockLevelForProductAndPointOfService_NoWarehouses()
	{
		when(pickupWarehouseSelectionStrategy.getWarehouses(pointOfService)).thenReturn(Collections.emptyList());

		final Long value = warehousingCommerceStockService.getStockLevelForProductAndPointOfService(product, pointOfService);

		assertEquals(ZERO, value);
	}

	@Test
	public void shouldGetStockLevelForProductAndPointOfService_NullStock()
	{
		when(pickupWarehouseSelectionStrategy.getWarehouses(pointOfService)).thenReturn(warehouses);
		when(stockService.getStockLevels(product, warehouses)).thenReturn(null);

		final Long value = warehousingCommerceStockService.getStockLevelForProductAndPointOfService(product, pointOfService);

		assertEquals(ZERO, value);
	}

	@Test
	public void shouldGetStockLevelForProductAndPointOfService_NoStock()
	{
		when(pickupWarehouseSelectionStrategy.getWarehouses(pointOfService)).thenReturn(warehouses);
		when(stockService.getStockLevels(product, warehouses)).thenReturn(Collections.emptyList());

		final Long value = warehousingCommerceStockService.getStockLevelForProductAndPointOfService(product, pointOfService);

		assertEquals(ZERO, value);
	}

	@Test
	public void shouldGetStockLevelForProductAndPointOfService_NullAvailability()
	{
		when(pickupWarehouseSelectionStrategy.getWarehouses(pointOfService)).thenReturn(warehouses);
		when(stockService.getStockLevels(product, warehouses)).thenReturn(stockLevels);
		when(commerceAvailabilityCalculationStrategy.calculateAvailability(stockLevels)).thenReturn(null);

		final Long value = warehousingCommerceStockService.getStockLevelForProductAndPointOfService(product, pointOfService);

		assertEquals(null, value);
	}

	@Test
	public void shouldGetStockLevelForProductAndPointOfService_Valid()
	{
		when(pickupWarehouseSelectionStrategy.getWarehouses(pointOfService)).thenReturn(warehouses);
		when(stockService.getStockLevels(product, warehouses)).thenReturn(stockLevels);
		when(commerceAvailabilityCalculationStrategy.calculateAvailability(stockLevels)).thenReturn(
				Long.valueOf(stockLevel.getAvailable()));

		final Long value = warehousingCommerceStockService.getStockLevelForProductAndPointOfService(product, pointOfService);

		assertEquals(TEN, value);
	}
}
