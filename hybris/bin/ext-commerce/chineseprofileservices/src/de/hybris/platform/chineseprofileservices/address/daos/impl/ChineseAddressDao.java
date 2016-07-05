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
package de.hybris.platform.chineseprofileservices.address.daos.impl;

import de.hybris.platform.chineseprofileservices.address.daos.AddressDao;
import de.hybris.platform.chineseprofileservices.model.CityModel;
import de.hybris.platform.chineseprofileservices.model.DistrictModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.user.daos.impl.DefaultAddressDao;

import java.util.List;


public class ChineseAddressDao extends DefaultAddressDao implements AddressDao
{
	@Override
	public List<CityModel> getCitiesForRegion(final String regionCode)
	{
		// SELECT {c:pk},{c:name} FROM { City AS c JOIN Region AS r ON {c:region} = {r:pk} AND {r:isocode} = 'CN-42' }
		final String queryString = "SELECT {c:" + CityModel.PK + "}" + "FROM {" + CityModel._TYPECODE + " AS c JOIN "
				+ RegionModel._TYPECODE + " AS r " + "ON {r:" + RegionModel.PK + "} = {c:" + CityModel.REGION + "}  " + " AND {r:"
				+ RegionModel.ISOCODE + "} =?paramRegionCode }";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("paramRegionCode", regionCode);
		return getFlexibleSearchService().<CityModel> search(query).getResult();
	}

	@Override
	public List<DistrictModel> getDistrictsForCity(final String cityCode)
	{
		// SELECT {d:pk} FROM {District AS d JOIN City AS c ON {d:city} = {c.pk} AND {c:isocode} = 'CN-11-1' }
		final String queryString = "SELECT {d:" + DistrictModel.PK + "}" + "FROM {" + DistrictModel._TYPECODE + " AS d JOIN "
				+ CityModel._TYPECODE + " AS c " + "ON {c:" + CityModel.PK + "} = {d:" + DistrictModel.CITY + "}  " + " AND {c:"
				+ CityModel.ISOCODE + "} =?paramCityCode }";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("paramCityCode", cityCode);
		return getFlexibleSearchService().<DistrictModel> search(query).getResult();
	}

	@Override
	public CityModel getCityForIsocode(final String isocode)
	{
		// SELECT {c:pk} FROM { City AS c} WHERE {c:code} = 'CN-11-1'
		final String queryString = "SELECT {c:" + CityModel.PK + "}" + "FROM {" + CityModel._TYPECODE + " AS c } " + "WHERE "
				+ "{c:" + CityModel.ISOCODE + "}=?paramCityCode ";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("paramCityCode", isocode);

		final SearchResult<CityModel> result = getFlexibleSearchService().<CityModel> search(query);
		if (result == null || result.getCount() == 0)
		{
			return null;
		}

		return result.getResult().get(0);
	}

	@Override
	public DistrictModel getDistrictForIsocode(final String isocode)
	{
		// SELECT {d:pk} FROM {District AS d} WHERE {d:code} = 'CN-11-1-1'
		final String queryString = "SELECT {d:" + DistrictModel.PK + "} " + "FROM {" + DistrictModel._TYPECODE + " AS d} "
				+ "WHERE {d:" + DistrictModel.ISOCODE + "}=?paramDistrictCode";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("paramDistrictCode", isocode);

		final SearchResult<DistrictModel> result = getFlexibleSearchService().<DistrictModel> search(query);
		if (result == null || result.getCount() == 0)
		{
			return null;
		}

		return result.getResult().get(0);
	}

}
