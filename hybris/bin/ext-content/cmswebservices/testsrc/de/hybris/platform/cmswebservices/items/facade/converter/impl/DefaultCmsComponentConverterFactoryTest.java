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
package de.hybris.platform.cmswebservices.items.facade.converter.impl;


import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.cmswebservices.data.AbstractCMSComponentData;
import de.hybris.platform.cmswebservices.items.facade.converter.impl.DefaultCmsComponentConverterFactory;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCmsComponentConverterFactoryTest
{

	private Map<Class<? extends AbstractCMSComponentModel>, AbstractPopulatingConverter<AbstractCMSComponentModel, AbstractCMSComponentData>> converters = new HashMap<>();

	private DefaultCmsComponentConverterFactory cmsComponentConverterFactory;

	@Mock
	private AbstractPopulatingConverter converter;

	private CMSParagraphComponentModel paragraphModel = new CMSParagraphComponentModel();

	@Before
	public void setUp()
	{
		converters.put(CMSParagraphComponentModel.class, converter);
		cmsComponentConverterFactory = new DefaultCmsComponentConverterFactory(converters);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getConverterWithAbstractModelNotFound()
	{
		cmsComponentConverterFactory.getConverter(CMSLinkComponentModel.class).orElseThrow(IllegalArgumentException::new);
	}

	@Test
	public void getConverterWithParagraphModel()
	{

		Assert.assertTrue("Converter is not present" + paragraphModel.getClass(), cmsComponentConverterFactory.getConverter(paragraphModel.getClass()).isPresent());
	}
}
