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
package de.hybris.platform.yacceleratorordermanagement.impl;

import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.returns.ReturnActionAdapter;
import de.hybris.platform.warehousing.model.ReturnProcessModel;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.processengine.BusinessProcessEvent;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.returns.model.ReturnRequestModel;


/**
 * Specific OMS adapter implementation to request a return approval
 */
public class OmsReturnActionAdapter implements ReturnActionAdapter
{
	protected static final String CONFIRM_OR_CANCEL_REFUND_ACTION_EVENT_NAME = "ConfirmOrCancelRefundEvent";
	protected static final String FAIL_CAPTURE_ACTION_EVENT_NAME = "FailCaptureActionEvent";
	protected static final String APPROVE_CANCEL_GOODS_EVENT_NAME = "ApproveOrCancelGoodsEvent";
	protected static final String WAIT_FOR_FAIL_CAPTURE_ACTION = "waitForFailCaptureAction";
	protected static final String WAIT_FOR_CONFIRM_OR_CANCEL_REFUND_ACTION = "waitForConfirmOrCancelReturnAction";
	protected static final String WAIT_FOR_GOODS_ACTION = "waitForGoodsAction";

	protected static final String APPROVAL_CHOICE = "approveReturn";
	protected static final String ACCEPT_GOODS_CHOICE = "acceptGoods";
	protected static final String CANCEL_REFUND_CHOICE = "cancelReturn";

	private BusinessProcessService businessProcessService;

	@Override
	public void requestReturnApproval(ReturnRequestModel returnRequest)
	{
		getBusinessProcessService().triggerEvent(
				BusinessProcessEvent
						.builder(
								returnRequest.getReturnProcess().iterator().next().getCode() + "_"
										+ CONFIRM_OR_CANCEL_REFUND_ACTION_EVENT_NAME).withChoice(APPROVAL_CHOICE)
						.withEventTriggeringInTheFutureDisabled().build());
	}

	@Override
	public void requestReturnReception(ReturnRequestModel returnRequest)
	{
		getBusinessProcessService().triggerEvent(
				BusinessProcessEvent
						.builder(
								returnRequest.getReturnProcess().iterator().next().getCode() + "_"
										+ APPROVE_CANCEL_GOODS_EVENT_NAME).withChoice(ACCEPT_GOODS_CHOICE)
						.withEventTriggeringInTheFutureDisabled().build());
	}

	@Override
	public void requestReturnCancellation(ReturnRequestModel returnRequest)
	{
		String event = null;
		final ReturnProcessModel returnProcess = returnRequest.getReturnProcess().iterator().next();
		if (returnProcess.getCurrentTasks().iterator().next().getAction().equals(WAIT_FOR_FAIL_CAPTURE_ACTION))
		{
			event = FAIL_CAPTURE_ACTION_EVENT_NAME;
		}
		else if(returnProcess.getCurrentTasks().iterator().next().getAction().equals(WAIT_FOR_CONFIRM_OR_CANCEL_REFUND_ACTION))
		{
			event = CONFIRM_OR_CANCEL_REFUND_ACTION_EVENT_NAME;
		}
		else if(returnProcess.getCurrentTasks().iterator().next().getAction().equals(WAIT_FOR_GOODS_ACTION))
		{
			event = APPROVE_CANCEL_GOODS_EVENT_NAME;
		}

		getBusinessProcessService()
				.triggerEvent(BusinessProcessEvent.builder(returnProcess.getCode() + "_" + event)
						.withChoice(CANCEL_REFUND_CHOICE)
						.withEventTriggeringInTheFutureDisabled()
						.build());
	}

	protected BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	@Required
	public void setBusinessProcessService(BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

}
