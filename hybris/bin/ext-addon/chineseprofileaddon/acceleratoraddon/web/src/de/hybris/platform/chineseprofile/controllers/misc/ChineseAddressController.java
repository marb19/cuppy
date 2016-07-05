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
package de.hybris.platform.chineseprofile.controllers.misc;

import de.hybris.platform.chineseprofilefacades.address.AddressFacade;
import de.hybris.platform.chineseprofilefacades.data.CityData;
import de.hybris.platform.chineseprofilefacades.data.DistrictData;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 *
 */
@Controller
@Scope("tenant")
@RequestMapping("/cn-address")
public class ChineseAddressController
{
	@Resource(name = "chineseAddressFacade")
	private AddressFacade chineseAddressFacade;

	@RequestMapping(value = "/region/{code}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<CityData> getRegionAddressForm(@PathVariable("code") final String region)
	{
		return chineseAddressFacade.getCitiesForRegion(region);
	}

	@RequestMapping(value = "/city/{code}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<DistrictData> getCityAddressForm(@PathVariable("code") final String city)
	{
		return chineseAddressFacade.getDistrictsForCity(city);
	}

}
