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
package de.hybris.platform.smarteditaddon.cms.services.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import javax.servlet.jsp.PageContext;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;

@IntegrationTest
public class CMSSmartEditDynamicAttributeServiceTest extends ServicelayerBaseTest
{
	private static final String CLASS_ATTRIBUTE = "class";
	private static final String COMPONENT_TYPE_ATTRIBUTE = "data-smartedit-component-type";
	private static final String COMPONENT_ID_ATTRIBUTE = "data-smartedit-component-id";
	private static final String SMART_EDIT_COMPONENT_CLASS = "smartEditComponent";
	private static final String CONTENT_SLOT_TYPE = "ContentSlot";
	private static final String CONTENT_SLOT_UID = "TestSlot";
	private static final String COMPONENT_TYPE = "Component";
	private static final String COMPONENT_UID = "TestComponent";

	@Mock
	private AbstractCMSComponentModel component;

	@Mock
	private ConfigurationService configurationService;

	@Mock
	private org.apache.commons.configuration.Configuration configuration;

	@Mock
	private ContentSlotModel contentSlot;

	@InjectMocks
	private CMSSmartEditDynamicAttributeService cmsSmartEditDynamicAttributeService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		BDDMockito.given(contentSlot.getUid()).willReturn(CONTENT_SLOT_UID);
		BDDMockito.given(contentSlot.getItemtype()).willReturn(CONTENT_SLOT_TYPE);
		BDDMockito.given(component.getUid()).willReturn(COMPONENT_UID);
		BDDMockito.given(component.getItemtype()).willReturn(COMPONENT_TYPE);
		BDDMockito.when(configuration.getBoolean(Mockito.anyString())).thenReturn(true);
		BDDMockito.when(configurationService.getConfiguration()).thenReturn(configuration);
	}

	@Test
	public void testGetDynamicComponentAttributes()
	{
		final Map<String, String> dynamicAttributes = cmsSmartEditDynamicAttributeService.getDynamicComponentAttributes(component,
				contentSlot);

		Assert.assertNotNull("Dynamic attribute map should not be null", dynamicAttributes);
		Assert.assertEquals("component id attribute does not match expected value", COMPONENT_UID,
				dynamicAttributes.get(COMPONENT_ID_ATTRIBUTE));
		Assert.assertEquals("component type attribute does not match expected value", COMPONENT_TYPE,
				dynamicAttributes.get(COMPONENT_TYPE_ATTRIBUTE));
		Assert.assertEquals("class does not match the expected value", SMART_EDIT_COMPONENT_CLASS,
				dynamicAttributes.get(CLASS_ATTRIBUTE));
	}

	@Test
	public void testGetDynamicComponentAttributesComponentNull()
	{
		final Map<String, String> dynamicAttributes = cmsSmartEditDynamicAttributeService.getDynamicComponentAttributes(null,
				contentSlot);

		Assert.assertNotNull("Dynamic attribute map should not be null", dynamicAttributes);
		Assert.assertTrue("Dynamic attribute map should be empty but is: " + dynamicAttributes.toString(),
				MapUtils.isEmpty(dynamicAttributes));
	}

	@Test
	public void testGetDynamicComponentAttributesContentSlotNull()
	{
		final Map<String, String> dynamicAttributes = cmsSmartEditDynamicAttributeService.getDynamicComponentAttributes(component,
				null);

		Assert.assertNotNull("Dynamic attribute map should not be null", dynamicAttributes);
		Assert.assertTrue("Dynamic attribute map should be empty but is: " + dynamicAttributes.toString(),
				MapUtils.isEmpty(dynamicAttributes));
	}

	@Test
	public void testGetDynamicContentSlotAttributes()
	{
		final Map<String, String> dynamicAttributes = cmsSmartEditDynamicAttributeService
				.getDynamicContentSlotAttributes(contentSlot, mock(PageContext.class), new HashMap<>());

		Assert.assertNotNull("Dynamic attribute map should not be null", dynamicAttributes);
		Assert.assertEquals("component id attribute does not match expected value", CONTENT_SLOT_UID,
				dynamicAttributes.get(COMPONENT_ID_ATTRIBUTE));
		Assert.assertEquals("component type attribute does not match expected value", CONTENT_SLOT_TYPE,
				dynamicAttributes.get(COMPONENT_TYPE_ATTRIBUTE));
		Assert.assertEquals("class does not match the expected value", SMART_EDIT_COMPONENT_CLASS,
				dynamicAttributes.get(CLASS_ATTRIBUTE));
	}

	@Test
	public void testGetDynamicContentSlotAttributesContentSlotNull()
	{
		final Map<String, String> dynamicAttributes = cmsSmartEditDynamicAttributeService.getDynamicContentSlotAttributes(null, mock(PageContext.class), new HashMap<>());

		Assert.assertNotNull("Dynamic attribute map should not be null", dynamicAttributes);
		Assert.assertTrue("Dynamic attribute map should be empty but is: " + dynamicAttributes.toString(),
				MapUtils.isEmpty(dynamicAttributes));
	}
}
