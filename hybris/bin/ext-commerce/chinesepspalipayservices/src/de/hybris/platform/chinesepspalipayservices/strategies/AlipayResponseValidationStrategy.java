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

import java.util.Map;



public interface AlipayResponseValidationStrategy
{
	/**
	 * Validate response map is response from Alipay.Return true if response is correct
	 *
	 * @param params
	 *           Alipay request map
	 * @return boolean
	 */
	public boolean validateResponse(final Map<String, String> params);
}
