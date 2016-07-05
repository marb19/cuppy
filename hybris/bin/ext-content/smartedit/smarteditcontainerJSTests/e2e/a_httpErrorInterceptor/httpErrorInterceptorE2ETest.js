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
describe('HTTP Error Interceptor - ', function() {

    var smartEdit;

    beforeEach(function() {
        smartEdit = require("../utils/components/ConfigurationNavigator.js");

        browser.get('smarteditcontainerJSTests/e2e/a_httpErrorInterceptor/httpErrorInterceptorTest.html');
        browser.disableAnimations();
        browser.waitForWholeAppToBeReady();
    });

    it("GIVEN I'm in the SmartEdit container WHEN I trigger an AJAX request to an API AND I receive a failure response status THEN I expect to see a timed alert with the message ", function() {
        // GIVEN
        smartEdit.openConfigurationEditor();

        // WHEN
        smartEdit.setConfigurationValue(0, '\"new\"');
        smartEdit.clickSave();

        // THEN
        expect(alertMsg()).toBeDisplayed();
        expect(alertMsg().getText()).toBe('Your request could not be processed! Please try again later!');
    });

    it("GIVEN I'm in the SmartEdit application WHEN I trigger a AJAX request to an API AND I receive a failure response status THEN I expect to see a timed alert with the message", function() {
        // GIVEN
        browser.switchToIFrame();

        // WHEN
        storefrontComponentButton().click();

        // THEN
        browser.switchToParent();
        expect(alertMsg()).toBeDisplayed();
        expect(alertMsg().getText()).toBe('Your request could not be processed! Please try again later!');
    });

    // Helper Functions
    function alertMsg() {
        return element(by.binding('alert.message'));
    }

    function storefrontComponentButton() {
        return element(by.css('#submitButton'));
    }

});
