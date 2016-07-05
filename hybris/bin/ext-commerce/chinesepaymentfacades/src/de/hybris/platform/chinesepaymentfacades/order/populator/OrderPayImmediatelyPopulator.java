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
package de.hybris.platform.chinesepaymentfacades.order.populator;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.stereotype.Component;


@Component
public class OrderPayImmediatelyPopulator implements Populator<OrderModel, OrderData>
{

	@Override
	public void populate(final OrderModel source, final OrderData target) throws ConversionException
	{

		target.setPaymentStatus(source.getPaymentStatus());
		target.setStatus(source.getStatus());
	}

}
