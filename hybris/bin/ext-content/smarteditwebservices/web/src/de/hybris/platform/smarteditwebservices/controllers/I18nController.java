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
package de.hybris.platform.smarteditwebservices.controllers;

import de.hybris.platform.smarteditwebservices.data.TranslationMapData;
import de.hybris.platform.smarteditwebservices.i18n.facade.SmarteditI18nFacade;

import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller to retrieve cms management internationalization data
 */
@RestController("smartEditI18nController")
@RequestMapping(value = "/i18n")
public class I18nController
{
	@Resource
	private SmarteditI18nFacade smarteEditI18nFacade;

	@RequestMapping(value = "/languages/{locale}", method = RequestMethod.GET)
	@ResponseBody
	public TranslationMapData getTranslationMap(@PathVariable("locale") final Locale locale, final HttpServletRequest request,
			final HttpServletResponse response)
	{
		final TranslationMapData translationMapData = new TranslationMapData();
		translationMapData.setValue(getSmartEditI18nFacade().getTranslationMap(locale));
		return translationMapData;
	}

	protected SmarteditI18nFacade getSmartEditI18nFacade()
	{
		return smarteEditI18nFacade;
	}

	public void setSmartEditI18nFacade(final SmarteditI18nFacade cmsI18nFacade)
	{
		this.smarteEditI18nFacade = cmsI18nFacade;
	}
}