/*
 *  
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
 */
package de.hybris.platform.warehousing.sourcing.fitness.evaluation.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingContext;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class AllocationEvaluatorTest
{
	private static final Long FIVE = Long.valueOf(5L);
	private static final Long FIFTEEN = Long.valueOf(15L);
	private static final Long TEN = Long.valueOf(10L);

	private static final String CODE_PRODUCT1 = "sku1";
	private static final String CODE_PRODUCT2 = "sku2";

	private final AllocationFitnessEvaluator evaluator = new AllocationFitnessEvaluator();

	@Mock
	private OrderEntryModel entry1;
	@Mock
	private OrderEntryModel entry2;

	private Map<ProductModel, Long> availability;

	private ProductModel product1;
	private ProductModel product2;

	private SourcingContext sourcingContext;
	private SourcingLocation sourcingLocation;

	@Before
	public void setup()
	{
		product1 = new ProductModel();
		product1.setCode(CODE_PRODUCT1);
		product2 = new ProductModel();
		product2.setCode(CODE_PRODUCT2);

		when(entry1.getProduct()).thenReturn(product1);
		when(entry2.getProduct()).thenReturn(product2);

		sourcingContext = new SourcingContext();
		sourcingContext.setOrderEntries(Sets.newHashSet(entry1, entry2));

		availability = new HashMap<>();

		sourcingLocation = new SourcingLocation();
		sourcingLocation.setAvailability(availability);
		sourcingLocation.setContext(sourcingContext);
	}

	@Test
	public void shouldHandleFullAvailability()
	{
		// Given
		when(entry1.getQuantity()).thenReturn(TEN);
		when(entry1.getQuantityUnallocated()).thenReturn(TEN);

		when(entry2.getQuantity()).thenReturn(FIVE);
		when(entry2.getQuantityUnallocated()).thenReturn(FIVE);

		availability.put(product1, TEN);
		availability.put(product2, TEN);

		// When
		final double quantityUnassigned = evaluator.evaluate(sourcingLocation).doubleValue();

		// Then
		Assert.assertThat(Double.valueOf(0d), is(Double.valueOf(quantityUnassigned)));
	}

	@Test
	public void shouldHandlePartialAvailability()
	{
		// Given
		when(entry1.getQuantity()).thenReturn(TEN);
		when(entry1.getQuantityUnallocated()).thenReturn(TEN);

		when(entry2.getQuantity()).thenReturn(FIFTEEN);
		when(entry2.getQuantityUnallocated()).thenReturn(FIFTEEN);

		availability.put(product1, TEN);
		availability.put(product2, TEN);

		// When
		final double quantityUnassigned = evaluator.evaluate(sourcingLocation).doubleValue();

		// Then
		Assert.assertThat(Double.valueOf(5d), is(Double.valueOf(quantityUnassigned)));
	}

	@Test
	public void shouldHandleLargeAvailability()
	{
		// Given
		when(entry1.getQuantity()).thenReturn(TEN);
		when(entry1.getQuantityUnallocated()).thenReturn(TEN);

		when(entry2.getQuantity()).thenReturn(FIFTEEN);
		when(entry2.getQuantityUnallocated()).thenReturn(FIFTEEN);

		availability.put(product1, Long.valueOf(1000L));
		availability.put(product2, Long.valueOf(1000L));

		// When
		final double quantityUnassigned = evaluator.evaluate(sourcingLocation).doubleValue();

		// Then
		Assert.assertThat(Double.valueOf(0d), is(Double.valueOf(quantityUnassigned)));
	}

	@Test
	public void shouldHandleNoAvailability()
	{
		// Given
		when(entry1.getQuantity()).thenReturn(TEN);
		when(entry1.getQuantityUnallocated()).thenReturn(TEN);

		when(entry2.getQuantity()).thenReturn(FIFTEEN);
		when(entry2.getQuantityUnallocated()).thenReturn(FIFTEEN);

		availability.put(product1, Long.valueOf(0L));
		availability.put(product2, Long.valueOf(0L));

		// When
		final double quantityUnassigned = evaluator.evaluate(sourcingLocation).doubleValue();

		// Then
		Assert.assertThat(Double.valueOf(25d), is(Double.valueOf(quantityUnassigned)));
	}

}
