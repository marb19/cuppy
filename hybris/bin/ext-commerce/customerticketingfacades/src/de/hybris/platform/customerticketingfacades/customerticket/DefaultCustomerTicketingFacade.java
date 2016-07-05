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
package de.hybris.platform.customerticketingfacades.customerticket;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.customerticketingfacades.TicketFacade;
import de.hybris.platform.customerticketingfacades.constants.CustomerticketingfacadesConstants;
import de.hybris.platform.customerticketingfacades.data.StatusData;
import de.hybris.platform.customerticketingfacades.data.TicketAssociatedData;
import de.hybris.platform.customerticketingfacades.data.TicketCategory;
import de.hybris.platform.customerticketingfacades.data.TicketData;
import de.hybris.platform.customerticketingfacades.strategies.TicketAssociationStrategies;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ClassMismatchException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.ticket.enums.CsEventReason;
import de.hybris.platform.ticket.enums.CsInterventionType;
import de.hybris.platform.ticket.enums.CsResolutionType;
import de.hybris.platform.ticket.enums.CsTicketCategory;
import de.hybris.platform.ticket.enums.CsTicketPriority;
import de.hybris.platform.ticket.enums.CsTicketState;
import de.hybris.platform.ticket.events.model.CsCustomerEventModel;
import de.hybris.platform.ticket.model.CsAgentGroupModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.TicketBusinessService;
import de.hybris.platform.ticket.service.TicketException;
import de.hybris.platform.ticket.service.TicketService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * CS Integration Ticket Facade Facade should provide access to a user's support ticket details and full details of
 * support ticket.
 *
 */
public class DefaultCustomerTicketingFacade implements TicketFacade
{
	protected static final Logger LOG = Logger.getLogger(DefaultCustomerTicketingFacade.class);
	protected static final String UNKNOWN_TYPE = "Unknown type";

	private TicketService ticketService;
	private UserService userService;
	private TicketBusinessService ticketBusinessService;
	private Converter<CsTicketModel, TicketData> ticketConverter;
	private Map<String, StatusData> statusMapping;
	private List<TicketAssociationStrategies> associationStrategies;
	private String ticketPriority;
	private String ticketReason;
	private EnumerationService enumerationService;
	private ConfigurationService configurationService;

	@Override
	public TicketData createTicket(final TicketData ticketData)
	{
		CsTicketModel ticket = null;
		try
		{
			final CsTicketPriority priority = enumerationService.getEnumerationValue(CsTicketPriority._TYPECODE, ticketPriority);
			final CsEventReason reason = enumerationService.getEnumerationValue(CsEventReason._TYPECODE, ticketReason);
			final AbstractOrderModel abstractOder = ticketService.getAssociatedObject(ticketData.getAssociatedTo(), null, null);

			final CsAgentGroupModel agnetGroup = this.getDefaultCsAgentManagerGroup();
			final CsTicketCategory csTicketCategory = CsTicketCategory.valueOf(ticketData.getTicketCategory().name());
			if (null == abstractOder)
			{
				ticket = getTicketBusinessService().createTicket(getUserService().getCurrentUser(), csTicketCategory, priority, null,
						agnetGroup, ticketData.getSubject(), CsInterventionType.TICKETMESSAGE, reason, ticketData.getMessage());
			}
			else if (abstractOder instanceof OrderModel)
			{

				ticket = getTicketBusinessService().createTicket(getUserService().getCurrentUser(), (OrderModel) abstractOder,
						csTicketCategory, priority, null, agnetGroup, ticketData.getSubject(), CsInterventionType.TICKETMESSAGE,
						reason, ticketData.getMessage());
			}
			else if (abstractOder instanceof CartModel)
			{
				ticket = getTicketBusinessService().createTicket(getUserService().getCurrentUser(), (CartModel) abstractOder,
						csTicketCategory, priority, null, agnetGroup, ticketData.getSubject(), CsInterventionType.TICKETMESSAGE,
						reason, ticketData.getMessage());
			}
			else
			{
				throw new UnknownIdentifierException(UNKNOWN_TYPE);
			}

		}
		catch (final ModelSavingException msEx)
		{
			LOG.error(msEx.getMessage(), msEx);
		}
		catch (final Exception ex)
		{
			// We can treat other exceptions, other than ModelSavingException, as the ticket has been created.
			LOG.error(ex.getMessage(), ex);
		}

		if (ticket == null || ticket.getTicketID() == null)
		{
			return null;
		}
		else
		{
			ticketData.setId(ticket.getTicketID());
		}
		return ticketData;
	}

