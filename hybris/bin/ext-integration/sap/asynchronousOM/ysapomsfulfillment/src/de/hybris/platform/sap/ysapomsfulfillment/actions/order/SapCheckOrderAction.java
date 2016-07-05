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
package de.hybris.platform.sap.ysapomsfulfillment.actions.order;

import org.apache.log4j.Logger;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;

public class SapCheckOrderAction extends AbstractSimpleDecisionAction<OrderProcessModel> {
	private static final Logger LOG = Logger.getLogger(SapCheckOrderAction.class);

	@Override
	public Transition executeAction(final OrderProcessModel process) {
		final OrderModel order = process.getOrder();
		if (order == null) {
			LOG.error("Missing the order, exiting the process");
			return Transition.NOK;
		}
		setOrderStatus(order, OrderStatus.CHECKED_VALID);
		return Transition.OK;

		// ** Service for Order Check needs to be called here; implementation is
		// customer specific **

	}
}