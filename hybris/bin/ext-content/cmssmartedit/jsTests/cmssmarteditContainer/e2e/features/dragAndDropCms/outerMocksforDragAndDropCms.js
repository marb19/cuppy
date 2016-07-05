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
    .module('e2eBackendMocks', ['ngMockE2E', 'functionsModule', 'resourceLocationsModule', 'languageServiceModule'])
    .constant('SMARTEDIT_ROOT', 'buildArtifacts')
    .constant('SMARTEDIT_RESOURCE_URI_REGEXP', /^(.*)\/jsTests/)
    .run(
        function($httpBackend, I18N_RESOURCE_URI, languageService, parseQuery) {

            var map = [{
                "id": "2",
                "value": "\"/thepreviewTicketURI\"",
                "key": "previewTicketURI"
            }, {
                "id": "7",
                "value": "{\"smartEditLocation\":\"/jsTests/cmssmarteditContainer/e2e/features/dragAndDropCms/dummyCmsApp.js\"}",
                "key": "applications.cmssmarteditmock"
            }, {
                "id": "8",
                "value": "{\"smartEditContainerLocation\":\"/jsTarget/cmssmarteditContainer.js\"}",
                "key": "applications.cmssmarteditContainer"
            }, {
                "id": "9",
                "value": "{\"smartEditLocation\":\"/jsTests/cmssmarteditContainer/e2e/features/dragAndDropCms/innerMocksforDragAndDropCms.js\"}",
                "key": "applications.BackendMockModule"
            }];

            $httpBackend.whenGET(/configuration/).respond(
                function(method, url, data, headers) {
                    return [200, map];
                });

            $httpBackend.whenGET(I18N_RESOURCE_URI + "/" + languageService.getBrowserLocale()).respond({
                'drag.and.drop.not.valid.component.type': 'Component {{componentUID}} not allowed in {{slotUID}}.'
            });

            $httpBackend.whenPOST("/thepreviewTicketURI")
                .respond({
                    resourcePath: '/jsTests/cmssmarteditContainer/e2e/features/dummystorefront.html',
                    ticketId: 'dasdfasdfasdfa'
                });

            $httpBackend.whenGET(/cmswebservices\/v1\/catalogs\/apparel-ukContentCatalog\/versions\/Staged\/pages\/homepage\/contentslots\/topHeaderSlot\/typerestrictions/).respond(function(method, url, data, headers) {
                return [200, {
                    contentSlotName: "bottomHeaderSlot",
                    validComponentTypes: [
                        //"componentType0",
                        "componentType1",
                        "componentType2",
                        "componentType3",
                        "CMSParagraphComponent"
                    ]
                }];
            });

            $httpBackend.whenGET(/cmswebservices\/v1\/catalogs\/apparel-ukContentCatalog\/versions\/Staged\/pages\/homepage\/contentslots\/bottomHeaderSlot\/typerestrictions/).respond(function(method, url, data, headers) {
                return [200, {
                    contentSlotName: "bottomHeaderSlot",
                    validComponentTypes: [
                        "componentType4",
                        "CMSParagraphComponent",
                        "SimpleBannerComponent"
                    ]
                }];
            });

            $httpBackend.whenGET(/cmswebservices\/v1\/catalogs\/apparel-ukContentCatalog\/versions\/Staged\/pages\/homepage\/contentslots\/footerSlot\/typerestrictions/).respond(function(method, url, data, headers) {
                return [200, {
                    contentSlotName: "bottomHeaderSlot",
                    validComponentTypes: [
                        "componentType0",
                        "componentType1",
                        "componentType2",
                        "componentType3",
                        "componentType4",
                        "componentType5",
                        "CMSParagraphComponent"
                    ]
                }];
            });

            $httpBackend.whenGET(/cmswebservices\/v1\/catalogs\/apparel-ukContentCatalog\/versions\/Staged\/pages\/homepage\/contentslots\/otherSlot\/typerestrictions/).respond(function(method, url, data, headers) {
                return [200, {
                    contentSlotName: "bottomHeaderSlot",
                    validComponentTypes: [
                        "componentType0",
                        "componentType1",
                        "componentType2",
                        "componentType3",
                        "componentType4",
                        "componentType5"
                    ]
                }];
            });

            $httpBackend.whenGET(/cmswebservices\/v1\/types/).respond(function(method, url, data, headers) {
                return [200, {
                    "componentTypes": [{
                        "code": "CMSParagraphComponent",
                        "i18nKey": "type.cmsparagraphcomponent.name",
                        "name": "Paragraph",
                        "attributes": [{
                            "cmsStructureType": "RichText",
                            "i18nKey": "type.cmsparagraphcomponent.content.name",
                            "qualifier": "content",
                            "localized": true
                        }]
                    }, {
                        "code": "CMSProductListComponent",
                        "i18nKey": "type.cmsproductlistcomponent.name",
                        "name": "Product List Component",
                        "attributes": []
                    }]
                }];
            });

            $httpBackend.whenGET(/cmswebservices\/v1\/items/).respond(function(method, url, data, headers) {
                return [200, {
                    "componentItems": [{
                        "uid": "UpdatePasswordLink",
                        "modifiedtime": "2015-12-07T16:53:47+0000",
                        "visible": true,
                        "name": "UpdatePasswordLink",
                        "slotId": null,
                        "pk": "8796102657084",
                        "creationtime": "2015-12-07T16:53:45+0000",
                        "position": null,
                        "pageId": null,
                        "typeCode": "CMSLinkComponent"
                    }, {
                        "uid": "NextDayDeliveryBanner",
                        "modifiedtime": "2015-12-07T16:53:45+0000",
                        "visible": true,
                        "name": "Next Day Delivery Banner",
                        "slotId": null,
                        "pk": "8796096267324",
                        "creationtime": "2015-12-07T16:53:41+0000",
                        "position": null,
                        "pageId": null,
                        "typeCode": "SimpleBannerComponent"
                    }]
                }];
            });

            $httpBackend.whenPOST(/cmswebservices\/v1\/items/).respond(function(method, url, data, headers) {
                return [200, {
                    "uid": "id"
                }];
            });

            $httpBackend.whenPUT(/cmswebservices\/v1\/catalogs\/apparel-ukContentCatalog\/versions\/Staged\/pagescontentslotscomponents/).respond(function(method, url, data, headers) {
                var requestData = JSON.parse(data);
                if (requestData.slotId === "footerSlot") {
                    // Simulates a forbidden operation.
                    return [403];
                }

                return [200, {}];
            });

            $httpBackend.whenGET(/cmswebservices\/v1\/sites\/.*\/languages/).respond({
                languages: [{
                    nativeName: 'English',
                    isocode: 'en',
                    name: 'English',
                    required: true
                }]
            });

        });
angular.module('smarteditloader').requires.push('e2eBackendMocks');
angular.module('smarteditcontainer').requires.push('e2eBackendMocks');
