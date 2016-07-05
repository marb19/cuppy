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

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;


public class DummyTrustManager implements X509TrustManager
{
	private static final X509Certificate[] ACCEPTED_ISSUERS = new X509Certificate[0];

	@Override
	public void checkClientTrusted(final X509Certificate[] arg0, final String arg1) throws CertificateException
	{
		return;
	}

	@Override
	public void checkServerTrusted(final X509Certificate[] arg0, final String arg1) throws CertificateException
	{
		return;
	}

	@Override
	public X509Certificate[] getAcceptedIssuers()
	{
		return ACCEPTED_ISSUERS;
	}
}
