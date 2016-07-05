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
describe("GenericEditor form save", function() {

    getValidationErrorElementByLanguage = function(qualifier, language) {
        return element(by.id('validation-error-' + qualifier + '-' + language + '-0'));
    };


    beforeEach(function() {
        require("../commonFunctions.js");
        browser.get('smarteditcontainerJSTests/e2e/genericEditor/componentSave/genericEditorTest.html');
    });

    it("will display cancel button and not display submit button by default", function() {

        expect(element(by.id('cancel')).isPresent()).toBeFalsy();
        expect(element(by.id('submit')).isPresent()).toBeFalsy();

    });

    it("will display cancel and submit buttons when component attribute is modified", function() {

        element(by.name('headline')).sendKeys("I have changed");
        expect(element(by.id('cancel')).isPresent()).toBeTruthy();
        expect(element(by.id('submit')).isPresent()).toBeTruthy();

    });

    it("will display validation errors for headline when headline is modified and saved", function() {

        element(by.name('headline')).clear().sendKeys("I have changed to an invalid headline with two validation errors, % and lots of text");
        browser.click(by.id('submit'));
        var elements = getValidationErrorElements('headline');

        expect(elements.count()).toBe(2);

        expect(elements.get(0).getAttribute('id')).toBe('validation-error-headline-headline-0');
        expect(elements.get(1).getAttribute('id')).toBe('validation-error-headline-headline-1');

    });

    it("will display a validation error for only 'en' and 'it' tab for content when content of 'en' and 'it' tab's are modified and saved", function() {

        browser.click(by.id('mceu_15-open'));
        browser.click(by.id('mceu_156'));

        switchToIframeForRichTextAndAddContent("ui-tinymce-0_ifr", "ui-tinymce-0", "I have changed to an invalid content with one validation error");
        switchToDefaultContent();

        selectLocalizedTab('it', 'content', false);
        browser.click(by.id('mceu_77-open'));
        browser.click(by.id('mceu_157'));

        switchToIframeForRichTextAndAddContent("ui-tinymce-2_ifr", "ui-tinymce-2", "Ho cambiato ad un contenuto non valido con un errore di validazione");
        switchToDefaultContent();

        browser.click(by.id('submit'));
        browser.sleep(2000);

        selectLocalizedTab('en', 'content', false);
        expect(getValidationErrorElementByLanguage('content', 'en').getText()).toEqual("This field is required and must to be between 1 and 255 characters long.");

        selectLocalizedTab('it', 'content', false);
        expect(getValidationErrorElementByLanguage('content', 'it').getText()).toEqual("This field is required and must to be between 1 and 255 characters long.");

        browser.driver.manage().timeouts().implicitlyWait(0);

        selectLocalizedTab('fr', 'content', false);
        expect(getValidationErrorElementByLanguage('content', 'fr').isPresent()).toBe(false);

        selectLocalizedTab('pl', 'content', true);
        expect(getValidationErrorElementByLanguage('content', 'pl').isPresent()).toBe(false);

        selectLocalizedTab('hi', 'content', true);
        expect(getValidationErrorElementByLanguage('content', 'hi').isPresent()).toBe(false);

        browser.driver.manage().timeouts().implicitlyWait(5000);
    });

    it("will remove validation errors when reset is clicked after contents are modified and saved", function() {

        element(by.name('headline')).clear().sendKeys("I have changed to an invalid headline with two validation errors, % and lots of text");
        browser.click(by.id('submit'));

        expect(element(by.css("[id^='validation-error']")).isPresent()).toBeTruthy();

        browser.click(by.id('cancel'));

        expect(element(by.css("[id^='validation-error']")).isPresent()).toBeFalsy();

    });

    it("will display 2 validation errors, then on second save will display 1 validation error for headline", function() {

        element(by.name('headline')).clear().sendKeys("I have changed to an invalid headline with two validation errors, % and lots of text");
        browser.click(by.id('submit'));
        var elements = element.all(by.xpath("//*[@validation-id='headline']//span[starts-with(@id, 'validation-error')]"));

        expect(elements.count()).toBe(2);

        expect(elements.get(0).getAttribute('id')).toBe('validation-error-headline-headline-0');
        expect(elements.get(1).getAttribute('id')).toBe('validation-error-headline-headline-1');

        element(by.name('headline')).clear().sendKeys("I have changed to an invalid headline with one validation error, %");
        browser.click(by.id('submit'));

        expect(elements.count()).toBe(1);

        expect(elements.get(0).getAttribute('id')).toBe('validation-error-headline-headline-0');
    });

    it("will display no validation errors when submit is clicked and when API returns a field that does not exist", function() {

        element(by.name('headline')).clear().sendKeys("Checking unknown type");
        browser.click(by.id('submit'));

        expect(element(by.css("[id^='validation-error']")).isPresent()).toBeFalsy();

    });

    it("will display validation error when submit is clicked without image being uploaded (image is removed)", function() {

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(element(by.id('image-media'))), 20000, "could not find media item");

        browser.click(by.id('media-replaceImage'));
        browser.click(by.id('submit'));

        expect(getValidationErrorElementByLanguage('media', 'en').isDisplayed()).toBe(true);

        browser.driver.manage().timeouts().implicitlyWait(0);

        expect(getValidationErrorElementByLanguage('media', 'fr').isDisplayed()).toBe(false);

        expect(getValidationErrorElementByLanguage('media', 'it').isPresent()).toBe(false);

        expect(getValidationErrorElementByLanguage('media', 'pl').isPresent()).toBe(false);

        expect(getValidationErrorElementByLanguage('media', 'hi').isPresent()).toBe(false);

        browser.driver.manage().timeouts().implicitlyWait(5000);

    });

    it("will display validation error when submit is clicked without image being uploaded (click on replace image)", function() {

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(element(by.id('image-media'))), 20000, "could not find media item");

        browser.click(by.id('media-replaceImage'));
        browser.click(by.id('id-shortstring'));
        browser.click(by.id('submit'));


        expect(getValidationErrorElementByLanguage('media', 'en').isDisplayed()).toBe(true);

        browser.driver.manage().timeouts().implicitlyWait(0);

        expect(getValidationErrorElementByLanguage('media', 'fr').isDisplayed()).toBe(false);

        expect(getValidationErrorElementByLanguage('media', 'it').isPresent()).toBe(false);

        expect(getValidationErrorElementByLanguage('media', 'pl').isPresent()).toBe(false);

        expect(getValidationErrorElementByLanguage('media', 'hi').isPresent()).toBe(false);

        browser.driver.manage().timeouts().implicitlyWait(5000);

    });

    it("will show the selected media selected for only 'fr' language when a media is selected for 'fr' language and submit is clicked", function() {

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(element(by.id('image-media'))), 20000, "could not find media item");

        addMedia('fr', 'trash');

        browser.click(by.id('submit'));

        var media_en = getMediaElement('en');
        expect(media_en.getAttribute('data-ng-src')).toEqual('web/webroot/icons/clone_bckg.png');

        var media_fr = getMediaElement('fr');
        expect(media_fr.getAttribute('data-ng-src')).toEqual('web/webroot/icons/trash_bckg.png');

    });

});
