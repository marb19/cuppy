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
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmswebservices.constants.CmswebservicesConstants;
import de.hybris.platform.cmswebservices.data.CMSLinkComponentData;
import de.hybris.platform.cmswebservices.localization.data.LocalizedValueMap;
import de.hybris.platform.cmswebservices.localization.data.LocalizedValueString;
import de.hybris.platform.cmswebservices.localization.service.impl.DefaultLocalizationService;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class LinkComponentValidatorTest
{
	@Mock
	private SessionService sessionService;
	@Mock
	private StoreSessionFacade storeSessionFacade;

	private final LinkComponentValidator validator = new LinkComponentValidator();
	private final DefaultLocalizationService localizationService = new DefaultLocalizationService();

	@Before
	public void setUp()
	{
		LanguageData languageEN = new LanguageData();
		languageEN.setIsocode("EN");
		when(storeSessionFacade.getDefaultLanguage()).thenReturn(languageEN);

		localizationService.setSessionService(sessionService);
		validator.setLocalizationService(localizationService);
		localizationService.setStoreSessionFacade(storeSessionFacade);
	}

	@Test
	public void shouldValidateLink_ValidNotLocalized()
	{
		final CMSLinkComponentData data = new CMSLinkComponentData();
		final LocalizedValueString linkName = new LocalizedValueString();
		linkName.setValue("Contact Us");
		data.setLinkName(linkName);
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(data, errors);

		assertTrue(errors.getAllErrors().isEmpty());
	}

	@Test
	public void shouldValidateLink_ValidLocalized()
	{
		when(sessionService.getAttribute(CmswebservicesConstants.HYBRIS_LANGUAGES)).thenReturn(
				new HashSet<>(Arrays.asList(Locale.ENGLISH, Locale.FRENCH)));

		final CMSLinkComponentData data = new CMSLinkComponentData();
		final LocalizedValueMap linkName = new LocalizedValueMap();
		final Map<String, String> names = new HashMap();
		names.put("en", "Contact Us");
		names.put("fr", "Contactez Nous");
		linkName.setValue(names);
		data.setLinkName(linkName);
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(data, errors);

		assertTrue(errors.getAllErrors().isEmpty());
	}

	@Test
	public void shouldValidateLink_InvalidNotLocalizedNull()
	{
		final CMSLinkComponentData data = new CMSLinkComponentData();
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(data, errors);

		assertEquals(1, errors.getAllErrors().size());
	}

	@Test
	public void shouldValidateLink_InvalidLocalizedNull()
	{
		when(sessionService.getAttribute(CmswebservicesConstants.HYBRIS_LANGUAGES)).thenReturn(
				new HashSet<>(Arrays.asList(Locale.ENGLISH, Locale.FRENCH)));

		final CMSLinkComponentData data = new CMSLinkComponentData();
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

		validator.validate(data, errors);

		// only english is required
		assertEquals(1, errors.getAllErrors().size());
	}
}
