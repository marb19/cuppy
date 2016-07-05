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
 */

package de.hybris.platform.fractussyncconfigurationtrigger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import de.hybris.platform.fractussyncservices.service.FractussyncService;


@Controller
@RequestMapping(value = "configuration-request")
public class FractussyncConfigurationRequestController
{

	@Autowired
	private FractussyncService fractussyncService;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void request()
	{
		fractussyncService.pushYaasConfiguration();
	}
}
