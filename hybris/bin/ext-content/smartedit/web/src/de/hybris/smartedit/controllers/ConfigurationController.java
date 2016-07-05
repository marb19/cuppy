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
package de.hybris.smartedit.controllers;

import de.hybris.platform.smartedit.dto.ConfigurationData;
import de.hybris.platform.smartedit.dto.ConfigurationDataListWsDto;
import de.hybris.smartedit.security.IsAuthorizedCmsManager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("configurationController")
@Component
@RequestMapping("/configuration")
public class ConfigurationController {

	private static final String HEADER_AUTHORIZATION = "Authorization";

	private final HttpGETGateway httpGETGateway;
	private final HttpPOSTGateway httpPOSTGateway;
	private final HttpPUTGateway httpPUTGateway;
	private final HttpDELETEGateway httpDELETEGateway;

	private final ObjectMapper mapper = new ObjectMapper();

	private static final String KEY = "key";
	private static final String VALUE = "value";
	private static final String ID = "id";
	private static final String SECURED = "secured";

	@Autowired
	public ConfigurationController(HttpGETGateway httpGETGateway,HttpPOSTGateway httpPOSTGateway,HttpPUTGateway httpPUTGateway,HttpDELETEGateway httpDELETEGateway)
	{
		this.httpGETGateway = httpGETGateway;
		this.httpPOSTGateway = httpPOSTGateway;
		this.httpPUTGateway = httpPUTGateway;
		this.httpDELETEGateway = httpDELETEGateway;
	}

	@RequestMapping(value = "", method=RequestMethod.GET)
	@ResponseBody
    @IsAuthorizedCmsManager
	public Collection<ConfigurationData> getConfiguration(HttpServletRequest request)
			throws IOException, IllegalAccessException, InvocationTargetException
	{
		final String data = httpGETGateway.loadAll("", request.getHeader(HEADER_AUTHORIZATION));
		final ConfigurationDataListWsDto configurations = mapper.readValue(data, ConfigurationDataListWsDto.class);
		return configurations.getConfigurations();
	}

	@RequestMapping(value = "", method=RequestMethod.POST)
	@ResponseBody
    @IsAuthorizedCmsManager
	public ConfigurationData saveConfiguration(@RequestBody Map<String, String> payload, HttpServletRequest request)
			throws IOException, IllegalAccessException, InvocationTargetException
	{
		payload.remove(SECURED);
		final String stringPayload = httpPOSTGateway.save(payload, request.getHeader(HEADER_AUTHORIZATION));
		return mapper.readValue(stringPayload, ConfigurationData.class);
	}

	@RequestMapping(value = "/{key:.+}", method=RequestMethod.PUT)
	@ResponseBody
    @IsAuthorizedCmsManager
	public ConfigurationData updateConfiguration(@RequestBody Map<String, String> payload, @PathVariable("key") String id,
			HttpServletRequest request) throws IOException, IllegalAccessException, InvocationTargetException
	{
		final Optional<ConfigurationData> optional = getConfiguration(request).stream().filter((configurationData) -> {
			return (configurationData.getKey().equals(id));
		}).findFirst();

		if (optional.isPresent())
		{
			final String stringPayload = httpPUTGateway.update(payload, id, request.getHeader(HEADER_AUTHORIZATION));
			return mapper.readValue(stringPayload, ConfigurationData.class);
		}
		else
		{
			return null;
		}
	}

	@RequestMapping(value = "/{key:.+}", method=RequestMethod.DELETE)
	@ResponseBody
    @IsAuthorizedCmsManager
	public void deleteConfiguration(@PathVariable("key") String id, HttpServletRequest request)
			throws IOException, IllegalAccessException, InvocationTargetException
	{
		final Map<String, String> configuration = new HashMap<>();
		getConfiguration(request).stream().forEach((configurationData) -> {
			if(configurationData.getKey().equals(id)) {
		    		configuration.put(KEY, configurationData.getKey());
		    		configuration.put(VALUE, configurationData.getValue());
		    	}
		});

		httpDELETEGateway.delete(configuration, id, request.getHeader(HEADER_AUTHORIZATION));

	}

}
