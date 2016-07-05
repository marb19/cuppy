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

package de.hybris.platform.fractussyncservices.translator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.ordersplitting.jalo.Consignment;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class FractusConsignmentOrderStatusTranslatorTest
{
	@InjectMocks
	private FractusConsignmentOrderStatusTranslator translator;
	@Mock
	private ModelService modelService;
	@Mock
	private Consignment consignment;
	@Mock
	private ConsignmentModel consignmentModel;
	@Mock
	private OrderModel orderModel;

	@Before
	public void setup()
	{
		translator = new FractusConsignmentOrderStatusTranslator();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldReturnYaasOrderStatusShipped() throws ImpExException
	{
		Mockito.when(modelService.get(consignment)).thenReturn(consignmentModel);
		Mockito.when(consignmentModel.getOrder()).thenReturn(orderModel);
		Mockito.when(orderModel.getYaasOrderStatus()).thenReturn("SHIPPED");

		String returnVal = translator.performExport(consignment);

		Mockito.verify(modelService, Mockito.times(1)).get(consignment);

		Assert.assertEquals("SHIPPED", returnVal);
	}

	@Test
	public void shouldReturnEmptyIfNoOrderFound() throws ImpExException
	{
		Mockito.when(modelService.get(consignment)).thenReturn(consignmentModel);
		Mockito.when(consignmentModel.getOrder()).thenReturn(null);

		String returnVal = translator.performExport(consignment);

		Mockito.verify(modelService, Mockito.times(1)).get(consignment);

		Assert.assertEquals(StringUtils.EMPTY, returnVal);
	}
}
