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
package com.sap.wec.adtreco.be;

import java.net.MalformedURLException;
import java.net.URL;

import de.hybris.platform.sap.core.configuration.global.SAPGlobalConfigurationService;
import de.hybris.platform.sap.core.configuration.http.HTTPDestination;
import de.hybris.platform.sap.core.configuration.http.impl.HTTPDestinationServiceImpl;
import de.hybris.platform.servicelayer.exceptions.ConfigurationException;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 */
public class HMCConfigurationReader
{
	private final static Logger LOG = Logger.getLogger(HMCConfigurationReader.class.getName());
	
	@Resource(name = "sapCoreDefaultSAPGlobalConfigurationService")
	private SAPGlobalConfigurationService globalConfigurationService;
	
	@Resource(name = "sapCoreHTTPDestinationService")
	private HTTPDestinationServiceImpl httpDestinationService;

	private String idOrigin = "";
	private String filterCategory = "";
	private String anonIdOrigin = "";
	private String httpDestinationId= "";

	/**
	 * Get Global Configuration Property
	 * 
	 */
	private String getGlobalConfigurationProperty(final String propertyName){
		String value = "";
		if (globalConfigurationService != null & globalConfigurationService.sapGlobalConfigurationExists())
		{
			if (globalConfigurationService.getProperty(propertyName) != null)
			{
				value = globalConfigurationService.getProperty(propertyName).toString();
			}
		}
		
		if (StringUtils.isNotEmpty(value))
		{
			return value.toString();
		} 
		else
		{
			throw new ConfigurationException(propertyName);
		}
	}
	
	/**
	 * Get backoffice global configuration service
	 * @return SAPGlobalConfigurationService globalConfigurationService
	 */
	public SAPGlobalConfigurationService getGlobalConfigurationService()
	{
		return globalConfigurationService;
	}

	/**
	 * @param globalConfigurationService
	 */
	public void setGlobalConfigurationService(final SAPGlobalConfigurationService globalConfigurationService)
	{
		this.globalConfigurationService = globalConfigurationService;
	}

	/**
	 * Get Contact ID Origin backoffice configuration
	 * @return String idOrigin
	 */
	public String getIdOrigin()
	{
		setIdOrigin(this.getGlobalConfigurationProperty("sapadtreco_idOrigin"));
		return idOrigin;
	}

	/**
	 * @param idOrigin
	 */
	public void setIdOrigin(final String idOrigin)
	{
		this.idOrigin = idOrigin;
	}
	
	/**
	 * @return String anonIdOrigin
	 */
	public String getAnonIdOrigin() {
		return anonIdOrigin;
	}

	/**
	 * @param anonIdOrigin
	 */
	public void setAnonIdOrigin(String anonIdOrigin) {
		this.anonIdOrigin = anonIdOrigin;
	}

	/**
	 * Get Campaign Filter Category from backoffice configuration
	 * @return String filterCategory
	 */
	public String getFilterCategory()
	{
		//filterCategory can be blank, therefore exception handling is not needed.
		filterCategory = (String) globalConfigurationService.getProperty("sapadtreco_filterCategory");
		if (filterCategory == null)
		{
			this.setFilterCategory("");
		}
		return filterCategory;
	}

	/**
	 * @param filterCategory
	 */
	public void setFilterCategory(final String filterCategory)
	{
		this.filterCategory = filterCategory;
	}
	
	/**
	 * Get HTTP Destination object containing the backoffice HTTP Destination configuration
	 * @return HTTPDestination httpDestination
	 */
	public HTTPDestination getHttpDestination()
	{
		//HTTPDestination is required, a ConfigurationException will be thrown if not maintained in the Backoffice
		if (httpDestinationService != null)
		{
			return httpDestinationService.getHTTPDestination(this.getGlobalConfigurationProperty("sapadtreco_httpdest"));
		}		
		return null;
	}
	
	/**
	 * @return httpDestination
	 */
	public String getHttpDestinationURL()
	{
		URL url;
		String path = "";
		try
		{
			url = new URL(this.getHttpDestination().getTargetURL());
			path = url.getProtocol() + "://" + url.getAuthority();

		}
		catch (MalformedURLException e)
		{
			LOG.error("Configured HTTP Destination contains a malformed target URL");
		}
		return path;
	}
	
	/**
	 * @return httpDestination
	 */
	public String getHttpDestinationSAPClient()
	{
		URL url;
		String client = "";
		try
		{
			url = new URL(getHttpDestination().getTargetURL());
			String query = url.getQuery();
			if (StringUtils.isNotEmpty(query))
			{
				client = query.substring(query.lastIndexOf("=") + 1);
			}
		}
		catch (MalformedURLException e)
		{
			LOG.warn("Configured URL is malformed, does not contain a client number");
		}
		return client;
	}

	/**
	 * @return the httpDestinationId
	 */
	public String getHttpDestinationId() {
		return httpDestinationId;
	}

	/**
	 * @param httpDestinationId the httpDestinationId to set
	 */
	public void setHttpDestinationId(String httpDestinationId) {
		this.httpDestinationId = httpDestinationId;
	}

}