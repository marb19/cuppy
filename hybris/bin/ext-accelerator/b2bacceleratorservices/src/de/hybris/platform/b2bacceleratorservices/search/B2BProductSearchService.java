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
package de.hybris.platform.b2bacceleratorservices.search;

import de.hybris.platform.b2bacceleratorservices.product.B2BProductService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Collection;


/**
 * @deprecated Since 6.0. Use {@link de.hybris.platform.b2bacceleratorservices.product.B2BProductService} instead.
 */
@Deprecated
public interface B2BProductSearchService<T extends ProductModel>
{
	/**
	 * Gets all visible {@link de.hybris.platform.core.model.product.ProductModel} for a given collection of SKUs.
	 *
	 * @deprecated Since 6.0. Use {@link B2BProductService#getProductsForSkus(Collection, PageableData)} instead.
	 *
	 * @param skus
	 *           String collection of product IDs
	 * @param pageableData
	 *           Pagination information
	 * @return List of paginated {@link ProductModel} objects
	 *
	 */
	@Deprecated
	SearchPageData<T> findProductsBySkus(Collection<String> skus, PageableData pageableData);

}
