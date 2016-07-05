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
package de.hybris.platform.cmswebservices.enumdata.facade.impl;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * TestEnumModel is a class that implements the
 * <code>HybrisEnumValue</code> interface. The intention
 * is to use this for test purposes to avoid dependencies
 * on other projects.
 *
 */
public enum TestEnumModel implements HybrisEnumValue {
	TEST_ENUM1, TEST_ENUM2;

	@Override
	public String getType()
	{
		return this.getClass().getSimpleName();
	}

	@Override
	public String getCode()
	{
		return this.name();
	}
}
