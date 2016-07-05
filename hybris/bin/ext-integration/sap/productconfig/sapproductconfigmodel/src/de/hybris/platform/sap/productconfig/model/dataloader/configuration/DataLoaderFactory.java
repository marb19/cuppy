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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.sap.custdev.projects.fbs.slc.dataloader.settings.IDataloaderSource;


public class DataLoaderFactory
{
	/**
	 *
	 */
	public static class DataloaderSourceHandler implements InvocationHandler
	{
		private final DataloaderSource dlSource;

		public DataloaderSourceHandler(final DataloaderSource dlSource)
		{
			super();
			this.dlSource = dlSource;
		}


		@Override
		public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable
		{
			Object retObj;
			if ("getOutboundDestinationName".equals(method.getName()))
			{
				retObj = dlSource.getOutboundDestinationName();
			}
			else if ("setOutboundDestinationName".equals(method.getName()))
			{
				throw new IllegalArgumentException("method setOutboundDestinationName is not supported");
			}
			else
			{
				retObj = method.invoke(dlSource, args);
			}
			return retObj;

		}

	}

	private boolean isSSC24 = false;
	private Class<?> dlSourceExtIntf;

	public DataLoaderFactory()
	{
		isSSC24 = checkIfIDataloaderSourceExtensionInterfaceExists();
	}

	public boolean isSSC24()
	{
		return isSSC24;
	}

	private boolean checkIfIDataloaderSourceExtensionInterfaceExists()
	{
		boolean intfExists;
		try
		{
			dlSourceExtIntf = Class.forName("com.sap.custdev.projects.fbs.slc.dataloader.settings.IDataloaderSourceExtension");
			intfExists = true;

		}
		catch (final ClassNotFoundException e)
		{
			intfExists = false;
		}
		return intfExists;
	}

	public IDataloaderSource createDataLoaderSource(final DataloaderSourceParameters params)
	{

		IDataloaderSource source;

		if (isSSC24)
		{
			// create a dynamic Proxy implementing the IDataloaderSourceExtension Interface which only exists in SSC24
			final Class<?>[] interfaces = new Class<?>[1];
			interfaces[0] = dlSourceExtIntf;
			source = (IDataloaderSource) Proxy.newProxyInstance(dlSourceExtIntf.getClassLoader(), interfaces,
					new DataloaderSourceHandler(new DataloaderSource(params)));
		}
		else
		{
			source = new DataloaderSource(params);
		}



		return source;
	}


}
