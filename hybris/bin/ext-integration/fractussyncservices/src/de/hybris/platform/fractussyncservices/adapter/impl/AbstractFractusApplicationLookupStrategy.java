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

package de.hybris.platform.fractussyncservices.adapter.impl;


import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Sets;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.fractussyncservices.adapter.FractusApplicationLookupStrategy;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.yaasconfiguration.model.YaasApplicationModel;
import de.hybris.platform.yaasconfiguration.model.YaasProjectModel;


public abstract class AbstractFractusApplicationLookupStrategy implements FractusApplicationLookupStrategy
{
	private ModelService modelService;

	protected Set<YaasApplicationModel> getApplications(final ProductModel productModel)
	{
		Set<YaasApplicationModel> apps = Sets.newHashSet();

		Collection<BaseStoreModel> baseStores = productModel.getCatalogVersion().getCatalog().getBaseStores();

		for (BaseStoreModel baseStore : baseStores)
		{
			for (BaseSiteModel baseSiteModel : baseStore.getCmsSites())
			{
				apps.addAll(getApplications(baseSiteModel));
			}
		}

		return apps;
	}

	protected Set<YaasApplicationModel> getApplications(final BaseSiteModel baseSiteModel)
	{
		Set<YaasApplicationModel> apps = Sets.newHashSet();

		for (YaasProjectModel yaasProjectModel : baseSiteModel.getYaasProjects())
		{
			apps.addAll(yaasProjectModel.getYaasApplications());
		}

		return apps;
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
}
