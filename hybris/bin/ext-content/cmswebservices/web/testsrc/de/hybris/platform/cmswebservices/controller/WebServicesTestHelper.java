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
package de.hybris.platform.cmswebservices.controller;

import static java.util.Arrays.asList;
import static java.util.Collections.EMPTY_MAP;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.webservicescommons.errors.converters.AbstractErrorConverter;
import de.hybris.platform.webservicescommons.errors.converters.ExceptionConverter;
import de.hybris.platform.webservicescommons.errors.converters.ValidationErrorConverter;
import de.hybris.platform.webservicescommons.errors.converters.WebserviceExceptionConverter;
import de.hybris.platform.webservicescommons.errors.factory.WebserviceErrorFactory;
import de.hybris.platform.webservicescommons.errors.factory.impl.DefaultWebserviceErrorFactory;
import de.hybris.platform.webservicescommons.jaxb.Jaxb2HttpMessageConverter;
import de.hybris.platform.webservicescommons.jaxb.MoxyJaxbContextFactoryImpl;
import de.hybris.platform.webservicescommons.jaxb.metadata.MetadataSourceFactory;
import de.hybris.platform.webservicescommons.jaxb.metadata.impl.DefaultMetadataNameProvider;
import de.hybris.platform.webservicescommons.jaxb.metadata.impl.DefaultMetadataSourceFactory;
import de.hybris.platform.webservicescommons.mapping.impl.DefaultSubclassRegistry;
import de.hybris.platform.webservicescommons.resolver.RestHandlerExceptionResolver;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


/**
 * @cmsxapi TODO remove after refactoring type restrictions
 */
public class WebServicesTestHelper
{
	public static MockMvc getMockMvcWithRestExceptionHandling(final Object controller)
	{
		return MockMvcBuilders
				.standaloneSetup(controller)
				.setHandlerExceptionResolvers(buildRestHandlerExceptionHandler(EMPTY_MAP))
				.build();
	}

	public static MockMvc getMockMvcWithRestExceptionHandling(final Object controller, final Map<String, String> messagesByKey)
	{
		return MockMvcBuilders
				.standaloneSetup(controller)
				.setHandlerExceptionResolvers(buildRestHandlerExceptionHandler(messagesByKey))
				.build();
	}

	private static RestHandlerExceptionResolver buildRestHandlerExceptionHandler(final Map<String, String> messagesByKey) {
		final RestHandlerExceptionResolver restHandlerExceptionResolver = new RestHandlerExceptionResolver();
		restHandlerExceptionResolver.setWebserviceErrorFactory(buildWebServiceErrorFactory(messagesByKey));
		restHandlerExceptionResolver.setStatusCodeMappings(EMPTY_MAP);
		restHandlerExceptionResolver.setDefaultStatusCode(400);
		restHandlerExceptionResolver.setMessageConverters(new Jaxb2HttpMessageConverter[] { buildMessageConverter() });
		return restHandlerExceptionResolver;
	}

	private static WebserviceErrorFactory buildWebServiceErrorFactory(final Map<String, String> messagesByKey) {
		final DefaultWebserviceErrorFactory webserviceErrorFactory = new DefaultWebserviceErrorFactory();
		final List<AbstractErrorConverter> converters = asList(
				buildWebserviceExceptionConverter(),
				buildValidationErrorConverter(messagesByKey),
				buildExceptionConverter());
		webserviceErrorFactory.setConverters(converters);
		return webserviceErrorFactory;
	}

	private static Jaxb2HttpMessageConverter buildMessageConverter() {
		final Jaxb2HttpMessageConverter messageConverter = new Jaxb2HttpMessageConverter();
		messageConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
		messageConverter.setMarshallerProperties(Collections.singletonMap("eclipselink.media-type", "application/json"));
		messageConverter.setUnmarshallerProperties(Collections.singletonMap("eclipselink.media-type", "application/json"));
		final MoxyJaxbContextFactoryImpl jaxbContextFactory = new MoxyJaxbContextFactoryImpl();
		jaxbContextFactory.setMetadataSourceFactory(buildMetadataSourceFactory());
		jaxbContextFactory.setSubclassRegistry(new DefaultSubclassRegistry());
		messageConverter.setJaxbContextFactory(jaxbContextFactory);
		return messageConverter;
	}

	private static MetadataSourceFactory buildMetadataSourceFactory()
	{
		final DefaultMetadataSourceFactory metadataSourceFactory = new DefaultMetadataSourceFactory();
		metadataSourceFactory.setNameProvider(new DefaultMetadataNameProvider());
		return metadataSourceFactory;
	}

	private static WebserviceExceptionConverter buildWebserviceExceptionConverter() {
		return new WebserviceExceptionConverter();
	}

	private static ValidationErrorConverter buildValidationErrorConverter(final Map<String, String> messagesByKey) {
		final ValidationErrorConverter validationErrorConverter = new ValidationErrorConverter();
		validationErrorConverter.setI18NService(buildI18nServiceMock());
		validationErrorConverter.setMessageSource(buildMessageSourceMock(messagesByKey));
		return validationErrorConverter;
	}

	private static ExceptionConverter buildExceptionConverter() {
		return new ExceptionConverter();
	}

	private static I18NService buildI18nServiceMock() {
		final I18NService i18NService = mock(I18NService.class);
		when(i18NService.getCurrentLocale()).thenReturn(Locale.ENGLISH);
		return i18NService;
	}

	private static MessageSource buildMessageSourceMock(final Map<String, String> messagesByKey) {
		final MessageSource messageSource = mock(MessageSource.class);
		messagesByKey.forEach((key, message) -> {
			when(messageSource.getMessage(key, null, Locale.ENGLISH)).thenReturn(message);
		});
		return messageSource;
	}
}
