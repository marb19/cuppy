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

import static com.sap.hybris.sapcustomerb2b.CustomerB2BConstantsUtils.B2BCUSTOMER_B2BUNIT;
import static com.sap.hybris.sapcustomerb2b.CustomerB2BConstantsUtils.B2BCUSTOMER_EMAIL;
import static com.sap.hybris.sapcustomerb2b.CustomerB2BConstantsUtils.B2BCUSTOMER_FIRST_NAME;
import static com.sap.hybris.sapcustomerb2b.CustomerB2BConstantsUtils.B2BCUSTOMER_LAST_NAME;
import static com.sap.hybris.sapcustomerb2b.CustomerB2BConstantsUtils.B2BCUSTOMER_NAME;
import static com.sap.hybris.sapcustomerb2b.CustomerB2BConstantsUtils.B2BCUSTOMER_SESSION_LANGUAGE;
import static com.sap.hybris.sapcustomerb2b.CustomerB2BConstantsUtils.B2BCUSTOMER_TITLE_CODE;
import static com.sap.hybris.sapcustomerb2b.CustomerB2BConstantsUtils.CUSTOMER_ID;
import static com.sap.hybris.sapcustomerb2b.CustomerB2BConstantsUtils.KEY_CUSTOMER_ID;
import static com.sap.hybris.sapcustomerb2b.CustomerB2BConstantsUtils.KEY_FIRST_NAME;
import static com.sap.hybris.sapcustomerb2b.CustomerB2BConstantsUtils.KEY_LAST_NAME;
import static com.sap.hybris.sapcustomerb2b.CustomerB2BConstantsUtils.KEY_SESSION_LANGUAGE;
import static com.sap.hybris.sapcustomerb2b.CustomerB2BConstantsUtils.KEY_TITLE;
import static com.sap.hybris.sapcustomerb2b.CustomerB2BConstantsUtils.KEY_UID;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.B2BADMINGROUP;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.B2BCUSTOMERGROUP;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.B2BGROUP;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.BUYER;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.CONTACT_ID;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.COSTOMERID;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.COUNTRY;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.COUNTRY_DE;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.DEFAULT_FEED;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.EXECUTIVEBOARD;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.FIRSTNAME;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.HEADOFPURCHASING;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.LASTNAME;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.OBJ_TYPE;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.PARENT;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.PHONE;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.RAW_HYBRIS_B2B_CONTACT;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.RAW_HYBRIS_B2B_CUSTOMER;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.SESSION_LANGUAGE;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.TITLE;
import static com.sap.hybris.sapcustomerb2b.constants.Sapcustomerb2bConstants.UID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.strategies.CustomerNameStrategy;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.TitleModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.hybris.datahub.core.rest.DataHubCommunicationException;
import com.hybris.datahub.core.rest.DataHubOutboundException;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.commerceservices.strategies.CustomerNameStrategy;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.hybris.datahub.core.services.DataHubOutboundService;
import com.sap.hybris.sapcustomerb2b.outbound.*;

@UnitTest
public class B2BContactExportServiceTest {

	@InjectMocks
	private final B2BContactExportService b2BContactExportService = new B2BContactExportService();

	@Mock
	private final CustomerNameStrategy customerNameStrategy = mock(CustomerNameStrategy.class);

	@Mock
	private final B2BCustomerModel b2bCustomerModel = mock(B2BCustomerModel.class);

	@Mock
	private final DataHubOutboundService dataHubOutboundService = mock(DataHubOutboundService.class);

	@Test
	public void testSendB2BContactData() throws InterceptorException {
		// given
		mockBaseCustomerData();

		final List<Map<String, Object>> b2bContactData = new ArrayList<>();
		AddressModel addressModel = new AddressModel();
		CountryModel countryModel = new CountryModel();
		countryModel.setName("DE", new Locale("de"));
		addressModel.setCountry(countryModel);
		addressModel.setPhone1("123456789");
		b2bContactData.add(b2BContactExportService.prepareB2BCustomerData(b2bCustomerModel, addressModel, false, "de"));
		b2BContactExportService.setDataHubOutboundService(dataHubOutboundService);
		b2BContactExportService.sendCustomerToDataHub(b2bContactData);
		try {
			verify(dataHubOutboundService, times(1)).sendToDataHub("SAPCONSUMER_OUTBOUND_FEED", RAW_HYBRIS_B2B_CONTACT, b2bContactData);
		} catch (final DataHubOutboundException e) {
			fail("Error processing sending data to Data Hub. DataHubOutboundException: " + e.getMessage());
		} catch (final DataHubCommunicationException e) {
			fail("Error processing sending data to Data Hub. DataHubCommunicationException: " + e.getMessage());
		}
	}

