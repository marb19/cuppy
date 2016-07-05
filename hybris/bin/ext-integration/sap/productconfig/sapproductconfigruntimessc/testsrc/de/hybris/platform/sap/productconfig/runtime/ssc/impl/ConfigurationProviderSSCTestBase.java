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

import de.hybris.bootstrap.annotations.ManualTest;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.InstanceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticValueModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.InstanceModelImpl;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.sap.custdev.projects.fbs.slc.cfg.command.beans.FactData;


@ManualTest
public abstract class ConfigurationProviderSSCTestBase
{
	@Mock
	I18NService i18nService;


	protected BaseConfigurationProviderSSCImpl provider;

	protected abstract BaseConfigurationProviderSSCImpl createProvider();

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		provider = createProvider();
	}


	@Test
	public void testReplaceConflictTextNoText()
	{
		final BaseConfigurationProviderSSCImpl sscProvider = provider;

		final IntervalInDomainHelperImpl intervalInDomainHelper = Mockito.spy(new IntervalInDomainHelperImpl());
		Mockito.doReturn("Value 40 is not part of interval 10 - 20 ; 50 - 60").when(intervalInDomainHelper)
				.retrieveErrorMessage("40", "10 - 20 ; 50 - 60");
		intervalInDomainHelper.setI18NService(i18nService);
		Mockito.when(i18nService.getCurrentLocale()).thenReturn(Locale.ENGLISH);
		sscProvider.setIntervalInDomainHelper(intervalInDomainHelper);

		final CsticModel cstic = new CsticModelImpl();
		cstic.setName("CSTIC1");
		cstic.setIntervalInDomain(true);
		cstic.setValueType(CsticModel.TYPE_INTEGER);

		final List<CsticValueModel> assignedValues = new ArrayList<CsticValueModel>();
		final CsticValueModel csticValue = new CsticValueModelImpl();
		csticValue.setName("40");
		assignedValues.add(csticValue);
		cstic.setAssignedValuesWithoutCheckForChange(assignedValues);

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

		final String conflictText = sscProvider.replaceConflictText(cstic, null);

		assertEquals("Value 40 is not part of interval 10 - 20 ; 50 - 60", conflictText);
	}

	@Test
	public void testReplaceConflictTextWhenTextExists()
	{
		final BaseConfigurationProviderSSCImpl sscProvider = provider;
		final String conflictText = sscProvider.replaceConflictText(new CsticModelImpl(), "ConflictText");
		assertEquals("ConflictText", conflictText);
	}

	@Test
	public void testAddConflictingCsticNewConflict()
	{
		final BaseConfigurationProviderSSCImpl sscProvider = provider;

		final CsticModel cstic = new CsticModelImpl();
		cstic.setName("CSTIC1");
		final List<CsticModel> cstics = new ArrayList<>();
		cstics.add(cstic);
		final InstanceModel instance = new InstanceModelImpl();
		instance.setCstics(cstics);

		final FactData fact = new FactData();
		fact.setObsName(cstic.getName());

		final List<CsticModel> conflictingCsics = new ArrayList<>();

		sscProvider.addConflictingCstic(instance, conflictingCsics, fact);
		assertEquals(1, conflictingCsics.size());
		assertEquals(cstic, conflictingCsics.get(0));
	}

	@Test
	public void testAddConflictingCsticExistingConflict()
	{
		final BaseConfigurationProviderSSCImpl sscProvider = provider;

		final CsticModel cstic = new CsticModelImpl();
		cstic.setName("CSTIC1");
		final List<CsticModel> cstics = new ArrayList<>();
		cstics.add(cstic);
		final InstanceModel instance = new InstanceModelImpl();
		instance.setCstics(cstics);

		final FactData fact = new FactData();
		fact.setObsName(cstic.getName());

		final List<CsticModel> conflictingCsics = new ArrayList<>();
		conflictingCsics.add(cstic);

		sscProvider.addConflictingCstic(instance, conflictingCsics, fact);
		assertEquals(1, conflictingCsics.size());
		assertEquals(cstic, conflictingCsics.get(0));
	}

	@Test
	public void testAddConflictingCsticDifferentInstance()
	{
		final BaseConfigurationProviderSSCImpl sscProvider = provider;

		final CsticModel cstic = new CsticModelImpl();
		cstic.setName("CSTIC1");
		final List<CsticModel> cstics = new ArrayList<>();

		final InstanceModel instance = new InstanceModelImpl();
		instance.setCstics(cstics);

		final FactData fact = new FactData();
		fact.setObsName(cstic.getName());

		final List<CsticModel> conflictingCsics = new ArrayList<>();

		sscProvider.addConflictingCstic(instance, conflictingCsics, fact);
		assertEquals(0, conflictingCsics.size());
	}


}
