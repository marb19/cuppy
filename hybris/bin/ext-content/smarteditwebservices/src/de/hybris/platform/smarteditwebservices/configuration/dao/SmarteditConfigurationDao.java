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
package de.hybris.platform.smarteditwebservices.configuration.dao;

import de.hybris.platform.smarteditwebservices.model.SmarteditConfigurationModel;

import java.util.List;


/**
 * Interface for SmartEdit Configuration DAO
 */
public interface SmarteditConfigurationDao
{
	/**
	 * Loads all configurations persisted
	 * @return a list of {@link SmarteditConfigurationModel}
	 */
	List<SmarteditConfigurationModel> loadAll();

	/**
	 * Finds a {@link SmarteditConfigurationModel} by its key value.
	 * @param key the configuration key
	 * @return a {@link SmarteditConfigurationModel} instance, or null if it does not exist in the database
	 */
	SmarteditConfigurationModel findByKey(String key);

}
