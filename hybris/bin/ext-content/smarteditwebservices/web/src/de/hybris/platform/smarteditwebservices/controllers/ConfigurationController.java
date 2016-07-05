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
package de.hybris.platform.smarteditwebservices.controllers;

import de.hybris.platform.smarteditwebservices.configuration.facade.SmarteditConfigurationFacade;
import de.hybris.platform.smarteditwebservices.data.ConfigurationData;
import de.hybris.platform.smarteditwebservices.dto.ConfigurationDataListWsDto;
import de.hybris.platform.smarteditwebservices.validation.facade.ValidationException;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/configurations")
public class ConfigurationController
{
	@Resource
	private SmarteditConfigurationFacade smarteditConfigurationFacade;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ConfigurationDataListWsDto loadAll()
	{
		final ConfigurationDataListWsDto configurations = new ConfigurationDataListWsDto();
		configurations.setConfigurations(getSmarteditConfigurationFacade().findAll());
		return configurations;
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ConfigurationData save(@RequestBody final ConfigurationData data)
	{
		try
		{
			return getSmarteditConfigurationFacade().create(data);
		}
		catch (final ValidationException e)
		{
			throw new WebserviceValidationException(e.getValidationObject());
		}
	}

	@RequestMapping(value = "/{key:.+}", method = RequestMethod.GET)
	@ResponseBody
	public ConfigurationData findByKey(@PathVariable("key") final String key)
	{
		return getSmarteditConfigurationFacade().findByUid(key);
	}

	@RequestMapping(value = "/{key:.+}", method = RequestMethod.PUT)
	@ResponseBody
	public ConfigurationData update(@RequestBody final ConfigurationData data, @PathVariable("key") final String key)
	{
		try
		{
			return getSmarteditConfigurationFacade().update(key, data);
		}
		catch (final ValidationException e)
		{
			throw new WebserviceValidationException(e.getValidationObject());
		}
	}

	@RequestMapping(value = "/{key:.+}", method = RequestMethod.DELETE)
	@ResponseBody
	public void delete(@RequestBody final ConfigurationData data, @PathVariable("key") final String key)
	{
		getSmarteditConfigurationFacade().delete(key);
	}

	public SmarteditConfigurationFacade getSmarteditConfigurationFacade()
	{
		return smarteditConfigurationFacade;
	}

	public void setSmarteditConfigurationFacade(final SmarteditConfigurationFacade smarteditConfigurationFacade)
	{
		this.smarteditConfigurationFacade = smarteditConfigurationFacade;
	}
}
