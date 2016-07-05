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

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.sap.core.configuration.model.SAPConfigurationModel;
import de.hybris.platform.sap.core.configuration.model.SAPRFCDestinationModel;
import de.hybris.platform.sap.productconfig.model.dataloader.configuration.DataloaderSourceParameters;
import de.hybris.platform.sap.productconfig.model.enums.DataLoadTriggerMode;
import de.hybris.platform.sap.productconfig.model.enums.DataloadStatus;
import de.hybris.platform.sap.productconfig.model.intf.DataLoaderManagerContainer;
import de.hybris.platform.sap.productconfig.model.model.CPQDataloadStatusModel;
import de.hybris.platform.sap.productconfig.model.model.DataLoaderCronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sap.custdev.projects.fbs.slc.dataloader.settings.IDataloaderConfiguration;
import com.sap.custdev.projects.fbs.slc.dataloader.standalone.DataloaderConfiguration;
import com.sap.custdev.projects.fbs.slc.dataloader.standalone.manager.DataloaderFailureException;
import com.sap.custdev.projects.fbs.slc.dataloader.standalone.manager.DataloaderManager;
import com.sap.custdev.projects.fbs.slc.dataloader.standalone.manager.DataloaderManagerImpl;
import com.sap.custdev.projects.fbs.slc.dataloader.standalone.manager.DataloaderWorker;
import com.sap.custdev.projects.fbs.slc.dataloader.standalone.manager.DataloaderWorkerHybris;


/**
 * Performs initial and delta load
 */
public class DataLoaderJob extends AbstractJobPerformable<DataLoaderCronJobModel>
{
	private DataLoaderManagerContainer dataLoaderManagerContainer;

	private CronJobService cronJobService;

	private DefaultDataloaderProgressListenerImpl progressListener;

	private DefaultDataloaderMessageListenerImpl messageListener;

	private PropertyAccessFacade propertyAccessFacade;

	/**
	 * @return the propertyAccessFacade
	 */
	public PropertyAccessFacade getPropertyAccessFacade()
	{
		return propertyAccessFacade;
	}

	/**
	 * @return the messageListener
	 */
	public DefaultDataloaderMessageListenerImpl getMessageListener()
	{
		return messageListener;
	}

	/**
	 * @return the progressListener
	 */
	public DefaultDataloaderProgressListenerImpl getProgressListener()
	{
		return progressListener;
	}

	private static final Logger LOG = Logger.getLogger(DataLoaderJob.class);

