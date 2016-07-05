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
package de.hybris.platform.cmswebservices.types.service.impl;

import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.jalo.type.AttributeDescriptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Maps;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultComponentStructureTypeFactoryTest
{
	private static final String CMS_TYPE = "cms-type";
	private static final String PROPERTY_NAME = "property";

	@InjectMocks
	private DefaultComponentStructureTypeFactory componentStructureTypeFactory;

	private Map<String, Map<String, String>> componentStructureTypes;

	@Mock
	private AttributeDescriptorModel attributeDescriptorModel;
	@Mock
	private AttributeDescriptor attributeDescriptor;

	@Before
	public void setUp()
	{
		componentStructureTypes = Maps.newHashMap();
		final HashMap<String, String> fields = Maps.newHashMap();
		fields.put(SimpleBannerComponentModel.EXTERNAL, CMS_TYPE);
		componentStructureTypes.put(SimpleBannerComponentModel._TYPECODE, fields);
		componentStructureTypeFactory.setComponentStructureTypes(componentStructureTypes);
	}

	@Test
	public void shouldGetAttributeTypeMapping()
	{
		final ComposedTypeModel value = new ComposedTypeModel();
		value.setCode(SimpleBannerComponentModel._TYPECODE);
		when(attributeDescriptorModel.getEnclosingType()).thenReturn(value);
		when(attributeDescriptorModel.getQualifier()).thenReturn(SimpleBannerComponentModel.EXTERNAL);

		final Optional<Object> type = componentStructureTypeFactory.getStructureTypeAttribute(attributeDescriptorModel);
		Assert.assertEquals(CMS_TYPE, type.get());
	}

	@Test
	public void shouldGetNullAttributeTypeMapping_PropertyNotFound()
	{
		final ComposedTypeModel value = new ComposedTypeModel();
		value.setCode(SimpleBannerComponentModel._TYPECODE);
		when(attributeDescriptorModel.getEnclosingType()).thenReturn(value);
		when(attributeDescriptorModel.getQualifier()).thenReturn(null);

		final Optional<Object> type = componentStructureTypeFactory.getStructureTypeAttribute(attributeDescriptorModel);
		Assert.assertTrue(!type.isPresent());
	}

}
