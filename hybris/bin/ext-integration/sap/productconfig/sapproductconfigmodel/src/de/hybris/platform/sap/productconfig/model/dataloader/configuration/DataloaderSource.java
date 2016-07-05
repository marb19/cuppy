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
package de.hybris.platform.sap.productconfig.model.dataloader.configuration;

import com.sap.custdev.projects.fbs.slc.dataloader.settings.IClientSetting;
import com.sap.custdev.projects.fbs.slc.dataloader.settings.IDataloaderSource;
import com.sap.custdev.projects.fbs.slc.dataloader.settings.IEccSetting;


public class DataloaderSource implements IDataloaderSource
{
	private final boolean loadBalanced;
	private final String sid;
	private final String messageServer;
	private String group;
	private final String rfcDestination;
	private final ClientSettings clientSetting;
	private final String outboundDestinationName;

	public DataloaderSource(final DataloaderSourceParameters params)
	{
		this.loadBalanced = params.isUseLoadBalance();
		this.clientSetting = new ClientSettings(params.getClient(), params.getUser(), params.getPassword());
		if (loadBalanced)
		{
			this.sid = params.getSysId();
			this.messageServer = params.getMsgServer();
			this.group = params.getLogonGroup();
		}
		else
		{
			this.sid = params.getInstanceno();
			this.messageServer = params.getTargetHost();
		}
		this.rfcDestination = params.getClientRfcDestination();
		this.outboundDestinationName = params.getServerRfcDestination();
	}


	@Override
	public IClientSetting getClientSetting()
	{
		return clientSetting;
	}

	@Override
	public IEccSetting getEccSetting()
	{
		final IEccSetting eccSetting = new IEccSetting()
		{

			@Override
			public boolean isLoadBalanced()
			{
				return loadBalanced;
			}

			@Override
			public String getSid()
			{
				return sid;
			}

			@Override
			public String getMessageServer()
			{
				return messageServer;
			}

			@Override
			public String getGroup()
			{
				return group;
			}
		};

		return eccSetting;
	}

	@Override
	public String getRfcDestination()
	{
		return rfcDestination;
	}



	public String getOutboundDestinationName()
	{
		return outboundDestinationName;
	}

}
