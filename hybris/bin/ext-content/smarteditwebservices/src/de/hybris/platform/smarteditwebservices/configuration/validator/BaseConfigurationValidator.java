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
package de.hybris.platform.smarteditwebservices.configuration.validator;

import de.hybris.platform.smarteditwebservices.constants.SmarteditwebservicesConstants;
import de.hybris.platform.smarteditwebservices.data.ConfigurationData;

import java.util.Objects;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validates the configuration Data bean
 */
public class BaseConfigurationValidator implements Validator
{

	private static final String KEY = "key";
	private static final String VALUE = "value";

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return ConfigurationData.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object obj, final Errors errors)
	{
		final ConfigurationData target = (ConfigurationData) obj;

		if (Objects.isNull(target.getKey()))
		{
			errors.rejectValue(KEY, SmarteditwebservicesConstants.FIELD_REQUIRED);
		}

		if (Objects.isNull(target.getValue()))
		{
			errors.rejectValue(VALUE, SmarteditwebservicesConstants.FIELD_REQUIRED);
		}

	}

}
