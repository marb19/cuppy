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
package de.hybris.platform.sap.productconfig.frontend.controllers;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.sap.productconfig.frontend.constants.Sapproductconfigb2baddonConstants;
import de.hybris.platform.yacceleratorstorefront.controllers.cms.DefaultCMSComponentController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller("ListAddToCartActionController")
@RequestMapping(value = "/view/ListAddToCartActionController")
public class ListAddToCartActionController extends DefaultCMSComponentController
{

	@Override
	protected String getView(final AbstractCMSComponentModel component)
	{
		return "addon:/" + Sapproductconfigb2baddonConstants.EXTENSIONNAME + "/cms/configureproductaction";
	}

}
