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
package de.hybris.platform.cmswebservices.util.builder;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaModel;


public class MediaModelBuilder
{

	private final MediaModel model;

	private MediaModelBuilder()
	{
		model = new MediaModel();
	}

	private MediaModelBuilder(MediaModel model)
	{
		this.model = model;
	}

	private MediaModel getModel()
	{
		return this.model;
	}

	public static MediaModelBuilder aModel()
	{
		return new MediaModelBuilder();
	}

	public static MediaModelBuilder fromModel(MediaModel model)
	{
		return new MediaModelBuilder(model);
	}

	public MediaModelBuilder withCatalogVersion(CatalogVersionModel catalogVersion)
	{
		getModel().setCatalogVersion(catalogVersion);
		return this;
	}

	public MediaModelBuilder withMimeType(String mimetype)
	{
		getModel().setMime(mimetype);
		return this;
	}

	public MediaModelBuilder withRealFileName(String realFilename)
	{
		getModel().setRealFileName(realFilename);
		return this;
	}

	public MediaModelBuilder withCode(String code)
	{
		getModel().setCode(code);
		return this;
	}

	public MediaModelBuilder withUrl(String url)
	{
		getModel().setURL(url);
		return this;
	}

	public MediaModelBuilder withAltText(String altText)
	{
		getModel().setAltText(altText);
		return this;
	}

	public MediaModelBuilder withDescription(String description)
	{
		getModel().setDescription(description);
		return this;
	}

	public MediaModel build()
	{
		return this.getModel();
	}

}