	@Test
	public void testNotSendEmptyB2BContactDataNull() throws InterceptorException {
		// given
		final List<Map<String, Object>> b2bContactData = null;
		b2BContactExportService.sendCustomerToDataHub(b2bContactData);

		try {
			verify(dataHubOutboundService, times(0)).sendToDataHub("DEFAULT_FEED", RAW_HYBRIS_B2B_CONTACT, b2bContactData);
		} catch (final DataHubOutboundException e) {
			fail("Error processing sending data to Data Hub. DataHubOutboundException: " + e.getMessage());
		} catch (final DataHubCommunicationException e) {
			fail("Error processing sending data to Data Hub. DataHubCommunicationException: " + e.getMessage());
		}
	}

	@Test
	public void testPrepareB2BContactDataNoLnaguage() throws InterceptorException {
		mockBaseCustomerData();
		final TitleModel title = mock(TitleModel.class);
		given(title.getCode()).willReturn(B2BCUSTOMER_TITLE_CODE);
		given(b2bCustomerModel.getTitle()).willReturn(title);
		AddressModel addressModel = new AddressModel();
		CountryModel countryModel = new CountryModel();
		countryModel.setName("DE", new Locale("de"));
		addressModel.setCountry(countryModel);
		addressModel.setPhone1("123456789");

		final Map<String, Object> b2bCustomerData = b2BContactExportService.prepareB2BCustomerData(b2bCustomerModel, addressModel, false, "de");
		checkBaseCustomerData(b2bCustomerData);
		Assert.assertEquals(b2bCustomerData.get(KEY_TITLE), B2BCUSTOMER_TITLE_CODE);
		Assert.assertEquals(b2bCustomerData.get(KEY_SESSION_LANGUAGE), "de");
	}

	@Test
	public void testPrepareB2BCustomerDataNoLnaguageNoTitleCode() throws InterceptorException {
		// given
		mockBaseCustomerData();
		AddressModel addressModel = new AddressModel();
		CountryModel countryModel = new CountryModel();
		countryModel.setName("DE", new Locale("de"));
		addressModel.setCountry(countryModel);
		addressModel.setPhone1("123456789");

		final Map<String, Object> b2bCustomerData = b2BContactExportService.prepareB2BCustomerData(b2bCustomerModel, addressModel, false, "de");
		checkBaseCustomerData(b2bCustomerData);
		Assert.assertEquals(b2bCustomerData.get(KEY_TITLE), null);
		Assert.assertEquals(b2bCustomerData.get(KEY_SESSION_LANGUAGE), "de");
	}

	@Test
	public void testPrepareB2BContactData() throws InterceptorException {
		// given
		mockBaseCustomerData();
		final TitleModel title = mock(TitleModel.class);
		given(title.getCode()).willReturn(B2BCUSTOMER_TITLE_CODE);
		given(b2bCustomerModel.getTitle()).willReturn(title);

		final LanguageModel languageModel = mock(LanguageModel.class);
		given(languageModel.getIsocode()).willReturn(B2BCUSTOMER_SESSION_LANGUAGE);
		given(b2bCustomerModel.getSessionLanguage()).willReturn(languageModel);


		final Map<String, Object> b2bCustomerData = b2BContactExportService.prepareB2BCustomerData(b2bCustomerModel, null, false, "de");
		checkBaseCustomerData(b2bCustomerData);
		Assert.assertEquals(b2bCustomerData.get(KEY_SESSION_LANGUAGE), B2BCUSTOMER_SESSION_LANGUAGE);
		Assert.assertEquals(b2bCustomerData.get(KEY_TITLE), B2BCUSTOMER_TITLE_CODE);
	}

	protected void mockBaseCustomerData() {
		final B2BUnitModel b2bUnit = mock(B2BUnitModel.class);
		given(b2bCustomerModel.getDefaultB2BUnit()).willReturn(b2bUnit);
		given(b2bCustomerModel.getDefaultB2BUnit().getUid()).willReturn(B2BCUSTOMER_B2BUNIT);
		given(b2bCustomerModel.getUid()).willReturn(B2BCUSTOMER_EMAIL);
		given(b2bCustomerModel.getName()).willReturn(B2BCUSTOMER_NAME);
		final String[] names = new String[] { B2BCUSTOMER_FIRST_NAME, B2BCUSTOMER_LAST_NAME };
		given(customerNameStrategy.splitName(b2bCustomerModel.getName())).willReturn(names);
		b2BContactExportService.setCustomerNameStrategy(customerNameStrategy);
		given(b2bCustomerModel.getCustomerID()).willReturn(CUSTOMER_ID);
	}

	protected void checkBaseCustomerData(final Map<String, Object> b2bContactData) {
		Assert.assertFalse(b2bContactData.isEmpty());
		Assert.assertEquals(b2bContactData.get(KEY_UID), B2BCUSTOMER_EMAIL);
		Assert.assertEquals(b2bContactData.get(KEY_CUSTOMER_ID), CUSTOMER_ID);
		Assert.assertEquals(b2bContactData.get(KEY_FIRST_NAME), B2BCUSTOMER_FIRST_NAME);
		Assert.assertEquals(b2bContactData.get(KEY_LAST_NAME), B2BCUSTOMER_LAST_NAME);
	}

}
