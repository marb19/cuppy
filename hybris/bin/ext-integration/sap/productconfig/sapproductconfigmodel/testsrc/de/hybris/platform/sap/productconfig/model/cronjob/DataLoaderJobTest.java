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
package de.hybris.platform.sap.productconfig.model.cronjob;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.sap.core.configuration.model.SAPConfigurationModel;
import de.hybris.platform.sap.core.configuration.model.SAPRFCDestinationModel;
import de.hybris.platform.sap.productconfig.model.dataloader.configuration.DataloaderSourceParameters;
import de.hybris.platform.sap.productconfig.model.impl.DataLoaderManagerContainerImpl;
import de.hybris.platform.sap.productconfig.model.model.DataLoaderCronJobModel;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.model.ItemModelContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.sap.custdev.projects.fbs.slc.dataloader.standalone.DataloaderConfiguration;


/**
 * Unit test for data loader job
 */
@SuppressWarnings("javadoc")
@UnitTest
public class DataLoaderJobTest
{
	DataLoaderJob classUnderTest = new DataLoaderJob();

	@Mock
	private DataLoaderCronJobModel dataLoaderCronJobModel;

	@Mock
	private CronJobService cronJobService;

	private final List<CronJobModel> cronJobListWrongType = new ArrayList<CronJobModel>();

	@Mock
	private CronJobModel cronJobDifferentType;

	private final List<CronJobModel> cronJobListSameType = new ArrayList<CronJobModel>();

	@Mock
	private DataLoaderCronJobModel cronJobSameType;

	private final String code = "A";
	private final String code2 = "B";

	@Mock
	private SAPConfigurationModel sapConfigurationModel;

	@Mock
	private SAPRFCDestinationModel sapDestinationModel;

	private static String destination = "destination";

	private UnitTestPropertyAccess propertyAccessFacade;

	private final String serverRfcDestination = "ZZZ000";

	@Mock
	private MediaModel mediaModel;

	@Mock
	private ItemModelContext itemModelContextKBFilterFile;

	private DataLoaderManagerContainerImpl container;



	/**
	 * Setup method
	 */
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		classUnderTest.setCronJobService(cronJobService);
		cronJobListWrongType.add(cronJobDifferentType);
		cronJobListSameType.add(cronJobSameType);
		Mockito.when(dataLoaderCronJobModel.getCode()).thenReturn(code);
		Mockito.when(dataLoaderCronJobModel.getSapConfiguration()).thenReturn(sapConfigurationModel);
		Mockito.when(cronJobSameType.getCode()).thenReturn(code2);
		Mockito.when(sapDestinationModel.getRfcDestinationName()).thenReturn(destination);
		propertyAccessFacade = new UnitTestPropertyAccess();
		classUnderTest.setPropertyAccessFacade(propertyAccessFacade);
		Mockito.when(mediaModel.getItemModelContext()).thenReturn(itemModelContextKBFilterFile);
		Mockito.when(itemModelContextKBFilterFile.isUpToDate()).thenReturn(Boolean.FALSE);
		Mockito.when(sapConfigurationModel.getSapproductconfig_filterKnowledgeBase()).thenReturn(mediaModel);
		container = new DataLoaderManagerContainerImpl();

		classUnderTest.setDataLoaderManagerContainer(container);
	}

	@Test
	public void testDataLoaderContainer()
	{

		assertEquals(container, classUnderTest.getDataLoaderManagerContainer());
	}

	@Test
	public void testProgressListener()
	{
		final DefaultDataloaderProgressListenerImpl progressListener = new DefaultDataloaderProgressListenerImpl();
		classUnderTest.setProgressListener(progressListener);
		assertEquals(progressListener, classUnderTest.getProgressListener());
	}

	@Test
	public void testMessageListener()
	{
		final DefaultDataloaderMessageListenerImpl messageListener = new DefaultDataloaderMessageListenerImpl();
		classUnderTest.setMessageListener(messageListener);
		assertEquals(messageListener, classUnderTest.getMessageListener());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPerformWoConfiguration()
	{
		classUnderTest.perform(dataLoaderCronJobModel);
	}


	@Test
	public void testAbortNoRunningJobs()
	{
		assertFalse("No need to abort (no jobs)", classUnderTest.isAbortNeeded(dataLoaderCronJobModel));
	}

	@Test
	public void testAbortDifferentType()
	{
		Mockito.when(cronJobService.getRunningOrRestartedCronJobs()).thenReturn(cronJobListWrongType);
		assertFalse("No need to abort (no jobs with same type)", classUnderTest.isAbortNeeded(dataLoaderCronJobModel));
	}

	@Test
	public void testAbortSameType()
	{
		Mockito.when(cronJobService.getRunningOrRestartedCronJobs()).thenReturn(cronJobListSameType);
		assertTrue("Need to abort", classUnderTest.isAbortNeeded(dataLoaderCronJobModel));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetDataLoaderSourceParamNoDestination()
	{
		classUnderTest.getDataloaderSourceParam(sapConfigurationModel);
	}

	@Test
	public void testGetDataLoaderSourceParam()
	{
		Mockito.when(sapConfigurationModel.getSapproductconfig_sapServer()).thenReturn(sapDestinationModel);
		final DataloaderSourceParameters dataloaderSourceParam = classUnderTest.getDataloaderSourceParam(sapConfigurationModel);
		assertEquals("We expect a destination name", destination, dataloaderSourceParam.getServerRfcDestination());
	}

	@Test
	public void testPropertyAccessFacade()
	{
		final PropertyAccessFacade propertyAccessFacade = new DefaultPropertyAccessFacade();
		classUnderTest.setPropertyAccessFacade(propertyAccessFacade);
		assertEquals(propertyAccessFacade, classUnderTest.getPropertyAccessFacade());
	}

	@Test
	public void testCreateConfigMapFromConfig()
	{
		final DataloaderSourceParameters params = new DataloaderSourceParameters();
		final Map<String, String> configMap = classUnderTest.createConfigMap(params);
		assertNotNull("We expect a map", configMap);
		final String targetFromProperties = configMap.get(DataloaderConfiguration.TARGET_FROM_PROPERTIES);
		assertEquals("TaargetFromProperties=true expected", Boolean.toString(true), targetFromProperties);
	}

	@Test
	public void testCreateConfigMapFromParams()
	{
		final DataloaderSourceParameters params = new DataloaderSourceParameters();
		params.setServerRfcDestination(serverRfcDestination);
		final Map<String, String> configMap = classUnderTest.createConfigMap(params);
		assertNotNull("We expect a map", configMap);
		final String dest = configMap.get(DataloaderConfiguration.OUTBOUND_DESTINATION_NAME);
		assertEquals("Destination expected", serverRfcDestination, dest);
	}

	@Test
	public void testPrepareFilterFiles()
	{
		final Map<String, String> dataloaderConfigMap = new HashMap<String, String>();
		classUnderTest.prepareFilterFiles(dataloaderConfigMap, sapConfigurationModel);
		final String kbFilterPath = dataloaderConfigMap.get(DataloaderConfiguration.KB_FILTER_FILE_PATH);
		assertNull("We expect no filter path since media model is not upToDate", kbFilterPath);
	}

	@Test
	public void testGetPathForMedia()
	{
		assertNull("No path since model is not upToDate", classUnderTest.getAbsolutFilePathForMedia(mediaModel));
	}

	@Test
	public void testIsResumePerformedInitialState()
	{
		assertFalse(classUnderTest.isResumePerformed());
	}

	@Test
	public void testIsResumePerformed()
	{
		container.setResumePerformed(true);
		assertTrue(classUnderTest.isResumePerformed());
	}





}
