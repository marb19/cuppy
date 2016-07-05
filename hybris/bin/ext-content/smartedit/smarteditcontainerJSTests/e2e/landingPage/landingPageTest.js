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

describe('Landing page - ', function() {

    var page;

    beforeEach(function() {
        page = require('../utils/components/LandingPageNavigator.js');

        browser.get('smarteditcontainerJSTests/e2e/landingPage/landingPageTest.html');
        browser.waitForContainerToBeReady();
        browser.disableAnimations();
    });

    it('GIVEN I am on the landing page WHEN the page is fully loaded THEN I expect to see the first four catalogs', function() {
        // THEN
        expect(page.catalogs().count()).toEqual(4);
    });

    it('GIVEN I am on the landing page WHEN I click on the "2" link of the pagination THEN I expect to see the remaining catalogs', function() {
        // WHEN
        page.catalogPageNumber(2).click();

        // THEN
        expect(page.catalogs().count()).toEqual(1);
    });

    it('GIVEN I am on the landing page WHEN I click on the first of the catalogs THEN I will be redirected to its storefront', function() {
        // WHEN
        page.firstCatalog().click();
        browser.waitForWholeAppToBeReady();
        browser.waitForUrlToMatch(/\/storefront/);

        // THEN
        expect(browser.getCurrentUrl()).toContain('/storefront');
        expect(page.experienceSelectorWidget().getText()).toBe('Apparel UK Content Catalog - Online | English');
    });

    it('GIVEN I am on a store front WHEN I click on the burger menu and the SITES link THEN I will be redirected to the landing page', function() {
        // WHEN
        page.firstCatalog().click();
        browser.waitForWholeAppToBeReady();
        page.leftMenuButton().click();
        page.sitesButton().click();
        browser.waitForContainerToBeReady();
        browser.waitForUrlToMatch(/^(?!.*storefront)/);

        // THEN
        expect(browser.getCurrentUrl()).not.toContain('/storefront');
        expect(page.catalogs().count()).toBe(4);
    });

    it('GIVEN I am on the landing page and inflection point icon should not be visible on this page', function() {
        // THEN
        expect(page.inflectionPointSelector()).not.toBeDisplayed();
    });


});
