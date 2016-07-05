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
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * Populating from DeliveryTimeSlotModel to DeliveryTimeSlotData
 */
public class DeliveryTimeSlotPopulator implements Populator<DeliveryTimeSlotModel, DeliveryTimeSlotData>
{

	@Override
	public void populate(DeliveryTimeSlotModel source, DeliveryTimeSlotData target) throws ConversionException
	{
		target.setCode(source.getCode());
		target.setName(source.getName());
	}

}
