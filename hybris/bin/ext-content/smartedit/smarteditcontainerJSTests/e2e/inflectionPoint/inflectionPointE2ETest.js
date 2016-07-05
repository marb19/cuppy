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
var InflectionPoint = require('./inflectionPointObject.js');

describe(
    'end-to-end Test for inflection point module',
    function() {
        var page;
        beforeEach(function() {

            page = new InflectionPoint();
            browser.waitForWholeAppToBeReady();

        });

        it(
            "Upon loading SmartEdit, inflection-point-selector should be displayed and select the first option. On selection width of the iframe should be changed",
            function() {
                page.inflectionMenu.click();
                page.firstInflectionDevice.click();
                expect(page.iframeWidth).toBe(page.firstDeviceWidth);

            });


    });
