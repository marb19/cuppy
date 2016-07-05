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
package de.hybris.platform.chinesepspalipayservices.strategies;

import de.hybris.platform.chinesepspalipayservices.data.AlipayCancelPaymentRequestData;
import de.hybris.platform.chinesepspalipayservices.data.AlipayDirectPayRequestData;
import de.hybris.platform.chinesepspalipayservices.data.AlipayPaymentStatusRequestData;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRawCancelPaymentResult;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRawPaymentStatus;


public interface AlipayCreateRequestStrategy
{
	/**
	 * Create direct_pay_url from requestData
	 *
	 * @param requestData
	 *           Direct_Pay request data needed by alipay {@link AlipayDirectPayRequestData}
	 * @return Created url by the requestData
	 * @throws Exception
	 */
	public String createDirectPayUrl(final AlipayDirectPayRequestData requestData) throws Exception;

	/**
	 *
	 * Send check request with POST method
	 *
	 * @param checkRequest
	 *           Check request data needed by alipay
	 * @return Check status {@link AlipayRawPaymentStatus}
	 * @throws Exception
	 */
	public AlipayRawPaymentStatus submitPaymentStatusRequest(final AlipayPaymentStatusRequestData checkRequest) throws Exception;

	/**
	 *
	 * Send close request with POST method
	 *
	 * @param closeRequest
	 *           Close request data needed by alipay
	 * @return The result of close request {@link AlipayRawCancelPaymentResult}
	 * @throws Exception
	 */
	public AlipayRawCancelPaymentResult submitCancelPaymentRequest(final AlipayCancelPaymentRequestData closeRequest)
			throws Exception;
}
