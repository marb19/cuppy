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
package de.hybris.platform.smarteditwebservices.i18n.facade.impl;

import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.smarteditwebservices.i18n.facade.SmarteditI18nFacade;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link SmarteditI18nFacade} to retrieve cms management data
 */
public class DefaultSmarteditI18nFacade implements SmarteditI18nFacade
{
	private L10NService l10nService;
	private I18NService i18nService;

	@Override
	public Map<String, String> getTranslationMap(final Locale locale)
	{
		final ResourceBundle resourceBundle = getL10nService().getResourceBundle(getI18nService().getAllLocales(locale));
		return resourceBundle.keySet().stream().collect(Collectors.toMap(key -> key, key -> resourceBundle.getString(key)));
	}

	protected L10NService getL10nService()
	{
		return l10nService;
	}

	@Required
	public void setL10nService(final L10NService l10nService)
	{
		this.l10nService = l10nService;
	}

	public I18NService getI18nService()
	{
		return i18nService;
	}

	@Required
	public void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
	}

}
