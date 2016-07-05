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


import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.B2BADMINGROUP;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.B2BCUSTOMERGROUP;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.B2BGROUP;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.BUYER;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.CONTACT_ID;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.COSTOMERID;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.COUNTRY;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.DEFAULT_FEED;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.EXECUTIVEBOARD;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.FIRSTNAME;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.HEADOFPURCHASING;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.LASTNAME;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.OBJ_TYPE;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.PARENT;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.PHONE;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.RAW_HYBRIS_B2B_CONTACT;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.SAPCONSUMER_OUTBOUND_FEED;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.SESSION_LANGUAGE;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.TITLE;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.UID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hybris.datahub.core.rest.DataHubCommunicationException;
import com.hybris.datahub.core.rest.DataHubOutboundException;
import com.hybris.datahub.core.services.DataHubOutboundService;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.commerceservices.strategies.CustomerNameStrategy;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.AddressModel;

/**
 * Class to prepare the customer data and send the data to the Data Hub
 */
public class B2BContactExportService {
	private static final Logger LOGGER = Logger.getLogger(com.sap.hybris.sapcustomerb2b.outbound.B2BContactExportService.class.getName());

	private CustomerNameStrategy customerNameStrategy;
	private DataHubOutboundService dataHubOutboundService;
	private B2BUnitService<B2BUnitModel, ?> b2bUnitService;

	/**
	 * return Data Hub Outbound Service
	 * 
	 * @return dataHubOutboundService
	 */
	public DataHubOutboundService getDataHubOutboundService() {
		return dataHubOutboundService;
	}

	/**
	 * set Data Hub Outbound Service
	 * 
	 * @param dataHubOutboundService
	 */
	public void setDataHubOutboundService(final DataHubOutboundService dataHubOutboundService) {
		this.dataHubOutboundService = dataHubOutboundService;
	}

	protected void prepareAddressData(final AddressModel addressModel, final Map<String, Object> target)
	{
		final String countryIsoCode = addressModel.getCountry() != null ? addressModel.getCountry().getIsocode() : null;
		target.put(COUNTRY, countryIsoCode);
		target.put(PHONE, addressModel.getPhone1());
	}

