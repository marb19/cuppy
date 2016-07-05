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
 */
package de.hybris.platform.yacceleratorordermanagement.actions.returns;

import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.warehousing.model.ReturnProcessModel;
import org.apache.log4j.Logger;

import java.util.Set;


/**
 * Approves the {@link de.hybris.platform.returns.model.ReturnRequestModel} by updating the {@link ReturnRequestModel#STATUS}
 * recalculate the return if quantity approved is not same as quantity requested originally and redirects the process.
 */
public class ApproveReturnAction extends AbstractProceduralAction<ReturnProcessModel>
{
	private static final Logger LOG = Logger.getLogger(ApproveReturnAction.class);

	@Override
	public void executeAction(ReturnProcessModel process) throws RetryLaterException, Exception
	{
		LOG.info("Process: " + process.getCode() + " in step " + getClass().getSimpleName());

		ReturnRequestModel returnRequest = process.getReturnRequest();
		returnRequest.setStatus(ReturnStatus.WAIT);
		returnRequest.getReturnEntries().forEach(entry -> {
			entry.setStatus(ReturnStatus.WAIT);
			getModelService().save(entry);
		});
		getModelService().saveAll(returnRequest);
		LOG.debug("Process: " + process.getCode() + " transitions to printReturnLabelAction");
	}

	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}

}
