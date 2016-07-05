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
selectLocalizedTab = function(language, qualifier, isHidden) {

    if (isHidden) {
        element.all(by.xpath("//*[@id='" + qualifier + "']//*[@data-toggle='dropdown']")).click();
    }

    element.all(by.xpath("//*[@id='" + qualifier + "']//*[@data-tab-id='" + language + "']")).click();
};

switchToIframeForRichTextAndAddContent = function(iframeId, dataId, content) {

    browser.switchTo().frame(element(by.id(iframeId)).getWebElement(''));
    browser.driver.findElement(by.css("[data-id='" + dataId + "']")).sendKeys(content);

};

switchToIframeForRichTextAndValidateContent = function(iframeId, dataId, content) {

    browser.switchTo().frame(element(by.id(iframeId)).getWebElement(''));
    expect(browser.driver.findElement(by.css("[data-id='" + dataId + "']")).getText()).toEqual(content);

};

getValidationErrorElements = function(qualifier) {

    return element.all(by.xpath("//*[@validation-id='" + qualifier + "']//span[starts-with(@id, 'validation-error')]"));
};


addMedia = function(language, searchKey) {

    browser.wait(protractor.ExpectedConditions.elementToBeClickable(element(by.xpath("//*[@id='media']//li[@data-tab-id='" + language + "']"))), 20000, "could not find tab for language: " + language);
    element.all(by.xpath("//*[@id='media']//li[@data-tab-id='" + language + "']")).click();
    browser.wait(protractor.ExpectedConditions.elementToBeClickable(element(by.xpath("//*[@data-tab-id='" + language + "']//*[text()='Search...']"))), 20000, "could not find 'Search...' placeholder for language tab: " + language);
    element.all(by.xpath("//*[@data-tab-id='" + language + "']//*[text()='Search...']")).click();
    browser.wait(protractor.ExpectedConditions.visibilityOf(element(by.xpath("//*[@data-tab-id='" + language + "']//div[@id='media-selector']//input[@aria-label='Select box']"))), 20000, "could not enter mask in media search for language tab: " + language);
    element(by.xpath("//*[@data-tab-id='" + language + "']//div[@id='media-selector']//input[@aria-label='Select box']")).sendKeys(searchKey);
    browser.wait(protractor.ExpectedConditions.elementToBeClickable(element(by.xpath("//*[@data-tab-id='" + language + "']//li[@role='option']"))), 20000, "could not click on media selection for language tab: " + language);
    element(by.xpath("//*[@data-tab-id='" + language + "']//li[@role='option']")).click();

};

getMediaElement = function(language) {

    return element(by.xpath("//*[@data-tab-id='" + language + "']//img[@id='image-media']"));
};

switchToDefaultContent = function() {

    browser.driver.switchTo().defaultContent();

};
