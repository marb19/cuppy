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
package de.hybris.platform.sap.productconfig.frontend.validator;

import de.hybris.platform.sap.productconfig.facades.ConfigurationData;

import org.springframework.validation.BindingResult;


public interface ConflictChecker
{
	public void checkConflicts(ConfigurationData config, BindingResult bindingResult);

	public void checkMandatoryFields(ConfigurationData config, BindingResult bindingResult);

	/**
	 * Checks whether a UI-Group is considered complete, and if so sets the group status accordingly.
	 *
	 * @param config
	 *           configuration to check
	 */
	public void checkCompletness(ConfigurationData config);
}