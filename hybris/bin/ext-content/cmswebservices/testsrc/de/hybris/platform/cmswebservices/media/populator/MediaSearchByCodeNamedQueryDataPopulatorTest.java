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
package de.hybris.platform.cmswebservices.media.populator;


import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cmswebservices.data.NamedQueryData;
import de.hybris.platform.cmswebservices.media.facade.populator.MediaSearchByCodeNamedQueryDataPopulator;
import de.hybris.platform.cmswebservices.namedquery.NamedQuery;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class MediaSearchByCodeNamedQueryDataPopulatorTest
{

	private static final String PERCENT = "%";
	private static final String CODE_VALUE = "code-value";
	public static final String CATALOG_ID_VALUE = "catalogId-value";
	public static final String CATALOG_VERSION_VALUE = "catalogVersion-value";
	private static final String GOOD_PARAMS = //
			MediaSearchByCodeNamedQueryDataPopulator.PARAM_CODE + ":" + CODE_VALUE //
					+ "," //
					+ MediaSearchByCodeNamedQueryDataPopulator.PARAM_CATALOG_ID + ":" + CATALOG_ID_VALUE //
					+ "," //
					+ MediaSearchByCodeNamedQueryDataPopulator.PARAM_CATALOG_VERSION + ":" + CATALOG_VERSION_VALUE;

	private static final String BAD_PARAMS_DUPLICATE = //
			MediaSearchByCodeNamedQueryDataPopulator.PARAM_CODE + ":" + CODE_VALUE //
					+ "," //
					+ MediaSearchByCodeNamedQueryDataPopulator.PARAM_CATALOG_ID + ":" + CATALOG_ID_VALUE //
					+ "," //
					+ MediaSearchByCodeNamedQueryDataPopulator.PARAM_CATALOG_ID + ":" + CATALOG_VERSION_VALUE;

	private static final String BAD_PARAMS_EMPTY = //
			MediaSearchByCodeNamedQueryDataPopulator.PARAM_CODE + ":"  //
					+ "," //
					+ MediaSearchByCodeNamedQueryDataPopulator.PARAM_CATALOG_ID + ":" //
					+ "," //
					+ MediaSearchByCodeNamedQueryDataPopulator.PARAM_CATALOG_ID + ":";
	@Mock
	CatalogVersionService catalogVersionService;

	@InjectMocks
	MediaSearchByCodeNamedQueryDataPopulator populator;

	@Test(expected = ConversionException.class)
	public void testEmptyParametersException()
	{
		final NamedQueryData namedQueryData = new NamedQueryData();

		final NamedQuery namedQuery = new NamedQuery();
		populator.populate(namedQueryData, namedQuery);
	}

	@Test
	public void testGoodParameterRequestConversion()
	{
		final NamedQueryData namedQueryData = new NamedQueryData();
		namedQueryData.setParams(GOOD_PARAMS);

		final CatalogVersionModel catalogVersionModel = Mockito.mock(CatalogVersionModel.class);

		Mockito.when(catalogVersionService.getCatalogVersion(CATALOG_ID_VALUE, CATALOG_VERSION_VALUE)).then(
				r -> catalogVersionModel
		);

		final NamedQuery namedQuery = new NamedQuery();
		populator.populate(namedQueryData, namedQuery);


		Assert.assertEquals(PERCENT + CODE_VALUE + PERCENT, namedQuery.getParameters().get(MediaModel.CODE));
		Assert.assertEquals(catalogVersionModel, namedQuery.getParameters().get(MediaModel.CATALOGVERSION));
	}


	@Test(expected = ConversionException.class)
	public void testBadParameterWithDuplicateKey()
	{
		final NamedQueryData namedQueryData = new NamedQueryData();
		namedQueryData.setParams(BAD_PARAMS_DUPLICATE);

		final CatalogVersionModel catalogVersionModel = Mockito.mock(CatalogVersionModel.class);

		Mockito.when(catalogVersionService.getCatalogVersion(CATALOG_ID_VALUE, CATALOG_VERSION_VALUE)).then(
				r -> catalogVersionModel
		);

		final NamedQuery namedQuery = new NamedQuery();
		populator.populate(namedQueryData, namedQuery);
	}


	@Test(expected = ConversionException.class)
	public void testGoodParametersButWithBadCatalogVersionInput()
	{
		final NamedQueryData namedQueryData = new NamedQueryData();
		namedQueryData.setParams(GOOD_PARAMS);

		Mockito.when(catalogVersionService.getCatalogVersion(CATALOG_ID_VALUE, CATALOG_VERSION_VALUE)).thenThrow(
				UnknownIdentifierException.class);

		final NamedQuery namedQuery = new NamedQuery();
		populator.populate(namedQueryData, namedQuery);
	}


	@Test(expected = ConversionException.class)
	public void testBadParametersInputWithEmptyValues()
	{
		final NamedQueryData namedQueryData = new NamedQueryData();
		namedQueryData.setParams(BAD_PARAMS_EMPTY);

		final CatalogVersionModel catalogVersionModel = Mockito.mock(CatalogVersionModel.class);

		Mockito.when(catalogVersionService.getCatalogVersion(CATALOG_ID_VALUE, CATALOG_VERSION_VALUE)).then(
				r -> catalogVersionModel);

		final NamedQuery namedQuery = new NamedQuery();
		populator.populate(namedQueryData, namedQuery);
	}
}
