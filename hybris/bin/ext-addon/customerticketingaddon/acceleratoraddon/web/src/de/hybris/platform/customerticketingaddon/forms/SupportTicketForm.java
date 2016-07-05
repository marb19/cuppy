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
package de.hybris.platform.customerticketingaddon.forms;

import de.hybris.platform.customerticketingfacades.data.TicketCategory;
import de.hybris.platform.validation.annotations.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class SupportTicketForm
{
	private String id;

	@NotEmpty(message = "{supportticket.subject.invalid}")
	@NotNull(message = "{supportticket.subject.invalid}")
	@Size(max = 255, message = "{supportticket.subject.invalid.length}")
	private String subject;

	@NotEmpty(message = "{supportticket.message.invalid}")
	@NotNull(message = "{supportticket.message.invalid}")
	@Size(max = 5000, message = "{supportticket.message.invalid.length}")
	private String message;
	private String status;
	private String associatedTo;
	private TicketCategory ticketCategory;

	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @param id
	 *           the id to set
	 */
	public void setId(final String id)
	{
		this.id = id;
	}

	/**
	 * @return the subject
	 */

	public String getSubject()
	{
		return subject;
	}

	/**
	 * @param subject
	 *           the subject to set
	 */
	public void setSubject(final String subject)
	{
		this.subject = subject;
	}

	/**
	 * @return the message
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * @param message
	 *           the message to set
	 */
	public void setMessage(final String message)
	{
		this.message = message;
	}

	/**
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * @param status
	 *           the status to set
	 */
	public void setStatus(final String status)
	{
		this.status = status;
	}

	/**
	 * @return the associtedObject
	 */
	public String getAssociatedTo()
	{
		return associatedTo;
	}

	/**
	 * @param associtedTo
	 *           the associtedObject to set
	 */
	public void setAssociatedTo(final String associtedTo)
	{
		this.associatedTo = associtedTo;
	}

	/**
	 * @return the ticketCategory
	 */
	public TicketCategory getTicketCategory()
	{
		return ticketCategory;
	}

	/**
	 * @param ticketCategory
	 *           the ticketCategory to set
	 */
	public void setTicketCategory(final TicketCategory ticketCategory)
	{
		this.ticketCategory = ticketCategory;
	}
}