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
package de.hybris.platform.sap.sapcarintegration.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

public interface CarConnectionService {

	public abstract HttpURLConnection createConnection(String absoluteUri, String contentType,
			String httpMethod) throws IOException, MalformedURLException;

}