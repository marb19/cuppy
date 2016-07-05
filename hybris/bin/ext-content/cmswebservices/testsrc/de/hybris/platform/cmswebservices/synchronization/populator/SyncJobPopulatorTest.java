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
package de.hybris.platform.cmswebservices.synchronization.populator;


import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmswebservices.data.SyncJobData;
import de.hybris.platform.cmswebservices.synchronization.facade.populator.SyncJobDataPopulator;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;

import java.util.Date;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

@UnitTest
public class SyncJobPopulatorTest
{

	@Test
	public void testSuccessfulPopulation()
	{
		final SyncJobDataPopulator syncJobDataPopulator = new SyncJobDataPopulator();
		final CronJobModel cronJob = new CronJobModel();

		final CronJobStatus status = CronJobStatus.RUNNING;
		final CronJobResult result = CronJobResult.SUCCESS;
		final String statusString = status.toString();
		final String resultString = result.toString();
		final Date currentDate = new Date();

		cronJob.setStatus(status);
		cronJob.setModifiedtime(currentDate);
		cronJob.setCreationtime(currentDate);
		cronJob.setEndTime(currentDate);
		cronJob.setResult(result);
		cronJob.setStartTime(currentDate);
		
		final SyncJobData syncJob = new SyncJobData();

		final Optional<CronJobModel> cronJobOpt = Optional.of(cronJob);



		syncJobDataPopulator.populate(cronJobOpt, syncJob);

		Assert.assertEquals(statusString, syncJob.getSyncStatus());
		Assert.assertEquals(resultString, syncJob.getSyncResult());
		Assert.assertEquals(currentDate, syncJob.getLastModifiedDate());
		Assert.assertEquals(currentDate, syncJob.getCreationDate());
		Assert.assertEquals(currentDate, syncJob.getEndDate());
		Assert.assertEquals(currentDate, syncJob.getStartDate());
	}

}
