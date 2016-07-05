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
describe('E2E Test for decorator service module', function() {

    beforeEach(function() {
        browser.get('smarteditcontainerJSTests/e2e/decorator/decoratorTest.html');
    });

    it("Upon Clicking LoadPreview Button \"textDisplay\" decorator should be added to ComponentType1 " + "Assert for ComponentType1 \"textDisplay\" decorator is loaded , Assert \"buttonDisplay\" decorator is not added to ComponentType1",
        function() {

            browser.switchToIFrame();

            //Assert ComponentType1
            expect(element(by.model('component1')).getText()).toContain('test component 1');
            expect(element(by.model('component1')).getText()).toContain('Text_is_been_displayed_TextDisplayDecorator');
            expect(element(by.model('component1')).getText()).not.toContain('Button_is_been_displayed');

        });

    it("Upon Clicking LoadPreview Button \"ButtonDisplay\" decorator should be added to ComponentType2 " + "Assert for ComponentType2 \"ButtonDisplay\" decorator is loaded , Assert \"TextDisplay\" decorator is not added to ComponentType1",
        function() {

            browser.switchToIFrame();

            //Assert ComponentType2
            expect(element(by.model('component2')).getText()).toContain('test component 2');
            expect(element(by.model('component2')).getText()).toContain('Button_is_been_Displayed');
            expect(element(by.model('component2')).getText()).not.toContain('Text_is_been_displayed_TextDisplayDecorator');

        });

    it("Upon Clicking LoadPreview Button both \"textDisplay & buttonDisplay\" Plugin should be applied to ComponentType3 " + "Assert That componentType3 has both the decorator displayed ",
        function() {

            browser.switchToIFrame();

            //Assert ComponentType3
            expect(element(by.model('component3')).getText()).toContain('test component 3');
            expect(element(by.model('component3')).getText()).toContain('Text_is_been_displayed_TextDisplayDecorator');
            expect(element(by.model('component3')).getText()).toContain('Button_is_been_Displayed');

        });

});
