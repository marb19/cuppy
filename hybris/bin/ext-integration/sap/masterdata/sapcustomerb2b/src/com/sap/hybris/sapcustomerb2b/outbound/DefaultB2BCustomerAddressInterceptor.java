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
package com.sap.hybris.sapcustomerb2b.outbound;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commercefacades.storesession.impl.DefaultStoreSessionFacade;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;


/**
 * If B2B customer has changes regarding name, title or session language trigger export of customer to Data Hub
 */
public class DefaultB2BCustomerAddressInterceptor implements ValidateInterceptor<AddressModel>
{

	private B2BContactExportService   b2bContactExportService;
	private DefaultStoreSessionFacade storeSessionFacade;
	@Override
	public void onValidate(final AddressModel addressModel, final InterceptorContext ctx) throws InterceptorException
	{
		B2BCustomerModel b2bCustomerModel = null;

		//get the customer related to the address
		final ItemModel owner = addressModel.getOwner();
		if (owner instanceof B2BCustomerModel)
		{
			b2bCustomerModel = (B2BCustomerModel) owner;


			// check if customer if is null or empty
			if (b2bCustomerModel.getCustomerID() == null || b2bCustomerModel.getCustomerID().isEmpty())
			{
				return;
			}
			// check if either name, title or sessionLanguage is modified

			if (b2bCustomerModel.getDefaultShipmentAddress()!=null && ctx.isModified(b2bCustomerModel.getDefaultShipmentAddress(), AddressModel.PHONE1))
			{
				final String sessionLanguage = getStoreSessionFacade().getCurrentLanguage() != null ? getStoreSessionFacade()
						.getCurrentLanguage().getIsocode() : "en";
				// We have to send all addresses of the B2BCustomer, so we reuse the B2BCustomerExportService
				getB2bContactExportService().prepareAndSend(b2bCustomerModel,b2bCustomerModel.getDefaultShipmentAddress(),true,sessionLanguage);
			}
		}
	}



	public B2BContactExportService getB2bContactExportService() {
		return b2bContactExportService;
	}

	public void setB2bContactExportService(
			B2BContactExportService b2bContactExportService) {
		this.b2bContactExportService = b2bContactExportService;
	}
	public DefaultStoreSessionFacade getStoreSessionFacade() {
		return storeSessionFacade;
	}
	public void setStoreSessionFacade(DefaultStoreSessionFacade storeSessionFacade) {
		this.storeSessionFacade = storeSessionFacade;
	}	

}
