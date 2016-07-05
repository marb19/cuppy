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

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.yaasconfiguration.model.YaasApplicationModel;
import de.hybris.platform.yaasconfiguration.service.YaasConfigurationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


public class FractusOrderSiteAdapter implements FractusSpecialValueImportAdapter
{
	private static final Logger LOG = Logger.getLogger(FractusOrderSiteAdapter.class);
	private ModelService modelService;
	private String baseStoreUID;
	private YaasConfigurationService yaasConfigurationService;

	@Override
	public void performImport(String yaasApplicationId, Item item)
	{
		if (item instanceof Order)
		{
			OrderModel orderModel = getModelService().get(item);

			YaasApplicationModel yaasApplicationModel = getYaasConfigurationService().getYaasApplicationForId(yaasApplicationId);
			orderModel.setSite(yaasApplicationModel.getYaasProject().getBaseSite());
			orderModel.setStore(getBaseStore(yaasApplicationModel.getYaasProject().getBaseSite()));
			getModelService().save(orderModel);
		}
		else
		{
			LOG.warn("Cannot perform import for item [" + item.getComposedType() + "]");
		}
	}

	protected BaseStoreModel getBaseStore(BaseSiteModel siteMode)
	{
		return siteMode.getStores().stream().filter(store -> store.getUid().equals(getBaseStoreUID())).findFirst().orElse(null);
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

	protected YaasConfigurationService getYaasConfigurationService()
	{
		return yaasConfigurationService;
	}

	@Required
	public void setYaasConfigurationService(YaasConfigurationService yaasConfigurationService)
	{
		this.yaasConfigurationService = yaasConfigurationService;
	}

	protected String getBaseStoreUID()
	{
		return baseStoreUID;
	}

	@Required
	public void setBaseStoreUID(String baseStoreUID)
	{
		this.baseStoreUID = baseStoreUID;
	}
}
