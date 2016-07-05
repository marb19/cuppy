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
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmswebservices.common.function.ValidationConsumer;
import de.hybris.platform.cmswebservices.constants.CmswebservicesConstants;
import de.hybris.platform.cmswebservices.data.BannerComponentData;
import de.hybris.platform.cmswebservices.items.facade.validator.consumer.LocalizedMediaAttributeValidationConsumer;
import de.hybris.platform.cmswebservices.items.facade.validator.consumer.LocalizedStringAttributeValidationConsumer;
import de.hybris.platform.cmswebservices.items.facade.validator.consumer.LocalizedValidationData;
import de.hybris.platform.cmswebservices.localization.service.impl.DefaultLocalizationService;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class BannerComponentValidatorTest
{

	private static final String MEDIA_CODE_FIELD = "media";
	private static final String HEADLINE = "headline";
	private static final String CONTENT = "content";
	private static final String MEDIA_CODE = "1234";

	@InjectMocks
	private final BannerComponentValidator validator = new BannerComponentValidator();

	@Mock
	private MediaService mediaService;

	@Mock
	private CMSAdminSiteService cmsAdminSiteService;

	@InjectMocks
	private ValidationConsumer<LocalizedValidationData> localizedMediaAttributePredicate = new LocalizedMediaAttributeValidationConsumer();

	private ValidationConsumer<LocalizedValidationData> localizedStringAttributePredicate = new LocalizedStringAttributeValidationConsumer();

	@Mock
	private SessionService sessionService;
	@Mock
	private StoreSessionFacade storeSessionFacade;

	private final DefaultLocalizationService localizationService = new DefaultLocalizationService();

	@Before
	public void setup()
	{
		LanguageData languageEN = new LanguageData();
		languageEN.setIsocode("EN");
		when(storeSessionFacade.getDefaultLanguage()).thenReturn(languageEN);

		localizationService.setSessionService(sessionService);
		validator.setLocalizationService(localizationService);
		localizationService.setStoreSessionFacade(storeSessionFacade);
		validator.setLocalizedStringAttributeValidationConsumer(localizedStringAttributePredicate);
		validator.setLocalizedMediaAttributeValidationConsumer(localizedMediaAttributePredicate);
	}
	@Test
	public void shouldValidateMediaCode_Media_found()
	{
		final BannerComponentData data = createBannerComponent();
		when(mediaService.getMedia(cmsAdminSiteService.getActiveCatalogVersion(), MEDIA_CODE)).thenReturn(new MediaModel());
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);
		assertTrue(errors.getAllErrors().isEmpty());
	}


	@Test
	public void shouldValidateMediaCode_MediaNotFound()
	{
		final BannerComponentData data = createBannerComponent();
		data.setMedia(null);
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);
		assertTrue(errors.getAllErrors().size() == 1);
		assertEquals(MEDIA_CODE_FIELD, errors.getFieldError().getField());
	}

	@Test
	public void shouldValidateMediaCode_ExpectUnknownIdentifierException()
	{
		final BannerComponentData data = createBannerComponent();
		when(mediaService.getMedia(cmsAdminSiteService.getActiveCatalogVersion(), MEDIA_CODE)).thenThrow(
				new UnknownIdentifierException(""));
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);
		assertTrue(errors.getAllErrors().size() == 1);
		assertEquals(MEDIA_CODE_FIELD, errors.getFieldError().getField());
	}

	@Test
	public void shouldValidateMediaCode_AmbiguousIdentifierException()
	{
		final BannerComponentData data = createBannerComponent();
		when(mediaService.getMedia(cmsAdminSiteService.getActiveCatalogVersion(), MEDIA_CODE)).thenThrow(
				new AmbiguousIdentifierException(""));
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);
		assertTrue(errors.getAllErrors().size() == 1);
		assertEquals(MEDIA_CODE_FIELD, errors.getFieldError().getField());
	}

	@Test
	public void shouldValidate_allFields_are_Valid()
	{
		final BannerComponentData data = createBannerComponent();
		when(mediaService.getMedia(cmsAdminSiteService.getActiveCatalogVersion(), MEDIA_CODE)).thenReturn(new MediaModel());
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);
		assertTrue(errors.getAllErrors().isEmpty());

	}

	@Test
	public void shouldValidate_allFields_are_Invalid()
	{
		final BannerComponentData data = new BannerComponentData();
		when(mediaService.getMedia(Mockito.any(), Mockito.any())).thenReturn(null);
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);
		assertTrue(errors.getAllErrors().size() == 3);
	}

	@Test
	public void shouldValidate_allFields_are_Invalid_MultipleLanguage()
	{
		when(sessionService.getAttribute(CmswebservicesConstants.HYBRIS_LANGUAGES)).thenReturn(
				new HashSet<>(Arrays.asList(Locale.ENGLISH, Locale.FRENCH)));
		final BannerComponentData data = new BannerComponentData();
		when(mediaService.getMedia(Mockito.any(), Mockito.any())).thenReturn(null);
		final Errors errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());
		validator.validate(data, errors);
		// only english is required now
		assertTrue(errors.getAllErrors().size() == 3);
	}

	private BannerComponentData createBannerComponent()
	{
		final BannerComponentData data = new BannerComponentData();
		data.setContent(localizationService.build(CONTENT));
		data.setHeadline(localizationService.build(HEADLINE));
		data.setMedia(localizationService.build(MEDIA_CODE));
		return data;
	}

}
