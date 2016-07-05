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
import static org.junit.Assert.assertNotNull;

import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.webservicescommons.testsupport.client.WsRequestBuilder;
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import net.sf.json.JSONObject;


@NeedsEmbeddedServer(webExtensions = "oauthauthorizationserver")
public class OAuthAnonymousClientTest extends ServicelayerTest
{
	private static final String URI = "oauth/token";
	private static final Logger LOG = Logger.getLogger(OAuthAnonymousClientTest.class);
	private static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
	private static final String GRANT_TYPE_PASSWORD = "password";
	private static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";
	private static final String GRANT_TYPE_AUTHORIZATION = "authorization_code";
	private static final String CUSTOMER_USERNAME = "testoauthcustomer";
	private static final String CUSTOMER_PASSWORD = "1234";
	//private static final String OAUTH_CLIENT_ID = "mobile_android";
	//private static final String OAUTH_CLIENT_PASSWORD = "secret";

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
	public void testGetToken()
	{
		final Response result = wsRequestBuilder.queryParam("grant_type", GRANT_TYPE_PASSWORD)
				.queryParam("username", CUSTOMER_USERNAME).queryParam("password", CUSTOMER_PASSWORD).build()
				.post(Entity.entity(null, MediaType.APPLICATION_JSON));
		result.bufferEntity();
		assertOk(result, false);
		final String output = result.readEntity(String.class);
		LOG.debug("output: " + output);
		final JSONObject response = JSONObject.fromObject(output);
		assertNotNull("Expected refresh token in the response but got null", response.get("refresh_token"));
		assertNotNull("Expected access token in the response but got null", response.get("access_token"));
		assertNotNull("Expected token type in the response but got null", response.get("token_type"));
		assertNotNull("Expected expiry time in the response but got null", response.get("expires_in"));
	}

	@Test
	public void testRefreshToken()
	{
		final Response result = wsRequestBuilder//
				.queryParam("grant_type", GRANT_TYPE_PASSWORD)//
				.queryParam("username", CUSTOMER_USERNAME)//
				.queryParam("password", CUSTOMER_PASSWORD).build()//
				.post(Entity.entity(null, MediaType.APPLICATION_JSON));
		result.bufferEntity();
		assertOk(result, false);
		final String output = result.readEntity(String.class);
		LOG.debug("output: " + output);
		final JSONObject response = JSONObject.fromObject(output);
		final String refreshToken = response.get("refresh_token").toString();
		LOG.debug("token: " + refreshToken);

		final Response refreshResult = wsRequestBuilder.queryParam("grant_type", GRANT_TYPE_REFRESH_TOKEN)
				.queryParam("refresh_token", refreshToken).build().post(Entity.entity(null, MediaType.APPLICATION_JSON));
		result.bufferEntity();
		assertOk(refreshResult, false);
		final String newOutput = result.readEntity(String.class);
		final JSONObject newResponse = JSONObject.fromObject(newOutput);
		final String newRefreshToken = newResponse.get("refresh_token").toString();
		if (refreshToken.equals(newRefreshToken))
		{
			LOG.info("new refresh token is the same as old one");
		}
		else
		{
			LOG.info("new refresh token is different than the old one");
		}

	}

	@Test
	public void testGetTokenWithClientCredentialsGrant()
	{
		final Response result = wsRequestBuilder//
				.queryParam("grant_type", GRANT_TYPE_CLIENT_CREDENTIALS).build()//
				.post(Entity.entity(null, MediaType.APPLICATION_JSON));
		result.bufferEntity();
		assertUnauthorized(result, false);
	}


	@Test
	public void testGetTokenWithAuthorizationGrant()
	{
		final Response result = wsRequestBuilder//
				.queryParam("authorization_code", "fakeCode")//
				.queryParam("grant_type", GRANT_TYPE_AUTHORIZATION).build()//
				.post(Entity.entity(null, MediaType.APPLICATION_JSON));
		result.bufferEntity();
		assertUnauthorized(result, false);
	}


	@Test
	public void testRefreshTokenUsingDifferentClient()
	{
		//obtain tokens anonymously
		final Response result = wsRequestBuilder//
				.queryParam("grant_type", GRANT_TYPE_PASSWORD)//
				.queryParam("username", CUSTOMER_USERNAME)//
				.queryParam("password", CUSTOMER_PASSWORD).build()//
				.post(Entity.entity(null, MediaType.APPLICATION_JSON));
		result.bufferEntity();
		assertOk(result, false);
		final String output = result.readEntity(String.class);
		LOG.debug("output: " + output);
		final JSONObject response = JSONObject.fromObject(output);
		final String refreshToken = response.get("refresh_token").toString();
		LOG.debug("token: " + refreshToken);
		//refresh token passing a client ID different than the one used to obtain it
		final Response refreshResult = wsRequestBuilder//
				.queryParam("grant_type", GRANT_TYPE_REFRESH_TOKEN)//
				.queryParam("refresh_token", refreshToken)//
				.queryParam("client_id", "mobile_android")//
				.queryParam("client_secret", "secret").build()//
				.post(Entity.entity(null, MediaType.APPLICATION_JSON));
		result.bufferEntity();
		assertBadRequest(refreshResult, false);
	}

	@Test
	public void testGetTokenWithEmptyClientParam()
	{
		final Response result = wsRequestBuilder//
				.queryParam("grant_type", GRANT_TYPE_PASSWORD)//
				.queryParam("username", CUSTOMER_USERNAME)//
				.queryParam("password", CUSTOMER_PASSWORD)//
				.queryParam("client_id", "")//
				.queryParam("client_secret", "").build()//
				.post(Entity.entity(null, MediaType.APPLICATION_JSON));
		result.bufferEntity();
		assertUnauthorized(result, false);
	}

}