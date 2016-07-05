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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.cmswebservices.data.CMSParagraphComponentData;
import de.hybris.platform.cmswebservices.localization.service.LocalizationService;
import de.hybris.platform.servicelayer.session.SessionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CmsParagraphComponentItemPopulatorTest
{


	@Mock
	CMSParagraphComponentModel source;

	@Mock
	SessionService sessionService;

	@Mock
	LocalizationService localizationService;


	@Test
	public void shouldPopulateTheWholeStructure()
	{
		CMSParagraphComponentData target = new CMSParagraphComponentData();

		CmsParagraphComponentModelPopulator populator = new CmsParagraphComponentModelPopulator();
		populator.setLocalizationService(localizationService);
		populator.populate(source, target);

		assertEquals(source.getContent(), target.getContent());
	}
}
