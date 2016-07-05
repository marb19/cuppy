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
 *
 */
package de.hybris.platform.chineseprofile.forms.validation;

import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.AddressValidator;
import de.hybris.platform.chineseprofile.forms.ChineseAddressForm;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;


/**
 * Validator for Chinese address form
 */
@Component("chineseAddressValidator")
public class ChineseAddressValidator extends AddressValidator
{
	private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
	private static final String[] IGNORE_FIELDS = new String[]
	{ "firstName", "lastName", "townCity", "postcode" }; // ignore the validation annotations defined in AddressForm

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return ChineseAddressForm.class.isAssignableFrom(aClass);
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		final ChineseAddressForm addressForm = (ChineseAddressForm) object;
		for (final ConstraintViolation<ChineseAddressForm> e : VALIDATOR.validate(addressForm))
		{
			if (!ArrayUtils.contains(IGNORE_FIELDS, e.getPropertyPath().toString()))
			{
				final String msgKey = StringUtils.substringBetween(e.getMessage(), "{", "}");
				errors.rejectValue(e.getPropertyPath().toString(), msgKey);
			}
		}
	}
}
