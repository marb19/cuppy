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
package de.hybris.platform.yacceleratorordermanagement.actions.returns;

import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.payment.PaymentService;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.warehousing.model.ReturnProcessModel;

import java.math.BigDecimal;
import java.util.List;

import de.hybris.platform.warehousing.returns.service.RefundAmountCalculationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Part of the refund process that returns the money to the customer.
 */
public class CaptureRefundAction extends AbstractSimpleDecisionAction<ReturnProcessModel>
{
	private static final Logger LOG = Logger.getLogger(CaptureRefundAction.class);

	private PaymentService paymentService;
	private RefundAmountCalculationService refundAmountCalculationService;

	@Override
	public Transition executeAction(final ReturnProcessModel process) throws RetryLaterException, Exception
	{
		LOG.debug("Process: " + process.getCode() + " in step " + getClass().getSimpleName());

		final ReturnRequestModel returnRequest = process.getReturnRequest();
		final List<PaymentTransactionModel> transactions = returnRequest.getOrder().getPaymentTransactions();

		if (transactions.isEmpty())
		{
			LOG.info("Unable to refund for ReturnRequest " + returnRequest.getCode() + ", no PaymentTransactions found");
			setReturnRequestStatus(returnRequest, ReturnStatus.PAYMENT_REVERSAL_FAILED);
			return Transition.NOK;
		}
		//This assumes that the Order only has one PaymentTransaction
		final PaymentTransactionModel transaction = transactions.get(0);

		final BigDecimal customRefundAmount = refundAmountCalculationService.getCustomRefundAmount(returnRequest);
		BigDecimal amountToRefund = null;

		if (customRefundAmount != null && customRefundAmount.compareTo(new BigDecimal(0)) > 0)
		{
			amountToRefund = customRefundAmount;
		}
		else
		{
			amountToRefund = refundAmountCalculationService.getOriginalRefundAmount(returnRequest);
		}

		Transition result;
		try
		{
			getPaymentService().refundFollowOn(transaction, amountToRefund);
			setReturnRequestStatus(returnRequest, ReturnStatus.PAYMENT_REVERSED);
			result = Transition.OK;
		}
		catch (final AdapterException e)
		{
			LOG.info("Unable to refund for ReturnRequest " + returnRequest.getCode() + ", exception ocurred: " + e.getMessage());
			setReturnRequestStatus(returnRequest, ReturnStatus.PAYMENT_REVERSAL_FAILED);
			result = Transition.NOK;
		}

		return result;
	}

	/**
	 * Update the return status for all return entries in {@link ReturnRequestModel}
	 *
	 * @param returnRequest
	 *           - the return request
	 * @param status
	 *           - the return status
	 */
	protected void setReturnRequestStatus(final ReturnRequestModel returnRequest, final ReturnStatus status)
	{
		returnRequest.setStatus(status);
		returnRequest.getReturnEntries().stream().forEach(entry -> {
			entry.setStatus(status);
			getModelService().save(entry);
		});
		getModelService().save(returnRequest);
	}

	protected PaymentService getPaymentService()
	{
		return paymentService;
	}

	@Required
	public void setPaymentService(final PaymentService paymentService)
	{
		this.paymentService = paymentService;
	}

	protected RefundAmountCalculationService getRefundAmountCalculationService()
	{
		return refundAmountCalculationService;
	}

	@Required
	public void setRefundAmountCalculationService(RefundAmountCalculationService refundAmountCalculationService)
	{
		this.refundAmountCalculationService = refundAmountCalculationService;
	}
}
