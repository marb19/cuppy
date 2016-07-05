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
package de.hybris.platform.cmswebservices.items.facade.populator.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmswebservices.data.SimpleBannerComponentData;
import de.hybris.platform.cmswebservices.localization.service.impl.DefaultLocalizationService;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
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


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class SimpleBannerComponentPopulatorTest
{
	private static final String MEDIA_CODE = "testMediaUid";
	private static final String URL_LINK = "http://something-to-test.com";
	private static final Locale LOCALE_ENG = Locale.ENGLISH;


	@Mock
	private SessionService sessionService;
	@Mock
	private MediaService mediaService;
	@Mock
	private MediaModel mediaModel;
	@Mock
	private CMSAdminSiteService cmsAdminSiteService;
	@Mock
	private CatalogVersionModel catalogVersionModel;
	@Mock
	private StoreSessionFacade storeSessionFacade;

	private final DefaultLocalizationService localizationService = new DefaultLocalizationService();

	@InjectMocks
	private final SimpleBannerComponentDataPopulator populator = new SimpleBannerComponentDataPopulator();

	private SimpleBannerComponentData bannerDto;
	private SimpleBannerComponentModel bannerModel;


	@Before
	public void setUp()
	{
		bannerModel = spy(new SimpleBannerComponentModel());

		bannerDto = new SimpleBannerComponentData();
		bannerDto.setMedia(localizationService.build(MEDIA_CODE));
		bannerDto.setUrlLink(URL_LINK);
		bannerDto.setExternal(Boolean.TRUE);

		LanguageData languageEN = new LanguageData();
		languageEN.setIsocode("EN");
		when(storeSessionFacade.getDefaultLanguage()).thenReturn(languageEN);

		when(sessionService.getAttribute(Mockito.anyString())).thenReturn(new HashSet<Locale>(Arrays.asList(LOCALE_ENG)));
		localizationService.setSessionService(sessionService);
		populator.setLocalizationService(localizationService);
		localizationService.setStoreSessionFacade(storeSessionFacade);
	}

	@Test
	public void shouldPopulateLocalizedAttributes()
	{
		when(cmsAdminSiteService.getActiveCatalogVersion()).then(a -> catalogVersionModel);
		when(mediaService.getMedia(catalogVersionModel, MEDIA_CODE)).thenReturn(mediaModel);
		when(mediaService.getMedia(cmsAdminSiteService.getActiveCatalogVersion(), MEDIA_CODE)).thenReturn(mediaModel);

		populator.populate(bannerDto, bannerModel);

		assertEquals(mediaModel, bannerModel.getMedia(LOCALE_ENG));
		assertEquals(URL_LINK, bannerModel.getUrlLink());
		assertTrue(bannerModel.isExternal());
	}

	@Test
	public void shouldPopulateNonLocalizedAttributes()
	{
		when(mediaService.getMedia(cmsAdminSiteService.getActiveCatalogVersion(), MEDIA_CODE)).thenReturn(mediaModel);
		when(localizationService.getAcceptLanguages()).thenReturn(null);
		when(localizationService.getHybrisLanguages()).thenReturn(null);
		doNothing().when(bannerModel).setMedia(mediaModel);
		populator.populate(bannerDto, bannerModel);

		verify(bannerModel).setMedia(mediaModel);
		assertEquals(URL_LINK, bannerModel.getUrlLink());
		assertTrue(bannerModel.isExternal());
	}

	@Test(expected = ConversionException.class)
	public void shouldFailMediaNotFound()
	{
		when(mediaService.getMedia(cmsAdminSiteService.getActiveCatalogVersion(), MEDIA_CODE)).thenThrow(
				new UnknownIdentifierException("not exist"));
		populator.populate(bannerDto, bannerModel);
	}

	@Test
	public void shouldDefaultBooleanValue()
	{
		bannerDto.setExternal(null);

		when(mediaService.getMedia(cmsAdminSiteService.getActiveCatalogVersion(), MEDIA_CODE)).thenReturn(mediaModel);

		populator.populate(bannerDto, bannerModel);

		assertFalse(bannerModel.isExternal());
	}
}
