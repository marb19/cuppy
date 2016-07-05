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
package de.hybris.platform.smarteditwebservices.configuration.populator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.smarteditwebservices.data.ConfigurationData;
import de.hybris.platform.smarteditwebservices.model.SmarteditConfigurationModel;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Test;


@UnitTest
public class SmarteditConfigurationDataToModelPopulatorTest
{

	private SmarteditConfigurationDataToModelPopulator populator = new SmarteditConfigurationDataToModelPopulator();
	@Test
	public void populateNonEmptyData()
	{
		final ConfigurationData data = new ConfigurationData();
		data.setKey("KEY");
		data.setValue("VALUE");
		final SmarteditConfigurationModel model = new SmarteditConfigurationModel();
		populator.populate(data, model);
		assertThat(data.getKey(), is(model.getKey()));
		assertThat(data.getValue(), is(model.getValue()));
	}
}
