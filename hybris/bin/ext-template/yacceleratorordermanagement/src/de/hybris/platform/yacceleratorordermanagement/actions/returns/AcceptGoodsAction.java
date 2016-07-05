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


/**
 * Approves the goods requested in the {@link de.hybris.platform.returns.model.ReturnRequestModel}.
 */
public class AcceptGoodsAction extends AbstractProceduralAction<ReturnProcessModel>
{
	private static final Logger LOG = Logger.getLogger(AcceptGoodsAction.class);

	@Override
	public void executeAction(final ReturnProcessModel process) throws RetryLaterException, Exception
	{
		LOG.info("Process: " + process.getCode() + " in step " + getClass().getSimpleName());

		final ReturnRequestModel returnRequest = process.getReturnRequest();
		returnRequest.setStatus(ReturnStatus.RECEIVED);
		returnRequest.getReturnEntries().forEach(entry -> {
			entry.setStatus(ReturnStatus.RECEIVED);
			entry.setReceivedQuantity(entry.getExpectedQuantity());
			getModelService().save(entry);
		});
		getModelService().saveAll(returnRequest);
	}
}
