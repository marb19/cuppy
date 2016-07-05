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
package de.hybris.platform.smarteditwebservices.validation.facade;

/**
 * Thrown when the {@link FacadeValidationService} identifies validation errors.
 */
public class ValidationException extends RuntimeException
{
	private static final long serialVersionUID = 5922002536003254842L;

	protected Object validationObject;

	public ValidationException(final Object validationObject)
	{
		super("Validation error");
		this.validationObject = validationObject;
	}

	public Object getValidationObject()
	{
		return validationObject;
	}
}
