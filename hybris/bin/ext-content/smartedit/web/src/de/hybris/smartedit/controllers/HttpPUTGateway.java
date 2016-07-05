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

import java.util.Map;

import org.springframework.messaging.handler.annotation.Header;

public interface HttpPUTGateway {
	public String update(Map<String, String> payload, @Header("id") String id, @Header("Authorization") String token);
}
