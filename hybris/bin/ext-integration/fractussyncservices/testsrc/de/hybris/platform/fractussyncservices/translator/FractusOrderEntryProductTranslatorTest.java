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

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.fractussyncservices.adapter.FractusValueOrderEntryProductAdapter;
import de.hybris.platform.jalo.Item;


@UnitTest
public class FractusOrderEntryProductTranslatorTest
{
	@InjectMocks
	private FractusOrderEntryProductTranslator translator;

	@Mock
	private FractusValueOrderEntryProductAdapter fractusOrderEntryProductAdapter;

	@Mock
	private Item item;

	@Before
	public void setup()
	{
		translator = new FractusOrderEntryProductTranslator();
		MockitoAnnotations.initMocks(this);
	}


	@Test
	public void shouldPerformImport()
	{
		final String cellVal = "productCode|orderCode";

		translator.importValue(cellVal, item);

		Mockito.verify(fractusOrderEntryProductAdapter, Mockito.times(1)).performImport(cellVal);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void shouldThrowUnsupportedOperationException()
	{
		final Object o = "anyStringObject";

		translator.exportValue(o);
	}
}
