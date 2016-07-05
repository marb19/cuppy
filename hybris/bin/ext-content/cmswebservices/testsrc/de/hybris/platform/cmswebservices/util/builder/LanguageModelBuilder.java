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

import de.hybris.platform.core.model.c2l.LanguageModel;



public class LanguageModelBuilder
{
	private final LanguageModel model;

	private LanguageModelBuilder()
	{
		model = new LanguageModel();
	}

	private LanguageModelBuilder(final LanguageModel model)
	{
		this.model = model;
	}

	private LanguageModel getModel()
	{
		return this.model;
	}

	public static LanguageModelBuilder aModel()
	{
		return new LanguageModelBuilder();
	}

	public static LanguageModelBuilder fromModel(final LanguageModel model)
	{
		return new LanguageModelBuilder(model);
	}

	public LanguageModel build()
	{
		return this.getModel();
	}

	public LanguageModelBuilder withActive(final Boolean active)
	{
		getModel().setActive(active);
		return this;
	}

	public LanguageModelBuilder withIsocode(final String isocode)
	{
		getModel().setIsocode(isocode);
		return this;
	}
}
