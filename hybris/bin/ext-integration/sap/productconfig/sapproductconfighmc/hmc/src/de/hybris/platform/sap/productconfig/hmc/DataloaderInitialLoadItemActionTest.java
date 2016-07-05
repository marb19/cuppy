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
package de.hybris.platform.sap.productconfig.hmc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.core.configuration.jalo.SAPConfiguration;
import de.hybris.platform.sap.core.configuration.jalo.SAPRFCDestination;
import de.hybris.platform.sap.productconfig.hmc.constants.SapproductconfighmcConstants;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sap.custdev.projects.fbs.slc.dataloader.settings.IDataloaderSource;


/**
 *
 */
@UnitTest
public class DataloaderInitialLoadItemActionTest
{
	private DataloaderInitialLoadItemAction classUnderTest;
	@Mock
	private SAPConfiguration mockedConfiguration;
	@Mock
	private SAPRFCDestination mockedDestination;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		classUnderTest = new DataloaderInitialLoadItemAction();
		when(mockedConfiguration.getProperty(SapproductconfighmcConstants.CONFIGURATION_DATALOADER_SAP_SERVER)).thenReturn(
				mockedDestination);
		when(mockedConfiguration.getProperty(SapproductconfighmcConstants.CONFIGURATION_DATALOADER_SAP_RFC_DEST)).thenReturn(
				"testSapDestination");
	}




	protected void mockSapRfcDest(final boolean loadBalacing)
	{
		when(Boolean.valueOf(mockedDestination.isConnectionTypeAsPrimitive())).thenReturn(Boolean.valueOf(!loadBalacing));
		if (loadBalacing)
		{
			when(mockedDestination.getSid()).thenReturn("SID");
			when(mockedDestination.getMessageServer()).thenReturn("testMessageServer");
			when(mockedDestination.getGroup()).thenReturn("PUBLIC");
		}
		else
		{
			when(mockedDestination.getInstance()).thenReturn("10");
			when(mockedDestination.getTargetHost()).thenReturn("testTargetHost");

		}
		when(mockedDestination.getUserid()).thenReturn("testUser");
		when(mockedDestination.getPassword()).thenReturn("testPwd");
		when(mockedDestination.getClient()).thenReturn("001");

		when(mockedDestination.getRfcDestinationName()).thenReturn("testDestinationName");

	}

	@Test
	public void createDataloaderSource_SSC23_noLoadBalance()
	{
		mockSapRfcDest(false);
		final IDataloaderSource dlSource = classUnderTest.getSAPSource(mockedConfiguration);
		assertNotNull(dlSource);
		checkDirectDestination(dlSource);
	}



	@Test
	public void createDataloaderSource_SSC23_loadBalance()
	{
		mockSapRfcDest(true);
		final IDataloaderSource dlSource = classUnderTest.getSAPSource(mockedConfiguration);
		assertNotNull(dlSource);
		checkLoadBalancedDestination(dlSource);
	}


	@Test
	public void createDataloaderSource_SSC24_loadBalance() throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException
	{
		mockSapRfcDest(true);
		final IDataloaderSource dlSource = classUnderTest.getSAPSource(mockedConfiguration);
		assertNotNull(dlSource);
		checkLoadBalancedDestination(dlSource);
		checkSSC24changes(dlSource);
	}

	@Test
	public void createDataloaderSource_SSC24_noLoadBalance() throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException
	{
		mockSapRfcDest(false);
		final IDataloaderSource dlSource = classUnderTest.getSAPSource(mockedConfiguration);
		assertNotNull(dlSource);
		checkDirectDestination(dlSource);
		checkSSC24changes(dlSource);
	}



	private void checkLoadBalancedDestination(final IDataloaderSource dlSource)
	{
		assertEquals("testSapDestination", dlSource.getRfcDestination());
		assertEquals("001", dlSource.getClientSetting().getClient());
		assertEquals("testUser", dlSource.getClientSetting().getUser());
		assertEquals("testPwd", dlSource.getClientSetting().getPassword());
		assertEquals("SID", dlSource.getEccSetting().getSid());
		assertEquals("testMessageServer", dlSource.getEccSetting().getMessageServer());
		assertEquals("PUBLIC", dlSource.getEccSetting().getGroup());
	}


	private void checkDirectDestination(final IDataloaderSource dlSource)
	{
		assertEquals("testSapDestination", dlSource.getRfcDestination());
		assertEquals("001", dlSource.getClientSetting().getClient());
		assertEquals("testUser", dlSource.getClientSetting().getUser());
		assertEquals("testPwd", dlSource.getClientSetting().getPassword());
		assertEquals("10", dlSource.getEccSetting().getSid());
		assertEquals("testTargetHost", dlSource.getEccSetting().getMessageServer());
	}


	private void checkSSC24changes(final IDataloaderSource dlSource) throws IllegalAccessException, InvocationTargetException
	{
		boolean dlSourceExtIntfExists;
		try
		{
			Class.forName("com.sap.custdev.projects.fbs.slc.dataloader.settings.IDataloaderSourceExtension");
			dlSourceExtIntfExists = true;
		}
		catch (final ClassNotFoundException e)
		{
			dlSourceExtIntfExists = false;
		}
		assumeTrue("Interface IDataloaderSourceExtension not in classpath, using SSC version 2.3 or below?", dlSourceExtIntfExists);
		final List<Class<?>> interfaces = Arrays.asList(dlSource.getClass().getInterfaces());
		boolean dlSourceExtIntfFound = false;
		for (final Class<?> intf : interfaces)
		{
			if (intf.getName().equals("com.sap.custdev.projects.fbs.slc.dataloader.settings.IDataloaderSourceExtension"))
			{
				dlSourceExtIntfFound = true;
				break;
			}
		}
		assertTrue("class DataloaderSource doesn't implement interface IDataloaderSourceExtension! ", dlSourceExtIntfFound);
		final List<Method> methods = Arrays.asList(dlSource.getClass().getMethods());
		boolean dlSourceExtMethodFound = false;
		for (final Method method : methods)
		{
			if ("getOutboundDestinationName".equals(method.getName()))
			{
				final Object destNameObj = method.invoke(dlSource);
				assertEquals("testDestinationName", destNameObj.toString());
				dlSourceExtMethodFound = true;
				break;
			}

		}
		assertTrue("class DataloaderSource doesn't implement method getOutboundDestinationName! ", dlSourceExtMethodFound);
	}
}
