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
describe('end-to-end Test for contextual menu service module', function() {

    beforeEach(function() {
        browser.get('smarteditcontainerJSTests/e2e/contextualMenu/contextualMenuTest.html');

    });

    it("Upon loading SmartEdit, contextualMenu named 'INFO' will be added to ComponentType1 and contextualMenu named 'DELETE' will be added to ComponentType2",
        function() {
            browser.switchToIFrame();

            //Assert on ComponentType1
            browser.click(by.id('component1'));
            expect(element(by.id('INFO-component1-componentType1-0')).isPresent()).toBe(true);
            expect(element(by.id('DELETE-component1-componentType1-0')).isPresent()).toBe(false);

            //Assert on ComponentType2
            browser.click(by.id('component2'));
            expect(element(by.id('INFO-component2-componentType2-0')).isPresent()).toBe(false);
            expect(element(by.id('DELETE-component2-componentType2-0')).isPresent()).toBe(true);

        });

});
