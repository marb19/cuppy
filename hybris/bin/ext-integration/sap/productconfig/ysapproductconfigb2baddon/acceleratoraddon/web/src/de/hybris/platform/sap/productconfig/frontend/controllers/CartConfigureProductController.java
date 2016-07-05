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
import de.hybris.platform.sap.productconfig.facades.UiGroupData;
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
public class CartConfigureProductController extends AbstractProductConfigController
{
	@Resource(name = "sapProductConfigBreadcrumbBuilder")
	private ProductConfigureBreadcrumbBuilder productConfigurationBreadcrumbBuilder;

	static private final Logger LOG = Logger.getLogger(CartConfigureProductController.class);

	private final UiStatusSync uiSync = new UiStatusSync();

	@RequestMapping(value = "/**/{configCartItemHandle:.*}/{productCode:.*}/configCartEntry", method = RequestMethod.GET)
	public String configureCartEntry(@PathVariable("configCartItemHandle")
	final String cartEntryHandle, @PathVariable("productCode")
	final String productCode) throws CMSItemNotFoundException, UnsupportedEncodingException
	{

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Retrieve content for cartEntry via GET ('" + productCode + "')");
			LOG.debug("Current Session: '" + getSessionService().getCurrentSession().getSessionId() + "'");
		}

		final KBKeyData kbKey = new KBKeyData();
		kbKey.setProductCode(productCode);
		final UiStatus uiStatus = getUiStatusFromSession(cartEntryHandle, kbKey);
		if (uiStatus == null)
		{
			final String redirectUrl = REDIRECT_PREFIX + "/";
			return redirectUrl;
		}
		getSessionAccessFacade().setUiStatusForProduct(productCode, uiStatus);

		final String selectedGroup = getSelectedGroup(uiStatus);

		return redirectToProductConfig(cartEntryHandle, productCode, selectedGroup);
	}


	@RequestMapping(value = "/**/{configCartItemHandle:.*}/{productCode:.*}/configCart", method = RequestMethod.GET)
	public String configureCartEntry(@PathVariable("configCartItemHandle")
	final String cartItemHandle, @PathVariable("productCode")
	final String productCode, @RequestParam(value = "tab", required = false)
	final String selectedGroup, final Model model, final HttpServletRequest request) throws CMSItemNotFoundException,
			UnsupportedEncodingException
	{

		final ProductModel productmodel = getProductService().getProductForCode(productCode);
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, productConfigurationBreadcrumbBuilder.getBreadcrumbs(productmodel));

		populateConfigurationModel(request, selectedGroup, model, cartItemHandle, productCode);

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Retrieve content for cartEntry via GET ('" + cartItemHandle + "')");
			LOG.debug("Current Session: '" + getSessionService().getCurrentSession().getSessionId() + "'");
		}

		final String viewName = "addon:/" + Sapproductconfigb2baddonConstants.EXTENSIONNAME
				+ "/pages/configuration/configurationPage";

		return viewName;
	}

	protected void populateConfigurationModel(final HttpServletRequest request, final String selectedGroup, final Model model,
			final String cartEntryHandleFromRequest, final String productCode) throws CMSItemNotFoundException
	{
		if (model.containsAttribute(Sapproductconfigb2baddonConstants.CONFIG_ATTRIBUTE))
		{
			return;
		}
		String configId = null;

		final ProductModel productModel = populateProductModel(productCode, model, request);
		final KBKeyData kbKey = createKBKeyForProduct(productModel);

		final ConfigurationData configData;
		UiStatus uiStatus = getUiStatusFromSession(cartEntryHandleFromRequest, kbKey);

		//assume configId != null
		configId = uiStatus.getConfigId();

		configData = reloadConfiguration(kbKey, configId, selectedGroup, uiStatus);

		// assume cartEntryHandleFromRequest != null
		configData.setCartItemPK(cartEntryHandleFromRequest);

		model.addAttribute(Sapproductconfigb2baddonConstants.CONFIG_ATTRIBUTE, configData);

		final BindingResult errors = getBindingResultForConfig(configData, uiStatus);

		configData.setAutoExpand(true);

		final UiGroupData expandedGroup = handleAutoExpand(configData);
		if (expandedGroup != null)
		{
			uiStatus.setGroupIdToDisplay(expandedGroup.getId());
			uiSync.compileGroupForDisplay(configData, uiStatus);
		}
		uiStatus = uiSync.extractUiStatusFromConfiguration(configData);
		getSessionAccessFacade().setUiStatusForProduct(productCode, uiStatus);


		model.addAttribute(BindingResult.MODEL_KEY_PREFIX + Sapproductconfigb2baddonConstants.CONFIG_ATTRIBUTE, errors);
	}

	/**
	 * Retrieves UI status based on a configuration attached to a cart entry
	 *
	 * @param cartItemHandle
	 * @param kbKey
	 * @return Null if no UI status could be created.
	 */
	protected UiStatus getUiStatusFromSession(final String cartItemHandle, final KBKeyData kbKey)
	{
		UiStatus uiStatus = getSessionAccessFacade().getUiStatusForCartEntry(cartItemHandle);

		//this shall happen only when the cart is restored
		if (uiStatus == null)
		{
			final String configId = getSessionAccessFacade().getConfigIdForCartEntry(cartItemHandle);
			final ConfigurationData confData = getConfigDataForRestoredProduct(kbKey, configId, cartItemHandle);
			if (confData == null)
			{
				return null;
			}

			logModelmetaData(confData);

			final UiStatusSync uiStatusSync = new UiStatusSync();
			uiStatus = uiStatusSync.extractUiStatusFromConfiguration(confData);
		}

		return uiStatus;
	}

	/**
	 * Fetches a configuration which might already reside in the session (configId != null) or which needs to be created
	 * from the external configuration attached to a cart entry.
	 *
	 * @param kbKey
	 * @param configId
	 * @param cartItemHandle
	 * @return Null if no configuration could be created
	 */
	protected ConfigurationData getConfigDataForRestoredProduct(final KBKeyData kbKey, final String configId,
			final String cartItemHandle)
	{
		ConfigurationData confData = null;
		if (configId == null)
		{
			confData = this.loadNewConfiguration(kbKey, null, cartItemHandle);
		}
		else
		{
			confData = this.getConfigData(kbKey, configId);
		}
		return confData;
	}



	private String redirectToProductConfig(final String cartEntryHandle, final String productCode, final String selectedGroup)
	{
		String redirectURL;
		if (selectedGroup != null)
		{
			redirectURL = "redirect:/" + cartEntryHandle + "/" + productCode + "/configCart?tab=" + selectedGroup;
		}
		else
		{
			redirectURL = "redirect:/" + cartEntryHandle + "/" + productCode + "/configCart";
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Redirect to: '" + redirectURL + "'");
		}

		return redirectURL;
	}



}
