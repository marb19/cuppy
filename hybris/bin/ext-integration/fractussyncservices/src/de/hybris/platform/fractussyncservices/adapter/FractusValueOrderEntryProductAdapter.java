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
 */

package de.hybris.platform.fractussyncservices.adapter;


import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.store.BaseStoreModel;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;



public class FractusValueOrderEntryProductAdapter implements FractusValueImportAdapter
{
	private static final Logger LOG = Logger.getLogger(FractusValueOrderEntryProductAdapter.class);
	private ModelService modelService;
	private CatalogVersionService catalogVersionService;
	private ProductService productService;
	private FlexibleSearchService flexibleSearchService;

	@Override
	public Object performImport(String cellVal)
	{
		if (StringUtils.isEmpty(cellVal))
		{
			return null;
		}

		final String productCode = extractProductCode(cellVal);
		final String orderCode = extractOrderCode(cellVal);

		final OrderModel order = findOrder(orderCode);

		if (order == null)
		{
			LOG.warn("No order [" + orderCode + "] found.");
			return null;
		}


		if (order.getStore() != null)
		{
			BaseStoreModel baseStoreModel = order.getStore();

			CatalogModel catalogModel = baseStoreModel.getCatalogs().stream()
					.filter(catalog -> CatalogManager.ONLINE_VERSION.equals(catalog.getVersion())).findFirst().orElse(null);

			if (catalogModel != null)
			{
				CatalogVersionModel catalogVersion = getCatalogVersionService().getCatalogVersion(catalogModel.getId(),
						CatalogManager.ONLINE_VERSION);

				ProductModel productModel = getProductService().getProductForCode(catalogVersion, productCode);

				return productModel;
			}
			else
			{
				LOG.warn("Cannot find catalog for order[" + order.getCode() + "] of product[" + productCode + "]");
			}
		}
		else
		{
			LOG.warn(
					"No base store found for order[" + order.getCode() + "], product[" + productCode + "] cannot find the catalog.");
		}

		return null;
	}

	protected String extractProductCode(String cellVal)
	{
		return StringUtils.substringBefore(cellVal, "|");
	}

	protected String extractOrderCode(String cellVal)
	{
		return StringUtils.substringAfter(cellVal, "|");
	}

	protected OrderModel findOrder(String orderCode)
	{
		OrderModel o = new OrderModel();
		o.setCode(orderCode);

		return flexibleSearchService.getModelByExample(o);
	}


	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	@Required
	public void setCatalogVersionService(CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	protected ProductService getProductService()
	{
		return productService;
	}

	@Required
	public void setProductService(ProductService productService)
	{
		this.productService = productService;
	}

	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}
}
