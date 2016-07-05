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
package de.hybris.platform.fractussyncservices.cronjob;


import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.fractussyncservices.model.FractusSyncCronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.y2ysync.services.SyncExecutionService;
import org.springframework.beans.factory.annotation.Required;


public class FractusSyncCronJobPerformable extends AbstractJobPerformable<FractusSyncCronJobModel>
{
	private SyncExecutionService syncExecutionService;

	@Override
	public PerformResult perform(FractusSyncCronJobModel jobModel)
	{
		getSyncExecutionService().startSync(jobModel.getY2ySyncJob(), SyncExecutionService.ExecutionMode.ASYNC);

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	protected SyncExecutionService getSyncExecutionService()
	{
		return syncExecutionService;
	}

	@Required
	public void setSyncExecutionService(SyncExecutionService syncExecutionService)
	{
		this.syncExecutionService = syncExecutionService;
	}
}
