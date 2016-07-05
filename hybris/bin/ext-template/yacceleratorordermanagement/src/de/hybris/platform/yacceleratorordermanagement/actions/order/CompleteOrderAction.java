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
package de.hybris.platform.yacceleratorordermanagement.actions.order;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;

import org.apache.log4j.Logger;


/**
 * This action sets the order status to the appropriate status ({@link OrderStatus#COMPLETED} or @link OrderStatus#CANCELLED})
 */
public class CompleteOrderAction extends AbstractProceduralAction<OrderProcessModel>
{
	private static final Logger LOG = Logger.getLogger(CompleteOrderAction.class);

	@Override
	public void executeAction(final OrderProcessModel process)
	{
		final OrderModel order = process.getOrder();
		if(order.getConsignments().stream().allMatch(consignment -> consignment.getStatus().equals(ConsignmentStatus.CANCELLED))){
			setOrderStatus(order, OrderStatus.CANCELLED);
		} else {
			setOrderStatus(order, OrderStatus.COMPLETED);
		}


		LOG.info("Process: " + process.getCode() + ", completed.");
	}
}
