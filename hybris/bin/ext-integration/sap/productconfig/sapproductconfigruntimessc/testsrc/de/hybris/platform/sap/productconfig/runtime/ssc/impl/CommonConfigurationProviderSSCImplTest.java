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
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.PriceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticValueModelImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.sap.custdev.projects.fbs.slc.kbo.local.OrchestratedCstic;
import com.sap.sce.front.base.Cstic;
import com.sap.sce.front.base.PricingConditionRate;


@UnitTest
public class CommonConfigurationProviderSSCImplTest extends ConfigurationProviderSSCTestBase
{
	@Mock
	protected OrchestratedCstic mockedOrchestratedCstic;

	@Mock
	Cstic mockedFirstSharedCstic;

	@Mock
	PricingConditionRate mockedPricingConditionRate;

	@Mock
	IntervalInDomainHelperImpl intervallInDomainHelperImpl;

	@Override
	protected BaseConfigurationProviderSSCImpl createProvider()
	{
		return new CommonConfigurationProviderSSCImpl();
	}


	@Test
	public void testDeltaPriceMapping() throws Exception
	{
		final CommonConfigurationProviderSSCImpl myProvider = new CommonConfigurationProviderSSCImpl();
		myProvider.setConfigModelFactory(new ConfigModelFactoryImpl());

		final String valueName = "ABC";
		Mockito.when(mockedOrchestratedCstic.getValueLangDependentName("ABC")).thenReturn("abc");
		Mockito.when(Boolean.valueOf(mockedOrchestratedCstic.isValueUserOwned("ABC"))).thenReturn(Boolean.TRUE);
		Mockito.when(mockedPricingConditionRate.getConditionRateValue()).thenReturn(BigDecimal.ONE);
		Mockito.when(mockedPricingConditionRate.getConditionRateUnitName()).thenReturn("USD");

		final CsticValueModel modelValue = myProvider.createModelValue(mockedOrchestratedCstic, valueName, true, true,
				mockedPricingConditionRate);

		assertTrue("wrong price value", 0 == BigDecimal.ONE.compareTo(modelValue.getDeltaPrice().getPriceValue()));
		assertEquals("wrong currency", "USD", modelValue.getDeltaPrice().getCurrency());
	}

	@Test
	public void testDeltaPriceMapping_emptyPrice() throws Exception
	{
		final CommonConfigurationProviderSSCImpl myProvider = new CommonConfigurationProviderSSCImpl();
		myProvider.setConfigModelFactory(new ConfigModelFactoryImpl());

		final String valueName = "ABC";
		Mockito.when(mockedOrchestratedCstic.getValueLangDependentName("ABC")).thenReturn("abc");
		Mockito.when(Boolean.valueOf(mockedOrchestratedCstic.isValueUserOwned("ABC"))).thenReturn(Boolean.TRUE);
		Mockito.when(mockedPricingConditionRate.getConditionRateValue()).thenReturn(BigDecimal.ZERO);
		Mockito.when(mockedPricingConditionRate.getConditionRateUnitName()).thenReturn("");

		final CsticValueModel modelValue = myProvider.createModelValue(mockedOrchestratedCstic, valueName, true, true,
				mockedPricingConditionRate);

		assertSame(PriceModel.NO_PRICE, modelValue.getDeltaPrice());
	}

	@Test
	public void testDeltaPriceMapping_noPrice() throws Exception
	{
		final CommonConfigurationProviderSSCImpl myProvider = new CommonConfigurationProviderSSCImpl();
		myProvider.setConfigModelFactory(new ConfigModelFactoryImpl());

		final String valueName = "ABC";
		Mockito.when(mockedOrchestratedCstic.getValueLangDependentName("ABC")).thenReturn("abc");
		Mockito.when(Boolean.valueOf(mockedOrchestratedCstic.isValueUserOwned("ABC"))).thenReturn(Boolean.TRUE);

		final CsticValueModel modelValue = myProvider.createModelValue(mockedOrchestratedCstic, valueName, true, true, null);

		assertSame(PriceModel.NO_PRICE, modelValue.getDeltaPrice());
	}

	@Test
	public void testDeltaPriceMapping_zeroPrice() throws Exception
	{
		final CommonConfigurationProviderSSCImpl myProvider = new CommonConfigurationProviderSSCImpl();
		myProvider.setConfigModelFactory(new ConfigModelFactoryImpl());

		final String valueName = "ABC";
		Mockito.when(mockedOrchestratedCstic.getValueLangDependentName("ABC")).thenReturn("abc");
		Mockito.when(Boolean.valueOf(mockedOrchestratedCstic.isValueUserOwned("ABC"))).thenReturn(Boolean.TRUE);
		Mockito.when(mockedPricingConditionRate.getConditionRateValue()).thenReturn(BigDecimal.ZERO);
		Mockito.when(mockedPricingConditionRate.getConditionRateUnitName()).thenReturn("USD");

		final CsticValueModel modelValue = myProvider.createModelValue(mockedOrchestratedCstic, valueName, true, true,
				mockedPricingConditionRate);

		assertTrue("wrong price value", 0 == BigDecimal.ZERO.compareTo(modelValue.getDeltaPrice().getPriceValue()));
		assertEquals("wrong currency", "USD", modelValue.getDeltaPrice().getCurrency());
	}

