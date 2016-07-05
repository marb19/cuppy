/*
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
package de.hybris.platform.yacceleratorordermanagement.integration;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.warehousing.process.BusinessProcessException;
import de.hybris.platform.warehousing.util.VerifyOrderAndConsignment;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@IntegrationTest
public class OrderSourcingIntegrationTest extends BaseAcceleratorSourcingIntegrationTest
{
	private final VerifyOrderAndConsignment verifyOrderAndConsignment = new VerifyOrderAndConsignment();
	private static final Logger LOG = Logger.getLogger(OrderSourcingIntegrationTest.class);

	@Before
	public void setUp() throws Exception
	{
		cleanUpData();
		if (order != null)
		{
			modelService.remove(order);
		}
	}

	@After
	public void cleanUp()
	{
		cleanUpData();
	}

	/**
	 * Given an shipping order with 1 entries:<br>
	 * entry 1 : {quantity: 3, product: camera}<br>
	 * <p>
	 * Result:<br>
	 * Verify the order status and consignment result<br>
	 * <p>
	 */
	@Test
	public void shouldSourcingSuccess_SingleEntry() throws InterruptedException
	{
		// Given
		stockLevels.Camera(warehouses.Montreal(), 6);

		order = sourcingUtil.createCameraShippedOrder();
		final OrderProcessModel orderProcessModel = sourcingUtil.runOrderProcessForOrderBasedPriority(order, OrderStatus.READY);

		//then verify the ATP
		assertEquals(Long.valueOf(3),
				commerceStockService.getStockLevelForProductAndBaseStore(products.Camera(), baseStores.NorthAmerica()));
		assertEquals(Long.valueOf(3), commerceStockService.getStockLevelForProductAndPointOfService(products.Camera(),
				pointsOfService.Montreal_Downtown()));

		//then verify the consignment
		modelService.refresh(order);
		assertEquals(order.getConsignments().size(), 1);
		assertTrue(order.getConsignments().stream().anyMatch(result -> result.getStatus().getCode().equals("READY")));
		assertEquals(Boolean.TRUE,
				verifyOrderAndConsignment.verifyConsignment_Camera(order, CODE_MONTREAL, Long.valueOf(0L), CAMERA_QTY, CAMERA_QTY));

		//when confirm shipment
		order.getConsignments().stream().forEach(e -> sourcingUtil.confirmDefaultConsignment(orderProcessModel, e));

		//verify the confirmed shipment
		assertTrue(order.getConsignments().stream().anyMatch(result -> result.getStatus().getCode().equals("SHIPPED")));
		assertTrue(order.getStatus().getCode().equals("COMPLETED"));
		sourcingUtil.refreshOrder(order);
		LOG.info("Quantity Pending: " + ((OrderEntryModel) order.getEntries().iterator().next()).getQuantityUnallocated());
		assertTrue(((OrderEntryModel) order.getEntries().iterator().next()).getQuantityPending().equals(Long.valueOf(0L)));
	}

	/**
	 * Given an pickup order with 1 entries:<br>
	 * entry 1 : {quantity: 3, product: camera}<br>
	 * <p>
	 * Result:<br>
	 * Verify the order status and consignment result<br>
	 * <p>
	 */
	@Test
	public void shouldSourcingSuccess_SingleEntry_PickUp() throws InterruptedException
	{
		// Given
		stockLevels.Camera(warehouses.Montreal(), 6);

		order = sourcingUtil.createCameraPickUpOrder();
		final OrderProcessModel orderProcessModel = sourcingUtil.runOrderProcessForOrderBasedPriority(order, OrderStatus.READY);

		//then verify the ATP
		assertEquals(Long.valueOf(3),
				commerceStockService.getStockLevelForProductAndBaseStore(products.Camera(), baseStores.NorthAmerica()));
		assertEquals(Long.valueOf(3), commerceStockService.getStockLevelForProductAndPointOfService(products.Camera(),
				pointsOfService.Montreal_Downtown()));

		//then verify the consignment
		modelService.refresh(order);
		assertEquals(order.getConsignments().size(), 1);
		assertTrue(order.getConsignments().stream().anyMatch(result -> result.getStatus().getCode().equals("READY")));
		assertEquals(Boolean.TRUE,
				verifyOrderAndConsignment.verifyConsignment_Camera(order, CODE_MONTREAL, Long.valueOf(0L), CAMERA_QTY, CAMERA_QTY));

		//when confirm shipment
		order.getConsignments().stream().forEach(e -> sourcingUtil.confirmDefaultConsignment(orderProcessModel, e));

		//verify the confirmed shipment
		assertTrue(order.getConsignments().stream().anyMatch(result -> result.getStatus().getCode().equals("SHIPPED")));
		assertTrue(order.getStatus().getCode().equals("COMPLETED"));
		sourcingUtil.refreshOrder(order);
		LOG.info("Quantity unallocated: " + ((OrderEntryModel) order.getEntries().iterator().next()).getQuantityUnallocated());
		assertTrue(((OrderEntryModel) order.getEntries().iterator().next()).getQuantityPending().equals(Long.valueOf(0L)));
	}

	/**
	 * Given an shipping order with 1 entries:<br>
	 * entry 1 : {quantity: 3, product: camera}<br>
	 * <p>
	 * Result:<br>
	 * Verify the order sourcing failed<br>
	 * <p>
	 */
	@Test
	public void shouldSourcingFail_SingleEntry() throws InterruptedException
	{
		// Given
		stockLevels.Camera(warehouses.Montreal(), 0);

		order = sourcingUtil.createCameraShippedOrder();
		sourcingUtil.runOrderProcessForOrderBasedPriority(order, OrderStatus.SUSPENDED);

		//then verify the consignment
		modelService.refresh(order);
		assertEquals(order.getConsignments().size(), 0);
	}

	/**
	 * Given an shipping order with 1 entries:<br>
	 * entry 1 : {quantity: 3, product: camera}<br>
	 * <p>
	 * Result:<br>
	 * Verify the order status and consignment result<br>
	 * <p>
	 */
	@Test
	public void shouldSourcingSuccess_Priority() throws InterruptedException
	{
		// Given
		stockLevels.Camera(warehouses.Montreal(), 6);
		stockLevels.Camera(warehouses.Boston(), 6);

		//when create order
		order = sourcingUtil.createCameraShippedOrder();
		LOG.info("Sourcing from allocation sourcing factor only");
		sourcingUtil.setSourcingFactors(0, 0, 100);
		warehouses.Montreal().setPriority(Integer.valueOf(1));
		warehouses.Boston().setPriority(Integer.valueOf(50));
		sourcingUtil.runDefaultOrderProcessForOrder(order, OrderStatus.READY);

		//then verify the result
		assertEquals(Long.valueOf(3), commerceStockService.getStockLevelForProductAndPointOfService(products.Camera(),
				pointsOfService.Montreal_Downtown()));
		assertEquals(Long.valueOf(6),
				commerceStockService.getStockLevelForProductAndPointOfService(products.Camera(), pointsOfService.Boston()));
	}

	/**
	 * Given an shipping order with 2 entries:<br>
	 * entry 1 : {quantity: 3, product: camera}<br>
	 * entry 2 : {quantity: 2, product: memoryCard}<br>
	 * <p>
	 * Result:<br>
	 * Verify the order status and consignment result<br>
	 * <p>
	 */
	@Test
	public void shouldSourcingSuccess_MultiEntry_SingleConsignment() throws InterruptedException
	{
		// Given
		stockLevels.Camera(warehouses.Montreal(), 6);
		stockLevels.MemoryCard(warehouses.Montreal(), 6);

		order = sourcingUtil.createCameraAndMemoryCardShippingOrder();
		final OrderProcessModel orderProcessModel = sourcingUtil.runOrderProcessForOrderBasedPriority(order, OrderStatus.READY);

		//then verify the ATP
		assertEquals(Long.valueOf(3),
				commerceStockService.getStockLevelForProductAndBaseStore(products.Camera(), baseStores.NorthAmerica()));
		assertEquals(Long.valueOf(3), commerceStockService.getStockLevelForProductAndPointOfService(products.Camera(),
				pointsOfService.Montreal_Downtown()));
		assertEquals(Long.valueOf(4), commerceStockService.getStockLevelForProductAndPointOfService(products.MemoryCard(),
				pointsOfService.Montreal_Downtown()));

		//then verify the consignment
		modelService.refresh(order);
		assertEquals(order.getConsignments().size(), 1);
		assertTrue(order.getConsignments().stream().allMatch(result -> result.getStatus().getCode().equals("READY")));
		assertEquals(Boolean.TRUE, verifyOrderAndConsignment.verifyConsignment_Camera_MemoryCard(order, CODE_MONTREAL,
				Long.valueOf(0L), CAMERA_QTY, CAMERA_QTY, Long.valueOf(0L), MEMORY_CARD_QTY, MEMORY_CARD_QTY));

		//when confirm shipment
		order.getConsignments().stream().forEach(e -> sourcingUtil.confirmDefaultConsignment(orderProcessModel, e));

		//verify the confirmed shipment
		assertTrue(order.getConsignments().stream().allMatch(result -> result.getStatus().getCode().equals("SHIPPED")));
		assertTrue(order.getStatus().getCode().equals("COMPLETED"));
		sourcingUtil.refreshOrder(order);
		LOG.info("Quantity Pending: " + ((OrderEntryModel) order.getEntries().iterator().next()).getQuantityPending());
		assertTrue(order.getEntries().stream().allMatch(e -> ((OrderEntryModel) e).getQuantityPending().equals(Long.valueOf(0L))));
	}

	/**
	 * Given an shipping order with 2 entries:<br>
	 * entry 1 : {quantity: 3, product: camera}<br>
	 * entry 2 : {quantity: 2, product: memoryCard}<br>
	 * <p>
	 * Result:<br>
	 * Verify the order status and consignment result<br>
	 * <p>
	 */
	@Test
	public void shouldSourcingSuccess_MultiEntries_MultiConsignments() throws InterruptedException
	{
		// Given
		stockLevels.Camera(warehouses.Montreal(), 6);
		stockLevels.MemoryCard(warehouses.Boston(), 6);

		order = sourcingUtil.createCameraAndMemoryCardShippingOrder();
		final OrderProcessModel orderProcessModel = sourcingUtil.runOrderProcessForOrderBasedPriority(order, OrderStatus.READY);

		//then verify the ATP
		assertEquals(Long.valueOf(3),
				commerceStockService.getStockLevelForProductAndBaseStore(products.Camera(), baseStores.NorthAmerica()));
		assertEquals(Long.valueOf(3), commerceStockService.getStockLevelForProductAndPointOfService(products.Camera(),
				pointsOfService.Montreal_Downtown()));
		assertEquals(Long.valueOf(4),
				commerceStockService.getStockLevelForProductAndPointOfService(products.MemoryCard(), pointsOfService.Boston()));

		//then verify the consignment
		modelService.refresh(order);
		assertEquals(order.getConsignments().size(), 2);
		assertTrue(order.getConsignments().stream().allMatch(result -> result.getStatus().getCode().equals("READY")));
		assertEquals(Boolean.TRUE,
				verifyOrderAndConsignment.verifyConsignment_Camera(order, CODE_MONTREAL, Long.valueOf(0L), CAMERA_QTY, CAMERA_QTY));
		assertEquals(Boolean.TRUE, verifyOrderAndConsignment.verifyConsignment(order, CODE_MEMORY_CARD, CODE_BOSTON,
				Long.valueOf(0L), MEMORY_CARD_QTY, MEMORY_CARD_QTY));

		//when confirm first shipment
		sourcingUtil.confirmDefaultConsignment(orderProcessModel,
				order.getConsignments().stream().filter(e -> e.getWarehouse().getCode().equals(CODE_MONTREAL)).findFirst().get());

		//verify the order status
		assertTrue(order.getStatus().getCode().equals("READY"));

		//when confirm second consignment
		sourcingUtil.confirmDefaultConsignment(orderProcessModel,
				order.getConsignments().stream().filter(e -> e.getWarehouse().getCode().equals(CODE_BOSTON)).findFirst().get());

		//verify the order
		assertTrue(order.getConsignments().stream().allMatch(result -> result.getStatus().getCode().equals("SHIPPED")));
		sourcingUtil.refreshOrder(order);
		LOG.info("Quantity Pending: " + ((OrderEntryModel) order.getEntries().iterator().next()).getQuantityPending());
		assertTrue(order.getEntries().stream().allMatch(e -> ((OrderEntryModel) e).getQuantityPending().equals(Long.valueOf(0L))));
		assertTrue(order.getStatus().getCode().equals("COMPLETED"));
	}

	/**
	 * Given an shipping order with 2 entries:<br>
	 * entry 1 : {quantity: 3, product: camera}<br>
	 * entry 2 : {quantity: 2, product: memoryCard}<br>
	 * <p>
	 * Result:<br>
	 * Verify the order status and consignment result<br>
	 * <p>
	 */
	@Test
	public void shouldSourcingSuccess_MultiEntries_MultiConsignments_SplitOrderEntries() throws InterruptedException
	{
		// Given
		stockLevels.Camera(warehouses.Montreal(), 6);
		stockLevels.MemoryCard(warehouses.Boston(), 6);
		stockLevels.MemoryCard(warehouses.Montreal(), 1);

		order = sourcingUtil.createCameraAndMemoryCardShippingOrder();
		final OrderProcessModel orderProcessModel = sourcingUtil.runOrderProcessForOrderBasedPriority(order, OrderStatus.READY);

		//then verify the ATP
		assertEquals(Long.valueOf(3),
				commerceStockService.getStockLevelForProductAndBaseStore(products.Camera(), baseStores.NorthAmerica()));
		assertEquals(Long.valueOf(3), commerceStockService.getStockLevelForProductAndPointOfService(products.Camera(),
				pointsOfService.Montreal_Downtown()));
		assertEquals(Long.valueOf(5),
				commerceStockService.getStockLevelForProductAndPointOfService(products.MemoryCard(), pointsOfService.Boston()));

		//then verify the consignment
		modelService.refresh(order);
		assertEquals(order.getConsignments().size(), 2);
		assertTrue(order.getConsignments().stream().allMatch(result -> result.getStatus().getCode().equals("READY")));
		assertEquals(Boolean.TRUE, verifyOrderAndConsignment.verifyConsignment_Camera_MemoryCard(order, CODE_MONTREAL,
				Long.valueOf(0L), CAMERA_QTY, CAMERA_QTY, Long.valueOf(0L), Long.valueOf(1L), Long.valueOf(1L)));
		assertEquals(Boolean.TRUE, verifyOrderAndConsignment.verifyConsignment(order, CODE_MEMORY_CARD, CODE_BOSTON,
				Long.valueOf(0L), Long.valueOf(1L), Long.valueOf(1L)));

		//when confirm first shipment
		sourcingUtil.confirmDefaultConsignment(orderProcessModel,
				order.getConsignments().stream().filter(e -> e.getWarehouse().getCode().equals(CODE_MONTREAL)).findFirst().get());

		//verify the order status
		assertTrue(order.getStatus().getCode().equals("READY"));

		//when confirm second consignment
		sourcingUtil.confirmDefaultConsignment(orderProcessModel,
				order.getConsignments().stream().filter(e -> e.getWarehouse().getCode().equals(CODE_BOSTON)).findFirst().get());

		//verify the order
		assertTrue(order.getConsignments().stream().allMatch(result -> result.getStatus().getCode().equals("SHIPPED")));
		sourcingUtil.refreshOrder(order);
		LOG.info("Quantity Pending: " + ((OrderEntryModel) order.getEntries().iterator().next()).getQuantityPending());
		assertTrue(order.getEntries().stream().allMatch(e -> ((OrderEntryModel) e).getQuantityPending().equals(Long.valueOf(0L))));
		assertTrue(order.getStatus().getCode().equals("COMPLETED"));
	}

	/**
	 * Given an order with 3 entries:<br>
	 * entry 1 : {quantity: 1, product: product1} <br>
	 * entry 2 : {quantity: 1, product: product2} <br>
	 * entry 3 : {quantity: 7, product: product3} <br>
	 * <p>
	 * POS Montreal ->Montreal product1 quantity=6, product2 quantity =6, product3 quantity = 6 POS Boston ->Boston
	 * product1 quantity=6, product2 quantity =6, product3 quantity = 0 POS Toronto ->Toronto product1 quantity=0,
	 * product2 quantity =0, product3 quantity = 6
	 * <p>
	 * Result:<br>
	 * It should source complete from 2 location, 6 of product3 from Montreal, and 1 from the Toronto<br>
	 */
	@Test
	public void shouldSourcingSuccess_OMSE_640() throws InterruptedException
	{
		// Given
		stockLevels.Camera(warehouses.Montreal(), 6);
		stockLevels.MemoryCard(warehouses.Montreal(), 6);
		stockLevels.Lens(warehouses.Montreal(), 6);

		stockLevels.Camera(warehouses.Toronto(), 0);
		stockLevels.MemoryCard(warehouses.Toronto(), 0);
		stockLevels.Lens(warehouses.Toronto(), 6);

		stockLevels.Camera(warehouses.Boston(), 6);
		stockLevels.MemoryCard(warehouses.Boston(), 3);
		stockLevels.Lens(warehouses.Boston(), 0);


		order = sourcingUtil
				.createOrder(orders.CameraAndMemoryCardAndLens_Shipped(Long.valueOf(1L), Long.valueOf(1L), Long.valueOf(7L)));
		final OrderProcessModel orderProcessModel = sourcingUtil.runOrderProcessForOrderBasedPriority(order, OrderStatus.READY);

		//then verify the consignment
		modelService.refresh(order);
		assertEquals(order.getConsignments().size(), 2);
		assertTrue(order.getConsignments().stream().anyMatch(result -> result.getStatus().getCode().equals("READY")));
		assertEquals(Boolean.TRUE, verifyOrderAndConsignment.verifyConsignment_Camera_MemoryCard(order, CODE_MONTREAL,
				Long.valueOf(0L), Long.valueOf(1L), Long.valueOf(1L), Long.valueOf(0L), Long.valueOf(1L), Long.valueOf(1L)));
		assertEquals(Boolean.TRUE, verifyOrderAndConsignment.verifyConsignment(order, LENS_CODE, CODE_TORONTO, Long.valueOf(0L),
				Long.valueOf(1L), Long.valueOf(1L)));
		//when confirm shipment
		order.getConsignments().stream().forEach(e -> sourcingUtil.confirmDefaultConsignment(orderProcessModel, e));

		//verify the confirmed shipment
		assertTrue(order.getConsignments().stream().anyMatch(result -> result.getStatus().getCode().equals("SHIPPED")));
		assertTrue(order.getStatus().getCode().equals("COMPLETED"));
		sourcingUtil.refreshOrder(order);
		LOG.info("Quantity Pending: " + ((OrderEntryModel) order.getEntries().iterator().next()).getQuantityUnallocated());
		assertTrue(((OrderEntryModel) order.getEntries().iterator().next()).getQuantityPending().equals(Long.valueOf(0L)));

		//then verify the ATP
		assertEquals(Long.valueOf(5), commerceStockService.getStockLevelForProductAndPointOfService(products.Camera(),
				pointsOfService.Montreal_Downtown()));
		assertEquals(Long.valueOf(5), commerceStockService.getStockLevelForProductAndPointOfService(products.MemoryCard(),
				pointsOfService.Montreal_Downtown()));
		assertEquals(Long.valueOf(0),
				commerceStockService.getStockLevelForProductAndPointOfService(products.Lens(), pointsOfService.Montreal_Downtown()));
	}

	/**
	 * Given an shipping order with 1 entries:<br>
	 * entry 1 : {quantity: 3, product: camera}<br>
	 * <p>
	 * Result:<br>
	 * Verify cannot confirm order twice<br>
	 * <p>
	 */
	@Test(expected = BusinessProcessException.class)
	public void shouldNotConfirmTwice() throws InterruptedException
	{
		// Given
		stockLevels.Camera(warehouses.Montreal(), 6);

		order = sourcingUtil.createCameraShippedOrder();
		final OrderProcessModel orderProcessModel = sourcingUtil.runOrderProcessForOrderBasedPriority(order, OrderStatus.READY);

		//when confirm shipment twice
		order.getConsignments().stream().forEach(e -> sourcingUtil.confirmDefaultConsignment(orderProcessModel, e));
		sourcingUtil.refreshOrder(order);
		order.getConsignments().stream().forEach(e -> sourcingUtil.confirmDefaultConsignment(orderProcessModel, e));
	}
}
