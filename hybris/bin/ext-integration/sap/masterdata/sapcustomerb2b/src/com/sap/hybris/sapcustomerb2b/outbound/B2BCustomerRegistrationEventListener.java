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



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commercefacades.storesession.impl.DefaultStoreSessionFacade;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.sap.core.configuration.model.SAPConfigurationModel;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;


/**
 * Catch the register event and start the <code>sapCustomerPublishProcess</code> business process
 */
public class B2BCustomerRegistrationEventListener extends AbstractEventListener<B2BRegistrationEvent>
{
	private static final Logger LOGGER = Logger
			.getLogger(com.sap.hybris.sapcustomerb2b.outbound.B2BCustomerRegistrationEventListener.class.getName());

	private ModelService modelService;
	private BaseStoreService baseStoreService;
	private B2BContactExportService  b2bContactExportService;	
	private DefaultStoreSessionFacade storeSessionFacade;
	private PersistentKeyGenerator sapContactIdGenerator;

	
	/**
	 * @return businessProcessService
	 */
	public BusinessProcessService getBusinessProcessService()
	{
		return (BusinessProcessService) Registry.getApplicationContext().getBean("businessProcessService");
	}

	/**
	 * @return modelService
	 */
	protected ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * start the <code>sapCustomerPublishProcess</code> business process
	 * 
	 */
	@Override
	protected void onEvent(final B2BRegistrationEvent registerEvent)
	{
		// only if replication of user is requested start publishing to Data Hub process
		BaseStoreModel basestore = getBaseStoreService().getCurrentBaseStore();
		SAPConfigurationModel sapConfigurationModel =(basestore!=null)? basestore.getSAPConfiguration():null;
		boolean replicateb2bContact = (sapConfigurationModel!=null) ? sapConfigurationModel.getReplicateregisteredb2buser():false;
		B2BCustomerModel b2bCustomer = (B2BCustomerModel)registerEvent.getCustomer();
		if (replicateb2bContact && StringUtils.isEmpty(b2bCustomer.getSapContactID()))
		{
			AddressModel addressModel=null;
			boolean isNewContact = true;			
			final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			b2bCustomer.setSapReplicationInfo("Sent to datahub " + dateFormat.format(Calendar.getInstance().getTime()));		
			b2bCustomer.setSapContactID((String) getSapContactIdGenerator().generate());
			b2bCustomer.setCustomerID(b2bCustomer.getSapContactID());				
			b2bCustomer.setLoginDisabled(true);
			modelService.save(b2bCustomer);
			final String sessionLanguage = getStoreSessionFacade().getCurrentLanguage() != null ? getStoreSessionFacade()
					.getCurrentLanguage().getIsocode() : "en";
			getB2bContactExportService().prepareAndSend(b2bCustomer,addressModel,isNewContact,sessionLanguage);
			
		}
		else if (LOGGER.isDebugEnabled())
		{
			if (registerEvent != null && registerEvent.getCustomer() != null)
			{
				LOGGER.debug("During registration the B2B Contact " + registerEvent.getCustomer().getPk()
						+ " was not send to Data Hub. replicate register user not active");
			}
			else
			{
				LOGGER.debug("During registration no B2B Contact was send to Data Hub. replicate register user not active");
			}
		}
	}

	/**
	 * Create BusinessProcessService
	 * 
	 * @return StoreFrontCustomerProcessModel
	 */
	protected StoreFrontCustomerProcessModel createProcess()
	{
		return (StoreFrontCustomerProcessModel) getBusinessProcessService().createProcess(
				"customerPublishProcess" + System.currentTimeMillis(), "customerPublishProcess");
	}

	
	/**
	 * return Base store service instance
	 * 
	 * @return baseStoreService
	 */
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	/**
	 * Set Base Store Service instance
	 * 
	 * @param baseStoreService
	 */
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
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

	public PersistentKeyGenerator getSapContactIdGenerator() {
		return sapContactIdGenerator;
	}

	public void setSapContactIdGenerator(
			PersistentKeyGenerator sapContactIdGenerator) {
		this.sapContactIdGenerator = sapContactIdGenerator;
	}
	
	
	
}
