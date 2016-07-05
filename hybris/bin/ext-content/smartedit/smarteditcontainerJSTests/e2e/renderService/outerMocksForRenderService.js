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
    angular
        .module('e2eBackendMocks', ['ngMockE2E', 'resourceLocationsModule', 'languageServiceModule'])
        .constant('SMARTEDIT_ROOT', 'web/webroot')
        .constant('SMARTEDIT_RESOURCE_URI_REGEXP', /^(.*)\/smarteditcontainerJSTests/)
        .run(function($httpBackend, languageService, $location, I18N_RESOURCE_URI, STOREFRONT_PATH) {
            var map = [{
                "id": "2",
                "value": "\"thepreviewTicketURI\"",
                "key": "previewTicketURI"
            }, {
                "id": "7",
                "value": "{\"smartEditLocation\":\"/smarteditcontainerJSTests/e2e/renderService/renderServiceDecorators.js\"}",
                "key": "applications.CMSApp"
            }, {
                "id": "8",
                "value": "{\"smartEditContainerLocation\":\"/smarteditcontainerJSTests/e2e/renderService/dummyOuterModule.js\"}",
                "key": "applications.outerModule"
            }, {
                "id": "9",
                "value": "\"somepath\"",
                "key": "i18nAPIRoot"
            }];

            $httpBackend.whenGET(/configuration/).respond(
                function(method, url, data, headers) {
                    return [200, map];
                });

            $httpBackend.whenGET(I18N_RESOURCE_URI + "/" + languageService.getBrowserLocale()).respond({});


            $httpBackend.whenGET(/cmswebservices\/v1\/sites\/.*\/languages/).respond({
                languages: [{
                    nativeName: 'English',
                    isocode: 'en',
                    name: 'English',
                    required: true
                }]
            });

            $httpBackend.whenGET(/cmswebservices\/sites$/).respond({
                sites: [{
                    ///smarteditcontainerJSTests/e2e/renderService/innerApp.html
                    previewUrl: '/smarteditcontainerJSTests/e2e/renderService/innerApp.html',
                    name: {
                        type: "map",
                        value: {
                            en: "Electronics"
                        }
                    },
                    redirectUrl: 'redirecturlElectronics',
                    uid: 'electronics'
                }, {
                    previewUrl: '/smarteditcontainerJSTests/e2e/renderService/innerApp.html',
                    name: {
                        type: "map",
                        value: {
                            en: "Apparels"
                        }
                    },
                    redirectUrl: 'redirecturlApparels',
                    uid: 'apparel-uk'
                }]
            });

            $httpBackend.whenPOST(/thepreviewTicketURI/)
                .respond({
                    ticketId: 'dasdfasdfasdfa',
                    resourcePath: document.location.origin + '/smarteditcontainerJSTests/e2e/renderService/innerApp.html'
                });

            var pathWithExperience = STOREFRONT_PATH
                .replace(":siteId", "electronics")
                .replace(":catalogId", "electronicsContentCatalog")
                .replace(":catalogVersion", "Online");
            $location.path(pathWithExperience);

        });

    angular.module('smarteditloader').requires.push('e2eBackendMocks');
    angular.module('smarteditcontainer').requires.push('e2eBackendMocks');
})();
