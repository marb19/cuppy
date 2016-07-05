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
package de.hybris.cmssmartedit.jalo;

import de.hybris.cmssmartedit.constants.CmsSmarteditConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class CmsSmarteditManager extends GeneratedCmsSmarteditManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger( CmsSmarteditManager.class.getName() );
	
	public static final CmsSmarteditManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (CmsSmarteditManager) em.getExtension(CmsSmarteditConstants.EXTENSIONNAME);
	}
	
}
