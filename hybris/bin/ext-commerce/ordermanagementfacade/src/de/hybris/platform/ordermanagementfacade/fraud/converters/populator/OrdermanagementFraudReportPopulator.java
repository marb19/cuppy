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
package de.hybris.platform.ordermanagementfacade.fraud.converters.populator;

import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.fraud.model.FraudReportModel;
import de.hybris.platform.fraud.model.FraudSymptomScoringModel;
import de.hybris.platform.ordermanagementfacade.fraud.data.FraudReportData;
import de.hybris.platform.ordermanagementfacade.fraud.data.FraudSymptomScoringsData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.fest.util.Collections;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


/**
 * Default order management implementation of Fraud Report populator
 */
public class OrdermanagementFraudReportPopulator implements Populator<FraudReportModel, FraudReportData>
{
	private Converter<FraudSymptomScoringModel, FraudSymptomScoringsData> fraudSymptomScoringConverter;

	@Override
	public void populate(final FraudReportModel source, final FraudReportData target) throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		
		target.setExplanation(source.getExplanation());
		target.setProvider(source.getProvider());
		target.setTimestamp(source.getTimestamp());
		
		if(!Collections.isEmpty(source.getFraudSymptomScorings()))
		{
			target.setFraudSymptomScorings(Converters.convertAll(source.getFraudSymptomScorings(), getFraudSymptomScoringConverter()));
		}
		
		if(source.getStatus() != null)
		{
			target.setStatus(source.getStatus().getCode());
		}
	}

	protected Converter<FraudSymptomScoringModel, FraudSymptomScoringsData> getFraudSymptomScoringConverter()
	{
		return fraudSymptomScoringConverter;
	}

	@Required
	public void setFraudSymptomScoringConverter(
			final Converter<FraudSymptomScoringModel, FraudSymptomScoringsData> fraudSymptomScoringConverter)
	{
		this.fraudSymptomScoringConverter = fraudSymptomScoringConverter;
	}
}
