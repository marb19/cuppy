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
 *
 */
package de.hybris.platform.chineseprofileservices.setup;

import de.hybris.platform.chineseprofileservices.constants.ChineseprofileservicesConstants;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;


@SystemSetup(extension = ChineseprofileservicesConstants.EXTENSIONNAME)
public class ChineseProfileServiceSystemSetup extends AbstractSystemSetup
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(ChineseProfileServiceSystemSetup.class);
	private static final String ELECTRONICS = "electronics";
	private static final String IMPORT_CORE_DATA = "import City Province Data";
	private static final String IMPORT_EMAIL_CONTENT = "import City Province Data";

	@Override
	@SystemSetupParameterMethod
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<SystemSetupParameter>();
		params.add(createBooleanSystemSetupParameter(IMPORT_CORE_DATA, "Import City Provinces Data", true));
		params.add(createBooleanSystemSetupParameter(IMPORT_EMAIL_CONTENT, "Import Email Template", true));
		return params;
	}

	@SystemSetup(type = Type.PROJECT, process = Process.ALL)
	public void createEssentialData(final SystemSetupContext context)
	{
		final List<ImportData> importData = new ArrayList<ImportData>();

		final ImportData electronicsImportData = new ImportData();
		electronicsImportData.setProductCatalogName(ELECTRONICS);
		electronicsImportData.setContentCatalogNames(Arrays.asList(ELECTRONICS));
		electronicsImportData.setStoreNames(Arrays.asList(ELECTRONICS));
		importData.add(electronicsImportData);
		importCommonData(context);
		updateEmailTemplate(context, importData);
	}

	protected void importCommonData(final SystemSetupContext context)
	{
		final String extensionName = context.getExtensionName();
		getSetupImpexService().importImpexFile(String.format("/%s/import/coredata/common/cities.impex", extensionName), false);
		getSetupImpexService().importImpexFile(String.format("/%s/import/coredata/common/districts.impex", extensionName), false);
	}

	public void updateEmailTemplate(final SystemSetupContext context, final List<ImportData> importData)
	{
		final String extensionName = context.getExtensionName();
		for (final ImportData data : importData)
		{
			for (final String contentCatalogName : data.getContentCatalogNames())
			{
				getSetupImpexService().importImpexFile(String.format(
						"/%s/import/contentCatalogs/%sContentCatalog/email-content.impex", extensionName, contentCatalogName), false);
			}
		}
	}

	protected List<String> getExtensionNames()
	{
		return Registry.getCurrentTenant().getTenantSpecificExtensionNames();
	}

	protected <T> T getBeanForName(final String name)
	{
		return (T) Registry.getApplicationContext().getBean(name);
	}
}
