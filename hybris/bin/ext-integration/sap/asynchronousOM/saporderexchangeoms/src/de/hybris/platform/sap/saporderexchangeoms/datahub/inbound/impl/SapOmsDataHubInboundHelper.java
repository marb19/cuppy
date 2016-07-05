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
package de.hybris.platform.sap.saporderexchangeoms.datahub.inbound.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.sap.orderexchange.constants.DataHubInboundConstants;
import de.hybris.platform.sap.orderexchange.datahub.inbound.impl.AbstractDataHubInboundHelper;
import de.hybris.platform.sap.saporderexchangeoms.datahub.inbound.SapDataHubInboundHelper;
import de.hybris.platform.sap.ysapomsfulfillment.enums.ConsignmentEntryStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

import org.apache.log4j.Logger;


/**
 * Default Data Hub Inbound helper for delivery related notifications
 */
public class SapOmsDataHubInboundHelper extends AbstractDataHubInboundHelper implements SapDataHubInboundHelper
{
	private static final Logger LOG = Logger.getLogger(SapOmsDataHubInboundHelper.class);

	private final BiConsumer<ConsignmentModel, ConsignmentStatus> updateConsignmentStatus = (consignment, status) -> consignment
			.setStatus(status);

	private final BiFunction<ConsignmentModel, String, Stream<ConsignmentEntryModel>> identifyConsignmentEntry = (consignment,
			entryNumber) -> consignment.getConsignmentEntries().stream()
			.filter(consignmentEntry -> consignmentEntry.getSapOrderEntryRowNumber() == Integer.parseInt(entryNumber));


