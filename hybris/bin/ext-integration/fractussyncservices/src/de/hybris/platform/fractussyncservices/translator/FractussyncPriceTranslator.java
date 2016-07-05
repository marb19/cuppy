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
package de.hybris.platform.fractussyncservices.translator;

import de.hybris.platform.impex.jalo.translators.AbstractValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import org.apache.commons.lang.StringUtils;


public class FractussyncPriceTranslator extends AbstractValueTranslator
{
	@Override
	public Object importValue(String s, Item item) throws JaloInvalidParameterException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String exportValue(Object o) throws JaloInvalidParameterException
	{
		return StringUtils.remove(o.toString(), ",");
	}
}
