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
describe("Configuration Editor - ", function() {

    var defaultConfigurations;
    var smartEdit;

    beforeEach(function() {
        defaultConfigurations = require('./defaultConfigurations.json');
        smartEdit = require("../utils/components/ConfigurationNavigator.js");

        browser.get('smarteditcontainerJSTests/e2e/a_editConfigurations/editConfigurationsTest.html');
        browser.disableAnimations();
        browser.waitForWholeAppToBeReady();
    });

    it("GIVEN I am in the Configuration Editor THEN I expect to see a title, a save and cancel button, and configurations as defined in the backend", function() {
        // GIVEN
        smartEdit.openConfigurationEditor();

        // THEN
        expect(smartEdit.getConfigurationTitle().getText()).toBe('edit configuration ');
        expect(smartEdit.getCancelButton().isPresent()).toBe(true);
        expect(smartEdit.getSaveButton().isPresent()).toBe(true);
        expect(smartEdit.getConfigurations()).toEqualData(defaultConfigurations);
    });

    it("GIVEN I'm in the Configuration Editor WHEN I delete a configuration entry AND I reopen the configuration editor THEN I expect to see the remaining configurations", function() {
        // GIVEN
        smartEdit.openConfigurationEditor();
        smartEdit.waitForConfigurationModal(3);

        // WHEN
        smartEdit.deleteConfiguration(1); //delete the 2nd configuration
        smartEdit.clickSave();
        smartEdit.clickCancel();
        smartEdit.openConfigurationEditor();

        // THEN
        expect(smartEdit.getConfigurations()).toEqualData([{
            "key": "previewTicketURI",
            "value": "\"thepreviewTicketURI\""
        }, {
            "key": "applications.e2eBackendMocks",
            "value": "{\n  \"smartEditLocation\": \"/smarteditcontainerJSTests/e2e/a_editConfigurations/mocksforConfiguration.js\"\n}"
        }]);
    });

    it("GIVEN I'm in the Configuration Editor WHEN I attempt to add a malformed configuration THEN an error is displayed", function() {
        // GIVEN
        smartEdit.openConfigurationEditor();
        smartEdit.waitForConfigurationModal(3);

        // WHEN
        smartEdit.clickAdd();
        smartEdit.waitForConfigurationsToPopulate(4);
        smartEdit.setConfigurationKeyAndValue(3, 'newkey', '{othermalformed}');
        smartEdit.clickSave();
        smartEdit.waitForErrorForKey("newkey");

        // THEN
        expect(smartEdit.getErrorForKey("newkey").getText()).toEqual("this value should be a valid JSON format");
    });

    it("GIVEN I'm in the Configuration Editor WHEN I attempt to add a malformed configuration AND re-open the configuration editor THEN I expect to see the original configurations", function() {
        // GIVEN
        smartEdit.openConfigurationEditor();
        smartEdit.waitForConfigurationModal(3);

        // WHEN
        smartEdit.clickAdd();
        smartEdit.waitForConfigurationsToPopulate(4);
        smartEdit.setConfigurationKeyAndValue(3, 'newkey', '{othermalformed}');
        smartEdit.clickSave();
        smartEdit.waitForErrorForKey("newkey");
        smartEdit.clickCancel();
        smartEdit.openConfigurationEditor();

        // THEN
        expect(smartEdit.getConfigurations()).toEqual([{
            "key": "previewTicketURI",
            "value": "\"thepreviewTicketURI\""
        }, {
            "key": 'i18nAPIRoot',
            "value": "{malformed}"
        }, {
            "key": "applications.e2eBackendMocks",
            "value": "{\n  \"smartEditLocation\": \"/smarteditcontainerJSTests/e2e/a_editConfigurations/mocksforConfiguration.js\"\n}"
        }]);
    });

    it("GIVEN I'm in the Configuration Editor WHEN I attempt to add a new well-formed configuration THEN the configuration will be added", function() {
        // GIVEN
        smartEdit.openConfigurationEditor();
        smartEdit.waitForConfigurationModal(3);

        // WHEN
        smartEdit.clickAdd();
        smartEdit.waitForConfigurationsToPopulate(4);
        smartEdit.setConfigurationKeyAndValue(3, 'newkey', '\"new value\"');
        smartEdit.clickSave();
        smartEdit.clickCancel();
        smartEdit.openConfigurationEditor();

        // THEN
        expect(smartEdit.getConfigurations()).toEqual([{
            "key": "previewTicketURI",
            "value": "\"thepreviewTicketURI\""
        }, {
            "key": 'i18nAPIRoot',
            "value": "{malformed}"
        }, {
            "key": "applications.e2eBackendMocks",
            "value": "{\n  \"smartEditLocation\": \"/smarteditcontainerJSTests/e2e/a_editConfigurations/mocksforConfiguration.js\"\n}"
        }, {
            "key": "newkey",
            "value": "\"new value\""
        }]);
    });

    it("GIVEN I'm in the Configuration Editor WHEN I attempt to modify an configuration with a well-formed configuration THEN I expect to see the configuration modified", function() {
        // GIVEN
        smartEdit.openConfigurationEditor();
        smartEdit.waitForConfigurationModal(3);

        // WHEN
        smartEdit.setConfigurationValue(1, '\"new\"');
        smartEdit.clickSave();
        smartEdit.clickCancel();
        smartEdit.openConfigurationEditor();

        // THEN
        expect(smartEdit.getConfigurations()).toEqualData([{
            "key": "previewTicketURI",
            "value": "\"thepreviewTicketURI\""
        }, {
            "key": "i18nAPIRoot",
            "value": "\"new\""
        }, {
            "key": "applications.e2eBackendMocks",
            "value": "{\n  \"smartEditLocation\": \"/smarteditcontainerJSTests/e2e/a_editConfigurations/mocksforConfiguration.js\"\n}"
        }]);
    });


    it("GIVEN I'm in the Configuration Editor WHEN I attempt to add a duplicate key THEN I expect to see an error", function() {
        // GIVEN
        smartEdit.openConfigurationEditor();
        smartEdit.waitForConfigurationModal(3);

        // WHEN
        smartEdit.clickAdd();
        smartEdit.waitForConfigurationsToPopulate(4);
        smartEdit.setConfigurationKeyAndValue(3, 'previewTicketURI', 'previewTicketURI'); //add key and value
        smartEdit.clickSave();
        smartEdit.waitForErrorForKey("previewTicketURI");

        // THEN
        expect(smartEdit.getErrorForKey("previewTicketURI").getText()).toEqual("This is a duplicate key");
    });

    it("GIVEN I'm in the Configuration Editor WHEN I attempt to add a duplicate key AND click cancel THEN I expect to see the original configuration in tact", function() {
        // GIVEN
        smartEdit.openConfigurationEditor();
        smartEdit.waitForConfigurationModal(3);

        // WHEN
        smartEdit.clickAdd();
        smartEdit.waitForConfigurationsToPopulate(4);
        smartEdit.setConfigurationKeyAndValue(3, 'previewTicketURI', 'previewTicketURI'); //add key and value
        smartEdit.clickSave();
        smartEdit.waitForErrorForKey("previewTicketURI");
        smartEdit.clickCancel();
        smartEdit.clickConfirmOk();
        smartEdit.openConfigurationEditor();

        // THEN
        expect(smartEdit.getConfigurations()).toEqual([{
            "key": "previewTicketURI",
            "value": "\"thepreviewTicketURI\""
        }, {
            "key": 'i18nAPIRoot',
            "value": "{malformed}"
        }, {
            "key": "applications.e2eBackendMocks",
            "value": "{\n  \"smartEditLocation\": \"/smarteditcontainerJSTests/e2e/a_editConfigurations/mocksforConfiguration.js\"\n}"
        }]);
    });


    it("GIVEN I'm in the Configuration Editor WHEN I close the modal THEN the left toolbar should be back to the first-level menu", function() {
        // GIVEN
        smartEdit.openConfigurationEditor();
        smartEdit.waitForConfigurationModal(3);

        // WHEN
        smartEdit.clickCancel();
        smartEdit.openLeftToolbar();

        // THEN
        var hamburgerMenuElementLvl1 = element(by.id('hamburger-menu-level1'));
        expect(hamburgerMenuElementLvl1.getAttribute('class')).not.toContain('ySELeftHideLevel1');
        expect(hamburgerMenuElementLvl1.getAttribute('class')).toContain('ySELeftShowLevel1');

        var hamburgerMenuElementLvl2 = element(by.id('hamburger-menu-level2'));
        expect(hamburgerMenuElementLvl2.getAttribute('class')).toContain('ySELeftHideLevel2');
        expect(hamburgerMenuElementLvl2.getAttribute('class')).not.toContain('ySELeftShowLevel2');
    });

    it('WHEN user types an absolute URL THEN the editor shall display a checkbox', function() {
        // Arrange
        browser.waitForContainerToBeReady();

        smartEdit.openConfigurationEditor();
        browser.wait(function() {
            return element(by.css('div.modal-content'));
        }, 5000, "could not find modal content div");

        // Act/Assert
        smartEdit.setConfigurationValue(0, '"https://someuri"'); //add key and value
        expect(element(by.id('previewTicketURI_absoluteUrl_check_0')).isDisplayed()).toBeTruthy();
    });


    it('WHEN user types does not type an absolute URL THEN the editor shall not display a checkbox', function() {
        // Arrange
        browser.waitForContainerToBeReady();

        smartEdit.openConfigurationEditor();
        browser.wait(function() {
            return element(by.css('div.modal-content'));
        }, 5000, "could not find modal content div");

        // Act/Assert
        smartEdit.setConfigurationValue(0, '"/someuri/"'); //add key and value
        expect(element(by.id('previewTicketURI_absoluteUrl_check_0')).isPresent()).toBeFalsy();
    });

    it('WHEN user types an absolute URL and doesn not tick the checkbox THEN the editor shall highlight the message and not save', function() {
        // Arrange
        browser.waitForContainerToBeReady();

        smartEdit.openConfigurationEditor();
        browser.wait(function() {
            return element(by.css('div.modal-content'));
        }, 5000, "could not find modal content div");

        // Act/Assert
        element.all(by.css('textarea')).getAttribute('value').then(function(originalValues) {

            smartEdit.setConfigurationValue(0, '"https://someuri"'); //add key and value
            expect(element(by.id('previewTicketURI_absoluteUrl_check_0')).isDisplayed()).toBeTruthy();

            expect(element(by.id('previewTicketURI_absoluteUrl_msg_0')).getAttribute('class')).not.toMatch(' not-checked');
            smartEdit.clickSave();
            expect(element(by.id('previewTicketURI_absoluteUrl_msg_0')).getAttribute('class')).toMatch(' not-checked');

            smartEdit.clickCancel();
            smartEdit.openConfigurationEditor();

            element.all(by.css('textarea')).getAttribute('value').then(function(newValues) {
                expect(originalValues[0]).toBe(newValues[0]);
            });
        });
    });

    it('WHEN user types an absolute URL and ticks the checkbox THEN the editor shall not highlight the message and save the content', function() {
        // Arrange
        browser.waitForContainerToBeReady();

        smartEdit.openConfigurationEditor();
        browser.wait(function() {
            return element(by.css('div.modal-content'));
        }, 5000, "could not find modal content div");

        // Act/Assert
        element.all(by.css('textarea')).getAttribute('value').then(function(originalValues) {

            smartEdit.setConfigurationValue(0, '"https://someuri"'); //add key and value
            expect(element(by.id('previewTicketURI_absoluteUrl_check_0')).isDisplayed()).toBeTruthy();
            element(by.id('previewTicketURI_absoluteUrl_check_0')).click();

            expect(element(by.id('previewTicketURI_absoluteUrl_msg_0')).getAttribute('class')).not.toMatch(' not-checked');
            smartEdit.clickSave();
            expect(element(by.id('previewTicketURI_absoluteUrl_msg_0')).getAttribute('class')).not.toMatch(' not-checked');

            smartEdit.clickCancel();
            smartEdit.openConfigurationEditor();

            element.all(by.css('textarea')).getAttribute('value').then(function(newValues) {
                expect(originalValues[0]).not.toBe(newValues[0]);
            });
        });
    });

});
