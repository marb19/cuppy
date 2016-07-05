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
var InflectionPoint = function() {

    this.pageURI = 'smarteditcontainerJSTests/e2e/inflectionPoint/inflectionPointTest.html';
    browser.get(this.pageURI);
};

InflectionPoint.prototype = Object.create({}, {

    inflectionMenu: {
        get: function() {
            return element(by.id('inflectionPtDropdown'));
        }

    },
    firstInflectionDevice: {
        get: function() {
            return element(by.css('.dropdown-menu li a'));
        }
    },
    firstDeviceWidth: {
        get: function() {
            return '480px';
        }
    },
    iframeWidth: {
        get: function() {
            return element(by.tagName('iframe')).getCssValue('width');
        }
    }

});

module.exports = InflectionPoint;
