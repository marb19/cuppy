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
package de.hybris.platform.chineseprofileservices.address.daos;

import de.hybris.platform.chineseprofileservices.model.CityModel;
import de.hybris.platform.chineseprofileservices.model.DistrictModel;

import java.util.List;


public interface AddressDao extends de.hybris.platform.servicelayer.user.daos.AddressDao
{
	/**
	 * Find cities by region code
	 *
	 * @param regionCode
	 * @return cities
	 */
	List<CityModel> getCitiesForRegion(String regionCode);

	/**
	 * Find districts by city code
	 *
	 * @param cityCode
	 * @return districts
	 */
	List<DistrictModel> getDistrictsForCity(String cityCode);

	/**
	 * Find city by its code
	 *
	 * @param isocode
	 * @return city
	 */
	CityModel getCityForIsocode(String isocode);

	/**
	 * Find district by its code
	 *
	 * @param isocode
	 * @return district
	 */
	DistrictModel getDistrictForIsocode(String isocode);
}
