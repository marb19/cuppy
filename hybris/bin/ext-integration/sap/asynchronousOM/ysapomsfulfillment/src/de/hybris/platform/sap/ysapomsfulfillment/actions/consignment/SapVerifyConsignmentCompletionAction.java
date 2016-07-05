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
package de.hybris.platform.sap.ysapomsfulfillment.actions.consignment;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.processengine.action.AbstractAction;
import de.hybris.platform.sap.ysapomsfulfillment.enums.ConsignmentEntryStatus;
import de.hybris.platform.sap.ysapomsfulfillment.model.SapConsignmentProcessModel;

/**
 * Checks the state of the consignment to perform changes (e.g., setting the
 * consignment status) according to the consignment's state changes resulted
 * from the actions performed on it.
 */
public class SapVerifyConsignmentCompletionAction extends AbstractAction<SapConsignmentProcessModel> {

	private static Logger LOGGER = LoggerFactory.getLogger(SapVerifyConsignmentCompletionAction.class);

	@Override
	public String execute(final SapConsignmentProcessModel sapConsignmentProcessModel) throws Exception {

		if (isConsignmnetShipped(sapConsignmentProcessModel.getConsignment())) {
			LOGGER.info("All the consigment entries are shipped.");
			return okTransition(sapConsignmentProcessModel);
		}
		LOGGER.info("Waiting for all the consigment entries to be shipped.");
		return Transition.WAIT.toString();
	}

	protected boolean isConsignmnetShipped(ConsignmentModel consignment) {

		return consignment
				.getConsignmentEntries()
				.stream()
				.allMatch(
						enconsignmentEntryt -> ((ConsignmentEntryModel) enconsignmentEntryt).getStatus().equals(
								ConsignmentEntryStatus.SHIPPED));

	}

	protected String okTransition(final SapConsignmentProcessModel consignmentProcessModel) {

		final ConsignmentModel consignment = consignmentProcessModel.getConsignment();
		consignment.setStatus(ConsignmentStatus.SHIPPED);
		getModelService().save(consignment);

		return Transition.OK.toString();
	}

	protected enum Transition {
		OK, WAIT;

		public static Set<String> getStringValues() {
			final Set<String> res = new HashSet<String>();

			for (final Transition transition : Transition.values()) {
				res.add(transition.toString());
			}
			return res;
		}
	}

	@Override
	public Set<String> getTransitions() {
		return Transition.getStringValues();
	}

}
