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
describe("GenericEditor permanent POST (case of preview)", function() {

    beforeEach(function() {
        require("../commonFunctions.js");
        browser.get('smarteditcontainerJSTests/e2e/genericEditor/componentPermanentPOST/genericEditorTest.html');
    });

    it("will retrieve a different ticketId everytime one presses submit", function() {

        expect(element(by.id('ticketId')).getText()).toBe('');

        element(by.name('description')).clear().sendKeys("some description");
        browser.click(by.id('submit'));

        element(by.id('ticketId')).getText().then(function(initialValue) {

            expect(initialValue).toBeDefined();
            expect(initialValue).not.toBe('');

            element(by.name('description')).clear().sendKeys("some other description");
            browser.click(by.id('submit'));
            expect(element(by.id('ticketId')).getText()).toBeDefined();
            expect(element(by.id('ticketId')).getText()).not.toBe('');
            expect(element(by.id('ticketId')).getText()).not.toBe(initialValue);
        });

    });
});
