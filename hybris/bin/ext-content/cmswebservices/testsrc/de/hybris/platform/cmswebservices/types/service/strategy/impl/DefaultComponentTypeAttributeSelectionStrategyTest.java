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
import de.hybris.platform.cmswebservices.types.service.ComponentStructureTypeFactory;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

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
public class DefaultComponentTypeAttributeSelectionStrategyTest
{
	private static final String CMS_TYPE = "cms-type";
	private static final String PROPERTY_NAME = "property";

	@InjectMocks
	private DefaultComponentTypeAttributeSelectionStrategy componentTypeAttributeSelectionStrategy;

	@Mock
	private ComponentStructureTypeFactory customPropertyReader;

	@Mock
	private ComposedTypeModel composedTypeModel;
	@Mock
	private AttributeDescriptorModel declaredAttribute1;
	@Mock
	private AttributeDescriptorModel declaredAttribute2;
	@Mock
	private AttributeDescriptorModel inheritedAttribute1;
	@Mock
	private AttributeDescriptorModel inheritedAttribute2;

	@Before
	public void setUp()
	{
		when(composedTypeModel.getDeclaredattributedescriptors()).thenReturn(
				Lists.newArrayList(declaredAttribute1, declaredAttribute2));
		when(composedTypeModel.getInheritedattributedescriptors()).thenReturn(
				Lists.newArrayList(inheritedAttribute1, inheritedAttribute2));
	}

	@Test
	public void shouldGetNoMappedAttributes_NoPropertiesFoundOnAnyAttribute()
	{
		when(customPropertyReader.getStructureTypeAttribute(declaredAttribute1)).thenReturn(Optional.ofNullable(null));
		when(customPropertyReader.getStructureTypeAttribute(declaredAttribute2)).thenReturn(Optional.ofNullable(null));
		when(customPropertyReader.getStructureTypeAttribute(inheritedAttribute1)).thenReturn(Optional.ofNullable(null));
		when(customPropertyReader.getStructureTypeAttribute(inheritedAttribute2)).thenReturn(Optional.ofNullable(null));

		final Collection<AttributeDescriptorModel> attributes = componentTypeAttributeSelectionStrategy
				.getComponentTypeAttributes(composedTypeModel);
		Assert.assertTrue(attributes.isEmpty());
	}

	@Test
	public void shouldGetMappedAttributes_OnlyDeclaredAttributes()
	{
		when(composedTypeModel.getInheritedattributedescriptors()).thenReturn(Collections.emptyList());

		when(customPropertyReader.getStructureTypeAttribute(declaredAttribute1)).thenReturn(Optional.ofNullable(CMS_TYPE));
		when(customPropertyReader.getStructureTypeAttribute(declaredAttribute2)).thenReturn(Optional.ofNullable(CMS_TYPE));

		final Collection<AttributeDescriptorModel> attributes = componentTypeAttributeSelectionStrategy
				.getComponentTypeAttributes(composedTypeModel);
		Assert.assertTrue(attributes.contains(declaredAttribute1));
		Assert.assertTrue(attributes.contains(declaredAttribute2));
		Assert.assertFalse(attributes.contains(inheritedAttribute1));
		Assert.assertFalse(attributes.contains(inheritedAttribute2));
	}

	@Test
	public void shouldGetMappedAttributes_OnlyInheritedAttributes()
	{
		when(composedTypeModel.getDeclaredattributedescriptors()).thenReturn(Collections.emptyList());

		when(customPropertyReader.getStructureTypeAttribute(inheritedAttribute1)).thenReturn(Optional.ofNullable(CMS_TYPE));
		when(customPropertyReader.getStructureTypeAttribute(inheritedAttribute2)).thenReturn(Optional.ofNullable(CMS_TYPE));

		final Collection<AttributeDescriptorModel> attributes = componentTypeAttributeSelectionStrategy
				.getComponentTypeAttributes(composedTypeModel);
		Assert.assertTrue(attributes.contains(inheritedAttribute1));
		Assert.assertTrue(attributes.contains(inheritedAttribute2));
		Assert.assertFalse(attributes.contains(declaredAttribute1));
		Assert.assertFalse(attributes.contains(declaredAttribute2));
	}

	@Test
	public void shouldGetMappedAttributes_2outOf4()
	{
		when(customPropertyReader.getStructureTypeAttribute(declaredAttribute1)).thenReturn(Optional.ofNullable(CMS_TYPE));
		when(customPropertyReader.getStructureTypeAttribute(declaredAttribute2)).thenReturn(Optional.ofNullable(null));
		when(customPropertyReader.getStructureTypeAttribute(inheritedAttribute1)).thenReturn(Optional.ofNullable(CMS_TYPE));
		when(customPropertyReader.getStructureTypeAttribute(inheritedAttribute2)).thenReturn(Optional.ofNullable(null));

		final Collection<AttributeDescriptorModel> attributes = componentTypeAttributeSelectionStrategy
				.getComponentTypeAttributes(composedTypeModel);

		Assert.assertTrue(attributes.contains(inheritedAttribute1));
		Assert.assertFalse(attributes.contains(inheritedAttribute2));
		Assert.assertTrue(attributes.contains(declaredAttribute1));
		Assert.assertFalse(attributes.contains(declaredAttribute2));
	}

	@Test
	public void shouldGetMappedAttributes_All()
	{
		when(customPropertyReader.getStructureTypeAttribute(declaredAttribute1)).thenReturn(Optional.ofNullable(CMS_TYPE));
		when(customPropertyReader.getStructureTypeAttribute(declaredAttribute2)).thenReturn(Optional.ofNullable(CMS_TYPE));
		when(customPropertyReader.getStructureTypeAttribute(inheritedAttribute1)).thenReturn(Optional.ofNullable(CMS_TYPE));
		when(customPropertyReader.getStructureTypeAttribute(inheritedAttribute2)).thenReturn(Optional.ofNullable(CMS_TYPE));

		final Collection<AttributeDescriptorModel> attributes = componentTypeAttributeSelectionStrategy
				.getComponentTypeAttributes(composedTypeModel);

		Assert.assertTrue(attributes.contains(inheritedAttribute1));
		Assert.assertTrue(attributes.contains(inheritedAttribute2));
		Assert.assertTrue(attributes.contains(declaredAttribute1));
		Assert.assertTrue(attributes.contains(declaredAttribute2));
	}
}
