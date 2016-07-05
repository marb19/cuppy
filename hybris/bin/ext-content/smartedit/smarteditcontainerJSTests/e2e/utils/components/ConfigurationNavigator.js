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
module.exports = {
    openConfigurationEditor: function() {
        element(by.id('nav-expander')).click();
        element(by.id('configurationCenter')).click();
        element(by.id('generalConfiguration')).click();
    },

    getConfigurationsAsList: function() {
        browser.wait(function() {
            return element.all(by.css('#editConfigurationsBody .ySECfgEntity')).count().then(function(count) {
                return count > 0;
            });
        }, 5000, "could not find config list");
        return element.all(by.css('#editConfigurationsBody .ySECfgEntity'));
    },

    waitForModal: function() {
        browser.wait(function() {
            return element(by.css('div.modal-content'));
        }, 5000, "could not find modal content div");
    },

    setConfigurationValue: function(rowIndex, value) {
        this.getConfigurationsAsList()
            .get(rowIndex)
            .element(by.model('entry.value'))
            .clear()
            .sendKeys(value);
    },

    setConfigurationKeyAndValue: function(rowIndex, key, value) {
        this.getConfigurationsAsList()
            .get(rowIndex)
            .element(by.model('entry.key'))
            .clear()
            .sendKeys(key);

        this.setConfigurationValue(rowIndex, value);
    },

    clickSave: function() {
        element(by.cssContainingText('.modal-dialog .modal-footer button', 'Save')).click();
    },

    clickCancel: function() {
        element(by.cssContainingText('.modal-dialog .modal-footer button', 'Cancel')).click();
    },

    getCancelButton: function() {
        return element(by.cssContainingText('.modal-dialog .modal-footer button', 'Cancel'));
    },

    getSaveButton: function() {
        return element(by.cssContainingText('.modal-dialog .modal-footer button', 'Save'));
    },

    openLeftToolbar: function() {
        element(by.id('nav-expander')).click();
    },

    clickConfirmOk: function() {
        browser.click('#confirmOk', 'could not find confirmOk button');
    },

    getConfigurationTitle: function() {
        return element(by.id('smartedit-modal-title-modal.administration.configuration.edit.title'));
    },

    clickAdd: function() {
        browser.wait(protractor.ExpectedConditions.elementToBeClickable($('.y-add-btn')), 5000, "could not find add button");
        return browser.click(by.css('.y-add-btn'));
    },

    deleteConfiguration: function(rowIndex) {
        browser.wait(protractor.ExpectedConditions.elementToBeClickable(element(by.css("button[id*=removeButton]"))), 5000, "could not find removeButton");
        this.getConfigurationsAsList()
            .get(rowIndex)
            .element(by.css("button[id*=removeButton]"))
            .click();
    },

    getEntry: function(array, rows, index) {
        var deferred = protractor.promise.defer();
        var that = this;
        rows[index].element(by.model('entry.key')).getAttribute('value').then(function(key) {
            rows[index].element(by.model('entry.value')).getAttribute('value').then(function(value) {
                array.push({
                    key: key,
                    value: value
                });
                if (index < rows.length - 1) {
                    that.getEntry(array, rows, index + 1).then(function(array) {
                        deferred.fulfill(array);
                    });
                } else {
                    deferred.fulfill(array);
                }
            });
        });
        return deferred.promise;
    },

    getConfigurations: function() {
        var deferred = protractor.promise.defer();
        var that = this;
        element.all(by.css('#editConfigurationsBody .ySECfgEntity')).then(function(rows) {
            that.getEntry([], rows, 0).then(function(array) {
                deferred.fulfill(array);
            });
        });
        return deferred.promise;
    },

    waitForConfigurationModal: function(size, message) {
        this.waitForModal();
        this.waitForConfigurationsToPopulate(size, message);
    },

    waitForConfigurationsToPopulate: function(size, message) {
        message = message || "could not find initial list of entries";
        browser.wait(function() {
            return element.all(by.css('#editConfigurationsBody .ySECfgEntity')).count().then(function(count) {
                return count === size;
            });
        }, 5000, message);
    },

    waitForErrorForKey: function(key, message) {
        browser.wait(protractor.ExpectedConditions.visibilityOf($('#' + key + '_error_0')), 5000);
    },

    getErrorForKey: function(key) {
        return element(by.id(key + "_error_0"));
    }

};