	protected Date convertStringToDate(final String dateString)
	{
		Date date = null;
		if (isDateSet(dateString))
		{
			final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DataHubInboundConstants.IDOC_DATE_FORMAT);
			try
			{
				date = simpleDateFormat.parse(dateString);
			}
			catch (final ParseException e)
			{
				throw new IllegalArgumentException(String.format("Date %s can not be converted to a date", dateString), e);
			}
		}
		return date;
	}

	@Override
	public String determineEntryNumber(final String deliveryInfo)
	{
		final int firstIndex = deliveryInfo.indexOf(DataHubInboundConstants.STR) + 1;
		int secondIndex = deliveryInfo.indexOf(DataHubInboundConstants.STR, 5);
		secondIndex = secondIndex > 0 ? secondIndex : deliveryInfo.length();
		return secondIndex - firstIndex > 0 ? deliveryInfo.substring(firstIndex, secondIndex) : null;
	}

	@Override
	public String determineGoodsIssueDate(final String deliveryInfo)
	{
		final int firstIndex = deliveryInfo.indexOf(DataHubInboundConstants.STR, 10) + 1;
		final int secondIndex = deliveryInfo.indexOf(DataHubInboundConstants.STR, 19);
		String date = null;
		if (secondIndex != firstIndex)
		{
			final String delivDate = deliveryInfo.substring(firstIndex, secondIndex);
			if (!delivDate.equals(DataHubInboundConstants.DATE_NOT_SET))
			{
				date = deliveryInfo.substring(firstIndex, secondIndex);
			}
		}
		return date;
	}

	@Override
	public String determineQuantity(final String deliveryInfo)
	{
		final int index = deliveryInfo.indexOf(DataHubInboundConstants.STR, 19);
		final int firstIndex = index > 0 ? index + 1 : deliveryInfo.indexOf(DataHubInboundConstants.STR, 10) + 1;
		final int secondIndex = deliveryInfo.length();
		return secondIndex - firstIndex > 0 ? deliveryInfo.substring(firstIndex, secondIndex).split("\\.")[0] : "0";
	}

	protected boolean isDateSet(final String date)
	{
		return !(date == null || date.isEmpty() || DataHubInboundConstants.DATE_NOT_SET.equals(date));
	}

	@Override
	public void processDeliveryNotification(final String orderCode, final String entryNumber)
	{
		final OrderModel order = readOrder(orderCode);

		for (final ConsignmentModel consignment : order.getConsignments())
		{
			final Optional<ConsignmentEntryModel> consignmentEntries = identifyConsignmentEntry.apply(consignment, entryNumber)
					.findFirst();
			consignmentEntries.ifPresent(consignmentEntry -> triggerDeliveryEvent(order, consignmentEntry));
		}

	}

	protected void triggerDeliveryEvent(final OrderModel order, final ConsignmentEntryModel consignmentEntry)
	{

		consignmentEntry.setStatus(ConsignmentEntryStatus.PICKPACK);

		getModelService().save(consignmentEntry);

		updateConsignmentDeliveryStatus(order);

		LOG.info(String.format("Delivery of order entry %s_%d is processed!", order.getCode(),
				Integer.valueOf(consignmentEntry.getSapOrderEntryRowNumber())));

		final String event = new StringBuilder().append(DataHubInboundConstants.DELIVERY_EVENTNAME_PREFIX).append(order.getCode())
				.append("_").append(consignmentEntry.getConsignment().getCode()).toString();

		LOG.info(String.format("Delivery event %s is triggered!", event));

		getBusinessProcessService().triggerEvent(event);

	}


	@Override
	public void processGoodsIssueNotification(final String orderCode, final String entryNumber, final String quantity,
			final String goodsIssueDate)
	{

		final OrderModel order = readOrder(orderCode);

		for (final ConsignmentModel consignment : order.getConsignments())
		{
			final Optional<ConsignmentEntryModel> consignmentEntries = identifyConsignmentEntry.apply(consignment, entryNumber)
					.findFirst();
			consignmentEntries.ifPresent(consignmentEntry -> triggerGoodsIssueEvent(order, consignmentEntry, quantity,
					goodsIssueDate));
		}

	}


	/**
	 * @param order
	 * @param consignmentEntry
	 * @param quantity
	 * @param goodsIssueDate
	 */
	protected void triggerGoodsIssueEvent(final OrderModel order, final ConsignmentEntryModel consignmentEntry,
			final String quantity, final String goodsIssueDate)
	{

		Long shippedQuantity = consignmentEntry.getShippedQuantity() != null ? consignmentEntry.getShippedQuantity() : Long
				.valueOf(0);

		shippedQuantity = Long.valueOf(shippedQuantity.longValue() + Long.parseLong(quantity));
		consignmentEntry.setShippedQuantity(shippedQuantity);
		getModelService().save(consignmentEntry);


		if (consignmentEntry.getQuantity() == consignmentEntry.getShippedQuantity())
		{
			consignmentEntry.setStatus(ConsignmentEntryStatus.SHIPPED);
			consignmentEntry.getConsignment().setShippingDate(convertStringToDate(goodsIssueDate));
			getModelService().save(consignmentEntry);
		}

		updateConsignmentShippingStatus(order);

		LOG.info(String.format("Shipping of order entry %s_%d is processed!", order.getCode(),
				Integer.valueOf(consignmentEntry.getSapOrderEntryRowNumber())));

		final String event = new StringBuilder().append(DataHubInboundConstants.GOODS_ISSUE_EVENTNAME_PREFIX)
				.append(order.getCode()).append("_").append(consignmentEntry.getConsignment().getCode()).toString();

		LOG.info(String.format("Shipping event %s is triggered!", event));

		getBusinessProcessService().triggerEvent(event);

	}

	/**
	 * Update consignment delivery status. The consignment status is set to Pick Pack if one of its consignment entries
	 * has received a delivery notification from ERP
	 *
	 * @param order
	 */
	protected void updateConsignmentDeliveryStatus(final OrderModel order)
	{
		final BiPredicate<ConsignmentModel, ConsignmentEntryStatus> isStatusChanged = (consignment, consignmentEntryStatus) -> consignment
				.getConsignmentEntries().stream()
				.anyMatch(consignmentEntry -> consignmentEntry.getStatus().equals(consignmentEntryStatus));

		order.getConsignments().stream().forEach(entry -> {
			if (isStatusChanged.test(entry, ConsignmentEntryStatus.PICKPACK))
			{
				this.updateConsignmentStatus.accept(entry, ConsignmentStatus.PICKPACK);
				getModelService().save(entry);
			}
		});

	}

	/**
	 * Update consignment shipping status. The consignment status is set to Shipped if all of its consignment entries are
	 * shipped
	 *
	 * @param order
	 */
	protected void updateConsignmentShippingStatus(final OrderModel order)
	{
		final BiPredicate<ConsignmentModel, ConsignmentEntryStatus> isStatusChanged = (consignment, consignmentEntryStatus) -> consignment
				.getConsignmentEntries().stream()
				.allMatch(consignmentEntry -> consignmentEntry.getStatus().equals(consignmentEntryStatus));

		order.getConsignments().stream().forEach(entry -> {
			if (isStatusChanged.test(entry, ConsignmentEntryStatus.SHIPPED))
			{
				this.updateConsignmentStatus.accept(entry, ConsignmentStatus.SHIPPED);
				getModelService().save(entry);
			}
		});

	}

}
