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
package de.hybris.platform.sap.productconfig.frontend.util.impl;

import de.hybris.platform.sap.productconfig.frontend.validator.ConflictError;
import de.hybris.platform.sap.productconfig.frontend.validator.MandatoryFieldError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;


public class ErrorResolver
{
	private static final String CONFLICT_HEADER_GROUP_PATH = "groups[0]";

	public static List<ErrorMessage> getConflictErrorsWithoutDuplicates(final BindingResult bindResult)
	{
		final List<FieldError> containsDuplicates = getFieldErrors(bindResult.getFieldErrors(),
				ErrorResolver::isConfigErrorInConflictHeader);

		final List<FieldError> errorList = removeDuplicateFieldErrors(containsDuplicates);

		return errorList.stream().map(ErrorResolver::createErrorMessageForConflictType).collect(Collectors.toList());
	}

	public static List<ErrorMessage> getConflictErrors(final BindingResult bindResult)
	{
		return getErrorMessages(bindResult.getFieldErrors(), ErrorResolver::isConfigError,
				ErrorResolver::createErrorMessageForConflictType);
	}

	public static List<ErrorMessage> getMandatoryFieldErrors(final BindingResult bindResult)
	{
		return getErrorMessages(bindResult.getFieldErrors(), ErrorResolver::isMandatoryFieldError,
				ErrorResolver::createErrorMessageMandatoryFieldType);
	}

	public static List<ErrorMessage> getWarnings(final BindingResult bindResult)
	{
		final List<ErrorMessage> messages = new ArrayList<>();
		messages.addAll(getConflictErrors(bindResult));
		messages.addAll(getMandatoryFieldErrors(bindResult));
		return messages;
	}

	public static List<ErrorMessage> getValidationErrors(final BindingResult bindResult)
	{
		return getErrorMessages(bindResult.getFieldErrors(), ErrorResolver::isErrorMessage,
				ErrorResolver::createErrorMessageForErrorType);
	}

	public static List<ErrorMessage> getValidationErrorsForCstic(final BindingResult bindResult, final String path)
	{
		return getErrorMessages(bindResult.getFieldErrors(path), ErrorResolver::isErrorMessage,
				ErrorResolver::createErrorMessageForErrorType);
	}

	public static boolean hasErrorMessages(final BindingResult bindResult)
	{
		return !bindResult.getFieldErrors().stream().noneMatch(ErrorResolver::isErrorMessage);
	}

	public static List<ErrorMessage> getErrorsForGroup(final BindingResult bindResult, final String path)
	{
		final Predicate<FieldError> isErrorForGroup = f -> isCorrectGroup(path, f) && isErrorMessage(f);

		return getErrorMessages(bindResult.getFieldErrors(), isErrorForGroup, ErrorResolver::createErrorMessageForErrorType);
	}

	public static List<ErrorMessage> getWarningsForCstic(final BindingResult bindResult, final String path)
	{
		return getErrorMessages(bindResult.getFieldErrors(path), ErrorResolver::isWarningMessage,
				ErrorResolver::createErrorMessageBasedOnType);
	}

	public static List<ErrorMessage> getWarningsForGroup(final BindingResult bindResult, final String path)
	{
		final Predicate<FieldError> isErrorForGroup = f -> isCorrectGroup(path, f) && isWarningMessage(f);

		return getErrorMessages(bindResult.getFieldErrors(), isErrorForGroup, ErrorResolver::createErrorMessageBasedOnType);
	}

	/* #### */

	private static boolean isWarningMessage(final FieldError objError)
	{
		return objError instanceof ConflictError || objError instanceof MandatoryFieldError;
	}

	private static boolean isErrorMessage(final FieldError objError)
	{
		return !isWarningMessage(objError);
	}

	private static ErrorMessage createErrorMessageBasedOnType(final FieldError objError)
	{
		ErrorMessage errorMessage;
		if (objError instanceof MandatoryFieldError)
		{
			errorMessage = createErrorMessageMandatoryFieldType(objError);
		}
		else
		{
			errorMessage = createErrorMessageForConflictType(objError);

		}
		return errorMessage;
	}

	private static ErrorMessage createErrorMessageMandatoryFieldType(final FieldError objError)
	{
		return createErrorMessageForType(objError, ErrorType.MANDATORY_FIELD);
	}

	private static ErrorMessage createErrorMessageForConflictType(final FieldError objError)
	{
		return createErrorMessageForType(objError, ErrorType.CONFLICT);
	}

	private static ErrorMessage createErrorMessageForErrorType(final FieldError objError)
	{
		return createErrorMessageForType(objError, ErrorType.ERROR);
	}

	private static ErrorMessage createErrorMessageForType(final FieldError objError, final ErrorType type)
	{
		final String path = objError.getField();
		final String defaultErrorMessage = objError.getDefaultMessage();
		final String code;
		final Object[] args;
		if (objError instanceof MandatoryFieldError)
		{
			code = objError.getCodes()[0];
			args = new Object[]
			{ ((MandatoryFieldError) objError).getCstic().getLangdepname() };
		}
		else
		{
			code = objError.getCode();
			args = objError.getArguments();
		}

		final ErrorMessage errorMessage = new ErrorMessage();

		errorMessage.setPath(path);
		errorMessage.setMessage(defaultErrorMessage);
		errorMessage.setCode(code);
		errorMessage.setArgs(args);
		errorMessage.setType(type);

		return errorMessage;
	}

	private static List<FieldError> removeDuplicateFieldErrors(final List<FieldError> containsDuplicates)
	{
		final HashMap<String, FieldError> hashmap = new HashMap<>();
		for (final FieldError fieldError : containsDuplicates)
		{
			final String[] groupStructure = fieldError.getField().split("\\.");
			final String key = groupStructure[0] + "." + groupStructure[1];
			hashmap.put(key, fieldError);
		}

		return new ArrayList(hashmap.values());
	}

	private static List<ErrorMessage> getErrorMessages(final List<FieldError> errorList, final Predicate<FieldError> filter,
			final Function<FieldError, ErrorMessage> mapper)
	{
		return errorList.stream().filter(filter).map(mapper).collect(Collectors.toList());
	}

	private static List<FieldError> getFieldErrors(final List<FieldError> errorList, final Predicate<FieldError> filter)
	{
		return errorList.stream().filter(filter).collect(Collectors.toList());
	}

	private static boolean isMandatoryFieldError(final FieldError error)
	{
		return error instanceof MandatoryFieldError;
	}

	private static boolean isConfigError(final FieldError error)
	{
		return error instanceof ConflictError;
	}

	private static boolean isConfigErrorInConflictHeader(final FieldError error)
	{
		return isConfigError(error) && (error.getField().startsWith(CONFLICT_HEADER_GROUP_PATH));
	}

	private static boolean isCorrectGroup(final String path, final FieldError objError)
	{
		final String fieldName = objError.getField();

		if (!fieldName.startsWith(path))
		{
			return false;
		}

		if (fieldName.contains("subGroups"))
		{
			final int lastSubGroup = fieldName.lastIndexOf("subGroups") + 9;
			if (path.length() < lastSubGroup)
			{
				return false;
			}

			if (fieldName.substring(0, lastSubGroup).equals(path.substring(0, lastSubGroup)))
			{
				return true;
			}

			return false;
		}

		return true;
	}
}
