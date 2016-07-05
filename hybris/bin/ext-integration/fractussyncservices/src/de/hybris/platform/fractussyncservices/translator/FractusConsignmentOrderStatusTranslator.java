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

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.SpecialColumnDescriptor;
import de.hybris.platform.impex.jalo.translators.AbstractSpecialValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.ordersplitting.jalo.Consignment;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


public class FractusConsignmentOrderStatusTranslator extends AbstractSpecialValueTranslator
{
	private ModelService modelService;
	private static final Logger LOG = Logger.getLogger(FractusConsignmentOrderStatusTranslator.class);

	@Override
	public void init(SpecialColumnDescriptor columnDescriptor) throws HeaderValidationException
	{
		modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
	}

	@Override
	public String performExport(Item item) throws ImpExException
	{
		if (item instanceof Consignment)
		{
			ConsignmentModel consignmentModel = getModelService().get(item);
			if (consignmentModel != null && consignmentModel.getOrder() instanceof OrderModel)
			{
				return ((OrderModel) consignmentModel.getOrder()).getYaasOrderStatus();
			}
			else
			{
				if (consignmentModel == null || consignmentModel.getOrder() == null)
				{
					LOG.warn("No consignmentModel or orderModel found.");
				}
				else
				{
					LOG.warn("Require the order model but found " + consignmentModel.getOrder().getClass().getSimpleName());
				}
			}
		}

		return StringUtils.EMPTY;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}
}
