/*
 *
 * [y] hybris Platform 
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 */
package de.hybris.platform.warehousing;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.warehousing.allocation.AllocationService;
import de.hybris.platform.warehousing.data.sourcing.SourcingResults;
import de.hybris.platform.warehousing.inventoryevent.service.InventoryEventService;
import de.hybris.platform.warehousing.model.AllocationEventModel;
import de.hybris.platform.warehousing.util.BaseSourcingIntegrationTest;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@IntegrationTest
public class PickUpSourcingEndToEndIntegrationTest extends BaseSourcingIntegrationTest
{
	@Resource
	private AllocationService allocationService;
	@Resource
	private InventoryEventService inventoryEventService;
	@Resource
	private CommerceStockService commerceStockService;

	private static final Long CAMERA_QTY = new Long(3);
	private static final Long MEMORYCARD_QTY = new Long(4);
	public static final String CODE_MONTREAL = "montreal";
	public static final String NAME_MONTREAL_DOWNTOWN = "montreal-downtown";

	@Before
	public void setup()
	{
	}

	/**
	 * Given an pickup order with 1 entries:<br>
	 * entry 1 : {quantity: 3, product: camera}<br>
	 * <p>
	 * Result:<br>
	 * It should source complete from 1 location, Montreal<br>
	 * <p>
	 */
	@Test
	public void shouldSource1ProductFromDesignatedPickupLocation()
	{
		// Given
		stockLevels.Camera(warehouses.Montreal(), 5);
		stockLevels.Camera(warehouses.Boston(), 2);

		// When create consignment
		final OrderModel order = orders.Camera_PickupInMontreal(CAMERA_QTY);
		final SourcingResults results = sourcingService.sourceOrder(order);

		//check sourcing result
		assertTrue(results.isComplete());


		final Collection<ConsignmentModel> consignmentResult = allocationService.createConsignments(order, "con", results);

		//then verify consignment
		assertTrue(consignmentResult.size() == 1);
		assertTrue(consignmentResult.stream().allMatch(result -> result.getStatus().getCode().equals("READY")));
		assertTrue(consignmentResult.stream().allMatch(
				result -> result.getWarehouse().getName().equals(CODE_MONTREAL)
						&& result.getDeliveryPointOfService().getName().equals(NAME_MONTREAL_DOWNTOWN)));
		assertTrue(consignmentResult.stream().allMatch(
				result -> result.getConsignmentEntries().stream().allMatch(e -> e.getQuantity().longValue() == 3)));

		//then verify the ATP
		assertEquals(Long.valueOf(4),
				commerceStockService.getStockLevelForProductAndBaseStore(products.Camera(), baseStores.NorthAmerica()));
		assertEquals(Long.valueOf(2),
				commerceStockService.getStockLevelForProductAndPointOfService(products.Camera(), pointsOfService.Montreal_Downtown()));

		//then verify allocation result (note: this will cause ATP twice)
		final Collection<AllocationEventModel> allocationResult = consignmentResult.stream()
				.map(result -> inventoryEventService.createAllocationEvents(result)).flatMap(result -> result.stream())
				.collect(Collectors.toList());
		assertTrue(allocationResult.size() == 1);
		assertTrue(allocationResult.stream().allMatch(
				result -> result.getStockLevel().getWarehouse().getName().equals(CODE_MONTREAL)));
		assertTrue(allocationResult.stream().allMatch(result -> result.getQuantity() == 3));
	}

	/**
	 * Given an pickup order with 2 entries:<br>
	 * entry 1 : {quantity: 3, product: camera}<br>
	 * entry 2 : {quantity: 3, product: memoryCard}<br>
	 * <p>
	 * Result:<br>
	 * It should source complete from 1 location, Montreal<br>
	 * <p>
	 */
	@Test
	public void shouldSource2ProductFromDesignatedPickupLocation()
	{
		// Given
		stockLevels.Camera(warehouses.Montreal(), 5);
		stockLevels.Camera(warehouses.Boston(), 6);
		stockLevels.MemoryCard(warehouses.Montreal(), 5);
		stockLevels.MemoryCard(warehouses.Boston(), 6);

		// When create consignment
		final OrderModel order = orders.CameraAndMemoryCard_PickupInMontreal(CAMERA_QTY, MEMORYCARD_QTY);
		final SourcingResults results = sourcingService.sourceOrder(order);

		//check sourcing result
		assertTrue(results.isComplete());

		final Collection<ConsignmentModel> consignmentResult = allocationService.createConsignments(order, "con", results);

		assertTrue(consignmentResult.size() == 1);
		assertTrue(consignmentResult.stream().allMatch(result -> result.getStatus().getCode().equals("READY")));
		assertTrue(consignmentResult.stream().allMatch(
				result -> result.getWarehouse().getCode().equals(CODE_MONTREAL)
						&& result.getDeliveryPointOfService().getName().equals(NAME_MONTREAL_DOWNTOWN)));

		assertTrue(consignmentResult.stream().allMatch(
				result -> result.getConsignmentEntries().stream().anyMatch(e -> e.getQuantity().longValue() == 3)));
		assertTrue(consignmentResult.stream().anyMatch(
				result -> result.getConsignmentEntries().stream().anyMatch(e -> e.getQuantity().longValue() == 4)));

		//then verify the ATP
		assertEquals(Long.valueOf(8),
				commerceStockService.getStockLevelForProductAndBaseStore(products.Camera(), baseStores.NorthAmerica()));
		assertEquals(Long.valueOf(2),
				commerceStockService.getStockLevelForProductAndPointOfService(products.Camera(), pointsOfService.Montreal_Downtown()));
		assertEquals(Long.valueOf(7),
				commerceStockService.getStockLevelForProductAndBaseStore(products.MemoryCard(), baseStores.NorthAmerica()));
		assertEquals(
				Long.valueOf(1),
				commerceStockService.getStockLevelForProductAndPointOfService(products.MemoryCard(),
						pointsOfService.Montreal_Downtown()));

		//then verify allocation result (note: this will cause ATP twice)
		final Collection<AllocationEventModel> allocationResult = consignmentResult.stream()
				.map(result -> inventoryEventService.createAllocationEvents(result)).flatMap(result -> result.stream())
				.collect(Collectors.toList());
		assertTrue(allocationResult.size() == 2);
		assertTrue(consignmentResult.stream().allMatch(result -> result.getStatus().getCode().equals("READY")));
		assertTrue(allocationResult.stream().allMatch(
				result -> result.getStockLevel().getWarehouse().getCode().equals(CODE_MONTREAL)));
		assertTrue(allocationResult.stream().anyMatch(result -> result.getQuantity() == 3));
		assertTrue(allocationResult.stream().anyMatch(result -> result.getQuantity() == 4));
	}
}
