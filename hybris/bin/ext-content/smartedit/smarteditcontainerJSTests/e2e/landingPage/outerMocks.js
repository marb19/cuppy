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
    .module('OuterMocks', ['ngMockE2E', 'languageServiceModule', 'resourceLocationsModule'])
    .constant('SMARTEDIT_ROOT', 'web/webroot')
    .constant('SMARTEDIT_RESOURCE_URI_REGEXP', /^(.*)\/smarteditcontainerJSTests/)
    .run(
        function($httpBackend, languageService, I18N_RESOURCE_URI) {

            var map = [{
                "id": "2",
                "value": "\"thepreviewTicketURI\"",
                "key": "previewTicketURI"
            }, {
                "id": "8",
                "value": "\"/cmswebservices/v1/i18n/languages\"",
                "key": "i18nAPIRoot"
            }, {
                "id": "9",
                "value": "{\"smartEditLocation\":\"/smarteditcontainerJSTests/e2e/landingPage/innerMocks.js\"}",
                "key": "applications.InnerMocks"
            }];

            $httpBackend
                .whenGET(document.location.origin + "/cmswebservices/i18n/languages/" + languageService.getBrowserLocale())
                .respond({
                    'landingpage.title': 'Your Touchpoints',
                    'cataloginfo.pagelist': 'PAGE LIST',
                    'cataloginfo.lastsynced': 'LAST SYNCED',
                    'cataloginfo.button.sync': 'SYNC',
                });

            $httpBackend.whenGET(I18N_RESOURCE_URI + "/" + languageService.getBrowserLocale()).respond({
                'sync.confirm.msg': 'this {{catalogName}}is a test'
            });

            $httpBackend.whenGET(/configuration/).respond(
                function(method, url, data, headers) {
                    return [200, map];
                });

            $httpBackend.whenPUT(/configuration/).respond(404);


            $httpBackend.whenPOST(/thepreviewTicketURI/)
                .respond({
                    ticketId: 'dasdfasdfasdfa',
                    resourcePath: document.location.origin + '/smarteditcontainerJSTests/e2e/dummystorefront.html'
                });

            $httpBackend.whenGET(/fragments/).passThrough();


            $httpBackend.whenGET(/cmswebservices\/v1\/languages/).respond({
                languages: [{
                    language: 'en',
                    required: true
                }]
            });

            $httpBackend
                .whenGET("/cmswebservices/v1/i18n/languages/" + languageService.getBrowserLocale())
                .respond({});

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


            $httpBackend.whenGET(/cmswebservices\/v1\/catalogs\/apparel-ukContentCatalog\/synchronization\/versions\/Staged\/Online/).
            respond({
                date: '2016-01-29T16:25:28+0000',
                status: 'RUNNING'
            });

            $httpBackend.whenGET(/cmswebservices\/v1\/catalogs\/apparel-deContentCatalog\/synchronization\/versions\/Staged\/Online/).
            respond({
                date: "2015-01-29T16:25:44+0000",
                status: "ABORTED"
            });

            $httpBackend.whenGET(/cmswebservices\/v1\/catalogs\/electronicsContentCatalog\/synchronization\/versions\/Staged\/Online/).
            respond({
                date: "2014-01-28T17:05:29+0000",
                status: "FINISHED"
            });

            $httpBackend.whenGET(/cmswebservices\/v1\/catalogs\/actionFiguresContentCatalog\/synchronization\/versions\/Staged\/Online/).
            respond({
                date: "2013-01-28T17:05:29+0000",
                status: "FINISHED"
            });

            $httpBackend.whenPUT(/cmswebservices\/v1\/catalogs\/apparel-ukContentCatalog\/synchronization\/versions\/Staged\/Online/).
            respond({
                date: '2016-01-29T16:25:28+0000',
                status: 'RUNNING'
            });

            $httpBackend.whenPUT(/cmswebservices\/v1\/catalogs\/apparel-deContentCatalog\/synchronization\/versions\/Staged\/Online/).
            respond({
                date: "2016-01-29T16:25:44+0000",
                status: "ABORTED"
            });

            $httpBackend.whenPUT(/cmswebservices\/v1\/catalogs\/electronicsContentCatalog\/synchronization\/versions\/Staged\/Online/).
            respond({
                date: "2014-01-28T17:05:29+0000",
                status: "FINISHED"
            });

            $httpBackend.whenPUT(/cmswebservices\/v1\/catalogs\/actionFiguresContentCatalog\/synchronization\/versions\/Staged\/Online/).
            respond({
                date: "2013-01-28T17:05:29+0000",
                status: "FINISHED"
            });

            $httpBackend.whenGET(/cmswebservices\/v1\/sites$/).respond({
                sites: [{
                    previewUrl: '/smarteditcontainerJSTests/e2e/dummystorefront.html',
                    name: {
                        type: "map",
                        value: {
                            en: "Electronics"
                        }
                    },
                    redirectUrl: 'redirecturlElectronics',
                    uid: 'electronics'
                }, {
                    previewUrl: '/smarteditcontainerJSTests/e2e/dummystorefront.html',
                    name: {
                        type: "map",
                        value: {
                            en: "Apparels"
                        }
                    },
                    redirectUrl: 'redirecturlApparels',
                    uid: 'apparel-uk'
                }, {
                    previewUrl: '/smarteditcontainerJSTests/e2e/dummystorefront.html',
                    name: {
                        type: "map",
                        value: {
                            en: "Apparels"
                        }
                    },
                    redirectUrl: 'redirecturlApparels',
                    uid: 'apparel-de'
                }, {
                    previewUrl: '/smarteditcontainerJSTests/e2e/dummystorefront.html',
                    name: {
                        type: "map",
                        value: {
                            en: "Toys"
                        }
                    },
                    redirectUrl: 'redirectSomeOtherSite',
                    uid: 'toys'
                }]
            });

            $httpBackend.whenGET(/cmswebservices\/v1\/sites\/electronics\/catalogversiondetails/).respond({
                name: {
                    type: "map",
                    value: {
                        en: "Electronics"
                    }
                },
                uid: 'electronics',
                catalogVersionDetails: [{
                    name: {
                        type: "map",
                        value: {
                            en: "Electronics Content Catalog"
                        }
                    },
                    catalogId: 'electronicsContentCatalog',
                    version: 'Online',
                    redirectUrl: null
                }, {
                    name: {
                        type: "map",
                        value: {
                            en: "Electronics Content Catalog"
                        }
                    },
                    catalogId: 'electronicsContentCatalog',
                    version: 'Staged',
                    redirectUrl: null
                }]
            });

            $httpBackend.whenGET(/cmswebservices\/v1\/sites\/apparel-uk\/catalogversiondetails/).respond({
                name: {
                    type: "map",
                    value: {
                        en: "Apparels"
                    }
                },
                uid: 'apparel-uk',
                catalogVersionDetails: [{
                    name: {
                        type: "map",
                        value: {
                            en: "Apparel UK Content Catalog"
                        }
                    },
                    catalogId: 'apparel-ukContentCatalog',
                    version: 'Online',
                    redirectUrl: null
                }, {
                    name: {
                        type: "map",
                        value: {
                            en: "Apparel UK Content Catalog"
                        }
                    },
                    catalogId: 'apparel-ukContentCatalog',
                    version: 'Staged',
                    redirectUrl: null
                }]
            });

            $httpBackend.whenGET(/cmswebservices\/v1\/sites\/apparel-de\/catalogversiondetails/).respond({
                name: {
                    type: "map",
                    value: {
                        en: "Apparels"
                    }
                },
                uid: 'apparel-de',
                catalogVersionDetails: [{
                    name: {
                        type: "map",
                        value: {
                            en: "Apparel DE Content Catalog"
                        }
                    },
                    catalogId: 'apparel-deContentCatalog',
                    version: 'Online',
                    redirectUrl: null
                }, {
                    name: {
                        type: "map",
                        value: {
                            en: "Apparel DE Content Catalog"
                        }
                    },
                    catalogId: 'apparel-deContentCatalog',
                    version: 'Staged',
                    redirectUrl: null
                }]
            });

            $httpBackend.whenGET(/cmswebservices\/v1\/sites\/toys\/catalogversiondetails/).respond({
                name: {
                    type: "map",
                    value: {
                        en: "Toys"
                    }
                },
                uid: 'toys',
                catalogVersionDetails: [{
                    name: {
                        type: "map",
                        value: {
                            en: "Action Figures Content Catalog"
                        }
                    },
                    catalogId: 'actionFiguresContentCatalog',
                    version: 'Online',
                    redirectUrl: null
                }, {
                    name: {
                        type: "map",
                        value: {
                            en: "Action Figures Content Catalog"
                        }
                    },
                    catalogId: 'actionFiguresContentCatalog',
                    version: 'Staged',
                    redirectUrl: null
                }, {
                    name: {
                        type: "map",
                        value: {
                            en: "Stuffed Toys Content Catalog"
                        }
                    },
                    catalogId: 'stuffedToysContentCatalog',
                    version: 'Online',
                    redirectUrl: null
                }, {
                    name: {
                        type: "map",
                        value: {
                            en: "Stuffed Toys Content Catalog"
                        }
                    },
                    catalogId: 'stuffedToysContentCatalog',
                    version: 'Staged',
                    redirectUrl: null
                }]
            });

        });
angular.module('smarteditloader').requires.push('OuterMocks');
angular.module('smarteditcontainer').requires.push('OuterMocks');
