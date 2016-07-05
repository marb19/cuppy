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
package de.hybris.platform.sap.productconfig.runtime.ssc.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.runtime.interf.impl.ConfigModelFactoryImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.PriceModel;

import java.math.BigDecimal;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.sap.custdev.projects.fbs.slc.cfg.client.ICsticValueData;
import com.sap.custdev.projects.fbs.slc.cfg.client.IPriceData;


@UnitTest
public class ConfigurationProviderSSCImplTest extends ConfigurationProviderSSCTestBase
{
	@Mock
	protected IPriceData price;

	@Override
	protected BaseConfigurationProviderSSCImpl createProvider()
	{
		return new ConfigurationProviderSSCImpl();
	}

	@Test
	public void testSetSelectable() throws Exception
	{
		final ConfigurationProviderSSCImpl myProvider = new ConfigurationProviderSSCImpl();
		myProvider.setConfigModelFactory(new ConfigModelFactoryImpl());

		final ICsticValueData csticValueData = ConfigurationSSCTestData.createCsticValueData();
		csticValueData.setValueSelectable("Y");

		final CsticValueModel modelValue = myProvider.createModelValue(csticValueData);

		assertTrue(modelValue.isSelectable());
	}

	@Test
	public void testDeltaPriceMapping() throws Exception
	{
		final ConfigurationProviderSSCImpl myProvider = new ConfigurationProviderSSCImpl();
		myProvider.setConfigModelFactory(new ConfigModelFactoryImpl());

		final ICsticValueData csticValueData = ConfigurationSSCTestData.createCsticValueData();
		csticValueData.setDeltaPrice(price);
		Mockito.when(price.getPricingRate()).thenReturn(BigDecimal.ONE);
		Mockito.when(price.getPricingUnit()).thenReturn("USD");

		final CsticValueModel modelValue = myProvider.createModelValue(csticValueData);

		assertTrue("wrong price value", 0 == BigDecimal.ONE.compareTo(modelValue.getDeltaPrice().getPriceValue()));
		assertEquals("wrong currency", "USD", modelValue.getDeltaPrice().getCurrency());
	}

	@Test
	public void testDeltaPriceMapping_emptyPrice() throws Exception
	{
		final ConfigurationProviderSSCImpl myProvider = new ConfigurationProviderSSCImpl();
		myProvider.setConfigModelFactory(new ConfigModelFactoryImpl());

		final ICsticValueData csticValueData = ConfigurationSSCTestData.createCsticValueData();
		csticValueData.setDeltaPrice(price);
		Mockito.when(price.getPricingRate()).thenReturn(BigDecimal.ZERO);
		Mockito.when(price.getPricingUnit()).thenReturn("");

		final CsticValueModel modelValue = myProvider.createModelValue(csticValueData);

		assertSame(PriceModel.NO_PRICE, modelValue.getDeltaPrice());
	}

	@Test
	public void testDeltaPriceMapping_noPrice() throws Exception
	{
		final ConfigurationProviderSSCImpl myProvider = new ConfigurationProviderSSCImpl();
		myProvider.setConfigModelFactory(new ConfigModelFactoryImpl());

		final ICsticValueData csticValueData = ConfigurationSSCTestData.createCsticValueData();
		csticValueData.setDeltaPrice(null);

		final CsticValueModel modelValue = myProvider.createModelValue(csticValueData);

		assertSame(PriceModel.NO_PRICE, modelValue.getDeltaPrice());
	}

	@Test
	public void testDeltaPriceMapping_zeroPrice() throws Exception
	{
		final ConfigurationProviderSSCImpl myProvider = new ConfigurationProviderSSCImpl();
		myProvider.setConfigModelFactory(new ConfigModelFactoryImpl());

		final ICsticValueData csticValueData = ConfigurationSSCTestData.createCsticValueData();
		csticValueData.setDeltaPrice(price);
		Mockito.when(price.getPricingRate()).thenReturn(BigDecimal.ZERO);
		Mockito.when(price.getPricingUnit()).thenReturn("USD");

		final CsticValueModel modelValue = myProvider.createModelValue(csticValueData);

		assertTrue("wrong price value", 0 == BigDecimal.ZERO.compareTo(modelValue.getDeltaPrice().getPriceValue()));
		assertEquals("wrong currency", "USD", modelValue.getDeltaPrice().getCurrency());
	}
}
