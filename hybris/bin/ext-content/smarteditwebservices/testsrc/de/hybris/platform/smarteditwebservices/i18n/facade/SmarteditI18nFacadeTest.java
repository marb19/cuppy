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
package de.hybris.platform.smarteditwebservices.i18n.facade;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.i18n.daos.LanguageDao;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Lists;


@IntegrationTest
public class SmarteditI18nFacadeTest extends ServicelayerTest
{
	private final String L10N_KEY_DUMMY = "smarteditwebservices.key.dummy";
	private final String L10N_VALUE_DUMMY = "value.dummy";

	@Resource
	private SmarteditI18nFacade smarteditI18nFacade;

	@Resource
	private LanguageDao languageDao;

	@Resource
	private ModelService modelService;

	@Before
	public void setUp()
	{
		final LanguageModel english = languageDao.findLanguagesByCode("en").iterator().next();

		final LanguageModel french = modelService.create(LanguageModel.class);
		french.setIsocode("fr");
		french.setFallbackLanguages(Lists.newArrayList(english));

		final LanguageModel german = modelService.create(LanguageModel.class);
		german.setIsocode("de");

		modelService.saveAll(french, german);
	}

	@Test
	public void shouldGetTranslations_LocaleIsPresent()
	{
		final Map<String, String> translations = smarteditI18nFacade.getTranslationMap(Locale.ENGLISH);
		assertThat(L10N_VALUE_DUMMY, is(translations.get(L10N_KEY_DUMMY)));
	}

	@Test
	public void shouldGetTranslations_LocaleIsNotPresent_UseFallback()
	{
		final Map<String, String> translations = smarteditI18nFacade.getTranslationMap(Locale.FRENCH);
		assertThat(L10N_VALUE_DUMMY, is(translations.get(L10N_KEY_DUMMY)));
	}

	@Test
	@Ignore("Ignored as part of CMSX-1261 : rewrite and reenable")
	public void shouldGetTranslations_LocaleIsNotPresent_NoFallback()
	{
		final Map<String, String> translations = smarteditI18nFacade.getTranslationMap(Locale.GERMAN);
		assertNull(translations.get(L10N_KEY_DUMMY));
	}
}
