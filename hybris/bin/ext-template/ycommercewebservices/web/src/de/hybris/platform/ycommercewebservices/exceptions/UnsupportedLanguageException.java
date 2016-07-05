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
package de.hybris.platform.ycommercewebservices.exceptions;

import de.hybris.platform.core.model.c2l.LanguageModel;

import javax.servlet.ServletException;


/**
 *
 *
 */
public class UnsupportedLanguageException extends ServletException
{

	private LanguageModel language;

	/**
	 * @param languageToSet
	 */
	public UnsupportedLanguageException(final LanguageModel languageToSet)
	{
		super("Language " + languageToSet + " is not supported by the current base store");
		this.language = languageToSet;
	}

	public UnsupportedLanguageException(final LanguageModel languageToSet, final Throwable rootCause)
	{
		super("Language " + languageToSet + " is not supported by the current base store", rootCause);
		this.language = languageToSet;
	}

	/**
	 * @param msg
	 */
	public UnsupportedLanguageException(final String msg)
	{
		super(msg);
	}

	public UnsupportedLanguageException(final String msg, final Throwable rootCause)
	{
		super(msg, rootCause);
	}

	/**
	 * @return the language
	 */
	public LanguageModel getLanguage()
	{
		return language;
	}
}
