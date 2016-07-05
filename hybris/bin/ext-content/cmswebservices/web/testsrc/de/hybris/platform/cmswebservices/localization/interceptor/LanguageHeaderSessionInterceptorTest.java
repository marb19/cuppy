/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package de.hybris.platform.cmswebservices.localization.interceptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anySet;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmswebservices.constants.CmswebservicesConstants;
import de.hybris.platform.cmswebservices.exception.InvalidAcceptLanguagePreConditionFailedException;
import de.hybris.platform.cmswebservices.exception.LanguageConflictPreConditionFailedException;
import de.hybris.platform.cmswebservices.localization.interceptor.LanguageHeaderSessionInterceptor;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class LanguageHeaderSessionInterceptorTest
{

	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Mock
	private Object handler;
	@Mock
	private ModelAndView modelAndView;
	@Mock
	private SessionService sessionService;

	@InjectMocks
	private final LanguageHeaderSessionInterceptor interceptor = new LanguageHeaderSessionInterceptor();

	@Before
	public void init()
	{
		when(request.getMethod()).thenReturn("GET");
	}

	@Test
	public void testEmptyLanguagesDoNothing() throws Exception
	{
		interceptor.preHandle(request, response, handler);
		verifyZeroInteractions(sessionService);
	}

	@Test
	public void testSingleAcceptLanguagesPreHandle() throws Exception
	{
		when(request.getHeader(CmswebservicesConstants.ACCEPT_LANGUAGES)).thenReturn("en");
		interceptor.preHandle(request, response, handler);
		verify(sessionService).setAttribute(anyString(), anySet());
	}

	@Test
	public void testSingleHybrisLanguagesPreHandle() throws Exception
	{
		when(request.getHeader(CmswebservicesConstants.HYBRIS_LANGUAGES)).thenReturn("en");
		interceptor.preHandle(request, response, handler);
		verify(sessionService).setAttribute(anyString(), anySet());
	}

	@Test(expected = LanguageConflictPreConditionFailedException.class)
	public void testSingleAcceptLanguagesAndSingleHybrisLanguages() throws Exception
	{
		when(request.getHeader(CmswebservicesConstants.ACCEPT_LANGUAGES)).thenReturn("en");
		when(request.getHeader(CmswebservicesConstants.HYBRIS_LANGUAGES)).thenReturn("en");
		interceptor.preHandle(request, response, handler);
	}

	@Test(expected = InvalidAcceptLanguagePreConditionFailedException.class)
	public void testPostWithMultipleAcceptLanguage() throws Exception
	{
		when(request.getHeader(CmswebservicesConstants.ACCEPT_LANGUAGES)).thenReturn("en, fr");
		when(request.getMethod()).thenReturn("POST");
		interceptor.preHandle(request, response, handler);
	}

	@Test
	public void testParseLanguagesEmpty()
	{
		final Set<Locale> locales = interceptor.parseLanguages(null);
		assertNull(locales);
	}

	@Test
	public void testParseLanguagesEmptyString()
	{
		final Set<Locale> locales = interceptor.parseLanguages("");
		assertNull(locales);
	}

	@Test
	public void testParseLanguagesSingleLanguage()
	{
		final Set<Locale> locales = interceptor.parseLanguages("en");
		assertNotNull(locales);
		assertEquals(1, locales.size());
		assertEquals(Locale.ENGLISH, locales.stream().findFirst().orElse(null));
	}

	@Test
	public void testParseLanguagesMultipleLanguages()
	{
		final Set<Locale> locales = interceptor.parseLanguages("en, fr, de");
		assertNotNull(locales);
		assertEquals(3, locales.size());
		assertEquals(Locale.ENGLISH, locales.stream().findFirst().orElse(null));
		assertEquals(Locale.FRENCH, locales.stream().skip(1).findFirst().orElse(null));
		assertEquals(Locale.GERMAN, locales.stream().skip(2).findFirst().orElse(null));
	}

	@Test
	public void testParseLanguagesAlienLanguageWillBeAcceptedAnyways()
	{
		final Set<Locale> locales = interceptor.parseLanguages("alien");
		assertNotNull(locales);
		assertEquals(1, locales.size());
	}
}
