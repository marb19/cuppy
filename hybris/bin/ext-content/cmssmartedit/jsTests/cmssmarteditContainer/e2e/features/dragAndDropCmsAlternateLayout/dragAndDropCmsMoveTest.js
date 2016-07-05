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
        browser.get('jsTests/cmssmarteditContainer/e2e/features/dragAndDropCmsAlternateLayout/dragAndDropCmsTest.html');

    });

    it("component1 will successfully move to empty slot4", function() {

        browser.waitForWholeAppToBeReady();

        //drag and drop component1 onto slot3
        browser.switchToIFrame();

        browser.click("#component1", "component1 not found");

        expect(element(by.css('#slot1 #component1')).isPresent()).toBe(true);
        expect(element(by.css('#slot4 #component1')).isPresent()).toBe(false);


        //drag component1 over slot4 (Note: We move mouse twice because the position needs to be recalculated)
        browser.actions()
            .mouseMove(element(by.id('component1')))
            .mouseDown()
            .mouseMove(element(by.id('slot4')))
            .perform()
            .then(function() {
                return browser.sleep(200);
            }).then(function() {
                browser.actions()
                    .mouseMove(element(by.id('slot4')))
                    .perform();
            });

        expect(element(by.css('#slot4')).getAttribute('class')).toContain('over-slot-enabled');

        //drop component1 over slot3
        browser.actions().mouseUp()
            .perform();

        browser.switchToParent();

        browser.switchToIFrame();

        expect(element(by.css('#slot1 #component1')).isPresent()).toBe(false);
        expect(element(by.css('#slot4 #component1')).isPresent()).toBe(true);

    });


    it("will set eligible-slot css class to all eligible containers upon starting to drag", function() {

        browser.waitForWholeAppToBeReady();
        browser.switchToIFrame();

        browser.click("#component1", "component1 not found");

        browser.actions().mouseMove(element(by.id('component1')))
            .mouseDown()
            .mouseMove({
                x: 1,
                y: 0
            }) // just move 1px to the right
            .perform();

        expect(element.all(by.css('.smartEditComponent.eligible-slot')).count()).toBe(4);

    });


});
