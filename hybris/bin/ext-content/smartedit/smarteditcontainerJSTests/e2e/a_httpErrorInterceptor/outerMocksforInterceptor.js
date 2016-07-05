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
angular
    .module('e2eBackendMocks', ['ngMockE2E', 'resourceLocationsModule', 'languageServiceModule'])
    .constant('SMARTEDIT_ROOT', 'web/webroot')
    .constant('SMARTEDIT_RESOURCE_URI_REGEXP', /^(.*)\/smarteditcontainerJSTests/)
    .run(
        function($httpBackend, languageService, I18N_RESOURCE_URI) {

            var map = [{
                "id": "2",
                "value": "\"thepreviewTicketURI\"",
                "key": "previewTicketURI"
            }, {
                "id": "3",
                "value": "{\"smartEditLocation\":\"/smarteditcontainerJSTests/e2e/a_httpErrorInterceptor/dummyCmsDecorators.js\"}",
                "key": "applications.CMSApp"
            }, {
                "id": "4",
                "value": "{\"smartEditLocation\":\"/smarteditcontainerJSTests/e2e/a_httpErrorInterceptor/innerMocksforInterceptor.js\"}",
                "key": "applications.InterceptorMock"
            }, {
                "id": "8",
                "value": "\"somepath\"",
                "key": "i18nAPIRoot"
            }];

            $httpBackend.whenGET(/configuration/).respond(
                function(method, url, data, headers) {
                    return [200, map];
                });

            $httpBackend.whenGET(/cmswebservices\/v1\/sites\/.*\/languages/).respond({
                languages: [{
                    nativeName: 'English',
                    isocode: 'en',
                    name: 'English',
                    required: true
                }]
            });

            $httpBackend.whenPUT(/configuration/).respond(404);

            $httpBackend.whenGET(I18N_RESOURCE_URI + "/" + languageService.getBrowserLocale()).respond({
                "modal.administration.configuration.edit.title": "edit configuration",
                "configurationform.actions.cancel": "cancel",
                "configurationform.actions.submit": "submit",
                "configurationform.actions.close": "close",
                "actions.loadpreview": "load preview",
                'unknown.request.error': 'Your request could not be processed! Please try again later!'
            });

            $httpBackend.whenGET(/fragments/).passThrough();

        });
angular.module('smarteditloader').requires.push('e2eBackendMocks');
angular.module('smarteditcontainer').requires.push('e2eBackendMocks');
