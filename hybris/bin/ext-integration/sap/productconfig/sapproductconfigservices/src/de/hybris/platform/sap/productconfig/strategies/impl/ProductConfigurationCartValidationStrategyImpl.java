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
package de.hybris.platform.sap.productconfig.strategies.impl;

import de.hybris.platform.b2bacceleratorservices.strategies.impl.DefaultB2BCartValidationStrategy;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.sap.productconfig.runtime.interf.KBKey;
import de.hybris.platform.sap.productconfig.runtime.interf.impl.KBKeyImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.services.SessionAccessService;
import de.hybris.platform.sap.productconfig.services.intf.ProductConfigurationService;

import org.apache.log4j.Logger;


/**
 * Implements CPQ specific cart validations
 */
public class ProductConfigurationCartValidationStrategyImpl extends DefaultB2BCartValidationStrategy
{
	/**
	 * Indicates that customer needs to revisit product configuration. Postfix in corresponding resource text
	 */
	public static final String REVIEW_CONFIGURATION = "reviewConfiguration";
	private SessionAccessService sessionAccessService;
	private ProductConfigurationService productConfigurationService;
	private static final Logger LOG = Logger.getLogger(ProductConfigurationCartValidationStrategyImpl.class);

	/**
	 * @param productConfigurationService
	 *           the productConfigurationService to set
	 */
	public void setProductConfigurationService(final ProductConfigurationService productConfigurationService)
	{
		this.productConfigurationService = productConfigurationService;
	}

	/**
	 * @param sessionAccessService
	 *           the sessionAccessService to set
	 */
	public void setSessionAccessService(final SessionAccessService sessionAccessService)
	{
		this.sessionAccessService = sessionAccessService;
	}

	@Override
	protected CommerceCartModification validateCartEntry(final CartModel cartModel, final CartEntryModel cartEntryModel)
	{

		CommerceCartModification modification = super.validateCartEntry(cartModel, cartEntryModel);
		if (modification.getStatusCode().equals(CommerceCartModificationStatus.SUCCESS))
		{
			final CommerceCartModification configurationModification = validateConfiguration(cartEntryModel);
			if (configurationModification != null)
			{
				modification = configurationModification;
			}

		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Validate cart entry for product '" + modification.getProduct().getCode() + "' with status '"
					+ modification.getStatusCode() + "'");
		}

		return modification;
	}

	/**
	 * Validates a cart entry model with regards to product configuration
	 *
	 * @param cartEntryModel
	 *           Model representation of cart entry
	 * @return Null if no issue occurred. A modification in status
	 *         {@link ProductConfigurationCartValidationStrategyImpl#REVIEW_CONFIGURATION} in case a validation error
	 *         occurred.
	 */
	protected CommerceCartModification validateConfiguration(final CartEntryModel cartEntryModel)
	{
		//No issues so far: We check for consistency and completeness of configuration
		CommerceCartModification configurationModification = null;
		final String externalConfiguration = cartEntryModel.getExternalConfiguration();
		if (externalConfiguration != null && (!externalConfiguration.isEmpty()))
		{
			ConfigModel configurationModel = null;
			final String configIdForCartEntry = sessionAccessService.getConfigIdForCartEntry(cartEntryModel.getPk().toString());
			//Is there a configuration attached?
			if (configIdForCartEntry != null)
			{
				//Do we have a runtime representation yet?
				configurationModel = getConfigFromSession(configIdForCartEntry);
			}
			else
			{
				//We need to retrieve from external configuration
				configurationModel = getConfigFromExtCFG(cartEntryModel, externalConfiguration);
			}
			if (!configurationModel.isComplete() || !configurationModel.isConsistent())
			{
				configurationModification = new CommerceCartModification();
				configurationModification.setStatusCode(REVIEW_CONFIGURATION);
				configurationModification.setEntry(cartEntryModel);
				configurationModification.setProduct(cartEntryModel.getProduct());
			}
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Validate configuration for product '" + configurationModification.getProduct().getCode() + "' with status '"
					+ configurationModification.getStatusCode() + "'");
		}

		return configurationModification;
	}

	/**
	 * Retrieve configuration from session
	 *
	 * @param configIdForCartEntry
	 *           ID of configuration in session
	 * @return Configuration Model, representing the configuration attached to the current cart entry
	 */
	protected ConfigModel getConfigFromSession(final String configIdForCartEntry)
	{
		ConfigModel configurationModel;
		configurationModel = productConfigurationService.retrieveConfigurationModel(configIdForCartEntry);
		return configurationModel;
	}

	/**
	 * Creates a configuration from external representation and return it. Also stores SSC session ID in hybris session
	 * to be able to call the configuration UI later on.
	 *
	 * @param cartEntryModel
	 *           Cart entry
	 * @param externalConfiguration
	 *           XML representation of configuration
	 * @return Configuration Model, representing the configuration attached to the current cart entry
	 */
	protected ConfigModel getConfigFromExtCFG(final CartEntryModel cartEntryModel, final String externalConfiguration)
	{
		ConfigModel configurationModel;
		final KBKey kbKey = new KBKeyImpl(cartEntryModel.getProduct().getCode());
		configurationModel = productConfigurationService.createConfigurationFromExternal(kbKey, externalConfiguration);
		sessionAccessService.setConfigIdForCartEntry(cartEntryModel.getPk().toString(), configurationModel.getId());
		return configurationModel;
	}

	/**
	 * @return Session access service
	 */
	public SessionAccessService getSessionAccessService()
	{
		return this.sessionAccessService;
	}

	/**
	 * @return Product configuration service
	 */
	public ProductConfigurationService getProductConfigurationService()
	{
		return this.productConfigurationService;
	}


}
