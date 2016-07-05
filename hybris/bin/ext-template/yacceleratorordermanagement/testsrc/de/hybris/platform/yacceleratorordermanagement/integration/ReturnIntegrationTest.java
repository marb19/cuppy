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
import de.hybris.platform.basecommerce.enums.RefundReason;
import de.hybris.platform.basecommerce.enums.ReturnAction;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.warehousing.process.BusinessProcessException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;


/**
 * This integration test creates an order process model and kick-start it with underlying consignment process from
 * beginning to the end. Afterwards it create a return process and verifies if the process has progressed successfully.
 * Please make sure that you have run initialize and update junit tenant before running this test.
 */
@IntegrationTest
public class ReturnIntegrationTest extends BaseAcceleratorSourcingIntegrationTest
{

	private static final String RETURN_TEST_PROCESS = "return-process";
	private static final String RETURN_PROCESS_DEFINITION_NAME = "return-process";

	private static final String SHIP_CONSIGNMENT_CHOICE = "confirmShipConsignment";
	private static final String APPROVE_REFUND_CHOICE = "approveReturn";

	private static final String CONFIRM_REFUND_EVENT_NAME = "ConfirmOrCancelRefundEvent";
	private static final String ACCEPT_GOODS_CHOICE = "acceptGoods";
	private static final String APPROVE_CANCEL_GOODS_EVENT_NAME = "ApproveOrCancelGoodsEvent";
	private static final String CAPTURE_REFUND_CHOICE = "captureRefund";

	private static final Long CAMERA_QTY = new Long(1L);

	private final static int timeOut = 40; //seconds

	private List<PaymentTransactionModel> paymentTransactionList;
	private Map<AbstractOrderEntryModel, Long> refundMap;

	@Before
	public void setUp() throws Exception
	{
		cleanUpData();
		if (order != null)
		{
			modelService.remove(order);
		}
		refundMap = new HashMap<>();
	}

	@After
	public void cleanUp()
	{
		cleanUpData();
	}

	@Test
	public void shouldReturnSuccess_SingleEntry_ReturnInStore() throws RetryLaterException, InterruptedException
	{
		//when
		createOrderWithOneEntry_ShippingOrder();
		ReturnRequestModel returnRequest = returnUtil.createDefaultReturnRequest(order, 3L, ReturnAction.IMMEDIATE,
				RefundReason.DAMAGEDINTRANSIT, BigDecimal.valueOf(20), order.getEntries().get(0));
		returnUtil.runDefaultReturnProcessForOrder(returnRequest, ReturnStatus.COMPLETED);
		sourcingUtil.waitUntilReturnProcessIsNotRunning(returnRequest, timeOut);
		//then
		assertEquals(returnRequest.getStatus(), ReturnStatus.COMPLETED);
		assertEquals(returnRequest.getRefundDeliveryCost(), false);
		assertEquals(returnRequest.getOrder(), order);
		returnRequest.getReturnEntries().stream().forEach(e -> ((RefundEntryModel) e).getAmount().equals(BigDecimal.valueOf(20)));
		assertEquals(((OrderEntryModel) order.getEntries().get(0)).getQuantityReturned().longValue(), 3L);
	}

	@Test
	public void shouldReturnSuccess_SingleEntry_ReturnOnline() throws RetryLaterException, InterruptedException
	{
		//when create return
		createOrderWithOneEntry_ShippingOrder();
		ReturnRequestModel returnRequest = returnUtil.createDefaultReturnRequest(order, 3L, ReturnAction.HOLD,
				RefundReason.DAMAGEDINTRANSIT, BigDecimal.valueOf(20), order.getEntries().get(0));
		returnUtil.runDefaultReturnProcessForOrder(returnRequest, ReturnStatus.APPROVAL_PENDING);
		sourcingUtil.waitUntilReturnProcessIsNotRunning(returnRequest, timeOut);
		//then verify return creation
		assertEquals(returnRequest.getRefundDeliveryCost(), false);
		assertEquals(returnRequest.getOrder(), order);
		returnRequest.getReturnEntries().stream().forEach(e -> ((RefundEntryModel) e).getAmount().equals(BigDecimal.valueOf(20)));
		assertEquals(returnRequest.getStatus(), ReturnStatus.APPROVAL_PENDING);

		//when approve return
		returnUtil.approveDefaultReturn(returnRequest);
		//then verify the status
		assertEquals(returnRequest.getStatus(), ReturnStatus.WAIT);
		//when confirm receive goods
		returnUtil.confirmWaitForGoodsDefaultReturn(returnRequest);
		//then verify the status
		assertEquals(returnRequest.getStatus(), ReturnStatus.COMPLETED);
		sourcingUtil.refreshOrder(order);
		assertEquals(3L, ((OrderEntryModel) order.getEntries().get(0)).getQuantityReturned().longValue());
	}

