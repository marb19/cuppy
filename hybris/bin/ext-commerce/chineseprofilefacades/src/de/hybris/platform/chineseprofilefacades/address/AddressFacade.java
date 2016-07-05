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
package de.hybris.platform.chineseprofilefacades.address;

import de.hybris.platform.chineseprofilefacades.data.CityData;
import de.hybris.platform.chineseprofilefacades.data.DistrictData;

import java.util.List;


public interface AddressFacade
{
	/**
	 * Find city by its code
	 *
	 * @param isocode
	 * @return city
	 */
	CityData getCityForIsocode(String isocode);

	/**
	 * Find district by its code
	 *
	 * @param isocode
	 * @return district
	 */
	DistrictData getDistrcitForIsocode(String isocode);

	/**
	 * Find cities by region code
	 *
	 * @param regionCode
	 * @return cities
	 */
	List<CityData> getCitiesForRegion(String regionCode);

	/**
	 * Find districts by city code
	 *
	 * @param cityCode
	 * @return districts
	 */
	List<DistrictData> getDistrictsForCity(String cityCode);
}
