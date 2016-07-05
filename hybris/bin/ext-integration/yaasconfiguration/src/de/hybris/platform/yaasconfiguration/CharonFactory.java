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
package de.hybris.platform.yaasconfiguration;

import static com.google.common.base.Preconditions.checkArgument;
import static com.hybris.charon.utils.CharonUtils.join;

import de.hybris.platform.yaasconfiguration.service.YaasConfigurationService;

import com.hybris.charon.Charon;
import com.hybris.charon.CharonBuilder;
import com.hybris.charon.conf.CombinePropertyResolver;
import com.hybris.charon.conf.PropertyResolver;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Required;


public class CharonFactory
{
	private final ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<>();
	private PropertyResolver resolver;
	private YaasConfigurationService yaasConfigurationService;

	/**
	 * returns a cached instance of http client.
	 * the clientType must be an interface which represents the charon client.
	 * the related configuration will lookup to YassConfigurationModel
	 * the only properties that are loaded from the model are clientId and clientSecret
	 *
	 * @param appId
	 * @param clientType
	 * @param <T>
	 * @return
	 */
	public <T> T client(String appId, Class<T> clientType)
	{
		return client(appId, clientType, builder -> builder.build());
	}

	/**
	 * returns a cached instance of http client.
	 * the clientType must be an interface which represents the charon client.
	 * the related configuration will lookup to YassConfigurationModel
	 * the only properties that are loaded from the model are clientId and clientSecret
	 *
	 * @param appId
	 * @param clientType
	 * @param builder client builder modifier
	 * @param <T>
	 * @return
	 */
	public <T> T client(String appId, Class<T> clientType, Function<CharonBuilder<T>, T> builder)
	{
		checkArgument(appId != null, "appId must not be null");
		checkArgument(clientType != null, "clientType must not be null");
		checkArgument(builder != null, "builder must not be null");
		return (T) cache.computeIfAbsent(join(appId, clientType.getName()),//
				k -> builder.apply(Charon.from(clientType)
						.config(new CombinePropertyResolver(new ApplicationPropertyResolver(yaasConfigurationService, appId), resolver))));
	}

	/**
	 * remove all clients from the cache
	 */
	public void clearCache()
	{
		cache.clear();
	}

	@Required
	public void setResolver(final PropertyResolver resolver)
	{
		this.resolver = resolver;
	}

	@Required
	public void setYaasConfigurationService(final YaasConfigurationService yaasConfigurationService)
	{
		this.yaasConfigurationService = yaasConfigurationService;
	}
}
