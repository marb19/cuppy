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
import de.hybris.platform.cms2.model.relations.ContentSlotForTemplateModel;
import de.hybris.platform.cmswebservices.util.builder.ContentSlotForTemplateModelBuilder;
import de.hybris.platform.cmswebservices.util.dao.impl.ContentSlotForTemplateDao;

import org.springframework.beans.factory.annotation.Required;


public class ContentSlotForTemplateModelMother extends AbstractModelMother<ContentSlotForTemplateModel>
{
	public static final String UID_LOGO_HOMEPAGE = "uid-logo-homepage";

	private ContentSlotForTemplateDao contentSlotForTemplateDao;
	private ContentSlotModelMother contentSlotModelMother;
	private PageTemplateModelMother pageTemplateModelMother;

	public ContentSlotForTemplateModel LogoHomepage(CatalogVersionModel catalogVersion)
	{
		return getOrSaveAndReturn( //
				() -> contentSlotForTemplateDao.getByUidAndCatalogVersion(UID_LOGO_HOMEPAGE, catalogVersion), //
				() -> ContentSlotForTemplateModelBuilder.aModel() //
						.withUid(UID_LOGO_HOMEPAGE) //
						.withCatalogVersion(catalogVersion) //
						.withAllowOverwrite(Boolean.TRUE) //
						.withContentSlot(contentSlotModelMother.Logo_Slot(catalogVersion)) //
						.withPageTemplate(pageTemplateModelMother.HomePage_Template(catalogVersion)) //
						.withPosition(ContentSlotNameModelMother.NAME_LOGO) //
						.build());
	}


	protected ContentSlotForTemplateDao getContentSlotForTemplateDao()
	{
		return contentSlotForTemplateDao;
	}

	@Required
	public void setContentSlotForTemplateDao(ContentSlotForTemplateDao contentSlotForTemplateDao)
	{
		this.contentSlotForTemplateDao = contentSlotForTemplateDao;
	}


}
