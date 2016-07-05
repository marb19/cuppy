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
package de.hybris.platform.smarteditwebservices.configuration.facade;

import de.hybris.platform.smarteditwebservices.data.ConfigurationData;

import java.util.List;

/**
 * Interface methods for SmarteditConfigurationFacade.
 * The implementing class will provide methods for validating, populating and persisting configuration models.
 */
public interface SmarteditConfigurationFacade
{
	/**
	 * Finds all ConfigurationData stores in the data store.
	 * @return a list of {@link ConfigurationData}
	 */
	List<ConfigurationData> findAll();

	/**
	 * Create a new configuration model
	 * @param configurationData the data bean containing the values to be saved
	 * @return the {@link ConfigurationData} created
	 */
	ConfigurationData create(ConfigurationData configurationData);

	/**
	 * Updates the configuration model represented by the uid
	 * @param uid is the unique identifier of this configuration
	 * @param configurationData - the data bean to be updated
	 * @return the configuration bean updated
	 */
	ConfigurationData update(String uid, ConfigurationData configurationData);

	/**
	 * Finds a configuration data bean by its unique identifier
	 * @param uid the configuration's unique identifier
	 * @return the {@link ConfigurationData} represented by this uid
	 */
	ConfigurationData findByUid(String uid);

	/**
	 * Deletes the configuration model represented by this unique identifier
	 * @param uid the model's unique identifier
	 */
	void delete(String uid);
}
