/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 */
package de.hybris.platform.chinesepaymentaddon.constants;



public interface ControllerConstants
{

	interface Views
	{
		String _AddonPrefix = "addon:/chinesepaymentaddon/";

		interface Pages
		{

			interface Checkout
			{
				String CheckoutConfirmationPage = _AddonPrefix + "pages/checkout/checkoutConfirmationPage";
			}

			interface MultiStepCheckout
			{
				String AddEditDeliveryAddressPage = _AddonPrefix + "pages/checkout/multi/addEditDeliveryAddressPage";
				String ChooseDeliveryMethodPage = "pages/checkout/multi/chooseDeliveryMethodPage";
				String ChoosePickupLocationPage = "pages/checkout/multi/choosePickupLocationPage";
				String AddPaymentMethodPage = _AddonPrefix + "pages/checkout/multi/addPaymentMethodPage";
				String CheckoutSummaryPage = _AddonPrefix + "pages/checkout/multi/checkoutSummaryPage";
				String HostedOrderPageErrorPage = _AddonPrefix + "pages/checkout/multi/hostedOrderPageErrorPage";
				String HostedOrderPostPage = _AddonPrefix + "pages/checkout/multi/hostedOrderPostPage";
				String SilentOrderPostPage = _AddonPrefix + "pages/checkout/multi/silentOrderPostPage";
				String GiftWrapPage = "pages/checkout/multi/giftWrapPage";
				String HopPaymentPage = _AddonPrefix + "pages/checkout/multi/hopPaymentPage";
				String PaymentFailedPage = _AddonPrefix + "pages/checkout/multi/paymentFailedPage";
				String HopReturnPage = _AddonPrefix + "pages/checkout/multi/hopReturnPage";
			}
		}
	}
}