	@Override
	public PerformResult perform(final DataLoaderCronJobModel dataLoaderCronJobModel)
	{

		DataLoadTriggerMode triggerMode = dataLoaderCronJobModel.getTriggerMode();


		if (LOG.isDebugEnabled())
		{
			final Date timestamp = new Date();
			LOG.debug("Perform DataLoaderJob " + dataLoaderCronJobModel.getCode() + " " + timestamp + " " + triggerMode.toString());
		}

		if (triggerMode == DataLoadTriggerMode.RESUME)
		{
			if (isResumePerformed())
			{
				LOG.debug("Resume was already performed");
				return new PerformResult(CronJobResult.DATALOAD_RESUME_ATTEMPT_DONE, CronJobStatus.FINISHED);
			}
			else
			{
				getDataLoaderManagerContainer().setResumePerformed(true);
			}
		}

		final SAPConfigurationModel sapConfiguration = dataLoaderCronJobModel.getSapConfiguration();
		final DataloaderManager dataloaderManager = initializeDataLoaderManager(dataLoaderCronJobModel, sapConfiguration);

		if (dataloaderManager == null)
		{
			return new PerformResult(CronJobResult.DATALOAD_ALREADY_RUNNING, CronJobStatus.FINISHED);
		}

		// Start initial load
		if (triggerMode == DataLoadTriggerMode.STARTINITIAL)
		{

			try
			{
				performInitialLoad(dataloaderManager, sapConfiguration);
			}
			catch (final DataloaderFailureException e)
			{
				final CPQDataloadStatusModel dataloadStatus = sapConfiguration.getSapproductconfig_cpqDataloadStatus();
				dataloadStatus.setCpqLastInitialLoadEndTime(null);
				dataloadStatus.setCpqDataloadStatus(DataloadStatus.ERROR);
				dataloadStatus.setCpqCurrentInitialLoadTransferredVolume(null);
				dataloadStatus.setCpqCurrentDeltaLoadTransferredVolume(null);
				dataloadStatus.setCpqNumberOfEntriesInDeltaLoadQueue(null);
				getModelService().save(dataloadStatus);
				//This leads to job status CronJobResult.ERROR, CronJobStatus.ABORTED;
				throw new IllegalStateException("Initial load failed", e);
			}

			if (dataloaderManager.isStoppedDownloadManually() || !getPropertyAccessFacade().getStartDeltaloadAfterInitial())
			{
				return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
			}

			dataLoaderCronJobModel.setTriggerMode(DataLoadTriggerMode.STARTDELTA);
			triggerMode = dataLoaderCronJobModel.getTriggerMode();

		}

		if (triggerMode == DataLoadTriggerMode.RESUME || triggerMode == DataLoadTriggerMode.STARTDELTA)
		{
			// Check whether initial load has been done. Only in this case start the delta load
			if (!isDeltaLoadStartAllowed(sapConfiguration))
			{
				LOG.warn("Initial load has not been done. No delta load is possible");
				return new PerformResult(CronJobResult.DATALOAD_NO_INITIAL_DOWNLOAD, CronJobStatus.FINISHED);
			}
			else
			{

				try
				{
					performDeltaLoad(dataloaderManager, sapConfiguration);
				}
				catch (final DataloaderFailureException e)
				{
					final CPQDataloadStatusModel dataloadStatus = sapConfiguration.getSapproductconfig_cpqDataloadStatus();
					dataloadStatus.setCpqDataloadStatus(DataloadStatus.ERROR);
					dataloadStatus.setCpqCurrentInitialLoadTransferredVolume(null);
					dataloadStatus.setCpqCurrentDeltaLoadTransferredVolume(null);
					dataloadStatus.setCpqNumberOfEntriesInDeltaLoadQueue(null);
					getModelService().save(dataloadStatus);
					//This leads to job status CronJobResult.ERROR, CronJobStatus.ABORTED;
					throw new IllegalStateException("Delta Load failed", e);
				}

				return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
			}
		}
		else
		{
			//This leads to job status CronJobResult.ERROR, CronJobStatus.ABORTED;
			throw new IllegalStateException("Non supported trigger mode");

		}


	}

	protected boolean isDeltaLoadStartAllowed(final SAPConfigurationModel sapConfiguration)
	{
		boolean startDeltaLoadAllowed = false;
		final CPQDataloadStatusModel cpqStatus = sapConfiguration.getSapproductconfig_cpqDataloadStatus();
		if (cpqStatus != null && cpqStatus.getCpqLastInitialLoadStartTime() != null
				&& cpqStatus.getCpqLastInitialLoadEndTime() != null)
		{
			final long startTime = cpqStatus.getCpqLastInitialLoadStartTime().getTime();
			final long endTime = cpqStatus.getCpqLastInitialLoadEndTime().getTime();
			if (startTime < endTime)
			{
				startDeltaLoadAllowed = true;
			}
		}
		return startDeltaLoadAllowed;
	}

