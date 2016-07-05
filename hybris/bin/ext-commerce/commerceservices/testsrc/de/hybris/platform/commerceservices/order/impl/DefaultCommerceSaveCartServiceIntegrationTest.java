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
package de.hybris.platform.commerceservices.order.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.commerceservices.order.CommerceSaveCartService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 *  Tests for functionality for retrieving saved carts.
 *  @See de.hybris.platform.commerceservices.order.CommerceSaveCartService#getSavedCartsForSiteAndUser
 */
@IntegrationTest
public class DefaultCommerceSaveCartServiceIntegrationTest extends ServicelayerTest
{
    private static final Logger LOG = Logger.getLogger(DefaultCommerceSaveCartServiceIntegrationTest.class);
    private static final String TEST_BASESITE_UID = "testSite";
    private static final String TEST_BASESITE2_UID = "testSite2";

    @Resource
    private BaseSiteService baseSiteService;

    @Resource
    private CommerceSaveCartService commerceSaveCartService;

    @Resource
    private CartService cartService;

    @Resource
    private UserService userService;

    @Before
    public void setUp() throws Exception
    {
        LOG.info("Creating data for commerce cart ..");
        userService.setCurrentUser(userService.getAdminUser());
        final long startTime = System.currentTimeMillis();
        new CoreBasicDataCreator().createEssentialData(Collections.EMPTY_MAP, null);
        // importing test csv
        importCsv("/commerceservices/test/testCommerceCart.csv", "utf-8");

        baseSiteService.setCurrentBaseSite(baseSiteService.getBaseSiteForUID(TEST_BASESITE_UID), false);

        LOG.info("Finished data for commerce cart " + (System.currentTimeMillis() - startTime) + "ms");
    }

    /**
     * Test loads used and his cart, saves it, then sets another basesite and saves session cart.
     *
     * @throws CommerceSaveCartException
     */
    @Test
    public void testGetSavedCards() throws CommerceSaveCartException
    {
        // load user and his cart.
        final UserModel abrode = userService.getUserForUID("abrode");
        final CommerceSaveCartParameter commerceSaveCartParameter = new CommerceSaveCartParameter();
        CartModel cartModel = abrode.getCarts().iterator().next();
        // it have no site by default
        cartModel.setSite(baseSiteService.getBaseSiteForUID(TEST_BASESITE_UID));
        commerceSaveCartParameter.setCart(cartModel);
        commerceSaveCartParameter.setName("Name");
        commerceSaveCartParameter.setDescription("Description");

        commerceSaveCartService.saveCart(commerceSaveCartParameter);

        final PageableData pd = new PageableData();
        pd.setCurrentPage(0);
        pd.setPageSize(10);
        final SearchPageData<CartModel> savedFirst = commerceSaveCartService.getSavedCartsForSiteAndUser(pd, null, abrode, null);
        assertEquals(savedFirst.getResults().size(), 1);

        // switching base site and updating session cart
        baseSiteService.setCurrentBaseSite(TEST_BASESITE2_UID, false);
        userService.setCurrentUser(abrode);
        commerceSaveCartParameter.setCart(cartService.getSessionCart());

        commerceSaveCartService.saveCart(commerceSaveCartParameter);

        final SearchPageData<CartModel> savedSecond = commerceSaveCartService.getSavedCartsForSiteAndUser(pd, null, abrode, null);
        assertEquals(savedSecond.getResults().size(), 2);

        final BaseSiteModel baseSite1 = baseSiteService.getBaseSiteForUID(TEST_BASESITE_UID);
        SearchPageData<CartModel> savedCartsForSiteAndUserFirst = commerceSaveCartService.getSavedCartsForSiteAndUser(pd, baseSite1, abrode, null);
        assertEquals(savedCartsForSiteAndUserFirst.getResults().size(), 1);

        final BaseSiteModel baseSite2 = baseSiteService.getBaseSiteForUID(TEST_BASESITE2_UID);
        SearchPageData<CartModel> savedCartsForSiteAndUserSecond = commerceSaveCartService.getSavedCartsForSiteAndUser(pd, baseSite2, abrode, null);
        assertEquals(savedCartsForSiteAndUserSecond.getResults().size(), 1);

        // they are not the same
        assertFalse(savedCartsForSiteAndUserSecond.getResults().get(0).getPk().equals(savedCartsForSiteAndUserFirst.getResults().get(0).getPk()));
    }
}
