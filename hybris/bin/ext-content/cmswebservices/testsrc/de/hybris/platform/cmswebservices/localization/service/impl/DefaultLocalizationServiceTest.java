/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package de.hybris.platform.cmswebservices.localization.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmswebservices.exception.LocalizationConsumerException;
import de.hybris.platform.cmswebservices.localization.data.LocalizedValueMap;
import de.hybris.platform.cmswebservices.localization.data.LocalizedValueString;

import java.util.Map;
import java.util.Optional;

import org.junit.Test;

@UnitTest
public class DefaultLocalizationServiceTest

{

	private DefaultLocalizationService localizationService = new DefaultLocalizationService();

	@Test
	public void testGetValueWhenNullString()
	{
		final LocalizedValueString value = localizationService.build((String) null);
		assertNotNull(value);
		assertNull(value.getValue());
	}

	@Test
	public void testGetValueWhenNonemptyString()
	{
		String stringValue = "TEST";
		final LocalizedValueString value = localizationService.build(stringValue);
		assertNotNull(value);
		assertEquals(stringValue, value.getValue());
	}

	@Test
	public void testGetValueAsMapWhenNullLocalizedValue()
	{
		final Optional<Map<String, String>> optionalMap = localizationService.valueAsMap(null);
		assertNotNull(optionalMap);
		assertFalse(optionalMap.isPresent());
	}

	@Test
	public void testGetValueAsMapWhenEmptyLocalizedValue()
	{
		final Optional<Map<String, String>> optionalMap = localizationService.valueAsMap(new LocalizedValueMap());
		assertNotNull(optionalMap);
		assertFalse(optionalMap.isPresent());
	}

	@Test(expected = LocalizationConsumerException.class)
	public void testGetValueAsMapWhenInstanceOfString()
	{
		localizationService.valueAsMap(new LocalizedValueString());
	}

	@Test
	public void testGetValueAsStringWhenValueIsNull()
	{
		assertFalse(localizationService.valueAsString(null).isPresent());
	}

	@Test
	public void testGetValueAsStringWhenValueIsEmpty()
	{
		assertFalse(localizationService.valueAsString(new LocalizedValueString()).isPresent());
	}

	@Test
	public void testGetValueAsStringWhenValueIsOfLocalizedString()
	{
		final LocalizedValueString stringValue = new LocalizedValueString();
		stringValue.setValue("TEST");
		assertTrue(localizationService.valueAsString(stringValue).isPresent());
	}

	@Test
	public void testGetValueAsStringWhenValueIsOfLocalizedMapEmpty()
	{
		final LocalizedValueMap mapValue = new LocalizedValueMap();
		assertFalse(localizationService.valueAsString(mapValue).isPresent());
	}

	@Test
	public void testGetValueAsStringWhenValueIsOfLocalizedMapNotEmptyAndUniqueEntry()
	{
		String value = "TEST";
		final LocalizedValueMap mapValue = new LocalizedValueMap();
		mapValue.getValue().put("en", value);
		final Optional<String> optionalString = localizationService.valueAsString(mapValue);
		assertTrue(optionalString.isPresent());
		assertEquals(value, optionalString.get());
	}


	@Test(expected = LocalizationConsumerException.class)
	public void testGetValueAsStringWhenValueIsOfLocalizedMapNotEmptyAndNonUniqueEntry()
	{
		String value = "TEST";
		final LocalizedValueMap mapValue = new LocalizedValueMap();
		mapValue.getValue().put("en", value);
		mapValue.getValue().put("fr", value);
		localizationService.valueAsString(mapValue);
	}

}
