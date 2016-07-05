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
package de.hybris.platform.cmswebservices.pagescontentslotscomponents.facade.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminComponentService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
import de.hybris.platform.cmswebservices.common.facade.validator.FacadeValidationService;
import de.hybris.platform.cmswebservices.data.PageContentSlotComponentData;
import de.hybris.platform.cmswebservices.exception.ComponentNotFoundInSlotException;
import de.hybris.platform.cmswebservices.exception.ValidationException;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.validation.Validator;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultPageContentSlotComponentFacadeTest
{
	private static final String PAGE_UID = "page-id";
	private static final String COMPONENT_UID = "testComponent";
	private static final String SLOT_UID = "slot-id";
	private static final String INVALID_SLOT_UID = "invalid-slot-id";
	private static final Integer INDEX = 0;

	@Mock
	private AbstractCMSComponentModel component;
	@Mock
	private ContentSlotModel contentSlot;
	@Mock
	private CMSAdminContentSlotService cmsAdminContentSlotService;
	@Mock
	private CMSAdminComponentService cmsAdminComponentService;
	@Mock
	private Validator contentSlotComponentValidator;
	@Mock
	private FacadeValidationService facadeValidationService;
	@Mock
	private Validator updatePageContentSlotComponentValidator;
	@Mock
	private PlatformTransactionManager manager;

	@InjectMocks
	private DefaultPageContentSlotComponentFacade defaultPageContentSlotComponentFacade;

	private PageContentSlotComponentData pageContentSlotComponentData;

	@Before
	public void setUp()
	{
		pageContentSlotComponentData = buildPageContentSlotComponent();

		when(cmsAdminComponentService.getCMSComponentForId(COMPONENT_UID)).thenReturn(component);
		when(cmsAdminContentSlotService.getContentSlotForId(SLOT_UID)).thenReturn(contentSlot);
		when(contentSlot.getCmsComponents()).thenReturn(Collections.emptyList());
	}

	@Test
	public void shouldAddComponentToContentSlot() throws CMSItemNotFoundException
	{
		defaultPageContentSlotComponentFacade.addComponentToContentSlot(pageContentSlotComponentData);

		verify(cmsAdminContentSlotService).addCMSComponentToContentSlot(component, contentSlot, INDEX);
	}

	@Test(expected = ValidationException.class)
	public void shouldFailAddComponentToContentSlot_ValidationException() throws CMSItemNotFoundException
	{
		doThrow(new ValidationException("exception")).when(facadeValidationService).validate(Mockito.any(), Mockito.any());

		defaultPageContentSlotComponentFacade.addComponentToContentSlot(pageContentSlotComponentData);
	}

	@Test(expected = CMSItemNotFoundException.class)
	public void shouldFailAddComponentToContentSlot_SlotNotFound() throws CMSItemNotFoundException
	{
		pageContentSlotComponentData.setSlotId(INVALID_SLOT_UID);
		when(cmsAdminContentSlotService.getContentSlotForId(INVALID_SLOT_UID))
		.thenThrow(new UnknownIdentifierException("slot not found"));

		defaultPageContentSlotComponentFacade.addComponentToContentSlot(pageContentSlotComponentData);
	}

	@Test(expected = CMSItemNotFoundException.class)
	public void shouldFailAddComponentToContentSlot_AmbiguousSlot() throws CMSItemNotFoundException
	{
		pageContentSlotComponentData.setSlotId(INVALID_SLOT_UID);
		when(cmsAdminContentSlotService.getContentSlotForId(INVALID_SLOT_UID))
		.thenThrow(new AmbiguousIdentifierException("slot not found"));

		defaultPageContentSlotComponentFacade.addComponentToContentSlot(pageContentSlotComponentData);
	}

	@Test
	public void shouldRemoveComponent() throws CMSItemNotFoundException
	{
		when(cmsAdminComponentService.getCMSComponentForId(COMPONENT_UID)).thenReturn(component);
		when(cmsAdminContentSlotService.getContentSlotForId(SLOT_UID)).thenReturn(contentSlot);
		when(contentSlot.getCmsComponents()).thenReturn(Arrays.asList(component));

		defaultPageContentSlotComponentFacade.removeComponentFromContentSlot(SLOT_UID, COMPONENT_UID);

		verify(cmsAdminComponentService).removeCMSComponentFromContentSlot(component, contentSlot);
	}

	@Test(expected = CMSItemNotFoundException.class)
	public void shouldFailRemoveComponent_NotFoundComponent() throws CMSItemNotFoundException, ComponentNotFoundInSlotException
	{
		when(cmsAdminComponentService.getCMSComponentForId(COMPONENT_UID)).thenThrow(
				new AmbiguousIdentifierException("component_not_found"));
		defaultPageContentSlotComponentFacade.removeComponentFromContentSlot(SLOT_UID, COMPONENT_UID);
	}


	@Test(expected = CMSItemNotFoundException.class)
	public void shouldFailRemoveComponentFromContentSlot_AmbiguousSlot() throws CMSItemNotFoundException,
	ComponentNotFoundInSlotException
	{
		when(cmsAdminComponentService.getCMSComponentForId(COMPONENT_UID)).thenReturn(component);
		when(cmsAdminContentSlotService.getContentSlotForId(SLOT_UID)).thenThrow(
				new AmbiguousIdentifierException("ambiguous_slot_id"));

		defaultPageContentSlotComponentFacade.removeComponentFromContentSlot(SLOT_UID, COMPONENT_UID);
	}

	@Test(expected = ComponentNotFoundInSlotException.class)
	public void shouldFailRemoveComponent_ComponentNotInSlot() throws CMSItemNotFoundException, ComponentNotFoundInSlotException
	{
		when(cmsAdminComponentService.getCMSComponentForId(COMPONENT_UID)).thenReturn(component);
		when(cmsAdminContentSlotService.getContentSlotForId(SLOT_UID)).thenReturn(contentSlot);
		when(contentSlot.getCmsComponents()).thenReturn(Collections.emptyList());

		defaultPageContentSlotComponentFacade.removeComponentFromContentSlot(SLOT_UID, COMPONENT_UID);
	}

	@Test
	public void itShouldMoveAComponentFromOneSlotToAnotherOne() throws Exception
	{
		defaultPageContentSlotComponentFacade.moveComponent(PAGE_UID, COMPONENT_UID, "original-slot-id",
				buildPageContentSlotComponent());

		verify(cmsAdminContentSlotService).addCMSComponentToContentSlot(any(AbstractCMSComponentModel.class),
				any(ContentSlotModel.class), any(Integer.class));

		verify(cmsAdminComponentService).removeCMSComponentFromContentSlot(any(AbstractCMSComponentModel.class),
				any(ContentSlotModel.class));
	}

	@Test(expected = ValidationException.class)
	public void itShouldThrowValidationExceptionWhenMovingAPageContentSlotComponentWithDataIssues() throws Exception
	{
		final PageContentSlotComponentData pageContentSlotComponentData = buildPageContentSlotComponent();

		doThrow(new ValidationException("exception")).when(facadeValidationService)
				.validate(updatePageContentSlotComponentValidator, pageContentSlotComponentData);

		defaultPageContentSlotComponentFacade.moveComponent(PAGE_UID, COMPONENT_UID, "original-slot-id",
				pageContentSlotComponentData);
	}

	@Test(expected = CMSItemNotFoundException.class)
	public void itShouldThrowCMSItemNotFoundExceptionWhenPassedInvalidComponentUid() throws Exception
	{
		final PageContentSlotComponentData pageContentSlotComponentData = buildPageContentSlotComponent();

		when(defaultPageContentSlotComponentFacade.fetchComponent(COMPONENT_UID))
				.thenThrow(new UnknownIdentifierException("message"));

		defaultPageContentSlotComponentFacade.moveComponent(PAGE_UID, COMPONENT_UID, "original-slot-id",
				pageContentSlotComponentData);
	}

	protected PageContentSlotComponentData buildPageContentSlotComponent()
	{
		final PageContentSlotComponentData contentSlotComponent = new PageContentSlotComponentData();
		contentSlotComponent.setComponentId(COMPONENT_UID);
		contentSlotComponent.setPosition(INDEX);
		contentSlotComponent.setSlotId(SLOT_UID);
		return contentSlotComponent;
	}
}
