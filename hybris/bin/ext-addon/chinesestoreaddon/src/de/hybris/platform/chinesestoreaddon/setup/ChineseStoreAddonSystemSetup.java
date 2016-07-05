/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 */

package de.hybris.platform.chinesestoreaddon.setup;

import de.hybris.platform.chinesestoreaddon.constants.ChinesestoreaddonConstants;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * This class provides hooks into the system's initialization and update processes.
 *
 * @see "https://wiki.hybris.com/display/release4/Hooks+for+Initialization+and+Update+Process"
 */
@SystemSetup(extension = ChinesestoreaddonConstants.EXTENSIONNAME)
public class ChineseStoreAddonSystemSetup extends AbstractSystemSetup
{

	public static final String ELECTRONICS = "electronics";
	public static final String APPAREL = "apparel";
	public static final String POWERTOOLS = "powertools";

	private static final String IMPORT_CORE_DATA = "importCoreData";
	private static final String IMPORT_SAMPLE_DATA = "importSampleData";
	private static final String ACTIVATE_SOLR_CRON_JOBS = "activateSolrCronJobs";


	@Override
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<SystemSetupParameter>();

		params.add(createBooleanSystemSetupParameter(IMPORT_CORE_DATA, "Import Core Data", true));
		params.add(createBooleanSystemSetupParameter(IMPORT_SAMPLE_DATA, "Import Sample Data", true));
		params.add(createBooleanSystemSetupParameter(ACTIVATE_SOLR_CRON_JOBS, "Activate Solr Cron Jobs", true));

		return params;
	}

	/**
	 * This method will be called during the system initialization.
	 *
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.PROJECT, process = Process.UPDATE)
	public void createProjectData(final SystemSetupContext context)
	{
		final List<ImportData> importData = new ArrayList<ImportData>();
		final ImportData electronicsImportData = new ImportData();
		electronicsImportData.setProductCatalogName(ELECTRONICS);
		electronicsImportData.setContentCatalogNames(Arrays.asList(ELECTRONICS));
		electronicsImportData.setStoreNames(Arrays.asList(ELECTRONICS));
		importData.add(electronicsImportData);

		importCoreData(context, importData);
		importSampleData(context, importData);
	}

	/**
	 * this method will import that impex files in coredata folder.
	 *
	 * @param context
	 *           the context provides the selected parameters and values
	 * @param importData
	 */
	protected void importCoreData(final SystemSetupContext context, final List<ImportData> importData)
	{
		final String extensionName = context.getExtensionName();

		for (final ImportData data : importData)
		{
			importCoreDataProductCatalog(extensionName);
			synchronizeProductCatalog(context, data);
			for (final String storeName : data.getStoreNames())
			{
				logInfo(context, String.format("Begin importing store core data for [%s]", storeName));
				importCoreDataStore(context.getExtensionName());

				logInfo(context, String.format("Begin importing solr index core data for [%s]", storeName));
				importCoreDataSolrIndex(context.getExtensionName(), storeName);

				if (getBooleanSystemSetupParameter(context, ACTIVATE_SOLR_CRON_JOBS))
				{
					logInfo(context, String.format("Activating solr index for [%s]", storeName));
					runSolrIndex(storeName);
				}
			}
			importCoreDataCommon(extensionName);
		}
	}

	protected void importSampleData(final SystemSetupContext context, final List<ImportData> importData)
	{
		final String extensionName = context.getExtensionName();
		for (final ImportData data : importData)
		{
			importSampleDataProductCatalog(extensionName, data.getProductCatalogName());
			synchronizeProductCatalog(context, data);
			for (final String storeName : data.getStoreNames())
			{
				logInfo(context, String.format("Begin importing store sample data for [%s]", storeName));
				importSampleDataStore(context.getExtensionName(), storeName);

				logInfo(context, String.format("Begin importing solr index sample data for [%s]", storeName));
				importSampleDataSolrIndex(context.getExtensionName(), storeName);

				if (getBooleanSystemSetupParameter(context, ACTIVATE_SOLR_CRON_JOBS))
				{
					logInfo(context, String.format("Activating solr index for [%s]", storeName));
					runSolrIndex(storeName);
				}
			}
		}
	}

	private void importCoreDataCommon(final String extensionName)
	{
		getSetupImpexService().importImpexFile(String.format("/%s/import/common/common-addon-extra.impex", extensionName), true);
	}

	private void importCoreDataProductCatalog(final String extensionName)
	{
		getSetupImpexService().importImpexFile(String.format("/%s/import/productCatalogs/template/catalog.impex", extensionName),
				false);
	}

	private void importCoreDataStore(final String extensionName)
	{
		getSetupImpexService().importImpexFile(String.format("/%s/import/stores/template/store.impex", extensionName), false);
		getSetupImpexService().importImpexFile(String.format("/%s/import/stores/template/site.impex", extensionName), false);
	}

	private void importCoreDataSolrIndex(final String extensionName, final String storeName)
	{
		getSetupImpexService().importImpexFile(String.format("/%s/import/solr/template/solr.impex", extensionName), false);

		getSetupSolrIndexerService().createSolrIndexerCronJobs(String.format("%sIndex", storeName));
	}

	private void importSampleDataProductCatalog(final String extensionName, final String productCatalogName)
	{
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/productCatalogs/%sProductCatalog/products.impex", extensionName, productCatalogName),
				false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/productCatalogs/%sProductCatalog/products-prices.impex", extensionName, productCatalogName),
				false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/productCatalogs/%sProductCatalog/products-media.impex", extensionName, productCatalogName),
				false);
		getSetupImpexService().importImpexFile(String.format(
				"/%s/import/productCatalogs/%sProductCatalog/products-stocklevels.impex", extensionName, productCatalogName), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/productCatalogs/%sProductCatalog/products-pos-stocklevels.impex", extensionName), false);
	}

	private void importSampleDataStore(final String extensionName, final String storeName)
	{
		getSetupImpexService().importImpexFile(String.format("/%s/import/stores/%s/promotions.impex", extensionName, storeName),
				false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/stores/%s/points-of-service-media.impex", extensionName, storeName), false);
		getSetupImpexService()
				.importImpexFile(String.format("/%s/import/stores/%s/points-of-service.impex", extensionName, storeName), false);
	}

	private void importSampleDataSolrIndex(final String extensionName, final String storeName)
	{
		getSetupImpexService().importImpexFile(String.format("/%s/import/stores/%s/solr.impex", extensionName, storeName), false);

		getSetupSolrIndexerService().createSolrIndexerCronJobs(String.format("%sIndex", storeName));
	}

	private void synchronizeProductCatalog(final SystemSetupContext context, final ImportData importData)
	{

		final String catalogName = importData.getProductCatalogName();
		logInfo(context, String.format("Begin synchronizing Product Catalog [%s]", catalogName));

		getSetupSyncJobService().createProductCatalogSyncJob(String.format("%sProductCatalog", catalogName));
		final PerformResult syncCronJobResult = getSetupSyncJobService()
				.executeCatalogSyncJob(String.format("%sProductCatalog", catalogName));
		if (isSyncRerunNeeded(syncCronJobResult))
		{
			logInfo(context, String.format("Product Catalog [%s] sync has issues.", catalogName));
		}
	}

	private void runSolrIndex(final String storeName)
	{
		getSetupSolrIndexerService().executeSolrIndexerCronJob(String.format("%sIndex", storeName), true);
		getSetupSolrIndexerService().activateSolrIndexerCronJobs(String.format("%sIndex", storeName));
	}

}
