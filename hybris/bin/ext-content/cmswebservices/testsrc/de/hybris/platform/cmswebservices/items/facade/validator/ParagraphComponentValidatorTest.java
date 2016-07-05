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

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmswebservices.constants.CmswebservicesConstants;
import de.hybris.platform.cmswebservices.data.CMSParagraphComponentData;
import de.hybris.platform.cmswebservices.localization.data.LocalizedValueString;
import de.hybris.platform.cmswebservices.localization.service.impl.DefaultLocalizationService;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ParagraphComponentValidatorTest
{
	private final ParagraphComponentValidator validator = new ParagraphComponentValidator();

	private final DefaultLocalizationService localizationService = new DefaultLocalizationService();

	@Mock
	private SessionService sessionService;

	@Mock
	private StoreSessionFacade storeSessionFacade;

	@Before
	public void test()
	{
		localizationService.setSessionService(sessionService);
		validator.setLocalizationService(localizationService);

		LanguageData languageEN = new LanguageData();
		languageEN.setIsocode("EN");
		when(storeSessionFacade.getDefaultLanguage()).thenReturn(languageEN);
		localizationService.setStoreSessionFacade(storeSessionFacade);
	}

	@Test
	public void shouldValidateParagraph_Valid_Content()
	{
		final CMSParagraphComponentData data = new CMSParagraphComponentData();

		final LocalizedValueString content = new LocalizedValueString();
		content.setValue("Content");
		data.setContent(content);

		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);
		assertTrue(errors.getAllErrors().isEmpty());
	}

	@Test
	public void shouldValidateParagraph_Content_Null()
	{
		final CMSParagraphComponentData data = new CMSParagraphComponentData();
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);
		assertTrue(errors.getAllErrors().size() == 1);
	}

	@Test
	public void shouldValidateParagraph_Content_Null_MultipleLanguages()
	{
		when(sessionService.getAttribute(CmswebservicesConstants.HYBRIS_LANGUAGES)).thenReturn(
				new HashSet<>(Arrays.asList(Locale.ENGLISH, Locale.FRENCH)));
		final CMSParagraphComponentData data = new CMSParagraphComponentData();
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);
		// only english is required
		assertTrue(errors.getAllErrors().size() == 1);
	}
}
