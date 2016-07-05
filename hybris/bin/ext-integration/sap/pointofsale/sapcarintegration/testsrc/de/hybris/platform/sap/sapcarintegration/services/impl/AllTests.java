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
package de.hybris.platform.sap.sapcarintegration.services.impl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import de.hybris.bootstrap.annotations.UnitTest;

@UnitTest
@RunWith(Suite.class)
@SuiteClasses(
{ 
	DefaultCarDataProviderServiceTest.class, 
	DefaultCarOrderHistoryExtractorServiceTest.class,
	DefaultCarOrderHistoryServiceTest.class,
	DefaultMultichannelDataProviderServiceTest.class, 
	DefaultMultichannelOrderHistoryExtractorServiceTest.class,
	DefaultMultichannelOrderHistoryServiceTest.class
})
public class AllTests
{

}
