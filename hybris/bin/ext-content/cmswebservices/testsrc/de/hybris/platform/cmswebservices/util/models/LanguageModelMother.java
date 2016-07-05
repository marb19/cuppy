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
package de.hybris.platform.cmswebservices.util.models;

import de.hybris.platform.cmswebservices.util.builder.LanguageModelBuilder;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.daos.LanguageDao;

import org.springframework.beans.factory.annotation.Required;


public class LanguageModelMother extends AbstractModelMother<LanguageModel>
{
	public static final String CODE_EN = "EN";
	public static final String CODE_FR = "FR";

	private LanguageDao languageDao;

	protected LanguageModel defaultLanguage()
	{
		return LanguageModelBuilder.aModel().withActive(Boolean.TRUE).build();
	}

	public LanguageModel createEnglish() {
		return getFromCollectionOrSaveAndReturn(() -> getLanguageDao().findLanguagesByCode(CODE_EN),
				() -> LanguageModelBuilder.fromModel(defaultLanguage()).withIsocode(CODE_EN).build());
	}

	public LanguageModel createFrench() {
		return getFromCollectionOrSaveAndReturn(() -> getLanguageDao().findLanguagesByCode(CODE_FR),
				() -> LanguageModelBuilder.fromModel(defaultLanguage()).withIsocode(CODE_FR).build());
	}

	protected LanguageDao getLanguageDao()
	{
		return languageDao;
	}

	@Required
	public void setLanguageDao(final LanguageDao languageDao)
	{
		this.languageDao = languageDao;
	}

}
