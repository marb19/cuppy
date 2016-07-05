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

package de.hybris.platform.chinesetaxinvoiceservices.daos;

import de.hybris.platform.chinesetaxinvoiceservices.model.TaxInvoiceModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;

import java.util.List;


public interface TaxInvoiceDao extends Dao
{

	/**
	 * Query by PK.
	 *
	 * @param code
	 *           PK
	 * @return TaxInvoiceModel
	 */
	TaxInvoiceModel findInvoiceByCode(String code);

	/**
	 * Query by serialCode
	 *
	 * @param serialCode
	 *           SerialCode of TaxInvoiceModel.
	 * @return TaxInvoiceModel
	 */
	TaxInvoiceModel findInvoiceBySerialCode(String serialCode);

	/**
	 * Query by order.
	 *
	 * @param orderCode
	 *           OrderCode of an order.
	 * @return TaxInvoiceModel
	 */
	TaxInvoiceModel findInvoiceByOrder(String orderCode);

	/**
	 * Query all TaxInvoiceModel of a Customer.
	 *
	 * @param customer
	 * @return List<TaxInvoiceModel>
	 */
	List<TaxInvoiceModel> findInvoicesByCustomer(CustomerModel customer);
}
