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
    .module('OuterMocks', ['ngMockE2E', 'resourceLocationsModule', 'languageServiceModule'])
    .constant('SMARTEDIT_ROOT', 'web/webroot')
    .constant('SMARTEDIT_RESOURCE_URI_REGEXP', /^(.*)\/smarteditcontainerJSTests/)
    .run(
        function($httpBackend, languageService, I18N_RESOURCE_URI) {

            var map = [{
                "id": "2",
                "value": "\"previewwebservices/v1/preview\"",
                "key": "previewTicketURI"
            }, {
                "id": "9",
                "value": "{\"smartEditLocation\":\"/smarteditcontainerJSTests/e2e/experienceSelector/innerMocks.js\"}",
                "key": "applications.InnerMocks"
            }, {
                "id": "10",
                "value": "{\"smartEditContainerLocation\":\"/smarteditcontainerJSTests/e2e/experienceSelector/experienceSelectorApp.js\"}",
                "key": "applications.experienceSelectorApp"
            }];

            $httpBackend.whenGET(/configuration/).respond(
                function(method, url, data, headers) {
                    return [200, map];
                });

            $httpBackend.whenPUT(/configuration/).respond(404);

            $httpBackend
                .whenGET(I18N_RESOURCE_URI + "/" + languageService.getBrowserLocale())
                .respond({
                    'experience.selector.catalog': 'CATALOG',
                    'experience.selector.date.and.time': 'DATE/TIME',
                    'experience.selector.language': 'LANGUAGE',
                    'componentform.actions.cancel': 'CANCEL',
                    'componentform.actions.apply': 'APPLY',
                    'componentform.select.date': 'Select a Date and Time'
                });

            $httpBackend.whenGET(/fragments/).passThrough();


            $httpBackend.whenGET(/cmswebservices\/v1\/sites\/electronics\/languages/).respond({
                languages: [{
                    nativeName: 'English',
                    isocode: 'en',
                    required: true
                }, {
                    nativeName: 'Polish',
                    isocode: 'pl',
                    required: true
                }, {
                    nativeName: 'Italian',
                    isocode: 'it'
                }]
            });

            $httpBackend.whenGET(/dummystorefront\.html/).respond("<somehtml/>");

            $httpBackend.whenGET(/cmswebservices\/v1\/sites\/apparel-uk\/languages/).respond({
                languages: [{
                    nativeName: 'English',
                    isocode: 'en',
                    required: true
                }, {
                    nativeName: 'French',
                    isocode: 'fr'
                }]
            });

            $httpBackend.whenPOST(/previewwebservices\/v1\/preview/).respond(function(method, url, data) {
                var postedData = angular.fromJson(data);
                for (var ref in postedData) {
                    postedData[ref] = '' + postedData[ref];
                }
                if (postedData.catalog === 'electronicsContentCatalog' &&
                    postedData.catalogVersion === 'Online' &&
                    postedData.language === 'it') {
                    return [400, {
                        errors: [{
                            message: 'CatalogVersion with catalogId \'electronicsContentCatalog\' and version \'Online\' not found!',
                            "type": "UnknownIdentifierError"
                        }]
                    }];
                }

                if (angular.equals(postedData, {
                        catalog: 'electronicsContentCatalog',
                        catalogVersion: 'Online',
                        resourcePath: document.location.origin + '/smarteditcontainerJSTests/e2e/dummystorefront.html',
                        language: 'pl'
                    })) {
                    return ['200', {
                        catalog: 'electronicsContentCatalog',
                        catalogVersion: 'Online',
                        resourcePath: document.location.origin + '/smarteditcontainerJSTests/e2e/dummystorefront.html',
                        language: 'pl',
                        ticketId: 'validTicketId1'
                    }];
                }

                if (angular.equals(postedData, {
                        catalog: 'electronicsContentCatalog',
                        catalogVersion: 'Online',
                        resourcePath: document.location.origin + '/smarteditcontainerJSTests/e2e/dummystorefront.html',
                        time: '2016-01-01T13:00:00' + new Date().toString().match(/([-\+][0-9]+)/)[0],
                        language: 'pl'
                    })) {
                    return ['200', {
                        catalog: 'electronicsContentCatalog',
                        catalogVersion: 'Online',
                        resourcePath: document.location.origin + '/smarteditcontainerJSTests/e2e/dummystorefront.html',
                        language: 'pl',
                        ticketId: 'validTicketId2'
                    }];
                }

                return [200, {
                    resourcePath: document.location.origin + '/smarteditcontainerJSTests/e2e/dummystorefront.html',
                    ticketId: 'validTicketId'
                }];
            });

        });
angular.module('smarteditloader').requires.push('OuterMocks');
angular.module('smarteditcontainer').requires.push('OuterMocks');