	/**
	 * @return CsAgentGroupModel - Default Cs Agent Group Manager
	 */
	protected CsAgentGroupModel getDefaultCsAgentManagerGroup()
	{
		final String csManagerGroup = getConfigurationService().getConfiguration().getString(
				CustomerticketingfacadesConstants.DEFAULT_CS_AGENT_MANAGER_GROUP_UID, "");
		if (StringUtils.isNotBlank(csManagerGroup))
		{
			try
			{
				return getUserService().getUserGroupForUID(csManagerGroup, CsAgentGroupModel.class);
			}
			catch (ClassMismatchException | UnknownIdentifierException exception)
			{
				LOG.error("Can't set AssignedGroup for the group " + csManagerGroup + ", cause: " + exception);
			}
		}
		return null;
	}

	@Override
	public TicketData updateTicket(final TicketData ticketData)
	{
		CsTicketModel ticket = ticketService.getTicketForTicketId(ticketData.getId());

		// if with status change
		if (!statusMapping.get(ticket.getState().getCode()).getId().equalsIgnoreCase(ticketData.getStatus().getId()))
		{
			ticket = stateChanges.get(getCsStatus(ticketData)).apply(ticket, ticketData);
		}
		else
		// only a note
		{
			final CsCustomerEventModel customerEventModel = ticketBusinessService.addNoteToTicket(ticket, CsInterventionType.IM,
					CsEventReason.UPDATE, ticketData.getMessage(), null);
			ticket = customerEventModel != null ? ticketService.getTicketForTicketId(ticket.getTicketID()) : null;
		}

		if (ticket == null)
		{
			return null;
		}

		return ticketData;
	}

	/**
	 * Matches TicketData.Status with CsTicketStatus, using statusMapping map.
	 *
	 * @param data
	 * @return CsTicketState
	 */
	protected CsTicketState getCsStatus(final TicketData data)
	{
		for (final String key : statusMapping.keySet())
		{
			if (data.getStatus().getId().equalsIgnoreCase(statusMapping.get(key).getId()))
			{
				LOG.info("matching with: " + key);
				return CsTicketState.valueOf(key);
			}
		}
		LOG.warn("Status key not found");
		return null;
	}

	/**
	 * Special map, that know what to do in case of status changed.
	 */
	protected Map<CsTicketState, BiFunction<CsTicketModel, TicketData, CsTicketModel>> stateChanges = new HashMap<CsTicketState, BiFunction<CsTicketModel, TicketData, CsTicketModel>>()
	{
		{
			put(CsTicketState.CLOSED, (updatedTicket, ticketData) -> {
				try
				{
					return ticketBusinessService.resolveTicket(updatedTicket, CsInterventionType.IM, CsResolutionType.CLOSED,
							ticketData.getMessage()) != null ? ticketService.getTicketForTicketId(updatedTicket.getTicketID()) : null;
				}
				catch (final TicketException ex)
				{
					LOG.error(ex.getMessage(), ex);
					return null;
				}
			});

			put(CsTicketState.OPEN, (updatedTicket, ticketData) -> {
				try
				{
					return ticketBusinessService.unResolveTicket(updatedTicket, CsInterventionType.IM, CsEventReason.UPDATE,
							ticketData.getMessage()) != null ? ticketService.getTicketForTicketId(updatedTicket.getTicketID()) : null;
				}
				catch (final TicketException ex)
				{
					LOG.error(ex.getMessage(), ex);
					return null;
				}
			});
		}
	};

