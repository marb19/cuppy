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
package de.hybris.platform.smarteditaddon.cms.services.impl;

import de.hybris.platform.acceleratorcms.model.components.NavigationBarCollectionComponentModel;
import de.hybris.platform.acceleratorcms.model.components.NavigationBarComponentModel;
import de.hybris.platform.acceleratorcms.services.CMSDynamicAttributeService;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.smarteditaddon.constants.SmarteditaddonConstants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.servlet.jsp.PageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * SmartEdit implementation of the {@link CMSDynamicAttributeService}.
 */
public class CMSSmartEditDynamicAttributeService implements CMSDynamicAttributeService
{
	private static final Logger LOG = LoggerFactory.getLogger(CMSSmartEditDynamicAttributeService.class);

	private static final String SMART_EDIT_COMPONENT_CLASS = "smartEditComponent";
	private static final String CLASS_ATTRIBUTE = "class";
	private static final String SMARTEDIT_COMPONENT_TYPE_ATTRIBUTE = "data-smartedit-component-type";
	private static final String SMARTEDIT_COMPONENT_ID_ATTRIBUTE = "data-smartedit-component-id";

	private ConfigurationService configurationService;

	@Override
	public Map<String, String> getDynamicComponentAttributes(final AbstractCMSComponentModel component,
			final ContentSlotModel contentSlot)
	{
		if (!isEnabled())
		{
			return Collections.emptyMap();
		}
		final Map<String, String> dynamicAttributes = new HashMap<String, String>();

		if (component != null && contentSlot != null)
		{
			dynamicAttributes.put(SMARTEDIT_COMPONENT_ID_ATTRIBUTE, component.getUid());
			dynamicAttributes.put(SMARTEDIT_COMPONENT_TYPE_ATTRIBUTE, component.getItemtype());
			dynamicAttributes.put(CLASS_ATTRIBUTE, SMART_EDIT_COMPONENT_CLASS);
		}

		return dynamicAttributes;
	}

	@Override
	public Map<String, String> getDynamicContentSlotAttributes(final ContentSlotModel contentSlot, PageContext pageContext, Map<String, String> initialMaps)
	{
		if (!isEnabled())
		{
			return Collections.emptyMap();
		}
		final Map<String, String> dynamicAttributes = new HashMap<String, String>();

		if (contentSlot != null)
		{
			dynamicAttributes.put(SMARTEDIT_COMPONENT_ID_ATTRIBUTE, contentSlot.getUid());
			dynamicAttributes.put(SMARTEDIT_COMPONENT_TYPE_ATTRIBUTE, contentSlot.getItemtype());
			dynamicAttributes.put(CLASS_ATTRIBUTE, SMART_EDIT_COMPONENT_CLASS);
		}

		return dynamicAttributes;
	}

	@Override
	public void afterAllItems(final PageContext pageContext)
	{
		if (!isEnabled())
		{
			return;
		}
	}

	/**
	 * will wrapp in a div any smarteditcomponent that is neither a NavigationBarComponent nor NavigationBarCollectionComponent
	 * @param cmsItemModel
	 * @return
	 */
	@Override
	public String getFallbackElement(CMSItemModel cmsItemModel) {
		if (!isEnabled())
		{
			return null;
		}
		if (NavigationBarComponentModel.class.isAssignableFrom(cmsItemModel.getClass())
				|| NavigationBarCollectionComponentModel.class.isAssignableFrom(cmsItemModel.getClass())) {
			return null;
		}else{
			return "div";
		}
	}


	/**
	 * Checks if smarteditaddon is enabled using the configurationService.
	 *
	 * @return true if this dyanamic attribute service is enabled and false otherwise.
	 */
	protected boolean isEnabled()
	{
		boolean enabled = false;
		try
		{
			enabled = getConfigurationService().getConfiguration().getBoolean(
					SmarteditaddonConstants.SMARTEDIT_DYNAMICATTRIBUTE_ENABLED);
		}
		catch (NoSuchElementException e)
		{
			LOG.info("Failed to load ' " + SmarteditaddonConstants.SMARTEDIT_DYNAMICATTRIBUTE_ENABLED + "' configuration.", e);
		}
		return enabled;
	}


	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

}
