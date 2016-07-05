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
package de.hybris.platform.chineselogisticfacades.delivery.populator;

import de.hybris.platform.chineselogisticfacades.delivery.data.DeliveryTimeSlotData;
import de.hybris.platform.chineselogisticservices.model.DeliveryTimeSlotModel;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * Populating from CartModel to CartData and adding DeliveryTimeSlot into CartData
 */
public class CartDeliveryTimeSlotPopulator implements Populator<CartModel, CartData>
{
	@Override
	public void populate(CartModel source, CartData target) throws ConversionException
	{
		if (source != null && source.getDeliveryTimeSlot() != null)
		{
			final DeliveryTimeSlotModel deliveryTimeSlotModel = source.getDeliveryTimeSlot();
			if (deliveryTimeSlotModel != null)
			{
				final DeliveryTimeSlotData deliveryTimeSlotData = new DeliveryTimeSlotData();
				deliveryTimeSlotData.setCode(deliveryTimeSlotModel.getCode());
				deliveryTimeSlotData.setName(deliveryTimeSlotModel.getName());
				target.setDeliveryTimeSlot(deliveryTimeSlotData);
			}
		}
	}

}