	protected DataloaderManager initializeDataLoaderManager(final DataLoaderCronJobModel dataLoaderCronJobModel,
			final SAPConfigurationModel sapConfiguration)
	{
		if (sapConfiguration == null)
		{
			//This leads to job status CronJobResult.ERROR, CronJobStatus.ABORTED;
			throw new IllegalArgumentException("We require an instance of SAPConfiguration to persist our statistics");
		}

		//The following check is not synchronized, as we cannot synchronize the entire process including writing the job status to the hybris persistence.
		//Worst case scenario: Two parallel AbstractJobPerformables reach this point, detect that another job runs, and quit execution.
		//In this case the log files tell the actual root cause.
		final boolean needToAbort = isAbortNeeded(dataLoaderCronJobModel);
		if (needToAbort)
		{
			return null;
		}



		// Prepare Dataload
		final DataloaderManager dataloaderManager = prepareDataloadManager(dataLoaderCronJobModel);

		if (dataloaderManager == null)
		{
			//This leads to job status CronJobResult.ERROR, CronJobStatus.ABORTED;
			throw new IllegalStateException("Error while dataload preparation");
		}

		getDataLoaderManagerContainer().setDataLoaderManager(dataloaderManager);
		return dataloaderManager;
	}

	protected boolean isAbortNeeded(final DataLoaderCronJobModel dataLoaderCronJobModel)
	{
		boolean needToAbort = false;
		// Verify if another dataload job is already running
		final String currentJobCode = dataLoaderCronJobModel.getCode();

		final List<CronJobModel> cjList = getCronJobService().getRunningOrRestartedCronJobs();
		for (final CronJobModel cj : cjList)
		{
			if (cj instanceof DataLoaderCronJobModel && !cj.getCode().equalsIgnoreCase(currentJobCode))
			{
				LOG.info("Dataload job start aborted while another dataload job is running already");
				needToAbort = true;
			}
		}
		return needToAbort;
	}


	protected DataloaderManager prepareDataloadManager(final DataLoaderCronJobModel dataLoaderCronJobModel)
	{
		DataloaderManager dataloaderManager = null;

		final SAPConfigurationModel sapConfiguration = dataLoaderCronJobModel.getSapConfiguration();

		// Prepare data load parameters
		final DataloaderSourceParameters params = getDataloaderSourceParam(sapConfiguration);

		final Map<String, String> dataloaderConfigMap = createConfigMap(params);

		// Filter files
		prepareFilterFiles(dataloaderConfigMap, sapConfiguration);

		// Set up dataloader manager
		final DataloaderConfiguration dataloaderConfigurationWrapper = new DataloaderConfiguration(true, true, true, true, true,
				(HashMap<String, String>) dataloaderConfigMap);
		final IDataloaderConfiguration dataloaderConfiguration = dataloaderConfigurationWrapper.getConfiguration();
		final DataloaderWorker dataloaderWorker = new DataloaderWorkerHybris();
		dataloaderManager = new DataloaderManagerImpl(dataloaderConfiguration, dataloaderWorker);

		// Add dataloader event listeners
		final DefaultDataloaderProgressListenerImpl newProgressListener = getProgressListener();
		newProgressListener.setSapConfiguration(sapConfiguration);
		dataloaderManager.setOrReplaceProgressListener(newProgressListener);
		dataloaderManager.setOrReplaceMessageListener(getMessageListener());

		return dataloaderManager;
	}

	protected Map<String, String> createConfigMap(final DataloaderSourceParameters params)
	{
		final Map<String, String> dataloaderConfigMap = new HashMap<>();

		final String eccClient = params.getClient();
		final String outboundDestination = params.getServerRfcDestination();
		final String eccDestination = params.getClientRfcDestination();

		final String targetFromProperties = Boolean.toString(true);

		if (LOG.isDebugEnabled())
		{
			final StringBuilder debugOutput = new StringBuilder("\n Dataloader configuration attributes:");
			debugOutput.append("\n ECC client               : ").append(eccClient);
			debugOutput.append("\n RFC outbound dest        : ").append(outboundDestination);
			debugOutput.append("\n RFC ECC dest             : ").append(eccDestination);
			debugOutput.append("\n Target DB from properties: ").append(targetFromProperties);

			LOG.debug(debugOutput.toString());
		}
		dataloaderConfigMap.put(DataloaderConfiguration.ECC_CLIENT, eccClient);
		dataloaderConfigMap.put(DataloaderConfiguration.OUTBOUND_DESTINATION_NAME, outboundDestination);
		dataloaderConfigMap.put(DataloaderConfiguration.ECC_RFC_DESTINATION, eccDestination);

		// SSC DB
		dataloaderConfigMap.put(DataloaderConfiguration.TARGET_FROM_PROPERTIES, targetFromProperties);

		return dataloaderConfigMap;
	}

