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
package de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.dao;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.daos.OrderDao;

import java.util.List;


public interface YaasOrderDao extends OrderDao
{
	/**
	 * API to retrieve list of yaas orders with "created" status
	 *
	 * @return
	 */
	public List<OrderModel> findOrdersForFulFilment();
}
