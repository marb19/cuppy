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
package de.hybris.platform.smarteditwebservices.validation.factory.impl;

import de.hybris.platform.smarteditwebservices.validation.factory.ErrorFactory;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

/**
 * Default implementation of {@link ErrorFactory}
 */
public class DefaultErrorFactory implements ErrorFactory
{

	@Override
	public Errors createInstance(final Object object)
	{
		return new BeanPropertyBindingResult(object, object.getClass().getSimpleName());
	}
}
