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

import de.hybris.platform.commerceservices.delivery.dao.CountryZoneDeliveryModeDao;
import de.hybris.platform.core.model.c2l.C2LItemModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;

import java.util.Collection;


/**
 * Looking up DeliveryModes in the database
 */
public interface C2LItemZoneDeliveryModeDao extends CountryZoneDeliveryModeDao
{
	/**
	 * Getting DeliveryModes by C2LItem
	 *
	 * @param c2LItem
	 * @param order
	 * @return Collection<DeliveryModeModel>
	 */
	Collection<DeliveryModeModel> findDeliveryModesByC2LItem(final C2LItemModel c2LItem, AbstractOrderModel order);
}
