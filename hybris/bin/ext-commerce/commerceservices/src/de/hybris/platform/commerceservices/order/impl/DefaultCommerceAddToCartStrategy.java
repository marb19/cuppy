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
package de.hybris.platform.commerceservices.order.impl;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.storelocator.model.PointOfServiceModel;


public class DefaultCommerceAddToCartStrategy extends AbstractCommerceAddToCartStrategy
{
	/**
	 * Adds an item to the cart for pickup in a given location
	 *
	 * @param parameter
	 *           Cart parameters
	 * @return Cart modification information
	 * @throws de.hybris.platform.commerceservices.order.CommerceCartModificationException
	 *
	 */
	@Override
	public CommerceCartModification addToCart(final CommerceCartParameter parameter) throws CommerceCartModificationException
	{
		CommerceCartModification modification;

		final CartModel cartModel = parameter.getCart();
		final ProductModel productModel = parameter.getProduct();
		final long quantityToAdd = parameter.getQuantity();
		final PointOfServiceModel deliveryPointOfService = parameter.getPointOfService();

		this.beforeAddToCart(parameter);
		validateAddToCart(parameter);

		if (isProductForCode(parameter).booleanValue())
		{
			// So now work out what the maximum allowed to be added is (note that this may be negative!)
			final long actualAllowedQuantityChange = getAllowedCartAdjustmentForProduct(cartModel, productModel, quantityToAdd,
					deliveryPointOfService);
			final Integer maxOrderQuantity = productModel.getMaxOrderQuantity();
			final long cartLevel = checkCartLevel(productModel, cartModel, deliveryPointOfService);
			final long cartLevelAfterQuantityChange = actualAllowedQuantityChange + cartLevel;

			if (actualAllowedQuantityChange > 0)
			{
				// We are allowed to add items to the cart
				final CartEntryModel entryModel = addCartEntry(parameter, actualAllowedQuantityChange);

				final String statusCode = getStatusCodeAllowedQuantityChange(actualAllowedQuantityChange, maxOrderQuantity,
						quantityToAdd, cartLevelAfterQuantityChange);

				modification = createAddToCartResp(parameter, statusCode, entryModel, actualAllowedQuantityChange);
			}
			else
			{
				// Not allowed to add any quantity, or maybe even asked to reduce the quantity
				// Do nothing!
				final String status = getStatusCodeForNotAllowedQuantityChange(maxOrderQuantity, maxOrderQuantity);

				modification = createAddToCartResp(parameter, status, createEmptyCartEntry(parameter), 0);

			}
		}
		else
		{
			modification = createAddToCartResp(parameter, CommerceCartModificationStatus.UNAVAILABLE,
					createEmptyCartEntry(parameter), 0);
		}

		afterAddToCart(parameter, modification);

		return modification;
	}

	private Boolean isProductForCode(final CommerceCartParameter parameter)
	{

		final ProductModel productModel = parameter.getProduct();
		try
		{
			getProductService().getProductForCode(productModel.getCode());
		}
		catch (final UnknownIdentifierException e)
		{
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	private CommerceCartModification createAddToCartResp(final CommerceCartParameter parameter, final String status,
			final CartEntryModel entry, final long quantityAdded)
	{
		final long quantityToAdd = parameter.getQuantity();

		final CommerceCartModification modification = new CommerceCartModification();
		modification.setStatusCode(status);
		modification.setQuantityAdded(quantityAdded);
		modification.setQuantity(quantityToAdd);

		modification.setEntry(entry);

		return modification;
	}

	private UnitModel getUnit(final CommerceCartParameter parameter) throws CommerceCartModificationException
	{
		final ProductModel productModel = parameter.getProduct();
		try
		{
			return getProductService().getOrderableUnit(productModel);
		}
		catch (final ModelNotFoundException e)
		{
			throw new CommerceCartModificationException(e.getMessage(), e);
		}
	}

	private CartEntryModel addCartEntry(final CommerceCartParameter parameter, final long actualAllowedQuantityChange)
			throws CommerceCartModificationException
	{

		final CartModel cartModel = parameter.getCart();
		final ProductModel productModel = parameter.getProduct();
		final PointOfServiceModel deliveryPointOfService = parameter.getPointOfService();
		final UnitModel unit = parameter.getUnit();

		final boolean forceNewEntry = parameter.isCreateNewEntry();

		final UnitModel orderableUnit = (unit != null ? unit : getUnit(parameter));

		// We are allowed to add items to the cart
		CartEntryModel cartEntryModel;

		if (deliveryPointOfService == null)
		{
			// Modify the cart
			cartEntryModel = getCartService().addNewEntry(cartModel, productModel, actualAllowedQuantityChange, orderableUnit,
					APPEND_AS_LAST, !forceNewEntry);
		}
		else
		{
			// Find the entry to modify
			final Integer entryNumber = getEntryForProductAndPointOfService(cartModel, productModel, deliveryPointOfService);

			// Modify the cart
			cartEntryModel = getCartService().addNewEntry(cartModel, productModel, actualAllowedQuantityChange, orderableUnit,
					entryNumber.intValue(), (entryNumber.intValue() < 0) ? false : !forceNewEntry);

			if (cartEntryModel != null)
			{
				cartEntryModel.setDeliveryPointOfService(deliveryPointOfService);
			}
		}

		getModelService().save(cartEntryModel);
		getCommerceCartCalculationStrategy().calculateCart(cartModel);
		getModelService().save(cartEntryModel);

		return cartEntryModel;
	}

	private String getStatusCodeAllowedQuantityChange(final long actualAllowedQuantityChange, final Integer maxOrderQuantity,
			final long quantityToAdd, final long cartLevelAfterQuantityChange)
	{
		// Are we able to add the quantity we requested?
		if (isMaxOrderQuantitySet(maxOrderQuantity) && (actualAllowedQuantityChange < quantityToAdd)
				&& (cartLevelAfterQuantityChange == maxOrderQuantity.longValue()))
		{
			return CommerceCartModificationStatus.MAX_ORDER_QUANTITY_EXCEEDED;
		}
		else if (actualAllowedQuantityChange == quantityToAdd)
		{
			return CommerceCartModificationStatus.SUCCESS;
		}
		else
		{
			return CommerceCartModificationStatus.LOW_STOCK;
		}
	}

	private String getStatusCodeForNotAllowedQuantityChange(final Integer maxOrderQuantity,
			final Integer cartLevelAfterQuantityChange)
	{

		if (isMaxOrderQuantitySet(maxOrderQuantity) && (cartLevelAfterQuantityChange.longValue() == maxOrderQuantity.longValue()))
		{
			return CommerceCartModificationStatus.MAX_ORDER_QUANTITY_EXCEEDED;
		}
		else
		{
			return CommerceCartModificationStatus.NO_STOCK;
		}
	}

	private CartEntryModel createEmptyCartEntry(final CommerceCartParameter parameter)
	{

		final ProductModel productModel = parameter.getProduct();
		final PointOfServiceModel deliveryPointOfService = parameter.getPointOfService();

		final CartEntryModel entry = new CartEntryModel();
		entry.setProduct(productModel);
		entry.setDeliveryPointOfService(deliveryPointOfService);

		return entry;
	}
}