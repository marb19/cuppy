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

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.smarteditwebservices.configuration.SmarteditConfigurationModelFactory;
import de.hybris.platform.smarteditwebservices.data.ConfigurationData;
import de.hybris.platform.smarteditwebservices.model.SmarteditConfigurationModel;

import org.junit.Test;

@UnitTest
public class SmarteditConfigurationModelToDataPopulatorTest
{

	private SmarteditConfigurationModelToDataPopulator populator = new SmarteditConfigurationModelToDataPopulator();
	private final SmarteditConfigurationModel model = SmarteditConfigurationModelFactory.modelBuilder("1", "KEY", "VALUE");

	@Test
	public void populateNonEmptyData()
	{
		final ConfigurationData data = new ConfigurationData();
		populator.populate(model, data);
		assertThat(model.getKey(), is(data.getKey()));
		assertThat(model.getValue(), is(data.getValue()));
	}
}
