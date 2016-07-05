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
package de.hybris.platform.ordermanagementwebservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;
import de.hybris.platform.ordermanagementwebservices.constants.OrdermanagementwebservicesConstants;

@SuppressWarnings("PMD")
public class OrdermanagementwebservicesManager extends GeneratedOrdermanagementwebservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( OrdermanagementwebservicesManager.class.getName() );
	
	public static final OrdermanagementwebservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (OrdermanagementwebservicesManager) em.getExtension(OrdermanagementwebservicesConstants.EXTENSIONNAME);
	}
	
}
