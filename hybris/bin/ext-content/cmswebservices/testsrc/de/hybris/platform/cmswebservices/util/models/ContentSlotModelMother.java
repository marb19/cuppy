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
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSContentSlotDao;
import de.hybris.platform.cmswebservices.util.builder.ContentSlotModelBuilder;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Required;


public class ContentSlotModelMother extends AbstractModelMother<ContentSlotModel>
{
	public static final String UID_LOGO = "uid-contentslot-logo";
	public static final String UID_HEADER = "uid-contentslot-header";
	public static final String UID_FOOTER = "uid-contentslot-footer";

	private CMSContentSlotDao cmsContentSlotDao;
	private ParagraphComponentModelMother paragraphComponentModelMother;
	private SimpleBannerComponentModelMother simpleBannerComponentModelMother;

	public ContentSlotModel Logo_Slot(final CatalogVersionModel catalogVersion)
	{
		return getFromCollectionOrSaveAndReturn( //
				() -> getCmsContentSlotDao().findContentSlotsByIdAndCatalogVersions(UID_LOGO,
						Collections.singletonList(catalogVersion)), //
				() -> ContentSlotModelBuilder.fromModel(defaultSlot(catalogVersion)) //
						.withUid(UID_LOGO) //
						.withCmsComponents( //
								simpleBannerComponentModelMother.createHeaderLogoBannerComponentModel(catalogVersion)) //
						.build());
	}

	public ContentSlotModel createFooterEmptySlot(final CatalogVersionModel catalogVersion)
	{
		return getFromCollectionOrSaveAndReturn( //
				() -> getCmsContentSlotDao().findContentSlotsByIdAndCatalogVersions(UID_FOOTER,
						Collections.singletonList(catalogVersion)), //
				() -> ContentSlotModelBuilder.fromModel(defaultSlot(catalogVersion)) //
						.withUid(UID_FOOTER) //
						.build());
	}

	public ContentSlotModel createHeaderSlotWithParagraphAndBanner(final CatalogVersionModel catalogVersion)
	{
		return getFromCollectionOrSaveAndReturn( //
				() -> getCmsContentSlotDao().findContentSlotsByIdAndCatalogVersions(UID_HEADER,
						Collections.singletonList(catalogVersion)), //
				() -> ContentSlotModelBuilder.fromModel(defaultSlot(catalogVersion)) //
						.withUid(UID_HEADER) //
						.withCmsComponents( //
								paragraphComponentModelMother.createHeaderParagraphComponentModel(catalogVersion), //
								simpleBannerComponentModelMother.createHeaderLogoBannerComponentModel(catalogVersion)) //
						.build());
	}

	public ContentSlotModel createHeaderSlotWithParagraph(final CatalogVersionModel catalogVersion)
	{
		return getFromCollectionOrSaveAndReturn( //
				() -> getCmsContentSlotDao().findContentSlotsByIdAndCatalogVersions(UID_HEADER,
						Collections.singletonList(catalogVersion)), //
				() -> ContentSlotModelBuilder.fromModel(defaultSlot(catalogVersion)) //
						.withUid(UID_HEADER) //
						.withCmsComponents( //
								paragraphComponentModelMother.createHeaderParagraphComponentModel(catalogVersion)) //
						.build());
	}

	protected ContentSlotModel defaultSlot(final CatalogVersionModel catalogVersion)
	{
		return ContentSlotModelBuilder.aModel() //
				.withCatalogVersion(catalogVersion) //
				.build();
	}

	public CMSContentSlotDao getCmsContentSlotDao()
	{
		return cmsContentSlotDao;
	}

	@Required
	public void setCmsContentSlotDao(final CMSContentSlotDao cmsContentSlotDao)
	{
		this.cmsContentSlotDao = cmsContentSlotDao;
	}

	public ParagraphComponentModelMother getParagraphComponentModelMother()
	{
		return paragraphComponentModelMother;
	}

	@Required
	public void setParagraphComponentModelMother(final ParagraphComponentModelMother paragraphComponentModelMother)
	{
		this.paragraphComponentModelMother = paragraphComponentModelMother;
	}

	public SimpleBannerComponentModelMother getSimpleBannerComponentModelMother()
	{
		return simpleBannerComponentModelMother;
	}

	@Required
	public void setSimpleBannerComponentModelMother(final SimpleBannerComponentModelMother simpleBannerComponentModelMother)
	{
		this.simpleBannerComponentModelMother = simpleBannerComponentModelMother;
	}

}