	@Override
	public TicketData getTicket(final String ticketId)
	{
		final CsTicketModel ticketModel = ticketService.getTicketForTicketId(ticketId);
		if (ticketModel == null)
		{
			return null; // preventing NPE below
		}

		return ticketModel.getCustomer().getUid().equals(userService.getCurrentUser().getUid()) ? ticketConverter.convert(
				ticketModel, new TicketData()) : null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public SearchPageData<TicketData> getTickets(final PageableData pageableData)
	{
		final List<TicketData> tickets = new ArrayList<TicketData>();
		final List<CsTicketModel> ticketsForCustomer = ticketService.getTicketsForCustomerOrderByModifiedTime(userService
				.getCurrentUser());
		for (final CsTicketModel csTicketModel : ticketsForCustomer)
		{
			tickets.add(ticketConverter.convert(csTicketModel, new TicketData()));
		}

		final SearchPageData<TicketData> results = new SearchPageData<TicketData>();
		results.setResults(tickets);

		return results;
	}

	@Override
	public Map<String, List<TicketAssociatedData>> getAssociatedToObjects()
	{
		final Map<String, List<TicketAssociatedData>> associatedObjects = new HashMap<String, List<TicketAssociatedData>>();
		for (final TicketAssociationStrategies ticketAssocitedStartegy : associationStrategies)
		{
			associatedObjects.putAll(ticketAssocitedStartegy.getObjects(userService.getCurrentUser()));
		}
		return associatedObjects;
	}

	/**
	 * @return the ticketService
	 */
	protected TicketService getTicketService()
	{
		return ticketService;
	}

	/**
	 * @return the userService
	 */
	protected UserService getUserService()
	{
		return userService;
	}

	/**
	 * @return the ticketBusinessService
	 */
	protected TicketBusinessService getTicketBusinessService()
	{
		return ticketBusinessService;
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

	/**
	 * @param userService
	 *           the userService to set
	 */
	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @param ticketBusinessService
	 *           the ticketBusinessService to set
	 */
	@Required
	public void setTicketBusinessService(final TicketBusinessService ticketBusinessService)
	{
		this.ticketBusinessService = ticketBusinessService;
	}

	/**
	 * @return the ticketConverter
	 */
	protected Converter<CsTicketModel, TicketData> getTicketConverter()
	{
		return ticketConverter;
	}

	/**
	 * @return the statusMapping
	 */
	protected Map<String, StatusData> getStatusMapping()
	{
		return statusMapping;
	}

	/**
	 * @return the associationStrategies
	 */
	protected List<TicketAssociationStrategies> getAssociationStrategies()
	{
		return associationStrategies;
	}

	/**
	 * @param ticketConverter
	 *           the ticketConverter to set
	 */
	@Required
	public void setTicketConverter(final Converter<CsTicketModel, TicketData> ticketConverter)
	{
		this.ticketConverter = ticketConverter;
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
	 * @param associationStrategies
	 *           the associationStrategies to set
	 */
	@Required
	public void setAssociationStrategies(final List<TicketAssociationStrategies> associationStrategies)
	{
		this.associationStrategies = associationStrategies;
	}

	/**
	 * @return the ticketPriority
	 */
	protected String getTicketPriority()
	{
		return ticketPriority;
	}

	/**
	 * @param ticketPriority
	 *           the ticketPriority to set
	 */
	@Required
	public void setTicketPriority(final String ticketPriority)
	{
		this.ticketPriority = ticketPriority;
	}

	/**
	 * @return the ticketReason
	 */
	protected String getTicketReason()
	{
		return ticketReason;
	}

	/**
	 * @param ticketReason
	 *           the ticketReason to set
	 */
	@Required
	public void setTicketReason(final String ticketReason)
	{
		this.ticketReason = ticketReason;
	}

	/**
	 * @return the enumerationService
	 */
	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	/**
	 * @param enumerationService
	 *           the enumerationService to set
	 */
	@Required
	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

	@Override
	public List<TicketCategory> getTicketCategories()
	{
		return Arrays.asList(TicketCategory.values());
	}

	/**
	 * @return the configurationService
	 */
	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}
}