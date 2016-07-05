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
angular.module('backendMocks', ['ngMockE2E', 'functionsModule', 'resourceLocationsModule', 'languageServiceModule'])
    .run(function($httpBackend, filterFilter, parseQuery, I18N_RESOURCE_URI, languageService) {

        $httpBackend.whenGET(I18N_RESOURCE_URI + "/" + languageService.getBrowserLocale()).respond({
            "componentform.actions.cancel": "Cancel",
            "componentform.actions.submit": "Submit",
            "type.thesmarteditComponentType.description.name": "Description"
        });

        $httpBackend.whenGET(/cmswebservices\/v1\/types\/thesmarteditComponentType/).respond(function(method, url, data, headers) {
            var structure = {
                attributes: [{
                    cmsStructureType: "ShortString",
                    qualifier: "description",
                    i18nKey: 'type.thesmarteditComponentType.description.name',
                    localized: false
                }]
            };

            return [200, structure];
        });

        var component = {};
        var ticketId = 0;
        $httpBackend.whenPOST(/previewApi/).respond(function(method, url, data, headers) {
            component = JSON.parse(data);
            ticketId++;
            return [200, {
                ticketId: ticketId
            }];
        });

        $httpBackend.whenGET(/cmswebservices\/v1\/sites\/.*\/languages/).respond({
            languages: [{
                nativeName: 'English',
                isocode: 'en',
                required: true
            }, {
                nativeName: 'French',
                isocode: 'fr',
                required: true
            }, {
                nativeName: 'Italian',
                isocode: 'it'
            }, {
                nativeName: 'Polish',
                isocode: 'pl'
            }, {
                nativeName: 'Hindi',
                isocode: 'hi'
            }]
        });

        $httpBackend.whenGET(/i18n/).passThrough();
        $httpBackend.whenGET(/cmsxstructure/).passThrough();
        $httpBackend.whenGET(/cmsxdata/).passThrough();
        $httpBackend.whenPOST(/cmsxdata/).passThrough();
        $httpBackend.whenPUT(/cmsxdata/).passThrough();
        $httpBackend.whenDELETE(/cmsxdata/).passThrough();
        $httpBackend.whenGET(/view/).passThrough(); //calls to storefront render API
        $httpBackend.whenPUT(/contentslots/).passThrough();
        $httpBackend.whenGET(/\.html/).passThrough();

    });
angular.module('genericEditorApp').requires.push('backendMocks');
