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
package de.hybris.platform.sap.productconfig.frontend.attributehandlers;

import de.hybris.platform.sap.productconfig.frontend.model.CMSConfigurableProductRestrictionModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.util.localization.Localization;


public class ConfigurableProductRestrictionDynamicDescription
		implements DynamicAttributeHandler<String, CMSConfigurableProductRestrictionModel>
{


	@Override
	public String get(final CMSConfigurableProductRestrictionModel model)
	{
		final String localizedString = Localization.getLocalizedString("type.CMSConfigurableRestriction.description.text");
		String result;
		if (localizedString == null)
		{
			result = "Page/Component only applies for configurable products";
		}
		else
		{
			result = localizedString;
		}
		return result.toString();
	}


	@Override
	public void set(final CMSConfigurableProductRestrictionModel model, final String value)
	{
		throw new UnsupportedOperationException();
	}

}
