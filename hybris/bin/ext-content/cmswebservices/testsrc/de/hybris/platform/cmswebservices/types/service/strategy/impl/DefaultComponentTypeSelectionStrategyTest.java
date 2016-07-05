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
package de.hybris.platform.cmswebservices.types.service.strategy.impl;

import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.cms2lib.model.components.BannerComponentModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultComponentTypeSelectionStrategyTest
{
	@InjectMocks
	private DefaultComponentTypeSelectionStrategy componentTypeSelectionStrategy;

	@Mock
	private TypeService typeService;
	@Mock
	private ComposedTypeModel paragraph;
	@Mock
	private ComposedTypeModel banner;
	@Mock
	private ComposedTypeModel link;
	@Mock
	private ComposedTypeModel simpleBanner;
	@Mock
	private ComposedTypeModel notsupported;

	@Before
	public void setUp()
	{
		when(typeService.getComposedTypeForCode(CMSParagraphComponentModel._TYPECODE)).thenReturn(paragraph);
		when(typeService.getComposedTypeForCode(SimpleBannerComponentModel._TYPECODE)).thenReturn(banner);
		when(typeService.getComposedTypeForCode(CMSLinkComponentModel._TYPECODE)).thenReturn(link);
		when(typeService.getComposedTypeForCode(BannerComponentModel._TYPECODE)).thenReturn(simpleBanner);

	}

	@Test
	public void shouldGetAllSupportedTypes()
	{
		final List<String> supportedComponents = Lists.newArrayList(CMSParagraphComponentModel._TYPECODE,
				SimpleBannerComponentModel._TYPECODE, CMSLinkComponentModel._TYPECODE, BannerComponentModel._TYPECODE);

		componentTypeSelectionStrategy.setSupportedTypes(supportedComponents);
		final List<ComposedTypeModel> componentTypes = componentTypeSelectionStrategy.getComponentTypes();

		Assert.assertTrue(componentTypes.contains(banner));
		Assert.assertTrue(componentTypes.contains(link));
		Assert.assertTrue(componentTypes.contains(paragraph));
		Assert.assertTrue(componentTypes.contains(simpleBanner));
	}


	@Test
	public void shouldNotReturnUnsupportedType()
	{
		final List<String> supportedComponents = Lists.newArrayList(CMSParagraphComponentModel._TYPECODE);

		componentTypeSelectionStrategy.setSupportedTypes(supportedComponents);
		final List<ComposedTypeModel> componentTypes = componentTypeSelectionStrategy.getComponentTypes();
		Assert.assertTrue(componentTypes.contains(paragraph));
		Assert.assertFalse(componentTypes.contains(notsupported));
	}
}
