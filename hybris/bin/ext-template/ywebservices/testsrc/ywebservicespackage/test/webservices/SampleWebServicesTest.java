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
package ywebservicespackage.test.webservices;

import static de.hybris.platform.webservicescommons.testsupport.client.WebservicesAssert.assertBadRequest;
import static de.hybris.platform.webservicescommons.testsupport.client.WebservicesAssert.assertOk;
import static de.hybris.platform.webservicescommons.testsupport.client.WebservicesAssert.assertUnauthorized;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;
import de.hybris.platform.webservicescommons.testsupport.client.WsRequestBuilder;
import de.hybris.platform.webservicescommons.testsupport.client.WsSecuredRequestBuilder;
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;

import ywebservicespackage.constants.YWebServicesConstants;
import ywebservicespackage.dto.SampleWsDTO;


@NeedsEmbeddedServer(webExtensions =
{ YWebServicesConstants.EXTENSIONNAME, "oauthauthorizationserver" })
@IntegrationTest
public class SampleWebServicesTest extends ServicelayerTest
{
	public static final String OAUTH_CLIENT_ID = "mobile_android";
	public static final String OAUTH_CLIENT_PASS = "secret";

	private static final String MAP_URI = "sample/map";
	private static final String URI = "sample/users";

	private WsRequestBuilder wsRequestBuilder;
	private WsSecuredRequestBuilder wsSecuredRequestBuilder;

	@Before
	public void setUp() throws Exception
	{
		wsRequestBuilder = new WsRequestBuilder()//
				.extensionName(YWebServicesConstants.EXTENSIONNAME);

		wsSecuredRequestBuilder = new WsSecuredRequestBuilder()//
				.extensionName(YWebServicesConstants.EXTENSIONNAME)//
				.client(OAUTH_CLIENT_ID, OAUTH_CLIENT_PASS)//
				.grantClientCredentials();

		createCoreData();
		createDefaultUsers();
		importCsv("/ywebservices/test/democustomer-data.impex", "utf-8");
	}

	@Test
	public void testGetSampleUsersWithoutAuthorization()
	{
		final Response result = wsRequestBuilder//
				.path(URI)//
				.build()//
				.accept(MediaType.APPLICATION_XML)//
				.get();
		result.bufferEntity();
		assertUnauthorized(result, false);
	}

	@Test
	public void testGetSampleUserUsingClientCredentials()
	{
		final Response result = wsSecuredRequestBuilder//
				.path(URI)//
				.path("user1")//
				.build()//
				.accept(MediaType.APPLICATION_XML)//
				.get();
		result.bufferEntity();
		assertOk(result, false);
	}

	@Test
	public void testPostSampleDTO()
	{
		final SampleWsDTO sampleWSDTO = new SampleWsDTO();
		sampleWSDTO.setValue("123");
		final SampleWsDTO respSampleWSDTO = wsSecuredRequestBuilder//
				.path("sample/dto")//
				.build()//
				.post(Entity.entity(sampleWSDTO, MediaType.APPLICATION_JSON), SampleWsDTO.class);
		assertNotNull(respSampleWSDTO);
		assertEquals(respSampleWSDTO.getValue(), "123");
	}

	@Test
	public void testPostEmptySampleDTO()
	{
		final SampleWsDTO sampleWSDTO = new SampleWsDTO();
		final Response response = wsSecuredRequestBuilder.path("sample/dto").build()
				.post(Entity.entity(sampleWSDTO, MediaType.APPLICATION_JSON));
		assertBadRequest(response, false);
		final ErrorListWsDTO errors = response.readEntity(ErrorListWsDTO.class);
		assertNotNull(errors);
		assertNotNull(errors.getErrors());
		assertEquals(errors.getErrors().size(), 1);
		final ErrorWsDTO error = errors.getErrors().get(0);
		assertEquals(error.getReason(), "missing");
		assertEquals(error.getSubject(), "value");
		assertEquals(error.getSubjectType(), "parameter");
	}

	@Test
	public void testGetObjectWithMap()
	{
		final Response result = wsSecuredRequestBuilder.path(MAP_URI).build().accept(MediaType.APPLICATION_XML).get();

		assertOk(result, false);
		final String entity = result.readEntity(String.class);
		assertNotNull(entity);
		assertTrue(entity.contains("integerKey"));
		assertTrue(entity.contains("10001"));
		assertTrue(entity.contains("StringKey"));
		assertTrue(entity.contains("StringValue"));
	}
}
