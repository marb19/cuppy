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
package de.hybris.platform.oauthauthorizationserver.test.webservices;

import static de.hybris.platform.webservicescommons.testsupport.client.WebservicesAssert.assertBadRequest;
import static de.hybris.platform.webservicescommons.testsupport.client.WebservicesAssert.assertOk;
import static de.hybris.platform.webservicescommons.testsupport.client.WebservicesAssert.assertUnauthorized;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.webservicescommons.testsupport.client.WsRequestBuilder;
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;


@NeedsEmbeddedServer(webExtensions = "oauthauthorizationserver")
@IntegrationTest
public class OAuthAuthorizationWebServiceTest extends ServicelayerTest
{
	private static final String URI = "oauth/token";

	private static final String GRANT_TYPE_CLIENT_CRIDENTIALS = "client_credentials";
	private static final String GRANT_TYPE_PASSWORD = "password";
	private static final String CLIENT_ID = "mobile_android";
	private static final String CLIENT_SECRET = "secret";
	private static final String CUSTOMER_USERNAME = "testoauthcustomer";
	private static final String CUSTOMER_PASSWORD = "1234";

	private WsRequestBuilder wsRequestBuilder;

	@Before
	public void setUp() throws Exception
	{
		wsRequestBuilder = new WsRequestBuilder().extensionName("oauthauthorizationserver").path(URI);

		createCoreData();
		createDefaultUsers();
		importCsv("/oauthauthorizationserver/test/democustomer-data.impex", "utf-8");
	}

	@Test
	public void testGetTokenUsingClientCredentials()
	{
		final Response result = wsRequestBuilder.queryParam("grant_type", GRANT_TYPE_CLIENT_CRIDENTIALS)
				.queryParam("client_id", CLIENT_ID).queryParam("client_secret", CLIENT_SECRET).build()
				.post(Entity.entity(null, MediaType.APPLICATION_JSON));
		result.bufferEntity();
		assertOk(result, false);
	}

	@Test
	public void testGetTokenUsingPassword()
	{
		final Response result = wsRequestBuilder.queryParam("grant_type", GRANT_TYPE_PASSWORD)
				.queryParam("username", CUSTOMER_USERNAME).queryParam("password", CUSTOMER_PASSWORD)
				.queryParam("client_id", CLIENT_ID).queryParam("client_secret", CLIENT_SECRET).build()
				.post(Entity.entity(null, MediaType.APPLICATION_JSON));
		result.bufferEntity();
		assertOk(result, false);
	}

	@Test
	public void testGetTokenUsingWrongClientID()
	{
		final Response result = wsRequestBuilder.queryParam("grant_type", GRANT_TYPE_CLIENT_CRIDENTIALS)
				.queryParam("client_id", "WRONG").queryParam("client_secret", CLIENT_SECRET).build()
				.post(Entity.entity(null, MediaType.APPLICATION_JSON));
		result.bufferEntity();
		assertUnauthorized(result, false);
		assertTrue(result.hasEntity());
		assertTrue(StringUtils.contains(result.readEntity(String.class), "UnauthorizedError"));
	}

	@Test
	public void testGetTokenUsingWrongClientSecret()
	{
		final Response result = wsRequestBuilder.queryParam("grant_type", GRANT_TYPE_CLIENT_CRIDENTIALS)
				.queryParam("client_id", CLIENT_ID).queryParam("client_secret", "WRONG").build()
				.post(Entity.entity(null, MediaType.APPLICATION_JSON));
		result.bufferEntity();
		assertUnauthorized(result, false);
		assertTrue(result.hasEntity());
		assertTrue(StringUtils.contains(result.readEntity(String.class), "BadClientCredentialsError"));
	}

	@Test
	public void testGetTokenUsingWrongGrantType()
	{
		final Response result = wsRequestBuilder.queryParam("grant_type", "WRONG").queryParam("client_id", CLIENT_ID)
				.queryParam("client_secret", CLIENT_SECRET).build().post(Entity.entity(null, MediaType.APPLICATION_JSON));
		result.bufferEntity();
		assertBadRequest(result, false);
	}
}
