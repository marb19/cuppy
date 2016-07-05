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
package ywebservicespackage.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.util.Assert;

import ywebservicespackage.data.AddressData;


public class SampleAddressPopulator implements Populator<AddressModel, AddressData>
{

	@Override
	public void populate(final AddressModel source, final AddressData target) throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setBillingAddress(getBoolean(source.getBillingAddress(), false));
		target.setDefaultAddress(getBoolean(source.getContactAddress(), false));
		target.setShippingAddress(getBoolean(source.getShippingAddress(), false));
		target.setStreetname(source.getStreetname());
		target.setStreetnumber(source.getStreetnumber());
		target.setTown(source.getTown());

		target.setFormattedAddress(getFormattedAddress(source));
	}

	private boolean getBoolean(final Boolean val, final boolean def)
	{
		return val != null ? val.booleanValue() : def;
	}

	private String getFormattedAddress(final AddressModel source)
	{
		return source.getTown() + " " + source.getStreetname() + " " + source.getStreetnumber();
	}
}
