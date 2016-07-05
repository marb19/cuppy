/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package de.hybris.platform.classificationsystems.hmc;

import de.hybris.platform.classificationsystems.constants.ClassificationsystemsConstants;
import de.hybris.platform.hmc.webchips.Chip;
import de.hybris.platform.hmc.webchips.DisplayState;



public class ProfiClass40DETreeLeaf extends ClassificationImportTreeLeaf
{
	public ProfiClass40DETreeLeaf(final DisplayState displayState, final Chip parent)
	{
		super(displayState, parent, ClassificationsystemsConstants.PROFICLASS_4_0_DE, "startproficlass40DE");
	}
}