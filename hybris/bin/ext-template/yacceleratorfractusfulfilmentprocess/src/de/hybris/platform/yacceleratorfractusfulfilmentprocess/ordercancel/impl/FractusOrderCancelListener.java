/*
 *
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
package de.hybris.platform.yacceleratorfractusfulfilmentprocess.ordercancel.impl;

import de.hybris.platform.basecommerce.enums.OrderModificationEntryStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.FractusSyncExecutionService;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus.YaasOrderStatusService;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus.exception.NoMatchingYaasStatusException;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus.exception.UnknownEcpStatusException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;

import java.text.MessageFormat;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * The listener to sync the order status.
 */
public class FractusOrderCancelListener extends AbstractEventListener<FractusOrderCancelEvent>
{

	private FractusSyncExecutionService syncExecutionService;
	private YaasOrderStatusService yaasOrderStatusService;
	private ModelService modelService;
	private static final Logger LOG = Logger.getLogger(FractusOrderCancelListener.class);
	private int sleepTimer;
	private String syncExecutionId;

	@Override
	protected void onEvent(final FractusOrderCancelEvent event)
	{
		validate(event);

		OrderCancelRecordEntryModel cancelRecord = event.getOrderCancelRecordEntryModel();
		OrderModel order = event.getOrderModel();

		try
		{
			while (true)
			{
				Thread.sleep(getSleepTimer());

				getModelService().refresh(cancelRecord);
				getModelService().refresh(order);

				if (!cancelRecord.getModificationRecord().isInProgress())
				{
					if (OrderModificationEntryStatus.SUCCESSFULL.equals(cancelRecord.getStatus())
							&& OrderStatus.CANCELLED.equals(order.getStatus()))
					{
						String yaasStatus = getYaasOrderStatusService().determineYaasStatus(order);

						final String message = "Determined new order status for Order : {0}, ECP status : {1}, YaaS Status : {2}";
						LOG.debug(MessageFormat.format(message, order.getCode(), order.getStatus().getCode(), yaasStatus));

						order.setYaasOrderStatus(yaasStatus);
						getModelService().save(order);

						getSyncExecutionService().synchOrderStatus(order.getCode(), getSyncExecutionId());
					}
					else if (OrderModificationEntryStatus.FAILED.equals(cancelRecord.getStatus()))
					{
						LOG.warn("Cancel order " + order.getCode() + " failed.");
					}

					break;
				}
			}
		}
		catch (final InterruptedException ex)
		{
			LOG.warn(ex.getMessage(), ex);
		}
		catch (final UnknownEcpStatusException e)
		{
			final String errorMessage = "Unknown ECP Status found for order {0} with status of {1}";
			LOG.error(MessageFormat.format(errorMessage, order.getCode(), order.getStatus().getCode()));
		}
		catch (final NoMatchingYaasStatusException e)
		{
			final String warnMessage = "Unknown Yaas Status found for order {0} with status of {1}";
			LOG.warn(MessageFormat.format(warnMessage, order.getCode(), order.getStatus().getCode()));
		}
	}

	protected boolean validate(final FractusOrderCancelEvent yaasOrderCancelEvent)
	{
		validateParameterNotNull(yaasOrderCancelEvent, "event cannot be null");
		validateParameterNotNull(yaasOrderCancelEvent.getOrderCancelRecordEntryModel(), "order cancel record entry cannot be null");
		validateParameterNotNull(yaasOrderCancelEvent.getOrderModel(), "order model be null");
		validateParameterNotNull(yaasOrderCancelEvent.getOrderCancelRecordEntryModel().getModificationRecord(),
				"OrderCancelRecordEntryModel.ModificationRecord cannot be null");

		return true;
	}

	protected FractusSyncExecutionService getSyncExecutionService()
	{
		return syncExecutionService;
	}

	@Required
	public void setSyncExecutionService(FractusSyncExecutionService syncExecutionService)
	{
		this.syncExecutionService = syncExecutionService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected String getSyncExecutionId()
	{
		return syncExecutionId;
	}

	@Required
	public void setSyncExecutionId(String syncExecutionId)
	{
		this.syncExecutionId = syncExecutionId;
	}

	protected YaasOrderStatusService getYaasOrderStatusService()
	{
		return yaasOrderStatusService;
	}

	@Required
	public void setYaasOrderStatusService(YaasOrderStatusService yaasOrderStatusService)
	{
		this.yaasOrderStatusService = yaasOrderStatusService;
	}

	protected int getSleepTimer()
	{
		return sleepTimer;
	}

	@Required
	public void setSleepTimer(int sleepTimer)
	{
		this.sleepTimer = sleepTimer;
	}
}
