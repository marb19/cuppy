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
package de.hybris.platform.acceleratorcms.jalo.restrictions;

import de.hybris.platform.jalo.SessionContext;

public class CMSActionRestriction extends GeneratedCMSActionRestriction
{
	@Override
	@Deprecated
	public String getDescription(final SessionContext sessionContext)
	{
		return "CMSCatalogRestriction";
	}
}