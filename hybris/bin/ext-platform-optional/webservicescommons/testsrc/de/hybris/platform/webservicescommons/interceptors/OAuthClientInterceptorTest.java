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
package de.hybris.platform.webservicescommons.interceptors;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.webservicescommons.constants.WebservicescommonsConstants;
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class OAuthClientInterceptorTest extends ServicelayerTransactionalTest
{
	private String clientId;

	@Resource
	private ModelService modelService;

	@Resource(name = "oauthClientDetailsInterceptor")
	private OAuthClientInterceptor oauthClientInterceptor;

	@Before
	public void setUp()
	{
		clientId = WebservicescommonsConstants.ANONYMOUS_CLIENT_ID;
	}

	private OAuthClientDetailsModel getAnonymousModel()
	{
		final OAuthClientDetailsModel model = modelService.create(OAuthClientDetailsModel.class);
		model.setClientId(clientId);
		modelService.save(model);
		return model;
	}

	private OAuthClientDetailsModel getAnyModel()
	{
		final OAuthClientDetailsModel model = modelService.create(OAuthClientDetailsModel.class);
		model.setClientId("test_anonymous_client");
		modelService.save(model);
		return model;
	}

	@Test
	public void testOnRemoveValid()
	{
		final OAuthClientDetailsModel model = getAnyModel();
		modelService.remove(model);
	}

	@Test
	public void testOnRemoveInvalid()
	{
		try
		{
			final OAuthClientDetailsModel model = getAnonymousModel();
			modelService.remove(model);
		}
		catch (final ModelRemovalException e)
		{
			Assert.assertTrue("Root cause should be of type InterceptorException but was " + e.getCause().getClass(),
					e.getCause() instanceof InterceptorException);
		}
	}

	@Test
	public void testChangeClientId()
	{
		try
		{
			//given
			final OAuthClientDetailsModel model = getAnyModel();
			model.setClientId("other_test_anonymous_client");

			//when
			modelService.save(model);
		}
		catch (final ModelSavingException e)
		{
			//then
			Assert.assertTrue("Root cause should be of type JaloInvalidParameterException but was " + e.getCause().getClass(),
					e.getCause() instanceof JaloInvalidParameterException);
		}
	}

	@Test
	public void testChangeClientIdForAnonymousClient()
	{
		try
		{
			//given
			final OAuthClientDetailsModel model = getAnonymousModel();
			model.setClientId("other_test_anonymous_client");

			//when
			modelService.save(model);
		}
		catch (final ModelSavingException e)
		{
			//then
			Assert.assertTrue("Root cause should be of type JaloInvalidParameterException but was " + e.getCause().getClass(),
					e.getCause() instanceof JaloInvalidParameterException);
		}
	}

	@Test
	public void testChangeClientSecretForAnonymousClient()
	{
		try
		{
			//given
			final OAuthClientDetailsModel model = getAnonymousModel();
			model.setClientSecret("newSecret");

			//when
			modelService.save(model);
		}
		catch (final ModelSavingException e)
		{
			//then
			Assert.assertTrue("Root cause should be of type JaloInvalidParameterException but was " + e.getCause().getClass(),
					e.getCause() instanceof JaloInvalidParameterException);
		}
	}

	@Test
	public void testSetNullClientSecretForAnonymousClient()
	{
		//given
		final OAuthClientDetailsModel model = getAnonymousModel();
		model.setClientSecret(null);

		//when
		modelService.save(model);

		//then
		Assert.assertNull(model.getClientSecret());
	}

	@Test
	public void testEncodeClientSecret()
	{
		//given
		final String secret = "newSecret";
		final OAuthClientDetailsModel model = getAnyModel();
		model.setClientSecret(secret);

		//when
		modelService.save(model);

		//then
		final String encodedPassword = model.getClientSecret();
		Assert.assertTrue(oauthClientInterceptor.getClientSecretEncoder().matches(secret, encodedPassword));
	}

	@Test
	public void testSetNullClientSecret()
	{
		//given
		final OAuthClientDetailsModel model = getAnyModel();
		model.setClientSecret(null);

		//when
		modelService.save(model);

		//then
		Assert.assertNull(model.getClientSecret());
	}

}
