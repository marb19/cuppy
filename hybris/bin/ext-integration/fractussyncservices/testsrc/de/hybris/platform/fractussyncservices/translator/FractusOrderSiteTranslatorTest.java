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
 */

package de.hybris.platform.fractussyncservices.translator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.fractussyncservices.adapter.FractusOrderSiteAdapter;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.jalo.Item;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class FractusOrderSiteTranslatorTest
{

	@InjectMocks
	private FractusOrderSiteTranslator translator;

	@Mock
	private FractusOrderSiteAdapter fractusOrderSiteAdapter;

	@Mock
	private Item item;

	@Before
	public void setup()
	{
		translator = new FractusOrderSiteTranslator();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldPerformImport() throws ImpExException
	{

		final String cellVal = "cellVal";

		translator.performImport(cellVal, item);

		Mockito.verify(fractusOrderSiteAdapter, Mockito.times(1)).performImport(cellVal, item);
	}

}
