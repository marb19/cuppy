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

import de.hybris.platform.chineseprofilefacades.data.DistrictData;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.C2LItemModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Locale;

import javax.annotation.Resource;

import org.springframework.util.Assert;


public class DistrictPopulator implements Populator<C2LItemModel, DistrictData>
{
	@Resource(name = "storeSessionFacade")
	private StoreSessionFacade storeSessionFacade;

	@Override
	public void populate(final C2LItemModel source, final DistrictData target) throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setCode(source.getIsocode());
		final LanguageData currentLanguage = storeSessionFacade.getCurrentLanguage();
		final String currentISOCode = currentLanguage.getIsocode();
		final Locale currentLocale = new Locale(currentISOCode);
		target.setName(source.getName(currentLocale));
	}

}
