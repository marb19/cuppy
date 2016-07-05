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
var itemMechanismPath = 'smarteditcontainerJSTests/e2e/toolbars/itemMechanism';
var itemMechanismIconPath = '../../' + itemMechanismPath;

describe("Configure toolbar through outer toolbarservice", function() {

    beforeEach(function() {
        browser.get(itemMechanismPath);
    });

    it("items of type 'ACTION' and 'HYBRID_ACTION' will be added", function() {
        expect(element.all(by.css('li.yToolbarBtn img')).count()).toBe(0);
        browser.click(by.id('sendActionsOuter'));
        expect(element.all(by.css('li.yToolbarBtn img')).count()).toBe(2);

        expect(element(by.id('toolbar_option_0')).getAttribute('alt')).toBe('action5');
        expect(element(by.id('toolbar_option_0')).getAttribute('data-ng-src')).toBe(itemMechanismIconPath + '/icon5.png');
        expect(element(by.id('toolbar_option_1')).getAttribute('alt')).toBe('action6');
        expect(element(by.id('toolbar_option_1')).getAttribute('data-ng-src')).toBe(itemMechanismIconPath + '/icon6.png');
    });


    it("item of type 'HYBRID_ACTION' will display its template", function() {
        expect(element.all(by.css('li.yToolbarBtn img')).count()).toBe(0);
        browser.click(by.id('sendActionsOuter'));
        expect(element.all(by.css('li.yToolbarBtn img')).count()).toBe(2);

        expect(element(by.id('hybridActiontemplate')).getText()).toBe('HYBRID ACTION TEMPLATE');
    });


    it("Callbacks will be executed successfully when items of type 'ACTION' and 'HYBRID_ACTION", function() {
        browser.click(by.id('sendActionsOuter'));

        browser.click(by.id('toolbar_option_0'));
        expect(element(by.id('message')).getText()).toBe('Action 5 called');
        browser.switchToIFrame();
        expect(element(by.id('message')).getText()).toBe('');
        browser.switchToParent();

        browser.click(by.id('toolbar_option_1'));
        expect(element(by.id('message')).getText()).toBe('Action 6 called');
        browser.switchToIFrame();
        expect(element(by.id('message')).getText()).toBe('');
    });

    it("item of type 'TEMPLATE' will display its template", function() {
        expect(element.all(by.css('li.yToolbarBtn img')).count()).toBe(0);
        browser.click(by.id('sendActionsOuter'));
        expect(element.all(by.css('li.yToolbarBtn img')).count()).toBe(2);

        expect(element(by.id('standardTemplate')).getText()).toBe('STANDARD TEMPLATE');
    });


});

describe("Configure toolbar through inner toolbarservice", function() {

    beforeEach(function() {
        browser.get(itemMechanismPath);
    });

    it("items of type 'ACTION' will be added", function() {
        expect(element.all(by.css('li.yToolbarBtn img')).count()).toBe(0);
        browser.switchToIFrame();
        browser.click(by.id('sendActionsInner'));
        browser.switchToParent();
        expect(element.all(by.css('li.yToolbarBtn img')).count()).toBe(2);

        expect(element(by.id('toolbar_option_0')).getAttribute('alt')).toBe('action3');
        expect(element(by.id('toolbar_option_1')).getAttribute('alt')).toBe('action4');
    });


    it("Callbacks will be executed successfully when items of type 'ACTION'", function() {
        browser.switchToIFrame();
        browser.click(by.id('sendActionsInner'));

        browser.switchToParent();
        browser.click(by.id('toolbar_option_0'));
        expect(element(by.id('message')).getText()).toBe('');
        browser.switchToIFrame();
        expect(element(by.id('message')).getText()).toBe('Action 3 called');

        browser.switchToParent();
        browser.click(by.id('toolbar_option_1'));
        expect(element(by.id('message')).getText()).toBe('');
        browser.switchToIFrame();
        expect(element(by.id('message')).getText()).toBe('Action 4 called');
    });
});


describe("Configure toolbar through inner AND outer toolbarservice", function() {

    beforeEach(function() {
        browser.get(itemMechanismPath);
    });


    it('Actions will not conflict', function() {
        browser.click(by.id('sendActionsOuter'));
        browser.switchToIFrame();
        browser.click(by.id('sendActionsInner'));
        browser.switchToParent();
        expect(element.all(by.css('li.yToolbarBtn img')).count()).toBe(4);

        expect(element(by.id('toolbar_option_0')).getAttribute('alt')).toBe('action5');
        expect(element(by.id('toolbar_option_1')).getAttribute('alt')).toBe('action6');
        expect(element(by.id('toolbar_option_2')).getAttribute('alt')).toBe('action3');
        expect(element(by.id('toolbar_option_3')).getAttribute('alt')).toBe('action4');
    });

});
