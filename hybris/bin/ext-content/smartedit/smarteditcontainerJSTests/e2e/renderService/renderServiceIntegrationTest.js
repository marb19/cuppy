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
describe('Integratation of render service', function() {

    beforeEach(function() {
        browser.get('smarteditcontainerJSTests/e2e/renderService/index.html');
    });

    it('from inner frame will replace target with custom content in inner frame', function() {
        browser.waitForContainerToBeReady();
        browser.switchToIFrame();

        var originalContent;
        var expectedContent = "New Content From Inside";

        element(by.css(".smartEditComponent[data-smartedit-component-id='smartEditComponent2']")).getAttribute("innerHTML")
            .then(function(value) {
                originalContent = value;
                return browser.click(by.css('#renderButtonInner'));
            })
            .then(function() {
                return element(by.css("[data-smartedit-component-id='smartEditComponent2'] span")).getAttribute("innerHTML");
            })
            .then(function(value) {
                expect(value).not.toEqual(originalContent);
                expect(value).toEqual(expectedContent);
            });
    });

    it('from outer frame will replace target with custom content in inner frame', function() {
        browser.waitForContainerToBeReady();
        browser.switchToIFrame();

        var originalContent = element(by.css("[data-smartedit-component-id='smartEditComponent1']")).text;
        var expectedContent = "<div data-ng-transclude=\"\" class=\"ng-scope\"><span class=\"ng-scope\">New Content From Outside</span></div>";

        element(by.css(".smartEditComponent[data-smartedit-component-id='smartEditComponent1']")).getAttribute("innerHTML")
            .then(function(value) {
                originalContent = value;
                browser.switchToParent();
                return browser.click(by.css('#renderButtonOuter'));
            })
            .then(function() {
                browser.switchToIFrame();
                return element(by.css("[data-smartedit-component-id='smartEditComponent1']")).getAttribute("innerHTML");
            })
            .then(function(value) {
                expect(value).not.toEqual(originalContent);
                expect(value).toEqual(expectedContent);
            });
    });

});
