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
package de.hybris.platform.assistedservicestorefront.redirect.impl;

import de.hybris.platform.assistedservicefacades.constants.AssistedservicefacadesConstants;
import de.hybris.platform.assistedservicestorefront.constants.AssistedservicestorefrontConstants;
import de.hybris.platform.assistedservicestorefront.redirect.AssistedServiceRedirectStrategy;
import de.hybris.platform.commerceservices.util.ResponsiveUtils;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default property based implementation for {@link AssistedServiceRedirectStrategy}
 */
public class DefaultAssistedServiceRedirectStrategy implements AssistedServiceRedirectStrategy
{
	private CartService cartService;
	private UserService userService;
	private SessionService sessionService;

	private final static String DEFAULT_ERROR_REDIRECT = "/";
	private final static String DEFAULT_CART_REDIRECT = "/cart";
	private final static String DEFAULT_CUSTOMER_REDIRECT = "/my-account";
	private final static String DEFAULT_ORDER_REDIRECT = "/my-account/order/%s";

	@Override
	public String getRedirectPath()
	{
		final String redirectOrderId = sessionService.getCurrentSession().getAttribute(
				AssistedservicefacadesConstants.ASM_ORDER_ID_TO_REDIRECT);
		if (redirectOrderId == null)
		{
			if (getCartService().getSessionCart() != null
					&& CollectionUtils.isNotEmpty(getCartService().getSessionCart().getEntries()))
			{
				return getPathWithCart();
			}
			else if (!ResponsiveUtils.isResponsive() && !getUserService().isAnonymousUser(getUserService().getCurrentUser()))
			{
				return getPathCustomerOnly();
			}
			return "/";
		}
		else
		{
			sessionService.getCurrentSession().removeAttribute(AssistedservicefacadesConstants.ASM_ORDER_ID_TO_REDIRECT);
			return getPathWithOrder(redirectOrderId);
		}
	}

	@Override
	public String getErrorRedirectPath()
	{
		return Config.getString(AssistedservicestorefrontConstants.REDIRECT_ERROR, DEFAULT_ERROR_REDIRECT);
	}

	protected String getPathWithCart()
	{
		return Config.getString(AssistedservicestorefrontConstants.REDIRECT_WITH_CART, DEFAULT_CART_REDIRECT);
	}

	protected String getPathCustomerOnly()
	{
		return Config.getString(AssistedservicestorefrontConstants.REDIRECT_CUSTOMER_ONLY, DEFAULT_CUSTOMER_REDIRECT);
	}

	protected String getPathWithOrder(final String redirectOrderId)
	{
		return String.format(Config.getString(AssistedservicestorefrontConstants.REDIRECT_WITH_ORDER, DEFAULT_ORDER_REDIRECT),
				redirectOrderId);
	}

	/**
	 * @return the cartService
	 */
	protected CartService getCartService()
	{
		return cartService;
	}

	/**
	 * @param cartService
	 *           the cartService to set
	 */
	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	/**
	 * @return the userService
	 */
	protected UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @return the sessionService
	 */
	protected SessionService getSessionService()
	{
		return sessionService;
	}

	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}
}