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

package de.hybris.platform.yaasconfiguration.service.impl;

import static de.hybris.platform.yaasconfiguration.model.YaasApplicationModel._TYPECODE;
import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.yaasconfiguration.model.YaasApplicationModel;
import de.hybris.platform.yaasconfiguration.service.YaasConfigurationService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;


public class DefaultYaasConfigurationService implements YaasConfigurationService
{
	private FlexibleSearchService flexibleSearchService;

	@Override
	public YaasApplicationModel getYaasApplicationForId(String applicationId)
	{
		YaasApplicationModel model = new YaasApplicationModel();
		model.setIdentifier(applicationId);
		return getFlexibleSearchService().getModelByExample(model);
	}

	@Override
	public Optional<YaasApplicationModel> takeFirstModel()
	{
		try
		{
			final FlexibleSearchQuery query = new FlexibleSearchQuery(format("select {pk} from {%s}", _TYPECODE))
			{{
				setCount(1);
			}};
			return ofNullable(getFlexibleSearchService().searchUnique(query));
		}
		catch (ModelNotFoundException exception)
		{
			return empty();
		}
	}

	protected FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	@Required
	public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}
}
