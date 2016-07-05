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
package de.hybris.platform.cmswebservices.util.apiclient;

import de.hybris.platform.cmswebservices.constants.CmswebservicesConstants;

import java.util.Arrays;
import java.util.Locale;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.MediaType;


/**
 * CmsWebServices web service tests' class for synchronous client-side HTTP requests. It encapsulates the Request build
 * and provides the interfaces for extension using different technologies, e.g. Spring or JAX-RS.
 *
 */
public class ApiClient
{
	private AbstractApiClientInvocation apiClientInvocation;
	private String authorizationHeader = "";

	public ApiClient(final String generateAuthorizationHeader) {
		setAuthorizationHeader(generateAuthorizationHeader);
	}

	public AbstractApiClientInvocation getApiClientInvocation()
	{
		return apiClientInvocation;
	}

	@Required
	public void setApiClientInvocation(final AbstractApiClientInvocation apiClientInvocation)
	{
		this.apiClientInvocation = apiClientInvocation;
	}

	/**
	 * Create the request builder, with a new instance of ApiClientInvocation
	 * @return the request builder
	 */
	public Builder request()
	{
		final Builder request = new Builder(apiClientInvocation.cloneClientInvocation());
		request.header(CmswebservicesConstants.HEADER_AUTHORIZATION, getAuthorizationHeader());
		return request;
	}

	public String getAuthorizationHeader() {
		return authorizationHeader;
	}

	public void setAuthorizationHeader(final String authorizationHeader) {
		this.authorizationHeader = authorizationHeader;
	}

	/**
	 * RequestBuilder class
	 */
	public class Builder implements ApiClientInvocation {
		private static final String X_CATALOG_HEADER = "X-Catalog";
		private static final String X_CATALOG_VERSION_HEADER = "X-Catalog-Version";

		private final AbstractApiClientInvocation apiClientInvocation;
		private final MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
		private final MultivaluedMap<String, Object> parameters = new MultivaluedHashMap<>();
		private String accept = MediaType.APPLICATION_JSON_VALUE;
		private Locale[] acceptLanguages = new Locale[] {Locale.ENGLISH};
		private Locale[] hybrisLanguages = null;
		private String endpoint;

		public Builder(final AbstractApiClientInvocation apiClient)
		{
			this.apiClientInvocation = apiClient;
		}

		public Builder header(final String key, final String value)
		{
			headers.add(key, value);
			return this;
		}

		public Builder authorizationHeader(final String value)
		{
			headers.replace(CmswebservicesConstants.HEADER_AUTHORIZATION, Arrays.asList(value));
			return this;
		}

		public Builder noAuthorization()
		{
			headers.remove(CmswebservicesConstants.HEADER_AUTHORIZATION);
			return this;
		}

		public Builder parameter(final String key, final String value)
		{
			parameters.add(key, value);
			return this;
		}

		public Builder endpoint(final String endpoint)
		{
			this.endpoint = endpoint;
			return this;
		}

		public Builder accept(final String accept)
		{
			this.accept = accept;
			return this;
		}

		public Builder acceptJson()
		{
			this.accept = MediaType.APPLICATION_JSON_VALUE;
			return this;
		}

		public Builder acceptXml()
		{
			this.accept = MediaType.APPLICATION_XML_VALUE;
			return this;
		}

		public Builder acceptLanguages(final Locale... locale)
		{
			this.acceptLanguages = locale;
			return this;
		}

		public Builder hybrisLanguages(final Locale... locale)
		{
			this.hybrisLanguages = locale;
			return this;
		}

		@Override
		public <T, R> Response<R> get(final Class<T> clazz, final Class<R> errorClazz)
				throws Exception
				{
			populate();
			return this.apiClientInvocation.get(clazz, errorClazz);
				}

		@Override
		public <R> Response<R> get(final Class<R> clazz) throws Exception
		{
			populate();
			return this.apiClientInvocation.get(clazz);
		}

		@Override
		public <T, R> Response<R> put(final T entity, final Class<R> clazz)
				throws Exception
				{
			populate();
			return this.apiClientInvocation.put(entity, clazz);
				}

		@Override
		public <T> Response<Void> put(final T entity) throws Exception
		{
			populate();
			return this.apiClientInvocation.put(entity);
		}

		@Override
		public <T, R> Response<R> post(final T entity, final Class<R> clazz) throws Exception
		{
			populate();
			return this.apiClientInvocation.post(entity, clazz);
		}

		@Override
		public <T> Response<Void> post(final T entity) throws Exception
		{
			populate();
			return this.apiClientInvocation.post(entity);
		}

		@Override
		public Response<Void> delete() throws Exception
		{
			populate();
			return this.apiClientInvocation.delete();
		}

		void populate()
		{
			apiClientInvocation.setEndpoint(this.endpoint);
			apiClientInvocation.setAccept(this.accept);
			apiClientInvocation.setAcceptLanguages(this.acceptLanguages);
			apiClientInvocation.setHybrisLanguages(this.hybrisLanguages);
			apiClientInvocation.setHeaders(this.headers);
			apiClientInvocation.setParameters(this.parameters);
		}
	}

}
