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
describe('Editor TabSet', function() {

    beforeEach(function() {
        browser.get('jsTests/cmssmarteditContainer/e2e/features/editorTabset/editorTabset.html');
    });

    it('will load tabset with all default tabs', function() {
        // Arrange/Act
        var tabset = element(by.css('editor-tabset'));

        // - Tab Headers
        var basicInfoTab = element(by.css('editor-tabset ul.nav-tabs li[data-tab-id="basicTab"]'));
        var adminTab = element(by.css('editor-tabset ul.nav-tabs li[data-tab-id="adminTab"]'));
        //var visibilityTab = element(by.css('editor-tabset ul.nav-tabs li[data-tab-id="visibilityTab"]'));//commented for 6.0
        var genericTab = element(by.css('editor-tabset ul.nav-tabs li[data-tab-id="genericTab"]'));

        // - Tab Contents
        var basicInfoTabContent = element(by.css('editor-tabset y-tab[data-tab-id="basicTab"]'));
        var adminTabContent = element(by.css('editor-tabset y-tab[data-tab-id="adminTab"]'));
        //var visibilityTabContent = element(by.css('editor-tabset y-tab[data-tab-id="visibilityTab"]'));//commented for 6.0
        var genericTabContent = element(by.css('editor-tabset y-tab[data-tab-id="genericTab"]'));

        // Assert
        expect(tabset).not.toBeUndefined();

        expect(basicInfoTab).not.toBeUndefined();
        expect(basicInfoTabContent).not.toBeUndefined();
        expect(adminTab).not.toBeUndefined();
        expect(adminTabContent).not.toBeUndefined();
        //expect(visibilityTab).not.toBeUndefined();//commented for 6.0
        //expect(visibilityTabContent).not.toBeUndefined();//commented for 6.0
        expect(genericTab).not.toBeUndefined();
        expect(genericTabContent).not.toBeUndefined();
    });

    it('will load tabset with test tabs', function() {
        // Arrange/Act
        var tabset = element(by.css('editor-tabset'));

        // - Tab Headers
        var sampleTab1 = element(by.css('editor-tabset ul.dropdown-menu li[data-tab-id="tab1"]'));
        var sampleTab2 = element(by.css('editor-tabset ul.dropdown-menu li[data-tab-id="tab2"]'));
        var sampleTab3 = element(by.css('editor-tabset ul.dropdown-menu li[data-tab-id="tab3"]'));

        // - Tab Contents
        var sampleTab1Content = element(by.css('editor-tabset y-tab[data-tab-id="tab1"]'));
        var sampleTab2Content = element(by.css('editor-tabset y-tab[data-tab-id="tab2"]'));
        var sampleTab3Content = element(by.css('editor-tabset y-tab[data-tab-id="tab3"]'));

        // Assert
        expect(tabset).not.toBeUndefined();

        expect(sampleTab1).not.toBeUndefined();
        expect(sampleTab1Content).not.toBeUndefined();
        expect(sampleTab2).not.toBeUndefined();
        expect(sampleTab2Content).not.toBeUndefined();
        expect(sampleTab3).not.toBeUndefined();
        expect(sampleTab3Content).not.toBeUndefined();
    });

    it('clicking on a tab will change the displayed content to the view of the selected tab', function() {
        // Arrange
        var dropDownHeader = getDropDownHeader();
        var targetTab = element(by.css('li[data-tab-id="tab2"]'));
        var targetTabLink = element(by.css('li[data-tab-id="tab2"] a'));
        var targetTabContent = element(by.css('editor-tabset y-tab[data-tab-id="tab2"]'));
        var currentSelectedHeader = element(by.css('li[data-tab-id="genericTab"] a'));
        var currentSelectedBody = element(by.css('editor-tabset y-tab[data-tab-id="genericTab"]'));

        expect(targetTab).not.toBe(currentSelectedHeader);
        expect(targetTab).not.toBeUndefined();
        expect(hasClass(targetTab, 'active')).toBeFalsy();
        expect(targetTabContent).not.toBeUndefined();
        expect(targetTabContent.isDisplayed()).toBeFalsy();
        expect(currentSelectedHeader).not.toBeUndefined();
        expect(currentSelectedBody).not.toBeUndefined();

        // Act
        dropDownHeader.click();
        targetTabLink.click();

        // Assert
        expect(currentSelectedBody.isDisplayed()).toBeFalsy();
        expect(targetTabContent.isDisplayed()).toBeTruthy();
    });

    it('clicking on a tab will change the displayed content to the view of the selected tab even when ' +
        'the tab has validation errors',
        function() {
            // Arrange
            var dropDownHeader = getDropDownHeader();
            var saveButton = element(by.css('#save-button'));
            var targetTab = element(by.css('li[data-tab-id="tab1"]'));
            var targetTabLink = element(by.css('li[data-tab-id="tab1"] a'));
            var targetTabContent = element(by.css('editor-tabset y-tab[data-tab-id="tab1"]'));
            var currentSelectedHeader = element(by.css('li[data-tab-id="tab1"]'));
            var currentSelectedBody = element(by.css('editor-tabset y-tab[data-tab-id="tab1"]'));

            expect(targetTab).not.toBe(currentSelectedHeader);
            expect(targetTab).not.toBeUndefined();
            expect(hasClass(targetTab, 'active')).toBeFalsy();
            expect(targetTabContent).not.toBeUndefined();
            expect(targetTabContent.isDisplayed()).toBeFalsy();
            expect(currentSelectedHeader).not.toBeUndefined();
            expect(currentSelectedBody).not.toBeUndefined();

            saveButton.click().then(function() {
                expect(hasClass(targetTabLink, 'sm-tab-error')).toBeTruthy();
            });

            // Act
            dropDownHeader.click();
            targetTabLink.click();

            // Assert
            expect(targetTabContent.isDisplayed()).toBeTruthy();
        });

    it('clicking on save will execute save on all tabs', function() {
        // Arrange
        var saveButton = element(by.css('#save-button'));
        var targetTabHeader1 = element(by.css('li[data-tab-id="tab1"] a'));
        var targetTabHeader2 = element(by.css('li[data-tab-id="tab2"] a'));
        var targetTabHeader3 = element(by.css('li[data-tab-id="tab3"] a'));

        expect(hasClass(targetTabHeader1, 'sm-tab-error')).toBeFalsy();
        expect(hasClass(targetTabHeader2, 'sm-tab-error')).toBeFalsy();
        expect(hasClass(targetTabHeader3, 'sm-tab-error')).toBeFalsy();

        // Act
        saveButton.click();

        // Assert
        expect(hasClass(targetTabHeader1, 'sm-tab-error')).toBeTruthy();
        expect(hasClass(targetTabHeader2, 'sm-tab-error')).toBeFalsy();
        expect(hasClass(targetTabHeader3, 'sm-tab-error')).toBeTruthy();
    });

    it('clicking on reset will clear all tabs', function() {
        // Arrange
        var saveButton = element(by.css('#save-button'));
        var resetButton = element(by.css('#reset-button'));
        var targetTabHeader1 = element(by.css('li[data-tab-id="tab1"] a'));
        var targetTabHeader2 = element(by.css('li[data-tab-id="tab2"] a'));
        var targetTabHeader3 = element(by.css('li[data-tab-id="tab3"] a'));

        expect(hasClass(targetTabHeader1, 'sm-tab-error')).toBeFalsy();
        expect(hasClass(targetTabHeader2, 'sm-tab-error')).toBeFalsy();
        expect(hasClass(targetTabHeader3, 'sm-tab-error')).toBeFalsy();

        saveButton.click();

        expect(hasClass(targetTabHeader1, 'sm-tab-error')).toBeTruthy();
        expect(hasClass(targetTabHeader2, 'sm-tab-error')).toBeFalsy();
        expect(hasClass(targetTabHeader3, 'sm-tab-error')).toBeTruthy();

        // Act
        resetButton.click();

        // Assert
        expect(hasClass(targetTabHeader1, 'sm-tab-error')).toBeFalsy();
        expect(hasClass(targetTabHeader2, 'sm-tab-error')).toBeFalsy();
        expect(hasClass(targetTabHeader3, 'sm-tab-error')).toBeFalsy();
    });

    it('clicking on cancel will clear all tabs', function() {
        // Arrange
        var saveButton = element(by.css('#save-button'));
        var cancelButton = element(by.css('#cancel-button'));
        var targetTabHeader1 = element(by.css('li[data-tab-id="tab1"] a'));
        var targetTabHeader2 = element(by.css('li[data-tab-id="tab2"] a'));
        var targetTabHeader3 = element(by.css('li[data-tab-id="tab3"] a'));

        expect(hasClass(targetTabHeader1, 'sm-tab-error')).toBeFalsy();
        expect(hasClass(targetTabHeader2, 'sm-tab-error')).toBeFalsy();
        expect(hasClass(targetTabHeader3, 'sm-tab-error')).toBeFalsy();

        saveButton.click();

        expect(hasClass(targetTabHeader1, 'sm-tab-error')).toBeTruthy();
        expect(hasClass(targetTabHeader2, 'sm-tab-error')).toBeFalsy();
        expect(hasClass(targetTabHeader3, 'sm-tab-error')).toBeTruthy();

        // Act
        cancelButton.click();

        // Assert
        expect(hasClass(targetTabHeader1, 'sm-tab-error')).toBeFalsy();
        expect(hasClass(targetTabHeader2, 'sm-tab-error')).toBeFalsy();
        expect(hasClass(targetTabHeader3, 'sm-tab-error')).toBeFalsy();
    });

    function hasClass(element, className) {
        return element.getAttribute('class').then(function(classes) {
            return classes.split(' ').indexOf(className) !== -1;
        });
    }

    function getDropDownHeader() {
        return element.all(by.css('ul.nav.nav-tabs li a.dropdown-toggle')).get(0);
    }
});
