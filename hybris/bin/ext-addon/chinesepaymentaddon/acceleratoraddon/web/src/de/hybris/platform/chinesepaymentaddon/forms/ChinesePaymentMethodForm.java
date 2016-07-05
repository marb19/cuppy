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
*/
package de.hybris.platform.chinesepaymentaddon.forms;

public class ChinesePaymentMethodForm
{
	private String code;

	private String name;

	private String logoUrl;

	/**
	 * @return the code
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * @param code
	 *           the code to set
	 */
	public void setCode(final String code)
	{
		this.code = code;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *           the name to set
	 */
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * @return the logoUrl
	 */
	public String getLogoUrl()
	{
		return logoUrl;
	}

	/**
	 * @param logoUrl
	 *           the logoUrl to set
	 */
	public void setLogoUrl(final String logoUrl)
	{
		this.logoUrl = logoUrl;
	}


}
