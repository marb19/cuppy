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

import de.hybris.platform.sap.core.bol.backend.BackendBusinessObject;

import com.sap.hybris.reco.dao.RecommendationContext;
import com.sap.hybris.reco.dao.InteractionContext;
import com.sap.hybris.reco.dao.ProductRecommendationData;

import java.util.List;


/**
 * Product Recommendation Manager Backend
 */
public interface ProductRecommendationManagerBackend extends BackendBusinessObject
{
	/**
	 * Get Product Recommendations from hybris Marketing based on current context
	 *
	 * @param context
	 * @return List<ProductRecommendation>
	 */
	public List<ProductRecommendationData> getProductRecommendation(RecommendationContext context);

	/**
	 * Post the user interactions to the hybris Marketing.
	 *
	 * @param context
	 */
	public void postInteraction(InteractionContext context);

	/**
	 * Prefetch Product Recommendations from hybris Marketing based on contexts for the batch
	 *
	 * @param contexts
	 */
	void prefetchRecommendations(List<RecommendationContext> contexts);

	/**
	 * @return true if prefetch is enabled
	 */
	public boolean isPrefetchEnabled();
}
