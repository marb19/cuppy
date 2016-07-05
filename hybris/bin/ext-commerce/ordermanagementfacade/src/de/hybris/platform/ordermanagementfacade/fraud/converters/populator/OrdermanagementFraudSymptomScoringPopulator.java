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

import de.hybris.platform.converters.Populator;
import de.hybris.platform.fraud.model.FraudSymptomScoringModel;
import de.hybris.platform.ordermanagementfacade.fraud.data.FraudSymptomScoringsData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.util.Assert;


/**
 * Default order management implementation of fraud symptom scoring populator. 
 */
public class OrdermanagementFraudSymptomScoringPopulator implements Populator<FraudSymptomScoringModel, FraudSymptomScoringsData>
{
	@Override
	public void populate(final FraudSymptomScoringModel source, final FraudSymptomScoringsData target) throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		
		target.setExplanation(source.getExplanation());
		target.setName(source.getName());
		target.setScore(source.getScore());
	}

}
