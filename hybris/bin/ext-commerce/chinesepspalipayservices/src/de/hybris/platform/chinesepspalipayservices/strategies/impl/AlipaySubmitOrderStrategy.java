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
package de.hybris.platform.chinesepspalipayservices.strategies.impl;

import de.hybris.platform.chinesepaymentservices.model.ChinesePaymentInfoModel;
import de.hybris.platform.chinesepspalipayservices.strategies.AlipayPaymentInfoStrategy;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.order.strategies.SubmitOrderStrategy;

import org.springframework.beans.factory.annotation.Required;



public class AlipaySubmitOrderStrategy implements SubmitOrderStrategy
{
	private AlipayPaymentInfoStrategy alipayPaymentInfoStrategy;

	@Override
	public void submitOrder(final OrderModel order)
	{
		if (order.getStatus() == null || order.getStatus().equals(OrderStatus.CREATED))
		{
			final PaymentInfoModel paymentInfoModel = order.getPaymentInfo();
			if (paymentInfoModel != null)
			{
				if (paymentInfoModel instanceof ChinesePaymentInfoModel)
				{
					alipayPaymentInfoStrategy.updatePaymentInfoForPlaceOrder(order);
				}
			}
		}
	}


	@Required
	public void setAlipayPaymentInfoStrategy(final AlipayPaymentInfoStrategy alipayPaymentInfoStrategy)
	{
		this.alipayPaymentInfoStrategy = alipayPaymentInfoStrategy;
	}


	protected AlipayPaymentInfoStrategy getAlipayPaymentInfoStrategy()
	{
		return alipayPaymentInfoStrategy;
	}



}
