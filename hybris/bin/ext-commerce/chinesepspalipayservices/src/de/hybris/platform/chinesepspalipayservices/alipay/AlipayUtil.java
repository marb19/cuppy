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
 *
 */
package de.hybris.platform.chinesepspalipayservices.alipay;

import de.hybris.platform.chinesepspalipayservices.constants.PaymentConstants;
import de.hybris.platform.chinesepspalipayservices.data.HttpRequest;
import de.hybris.platform.chinesepspalipayservices.data.HttpResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.NameValuePair;


/**
 *
 */
public class AlipayUtil
{

	public static String generateUrl(final Map<String, String> sParaTemp, final AlipayConfiguration alipayConfig) throws Exception
	{
		final Map<String, String> sPara = buildRequestPara(sParaTemp, alipayConfig.getWebKey(), alipayConfig.getSignType());
		final StringBuffer strResult = new StringBuffer();
		strResult.append(alipayConfig.getWebGateway());
		strResult.append(createLinkString(sPara));
		return strResult.toString();
	}

	/**
	 * Simulate the HTTP POST request, use this to get the XML response from Alipay
	 *
	 * @param sParaTemp
	 *           Request Parameters
	 * @param alipayConfig
	 * @return XML response returned by Alipay
	 */
	public static String postRequest(final Map<String, String> sParaTemp, final AlipayConfiguration alipayConfig)
	{
		final Map<String, String> sPara = buildRequestPara(sParaTemp, alipayConfig.getWebKey(), alipayConfig.getSignType());
		final HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance(alipayConfig.getTrustGateway());
		final HttpRequest request = new HttpRequest();
		request.setCharset(PaymentConstants.Basic.INPUT_CHARSET);
		request.setParameters(generatNameValuePairList(sPara));
		request.setUrl(alipayConfig.getWebGateway() + "_input_charset=" + PaymentConstants.Basic.INPUT_CHARSET);
		request.setMethod(PaymentConstants.HTTP.METHOD_POST);
		final HttpResponse response = httpProtocolHandler.execute(request);
		if (response == null)
		{
			return null;
		}
		final String strResult = response.getStringResult();
		return strResult;
	}


	public static Map<String, String> paraFilter(final Map<String, String> sArray)
	{
		final Map<String, String> result = new HashMap<String, String>();

		if (sArray == null || sArray.size() <= 0)
		{
			return result;
		}

		for (final String key : sArray.keySet())
		{
			final String value = sArray.get(key);
			if (value == null || value.equals("") || key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("sign_type"))
			{
				continue;
			}
			result.put(key, value);
		}

		return result;
	}

	public static String buildMysign(final Map<String, String> sArray, final String key, final String signType)
	{
		String prestr = createLinkString(sArray);
		prestr = prestr + key;
		final String mysign = encrypt(signType, prestr);
		return mysign;
	}


	protected static Map<String, String> buildRequestPara(final Map<String, String> sParaTemp, final String key,
			final String signType)
	{
		final Map<String, String> sPara = paraFilter(sParaTemp);
		final String mysign = buildMysign(sPara, key, signType);
		sPara.put("sign", mysign);
		sPara.put("sign_type", signType);
		return sPara;
	}



	public static String encrypt(final String SignType, final String preStr)
	{
		if (SignType.equalsIgnoreCase("MD5"))
		{
			return DigestUtils.md5Hex(preStr);
		}
		else if (SignType.equalsIgnoreCase("DSA"))
		{
			//TODO
		}
		else if (SignType.equalsIgnoreCase("RSA"))
		{
			//TODO
		}
		return "";

	}



	public static String createLinkString(final Map<String, String> params)
	{
		final List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		String prestr = "";
		for (int i = 0; i < keys.size(); i++)
		{
			final String key = keys.get(i);
			final String value = params.get(key);
			if (i == keys.size() - 1)
			{
				prestr = prestr + key + "=" + value;
			}
			else
			{
				prestr = prestr + key + "=" + value + "&";
			}
		}

		return prestr;
	}


	protected static List<NameValuePair> generatNameValuePairList(final Map<String, String> properties)
	{

		final List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();


		for (final Map.Entry<String, String> entry : properties.entrySet())
		{

			nameValuePairList.add(new NameValuePair(entry.getKey(), entry.getValue()));
		}

		return nameValuePairList;
	}



}
