/*
 *  
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
 */
package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.daos.ProductDao;
import de.hybris.platform.warehousing.util.builder.ProductModelBuilder;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Required;


public class Products extends AbstractItems<ProductModel>
{
	public static final String CODE_CAMERA = "camera";
	public static final String CODE_MEMORY_CARD = "memorycard";
	public static final String CODE_LENS = "lens";
	public static final Double PRICE=50.00;

	private ProductDao productDao;
	private CatalogVersions catalogVersions;
	private Units units;

	public ProductModel Camera()
	{
		return getFromCollectionOrSaveAndReturn(() -> getProductDao().findProductsByCode(CODE_CAMERA), 
				() -> ProductModelBuilder.aModel() 
				.withCode(CODE_CAMERA) 
				.withName("Camera", Locale.ENGLISH) 
				.withPrice(PRICE) 
				.withStartLineNumber(0) 
				.withUnit(getUnits().Unit()) 
				.withApprovalStatus(ArticleApprovalStatus.APPROVED) 
				.withCatalogVersion(getCatalogVersions().Staging()) 
				.build());
	}

	public ProductModel MemoryCard()
	{
		return getFromCollectionOrSaveAndReturn( 
				() -> getProductDao().findProductsByCode(CODE_MEMORY_CARD), 
				() -> ProductModelBuilder.aModel() 
				.withCode(CODE_MEMORY_CARD) 
				.withName("Memory Card", Locale.ENGLISH) 
				.withPrice(PRICE) 
				.withStartLineNumber(1) 
				.withUnit(getUnits().Unit()) 
				.withApprovalStatus(ArticleApprovalStatus.APPROVED) 
				.withCatalogVersion(getCatalogVersions().Staging()) 
				.build());
	}

	public ProductModel Lens()
	{
		return getFromCollectionOrSaveAndReturn(
				() -> getProductDao().findProductsByCode(CODE_LENS),
				() -> ProductModelBuilder.aModel()
						.withCode(CODE_LENS)
						.withName("Lens", Locale.ENGLISH)
						.withPrice(PRICE)
						.withStartLineNumber(1)
						.withUnit(getUnits().Unit())
						.withApprovalStatus(ArticleApprovalStatus.APPROVED)
						.withCatalogVersion(getCatalogVersions().Staging())
						.build());
	}

	public ProductDao getProductDao()
	{
		return productDao;
	}

	@Required
	public void setProductDao(final ProductDao productDao)
	{
		this.productDao = productDao;
	}

	public CatalogVersions getCatalogVersions()
	{
		return catalogVersions;
	}

	@Required
	public void setCatalogVersions(final CatalogVersions catalogVersions)
	{
		this.catalogVersions = catalogVersions;
	}

	public Units getUnits()
	{
		return units;
	}

	@Required
	public void setUnits(final Units units)
	{
		this.units = units;
	}

}
