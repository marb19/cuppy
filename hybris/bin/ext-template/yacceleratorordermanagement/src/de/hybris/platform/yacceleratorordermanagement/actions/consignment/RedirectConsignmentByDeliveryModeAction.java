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
package de.hybris.platform.yacceleratorordermanagement.actions.consignment;

import de.hybris.platform.commerceservices.model.PickUpDeliveryModeModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractAction;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;


/**
 * Redirects to the proper wait node depending on whether a consignment is for ship or pickup.
 */
public class RedirectConsignmentByDeliveryModeAction extends AbstractAction<ConsignmentProcessModel>
{
	private static final Logger LOG = Logger.getLogger(RedirectConsignmentByDeliveryModeAction.class);

	protected enum Transition
	{
		SHIP, PICKUP;

		public static Set<String> getStringValues()
		{
			final Set<String> res = new HashSet<String>();

			for (final Transition transition : Transition.values())
			{
				res.add(transition.toString());
			}
			return res;
		}
	}

	@Override
	public String execute(final ConsignmentProcessModel process)
	{
		LOG.info("Process: " + process.getCode() + " in step " + getClass().getSimpleName());
		final ConsignmentModel consignment = process.getConsignment();

		String transition = Transition.SHIP.toString();

		if (consignment.getDeliveryMode() instanceof PickUpDeliveryModeModel)
		{
			transition = Transition.PICKUP.toString();
		}

		LOG.debug("Process: " + process.getCode() + " transitions to " + transition);
		return transition;
	}

	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}
}
