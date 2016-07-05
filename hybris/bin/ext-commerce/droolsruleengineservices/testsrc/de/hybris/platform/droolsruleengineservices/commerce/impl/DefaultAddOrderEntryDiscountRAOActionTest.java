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
package de.hybris.platform.droolsruleengineservices.commerce.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ruleengineservices.enums.OrderEntrySelectionStrategy;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;
import de.hybris.platform.ruleengineservices.rao.EntriesSelectionStrategyRPD;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@UnitTest
public class DefaultAddOrderEntryDiscountRAOActionTest extends AbstractRAOActionTest
{
	private DefaultAddOrderEntryDiscountRAOAction action;

	@Before
	public void setUp()
	{
		action = new DefaultAddOrderEntryDiscountRAOAction();
		action.setRuleEngineCalculationService(getRuleEngineCalculationService());
		action.setEntriesSelectionStrategies(getUnitForBundleSelectorStrategies());
	}

	@Test
	public void testaddOrderEntryLevelDiscountAbsoluteWithEntriesSelectionStrategies()
	{
		//Order threshold perfect partner fixed price promotion. Get 1 digital camera, 1 tripod and 2 memory cards and get
		//2 memory cards with 4USD discount per unit.

		final CartRAO cartRao1 = createCartRAO("cart01", USD);
		cartRao1.setEntries(new HashSet<OrderEntryRAO>());


		final OrderEntryRAO orderEntry1 = createOrderEntryRAO(cartRao1, "10.00", USD, 2, 0);
		orderEntry1.getProduct().setCode("872912");
		cartRao1.getEntries().add(orderEntry1);

		final OrderEntryRAO orderEntry2 = createOrderEntryRAO(cartRao1, "147.04", USD, 1, 1);
		orderEntry2.getProduct().setCode("325414");
		cartRao1.getEntries().add(orderEntry2);

		final OrderEntryRAO orderEntry3 = createOrderEntryRAO(cartRao1, "580.38", USD, 1, 2);
		orderEntry3.getProduct().setCode("454831");
		cartRao1.getEntries().add(orderEntry3);

		getRuleEngineCalculationService().calculateTotals(cartRao1);
		Assert.assertEquals(new BigDecimal("747.42"), cartRao1.getTotal());

		final List<EntriesSelectionStrategyRPD> entriesSelectionStrategyRPDs = new ArrayList<EntriesSelectionStrategyRPD>();

		final EntriesSelectionStrategyRPD entriesSelectionStrategyRPD1 = createEntriesSelectionStrategyRPD(
				OrderEntrySelectionStrategy.CHEAPEST, 2, true, orderEntry1);
		entriesSelectionStrategyRPDs.add(entriesSelectionStrategyRPD1);

		final EntriesSelectionStrategyRPD entriesSelectionStrategyRPD2 = createEntriesSelectionStrategyRPD(
				OrderEntrySelectionStrategy.CHEAPEST, 1, false, orderEntry2);
		entriesSelectionStrategyRPDs.add(entriesSelectionStrategyRPD2);

		final EntriesSelectionStrategyRPD entriesSelectionStrategyRPD3 = createEntriesSelectionStrategyRPD(
				OrderEntrySelectionStrategy.CHEAPEST, 1, false, orderEntry3);
		entriesSelectionStrategyRPDs.add(entriesSelectionStrategyRPD3);

		final List<DiscountRAO> discountRAOs = action.addOrderEntryLevelDiscount(entriesSelectionStrategyRPDs, true,
				BigDecimal.valueOf(4.00), getResult(), getContext());

		getRuleEngineCalculationService().calculateTotals(cartRao1);

		Assert.assertEquals(1, discountRAOs.size());
		Assert.assertEquals(new BigDecimal("4.00"), discountRAOs.get(0).getValue());
		Assert.assertEquals(2, discountRAOs.get(0).getAppliedToQuantity());
		Assert.assertEquals(new BigDecimal("739.42"), cartRao1.getTotal());
	}

	@Test
	public void testaddOrderEntryLevelDiscountPercenteWithEntriesSelectionStrategies()
	{
		//Order threshold perfect partner fixed price promotion. Get 1 digital camera, 1 tripod and 2 memory cards and get
		//2 memory cards with 50% discount per unit.

		final CartRAO cartRao1 = createCartRAO("cart01", USD);
		cartRao1.setEntries(new HashSet<OrderEntryRAO>());


		final OrderEntryRAO orderEntry1 = createOrderEntryRAO(cartRao1, "10.00", USD, 2, 0);
		orderEntry1.getProduct().setCode("872912");
		cartRao1.getEntries().add(orderEntry1);

		final OrderEntryRAO orderEntry2 = createOrderEntryRAO(cartRao1, "147.04", USD, 1, 1);
		orderEntry2.getProduct().setCode("325414");
		cartRao1.getEntries().add(orderEntry2);

		final OrderEntryRAO orderEntry3 = createOrderEntryRAO(cartRao1, "580.38", USD, 1, 2);
		orderEntry3.getProduct().setCode("454831");
		cartRao1.getEntries().add(orderEntry3);

		getRuleEngineCalculationService().calculateTotals(cartRao1);
		Assert.assertEquals(new BigDecimal("747.42"), cartRao1.getTotal());

		final List<EntriesSelectionStrategyRPD> entriesSelectionStrategyRPDs = new ArrayList<EntriesSelectionStrategyRPD>();

		final EntriesSelectionStrategyRPD entriesSelectionStrategyRPD1 = createEntriesSelectionStrategyRPD(
				OrderEntrySelectionStrategy.CHEAPEST, 2, true, orderEntry1);
		entriesSelectionStrategyRPDs.add(entriesSelectionStrategyRPD1);

		final EntriesSelectionStrategyRPD entriesSelectionStrategyRPD2 = createEntriesSelectionStrategyRPD(
				OrderEntrySelectionStrategy.CHEAPEST, 1, false, orderEntry2);
		entriesSelectionStrategyRPDs.add(entriesSelectionStrategyRPD2);

		final EntriesSelectionStrategyRPD entriesSelectionStrategyRPD3 = createEntriesSelectionStrategyRPD(
				OrderEntrySelectionStrategy.CHEAPEST, 1, false, orderEntry3);
		entriesSelectionStrategyRPDs.add(entriesSelectionStrategyRPD3);

		final List<DiscountRAO> discountRAOs = action.addOrderEntryLevelDiscount(entriesSelectionStrategyRPDs, false,
				BigDecimal.valueOf(50.00), getResult(), getContext());

		getRuleEngineCalculationService().calculateTotals(cartRao1);

		Assert.assertEquals(1, discountRAOs.size());
		Assert.assertEquals(new BigDecimal("50.0"), discountRAOs.get(0).getValue());
		Assert.assertNull(discountRAOs.get(0).getCurrencyIsoCode());
		Assert.assertEquals(2, discountRAOs.get(0).getAppliedToQuantity());
		Assert.assertEquals(new BigDecimal("737.42"), cartRao1.getTotal());
	}
}
