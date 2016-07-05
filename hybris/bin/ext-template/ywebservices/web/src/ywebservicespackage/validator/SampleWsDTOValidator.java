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
package ywebservicespackage.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import ywebservicespackage.dto.SampleWsDTO;


public class SampleWsDTOValidator implements Validator
{
	@Override
	public boolean supports(final Class clazz)
	{
		return SampleWsDTO.class.equals(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors)
	{
		ValidationUtils.rejectIfEmpty(errors, "value", "field.required");
	}
}
