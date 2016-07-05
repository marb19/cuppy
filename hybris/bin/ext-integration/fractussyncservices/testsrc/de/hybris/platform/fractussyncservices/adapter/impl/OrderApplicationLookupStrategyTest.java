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
 */

package de.hybris.platform.fractussyncservices.adapter.impl;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Sets;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.yaasconfiguration.model.YaasApplicationModel;
import de.hybris.platform.yaasconfiguration.model.YaasProjectModel;


@UnitTest
public class OrderApplicationLookupStrategyTest
{
	@InjectMocks
	private OrderApplicationLookupStrategy orderApplicationLookupStrategy;
	@Mock
	private ModelService modelService;
	@Mock
	private Order order;
	@Mock
	private OrderModel orderModel;
	@Mock
	private BaseSiteModel baseSiteModel;
	@Mock
	private YaasProjectModel yaasProjectModel;
	@Mock
	private YaasApplicationModel yaasApplicationModel;
	private final String applicationID = "id";
	@Mock
	private Item notOrder;


	@Before
	public void setup()
	{
		orderApplicationLookupStrategy = new OrderApplicationLookupStrategy();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldReturnLookupValue()
	{
		String expectedVal = applicationID;

		Mockito.when(modelService.get(order)).thenReturn(orderModel);
		Mockito.when(orderModel.getSite()).thenReturn(baseSiteModel);
		Mockito.when(baseSiteModel.getYaasProjects()).thenReturn(Sets.newHashSet(yaasProjectModel));
		Mockito.when(yaasProjectModel.getYaasApplications()).thenReturn(Sets.newHashSet(yaasApplicationModel));
		Mockito.when(yaasApplicationModel.getIdentifier()).thenReturn(applicationID);

		String lookupVal = orderApplicationLookupStrategy.lookup(order);
		Assert.assertEquals(expectedVal, lookupVal);
	}

	@Test
	public void shouldReturnLookupEmptyIfNoYaasApplications()
	{
		String expectedVal = StringUtils.EMPTY;

		Mockito.when(modelService.get(order)).thenReturn(orderModel);
		Mockito.when(orderModel.getSite()).thenReturn(baseSiteModel);
		Mockito.when(baseSiteModel.getYaasProjects()).thenReturn(Sets.newHashSet(yaasProjectModel));
		Mockito.when(yaasProjectModel.getYaasApplications()).thenReturn(Sets.newHashSet());


		String lookupVal = orderApplicationLookupStrategy.lookup(order);
		Assert.assertEquals(expectedVal, lookupVal);
	}

	@Test
	public void shouldReturnLookupEmptyIfItemNotMatch()
	{
		String expectedVal = StringUtils.EMPTY;

		String lookupVal = orderApplicationLookupStrategy.lookup(notOrder);
		Assert.assertEquals(expectedVal, lookupVal);
	}

	@Test
	public void shouldReturnProductModelTypeCode()
	{
		Assert.assertEquals(OrderModel._TYPECODE, orderApplicationLookupStrategy.getTypeCode());
	}
}
