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
describe("Test authorization directives", function() {


    beforeEach(function() {
        browser.get('smarteditcontainerJSTests/e2e/authorizations');
    });


    it("when I have permissions for a component on a screen then the protected element is present", function() {
        expect(element(by.id('elementWithPermission')).isPresent()).toBe(true);
    });

    it("when I do not have permissions for a component on a screen then the protected element is not present", function() {
        expect(element(by.id('elementWithoutPermission')).isPresent()).toBe(false);
    });

    it("given that I do not have a permission for a component, when I change to a component that I do have permission then the protected element becomes present", function() {

        expect(element(by.id('elementWithoutPermission')).isPresent()).toBe(false);
        browser.click(by.id('toggle'));
        expect(element(by.id('elementWithoutPermission')).isPresent()).toBe(true);
    });

    it("when a permission is not configured then by default I do not have permission and the protected element is not present", function() {
        expect(element(by.id('elementWithoutDeletePermission')).isPresent()).toBe(false);
    });

});
