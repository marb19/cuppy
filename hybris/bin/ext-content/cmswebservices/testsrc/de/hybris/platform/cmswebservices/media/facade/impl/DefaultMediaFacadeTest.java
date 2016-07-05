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
package de.hybris.platform.cmswebservices.media.facade.impl;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmswebservices.common.facade.validator.FacadeValidationService;
import de.hybris.platform.cmswebservices.data.MediaData;
import de.hybris.platform.cmswebservices.data.NamedQueryData;
import de.hybris.platform.cmswebservices.exception.InvalidNamedQueryException;
import de.hybris.platform.cmswebservices.exception.SearchExecutionNamedQueryException;
import de.hybris.platform.cmswebservices.exception.ValidationException;
import de.hybris.platform.cmswebservices.namedquery.NamedQuery;
import de.hybris.platform.cmswebservices.namedquery.service.NamedQueryService;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.media.exceptions.MediaNotFoundException;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Validator;

import jersey.repackaged.com.google.common.collect.Lists;



@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultMediaFacadeTest
{
	private static final String CODE = "media-code";

	@InjectMocks
	private DefaultMediaFacade mediaFacade;

	@Mock
	private MediaService mediaService;
	@Mock
	private FacadeValidationService facadeValidationService;
	@Mock
	private NamedQueryService namedQueryService;
	@Mock
	private Validator namedQueryDataValidator;
	@Mock
	private Converter<MediaModel, MediaData> mediaModelConverter;
	@Mock
	private Converter<NamedQueryData, NamedQuery> mediaNamedQueryConverter;

	@Mock
	private CMSAdminSiteService adminSiteService;

	@Mock
	private MediaModel mediaModel1;
	@Mock
	private MediaData mediaData1;
	@Mock
	private MediaModel mediaModel2;
	@Mock
	private MediaData mediaData2;
	@Mock
	private MediaModel mediaModel3;
	@Mock
	private MediaData mediaData3;
	@Mock
	private NamedQueryData namedQueryData;
	@Mock
	private NamedQuery namedQuery;
	@Mock
	private CatalogVersionModel catalogVersion;

	@Before
	public void setUp() throws InvalidNamedQueryException
	{
		when(mediaModelConverter.convert(mediaModel1)).thenReturn(mediaData1);
		when(mediaModelConverter.convert(mediaModel2)).thenReturn(mediaData2);
		when(mediaModelConverter.convert(mediaModel3)).thenReturn(mediaData3);
		when(adminSiteService.getActiveCatalogVersion()).thenReturn(catalogVersion);
		when(mediaService.getMedia(catalogVersion, CODE)).thenReturn(mediaModel1);

		when(mediaNamedQueryConverter.convert(namedQueryData)).thenReturn(namedQuery);
		when(namedQueryService.search(namedQuery)).thenReturn(Lists.newArrayList(mediaModel1, mediaModel2, mediaModel3));
		doNothing().when(facadeValidationService).validate(namedQueryDataValidator, namedQueryData);
	}

	@Test
	public void shouldGetMediaCode()
	{
		final MediaData mediaData = mediaFacade.getMediaByCode(CODE);
		Assert.assertEquals(mediaData1, mediaData);
	}

	@Test(expected = MediaNotFoundException.class)
	public void shouldFailGetMediaByCode_MediaNotFound()
	{
		when(mediaService.getMedia(catalogVersion, CODE)).thenThrow(new UnknownIdentifierException("exception"));
		mediaFacade.getMediaByCode(CODE);
	}

	@Test(expected = ValidationException.class)
	public void shouldFailGetMediaByNamedQuery_ValidationErrors()
	{
		doThrow(new ValidationException("exception")).when(facadeValidationService).validate(namedQueryDataValidator,
				namedQueryData);
		mediaFacade.getMediaByNamedQuery(namedQueryData);
	}

	@Test
	public void shouldGetMediaByNamedQuery_NoResults_ConversionException()
	{
		doThrow(new ConversionException("exception")).when(mediaNamedQueryConverter)
				.convert(namedQueryData);
		final List<MediaData> mediaList = mediaFacade.getMediaByNamedQuery(namedQueryData);
		assertTrue(mediaList.isEmpty());
	}

	@Test
	public void shouldGetMediaByNamedQuery_NoResults_InvalidNamedQuery()
	{
		doThrow(new InvalidNamedQueryException("exception")).when(mediaNamedQueryConverter).convert(namedQueryData);
		final List<MediaData> mediaList = mediaFacade.getMediaByNamedQuery(namedQueryData);
		assertTrue(mediaList.isEmpty());
	}

	@Test
	public void shouldGetMediaByNamedQuery_NoResults_SearchException() throws InvalidNamedQueryException
	{
		Mockito.doThrow(new SearchExecutionNamedQueryException("exception")).when(namedQueryService).search(namedQuery);
		final List<MediaData> mediaList = mediaFacade.getMediaByNamedQuery(namedQueryData);
		assertTrue(mediaList.isEmpty());
	}

	@Test
	public void shouldGetMediaByNamedQuery()
	{
		final List<MediaData> mediaList = mediaFacade.getMediaByNamedQuery(namedQueryData);
		assertTrue(mediaList.contains(mediaData1));
		assertTrue(mediaList.contains(mediaData2));
		assertTrue(mediaList.contains(mediaData3));
	}
}
