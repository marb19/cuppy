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
package de.hybris.platform.chineselogisticservices.delivery.dao;

import de.hybris.platform.chineselogisticservices.model.DeliveryTimeSlotModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;

import java.util.List;


/**
 * Looking up DeliveryTimeSlot in the database
 */
public interface DeliveryTimeSlotDao extends Dao
{
	/**
	 * Getting all of theDeliveryTimeSlots
	 *
	 * @return List<DeliveryTimeSlotModel>
	 */
	List<DeliveryTimeSlotModel> getAllDeliveryTimeSlots();

	/**
	 * Getting the DeliveryTimeSlot by code
	 *
	 * @param code
	 * @return DeliveryTimeSlotModel
	 */
	DeliveryTimeSlotModel getDeliveryTimeSlotByCode(String code);
}
