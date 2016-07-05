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

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cmswebservices.util.builder.PageTemplateModelBuilder;
import de.hybris.platform.cmswebservices.util.dao.impl.PageTemplateDao;

import org.springframework.beans.factory.annotation.Required;


public class PageTemplateModelMother extends AbstractModelMother<PageTemplateModel>
{

	public static final String UID_HOME_PAGE = "uid-home-page-template";

	private PageTemplateDao pageTemplateDao;
	private ContentSlotNameModelMother contentSlotNameModelMother;
	private CMSPageTypeModelMother cmsPageTypeModelMother;

	public PageTemplateModel defaultPageTemplate(CatalogVersionModel catalogVersion)
	{
		return PageTemplateModelBuilder.aModel() //
				.withActive(Boolean.TRUE) //
				.withCatalogVersion(catalogVersion) //
				.build();
	}

	public PageTemplateModel HomePage_Template(CatalogVersionModel catalogVersion)
	{
		return getOrSaveAndReturn( //
				() -> pageTemplateDao.getByUidAndCatalogVersion(UID_HOME_PAGE, catalogVersion), //
				() -> PageTemplateModelBuilder.fromModel(defaultPageTemplate(catalogVersion)) //
						.withUid(UID_HOME_PAGE) //
						.withRestrictedPageTypes( //
								cmsPageTypeModelMother.ContentPage(), //
								cmsPageTypeModelMother.CatalogPage()) //
						.build());
	}

	protected PageTemplateDao getPageTemplateDao()
	{
		return pageTemplateDao;
	}

	@Required
	public void setPageTemplateDao(PageTemplateDao pageTemplateDao)
	{
		this.pageTemplateDao = pageTemplateDao;
	}

	protected ContentSlotNameModelMother getContentSlotNameModelMother()
	{
		return contentSlotNameModelMother;
	}

	@Required
	public void setContentSlotNameModelMother(ContentSlotNameModelMother contentSlotNameModelMother)
	{
		this.contentSlotNameModelMother = contentSlotNameModelMother;
	}

	protected CMSPageTypeModelMother getCmsPageTypeModelMother()
	{
		return cmsPageTypeModelMother;
	}

	@Required
	public void setCmsPageTypeModelMother(CMSPageTypeModelMother cmsPageTypeModelMother)
	{
		this.cmsPageTypeModelMother = cmsPageTypeModelMother;
	}

}
