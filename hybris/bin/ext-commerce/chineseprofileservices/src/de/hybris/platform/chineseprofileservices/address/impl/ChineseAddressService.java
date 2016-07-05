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
package de.hybris.platform.chineseprofileservices.address.impl;

import de.hybris.platform.chineseprofileservices.address.AddressService;
import de.hybris.platform.chineseprofileservices.address.daos.AddressDao;
import de.hybris.platform.chineseprofileservices.model.CityModel;
import de.hybris.platform.chineseprofileservices.model.DistrictModel;
import de.hybris.platform.servicelayer.user.impl.DefaultAddressService;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


public class ChineseAddressService extends DefaultAddressService implements AddressService
{
	private AddressDao chineseAddressDao;

	@Override
	public List<CityModel> getCitiesForRegion(final String regionCode)
	{
		final List<CityModel> result = chineseAddressDao.getCitiesForRegion(regionCode);
		return result.isEmpty() ? Collections.EMPTY_LIST : result;
	}

	@Override
	public List<DistrictModel> getDistrictsForCity(final String cityCode)
	{
		final List<DistrictModel> result = chineseAddressDao.getDistrictsForCity(cityCode);
		return result.isEmpty() ? Collections.EMPTY_LIST : result;
	}

	@Override
	public CityModel getCityForIsocode(final String isocode)
	{
		return chineseAddressDao.getCityForIsocode(isocode);
	}

	@Override
	public DistrictModel getDistrictForIsocode(final String isocode)
	{
		return chineseAddressDao.getDistrictForIsocode(isocode);
	}

	protected AddressDao getChineseAddressDao()
	{
		return chineseAddressDao;
	}

	@Required
	public void setChineseAddressDao(final AddressDao chineseAddressDao)
	{
		this.chineseAddressDao = chineseAddressDao;
	}

}
