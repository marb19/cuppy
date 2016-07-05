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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminComponentService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
import de.hybris.platform.cmswebservices.common.facade.validator.ValidationDtoFactory;
import de.hybris.platform.cmswebservices.data.AbstractCMSComponentData;
import de.hybris.platform.cmswebservices.dto.ComponentTypeAndContentSlotValidationDto;

import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;



@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CreateComponentValidatorTest
{
	private static final String NAME = "name";
	private static final String TYPE_CODE = "type-code";
	private static final String COMPONENT_ID = "component-id";
	private static final String INVALID_COMPONENT_ID = "invalid-component-id";
	private static final String SLOT_ID = "slot-id";
	private static final String INVALID_SLOT_ID = "invalid-slot-id";
	private static final String PAGE_ID = "homepage";
	private static final Integer POSITION = 0;


	@InjectMocks
	private final Validator validator = new CreateComponentValidator();

	@Mock
	private CMSAdminContentSlotService cmsAdminContentSlotService;
	@Mock
	private CMSAdminComponentService cmsAdminComponentService;
	@Mock
	private Predicate<String> componentExistsPredicate;
	@Mock
	private Predicate<String> contentSlotExistsPredicate;
	@Mock
	private Predicate<ComponentTypeAndContentSlotValidationDto> componentTypeAllowedForContentSlotPredicate;
	@Mock
	private Predicate<String> validStringLengthPredicate;
	@Mock
	private Predicate<String> typeCodeExistsPredicate;
	@Mock
	private ValidationDtoFactory validationDtoFactory;
	@Mock
	private Predicate<String> pageExistsPredicate;
	@Mock
	private ContentSlotModel contentSlot;
	@Mock
	private AbstractCMSComponentModel component;
	@Mock
	private ComponentTypeAndContentSlotValidationDto componentTypeAndContentSlotValidationDto;

	private AbstractCMSComponentData target;
	private Errors errors;

	@Before
	public void setUp()
	{
		target = new AbstractCMSComponentData();
		target.setUid(COMPONENT_ID);
		target.setSlotId(SLOT_ID);
		target.setName(NAME);
		target.setTypeCode(TYPE_CODE);
		target.setPageId(PAGE_ID);
		target.setPosition(POSITION);

		errors = new BeanPropertyBindingResult(target, target.getClass().getSimpleName());

		when(pageExistsPredicate.test(Mockito.anyString())).thenReturn(Boolean.TRUE);

		when(componentExistsPredicate.test(COMPONENT_ID)).thenReturn(Boolean.FALSE);
		when(componentExistsPredicate.test(INVALID_COMPONENT_ID)).thenReturn(Boolean.TRUE);

		when(contentSlotExistsPredicate.test(SLOT_ID)).thenReturn(Boolean.TRUE);
		when(contentSlotExistsPredicate.test(INVALID_SLOT_ID)).thenReturn(Boolean.FALSE);

		when(cmsAdminContentSlotService.getContentSlotForId(SLOT_ID)).thenReturn(contentSlot);
		when(cmsAdminComponentService.getCMSComponentForId(COMPONENT_ID)).thenReturn(component);

		when(componentTypeAllowedForContentSlotPredicate.test(Mockito.any(ComponentTypeAndContentSlotValidationDto.class)))
				.thenReturn(Boolean.TRUE);

		when(validStringLengthPredicate.test(NAME)).thenReturn(Boolean.TRUE);
		when(validStringLengthPredicate.test(COMPONENT_ID)).thenReturn(Boolean.TRUE);

		when(typeCodeExistsPredicate.test(TYPE_CODE)).thenReturn(Boolean.TRUE);

		when(validationDtoFactory.buildComponentTypeAndContentSlotValidationDto(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(componentTypeAndContentSlotValidationDto);
	}

	@Test
	public void shouldHaveNoFailures_ComponentUidDoesNotExist()
	{
		validator.validate(target, errors);
		assertFalse(errors.hasErrors());
	}

	@Test
	public void shouldFail_TypeCodeNotFound()
	{
		when(typeCodeExistsPredicate.test(TYPE_CODE)).thenReturn(Boolean.FALSE);

		validator.validate(target, errors);
		assertTrue(errors.hasErrors());
		assertEquals(1, errors.getFieldErrorCount());
	}

	@Test
	public void shouldFail_ComponentUidNull()
	{
		when(validStringLengthPredicate.test(COMPONENT_ID)).thenReturn(Boolean.FALSE);

		validator.validate(target, errors);
		assertTrue(errors.hasErrors());
		assertEquals(1, errors.getFieldErrorCount());
	}

	@Test
	public void shouldFail_PageIdNull()
	{
		target.setPageId(null);

		validator.validate(target, errors);
		assertTrue(errors.hasErrors());
		assertEquals(1, errors.getFieldErrorCount());
	}

	@Test
	public void shouldFail_PositionNull()
	{
		target.setPosition(null);
		validator.validate(target, errors);
		assertTrue(errors.hasErrors());
		assertEquals(1, errors.getFieldErrorCount());
	}


	@Test
	public void shouldFail_UidTooLong()
	{
		target.setName(null);

		validator.validate(target, errors);
		assertTrue(errors.hasErrors());
		assertEquals(1, errors.getFieldErrorCount());
	}

	@Test
	public void shouldFail_TypeNotAllowedInSlot()
	{
		when(componentTypeAllowedForContentSlotPredicate.test(Mockito.any(ComponentTypeAndContentSlotValidationDto.class)))
				.thenReturn(Boolean.FALSE);

		validator.validate(target, errors);
		assertTrue(errors.hasErrors());
		assertEquals(1, errors.getFieldErrorCount());
	}

	@Test
	public void shouldFail_NameNull()
	{
		target.setName(null);

		validator.validate(target, errors);
		assertTrue(errors.hasErrors());
		assertEquals(1, errors.getFieldErrorCount());
	}

	@Test
	public void shouldFail_NameTooLong()
	{
		when(validStringLengthPredicate.test(NAME)).thenReturn(Boolean.FALSE);

		validator.validate(target, errors);
		assertTrue(errors.hasErrors());
		assertEquals(1, errors.getFieldErrorCount());
	}

	@Test
	public void shouldFail_TypeCodeNull()
	{
		target.setTypeCode(null);

		validator.validate(target, errors);
		assertTrue(errors.hasErrors());
		assertEquals(1, errors.getFieldErrorCount());
	}

	@Test
	public void shouldFail_ComponentExist()
	{
		target.setUid(INVALID_COMPONENT_ID);

		validator.validate(target, errors);
		assertTrue(errors.hasErrors());
		assertEquals(1, errors.getFieldErrorCount());
	}

	@Test
	public void shouldFail_SlotDoesNotExist()
	{
		target.setSlotId(INVALID_SLOT_ID);

		validator.validate(target, errors);
		assertTrue(errors.hasErrors());
		assertEquals(1, errors.getFieldErrorCount());
	}

}
