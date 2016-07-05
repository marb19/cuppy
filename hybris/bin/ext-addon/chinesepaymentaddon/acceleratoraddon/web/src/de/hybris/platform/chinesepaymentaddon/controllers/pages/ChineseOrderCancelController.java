/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 */
package de.hybris.platform.chinesepaymentaddon.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.chinesepaymentservices.order.service.impl.DefaultChineseOrderService;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/my-account/order/cancel")
public class ChineseOrderCancelController
{

	@Resource(name = "chineseOrderService")
	private DefaultChineseOrderService chineseOrderService;

	@RequestMapping(value = "/{orderCode}")
	@RequireHardLogIn
	public String cancelOrder(@PathVariable final String orderCode)
	{
		chineseOrderService.cancelOrder(orderCode);
		return "redirect:/my-account/order/" + orderCode;
	}
}
