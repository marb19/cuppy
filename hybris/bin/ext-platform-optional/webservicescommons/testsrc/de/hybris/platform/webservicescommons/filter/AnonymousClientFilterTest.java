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
package de.hybris.platform.webservicescommons.filter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.webservicescommons.constants.WebservicescommonsConstants;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;


@UnitTest
public class AnonymousClientFilterTest
{
	private static final String CLIENT_ID = "client_id";
	private static final String CLIENT_SECRET = "client_secret";

	private static final String GRANT_TYPE = "grant_type";

	private static final String PASSWORD_GT = "password";
	private static final String REFRESH_GT = "refresh_token";
	private static final String CLIENT_GT = "client_credentials";


	private static final String VALID_PATH = "/oauth/token";
	private static final String INVALID_PATH = "/some/path";



	private AnonymousClientFilter filter;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private FilterChain chain;
	private Map<String, String[]> parameters;


	ArgumentCaptor<ServletRequest> requestCaptor;


	@Before
	public void setUp() throws IOException, ServletException
	{

		//setup client details and client details service
		final BaseClientDetails clientDetails = new BaseClientDetails();
		clientDetails.setClientId(WebservicescommonsConstants.ANONYMOUS_CLIENT_ID);


		final ClientDetailsService clientDetailsService = mock(ClientDetailsService.class);
		when(clientDetailsService.loadClientByClientId(WebservicescommonsConstants.ANONYMOUS_CLIENT_ID)).thenReturn(clientDetails);

		//setup filter
		filter = new AnonymousClientFilter();


		response = mock(HttpServletResponse.class);
		request = mock(HttpServletRequest.class);
		when(request.getContextPath()).thenReturn("");

		chain = mock(FilterChain.class);
		requestCaptor = ArgumentCaptor.forClass(ServletRequest.class);

		parameters = new HashMap<String, String[]>();
	}

	private void setupRequest(final String path)
	{
		when(request.getRequestURI()).thenReturn(path);
		when(request.getParameterMap()).thenReturn(parameters);
		when(request.getParameterNames()).thenReturn(Collections.enumeration(parameters.keySet()));
		parameters.forEach(this::setupRequestParameters);
	}

	private void setupRequestParameters(final String key, final String[] value)
	{
		when(request.getParameter(key)).thenReturn(value[0]);
		when(request.getParameterValues(key)).thenReturn(value);
	}

	private void addParameter(final String key, final String... value)
	{
		parameters.put(key, value);
	}

	private ServletRequest getActualServletRequest() throws IOException, ServletException
	{
		verify(chain).doFilter(requestCaptor.capture(), any());
		return requestCaptor.getValue();
	}

	@Test
	public void positivePathTest() throws IOException, ServletException
	{
		//given
		addParameter(GRANT_TYPE, PASSWORD_GT);
		setupRequest(VALID_PATH);
		filter.setEnabled(true);

		//when
		filter.doFilter(request, response, chain);

		//then
		final ServletRequest actual = getActualServletRequest();
		assertEquals("Invalid client id", WebservicescommonsConstants.ANONYMOUS_CLIENT_ID, actual.getParameter(CLIENT_ID));
	}

	@Test
	public void positivePathTokenTest() throws IOException, ServletException
	{
		//given
		addParameter(GRANT_TYPE, REFRESH_GT);
		setupRequest(VALID_PATH);
		filter.setEnabled(true);

		//when
		filter.doFilter(request, response, chain);

		//then
		final ServletRequest actual = getActualServletRequest();
		assertEquals("Invalid client id", WebservicescommonsConstants.ANONYMOUS_CLIENT_ID, actual.getParameter(CLIENT_ID));
	}

	@Test
	public void filterEnableTest() throws IOException, ServletException
	{
		//given
		addParameter(GRANT_TYPE, PASSWORD_GT);
		setupRequest(VALID_PATH);
		filter.setEnabled(false);

		//when
		filter.doFilter(request, response, chain);

		//then
		final ServletRequest actual = getActualServletRequest();
		assertEquals("Invalid client id", null, actual.getParameter(CLIENT_ID));
		assertEquals("Invalid client secret", null, actual.getParameter(CLIENT_SECRET));
	}

	@Test
	public void invalidGrantTypeTest() throws IOException, ServletException
	{
		//given
		addParameter(GRANT_TYPE, CLIENT_GT);
		setupRequest(VALID_PATH);
		filter.setEnabled(true);

		//when
		filter.doFilter(request, response, chain);

		//then
		final ServletRequest actual = getActualServletRequest();
		assertEquals("Invalid client id", null, actual.getParameter(CLIENT_ID));
		assertEquals("Invalid client secret", null, actual.getParameter(CLIENT_SECRET));
	}

	@Test
	public void invalidPathTest() throws IOException, ServletException
	{
		//given
		addParameter(GRANT_TYPE, PASSWORD_GT);
		setupRequest(INVALID_PATH);
		filter.setEnabled(true);

		//when
		filter.doFilter(request, response, chain);

		//then
		final ServletRequest actual = getActualServletRequest();
		assertEquals("Invalid client id", null, actual.getParameter(CLIENT_ID));
		assertEquals("Invalid client secret", null, actual.getParameter(CLIENT_SECRET));
	}

