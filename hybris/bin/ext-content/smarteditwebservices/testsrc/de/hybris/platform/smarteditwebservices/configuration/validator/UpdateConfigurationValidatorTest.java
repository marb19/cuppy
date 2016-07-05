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

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.smarteditwebservices.data.ConfigurationData;
import de.hybris.platform.smarteditwebservices.dto.UpdateConfigurationDto;

import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

@UnitTest
public class UpdateConfigurationValidatorTest
{

	private UpdateConfigurationValidator validator = new UpdateConfigurationValidator();


	@Test
	public void testValidConfigurationData()
	{
		final UpdateConfigurationDto data = new UpdateConfigurationDto();
		data.setUid("key");
		data.setKey("key");
		data.setValue("1");
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);
		assertEquals(0, errors.getErrorCount());
	}


	@Test
	public void testInvalidConfigurationData()
	{
		final UpdateConfigurationDto data = new UpdateConfigurationDto();
		data.setUid("KEY");
		data.setKey("key");
		data.setValue("1");
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);
		assertEquals(1, errors.getErrorCount());
	}

}
