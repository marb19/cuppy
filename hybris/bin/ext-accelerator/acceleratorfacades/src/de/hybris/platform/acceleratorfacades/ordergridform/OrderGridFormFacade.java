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
package de.hybris.platform.acceleratorfacades.ordergridform;


import de.hybris.platform.acceleratorfacades.product.data.ReadOnlyOrderGridData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import java.util.List;
import java.util.Map;

public interface OrderGridFormFacade
{
    /**
     *Populates the readonly order grid
     * @param orderEntryDataList
     * @return map containing grouped ReadOnlyOrderGridDatas based on the category
     */
    Map<String, ReadOnlyOrderGridData> getReadOnlyOrderGrid(final List<OrderEntryData> orderEntryDataList);
}
