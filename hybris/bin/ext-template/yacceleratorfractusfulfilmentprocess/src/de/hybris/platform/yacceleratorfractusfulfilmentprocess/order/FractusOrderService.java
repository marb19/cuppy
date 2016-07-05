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
package de.hybris.platform.yacceleratorfractusfulfilmentprocess.order;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.OrderService;

import java.util.List;


public interface FractusOrderService extends OrderService
{
	boolean isFractusOrder(OrderModel order);

	/**
	 * YaaS orders ready for fulfilment
	 *
	 * @return
	 */
	List<OrderModel> getOrdersForFulfilment();

	/**
	 * Starts fulfilment process for the YaaS order
	 *
	 * @param order
	 */
	void startFulfilmentProcess(final OrderModel order);

}
