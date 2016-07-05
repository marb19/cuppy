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
var SmartEditPage = require('./smartEditPage.js');

describe('Experience Selector - ', function() {

    var ELECTRONICS_SITE = {
        CATALOGS: {
            ONLINE: 3,
            STAGED: 4
        },
        LANGUAGES: {
            ENGLISH: 1,
            POLISH: 2,
            ITALIAN: 3
        }
    };

    var APPAREL_SITE = {
        CATALOGS: {
            ONLINE: 1,
            STAGED: 2
        },
        LANGUAGES: {
            ENGLISH: 1,
            FRENCH: 2
        }
    };

    var smartEditPage;

    beforeEach(function() {
        smartEditPage = new SmartEditPage();
    });

    it("GIVEN I'm in the SmartEdit application WHEN I click the Experience Selector button THEN I expect to see the Experience Selector", function() {
        browser.waitForWholeAppToBeReady();
        //GIVEN

        //WHEN
        smartEditPage.experienceSelectorButton.click();

        //THEN
        expect(smartEditPage.experienceSelector.catalogFieldLabel.getText()).toBe('CATALOG');
        expect(smartEditPage.experienceSelector.timeFieldLabel.getText()).toBe('DATE/TIME');
        expect(smartEditPage.experienceSelector.languageFieldLabel.getText()).toBe('LANGUAGE');

        expect(smartEditPage.experienceSelector.applyButton.getText()).toBe('APPLY');
        expect(smartEditPage.experienceSelector.cancelButton.getText()).toBe('CANCEL');
    });

    it("GIVEN I'm in the SmartEdit application WHEN I click the Experience Selector THEN I expect to see the currently selected experience in the Experience Selector", function() {
        browser.waitForWholeAppToBeReady();
        //GIVEN

        //WHEN
        smartEditPage.experienceSelectorButton.click();

        //THEN
        expect(smartEditPage.experienceSelector.catalogSelectedOption.getText()).toBe('Electronics Content Catalog - Online');
        expect(smartEditPage.experienceSelector.languageSelectedOption.getText()).toBe('English');
        smartEditPage.experienceSelector.timeField.getAttribute('placeholder').then(function(datePlaceholder) {
            expect(datePlaceholder).toBe('Select a Date and Time');
        });
    });

    it("GIVEN I'm in the experience selector WHEN I do not choose a catalog from the catalog dropdown THEN I expect to see a disabled Apply button", function() {
        browser.waitForWholeAppToBeReady();

        //GIVEN
        smartEditPage.experienceSelectorButton.click();

        //WHEN

        //THEN
        smartEditPage.experienceSelector.applyButton.getAttribute('disabled').then(function(attributeValue) {
            expect(attributeValue).toBe('true');
        });

    });

    it("GIVEN I'm in the experience selector WHEN I click on the catalog selector dropdown THEN I expect to see all catalog/catalog versions combinations", function() {
        browser.waitForWholeAppToBeReady();

        //GIVEN
        smartEditPage.experienceSelectorButton.click();

        //WHEN
        smartEditPage.experienceSelector.catalogDropdown.click();

        expect(smartEditPage.experienceSelector.catalogOptions.options.count()).toBe(4);
        expect(smartEditPage.experienceSelector.catalogOptions.option(1).getText()).toBe('Apparel UK Content Catalog - Online');
        expect(smartEditPage.experienceSelector.catalogOptions.option(2).getText()).toBe('Apparel UK Content Catalog - Staged');
        expect(smartEditPage.experienceSelector.catalogOptions.option(3).getText()).toBe('Electronics Content Catalog - Online');
        expect(smartEditPage.experienceSelector.catalogOptions.option(4).getText()).toBe('Electronics Content Catalog - Staged');
    });

    it("GIVEN I'm in the experience selector WHEN I select a catalog THEN I expect to see the apply button enabled", function() {
        browser.waitForWholeAppToBeReady();

        //GIVEN
        smartEditPage.experienceSelectorButton.click();

        //WHEN
        smartEditPage.experienceSelector.catalogDropdown.click();
        smartEditPage.experienceSelector.catalogOptions.option(1).click();

        //THEN
        smartEditPage.experienceSelector.applyButton.getAttribute('disabled').then(function(attributeValue) {
            expect(attributeValue).toBeFalsy();
        });
    });

    it("GIVEN I'm in the experience selector WHEN I select a catalog belonging to the electronics site THEN I expect to see the language dropdown populated with the electronics sites languages", function() {
        browser.waitForWholeAppToBeReady();

        //GIVEN
        smartEditPage.experienceSelectorButton.click();

        //WHEN
        smartEditPage.experienceSelector.catalogDropdown.click();
        smartEditPage.experienceSelector.catalogOptions.option(3).click();
        smartEditPage.experienceSelector.languageDropdown.click();

        //THEN
        expect(smartEditPage.experienceSelector.languageOptions.options.count()).toBe(3);
        expect(smartEditPage.experienceSelector.languageOptions.option(1).getText()).toBe('English');
        expect(smartEditPage.experienceSelector.languageOptions.option(2).getText()).toBe('Polish');
        expect(smartEditPage.experienceSelector.languageOptions.option(3).getText()).toBe('Italian');
    });

    it("GIVEN I'm in the experience selector WHEN I select a catalog belonging to the apparel site THEN I expect to see the language dropdown populated with the apprel sites languages", function() {
        browser.waitForWholeAppToBeReady();

        //GIVEN
        smartEditPage.experienceSelectorButton.click();

        //WHEN
        smartEditPage.experienceSelector.catalogDropdown.click();
        smartEditPage.experienceSelector.catalogOptions.option(1).click();
        smartEditPage.experienceSelector.languageDropdown.click();

        expect(smartEditPage.experienceSelector.languageOptions.options.count()).toBe(2);
        expect(smartEditPage.experienceSelector.languageOptions.option(1).getText()).toBe('English');
        expect(smartEditPage.experienceSelector.languageOptions.option(2).getText()).toBe('French');
    });

    it("GIVEN I'm in the experience selector WHEN I click the apply button AND the REST call to the preview service succeeds THEN I expect the smartEdit application with the new preview ticket", function() {
        browser.waitForWholeAppToBeReady();

        //GIVEN
        smartEditPage.experienceSelectorButton.click();
        smartEditPage.experienceSelector.catalogDropdown.click();
        smartEditPage.experienceSelector.catalogOptions.option(APPAREL_SITE.CATALOGS.ONLINE).click();
        smartEditPage.experienceSelector.languageDropdown.click();
        smartEditPage.experienceSelector.languageOptions.option(APPAREL_SITE.LANGUAGES.ENGLISH).click();

        //WHEN
        smartEditPage.experienceSelector.applyButton.click();
        browser.waitForWholeAppToBeReady();

        //THEN
        smartEditPage.iframe.getAttribute('src').then(function(src) {
            var RESOURCE_URI = '/smarteditcontainerJSTests/e2e/dummystorefront.html';
            var TICKET_ID_QUERY_PARAM = 'cmsTicketId=validTicketId';
            expect(src.indexOf(RESOURCE_URI + '?' + TICKET_ID_QUERY_PARAM)).toBeGreaterThan(0);
        });
    });

    it("GIVEN I'm in the experience selector WHEN I click the apply button AND the REST call to the preview service fails due to an invalid catalog and catalog version THEN I expect to see an error displayed", function() {
        browser.waitForWholeAppToBeReady();

        //GIVEN
        smartEditPage.experienceSelectorButton.click();
        smartEditPage.experienceSelector.catalogDropdown.click();
        smartEditPage.experienceSelector.catalogOptions.option(ELECTRONICS_SITE.CATALOGS.ONLINE).click();
        smartEditPage.experienceSelector.languageDropdown.click();
        smartEditPage.experienceSelector.languageOptions.option(ELECTRONICS_SITE.LANGUAGES.ITALIAN).click();

        //WHEN
        smartEditPage.experienceSelector.applyButton.click();
        browser.waitForWholeAppToBeReady();

        //THEN
        expect(smartEditPage.alert.isDisplayed()).toBe(true);


    });

    it("GIVEN I'm in the experience selector AND I click on the apply button to update the experience with the one I chose THEN it should update the experience widget text", function() {
        browser.waitForWholeAppToBeReady();

        //GIVEN
        smartEditPage.experienceSelectorButton.click();
        smartEditPage.experienceSelector.catalogDropdown.click();
        smartEditPage.experienceSelector.catalogOptions.option(APPAREL_SITE.CATALOGS.ONLINE).click();
        smartEditPage.experienceSelector.languageDropdown.click();
        smartEditPage.experienceSelector.languageOptions.option(APPAREL_SITE.LANGUAGES.FRENCH).click();

        //WHEN
        smartEditPage.experienceSelector.applyButton.click();
        browser.waitForWholeAppToBeReady();

        //THEN
        var VALID_EXPERIENCE_WIDGET_TEXT = 'Apparel UK Content Catalog - Online | French';
        smartEditPage.experienceSelector.widgetText.then(function(widgetText) {
            expect(widgetText).toBe(VALID_EXPERIENCE_WIDGET_TEXT);
        });

    });

    it("GIVEN I'm in the experience selector WHEN I click on the apply button to update the experience with the one I chose THEN it should update the experience widget text", function() {
        browser.waitForWholeAppToBeReady();

        //GIVEN
        smartEditPage.experienceSelectorButton.click();
        smartEditPage.experienceSelector.catalogDropdown.click();
        smartEditPage.experienceSelector.catalogOptions.option(APPAREL_SITE.CATALOGS.ONLINE).click();

        smartEditPage.experienceSelector.timeField.click();
        smartEditPage.experienceSelector.timeField.sendKeys("01/01/2016 12:00 AM");

        smartEditPage.experienceSelector.languageDropdown.click();
        smartEditPage.experienceSelector.languageOptions.option(APPAREL_SITE.LANGUAGES.FRENCH).click();

        //WHEN
        smartEditPage.experienceSelector.applyButton.click();
        browser.waitForWholeAppToBeReady();

        //THEN
        var VALID_EXPERIENCE_WIDGET_TEXT = 'Apparel UK Content Catalog - Online | French | 01/01/2016 12:00 AM';
        smartEditPage.experienceSelector.widgetText.then(function(widgetText) {
            expect(widgetText).toBe(VALID_EXPERIENCE_WIDGET_TEXT);
        });

    });

    it("GIVEN I'm in the experience selector AND I select a date and time using the date-time picker WHEN I click the apply button THEN it should update the experience widget text", function() {
        browser.waitForWholeAppToBeReady();

        // GIVEN
        smartEditPage.experienceSelectorButton.click();
        smartEditPage.experienceSelector.catalogDropdown.click();
        smartEditPage.experienceSelector.catalogOptions.option(APPAREL_SITE.CATALOGS.ONLINE).click();

        smartEditPage.experienceSelector.dateTimePickerButton.click();
        smartEditPage.selectExpectedDate();
        smartEditPage.experienceSelector.dateTimePickerButton.click();

        smartEditPage.experienceSelector.languageDropdown.click();
        smartEditPage.experienceSelector.languageOptions.option(APPAREL_SITE.LANGUAGES.FRENCH).click();

        // WHEN
        smartEditPage.experienceSelector.applyButton.click();
        browser.waitForWholeAppToBeReady();

        // THEN
        var VALID_EXPERIENCE_WIDGET_TEXT = 'Apparel UK Content Catalog - Online | French | 01/01/2016 1:00 PM';
        smartEditPage.experienceSelector.widgetText.then(function(widgetText) {
            expect(widgetText).toBe(VALID_EXPERIENCE_WIDGET_TEXT);
        });
    });

    it("GIVEN I'm in the experience selector WHEN I click outside the experience selector in the SmartEdit container THEN the experience selector is closed and reset", function() {
        browser.waitForWholeAppToBeReady();

        //GIVEN
        smartEditPage.experienceSelectorButton.click();

        //WHEN
        smartEditPage.clickInApplication();

        //THEN
        expect(smartEditPage.experienceSelector.catalogFieldLabel.isDisplayed()).toBe(false);
    });

    it("GIVEN I'm in the experience selector WHEN I click outside the experience selector in the SmartEdit application THEN the experience selector is closed and reset", function() {
        browser.waitForWholeAppToBeReady();

        //GIVEN
        smartEditPage.experienceSelectorButton.click();

        //WHEN
        smartEditPage.clickInIframe();

        //THEN
        expect(smartEditPage.experienceSelector.catalogFieldLabel.isDisplayed()).toBe(false);
    });

    it("GIVEN I have selected an experience with a time WHEN I click the apply button AND the REST call to the preview service succeeds AND I re-open the experience selector THEN I expect to see the newly selected experience", function() {
        browser.waitForWholeAppToBeReady();

        //GIVEN
        smartEditPage.experienceSelectorButton.click();
        smartEditPage.experienceSelector.catalogDropdown.click();
        smartEditPage.experienceSelector.catalogOptions.option(ELECTRONICS_SITE.CATALOGS.STAGED).click();

        smartEditPage.experienceSelector.timeField.click();
        smartEditPage.experienceSelector.timeField.sendKeys("01/01/2016 12:00 AM");

        smartEditPage.experienceSelector.languageDropdown.click();
        smartEditPage.experienceSelector.languageOptions.option(ELECTRONICS_SITE.LANGUAGES.ITALIAN).click();

        //WHEN
        smartEditPage.experienceSelector.applyButton.click();
        browser.waitForWholeAppToBeReady();
        browser.wait(protractor.ExpectedConditions.elementToBeClickable(element(by.id('experience-selector-btn'))), 5000, 'Experience Selector button not found');
        smartEditPage.experienceSelectorButton.click();

        //THEN
        expect(smartEditPage.experienceSelector.catalogSelectedOption.getText()).toBe('Electronics Content Catalog - Staged');
        expect(smartEditPage.experienceSelector.languageSelectedOption.getText()).toBe('Italian');
        smartEditPage.experienceSelector.timeField.getAttribute('value').then(function(value) {
            expect(value).toBe('01/01/2016 12:00 AM');
        });
    });

    it("GIVEN I have selected an experience without a time WHEN I click the apply button AND the REST call to the preview service succeeds AND I re-open the experience selector THEN I expect to see the newly selected experience", function() {
        browser.waitForWholeAppToBeReady();

        //GIVEN
        smartEditPage.experienceSelectorButton.click();
        smartEditPage.experienceSelector.catalogDropdown.click();
        smartEditPage.experienceSelector.catalogOptions.option(ELECTRONICS_SITE.CATALOGS.ONLINE).click();

        smartEditPage.experienceSelector.languageDropdown.click();
        smartEditPage.experienceSelector.languageOptions.option(ELECTRONICS_SITE.LANGUAGES.POLISH).click();

        //WHEN
        smartEditPage.experienceSelector.applyButton.click();
        browser.waitForWholeAppToBeReady();
        browser.wait(protractor.ExpectedConditions.elementToBeClickable(element(by.id('experience-selector-btn'))), 5000, 'Experience Selector button not found');
        smartEditPage.experienceSelectorButton.click();

        //THEN
        expect(smartEditPage.experienceSelector.catalogSelectedOption.getText()).toBe('Electronics Content Catalog - Online');
        expect(smartEditPage.experienceSelector.languageSelectedOption.getText()).toBe('Polish');
        smartEditPage.experienceSelector.timeField.getAttribute('placeholder').then(function(datePlaceholder) {
            expect(datePlaceholder).toBe('Select a Date and Time');
        });
    });

    it("GIVEN I'm in the experience selector AND I've changed the values in the editor fields WHEN I click cancel AND I re-open the experience selector THEN I expect to see the currently selected experience", function() {
        browser.waitForWholeAppToBeReady();

        //GIVEN
        smartEditPage.experienceSelectorButton.click();
        smartEditPage.experienceSelector.catalogDropdown.click();
        smartEditPage.experienceSelector.catalogOptions.option(ELECTRONICS_SITE.CATALOGS.STAGED).click();

        smartEditPage.experienceSelector.timeField.click();
        smartEditPage.experienceSelector.timeField.sendKeys("01/01/2016 12:00 AM");

        smartEditPage.experienceSelector.languageDropdown.click();
        smartEditPage.experienceSelector.languageOptions.option(ELECTRONICS_SITE.LANGUAGES.ITALIAN).click();

        //WHEN
        smartEditPage.clickInApplication();
        smartEditPage.experienceSelectorButton.click();

        //THEN
        expect(smartEditPage.experienceSelector.catalogSelectedOption.getText()).toBe('Electronics Content Catalog - Online');
        expect(smartEditPage.experienceSelector.languageSelectedOption.getText()).toBe('English');
        smartEditPage.experienceSelector.timeField.getAttribute('placeholder').then(function(datePlaceholder) {
            expect(datePlaceholder).toBe('Select a Date and Time');
        });
    });

    it("GIVEN I have selected an experience without a time WHEN I click the apply button AND the REST call to the preview service succeeds THEN I expect the payload to match the API's expected payload", function() {
        browser.waitForWholeAppToBeReady();

        // GIVEN
        smartEditPage.experienceSelectorButton.click();
        smartEditPage.experienceSelector.catalogDropdown.click();
        smartEditPage.experienceSelector.catalogOptions.option(ELECTRONICS_SITE.CATALOGS.ONLINE).click();

        smartEditPage.experienceSelector.languageDropdown.click();
        smartEditPage.experienceSelector.languageOptions.option(ELECTRONICS_SITE.LANGUAGES.POLISH).click();

        // WHEN
        smartEditPage.experienceSelector.applyButton.click();
        browser.waitForWholeAppToBeReady();

        // THEN
        smartEditPage.iframe.getAttribute('src').then(function(src) {
            expect(src).toMatch(/.*\/smarteditcontainerJSTests\/e2e\/dummystorefront.html\?cmsTicketId\=validTicketId1$/);
        });
    });

    it("GIVEN I have selected an experience with a time WHEN I click the apply button AND the REST call to the preview service succeeds THEN I expect the payload to match the API's expected payload", function() {
        browser.waitForWholeAppToBeReady();

        // GIVEN
        smartEditPage.experienceSelectorButton.click();
        smartEditPage.experienceSelector.catalogDropdown.click();
        smartEditPage.experienceSelector.catalogOptions.option(ELECTRONICS_SITE.CATALOGS.ONLINE).click();

        smartEditPage.experienceSelector.timeField.click();
        smartEditPage.experienceSelector.timeField.sendKeys("01/01/2016 1:00 PM");

        smartEditPage.experienceSelector.languageDropdown.click();
        smartEditPage.experienceSelector.languageOptions.option(ELECTRONICS_SITE.LANGUAGES.POLISH).click();

        // WHEN
        smartEditPage.experienceSelector.applyButton.click();
        browser.waitForWholeAppToBeReady();

        // THEN
        smartEditPage.iframe.getAttribute('src').then(function(src) {
            expect(src).toMatch(/.*\/smarteditcontainerJSTests\/e2e\/dummystorefront.html\?cmsTicketId\=validTicketId2$/);
        });
    });

    it("GIVEN that I have deep linked and I have selected a new experience with a time WHEN I click the apply button AND the REST call to the preview service succeeds THEN I expect to reload the page to which I have deep linked without a preview ticket", function() {
        browser.waitForWholeAppToBeReady();

        browser.switchToIFrame();

        browser.click(by.id("deepLink"));

        browser.switchToParent();

        browser.waitForWholeAppToBeReady();

        // GIVEN
        smartEditPage.experienceSelectorButton.click();
        smartEditPage.experienceSelector.catalogDropdown.click();
        smartEditPage.experienceSelector.catalogOptions.option(ELECTRONICS_SITE.CATALOGS.ONLINE).click();

        smartEditPage.experienceSelector.timeField.click();
        smartEditPage.experienceSelector.timeField.sendKeys("01/01/2016 1:00 PM");

        smartEditPage.experienceSelector.languageDropdown.click();
        smartEditPage.experienceSelector.languageOptions.option(ELECTRONICS_SITE.LANGUAGES.POLISH).click();

        // WHEN
        smartEditPage.experienceSelector.applyButton.click();
        browser.waitForWholeAppToBeReady();

        // THEN
        smartEditPage.iframe.getAttribute('src').then(function(src) {
            expect(src).toMatch(/.*\/smarteditcontainerJSTests\/e2e\/dummystorefrontSecondPage.html$/);
        });
    });


});
