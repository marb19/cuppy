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
package de.hybris.platform.chinesepspalipayservices.strategies;

import de.hybris.platform.chinesepaymentservices.model.ChinesePaymentInfoModel;
import de.hybris.platform.core.model.order.OrderModel;


public interface AlipayPaymentInfoStrategy
{
	/**
	 * update paymentinfo once payment method is chosed.
	 *
	 * @param chinesePaymentInfoModel
	 *           ChinesePaymentInfoModel to be updated {@link ChinesePaymentInfoModel}
	 * @return updated ChinesePaymentInfoModel
	 */
	ChinesePaymentInfoModel updatePaymentInfoForPayemntMethod(ChinesePaymentInfoModel chinesePaymentInfoModel);

	/**
	 * update paymentinfo once order is placed.
	 *
	 * @param order
	 *           Placed order {@link OrderModel}
	 */
	void updatePaymentInfoForPlaceOrder(OrderModel order);

}