	@Test
	public void testCreateCsticValues() throws Exception
	{
		final CommonConfigurationProviderSSCImpl myProvider = new CommonConfigurationProviderSSCImpl();
		myProvider.setConfigModelFactory(new ConfigModelFactoryImpl());

		final CsticModel csticModel = new CsticModelImpl();
		csticModel.setAllowsAdditionalValues(true);
		csticModel.setConstrained(true);
		csticModel.setAuthor(CsticModel.AUTHOR_USER);

		Mockito.when(mockedOrchestratedCstic.getValues()).thenReturn("A D".split(" "));
		Mockito.when(mockedOrchestratedCstic.getDynamicDomain()).thenReturn("A B C".split(" "));
		Mockito.when(mockedOrchestratedCstic.getTypicalDomain()).thenReturn("A B C".split(" "));
		Mockito.when(mockedOrchestratedCstic.getFirstSharedCstic()).thenReturn(mockedFirstSharedCstic);
		Mockito.when(mockedFirstSharedCstic.getDeltaPrices()).thenReturn(null);

		Mockito.when(mockedOrchestratedCstic.getValueLangDependentName(Mockito.anyString())).thenReturn("xxx");
		Mockito.when(Boolean.valueOf(mockedOrchestratedCstic.isValueUserOwned(Mockito.anyString()))).thenReturn(Boolean.TRUE);
		Mockito.when(Boolean.valueOf(mockedOrchestratedCstic.isValueDefault(Mockito.anyString()))).thenReturn(Boolean.TRUE);

		myProvider.createCsticValues(mockedOrchestratedCstic, csticModel);

		final List<CsticValueModel> assignableValues = csticModel.getAssignableValues();
		final List<CsticValueModel> assignedValues = csticModel.getAssignedValues();

		assertEquals("wrong number assignable values", 4, assignableValues.size());
		assertEquals("wrong number assigned values", 2, assignedValues.size());

		assertEquals("wrong assignable values [0]", "A", assignableValues.get(0).getName());
		assertEquals("wrong assignable values [1]", "B", assignableValues.get(1).getName());
		assertEquals("wrong assignable values [2]", "C", assignableValues.get(2).getName());
		assertEquals("wrong assignable values [3]", "D", assignableValues.get(3).getName());
		assertTrue("value should be selectable", assignableValues.get(0).isSelectable());
		assertTrue("value should be selectable", assignableValues.get(1).isSelectable());
		assertTrue("value should be selectable", assignableValues.get(2).isSelectable());
		assertTrue("value should be selectable", assignableValues.get(3).isSelectable());
		assertTrue("value should be a domain value", assignableValues.get(0).isDomainValue());
		assertTrue("value should be a domain value", assignableValues.get(1).isDomainValue());
		assertTrue("value should be a domain value", assignableValues.get(2).isDomainValue());
		assertTrue("value should not be a domain value", !assignableValues.get(3).isDomainValue());

		assertEquals("wrong assigned values [0]", "A", assignedValues.get(0).getName());
		assertEquals("wrong assigned values [1]", "D", assignedValues.get(1).getName());

		assertEquals("wrong cstic author", CsticModel.AUTHOR_DEFAULT, csticModel.getAuthor());
	}

	@Test
	public void testPreparePlaceholderForInterval() throws Exception
	{
		final CommonConfigurationProviderSSCImpl sscProviderCommon = new CommonConfigurationProviderSSCImpl();
		sscProviderCommon.setIntervalInDomainHelper(intervallInDomainHelperImpl);


		final CsticModel cstic = new CsticModelImpl();
		cstic.setIntervalInDomain(true);
		cstic.setValueType(CsticModel.TYPE_INTEGER);

		final List<CsticValueModel> assignableValues = new ArrayList<CsticValueModel>();
		final CsticValueModel csticValueInterval1 = new CsticValueModelImpl();
		csticValueInterval1.setName("10 - 20");
		csticValueInterval1.setDomainValue(true);
		assignableValues.add(csticValueInterval1);
		final CsticValueModel csticValueInterval2 = new CsticValueModelImpl();
		csticValueInterval2.setName("50 - 60");
		csticValueInterval2.setDomainValue(true);
		assignableValues.add(csticValueInterval2);
		cstic.setAssignableValues(assignableValues);

		Mockito.when(intervallInDomainHelperImpl.retrieveIntervalMask(cstic)).thenReturn("10 - 20 ; 50 - 60");
		sscProviderCommon.preparePlaceholderForInterval(cstic);
		assertEquals("10 - 20 ; 50 - 60", cstic.getPlaceholder());
	}

	@Test
	public void testAdjustIntervalInDomain() throws Exception
	{
		final CommonConfigurationProviderSSCImpl sscProviderCommon = new CommonConfigurationProviderSSCImpl();

		final CsticModel cstic = new CsticModelImpl();
		cstic.setIntervalInDomain(false);
		cstic.setAllowsAdditionalValues(true);
		cstic.setValueType(CsticModel.TYPE_INTEGER);

		final List<CsticValueModel> assignableValues = new ArrayList<CsticValueModel>();
		final CsticValueModel csticValueInterval1 = new CsticValueModelImpl();
		csticValueInterval1.setName("10 - 20");
		csticValueInterval1.setDomainValue(true);
		assignableValues.add(csticValueInterval1);
		final CsticValueModel csticValueInterval2 = new CsticValueModelImpl();
		csticValueInterval2.setName("50 - 60");
		csticValueInterval2.setDomainValue(true);
		assignableValues.add(csticValueInterval2);
		cstic.setAssignableValues(assignableValues);

		sscProviderCommon.adjustIntervalInDomain(cstic);
		assertTrue(cstic.isIntervalInDomain());
	}

}