	/**
	 * map B2B customer Model to the target map and send data to the Data Hub
	 * 
	 * @param changedB2bCustomerModel
	 */
	public void prepareAndSend(final B2BCustomerModel changedB2bCustomerModel,final AddressModel addressModel,boolean isNewContact,String language) {
		
		final List<Map<String, Object>> rawData = new ArrayList<>();
		if (changedB2bCustomerModel.getDefaultB2BUnit() == null) {
			return;
		}
		final B2BUnitModel rootUnit = b2bUnitService.getRootUnit(changedB2bCustomerModel.getDefaultB2BUnit());

			for (final PrincipalModel member : rootUnit.getMembers()) {
				if (member instanceof B2BCustomerModel) {
					final B2BCustomerModel b2bCustomerModel = (B2BCustomerModel) member;
					if (StringUtils.isNotEmpty(b2bCustomerModel.getUid()) && !(b2bCustomerModel.getUid().equalsIgnoreCase(changedB2bCustomerModel.getUid()))) {
							HashMap<String, Object> targetData = prepareB2BCustomerData(b2bCustomerModel,null,false,language);
							if (targetData!=null && !targetData.isEmpty()) { 
								rawData.add(targetData);			
							}
					}
								
				}
			}

		rawData.add(prepareB2BCustomerData(changedB2bCustomerModel,addressModel,isNewContact,language));
		
		// second send the data to Datahub in ONE block to ensure that auto compose and publish cannot occur in between  
		sendCustomerToDataHub(rawData);

	}

	
	protected void sendCustomerToDataHub(final List<Map<String, Object>> rawData)
	{
		if (rawData != null && !rawData.isEmpty())
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("The following values was send to Data Hub" + rawData + "(to the feed" + SAPCONSUMER_OUTBOUND_FEED + " into raw model "
						+ RAW_HYBRIS_B2B_CONTACT + ")");
			}
			try
			{
				dataHubOutboundService.sendToDataHub(SAPCONSUMER_OUTBOUND_FEED, RAW_HYBRIS_B2B_CONTACT, rawData);
			}
			catch (final DataHubOutboundException e)
			{
				LOGGER.warn("Error processing sending data to Data Hub. DataHubOutboundException: " + e.getMessage());
			}
			catch (final DataHubCommunicationException e)
			{
				LOGGER.warn("Error processing sending data to Data Hub. DataHubCommunicationException: " + e.getMessage());
			}
		}
		else
		{
			LOGGER.debug("No send to datahub occured because target is empty");
		}
	}

	protected HashMap<String, Object> prepareB2BCustomerData(final B2BCustomerModel b2bCustomerModel,final AddressModel addressModel ,boolean isNewContact, final String sessionLanguage) {
		HashMap<String, Object> target = new HashMap<String, Object>();
		final String[] names = customerNameStrategy.splitName(b2bCustomerModel.getName());
		final String titleCode = b2bCustomerModel.getTitle() != null ? b2bCustomerModel.getTitle().getCode() : null;

	
		B2BUnitModel parentB2BUnit = b2bCustomerModel.getDefaultB2BUnit();
		String parentB2BUnitUid = (parentB2BUnit != null) ? parentB2BUnit.getUid() : null;
		if(parentB2BUnitUid != null) {
			target.put(UID, b2bCustomerModel.getUid());
			target.put(COSTOMERID, b2bCustomerModel.getCustomerID());
			// For the new contact, the contact ID is empty but for data hub to process , we need to pass the unique id as contact ID so using email id as the unique id 
			target.put(CONTACT_ID, (isNewContact) ? b2bCustomerModel.getSapContactID()+"_"+b2bCustomerModel.getUid(): b2bCustomerModel.getSapContactID() );
			target.put(FIRSTNAME, names[0]);
			target.put(LASTNAME, names[1]);
			target.put(SESSION_LANGUAGE, sessionLanguage);
			target.put(TITLE, titleCode);
			target.put(OBJ_TYPE, "KNA1");	
			target.put(PARENT, parentB2BUnitUid);
			target.put(B2BGROUP,  getB2BContactFunction(b2bCustomerModel.getGroups()));			
			
			if (addressModel == null){
				target.put(COUNTRY, "");	
				target.put(PHONE,"");
			}
			else
			{
				prepareAddressData(addressModel, target);				
			}
			return target;
		}
		return null;
	}

	/*
	 * Get the Contact Function value for the corresponding group
	 */
	private String getB2BContactFunction(Set<PrincipalGroupModel> groups) {
		StringBuffer b2bGroups = new StringBuffer("");
		for (final PrincipalGroupModel group : groups) {
			b2bGroups.append(group.getUid() + ",");
		}
		if (b2bGroups.toString().contains(B2BADMINGROUP) && b2bGroups.toString().contains(B2BCUSTOMERGROUP)) {
			return HEADOFPURCHASING; 
		} else if (b2bGroups.toString().contains(B2BADMINGROUP)) {
			return EXECUTIVEBOARD;
		} else if (b2bGroups.toString().contains(B2BCUSTOMERGROUP)) {
			return BUYER;
		}
		return " ";
	}

	
	/**
	 * @return customerNameStrategy
	 */
	public CustomerNameStrategy getCustomerNameStrategy() {
		return customerNameStrategy;
	}

	/**
	 * @param customerNameStrategy
	 */
	public void setCustomerNameStrategy(final CustomerNameStrategy customerNameStrategy) {
		this.customerNameStrategy = customerNameStrategy;
	}

	/**
	 * @return B2BUnitService
	 */
	public B2BUnitService<B2BUnitModel, ?> getB2bUnitService() {
		return b2bUnitService;
	}

	/**
	 * set the B2B unit service
	 * 
	 * @param b2bUnitService
	 */
	public void setB2bUnitService(final B2BUnitService<B2BUnitModel, ?> b2bUnitService) {
		this.b2bUnitService = b2bUnitService;
	}

}
