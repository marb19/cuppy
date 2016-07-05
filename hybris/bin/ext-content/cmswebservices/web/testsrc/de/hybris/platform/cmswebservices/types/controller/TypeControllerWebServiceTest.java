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
package de.hybris.platform.cmswebservices.types.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cmswebservices.data.ComponentTypeData;
import de.hybris.platform.cmswebservices.data.ComponentTypeListData;
import de.hybris.platform.cmswebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.cmswebservices.util.apiclient.ApiClient;
import de.hybris.platform.cmswebservices.util.apiclient.Response;

import java.util.List;

import org.junit.Test;

@IntegrationTest
public class TypeControllerWebServiceTest extends ApiBaseIntegrationTest
{

	private static final String URI = "/v1/types";

	@Test
	public void getAllTypesTest() throws Exception
	{
		final ApiClient apiClient = getApiClientInstance();
		final Response<ComponentTypeListData> response = apiClient
				.request()
				.endpoint(URI)
				.acceptJson()
				.get(ComponentTypeListData.class);

		//check that we have a result
		assertStatusCode(response, 200);

		// check that we have a body
		assertNotNull(response.getBody());

		//check that we have a couple of entries
		assertTrue(response.getBody().getComponentTypes().size() > 1);

		final List<ComponentTypeData> components = response.getBody().getComponentTypes();
		ComponentTypeData paragraphComponent = new ComponentTypeData();

		for (final ComponentTypeData component : components)
		{
			if(component.getCode().equals("CMSParagraphComponent"))
			{
				paragraphComponent = component;
			}
		}

		// check that it contains the paragraph component
		assertNotNull(paragraphComponent);

	}
}
