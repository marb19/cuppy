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
package de.hybris.platform.cmswebservices.types.facade.populator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmswebservices.data.ComponentTypeAttributeData;
import de.hybris.platform.cmswebservices.data.ComponentTypeData;
import de.hybris.platform.cmswebservices.types.service.strategy.ComponentTypeAttributeSelectionStrategy;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CmsAttributesComponentTypePopulatorTest
{
	@InjectMocks
	private CmsAttributesComponentTypePopulator cmsAttributesComponentTypePopulator;

	@Mock
	private ComponentTypeAttributeSelectionStrategy componentTypeAttributeSelectionStrategy;
	@Mock
	private Converter<AttributeDescriptorModel, ComponentTypeAttributeData> componentTypeAttributeConverter;

	@Mock
	private ComposedTypeModel type;
	@Mock
	private AttributeDescriptorModel attribute1;
	@Mock
	private AttributeDescriptorModel attribute2;
	@Mock
	private ComponentTypeAttributeData attributeDto1;
	@Mock
	private ComponentTypeAttributeData attributeDto2;

	private ComponentTypeData typeDto;

	@Before
	public void setUp()
	{
		typeDto = new ComponentTypeData();

		Mockito.when(componentTypeAttributeConverter.convert(attribute1)).thenReturn(attributeDto1);
		Mockito.when(componentTypeAttributeConverter.convert(attribute2)).thenReturn(attributeDto2);
	}

	@Test
	public void shouldConvert_MultipleAttributes()
	{
		Mockito.when(componentTypeAttributeSelectionStrategy.getComponentTypeAttributes(type))
				.thenReturn(Lists.newArrayList(attribute1, attribute2));

		cmsAttributesComponentTypePopulator.populate(type, typeDto);

		Assert.assertTrue(typeDto.getAttributes().contains(attributeDto1));
		Assert.assertTrue(typeDto.getAttributes().contains(attributeDto2));
	}

	@Test
	public void shouldConvert_NoAttributes()
	{
		Mockito.when(componentTypeAttributeSelectionStrategy.getComponentTypeAttributes(type)).thenReturn(Collections.emptyList());

		cmsAttributesComponentTypePopulator.populate(type, typeDto);

		Assert.assertTrue(typeDto.getAttributes().isEmpty());
	}
}
