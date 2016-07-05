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
//TODO: this should be moved to a global JS util file
afterEach(function() {

    if (global.waitForSprintDemoLogTime !== null && global.waitForSprintDemoLogTime > 0) {
        // List logs
        var logs = browser.driver.manage().logs(),
            logType = 'browser'; // browser
        logs.getAvailableLogTypes().then(function(logTypes) {
            if (logTypes.indexOf(logType) > -1) {
                browser.driver.manage().logs().get(logType).then(function(logsEntries) {

                    var len = logsEntries.length;
                    for (var i = 0; i < len; ++i) {

                        var logEntry = logsEntries[i];
                        var showLog = hasLogLevel(logEntry.level.name, global.sprintDemoLogLevels);
                        if (showLog) {
                            waitForSprintDemo(global.waitForSprintDemoLogTime);
                            try {
                                var msg = JSON.parse(logEntry.message);
                                console.log(msg.message.text);
                            } catch (err) {
                                if (global.sprintDemoShowLogParsingErrors) {
                                    console.log("Error parsing log:  " + logEntry.message);
                                }
                            }
                        }
                    }
                }, null);
            }
        });
    }
});

function hasLogLevel(logLevelName, sprintDemoLogLevels) {
    var len = sprintDemoLogLevels.length;
    for (var i = 0; i < len; ++i) {
        if (sprintDemoLogLevels[i] == logLevelName) {
            return true;
        }
    }
    return false;
}

function waitForSprintDemo(milliseconds) {
    var start = new Date().getTime();
    for (var i = 0; i < 1e7; i++) {
        if ((new Date().getTime() - start) > milliseconds) {
            break;
        }
    }
}







describe("authentication API", function() {

    beforeEach(function() {
        browser.driver.manage().deleteAllCookies();
        browser.get('http://127.0.0.1:7000/smarteditcontainerJSTests/e2e/apiAuthentication/apiAuthenticationTest.html');
    });
    afterEach(function() {
        browser.driver.manage().deleteAllCookies();
    });

    var MAIN_AUTH_SUFFIX = 'L2F1dGhvcml6YXRpb25zZXJ2ZXIvb2F1dGgvdG9rZW4';
    var FAKE1_AUTH_SUFFIX = 'L2F1dGhFbnRyeVBvaW50MQ';
    var FAKE2_AUTH_SUFFIX = 'L2F1dGhFbnRyeVBvaW50Mg';

    it("when first login, user is presented with a login dialog",
        function() {

            browser.wait(protractor.ExpectedConditions.elementToBeClickable(element(by.id('username_' + MAIN_AUTH_SUFFIX))), 20000, "username input field is not clickable");
            browser.wait(protractor.ExpectedConditions.elementToBeClickable(element(by.id('password_' + MAIN_AUTH_SUFFIX))), 20000, "password input field is not clickable");
            browser.wait(protractor.ExpectedConditions.elementToBeClickable(element(by.id('submit_' + MAIN_AUTH_SUFFIX))), 20000, "submit input field is not clickable");

        });

    it("submitting an empty auth form will cause an error",
        function() {

            browser.click(by.id('submit_' + MAIN_AUTH_SUFFIX));
            expect(element(by.id('requiredError')).getText()).toBe('Username and password required');
        });

    it("submitting wrong credentials will cause an error",
        function() {

            element(by.id('username_' + MAIN_AUTH_SUFFIX)).sendKeys('admin');
            element(by.id('password_' + MAIN_AUTH_SUFFIX)).sendKeys('nimda');
            browser.click(by.id('submit_' + MAIN_AUTH_SUFFIX));
            expect(element(by.id('invalidError')).getText()).toBe('Invalid username or password');
        });


    it("submitting right credentials will cause successful authentication",
        function() {

            element(by.id('username_' + MAIN_AUTH_SUFFIX)).sendKeys('customermanager');
            element(by.id('password_' + MAIN_AUTH_SUFFIX)).sendKeys('123');
            browser.click(by.id('submit_' + MAIN_AUTH_SUFFIX));
            browser.waitForContainerToBeReady();
        });

    describe("after successful first auth", function() {

        beforeEach(function() {
            browser.driver.manage().deleteAllCookies();
            browser.wait(protractor.ExpectedConditions.elementToBeClickable(element(by.id('username_' + MAIN_AUTH_SUFFIX))), 10000, "username input field is not clickable");
            element(by.id('username_' + MAIN_AUTH_SUFFIX)).sendKeys('customermanager');
            element(by.id('password_' + MAIN_AUTH_SUFFIX)).sendKeys('123');
            browser.click(by.id('submit_' + MAIN_AUTH_SUFFIX));
            browser.waitForContainerToBeReady();
            browser.switchToIFrame();

            browser.wait(protractor.ExpectedConditions.elementToBeClickable(element(by.id('submit_' + FAKE2_AUTH_SUFFIX))), 20000, "submit input field is not clickable in iframe login 2");
        });

        it("user is presented with another login dialog and neither fake 1 nor fake 2 are visible",
            function() {

                expect(element(by.id('fake1')).isPresent()).toBe(false);
                expect(element(by.id('fake2')).isPresent()).toBe(false);
            });

        it("submitting right credentials to fake2 api will cause successful authentication and fake 2 appears",
            function() {

                element(by.id('username_' + FAKE2_AUTH_SUFFIX)).sendKeys('customermanager');
                element(by.id('password_' + FAKE2_AUTH_SUFFIX)).sendKeys('12345');
                browser.click(by.id('submit_' + FAKE2_AUTH_SUFFIX));

                expect(element(by.id('fake1')).isPresent()).toBe(false);
                expect(element(by.id('fake2')).isPresent()).toBe(true);

            });

        it("submitting right credentials to fake2 AND fake1 api will cause successful authentications and both fake 2 and fake 1 appear",
            function() {

                element(by.id('username_' + FAKE2_AUTH_SUFFIX)).sendKeys('customermanager');
                element(by.id('password_' + FAKE2_AUTH_SUFFIX)).sendKeys('12345');
                browser.click(by.id('submit_' + FAKE2_AUTH_SUFFIX));

                expect(element(by.id('fake1')).isPresent()).toBe(false);
                expect(element(by.id('fake2')).isPresent()).toBe(true);

                browser.wait(protractor.ExpectedConditions.elementToBeClickable(element(by.id('submit_' + FAKE1_AUTH_SUFFIX))), 20000, "submit input field is not clickable in iframe login 1");
                element(by.id('username_' + FAKE1_AUTH_SUFFIX)).sendKeys('customermanager');
                element(by.id('password_' + FAKE1_AUTH_SUFFIX)).sendKeys('1234');
                browser.click(by.id('submit_' + FAKE1_AUTH_SUFFIX));

                expect(element(by.id('fake1')).isPresent()).toBe(true);
                expect(element(by.id('fake2')).isPresent()).toBe(true);

            });

    });

});
