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
package com.sap.hybris.reco.be;

import java.util.List;

import com.sap.hybris.reco.dao.ProductRecommendationData;


/**
 * Product Recommendations Value Provider
 */

public interface ProductRecommendationsValueProvider
{

	/**
	 * @return List
	 */
	public List<ProductRecommendationData> getDefaultResult();
}
