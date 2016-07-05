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
package de.hybris.platform.chineseprofile.controllers;

/**
 */
public interface ControllerConstants
{
	final String ADDON_PREFIX = "addon:/chineseprofileaddon/";

	/**
	 * Class with view name constants
	 */
	interface Views
	{
		interface Fragments
		{
			interface Account
			{
				String CountryAddressForm = ADDON_PREFIX + "fragments/address/countryAddressForm";
			}

		}
	}
}
