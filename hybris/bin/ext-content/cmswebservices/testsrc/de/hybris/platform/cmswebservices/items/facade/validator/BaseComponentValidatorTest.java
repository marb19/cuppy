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
package de.hybris.platform.cmswebservices.items.facade.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import de.hybris.platform.cmswebservices.data.AbstractCMSComponentData;
import de.hybris.platform.cmswebservices.data.CMSParagraphComponentData;
import de.hybris.platform.cmswebservices.items.facade.validator.BaseComponentValidator;

import java.util.UUID;

import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;


@UnitTest
public class BaseComponentValidatorTest
{

	private static final String TYPE_CODE = "typeCode";
	private static final String NAME = "name";
	private static final String UID = "uid";
	private static final String PARAGRAPH_COMPONENT = "ParagraphComponent";
	private final BaseComponentValidator validator = new BaseComponentValidator();

	@Test
	public void shouldValidateComponent_All_Fields_are_valid()
	{
		final AbstractCMSComponentData data = createComponentData();
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);
		assertTrue(errors.getAllErrors().isEmpty());
	}

	@Test
	public void shouldValidateComponent_All_Fields_are_invalid()
	{
		final AbstractCMSComponentData data = new CMSParagraphComponentData();
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);
		assertTrue(errors.getAllErrors().size() == 3);
	}

	@Test
	public void shouldValidateComponent_Invalid_Uid()
	{
		final AbstractCMSComponentData data = createComponentData();
		data.setUid(null);
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);
		assertTrue(errors.getAllErrors().size() == 1);
		assertEquals(UID, errors.getFieldError().getField());
	}

	@Test
	public void shouldValidateComponent_Invalid_Name()
	{
		final AbstractCMSComponentData data = createComponentData();
		data.setName(null);

		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);
		assertTrue(errors.getAllErrors().size() == 1);
		assertEquals(NAME, errors.getFieldError().getField());
	}

	@Test
	public void shouldValidateComponent_Invalid_TypeCode()
	{
		final AbstractCMSComponentData data = createComponentData();
		data.setTypeCode(null);
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);
		assertTrue(errors.getAllErrors().size() == 1);
		assertEquals(TYPE_CODE, errors.getFieldError().getField());
	}

	private AbstractCMSComponentData createComponentData()
	{
		final CMSParagraphComponentData data = new CMSParagraphComponentData();
		data.setName(PARAGRAPH_COMPONENT);
		data.setTypeCode(SimpleBannerComponentModel._TYPECODE);
		data.setUid(UUID.randomUUID().toString());
		return data;
	}
}
