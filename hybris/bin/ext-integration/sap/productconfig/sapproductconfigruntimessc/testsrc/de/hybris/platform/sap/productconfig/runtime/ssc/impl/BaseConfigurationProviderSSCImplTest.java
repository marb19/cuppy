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

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConflictingAssumptionModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.SolvableConflictModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.ConfigModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.ConflictingAssumptionModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.SolvableConflictModelImpl;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sap.custdev.projects.fbs.slc.cfg.client.IConfigSessionClient;
import com.sap.custdev.projects.fbs.slc.cfg.exception.IpcCommandException;


/**
 * Unit Tests
 */
@UnitTest
public class BaseConfigurationProviderSSCImplTest
{
	BaseConfigurationProviderSSCImpl classUnderTest = new BaseConfigurationProviderSSCImpl()
	{

		@Override
		protected ConfigModel fillConfigModel(final String qualifiedId)
		{
			return null;
		}
	};
	private CsticModel csticModel;
	private ConfigModel configModel;
	@Mock
	private IConfigSessionClient session;
	private String configId;
	private SolvableConflictModel solvableConflict;
	private String name;
	private ConflictingAssumptionModel conflictingAssumption;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		csticModel = new CsticModelImpl();
		name = "CsticName";
		csticModel.setName(name);
		configModel = new ConfigModelImpl();
		configId = "1";
		classUnderTest.setConflictAdapter(new SolvableConflictAdapterImpl());
		solvableConflict = new SolvableConflictModelImpl();
		conflictingAssumption = new ConflictingAssumptionModelImpl();
	}

	@Test
	public void testHasBeenRetractedNoRetraction() throws IpcCommandException
	{
		classUnderTest.hasBeenRetracted(csticModel, configModel, session, configId);
	}

	@Test(expected = IllegalStateException.class)
	public void testHasBeenRetractedNoAssumptions() throws IpcCommandException
	{
		csticModel.setRetractTriggered(true);
		classUnderTest.hasBeenRetracted(csticModel, configModel, session, configId);
	}

	@Test(expected = IllegalStateException.class)
	public void testHasBeenRetractedAssumptionsNoMatch() throws IpcCommandException
	{
		csticModel.setRetractTriggered(true);
		configModel.setSolvableConflicts(Arrays.asList(solvableConflict));
		classUnderTest.hasBeenRetracted(csticModel, configModel, session, configId);
	}

	@Test
	public void testHasBeenRetractedAssumptions() throws IpcCommandException
	{
		csticModel.setRetractTriggered(true);
		configModel.setSolvableConflicts(Arrays.asList(solvableConflict));
		solvableConflict.setConflictingAssumptions(Arrays.asList(conflictingAssumption));
		conflictingAssumption.setCsticName(name);
		conflictingAssumption.setId("A");
		assertTrue("Retraction expected", classUnderTest.hasBeenRetracted(csticModel, configModel, session, configId));
	}

}
