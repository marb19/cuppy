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


import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.facades.KBKeyData;
import de.hybris.platform.sap.productconfig.frontend.UiStatus;
import de.hybris.platform.sap.productconfig.frontend.breadcrumb.ProductConfigureBreadcrumbBuilder;
import de.hybris.platform.sap.productconfig.frontend.constants.Sapproductconfigb2baddonConstants;
import de.hybris.platform.sap.productconfig.frontend.util.impl.UiStatusSync;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping()
public class ConfigureProductController extends AbstractProductConfigController
{
	@Resource(name = "sapProductConfigBreadcrumbBuilder")
	private ProductConfigureBreadcrumbBuilder productConfigurationBreadcrumbBuilder;

	static private final Logger LOG = Logger.getLogger(ConfigureProductController.class);

	@RequestMapping(value = "/**/{productCode:.*}/configEntry", method = RequestMethod.GET)
	public String configureProduct(@PathVariable("productCode")
	final String productCode) throws CMSItemNotFoundException, UnsupportedEncodingException
	{

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Config GET received for '" + productCode + "' - Current Session: '"
					+ getSessionService().getCurrentSession().getSessionId() + "'");
		}

		final UiStatus uiStatus = getSessionAccessFacade().getUiStatusForProduct(productCode);
		final String selectedGroup = getSelectedGroup(uiStatus);

		String redirectURL;
		if (selectedGroup != null)
		{
			redirectURL = "redirect:/" + productCode + "/config?tab=" + selectedGroup;
		}
		else
		{
			redirectURL = "redirect:/" + productCode + "/config";
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Redirect to: '" + redirectURL + "'");
		}

		return redirectURL;
	}

	@RequestMapping(value = "/**/{productCode:.*}/config", method = RequestMethod.GET)
	public String configureProduct(@PathVariable("productCode")
	final String productCode, @RequestParam(value = "tab", required = false)
	final String selectedGroup, final Model model, final HttpServletRequest request)
			throws CMSItemNotFoundException, UnsupportedEncodingException
	{

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Config GET received for '" + productCode + "' - Current Session: '"
					+ getSessionService().getCurrentSession().getSessionId() + "'");
		}

		final ProductModel productmodel = getProductService().getProductForCode(productCode);
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, productConfigurationBreadcrumbBuilder.getBreadcrumbs(productmodel));

		populateConfigurationModel(request, selectedGroup, model, productCode);

		final String viewName = "addon:/" + Sapproductconfigb2baddonConstants.EXTENSIONNAME
				+ "/pages/configuration/configurationPage";
		return viewName;
	}

	protected void populateConfigurationModel(final HttpServletRequest request, final String selectedGroup, final Model model,
			final String productCode) throws CMSItemNotFoundException
	{
		final ProductModel productModel = populateProductModel(productCode, model, request);
		if (model.containsAttribute(Sapproductconfigb2baddonConstants.CONFIG_ATTRIBUTE))
		{
			return;
		}

		final KBKeyData kbKey = createKBKeyForProduct(productModel);
		String configId = null;
		final ConfigurationData configData;

		final UiStatus uiStatus = getSessionAccessFacade().getUiStatusForProduct(productCode);
		if (uiStatus != null)
		{
			configId = uiStatus.getConfigId();
			configData = reloadConfiguration(kbKey, configId, selectedGroup, uiStatus);
		}
		else
		{
			configData = loadNewConfiguration(kbKey, selectedGroup, null);
		}

		final UiStatusSync uiStatusSync = new UiStatusSync();

		uiStatusSync.compileGroupForDisplay(configData, uiStatus);

		setCartItemPk(configData);

		model.addAttribute(Sapproductconfigb2baddonConstants.CONFIG_ATTRIBUTE, configData);

		final BindingResult errors = getBindingResultForConfig(configData, uiStatus);
		model.addAttribute(BindingResult.MODEL_KEY_PREFIX + Sapproductconfigb2baddonConstants.CONFIG_ATTRIBUTE, errors);
		countNumberOfUiErrorsPerGroup(configData.getGroups());

		logModelmetaData(configData);
	}

	@RequestMapping(value = "/**/{productCode:.*}/addToCartPopup", method = RequestMethod.GET)
	public String configAddToCartPopup(@PathVariable("productCode")
	final String productCode, final Model model, final HttpServletRequest request) throws CMSItemNotFoundException
	{

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Retrieving content for addToCartPopup via GET ('" + productCode + "')");
		}


		populateConfigurationModel(request, null, model, productCode);

		final String fragmentURL = "addon:/" + Sapproductconfigb2baddonConstants.EXTENSIONNAME
				+ "/fragments/configuration/configAddToCartPopup";

		return fragmentURL;
	}
}
