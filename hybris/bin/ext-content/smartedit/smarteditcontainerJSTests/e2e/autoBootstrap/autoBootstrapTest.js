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
describe('E2E Test for auto-loading of preview and auto-bootstrap of smartEdit ', function() {

    beforeEach(function() {
        browser.get('smarteditcontainerJSTests/e2e/autoBootstrap/autoBootstrapTest.html');
    });

    it("GIVEN that default page  is loaded, I click on the link to the second page THEN I see that text decorator is wrapped around my component", function() {


        browser.waitForWholeAppToBeReady();

        browser.switchToIFrame();

        browser.click(by.id("deepLink"));

        browser.waitForFrameToBeReady();

        browser.wait(function() {
            return element(by.cssContainingText('#component2', 'component2_Text_from_dummy_decorator'));
        }, 10000);

    });
});
