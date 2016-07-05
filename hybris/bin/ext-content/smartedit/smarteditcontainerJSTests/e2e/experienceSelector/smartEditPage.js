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
(function() {
    function SmartEditPage() {
        this.pageUri = 'smarteditcontainerJSTests/e2e/experienceSelector/experienceSelectorTest.html';
        browser.get(this.pageUri);
        browser.waitForContainerToBeReady();
    }

    SmartEditPage.prototype = Object.create({}, {
        // ELEMENTS
        experienceSelectorButton: {
            get: function() {
                return element(by.id('experience-selector-btn', 'Experience Selector button not found'));
            }
        },
        experienceSelector: {
            value: Object.create({}, {
                widgetText: {
                    get: function() {
                        return element(by.css('[id=\'experience-selector-btn\'] > span:first-child', 'Selector widget not found')).getText();
                    }
                },
                catalogFieldLabel: {
                    get: function() {
                        return element(by.id('catalog-label', 'Experience Selector Catalog Field Label not found'));
                    }
                },
                catalogSelectedOption: {
                    get: function() {
                        return element(by.css('[id=\'catalog\'] span[ng-bind=\'$select.selected.label\']', 'Experience Selector Catalog Field not found'));
                    }
                },
                catalogOptions: {
                    value: Object.create({}, {
                        option: {
                            value: function(index) {
                                return element(by.css('[id=\'catalog\'] ul[role=\'listbox\'] li[role=\'option\']:nth-child(' + index + ') span'));
                            }
                        },
                        options: {
                            get: function() {
                                return element.all(by.css('[id=\'catalog\'] ul[role=\'listbox\'] li[role=\'option\'] span'));
                            }
                        }
                    })
                },
                catalogDropdown: {
                    get: function() {
                        return element(by.css('[id=\'catalog\'] [class^=\'ui-select-container\'] > a'));
                    }
                },
                dateTimePickerButton: {
                    get: function() {
                        return element(by.css('[id=\'time\'] div[class*=\'date\'] span[class=\'input-group-addon\']'));
                    }
                },
                timeFieldLabel: {
                    get: function() {
                        return element(by.id('time-label', 'Experience Selector Date and Time Field Label not found'));
                    }
                },
                timeField: {
                    get: function() {
                        return element(by.css('input[name=\'time\']', 'Experience Selector Date and Time Field not found'));
                    }
                },
                languageFieldLabel: {
                    get: function() {
                        return element(by.id('language-label', 'Experience Selector Language Field Label not found'));
                    }
                },
                languageSelectedOption: {
                    get: function() {
                        return element(by.css('[id=\'language\'] span[ng-bind=\'$select.selected.label\']', 'Experience Selector Language Field not found'));
                    }
                },
                languageOptions: {
                    value: Object.create({}, {
                        option: {
                            value: function(index) {
                                return element(by.css('[id=\'language\'] ul[role=\'listbox\'] li[role=\'option\']:nth-child(' + index + ') span'));
                            }
                        },
                        options: {
                            get: function() {
                                return element.all(by.css('[id=\'language\'] ul[role=\'listbox\'] li[role=\'option\'] span'));
                            }
                        }
                    })
                },
                languageDropdown: {
                    get: function() {
                        return element(by.css('[id=\'language\'] [class^=\'ui-select-container\'] > a'));
                    }
                },
                applyButton: {
                    get: function() {
                        return element(by.id('submit', 'Experience Selector Apply Button not found'));
                    }
                },
                cancelButton: {
                    get: function() {
                        return element(by.id('cancel', 'Experience Selector Cancel Button not found'));
                    }
                }
            })
        },
        iframe: {
            get: function() {
                return element(by.css('#js_iFrameWrapper iframe', 'iFrame not found'));
            }
        },
        alert: {
            get: function() {
                return element(by.binding('alert.message'));
            }
        },

        // ACTIONS
        openExperienceSelector: {
            value: function() {
                this.experienceSelectorButton.click();
            }
        },
        clickInIframe: {
            value: function() {
                browser.switchToIFrame();
                element(by.css('.offset1')).click();
                browser.switchToParent();
            }
        },
        clickInApplication: {
            value: function() {
                element(by.css('.ySmartEditAppLogo')).click();
            }
        },
        selectExpectedDate: {
            value: function() {
                element(by.css('div[class*=\'datepicker-days\'] th[class*=\'picker-switch\']')).click();
                element(by.css('div[class*=\'datepicker-months\'] th[class*=\'picker-switch\']')).click();
                element(by.cssContainingText('span[class*=\'year\']', '2016')).click();
                element(by.css('span[class*=\'month\']:first-child')).click();
                element(by.xpath('.//*[.="1" and contains(@class,\'day\') and not(contains(@class, \'old\')) and not(contains(@class, \'new\'))]')).click();
                element(by.css('span[class*=\'glyphicon-time\']')).click();
                element(by.css('div[class=\'timepicker-picker\'] tr:first-child td:first-child a')).click();
                element(by.css('div[class=\'timepicker-picker\'] tr:nth-child(2) button')).click();
            }
        }
    });

    module.exports = SmartEditPage;
})();
