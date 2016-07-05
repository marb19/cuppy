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
angular.module('e2eBackendMocks', ['ngMockE2E', 'resourceLocationsModule', 'languageServiceModule', 'functionsModule'])
    .constant('SMARTEDIT_ROOT', 'web/webroot')
    .constant('SMARTEDIT_RESOURCE_URI_REGEXP', /^(.*)\/smarteditcontainerJSTests/)
    .run(function($httpBackend, languageService, I18N_RESOURCE_URI, copy) {

        var map = [{
            "id": "8796093091245",
            "value": "\"thepreviewTicketURI\"",
            "key": "previewTicketURI"
        }, {
            "id": "432534534645",
            "value": "{malformed}",
            "key": "i18nAPIRoot"
        }, {
            "id": "446547578787",
            "key": "applications.e2eBackendMocks",
            "value": "{\"smartEditLocation\":\"/smarteditcontainerJSTests/e2e/a_editConfigurations/mocksforConfiguration.js\"}"
        }];

        $httpBackend.whenGET(/configuration/).respond(function(method, url, data, headers) {
            return [200, map];
        });

        $httpBackend.whenPUT(/configuration/).respond(function(method, url, rawData, headers) {
            var key = /configuration\/(.+)/.exec(url)[1];
            var data = JSON.parse(rawData);
            var entry = map.find(function(entry) {
                return entry.key == key;
            });
            entry.key = data.key;
            entry.value = data.value;
            return [200, data];
        });

        $httpBackend.whenPOST(/configuration/).respond(function(method, url, rawData, headers) {
            var data = JSON.parse(rawData);
            data.id = Math.random();
            map.push(data);
            return [200, rawData];
        });

        $httpBackend.whenDELETE(/configuration/).respond(function(method, url, data, headers) {
            var key = /configuration\/(.+)/.exec(url)[1];
            map = map.filter(function(entry) {
                return entry.key != key;
            });
            return [200];
        });

        $httpBackend.whenGET(I18N_RESOURCE_URI + "/" + languageService.getBrowserLocale()).respond({
            "modal.administration.configuration.edit.title": "edit configuration",
            "configurationform.actions.cancel": "cancel",
            "configurationform.actions.submit": "submit",
            "configurationform.actions.close": "close",
            "configurationform.json.parse.error": "this value should be a valid JSON format",
            "configurationform.duplicate.entry.error": "This is a duplicate key",
            "configurationform.save.error": "Save error"
        });

        $httpBackend.whenGET(/fragments/).passThrough();

    });
try {
    angular.module('smarteditloader').requires.push('e2eBackendMocks');
} catch (e) {} //not longer there when smarteditcontainer is bootstrapped
try {
    angular.module('smarteditcontainer').requires.push('e2eBackendMocks');
} catch (e) {} //not there yet when smarteditloader is bootstrapped or in smartedit
