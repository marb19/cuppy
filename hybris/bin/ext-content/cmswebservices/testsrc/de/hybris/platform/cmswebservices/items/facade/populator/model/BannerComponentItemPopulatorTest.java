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
package de.hybris.platform.cmswebservices.items.facade.populator.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2lib.model.components.BannerComponentModel;
import de.hybris.platform.cmswebservices.data.BannerComponentData;
import de.hybris.platform.cmswebservices.localization.data.LocalizedValueString;
import de.hybris.platform.cmswebservices.localization.service.LocalizationService;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.session.SessionService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class BannerComponentItemPopulatorTest
{
	private static final String MEDIA_CODE = "testMediaUid";
	private static final String CONTENT = "content";

	@Mock
	SessionService sessionService;

	@Mock
	MediaService mediaService;

	@Mock
	MediaModel mediaModel;

	@Mock
	BannerComponentModel model;

	@Mock
	private MediaModel abstractBannerComponentModel;

	BannerComponentData target;

	@Mock
	LocalizationService localizationService;

	@InjectMocks
	BannerComponentModelPopulator populator = new BannerComponentModelPopulator();

	@Before
	public void setUp()
	{
		target = new BannerComponentData();

		when(model.getMedia()).thenReturn(abstractBannerComponentModel);
		when(abstractBannerComponentModel.getCode()).thenReturn(MEDIA_CODE);
		when(mediaService.getMedia(MEDIA_CODE)).thenReturn(mediaModel);

	}

	@Test
	public void shouldPopulateTheWholeStructure()
	{
		LocalizedValueString mediaLocalized = new LocalizedValueString();
		mediaLocalized.setValue(MEDIA_CODE);
		LocalizedValueString contentLocalized = new LocalizedValueString();
		contentLocalized.setValue(model.getContent());
		LocalizedValueString headlineLocalized = new LocalizedValueString();
		headlineLocalized.setValue(model.getHeadline());

		BannerComponentData target = new BannerComponentData();

		//noinspection unchecked
		when(localizationService.build(any(), any()))
				.thenReturn(mediaLocalized)
				.thenReturn(contentLocalized)
				.thenReturn(headlineLocalized);

		populator.populate(model, target);

		assertThat(((LocalizedValueString) target.getMedia()).getValue(), is(model.getMedia().getCode()));
		assertThat(((LocalizedValueString) target.getContent()).getValue(), is(model.getContent()));
		assertThat(((LocalizedValueString) target.getHeadline()).getValue(), is(model.getHeadline()));
	}
}