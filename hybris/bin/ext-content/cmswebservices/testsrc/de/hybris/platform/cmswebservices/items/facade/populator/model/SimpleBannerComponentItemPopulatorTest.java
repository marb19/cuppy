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
import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import de.hybris.platform.cmswebservices.data.LocalizedValueData;
import de.hybris.platform.cmswebservices.data.SimpleBannerComponentData;
import de.hybris.platform.cmswebservices.localization.data.LocalizedValueString;
import de.hybris.platform.cmswebservices.localization.service.LocalizationService;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.session.SessionService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class SimpleBannerComponentItemPopulatorTest
{

	private static final String MEDIA_CODE = "testMediaCode";
	private static final String MEDIA_LINK = "testMediaLink";

	@Mock
	SimpleBannerComponentModel model;

	@Mock
	SessionService sessionService;

	@Mock
	private MediaModel mediaModel;

	@Mock
	private LocalizationService localizationService;

	@InjectMocks
	SimpleBannerComponentModelPopulator populator = new SimpleBannerComponentModelPopulator();

	@Test
	public void shouldPopulateTheWholeStructure()
	{
		LocalizedValueString mediaLocalized = new LocalizedValueString();
		mediaLocalized.setValue(MEDIA_CODE);

		//noinspection unchecked
		when(localizationService.build(any(), any()))
				.thenReturn((LocalizedValueData)mediaLocalized);

		SimpleBannerComponentData target = new SimpleBannerComponentData();

		when(model.getMedia()).thenReturn(mediaModel);
		when(model.getUrlLink()).thenReturn(MEDIA_LINK);
		when(mediaModel.getCode()).thenReturn(MEDIA_CODE);

		populator.setLocalizationService(localizationService);
		populator.populate(model, target);

		assertThat(((LocalizedValueString)target.getMedia()).getValue(), is(MEDIA_CODE));
		assertThat(target.getUrlLink(), is(MEDIA_LINK));
	}
}
