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
import de.hybris.platform.chineseprofileservices.address.AddressService;
import de.hybris.platform.commercefacades.user.converters.populator.AddressReversePopulator;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;


public class ChineseAddressReversePopulator extends AddressReversePopulator
{
	private AddressService chineseAddressService;

	@Override
	public void populate(final AddressData addressData, final AddressModel addressModel) throws ConversionException
	{
		super.populate(addressData, addressModel);
		addressModel.setFullname(addressData.getFullname());
		addressModel.setCellphone(addressData.getCellphone());

		fillInFirstAndLastName(addressModel);

		final CityData city = addressData.getCity();
		if (city != null)
		{
			addressModel.setCity(chineseAddressService.getCityForIsocode(city.getCode()));
		}

		final DistrictData district = addressData.getDistrict();
		if (district != null)
		{
			addressModel.setCityDistrict(chineseAddressService.getDistrictForIsocode(district.getCode()));
		}
	}

	/**
	 * If first and last name are empty, try to copy full name to it, this is needed when guest checks out and creates
	 * new account based on address
	 *
	 * @param addressData
	 * @param addressModel
	 */
	protected void fillInFirstAndLastName(final AddressModel addressModel)
	{
		final String fullName = addressModel.getFullname();
		if (StringUtils.isEmpty(addressModel.getFirstname()) && StringUtils.isEmpty(addressModel.getLastname())
				&& StringUtils.isNotEmpty(fullName))
		{
			addressModel.setFirstname(fullName);
			addressModel.setLastname(StringUtils.EMPTY);
		}
	}

	protected AddressService getChineseAddressService()
	{
		return chineseAddressService;
	}

	@Required
	public void setChineseAddressService(final AddressService chineseAddressService)
	{
		this.chineseAddressService = chineseAddressService;
	}

}