	@Test
	public void shouldReturnSuccess_SingleEntry_PickUpOrder() throws RetryLaterException, InterruptedException
	{
		//when
		createOrderWithOneEntry_PickUpOrder();
		ReturnRequestModel returnRequest = returnUtil.createDefaultReturnRequest(order, 3L, ReturnAction.IMMEDIATE,
				RefundReason.DAMAGEDINTRANSIT, BigDecimal.valueOf(20), order.getEntries().get(0));
		returnUtil.runDefaultReturnProcessForOrder(returnRequest, ReturnStatus.COMPLETED);
		sourcingUtil.waitUntilReturnProcessIsNotRunning(returnRequest, timeOut);
		//then
		assertEquals(returnRequest.getStatus(), ReturnStatus.COMPLETED);
		assertEquals(returnRequest.getRefundDeliveryCost(), false);
		assertEquals(returnRequest.getOrder(), order);
		returnRequest.getReturnEntries().stream().forEach(e -> ((RefundEntryModel) e).getAmount().equals(BigDecimal.valueOf(20)));
		assertEquals(((OrderEntryModel) order.getEntries().get(0)).getQuantityReturned().longValue(), 3L);
	}

	@Test
	public void shouldReturnSuccess_SingleEntry_CreateTwoReturns() throws RetryLaterException, InterruptedException
	{
		//when
		createOrderWithOneEntry_PickUpOrder();
		ReturnRequestModel returnRequest = returnUtil.createDefaultReturnRequest(order, 2L, ReturnAction.IMMEDIATE,
				RefundReason.DAMAGEDINTRANSIT, BigDecimal.valueOf(20), order.getEntries().get(0));
		returnUtil.runDefaultReturnProcessForOrder(returnRequest, ReturnStatus.COMPLETED);
		sourcingUtil.waitUntilReturnProcessIsNotRunning(returnRequest, timeOut);
		//then
		assertEquals(returnRequest.getStatus(), ReturnStatus.COMPLETED);
		assertEquals(returnRequest.getRefundDeliveryCost(), false);
		assertEquals(returnRequest.getOrder(), order);
		returnRequest.getReturnEntries().stream().forEach(e -> ((RefundEntryModel) e).getAmount().equals(BigDecimal.valueOf(20)));
		assertEquals(((OrderEntryModel) order.getEntries().get(0)).getQuantityReturned().longValue(), 2L);

		//when return the rest quantity with deliver Cost
		ReturnRequestModel returnRequest2 = returnUtil.createDefaultReturnRequest(order, 1L, ReturnAction.IMMEDIATE,
				RefundReason.DAMAGEDINTRANSIT, BigDecimal.valueOf(4), order.getEntries().get(0));
		returnRequest2.setRefundDeliveryCost(true);
		modelService.save(returnRequest2);
		returnUtil.runDefaultReturnProcessForOrder(returnRequest2, ReturnStatus.COMPLETED);
		sourcingUtil.waitUntilReturnProcessIsNotRunning(returnRequest2, timeOut);

		//then
		assertEquals(returnRequest2.getStatus(), ReturnStatus.COMPLETED);
		assertEquals(returnRequest2.getRefundDeliveryCost(), true);
		assertEquals(returnRequest2.getOrder(), order);
		returnRequest2.getReturnEntries().stream().forEach(e -> ((RefundEntryModel) e).getAmount().equals(BigDecimal.valueOf(4)));
		assertEquals(((OrderEntryModel) order.getEntries().get(0)).getQuantityReturned().longValue(), 3L);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldReturnFail_SingleEntry_SecondAttemptExpectedQuantityTooHigh_InStore()
			throws RetryLaterException, InterruptedException
	{
		//when
		createOrderWithOneEntry_PickUpOrder();
		ReturnRequestModel returnRequest = returnUtil.createDefaultReturnRequest(order, 2L, ReturnAction.IMMEDIATE,
				RefundReason.DAMAGEDINTRANSIT, BigDecimal.valueOf(20), order.getEntries().get(0));
		returnUtil.runDefaultReturnProcessForOrder(returnRequest, ReturnStatus.COMPLETED);
		sourcingUtil.waitUntilReturnProcessIsNotRunning(returnRequest, timeOut);
		//then
		assertEquals(returnRequest.getStatus(), ReturnStatus.COMPLETED);
		assertEquals(returnRequest.getRefundDeliveryCost(), false);
		assertEquals(returnRequest.getOrder(), order);
		returnRequest.getReturnEntries().stream().forEach(e -> ((RefundEntryModel) e).getAmount().equals(BigDecimal.valueOf(20)));
		assertEquals(((OrderEntryModel) order.getEntries().get(0)).getQuantityReturned().longValue(), 2L);

		//when return the rest quantity with deliver Cost
		ReturnRequestModel returnRequest2 = returnUtil.createDefaultReturnRequest(order, 2L, ReturnAction.IMMEDIATE,
				RefundReason.DAMAGEDINTRANSIT, BigDecimal.valueOf(4), order.getEntries().get(0));
		returnRequest2.setRefundDeliveryCost(true);
		modelService.save(returnRequest2);
		returnUtil.runDefaultReturnProcessForOrder(returnRequest2, ReturnStatus.COMPLETED);
		sourcingUtil.waitUntilReturnProcessIsNotRunning(returnRequest2, timeOut);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldReturnFail_SingleEntry_SecondAttemptExpectedQuantityTooHigh_OnLine_PendingReturnExists()
			throws RetryLaterException, InterruptedException
	{
		//when
		createOrderWithOneEntry_PickUpOrder();
		ReturnRequestModel returnRequest = returnUtil.createDefaultReturnRequest(order, 2L, ReturnAction.HOLD,
				RefundReason.DAMAGEDINTRANSIT, BigDecimal.valueOf(20), order.getEntries().get(0));
		returnUtil.runDefaultReturnProcessForOrder(returnRequest, ReturnStatus.APPROVAL_PENDING);
		sourcingUtil.waitUntilReturnProcessIsNotRunning(returnRequest, timeOut);
		//then
		assertEquals(returnRequest.getStatus(), ReturnStatus.APPROVAL_PENDING);
		assertEquals(returnRequest.getRefundDeliveryCost(), false);
		assertEquals(returnRequest.getOrder(), order);
		returnRequest.getReturnEntries().stream().forEach(e -> ((RefundEntryModel) e).getAmount().equals(BigDecimal.valueOf(20)));

		//when return the rest quantity with deliver Cost
		ReturnRequestModel returnRequest2 = returnUtil.createDefaultReturnRequest(order, 2L, ReturnAction.IMMEDIATE,
				RefundReason.DAMAGEDINTRANSIT, BigDecimal.valueOf(4), order.getEntries().get(0));
		returnRequest2.setRefundDeliveryCost(true);
		modelService.save(returnRequest2);
		returnUtil.runDefaultReturnProcessForOrder(returnRequest2, ReturnStatus.COMPLETED);
		sourcingUtil.waitUntilReturnProcessIsNotRunning(returnRequest2, timeOut);
	}

	@Test
	public void shouldCancelReturnSuccess_AfterApproval_ReCreateReturnSuccess() throws RetryLaterException, InterruptedException
	{
		//when create return
		createOrderWithOneEntry_ShippingOrder();
		ReturnRequestModel returnRequest = returnUtil.createDefaultReturnRequest(order, 3L, ReturnAction.HOLD,
				RefundReason.DAMAGEDINTRANSIT, BigDecimal.valueOf(20), order.getEntries().get(0));
		returnUtil.runDefaultReturnProcessForOrder(returnRequest, ReturnStatus.APPROVAL_PENDING);
		sourcingUtil.waitUntilReturnProcessIsNotRunning(returnRequest, timeOut);
		//then verify return creation
		assertEquals(returnRequest.getRefundDeliveryCost(), false);
		assertEquals(returnRequest.getOrder(), order);
		returnRequest.getReturnEntries().stream().forEach(e -> ((RefundEntryModel) e).getAmount().equals(BigDecimal.valueOf(20)));
		assertEquals(returnRequest.getStatus(), ReturnStatus.APPROVAL_PENDING);

		//when approve return
		returnUtil.approveDefaultReturn(returnRequest);
		//then verify the status
		assertEquals(returnRequest.getStatus(), ReturnStatus.WAIT);
		//when cancel return
		returnUtil.cancelDefaultReturn_AfterApproval(returnRequest);
		//then verify the status
		assertEquals(returnRequest.getStatus(), ReturnStatus.CANCELED);
		assertEquals(0L, ((OrderEntryModel) order.getEntries().get(0)).getQuantityReturned().longValue());

		//when return the rest quantity with deliver Cost
		ReturnRequestModel returnRequest2 = returnUtil.createDefaultReturnRequest(order, 3L, ReturnAction.IMMEDIATE,
				RefundReason.DAMAGEDINTRANSIT, BigDecimal.valueOf(20), order.getEntries().get(0));
		returnRequest2.setRefundDeliveryCost(true);
		modelService.save(returnRequest2);
		returnUtil.runDefaultReturnProcessForOrder(returnRequest2, ReturnStatus.COMPLETED);
		sourcingUtil.waitUntilReturnProcessIsNotRunning(returnRequest2, timeOut);

		//then
		assertEquals(returnRequest2.getStatus(), ReturnStatus.COMPLETED);
		assertEquals(returnRequest2.getRefundDeliveryCost(), true);
		assertEquals(returnRequest2.getOrder(), order);
		returnRequest2.getReturnEntries().stream().forEach(e -> ((RefundEntryModel) e).getAmount().equals(BigDecimal.valueOf(14)));
		assertEquals(3L, ((OrderEntryModel) order.getEntries().get(0)).getQuantityReturned().longValue());
	}

	@Test
	public void shouldCancelReturnSuccess_AfterCreation() throws RetryLaterException, InterruptedException
	{
		//when create return
		createOrderWithOneEntry_ShippingOrder();
		ReturnRequestModel returnRequest = returnUtil.createDefaultReturnRequest(order, 3L, ReturnAction.HOLD,
				RefundReason.DAMAGEDINTRANSIT, BigDecimal.valueOf(20), order.getEntries().get(0));
		returnUtil.runDefaultReturnProcessForOrder(returnRequest, ReturnStatus.APPROVAL_PENDING);
		sourcingUtil.waitUntilReturnProcessIsNotRunning(returnRequest, timeOut);

		//when cancel return
		returnUtil.cancelDefaultReturn_AfterCreation(returnRequest);
		//then verify the status
		assertEquals(returnRequest.getStatus(), ReturnStatus.CANCELED);
		assertEquals(0L, ((OrderEntryModel) order.getEntries().get(0)).getQuantityReturned().longValue());
	}

	@Test
	public void shouldCancelReturnSuccess_AfterReturnCreation() throws RetryLaterException, InterruptedException
	{
		//when create return
		createOrderWithOneEntry_ShippingOrder();
		ReturnRequestModel returnRequest = returnUtil.createDefaultReturnRequest(order, 3L, ReturnAction.HOLD,
				RefundReason.DAMAGEDINTRANSIT, BigDecimal.valueOf(20), order.getEntries().get(0));
		returnUtil.runDefaultReturnProcessForOrder(returnRequest, ReturnStatus.APPROVAL_PENDING);
		sourcingUtil.waitUntilReturnProcessIsNotRunning(returnRequest, timeOut);
		//then verify return creation
		assertEquals(returnRequest.getRefundDeliveryCost(), false);
		assertEquals(returnRequest.getOrder(), order);
		returnRequest.getReturnEntries().stream().forEach(e -> ((RefundEntryModel) e).getAmount().equals(BigDecimal.valueOf(20)));
		assertEquals(returnRequest.getStatus(), ReturnStatus.APPROVAL_PENDING);

		//when cancel return
		returnUtil.cancelDefaultReturn_AfterCreation(returnRequest);
		//then verify the status
		assertEquals(returnRequest.getStatus(), ReturnStatus.CANCELED);
		assertEquals(0L, ((OrderEntryModel) order.getEntries().get(0)).getQuantityReturned().longValue());
	}

	@Test(expected = BusinessProcessException.class)
	public void shouldCancelReturnFail_AfterReturnCompleted() throws RetryLaterException, InterruptedException
	{
		//when create return
		createOrderWithOneEntry_ShippingOrder();
		ReturnRequestModel returnRequest = returnUtil.createDefaultReturnRequest(order, 3L, ReturnAction.IMMEDIATE,
				RefundReason.DAMAGEDINTRANSIT, BigDecimal.valueOf(20), order.getEntries().get(0));
		returnUtil.runDefaultReturnProcessForOrder(returnRequest, ReturnStatus.COMPLETED);
		sourcingUtil.waitUntilReturnProcessIsNotRunning(returnRequest, timeOut);

		//when cancel return
		returnUtil.cancelDefaultReturn_AfterCreation(returnRequest);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldReturnFail_SingleEntry_ExpectedQuantityTooHigh() throws RetryLaterException, InterruptedException
	{
		//when
		createOrderWithOneEntry_PickUpOrder();
		ReturnRequestModel returnRequest = returnUtil.createDefaultReturnRequest(order, 4L, ReturnAction.IMMEDIATE,
				RefundReason.DAMAGEDINTRANSIT, BigDecimal.valueOf(20), order.getEntries().get(0));
		returnUtil.runDefaultReturnProcessForOrder(returnRequest, ReturnStatus.COMPLETED);
		sourcingUtil.waitUntilReturnProcessIsNotRunning(returnRequest, timeOut);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldReturnFail_OrderNotShipped() throws RetryLaterException, InterruptedException
	{
		//when
		stockLevels.Camera(warehouses.Montreal(), 3);
		order = sourcingUtil.createCameraShippedOrder();
		modelService.save(order);
		order.setPaymentTransactions(sourcingUtil.setDummyOrderTransaction(order));
		final OrderProcessModel orderProcessModel = sourcingUtil.runOrderProcessForOrderBasedPriority(order, OrderStatus.READY);
		ReturnRequestModel returnRequest = returnUtil.createDefaultReturnRequest(order, 4L, ReturnAction.IMMEDIATE,
				RefundReason.DAMAGEDINTRANSIT, BigDecimal.valueOf(20), order.getEntries().get(0));
		returnUtil.runDefaultReturnProcessForOrder(returnRequest, ReturnStatus.COMPLETED);
		sourcingUtil.waitUntilReturnProcessIsNotRunning(returnRequest, timeOut);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldReturnSuccess_SingleEntry_OrderPartiallyShipped_ExpectedQuantityOverShipped()
			throws RetryLaterException, InterruptedException
	{
		//when
		OrderProcessModel orderProcessModel = createOrderWithOneEntry_MultiConsignments();
		order.getConsignments().stream().filter(e -> e.getWarehouse().getCode().equals(CODE_MONTREAL))
				.forEach(e -> sourcingUtil.confirmDefaultConsignment(orderProcessModel, e));

		ReturnRequestModel returnRequest = returnUtil.createDefaultReturnRequest(order, 3L, ReturnAction.IMMEDIATE,
				RefundReason.DAMAGEDINTRANSIT, BigDecimal.valueOf(20), order.getEntries().get(0));
		returnUtil.runDefaultReturnProcessForOrder(returnRequest, ReturnStatus.COMPLETED);
		sourcingUtil.waitUntilReturnProcessIsNotRunning(returnRequest, timeOut);
	}

	@Test
	public void shouldReturnSuccess_SingleEntry_OrderPartiallyShipped() throws RetryLaterException, InterruptedException
	{
		//when
		OrderProcessModel orderProcessModel = createOrderWithOneEntry_MultiConsignments();
		order.getConsignments().stream().filter(e -> e.getWarehouse().getCode().equals(CODE_MONTREAL))
				.forEach(e -> sourcingUtil.confirmDefaultConsignment(orderProcessModel, e));

		ReturnRequestModel returnRequest = returnUtil.createDefaultReturnRequest(order, 2L, ReturnAction.IMMEDIATE,
				RefundReason.DAMAGEDINTRANSIT, BigDecimal.valueOf(20), order.getEntries().get(0));
		returnUtil.runDefaultReturnProcessForOrder(returnRequest, ReturnStatus.COMPLETED);
		sourcingUtil.waitUntilReturnProcessIsNotRunning(returnRequest, timeOut);
		//then
		assertEquals(returnRequest.getStatus(), ReturnStatus.COMPLETED);
		assertEquals(returnRequest.getRefundDeliveryCost(), false);
		assertEquals(returnRequest.getOrder(), order);
		returnRequest.getReturnEntries().stream().forEach(e -> ((RefundEntryModel) e).getAmount().equals(BigDecimal.valueOf(20)));
		assertEquals(((OrderEntryModel) order.getEntries().get(0)).getQuantityReturned().longValue(), 2L);
	}

	@Ignore //TODO OMSE-1256
	@Test(expected = IllegalArgumentException.class)
	public void shouldReturnFail_DeliverCostDuplicated() throws RetryLaterException, InterruptedException
	{
		//when
		createOrderWithOneEntry_ShippingOrder();
		ReturnRequestModel returnRequest = returnUtil.createDefaultReturnRequest(order, 2L, ReturnAction.IMMEDIATE,
				RefundReason.DAMAGEDINTRANSIT, BigDecimal.valueOf(20), order.getEntries().get(0));
		returnRequest.setRefundDeliveryCost(true);
		modelService.save(returnRequest);
		returnUtil.runDefaultReturnProcessForOrder(returnRequest, ReturnStatus.COMPLETED);
		sourcingUtil.waitUntilReturnProcessIsNotRunning(returnRequest, timeOut);
		//then
		assertEquals(returnRequest.getStatus(), ReturnStatus.COMPLETED);
		assertEquals(returnRequest.getRefundDeliveryCost(), true);
		assertEquals(returnRequest.getOrder(), order);
		returnRequest.getReturnEntries().stream().forEach(e -> ((RefundEntryModel) e).getAmount().equals(BigDecimal.valueOf(20)));

		//when return the rest quantity with deliver Cost
		ReturnRequestModel returnRequest2 = returnUtil.createDefaultReturnRequest(order, 1L, ReturnAction.IMMEDIATE,
				RefundReason.DAMAGEDINTRANSIT, BigDecimal.valueOf(4), order.getEntries().get(0));
		returnRequest2.setRefundDeliveryCost(true);
		modelService.save(returnRequest2);
		returnUtil.runDefaultReturnProcessForOrder(returnRequest2, ReturnStatus.COMPLETED);
		sourcingUtil.waitUntilReturnProcessIsNotRunning(returnRequest2, timeOut);
	}

	@Test
	public void shouldReturnSuccess_MultiEntry_ReturnOnline() throws RetryLaterException, InterruptedException
	{
		//when create return
		createOrderWithMultiEntry_ShippingOrder();
		refundMap.put(order.getEntries().stream().filter(e -> e.getQuantity().equals(3L)).findFirst().get(), 3L);
		refundMap.put(order.getEntries().stream().filter(e -> e.getQuantity().equals(2L)).findFirst().get(), 2L);

		ReturnRequestModel returnRequest = returnUtil
				.createDefaultReturnRequest(order, ReturnAction.HOLD, RefundReason.DAMAGEDINTRANSIT, BigDecimal.valueOf(20),
						refundMap);
		returnUtil.runDefaultReturnProcessForOrder(returnRequest, ReturnStatus.APPROVAL_PENDING);
		sourcingUtil.waitUntilReturnProcessIsNotRunning(returnRequest, timeOut);
		//then verify return creation
		assertEquals(returnRequest.getRefundDeliveryCost(), false);
		assertEquals(returnRequest.getOrder(), order);
		returnRequest.getReturnEntries().stream().forEach(e -> ((RefundEntryModel) e).getAmount().equals(BigDecimal.valueOf(20)));
		assertEquals(returnRequest.getStatus(), ReturnStatus.APPROVAL_PENDING);

		//when approve return
		returnUtil.approveDefaultReturn(returnRequest);
		//then verify the status
		assertEquals(returnRequest.getStatus(), ReturnStatus.WAIT);
		//when confirm receive goods
		returnUtil.confirmWaitForGoodsDefaultReturn(returnRequest);
		//then verify the status
		assertEquals(returnRequest.getStatus(), ReturnStatus.COMPLETED);
		sourcingUtil.refreshOrder(order);
		assertEquals(3L, ((OrderEntryModel) order.getEntries().stream().filter(e -> e.getQuantity().equals(3L)).findFirst().get())
				.getQuantityReturned().longValue());
		assertEquals(2L, ((OrderEntryModel) order.getEntries().stream().filter(e -> e.getQuantity().equals(2L)).findFirst().get())
				.getQuantityReturned().longValue());
	}

	@Test
	public void shouldReturnSuccess_MultiEntry_MultiPartiallyReturns() throws RetryLaterException, InterruptedException
	{
		//when create return
		createOrderWithMultiEntry_ShippingOrder();
		refundMap.put(order.getEntries().stream().filter(e -> e.getQuantity().equals(3L)).findFirst().get(), 1L);
		refundMap.put(order.getEntries().stream().filter(e -> e.getQuantity().equals(2L)).findFirst().get(), 1L);

		ReturnRequestModel returnRequest = returnUtil
				.createDefaultReturnRequest(order, ReturnAction.IMMEDIATE, RefundReason.DAMAGEDINTRANSIT, BigDecimal.valueOf(20),
						refundMap);
		returnUtil.runDefaultReturnProcessForOrder(returnRequest, ReturnStatus.COMPLETED);
		sourcingUtil.waitUntilReturnProcessIsNotRunning(returnRequest, timeOut);
		//then verify return creation
		assertEquals(returnRequest.getRefundDeliveryCost(), false);
		assertEquals(returnRequest.getOrder(), order);
		returnRequest.getReturnEntries().stream().forEach(e -> ((RefundEntryModel) e).getAmount().equals(BigDecimal.valueOf(20)));
		assertEquals(returnRequest.getStatus(), ReturnStatus.COMPLETED);

		sourcingUtil.refreshOrder(order);
		assertEquals(1L, ((OrderEntryModel) order.getEntries().stream().filter(e -> e.getQuantity().equals(3L)).findFirst().get())
				.getQuantityReturned().longValue());
		assertEquals(1L, ((OrderEntryModel) order.getEntries().stream().filter(e -> e.getQuantity().equals(2L)).findFirst().get())
				.getQuantityReturned().longValue());

		//when return the rest quantity with deliver Cost
		ReturnRequestModel returnRequest2 = returnUtil
				.createDefaultReturnRequest(order, ReturnAction.IMMEDIATE, RefundReason.DAMAGEDINTRANSIT, BigDecimal.valueOf(20),
						refundMap);
		returnRequest2.setRefundDeliveryCost(true);
		modelService.save(returnRequest2);
		returnUtil.runDefaultReturnProcessForOrder(returnRequest2, ReturnStatus.COMPLETED);
		sourcingUtil.waitUntilReturnProcessIsNotRunning(returnRequest2, timeOut);

		//then
		assertEquals(returnRequest2.getStatus(), ReturnStatus.COMPLETED);
		assertEquals(returnRequest2.getRefundDeliveryCost(), true);
		assertEquals(returnRequest2.getOrder(), order);
		returnRequest2.getReturnEntries().stream().forEach(e -> ((RefundEntryModel) e).getAmount().equals(BigDecimal.valueOf(14)));
		assertEquals(2L, ((OrderEntryModel) order.getEntries().stream().filter(e -> e.getQuantity().equals(3L)).findFirst().get())
				.getQuantityReturned().longValue());
		assertEquals(2L, ((OrderEntryModel) order.getEntries().stream().filter(e -> e.getQuantity().equals(2L)).findFirst().get())
				.getQuantityReturned().longValue());
	}

	/**
	 * Create an order with 1 order entry, but do not ship it.
	 */
	private void createOrderWithOneEntry_ShippingOrder() throws InterruptedException
	{
		stockLevels.Camera(warehouses.Montreal(), 3);
		order = sourcingUtil.createCameraShippedOrder();
		order.setPaymentTransactions(sourcingUtil.setDummyOrderTransaction(order));
		sourcingUtil.setDummyPriceRowModel(productService.getProductForCode(CAMERA_CODE));
		modelService.save(order);
		final OrderProcessModel orderProcessModel = sourcingUtil.runOrderProcessForOrderBasedPriority(order, OrderStatus.READY);
		order.getConsignments().stream().forEach(e -> sourcingUtil.confirmDefaultConsignment(orderProcessModel, e));
		sourcingUtil.refreshOrder(order);
	}

	private void createOrderWithOneEntry_PickUpOrder() throws InterruptedException
	{
		stockLevels.Camera(warehouses.Montreal(), 3);
		order = sourcingUtil.createCameraPickUpOrder();
		order.setPaymentTransactions(sourcingUtil.setDummyOrderTransaction(order));
		sourcingUtil.setDummyPriceRowModel(productService.getProductForCode(CAMERA_CODE));
		modelService.save(order);
		final OrderProcessModel orderProcessModel = sourcingUtil.runOrderProcessForOrderBasedPriority(order, OrderStatus.READY);
		order.getConsignments().stream().forEach(e -> sourcingUtil.confirmDefaultConsignment(orderProcessModel, e));
		sourcingUtil.refreshOrder(order);
	}

	private OrderProcessModel createOrderWithOneEntry_MultiConsignments() throws InterruptedException
	{
		stockLevels.Camera(warehouses.Montreal(), 2);
		stockLevels.Camera(warehouses.Boston(), 2);
		order = sourcingUtil.createCameraShippedOrder();
		sourcingUtil.setDummyPriceRowModel(productService.getProductForCode(CAMERA_CODE));
		modelService.save(order);
		order.setPaymentTransactions(sourcingUtil.setDummyOrderTransaction(order));
		final OrderProcessModel orderProcessModel = sourcingUtil.runOrderProcessForOrderBasedPriority(order, OrderStatus.READY);
		sourcingUtil.refreshOrder(order);
		return orderProcessModel;
	}

	private void createOrderWithMultiEntry_ShippingOrder() throws InterruptedException
	{
		stockLevels.Camera(warehouses.Montreal(), 2);
		stockLevels.MemoryCard(warehouses.Montreal(), 2);
		stockLevels.Camera(warehouses.Boston(), 2);
		stockLevels.MemoryCard(warehouses.Boston(), 2);
		order = sourcingUtil.createCameraAndMemoryCardShippingOrder();
		order.setPaymentTransactions(sourcingUtil.setDummyOrderTransaction(order));
		sourcingUtil.setDummyPriceRowModel(productService.getProductForCode(CAMERA_CODE));
		sourcingUtil.setDummyPriceRowModel(productService.getProductForCode(MEMORY_CARD_CODE));
		modelService.save(order);
		final OrderProcessModel orderProcessModel = sourcingUtil.runOrderProcessForOrderBasedPriority(order, OrderStatus.READY);
		order.getConsignments().stream().forEach(e -> sourcingUtil.confirmDefaultConsignment(orderProcessModel, e));
		sourcingUtil.refreshOrder(order);
	}
}
