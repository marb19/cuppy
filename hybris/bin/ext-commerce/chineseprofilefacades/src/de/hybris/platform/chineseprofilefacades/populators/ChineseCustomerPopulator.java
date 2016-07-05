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
package de.hybris.platform.chineseprofilefacades.populators;

import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.apache.commons.lang3.StringUtils;


public class ChineseCustomerPopulator implements Populator<CustomerModel, CustomerData>
{
	@Override
	public void populate(final CustomerModel source, final CustomerData target) throws ConversionException
	{
		final String emailLanguage = source.getEmailLanguage();
		if (StringUtils.isNotEmpty(emailLanguage))
		{
			target.setEmailLanguage(emailLanguage);
		}
	}
}
