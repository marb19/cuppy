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
package de.hybris.platform.promotionengineservices.promotionengine.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.promotionengineservices.promotionengine.PromotionEngineService;
import de.hybris.platform.servicelayer.ServicelayerTest;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyOverrideConfigurer;


/**
 * Base class for promotionengineservices tests
 */
public class PromotionEngineServiceBaseTest extends ServicelayerTest
{

	@Autowired
	private PromotionEngineService promotionEngineService;


	/**
	 * imports the given impex file and overwrites beans based on the given properties file using a
	 * {@link PropertyOverrideConfigurer}
	 */
	protected void setupImpexAndOverrideEngineContext(final String impexFileName)
	{
		try
		{
			importCsv(impexFileName, "utf-8");
		}
		catch (final ImpExException e)
		{
			throw new IllegalStateException("exception during test setup:", e);
		}
		configurePromotionEngineService();
	}

	public void configurePromotionEngineService()
	{
		final DefaultPromotionEngineService defaultPromotionEngineService = (DefaultPromotionEngineService) Registry
				.getApplicationContext().getBean("promotionEngineService");
		defaultPromotionEngineService.setDefaultRuleEngineContextName("promotions-junit-context");
	}

	protected PromotionEngineService getPromotionEngineService()
	{
		return promotionEngineService;
	}

	protected String readRuleFile(final String fileName, final String path) throws IOException
	{
		final Path rulePath = Paths.get(Registry.getApplicationContext().getResource("classpath:" + path + fileName).getURI());
		final InputStream is = Files.newInputStream(rulePath);
		final StringWriter writer = new StringWriter();
		IOUtils.copy(is, writer, Charset.forName("UTF-8"));
		return writer.toString();
	}

}
