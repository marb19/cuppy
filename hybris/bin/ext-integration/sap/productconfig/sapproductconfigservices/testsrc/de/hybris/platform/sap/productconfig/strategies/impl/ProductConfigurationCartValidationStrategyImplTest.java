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
package de.hybris.platform.sap.productconfig.strategies.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.productconfig.runtime.interf.KBKey;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.ConfigModelImpl;
import de.hybris.platform.sap.productconfig.services.SessionAccessService;
import de.hybris.platform.sap.productconfig.services.intf.ProductConfigurationService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Unit tests
 */
@UnitTest
public class ProductConfigurationCartValidationStrategyImplTest
{
	ProductConfigurationCartValidationStrategyImpl classUnderTest = new ProductConfigurationCartValidationStrategyImpl();

	@Mock
	SessionAccessService sessionAccessService;

	@Mock
	ProductConfigurationService productConfigurationService;

	@Mock
	private CartModel cartModel;

	@Mock
	private CartEntryModel cartEntryModel;

	@Mock
	private ProductModel productModel;

	private ConfigModel configModel;

	/**
	 * Before each test
	 */
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		classUnderTest.setSessionAccessService(sessionAccessService);
		classUnderTest.setProductConfigurationService(productConfigurationService);
		Mockito.when(cartEntryModel.getProduct()).thenReturn(productModel);
		Mockito.when(cartEntryModel.getPk()).thenReturn(PK.fromLong(1));
		configModel = new ConfigModelImpl();
		Mockito.when(productConfigurationService.createConfigurationFromExternal((KBKey) Mockito.any(), (String) Mockito.any()))
				.thenReturn(configModel);
	}

	/**
	 * Session access service
	 */
	@Test
	public void testSessionAccessService()
	{
		assertNotNull(classUnderTest.getSessionAccessService());
	}

	/**
	 * Configuration
	 */
	@Test
	public void testConfigurationService()
	{
		assertNotNull(classUnderTest.getProductConfigurationService());
	}

	/**
	 * No external configuration attached to cart entry
	 */
	@Test
	public void testValidateNoExtCFG()
	{
		assertNull(classUnderTest.validateConfiguration(cartEntryModel));
	}

	/**
	 * Configuration is not complete
	 */
	@Test
	public void testValidateNotComplete()
	{
		Mockito.when(cartEntryModel.getExternalConfiguration()).thenReturn("X");
		final CommerceCartModification modification = classUnderTest.validateConfiguration(cartEntryModel);
		assertNotNull(modification);
		assertEquals(ProductConfigurationCartValidationStrategyImpl.REVIEW_CONFIGURATION, modification.getStatusCode());
	}

	/**
	 * Configuration is complete and consistent-> No validation message
	 */
	@Test
	public void testValidateCompleteAndConsistent()
	{
		Mockito.when(cartEntryModel.getExternalConfiguration()).thenReturn("X");
		configModel.setComplete(true);
		configModel.setConsistent(true);
		final CommerceCartModification modification = classUnderTest.validateConfiguration(cartEntryModel);
		assertNull(modification);
	}

	/**
	 * Configuration is not consistent but complete: validation message
	 */
	@Test
	public void testValidateCompleteNotConsistent()
	{
		Mockito.when(cartEntryModel.getExternalConfiguration()).thenReturn("X");
		configModel.setComplete(true);
		configModel.setConsistent(false);
		final CommerceCartModification modification = classUnderTest.validateConfiguration(cartEntryModel);
		assertNotNull(modification);
		assertEquals(ProductConfigurationCartValidationStrategyImpl.REVIEW_CONFIGURATION, modification.getStatusCode());
	}
}
