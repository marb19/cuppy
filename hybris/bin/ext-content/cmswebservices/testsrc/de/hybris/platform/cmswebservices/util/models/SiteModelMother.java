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

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSSiteDao;
import de.hybris.platform.cmswebservices.util.builder.SiteModelBuilder;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Required;


public class SiteModelMother extends AbstractModelMother<CMSSiteModel>
{
	public static final String ELECTRONICS = "electronics";

	private CMSSiteDao cmsSiteDao;
	private LanguageModelMother languageModelMother;
	private ContentCatalogModelMother catalogModelMother;

	protected CMSSiteModel defaultSite()
	{
		return SiteModelBuilder.aModel().withActive(Boolean.TRUE).build();
	}

	public CMSSiteModel createElectronicsWithAppleStagedCatalog()
	{
		return getFromCollectionOrSaveAndReturn(
				() -> getCmsSiteDao().findCMSSitesById(ELECTRONICS),
				() -> SiteModelBuilder.fromModel(defaultSite())
						.withUid(ELECTRONICS)
      				.withName(ELECTRONICS, Locale.ENGLISH)
      				.withLanguage(getLanguageModelMother().createEnglish())
      				.withDefaultCatalog(getCatalogModelMother().createAppleContentCatalogModel()).build());
	}

	protected LanguageModelMother getLanguageModelMother()
	{
		return languageModelMother;
	}

	@Required
	public void setLanguageModelMother(final LanguageModelMother languageModelMother)
	{
		this.languageModelMother = languageModelMother;
	}

	protected ContentCatalogModelMother getCatalogModelMother()
	{
		return catalogModelMother;
	}

	@Required
	public void setCatalogModelMother(final ContentCatalogModelMother catalogModelMother)
	{
		this.catalogModelMother = catalogModelMother;
	}

	protected CMSSiteDao getCmsSiteDao()
	{
		return cmsSiteDao;
	}

	@Required
	public void setCmsSiteDao(final CMSSiteDao cmsSiteDao)
	{
		this.cmsSiteDao = cmsSiteDao;
	}
}
