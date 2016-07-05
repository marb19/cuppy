/*
 *
 * [y] hybris Platform 
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 */
package de.hybris.platform.warehousing.sourcing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.warehousing.data.sourcing.SourcingResults;
import de.hybris.platform.warehousing.util.BaseSourcingIntegrationTest;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class SourceDeliveryHugeEntriesIntegrationTest extends BaseSourcingIntegrationTest
{
	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void shouldSource_Split50Times()
	{
		// Given
		setSourcingFactors(0, 1000, 0);
		for (int i = 0; i < 500; i++)
			stockLevels.Camera(warehouses.Random(), 2);
		// When
		final SourcingResults results = sourcingService.sourceOrder(orders.Camera_Shipped(new Long(1000)));
		assertTrue(results.isComplete());
		assertEquals(500, results.getResults().size());
	}
}
