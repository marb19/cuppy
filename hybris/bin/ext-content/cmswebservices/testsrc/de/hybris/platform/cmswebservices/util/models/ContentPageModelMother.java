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
import de.hybris.platform.cms2.enums.CmsApprovalStatus;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cmswebservices.util.builder.ContentPageModelBuilder;
import de.hybris.platform.cmswebservices.util.dao.impl.ContentPageDao;
import org.springframework.beans.factory.annotation.Required;

import static de.hybris.platform.cmswebservices.util.models.MediaModelMother.MediaTemplate.THUMBNAIL;


public class ContentPageModelMother extends AbstractModelMother<ContentPageModel>
{

	public static final String UID_HOMEPAGE = "uid-homepage";

	private ContentPageDao contentPageDao;
	private PageTemplateModelMother pageTemplateModelMother;
	private MediaModelMother mediaModelMother;

	public ContentPageModel Homepage_Page(CatalogVersionModel catalogVersion)
	{
		return getOrSaveAndReturn( //
				() -> getContentPageDao().getByUidAndCatalogVersion(UID_HOMEPAGE, catalogVersion), //
				() -> ContentPageModelBuilder.aModel() //
						.withUid(UID_HOMEPAGE) //
						.withCatalogVersion(catalogVersion) //
						.asHomepage()
						.withMasterTemplate(pageTemplateModelMother.HomePage_Template(catalogVersion)) //
						.withDefaultPage(Boolean.TRUE) //
						.withApprovalStatus(CmsApprovalStatus.APPROVED) //
						.withThumbnail(mediaModelMother.createMediaModel(catalogVersion, THUMBNAIL))
						.build());
	}

	public ContentPageDao getContentPageDao()
	{
		return contentPageDao;
	}

	@Required
	public void setContentPageDao(ContentPageDao contentPageDao)
	{
		this.contentPageDao = contentPageDao;
	}

	public PageTemplateModelMother getPageTemplateModelMother()
	{
		return pageTemplateModelMother;
	}

	@Required
	public void setPageTemplateModelMother(PageTemplateModelMother pageTemplateModelMother)
	{
		this.pageTemplateModelMother = pageTemplateModelMother;
	}

	public MediaModelMother getMediaModelMother() {
		return mediaModelMother;
	}

	@Required
	public void setMediaModelMother(MediaModelMother mediaModelMother) {
		this.mediaModelMother = mediaModelMother;
	}
}
