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
//we do not seem to be able to specify position within slot with the current selenium api : playing with coordinates always puts at the end of slot
describe('E2E Test for CMS drag and drop service within page', function() {

    beforeEach(function() {
        browser.get('jsTests/cmssmarteditContainer/e2e/features/dragAndDropCms/dragAndDropCmsTest.html');

    });

    it("component1 will not move to slot2 as slot2 is disabled", function() {

        browser.waitForWholeAppToBeReady();

        //this test is not testing drag and drop; it is testing the message service
        browser.switchToIFrame();

        expect(element(by.css('#slot1 #component1')).isPresent()).toBe(true);
        expect(element(by.css('#slot2 #component1')).isPresent()).toBe(false);

        moveMouseToElement('#component1').then(function() {
            browser.sleep('500');
            browser.actions()
                .mouseDown()
                .perform();

            browser.switchToParent();
            dragToPositionFromElement(element(by.css('iframe')), 0, 0);
            browser.switchToIFrame();

            return element(by.css('#slot2')).getLocation();

        }).then(function(position) {

            browser.actions()
                .mouseMove({
                    x: position.x,
                    y: position.y
                }).perform();

            expect(element(by.id('slot2')).getAttribute('class')).toContain('over-slot-disabled');

        });

        dropDraggedElement();

        browser.switchToParent();
        expect(element(by.binding('alert.message')).getText()).toBe('Component component1 not allowed in bottomHeaderSlot.'); //assert drag and drop failure

        browser.switchToIFrame();
        expect(element(by.css('#slot1 #component1')).isPresent()).toBe(true);
        expect(element(by.css('#slot2 #component1')).isPresent()).toBe(false);

    });

    it("component2 will fail to move to slot3 because of move API error", function() {

        browser.waitForWholeAppToBeReady();

        browser.switchToIFrame();

        expect(element(by.css('#slot1 #component2')).isPresent()).toBe(true);
        expect(element(by.css('#slot3 #component2')).isPresent()).toBe(false);

        moveMouseToElement('#component2').then(function() {
            browser.sleep('500');
            browser.actions()
                .mouseDown()
                .perform();

            browser.switchToParent();
            dragToPositionFromElement(element(by.css('iframe')), 0, 0);
            browser.switchToIFrame();

            return element(by.css('#slot3')).getLocation();

        }).then(function(position) {

            browser.actions()
                .mouseMove({
                    x: position.x,
                    y: position.y
                }).perform();

            expect(element(by.id('slot3')).getAttribute('class')).toContain('over-slot-enabled');

        });

        dropDraggedElement();

        browser.switchToParent();
        expect(element(by.binding('alert.message')).getText()).toBe('failed to move component component2 to slot footerSlot'); //assert drag and drop failure

        browser.switchToIFrame();

        expect(element(by.css('#slot1 #component2')).isPresent()).toBe(true);
        expect(element(by.css('#slot3 #component2')).isPresent()).toBe(false);

    });


    // HELPER FUNCTIONS

    function moveMouseToElement(elementSelector) {
        return browser.actions()
            .mouseMove(element(by.css(elementSelector)))
            .perform();
    }

    function dropDraggedElement() {
        return browser.actions().mouseUp().perform();
    }

    function dragToPositionFromElement(element, xCoord, yCoord) {
        return browser.actions()
            .mouseMove(element, {
                x: xCoord,
                y: yCoord
            })
            .perform();
    }

});
