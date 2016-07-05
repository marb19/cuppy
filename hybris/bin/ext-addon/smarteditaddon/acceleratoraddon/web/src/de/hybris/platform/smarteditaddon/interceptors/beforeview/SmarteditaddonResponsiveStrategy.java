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
package de.hybris.platform.smarteditaddon.interceptors.beforeview;

/**
 * Strategy to determine if the storefront is responsive or non responsive.
 */
public interface SmarteditaddonResponsiveStrategy
{
	/**
	 * Indicate if the storefront is responsive (true) or not (false).
	 *
	 * @return Boolean representing the responsiveness of the storefront
	 */
	Boolean isResponsive();
}
