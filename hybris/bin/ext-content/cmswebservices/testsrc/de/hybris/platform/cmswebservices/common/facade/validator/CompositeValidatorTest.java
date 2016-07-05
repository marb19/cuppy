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
package de.hybris.platform.cmswebservices.common.facade.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmswebservices.data.AbstractCMSComponentData;
import de.hybris.platform.cmswebservices.data.CMSParagraphComponentData;
import de.hybris.platform.cmswebservices.data.SyncJobData;
import de.hybris.platform.cmswebservices.items.facade.validator.BaseComponentValidator;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;


@UnitTest
public class CompositeValidatorTest
{

	private CompositeValidator compositeValidator;
	@Before
	public void setup()
	{
		compositeValidator = new CompositeValidator();
	}
	@Test
	public void shouldSupportClass()
	{
		final AbstractCMSComponentData data = new CMSParagraphComponentData();
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		compositeValidator.setValidators(Arrays.asList(new BaseComponentValidator()));
		compositeValidator.validate(data, errors);
		assertTrue(compositeValidator.supports(data.getClass()));
	}


	@Test
	public void shouldNotSupportSyncJobDataClass()
	{
		final SyncJobData data = new SyncJobData();
		//base component validator does not support SyncJobData
		final BaseComponentValidator baseComponentValidator = new BaseComponentValidator();
		compositeValidator.setValidators(Arrays.asList(baseComponentValidator));
		assertFalse(compositeValidator.supports(data.getClass()));
	}
}
