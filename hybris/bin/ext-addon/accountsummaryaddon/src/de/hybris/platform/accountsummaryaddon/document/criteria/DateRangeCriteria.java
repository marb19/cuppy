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
package de.hybris.platform.accountsummaryaddon.document.criteria;

import de.hybris.platform.accountsummaryaddon.constants.AccountsummaryaddonConstants;
import de.hybris.platform.accountsummaryaddon.utils.AccountSummaryAddonUtils;

import java.util.Date;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;


/**
 *
 */
public class DateRangeCriteria extends RangeCriteria
{

	protected Optional<Date> startRange;
	protected Optional<Date> endRange;

	public DateRangeCriteria(final String filterByKey)
	{
		this(filterByKey, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY);
	}

	public DateRangeCriteria(final String filterByKey, final String startRange, final String endRange, final String documentStatus)
	{
		super(filterByKey, startRange, endRange, documentStatus);
	}

	/**
	 * @return the startRange
	 */
	@Override
	public Optional<Date> getStartRange()
	{
		return this.startRange;
	}

	/**
	 * @param startRange
	 *           the startRange to set
	 */
	@Override
	protected void setStartRange(final String startRange)
	{
		this.startRange = StringUtils.isNotBlank(startRange) ? AccountSummaryAddonUtils.parseDate(startRange,
				AccountsummaryaddonConstants.DATE_FORMAT_MM_DD_YYYY) : Optional.empty();
	}

	/**
	 * @return the endRange
	 */
	@Override
	public Optional<Date> getEndRange()
	{
		return this.endRange;
	}

	/**
	 * @param endRange
	 *           the endRange to set
	 */
	@Override
	protected void setEndRange(final String endRange)
	{
		this.endRange = StringUtils.isNotBlank(endRange) ? AccountSummaryAddonUtils.parseDate(endRange,
				AccountsummaryaddonConstants.DATE_FORMAT_MM_DD_YYYY) : Optional.empty();
	}

}
