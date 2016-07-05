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

package de.hybris.platform.chinesepaymentservices.action.order;

import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;


/**
 * No need to check credit card information for Chinese customers, so hard code the result.
 */
public class ChineseCheckAuthorizeOrderPaymentAction extends AbstractSimpleDecisionAction<OrderProcessModel>
{
	@Override
	public Transition executeAction(final OrderProcessModel process)
	{
		return Transition.OK;
	}
}