	protected void performInitialLoad(final DataloaderManager dataloaderManager, final SAPConfigurationModel sapConfiguration)
			throws DataloaderFailureException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Initial load start");
		}

		CPQDataloadStatusModel dataloadStatus = sapConfiguration.getSapproductconfig_cpqDataloadStatus();

		if (dataloadStatus == null)
		{
			dataloadStatus = new CPQDataloadStatusModel();
			dataloadStatus.setCpqDataloadStatusForSapConfiguration(sapConfiguration.getCore_name());
			dataloadStatus.setOwner(sapConfiguration);
			getModelService().save(dataloadStatus);
			sapConfiguration.setSapproductconfig_cpqDataloadStatus(dataloadStatus);
			getModelService().save(sapConfiguration);
		}

		dataloadStatus.setCpqLastInitialLoadStartTime(new Date());
		dataloadStatus.setCpqLastInitialLoadEndTime(null);
		dataloadStatus.setCpqDataloadStatus(DataloadStatus.INITIAL_LOAD);
		dataloadStatus.setCpqCurrentInitialLoadTransferredVolume(null);
		dataloadStatus.setCpqCurrentDeltaLoadTransferredVolume(null);
		dataloadStatus.setCpqNumberOfEntriesInDeltaLoadQueue(null);
		getModelService().save(dataloadStatus);

		dataloaderManager.createTables();
		dataloaderManager.startInitialDownload();

		if (dataloaderManager.isStoppedDownloadManually())
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Dataload is stopped during initial load");
			}

			dataloadStatus.setCpqDataloadStatus(DataloadStatus.INITIAL_LOAD_STOPPED);
			dataloadStatus.setCpqLastInitialLoadTransferredVolume(dataloadStatus.getCpqCurrentInitialLoadTransferredVolume());
			dataloadStatus.setCpqCurrentInitialLoadTransferredVolume(null);
			getModelService().save(dataloadStatus);
			return;
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Initial load end");
		}

		dataloadStatus.setCpqLastInitialLoadEndTime(new Date());
		dataloadStatus.setCpqDataloadStatus(DataloadStatus.INITIAL_LOAD_COMPLETED);
		dataloadStatus.setCpqLastInitialLoadTransferredVolume(dataloadStatus.getCpqCurrentInitialLoadTransferredVolume());
		dataloadStatus.setCpqCurrentInitialLoadTransferredVolume(null);
		getModelService().save(dataloadStatus);
	}

	protected void performDeltaLoad(final DataloaderManager dataloaderManager, final SAPConfigurationModel sapConfiguration)
			throws DataloaderFailureException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Delta load start");
		}

		final CPQDataloadStatusModel datloadStatus = sapConfiguration.getSapproductconfig_cpqDataloadStatus();

		datloadStatus.setCpqDataloadStatus(DataloadStatus.DELTA_LOAD);
		getModelService().save(datloadStatus);

		dataloaderManager.startDeltaDownload();

		if (dataloaderManager.isStoppedDownloadManually())
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Dataload is stopped during delta load");
			}
			datloadStatus.setCpqDataloadStatus(DataloadStatus.DELTA_LOAD_STOPPED);
			datloadStatus.setCpqNumberOfEntriesInDeltaLoadQueue(null);
			datloadStatus.setCpqCurrentDeltaLoadTransferredVolume(null);
			getModelService().save(datloadStatus);
			return;
		}




		if (LOG.isDebugEnabled())
		{
			LOG.debug("Delta load end");
		}
	}

	protected void prepareFilterFiles(final Map<String, String> dataloaderConfigMap, final SAPConfigurationModel sapConfiguration)
	{
		MediaModel filterFile = sapConfiguration.getSapproductconfig_filterKnowledgeBase();

		final String kbFilterFile = getAbsolutFilePathForMedia(filterFile);

		filterFile = sapConfiguration.getSapproductconfig_filterMaterial();

		final String materialsFilterFile = getAbsolutFilePathForMedia(filterFile);

		filterFile = sapConfiguration.getSapproductconfig_filterCondition();

		final String conditionsFilterFile = getAbsolutFilePathForMedia(filterFile);


		dataloaderConfigMap.put(DataloaderConfiguration.KB_FILTER_FILE_PATH, kbFilterFile);
		dataloaderConfigMap.put(DataloaderConfiguration.MATERIALS_FILTER_FILE_PATH, materialsFilterFile);
		dataloaderConfigMap.put(DataloaderConfiguration.CONDITIONS_FILTER_FILE_PATH, conditionsFilterFile);

		return;
	}

	protected String getAbsolutFilePathForMedia(final MediaModel filterFile)
	{

		String filterFileAbsolutPath = null;

		if (filterFile != null)
		{

			final boolean isAlive = !filterFile.getItemModelContext().isRemoved() && filterFile.getItemModelContext().isUpToDate();

			if (filterFile.getSize() != 0 && isAlive)
			{
				final File file = MediaManager.getInstance().getMediaAsFile(filterFile.getFolder().getQualifier(),
						filterFile.getLocation());

				filterFileAbsolutPath = file.getAbsolutePath();

			}
		}

		return filterFileAbsolutPath;
	}

	protected DataloaderSourceParameters getDataloaderSourceParam(final SAPConfigurationModel configuration)
	{

		final SAPRFCDestinationModel sapServer = configuration.getSapproductconfig_sapServer();
		final String rfcDestination = configuration.getSapproductconfig_sapRFCDestination();

		DataloaderSourceParameters params = null;

		if (sapServer == null)
		{
			throw new IllegalArgumentException("An RFC destination is needed to connect to the backend system");
		}

		params = new DataloaderSourceParameters();
		params.setClient(sapServer.getClient());
		params.setClientRfcDestination(rfcDestination);
		params.setServerRfcDestination(sapServer.getRfcDestinationName());
		return params;
	}

	/**
	 * @return the dataLoaderManagerContainer
	 */
	public DataLoaderManagerContainer getDataLoaderManagerContainer()
	{
		return dataLoaderManagerContainer;
	}

	/**
	 * @param dataLoaderManagerContainer
	 *           the dataLoaderManagerContainer to set
	 */
	public void setDataLoaderManagerContainer(final DataLoaderManagerContainer dataLoaderManagerContainer)
	{
		this.dataLoaderManagerContainer = dataLoaderManagerContainer;
	}

	/**
	 * @return the cronJobService
	 */
	public CronJobService getCronJobService()
	{
		return cronJobService;
	}

	/**
	 * @param cronJobService
	 */
	public void setCronJobService(final CronJobService cronJobService)
	{
		this.cronJobService = cronJobService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * Sets progress listener
	 *
	 * @param progressListener
	 */
	public void setProgressListener(final DefaultDataloaderProgressListenerImpl progressListener)
	{
		this.progressListener = progressListener;
	}

	/**
	 * Sets message listener
	 *
	 * @param messageListener
	 */
	public void setMessageListener(final DefaultDataloaderMessageListenerImpl messageListener)
	{
		this.messageListener = messageListener;
	}

	/**
	 * @param propertyAccessFacade
	 */
	public void setPropertyAccessFacade(final PropertyAccessFacade propertyAccessFacade)
	{
		this.propertyAccessFacade = propertyAccessFacade;
	}

	protected boolean isResumePerformed()
	{
		return getDataLoaderManagerContainer().isResumePerformed();
	}

}
