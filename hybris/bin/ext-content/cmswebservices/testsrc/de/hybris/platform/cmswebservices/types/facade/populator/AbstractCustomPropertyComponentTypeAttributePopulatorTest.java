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
import de.hybris.platform.cmswebservices.types.service.ComponentStructureTypeFactory;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class AbstractCustomPropertyComponentTypeAttributePopulatorTest
{
	private static final String CMS_STRUCTURE_TYPE = "cms-structure-type";
	private static final String PROPERTY_NAME = "property";

	@InjectMocks
	private CmsStructureTypeComponentTypeAttributePopulator cmsStructureTypeComponentTypeAttributePopulator;

	@Mock
	private ComponentStructureTypeFactory componentStructureTypeFactory;

	@Mock
	private AttributeDescriptorModel attribute;

	private ComponentTypeAttributeData attributeDto;

	@Before
	public void setUp()
	{
		attributeDto = new ComponentTypeAttributeData();
	}

	@Test
	public void shouldPopulateStructureType()
	{
		final Object response = new String(CMS_STRUCTURE_TYPE);
		Mockito.when(componentStructureTypeFactory.getStructureTypeAttribute(attribute)).thenReturn(Optional.of(response));

		cmsStructureTypeComponentTypeAttributePopulator.populate(attribute, attributeDto);

		Assert.assertEquals(CMS_STRUCTURE_TYPE, attributeDto.getCmsStructureType());
	}

	@Test
	public void shouldNotPopulateStructureType_NoMappingFound()
	{
		Mockito.when(componentStructureTypeFactory.getStructureTypeAttribute(attribute)).thenReturn(Optional.ofNullable(null));

		cmsStructureTypeComponentTypeAttributePopulator.populate(attribute, attributeDto);

		Assert.assertNull(attributeDto.getCmsStructureType());
	}

	@Test(expected = ConversionException.class)
	public void shouldNotPopulateStructureType_ClassCastException()
	{
		final Object response = Boolean.TRUE;
		Mockito.when(componentStructureTypeFactory.getStructureTypeAttribute(attribute)).thenReturn(Optional.of(response));

		cmsStructureTypeComponentTypeAttributePopulator.populate(attribute, attributeDto);

		Assert.assertNull(attributeDto.getCmsStructureType());
	}

}
