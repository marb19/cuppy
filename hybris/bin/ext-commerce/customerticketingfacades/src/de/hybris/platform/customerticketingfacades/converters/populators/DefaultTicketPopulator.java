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
package de.hybris.platform.customerticketingfacades.converters.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.customerticketingfacades.data.StatusData;
import de.hybris.platform.customerticketingfacades.data.TicketCategory;
import de.hybris.platform.customerticketingfacades.data.TicketData;
import de.hybris.platform.customerticketingfacades.data.TicketEventData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.ticket.comparator.TicketEventsComparator;
import de.hybris.platform.ticket.enums.CsTicketState;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.TicketService;
import de.hybris.platform.util.localization.Localization;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Converter implementation for {@link de.hybris.platform.ticket.model.CsTicketModel} as source and
 * {@link de.hybris.platform.customerticketingfacades.data.TicketData} as target type.
 */
public class DefaultTicketPopulator<SOURCE extends CsTicketModel, TARGET extends TicketData> implements Populator<SOURCE, TARGET>
{
	protected static final Logger LOG = Logger.getLogger(DefaultTicketPopulator.class);

	protected static final String LAST_UPDATED = "Updated";
	protected static final String CUSTOMER_SERVICE = "Customer Service";

	private Map<String, StatusData> statusMapping;
	private Map<StatusData, List<StatusData>> validTransitions;
	private TicketService ticketService;

	@Override
	public void populate(final CsTicketModel source, final TicketData target) throws ConversionException
	{

		target.setSubject(source.getHeadline());
		target.setId(source.getTicketID());
		final CsTicketState csTicketState = source.getState();
		target.setStatus(statusMapping.get(csTicketState.getCode()));
		target.setAvailableStatusTransitions(validTransitions.get(target.getStatus()));
		target.setCreationDate(source.getCreationtime());
		target.setLastModificationDate(source.getModifiedtime());
		target.setCustomerId(source.getCustomer().getUid());
		target.setTicketEvents(getTicketEvents(source));
		try
		{
			target.setTicketCategory(TicketCategory.valueOf(source.getCategory().getCode().toUpperCase()));
		}
		catch (final IllegalArgumentException ex)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(source.getCategory().getCode().toUpperCase()
						+ "ticket category is not enabled to display for the customer ticketing");
			}
		}
		if (source.getOrder() != null)
		{
			final AbstractOrderModel abstractOrderModel = source.getOrder();
			final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
			target.setAssociatedTo(abstractOrderModel.getItemtype() + ": " + abstractOrderModel.getCode() + "; " + LAST_UPDATED
					+ ": " + sdf.format(abstractOrderModel.getModifiedtime()));
		}
	}

	/**
	 * @param source
	 *           as CsTicketModel
	 * @return List<TicketEventData>
	 */
	protected List<TicketEventData> getTicketEvents(final CsTicketModel source)
	{
		final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yy hh:mm a");
		final List<TicketEventData> ticketMessages = new ArrayList<TicketEventData>();

		final List<CsTicketEventModel> events = new ArrayList<CsTicketEventModel>(ticketService.getTicketEventsForCustomerByTicket(source));
		Collections.sort(events, new TicketEventsComparator());

		for (final CsTicketEventModel csTicketEventModel : events)
		{
			if (StringUtils.isNotEmpty(csTicketEventModel.getText()))
			{
				final TicketEventData ticketMessagesData = new TicketEventData();
				ticketMessagesData.setStartDateTime(format.format(csTicketEventModel.getCreationtime()));
				ticketMessagesData.setText(csTicketEventModel.getText());
				final StringBuilder textBuilder = new StringBuilder();
				if (csTicketEventModel.getAuthor() != null && csTicketEventModel.getAuthor() instanceof EmployeeModel)
				{
					ticketMessagesData.setAuthor(CUSTOMER_SERVICE);
					textBuilder.append(Localization.getLocalizedString("text.supporttickets.history.customer.service"));
				}
				else
				{
					ticketMessagesData.setAuthor(source.getCustomer().getName());
					textBuilder.append(source.getCustomer().getName());
				}

				textBuilder.append(" ").append(Localization.getLocalizedString("text.supporttickets.history.on")).append(" ")
						.append(ticketMessagesData.getStartDateTime()).append("\n").append(csTicketEventModel.getText());

				ticketMessagesData.setDisplayText(textBuilder.toString());
				ticketMessages.add(ticketMessagesData);
			}
		}
		return ticketMessages;
	}

	/**
	 * @deprecated Please use getTicketEvents method instead.
	 *
	 * @param source
	 *           as CsTicketModel
	 * @return messageHistory
	 */
	@Deprecated
	protected String getMessageHistory(final CsTicketModel source)
	{
		final List<CsTicketEventModel> events = ticketService.getEventsForTicket(source);
		final StringBuilder sb = new StringBuilder();
		final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yy hh:mm a");
		for (final CsTicketEventModel csTicketEventModel : events)
		{
			if (StringUtils.isNotEmpty(csTicketEventModel.getText()))
			{
				sb.append(format.format(csTicketEventModel.getCreationtime()) + " added by ");
				if (csTicketEventModel.getAuthor() != null && csTicketEventModel.getAuthor() instanceof EmployeeModel)
				{
					sb.append("Customer Service");
				}
				else
				{
					sb.append(source.getCustomer().getName());
				}
				sb.append("\n" + csTicketEventModel.getText() + "\n\n");
			}
		}
		return sb.toString();
	}

	/**
	 * @return the statusMapping
	 */
	public Map<String, StatusData> getStatusMapping()
	{
		return statusMapping;
	}

	/**
	 * @return the validTransitions
	 */
	public Map<StatusData, List<StatusData>> getValidTransitions()
	{
		return validTransitions;
	}

	/**
	 * @return the ticketService
	 */
	protected TicketService getTicketService()
	{
		return ticketService;
	}

	/**
	 * @param statusMapping
	 *           the statusMapping to set
	 */
	@Required
	public void setStatusMapping(final Map<String, StatusData> statusMapping)
	{
		this.statusMapping = statusMapping;
	}

	/**
	 * @param validTransitions
	 *           the validTransitions to set
	 */
	@Required
	public void setValidTransitions(final Map<StatusData, List<StatusData>> validTransitions)
	{
		this.validTransitions = validTransitions;
	}

	/**
	 * @param ticketService
	 *           the ticketService to set
	 */
	@Required
	public void setTicketService(final TicketService ticketService)
	{
		this.ticketService = ticketService;
	}
}
