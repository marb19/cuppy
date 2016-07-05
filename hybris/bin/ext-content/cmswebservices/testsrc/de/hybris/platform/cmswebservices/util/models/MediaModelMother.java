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
import de.hybris.platform.cmswebservices.util.builder.MediaModelBuilder;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.impl.MediaDao;

import static de.hybris.platform.cmswebservices.util.models.MediaModelMother.MediaTemplate.LOGO;


public class MediaModelMother extends AbstractModelMother<MediaModel>
{

	public enum MediaTemplate {
		LOGO("code-media-logo" ,"filename-media-logo" ,"mimetype-media-logo" ,"alt-text-media-logo" ,"description-media-logo" ,"url-media-logo"),
		THUMBNAIL("code-media-thumbnail" ,"filename-media-thumbnail" ,"mimetype-media-thumbnail" ,"alt-text-media-thumbnail" ,"description-media-thumbnail" ,"url-media-thumbnail");

		private final String code;
		private final String filename;
		private final String mimetype;
		private final String altText;
		private final String description;
		private final String url;

		MediaTemplate(final String code, final String filename, final String mimetype, final String altText, final String description, final String url) {
			this.code = code;
			this.filename = filename;
			this.mimetype = mimetype;
			this.altText = altText;
			this.description = description;
			this.url = url;
		}

		public String getCode() {
			return code;
		}

		public String getFilename() {
			return filename;
		}

		public String getMimetype() {
			return mimetype;
		}

		public String getAltText() {
			return altText;
		}

		public String getDescription() {
			return description;
		}

		public String getUrl() {
			return url;
		}
	}

	private MediaDao mediaDao;

	public MediaModel createLogoMediaModel(CatalogVersionModel catalogVersion)
	{
		return createMediaModel(catalogVersion, LOGO);
	}

	public MediaModel createLogoMediaModelWithCode(CatalogVersionModel catalogVersion, final String code)
	{
		return createMediaModelWithCode(catalogVersion, LOGO, code);
	}

	public MediaModel createMediaModel(CatalogVersionModel catalogVersion, MediaTemplate template)
	{
		return createMediaModelWithCode(catalogVersion, template, template.getCode());
	}

	private  MediaModel createMediaModelWithCode(CatalogVersionModel catalogVersion, MediaTemplate template, final String code)
	{

		return getFromCollectionOrSaveAndReturn(
				() -> getMediaDao().findMediaByCode(catalogVersion, code),
				() -> MediaModelBuilder.aModel() //
						.withCatalogVersion(catalogVersion) //
						.withCode(code) //
						.withRealFileName(template.getFilename()) //
						.withMimeType(template.getMimetype()) //
						.withUrl(template.getUrl()) //
						.withAltText(template.getAltText()) //
						.withDescription(template.getDescription()) //
						.build());
	}

	public MediaDao getMediaDao()
	{
		return mediaDao;
	}

	public void setMediaDao(MediaDao mediaDao)
	{
		this.mediaDao = mediaDao;
	}


}
