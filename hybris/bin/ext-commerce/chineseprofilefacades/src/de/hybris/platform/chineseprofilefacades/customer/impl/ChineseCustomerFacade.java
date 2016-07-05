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
package de.hybris.platform.chineseprofilefacades.customer.impl;

import de.hybris.platform.chineseprofilefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.customer.impl.DefaultCustomerFacade;
import de.hybris.platform.core.model.user.CustomerModel;


public class ChineseCustomerFacade extends DefaultCustomerFacade implements CustomerFacade
{
	@Override
	public void saveEmailLanguageForCurrentUser(final String languageISO)
	{
		final CustomerModel currentUser = (CustomerModel) getUserService().getCurrentUser();
		if (currentUser != null && !getUserService().isAnonymousUser(currentUser))
		{
			currentUser.setEmailLanguage(languageISO);
		}
	}
}
