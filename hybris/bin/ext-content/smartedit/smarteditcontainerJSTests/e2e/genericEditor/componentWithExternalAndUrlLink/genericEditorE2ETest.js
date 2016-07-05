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
describe("GenericEditor form view", function() {

    beforeEach(function() {
        require("../commonFunctions.js");
        browser.get('smarteditcontainerJSTests/e2e/genericEditor/componentWithExternalAndUrlLink/genericEditorTest.html');
    });

    it("will load 'Component ID' as default content for ID attribute", function() {

        var val = element(by.name('id')).getAttribute('value');
        expect(val).toEqual('Component ID');

    });

    it("will set 'The Headline' as default content for Headline attribute", function() {

        var val = element(by.name('headline')).getAttribute('value');
        expect(val).toEqual('The Headline');

    });

    it("will set different content for each language as default content for 'Content' attribute", function() {

        switchToIframeForRichTextAndValidateContent('ui-tinymce-0_ifr', 'ui-tinymce-0', 'the content to edit');
        switchToDefaultContent();

        selectLocalizedTab('fr', 'content', false);
        switchToIframeForRichTextAndValidateContent('ui-tinymce-1_ifr', 'ui-tinymce-1', 'le contenu a editer');
        switchToDefaultContent();

        selectLocalizedTab('it', 'content', false);
        switchToIframeForRichTextAndValidateContent('ui-tinymce-2_ifr', 'ui-tinymce-2', 'il contenuto da modificare');
        switchToDefaultContent();

        selectLocalizedTab('pl', 'content', true);
        switchToIframeForRichTextAndValidateContent('ui-tinymce-3_ifr', 'ui-tinymce-3', 'tresc edytowac');
        switchToDefaultContent();

        selectLocalizedTab('hi', 'content', true);
        switchToIframeForRichTextAndValidateContent('ui-tinymce-4_ifr', 'ui-tinymce-4', 'Sampaadit karanee kee liee saamagree');
        switchToDefaultContent();

    });

    it("will set 'vertical' as default content for Orientation attribute", function() {

        expect(element(by.xpath("//span[@id='enum-orientation']")).getText()).toEqual('Vertical');

    });


    it("will set active checkbox to selected", function() {

        expect(element(by.name('active')).isSelected()).toBeTruthy();

    });

    it("will set enabled checkbox not un-selected", function() {

        expect(element(by.name('enabled')).isSelected()).toBeFalsy();

    });

    it("will set 'Existing Page' radio button to selected and 'urlLink' value to 'myPageUrl'", function() {

        expect(element(by.id("external")).isSelected()).toBeFalsy();
        expect(element(by.id("internal")).isSelected()).toBeTruthy();
        expect(element(by.name('urlLink')).getAttribute('value')).toEqual('myPageUrl');

    });

    it("will set Media attribute of 'en' and 'hi' tabs to display 'clone_bckg.png' and 'dnd_bckg.png' respectively and no media is selected for the rest of the tabs", function() {

        var media_en = getMediaElement('en');
        expect(media_en.getAttribute('data-ng-src')).toEqual('web/webroot/icons/clone_bckg.png');

        selectLocalizedTab('fr', 'media', false);
        var media_fr = getMediaElement('fr');
        expect(media_fr.getAttribute('data-ng-src')).toBe(null);

        selectLocalizedTab('it', 'media', false);
        var media_it = getMediaElement('it');
        expect(media_it.getAttribute('data-ng-src')).toBe(null);

        selectLocalizedTab('pl', 'media', true);
        var media_pl = getMediaElement('pl');
        expect(media_pl.getAttribute('data-ng-src')).toBe(null);

        selectLocalizedTab('hi', 'media', true);
        var media_hi = getMediaElement('hi');
        expect(media_hi.getAttribute('data-ng-src')).toEqual('web/webroot/icons/dnd_bckg.png');

    });

    it("will set the description of the selected media", function() {
        var theCaption = element(by.xpath("//div[@id='media']//img[@id='image-media']/following-sibling::span[1]")).getText();
        expect(theCaption).toEqual("clone4");
    });

    it("will allow to search for media and auto filter contents", function() {

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(element(by.id('image-media'))), 20000, "could not find media item");

        var media_en = element(by.xpath("//*[@id='media']//*[@data-tab-id='en']//img[@id='image-media']"));
        media_en.click();

        var elements = element.all(by.xpath("//div[@id='media-selector']//ul[@id='media-list']//ul[@role='listbox']/li[@role='option']"));

        element(by.xpath("//*[@data-tab-id='en']//div[@id='media-selector']//input[@aria-label='Select box']")).sendKeys("rol");
        expect(elements.count()).toBe(4);

        element(by.xpath("//*[@data-tab-id='en']//div[@id='media-selector']//input[@aria-label='Select box']")).clear().sendKeys("trash");
        expect(elements.count()).toBe(1);

    });

    it("will set the description of the search result media", function() {

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(element(by.id('image-media'))), 20000, "could not find media item");

        var media_en = element(by.xpath("//*[@id='media']//*[@data-tab-id='en']//img[@id='image-media']"));
        media_en.click();

        var elements = element.all(by.xpath("//div[@id='media-selector']//ul[@id='media-list']//ul[@role='listbox']/li[@role='option']"));

        element(by.xpath("//*[@data-tab-id='en']//div[@id='media-selector']//input[@aria-label='Select box']")).clear().sendKeys("trash");
        expect(elements.count()).toBe(1);
        var theCaption = element(by.xpath("//div[@id='media-selector']//ul[@id='media-list']//ul[@role='listbox']/li[@role='option']//small/span")).getText();
        expect(theCaption).toEqual("trash3");
    });

    it("given both urlLink and external attributes are present, when the component is rendered then external is a radio selection with false selected and urllink is on the same line as external", function() {

        expect(element(by.css("[id='external-checkbox']")).isPresent()).toBe(false);

        expect(element.all(by.css("[name='external']")).count()).toBe(2);

        expect(element(by.css("[id='external']")).isPresent()).toBe(true);
        expect(element(by.css("[id='urlLink']")).isPresent()).toBe(true);

        expect(element(by.css("[id='external']")).isSelected()).toBe(false);
        expect(element(by.css("[id='internal']")).isSelected()).toBe(true);

        expect(element(by.css("[id='urlLink']")).isPresent()).toBe(true);
        expect(element(by.css("[id='urlLink-shortstring']")).isPresent()).toBe(false);

    });

    it('GIVEN I am editing a component WHEN I enter dangerous characters in a ShortString or LongString CMS component type THEN the genericEditor will escape those dangerous characters', function() {

        element(by.name('id')).clear().sendKeys("<script>var x = new XMLHttpRequest()</script>Foo Bar");
        element(by.name('headline')).clear().sendKeys("<button onclick='alert(1);'>Will trigger XSS</button>");

        browser.sleep('500');
        browser.click(by.id('submit'));

        expect(element(by.name('id')).getAttribute('value')).toBe('&lt;script&gt;var x = new XMLHttpRequest()&lt;/script&gt;Foo Bar');
        expect(element(by.name('headline')).getAttribute('value')).toBe('&lt;button onclick=&apos;alert(1);&apos;&gt;Will trigger XSS&lt;/button&gt;');

    });

});
