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

import de.hybris.platform.chineseprofilefacades.data.CityData;
import de.hybris.platform.chineseprofilefacades.data.DistrictData;
import de.hybris.platform.chineseprofilefacades.strategies.NameWithTitleFormatStrategy;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


public class ChineseAddressPopulator implements Populator<AddressModel, AddressData>
{
	private UserService userService;

	private StoreSessionFacade storeSessionFacade;

	private NameWithTitleFormatStrategy nameWithTitleFormatStrategy;

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	protected StoreSessionFacade getStoreSessionFacade()
	{
		return storeSessionFacade;
	}

	@Required
	public void setStoreSessionFacade(final StoreSessionFacade storeSessionFacade)
	{
		this.storeSessionFacade = storeSessionFacade;
	}

	protected NameWithTitleFormatStrategy getNameWithTitleFormatStrategy()
	{
		return nameWithTitleFormatStrategy;
	}

	@Required
	public void setNameWithTitleFormatStrategy(final NameWithTitleFormatStrategy nameWithTitleFormatStrategy)
	{
		this.nameWithTitleFormatStrategy = nameWithTitleFormatStrategy;
	}

	@Override
	public void populate(final AddressModel source, final AddressData target) throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setCity(extractCity(source));
		target.setDistrict(extractDistrict(source));
		final String fullname = source.getFullname();
		if (StringUtils.isEmpty(fullname))
		{
			target.setFullnameWithTitle(nameWithTitleFormatStrategy.getFullnameWithTitle(source.getFirstname(), source.getLastname(),
					extractTitleName(source.getTitle())));
		}
		else
		{
			target.setFullname(fullname);
			target.setFullnameWithTitle(
					nameWithTitleFormatStrategy.getFullnameWithTitle(fullname, extractTitleName(source.getTitle())));
		}
		final String cellphone = source.getCellphone();
		if (StringUtils.isNotEmpty(cellphone))
		{
			target.setCellphone(cellphone);
		}
	}

	protected CityData extractCity(final AddressModel source)
	{
		final CityData city = new CityData();
		if (source.getCity() != null)
		{
			if (StringUtils.isNotEmpty(source.getCity().getIsocode()))
			{
				city.setCode(source.getCity().getIsocode());
			}
			if (StringUtils.isNotEmpty(source.getCity().getName()))
			{
				city.setName(source.getCity().getName());
			}
		}
		return city;
	}

	protected DistrictData extractDistrict(final AddressModel source)
	{
		final DistrictData district = new DistrictData();
		if (source.getCityDistrict() != null)
		{
			if (StringUtils.isNotEmpty(source.getCityDistrict().getIsocode()))
			{
				district.setCode(source.getCityDistrict().getIsocode());
			}
			if (StringUtils.isNotEmpty(source.getCityDistrict().getName()))
			{
				district.setName(source.getCityDistrict().getName());
			}
		}
		return district;
	}

	/**
	 * if address.getTitle() is null, return reverent by default
	 *
	 * @param title
	 * @return title name
	 */
	protected String extractTitleName(final TitleModel title)
	{
		if (title == null)
		{
			return "";
		}
		final LanguageData currentLanguage = storeSessionFacade.getCurrentLanguage();
		final String currentISOCode = currentLanguage.getIsocode();
		final Locale currentLocale = new Locale(currentISOCode);
		return title.getName(currentLocale);
	}
}
