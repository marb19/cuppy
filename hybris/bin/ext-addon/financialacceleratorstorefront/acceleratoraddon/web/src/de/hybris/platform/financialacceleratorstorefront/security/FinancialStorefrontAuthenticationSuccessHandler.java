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
package de.hybris.platform.financialacceleratorstorefront.security;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.acceleratorstorefrontcommons.security.StorefrontAuthenticationSuccessHandler;
import de.hybris.platform.commercefacades.order.data.CommerceSaveCartResultData;
import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.financialfacades.facades.InsuranceCartFacade;

/**
 * The class of StorefrontAuthenticationSuccessHandler.
 * Override the restoreSavedCartAndMerge to make sure always save session cart after successful login.
 */
public class FinancialStorefrontAuthenticationSuccessHandler extends StorefrontAuthenticationSuccessHandler
{
    private Logger LOG = Logger.getLogger(FinancialStorefrontAuthenticationSuccessHandler.class);
    
    private InsuranceCartFacade insuranceCartFacade;

    @Override
    protected void restoreSavedCartAndMerge(HttpServletRequest request) {
        super.restoreSavedCartAndMerge(request);
        saveSessionCart();
    }

    protected CommerceSaveCartResultData saveSessionCart()
	{
		if (getCartFacade().hasEntries())
		{
			try
			{
				return getInsuranceCartFacade().saveCurrentUserCart();
			}
			catch (final CommerceSaveCartException e)
			{
				LOG.error("Unable to save cart : " + e.getMessage(), e);
			}
		}

		return null;
	}

    protected InsuranceCartFacade getInsuranceCartFacade()
    {
        return insuranceCartFacade;
    }

    @Required
    public void setInsuranceCartFacade(final InsuranceCartFacade insuranceCartFacade)
    {
        this.insuranceCartFacade = insuranceCartFacade;
    }
}