	@Test
	public void clientIdParameterTest() throws IOException, ServletException
	{
		//given
		final String clientId = "aaa";

		addParameter(GRANT_TYPE, PASSWORD_GT);
		addParameter(CLIENT_ID, clientId);
		setupRequest(VALID_PATH);
		filter.setEnabled(true);

		//when
		filter.doFilter(request, response, chain);

		//then
		final ServletRequest actual = getActualServletRequest();
		assertEquals("Invalid client id", clientId, actual.getParameter(CLIENT_ID));
		assertEquals("Invalid client secret", null, actual.getParameter(CLIENT_SECRET));
	}

	@Test
	public void clientSecretParameterTest() throws IOException, ServletException
	{
		//given
		final String clientSecret = "bbb";

		addParameter(GRANT_TYPE, PASSWORD_GT);
		addParameter(CLIENT_SECRET, clientSecret);
		setupRequest(VALID_PATH);
		filter.setEnabled(true);

		//when
		filter.doFilter(request, response, chain);

		//then
		final ServletRequest actual = getActualServletRequest();
		assertEquals("Invalid client id", null, actual.getParameter(CLIENT_ID));
		assertEquals("Invalid client secret", clientSecret, actual.getParameter(CLIENT_SECRET));
	}

	@Test
	public void positivePathWithOverrideTest() throws IOException, ServletException
	{
		//given
		final String clientId = "aaa";
		final String clientSecret = "bbb";

		addParameter(GRANT_TYPE, PASSWORD_GT);
		addParameter(CLIENT_ID, clientId);
		addParameter(CLIENT_SECRET, clientSecret);
		setupRequest(VALID_PATH);
		filter.setEnabled(true);

		//when
		filter.doFilter(request, response, chain);

		//then
		final ServletRequest actual = getActualServletRequest();
		assertEquals("Invalid client id", clientId, actual.getParameter(CLIENT_ID));
		assertEquals("Invalid client secret", clientSecret, actual.getParameter(CLIENT_SECRET));
	}

	@Test
	public void positivePathRefreshWithOverrideTest() throws IOException, ServletException
	{
		//given
		final String clientId = "aaa";
		final String clientSecret = "bbb";

		addParameter(GRANT_TYPE, REFRESH_GT);
		addParameter(CLIENT_ID, clientId);
		addParameter(CLIENT_SECRET, clientSecret);
		setupRequest(VALID_PATH);
		filter.setEnabled(true);

		//when
		filter.doFilter(request, response, chain);

		//then
		final ServletRequest actual = getActualServletRequest();
		assertEquals("Invalid client id", clientId, actual.getParameter(CLIENT_ID));
		assertEquals("Invalid client secret", clientSecret, actual.getParameter(CLIENT_SECRET));
	}

	@Test
	public void filterEnableWithOverrideTest() throws IOException, ServletException
	{
		//given
		final String clientId = "aaa";
		final String clientSecret = "bbb";

		addParameter(GRANT_TYPE, PASSWORD_GT);
		addParameter(CLIENT_ID, clientId);
		addParameter(CLIENT_SECRET, clientSecret);
		setupRequest(VALID_PATH);
		filter.setEnabled(false);

		//when
		filter.doFilter(request, response, chain);

		//then
		final ServletRequest actual = getActualServletRequest();
		assertEquals("Invalid client id", clientId, actual.getParameter(CLIENT_ID));
		assertEquals("Invalid client secret", clientSecret, actual.getParameter(CLIENT_SECRET));
	}

	@Test
	public void invalidGrantTypeWithOverrideTest() throws IOException, ServletException
	{
		//given
		final String clientId = "aaa";
		final String clientSecret = "bbb";

		addParameter(GRANT_TYPE, CLIENT_GT);
		addParameter(CLIENT_ID, clientId);
		addParameter(CLIENT_SECRET, clientSecret);
		setupRequest(VALID_PATH);
		filter.setEnabled(true);

		//when
		filter.doFilter(request, response, chain);

		//then
		final ServletRequest actual = getActualServletRequest();
		assertEquals("Invalid client id", clientId, actual.getParameter(CLIENT_ID));
		assertEquals("Invalid client secret", clientSecret, actual.getParameter(CLIENT_SECRET));
	}

	@Test
	public void invalidPathWithOverrideTest() throws IOException, ServletException
	{
		//given
		final String clientId = "aaa";
		final String clientSecret = "bbb";

		addParameter(GRANT_TYPE, PASSWORD_GT);
		addParameter(CLIENT_ID, clientId);
		addParameter(CLIENT_SECRET, clientSecret);
		setupRequest(INVALID_PATH);
		filter.setEnabled(true);

		//when
		filter.doFilter(request, response, chain);

		//then
		final ServletRequest actual = getActualServletRequest();
		assertEquals("Invalid client id", clientId, actual.getParameter(CLIENT_ID));
		assertEquals("Invalid client secret", clientSecret, actual.getParameter(CLIENT_SECRET));
	}
}
