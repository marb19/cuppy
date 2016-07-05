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
 */

package de.hybris.platform.yaasconfiguration.service;


import de.hybris.platform.yaasconfiguration.model.YaasApplicationModel;

import java.util.Optional;


public interface YaasConfigurationService
{

	YaasApplicationModel getYaasApplicationForId(final String applicationId);

	/**
	 * if exists it return the first record in YaasApplication table, otherwise it returns empty
	 *
	 * @return
	 */
	Optional<YaasApplicationModel> takeFirstModel();
}
