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
package de.hybris.platform.yacceleratorfractusfulfilmentprocess.cronjob;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.FractusOrderService;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * This cronjob checkes for YaaS orders ready for fulfilment and starts fulfilment process for them.
 */
public class FractusOrderFulfilmentInitiateCronJob extends AbstractJobPerformable<CronJobModel>
{
	private static final Logger LOG = Logger.getLogger(FractusOrderFulfilmentInitiateCronJob.class);

	private FractusOrderService fractusOrderService;

	protected FractusOrderService getFractusOrderService()
	{
		return fractusOrderService;
	}

	@Required
	public void setFractusOrderService(final FractusOrderService fractusOrderService)
	{
		this.fractusOrderService = fractusOrderService;
	}

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		final List<OrderModel> ordersForFulfillment = fractusOrderService.getOrdersForFulfilment();
		if (CollectionUtils.isNotEmpty(ordersForFulfillment))
		{
			for (final OrderModel order : ordersForFulfillment)
			{
				getFractusOrderService().startFulfilmentProcess(order);
			}
		}
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

}
