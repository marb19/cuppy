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
package de.hybris.platform.webservicescommons.testsupport.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.ClientResponse;


public class WebservicesAssert
{
	/**
	 * Tests whether the response has status OK. Expects correct status (200), content type of 'application/xml'
	 *
	 * @param response
	 *           {@link ClientResponse}
	 * @param expectEmptyBody
	 *           true - the body is checked for null value
	 */
	public static void assertOk(final Response response, final boolean expectEmptyBody)
	{
		assertResponseStatus(Status.OK, response, expectEmptyBody);
	}

	/**
	 * Tests whether resource was successfully created. Expects correct status (201), content type of 'application/xml'
	 *
	 * @param response
	 *           {@link ClientResponse}
	 * @param expectEmptyBody
	 *           true - the body is checked for null value
	 */
	public static void assertCreated(final Response response, final boolean expectEmptyBody)
	{
		assertResponseStatus(Status.CREATED, response, expectEmptyBody);
	}

	/**
	 * Tests whether the response has status FORBIDDEN. Expects correct status (403), content type of 'application/xml'
	 *
	 * @param response
	 *           {@link ClientResponse}
	 * @param expectEmptyBody
	 *           true - the body is checked for null value
	 */
	public static void assertForbidden(final Response response, final boolean expectEmptyBody)
	{
		assertResponseStatus(Status.FORBIDDEN, response, expectEmptyBody);
	}

	/**
	 * Tests whether the response has status BAD_REQUEST. Expects correct status (400), content type of 'application/xml'
	 *
	 * @param response
	 *           {@link ClientResponse}
	 * @param expectEmptyBody
	 *           true - the body is checked for null value
	 */
	public static void assertBadRequest(final Response response, final boolean expectEmptyBody)
	{
		assertResponseStatus(Status.BAD_REQUEST, response, expectEmptyBody);
	}

	/**
	 * Tests whether the response has status UNAUTHORIZED. Expects correct status (401), content type of
	 * 'application/xml'
	 *
	 * @param response
	 *           {@link ClientResponse}
	 * @param expectEmptyBody
	 *           true - the body is checked for null value
	 */
	public static void assertUnauthorized(final Response response, final boolean expectEmptyBody)
	{
		assertResponseStatus(Status.UNAUTHORIZED, response, expectEmptyBody);
	}

	/**
	 * Tests whether the response status has expected value.
	 *
	 * @param response
	 *           {@link ClientResponse}
	 * @param expectEmptyBody
	 *           true - the body is checked for null value
	 */
	public static void assertResponseStatus(final Status responseStatus, final Response response, final boolean expectEmptyBody)
	{
		assertResponseStatus(responseStatus, response);
		if (expectEmptyBody)
		{
			assertTrue("Body should be empty at response: " + response, !response.hasEntity());
		}
	}

	public static void assertResponseStatus(final Status status, final Response response)
	{
		assertEquals("Wrong HTTP status at response: " + response, status.getStatusCode(), response.getStatus());
	}

}
