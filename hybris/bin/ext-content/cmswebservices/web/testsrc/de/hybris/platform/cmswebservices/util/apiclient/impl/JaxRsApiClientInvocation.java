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
package de.hybris.platform.cmswebservices.util.apiclient.impl;

import de.hybris.platform.cmswebservices.constants.CmswebservicesConstants;
import de.hybris.platform.cmswebservices.util.apiclient.AbstractApiClientInvocation;
import de.hybris.platform.webservicescommons.jaxb.Jaxb2HttpMessageConverter;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


/**
 * Basic ApiClient implementation using JAX-RS technology. {@link Deprecated} use {@link SpringApiClientInvocation}
 * instead
 */
public class JaxRsApiClientInvocation extends AbstractApiClientInvocation
{

	private Jaxb2HttpMessageConverter messageConverter;
	private WebTarget webResource;

	@Override
	public <T, R> de.hybris.platform.cmswebservices.util.apiclient.Response<R> get(final Class<T> clazz, final Class<R> errorClazz)
			throws Exception
	{
		final WebTarget target = getWebTarget();
		final Response response = target.request()
				.accept(getAccept())
				.headers(getFinalHeaders())
				.get();
		response.bufferEntity();

		return response(unmarshallResult(response, errorClazz), response.getStatus(), toFlatMap(response.getHeaders()),
				unmarshallResult(response, errorClazz));

	}

	@Override
	public <T> de.hybris.platform.cmswebservices.util.apiclient.Response<T> get(final Class<T> clazz) throws JAXBException
	{
		final WebTarget target = getWebTarget();
		final Response response = target.request()
				.accept(getAccept())
				.headers(getFinalHeaders())
				.get();
		response.bufferEntity();
		final T object = unmarshallResult(response, clazz);
		return response(object, response.getStatus(), toFlatMap(response.getHeaders()), object);
	}

	@Override
	public <T> de.hybris.platform.cmswebservices.util.apiclient.Response<Void> put(final T entity) throws Exception
	{
		final WebTarget target = getWebTarget();
		final Response response = target.request()
				.accept(getAccept())
				.headers(getFinalHeaders())
				.put(Entity.json(marshallEntity(entity)));
		response.bufferEntity();
		return response(null, response.getStatus(), null, null);
	}

	@Override
	public <T, R> de.hybris.platform.cmswebservices.util.apiclient.Response<R> put(final T entity, final Class<R> clazz) throws Exception
	{
		final WebTarget target = getWebTarget();
		final Response response = target.request().accept(getAccept())
				.headers(getFinalHeaders()).put(Entity.json(marshallEntity(entity)));
		response.bufferEntity();
		final R object = unmarshallResult(response, clazz);
		return response(object, response.getStatus(), toFlatMap(response.getHeaders()), object);
	}

	@Override
	public <T, R> de.hybris.platform.cmswebservices.util.apiclient.Response<R> post(final T entity, final Class<R> clazz) throws Exception
	{
		final WebTarget target = getWebTarget();
		final Response response = target.request()
				.accept(getAccept())
				.headers(getFinalHeaders()).post(Entity.json(marshallEntity(entity)));
		response.bufferEntity();

		final R object = unmarshallResult(response, clazz);
		return response(object, response.getStatus(), toFlatMap(response.getHeaders()), object);
	}

	@Override
	public <T> de.hybris.platform.cmswebservices.util.apiclient.Response<Void> post(final T entity) throws Exception
	{
		final WebTarget target = getWebTarget();
		final Response response = target.request()
				.accept(getAccept()).headers(getFinalHeaders())
				.post(Entity.json(marshallEntity(entity)));
		response.bufferEntity();

		return response(null, response.getStatus(), toFlatMap(response.getHeaders()), null);
	}

	@Override
	public de.hybris.platform.cmswebservices.util.apiclient.Response<Void> delete() throws Exception
	{
		final WebTarget target = getWebTarget();
		final Response response = target.request()
				.accept(getAccept())
				.headers(getFinalHeaders())
				.delete();
		response.bufferEntity();
		return response(null, response.getStatus(), toFlatMap(response.getHeaders()), null);
	}


	/**
	 *
	 * @param headers
	 * @return
	 */
	private Map<String, String> toFlatMap(final MultivaluedMap<String, Object> headers)
	{
		return headers.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().toString()));
	}


	private MultivaluedMap<String, Object> getFinalHeaders()
	{
		MultivaluedMap<String, Object> map = new MultivaluedHashMap<>();

		map.put("Accept", (Arrays.asList(MediaType.valueOf(getAccept()))));
		map.put(HttpHeaders.ACCEPT_LANGUAGE, null);
		map.put(CmswebservicesConstants.HYBRIS_LANGUAGES, null);
		if (!ArrayUtils.isEmpty(getAcceptLanguages()))
		{
			map.put(HttpHeaders.ACCEPT_LANGUAGE,
					Arrays.asList(Arrays.stream(getAcceptLanguages())
							.map(locale -> locale.getLanguage())
							.collect(Collectors.joining(",")))
			);
		}
		if (!ArrayUtils.isEmpty(getHybrisLanguages()))
		{
			map.put(CmswebservicesConstants.HYBRIS_LANGUAGES,
					Arrays.asList(Arrays.stream(getHybrisLanguages())
							.map(locale -> locale.getLanguage())
							.collect(Collectors.joining(",")))
			);
		}
		getHeaders().entrySet().stream().forEach(e -> {
			map.put(e.getKey(), (List) e.getValue());
		});
		return map;
	}

	/**
	 * Unmarshall a response into a DTO.
	 *
	 * @param response
	 *           - the WS response
	 * @param clazz
	 *           - the DTO class type into which to unmarshall the response
	 * @return the unmarshalled DTO
	 * @throws JAXBException
	 *            - when an error occurs during the unmarshalling process
	 */
	private <T> T unmarshallResult(final Response response, final Class<T> clazz) throws JAXBException
	{
		final Unmarshaller unmarshaller = messageConverter.createUnmarshaller(clazz);

		final StreamSource source = new StreamSource(response.readEntity(InputStream.class));
		return unmarshaller.unmarshal(source, clazz).getValue();
	}

	private <T> String marshallEntity(T entity) throws JAXBException
	{
		final Marshaller marshaller = messageConverter.createMarshaller(entity.getClass());
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		marshaller.marshal(entity, byteArrayOutputStream);
		return byteArrayOutputStream.toString();
	}

	private WebTarget getWebTarget()
	{
		WebTarget target = webResource.path(getEndpoint());
		final Set<Map.Entry<String, List<Object>>> entries = getParameters().entrySet();
		for (Map.Entry<String, List<Object>> entry : entries)
		{
			List<Object> values = entry.getValue();
			final String[] strings = values.toArray(new String[values.size()]);
			target = target.queryParam(entry.getKey(), strings);
		}
		return target;
	}

	public void setMessageConverter(final Jaxb2HttpMessageConverter messageConverter)
	{
		this.messageConverter = messageConverter;
	}

	public void setWebResource(final WebTarget webResource)
	{
		this.webResource = webResource;
	}


	@Override
	public AbstractApiClientInvocation cloneClientInvocation()
	{
		final JaxRsApiClientInvocation jaxRsApiClient = new JaxRsApiClientInvocation();
		jaxRsApiClient.setMessageConverter(this.messageConverter);
		jaxRsApiClient.setWebResource(this.webResource);
		return jaxRsApiClient;
	}
}
