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
package com.sap.wec.adtreco.targetgroups.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.sap.wec.adtreco.targetgroups.tests.TestInitiativeConfig;
import com.sap.wec.adtreco.targetgroups.tests.TestInitiativeSearch;


@SuppressWarnings("javadoc")
@RunWith(Suite.class)
@SuiteClasses(
{ TestInitiativeSearch.class, TestInitiativeConfig.class })
public class UnitTestSuite
{

}
