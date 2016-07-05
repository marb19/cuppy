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

import de.hybris.platform.accountsummaryaddon.utils.AccountSummaryAddonUtils;

import java.math.BigDecimal;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;


/**
 *
 */
public class AmountRangeCriteria extends RangeCriteria
{

	protected Optional<BigDecimal> startRange;
	protected Optional<BigDecimal> endRange;

	public AmountRangeCriteria(final String filterByKey)
	{
		this(filterByKey, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY);
	}

	public AmountRangeCriteria(final String filterByKey, final String startRange, final String endRange,
			final String documentStatus)
	{
		super(filterByKey, startRange, endRange, documentStatus);
	}

	/**
	 * @return the startRange
	 */
	@Override
	public Optional<BigDecimal> getStartRange()
	{
		return startRange;
	}

	/**
	 * @param startRange
	 *           the startRange to set
	 */
	@Override
	protected void setStartRange(final String startRange)
	{
		this.startRange = StringUtils.isNotBlank(startRange) ? AccountSummaryAddonUtils.parseBigDecimal(startRange) : Optional
				.empty();
	}

	/**
	 * @return the endRange
	 */
	@Override
	public Optional<BigDecimal> getEndRange()
	{
		return endRange;
	}

	/**
	 * @param endRange
	 *           the endRange to set
	 */
	@Override
	protected void setEndRange(final String endRange)
	{
		this.endRange = StringUtils.isNotBlank(endRange) ? AccountSummaryAddonUtils.parseBigDecimal(endRange) : Optional.empty();
	}
}
