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
angular.module('backendMocks', ['ngMockE2E', 'functionsModule', , 'cmsWebservicesConstantsModule', 'resourceLocationsModule'])
    .run(function($httpBackend, filterFilter, parseQuery, cmsWebservicesConstants, I18N_RESOURCE_URI, LANGUAGE_RESOURCE_URI) {

        var map = [{
            "id": "432534534645",
            "value": "\"somepath\"",
            "key": "i18nAPIRoot"
        }];


        $httpBackend.whenGET(/configuration/).respond(function(method, url, data, headers) {
            return [200, map];
        });


        $httpBackend.whenGET(new RegExp(I18N_RESOURCE_URI)).respond({
            "tab1.name": "Tab 1",
            "tab2.name": "Tab 2",
            "tab3.name": "Tab 3",
            "editortabset.basictab.title": "BASIC INFO",
            "editortabset.admintab.title": "ADMIN",
            "editortabset.visibilitytab.title": "VISIBILITY",
            "editortabset.generictab.title": "GENERIC",
            "type.Item.name.name": 'Name',
            "type.AbstractItem.creationtime.name": "Created",
            "type.AbstractItem.modifiedtime.name": "Modified",
            "type.AbstractCMSComponent.visible.name": "Component Visibility",
            "type.Item.uid.name": "ID",
            "type.AbstractItem.pk.name": "Key",
            "genericeditor.dropdown.placeholder": "Select an image",
            "componentform.actions.cancel": "Cancel",
            "componentform.actions.submit": "Submit",
            "componentform.actions.replaceImage": "Replace Image",
            "type.thesmarteditcomponenttype.id.name": "id",
            "type.thesmarteditcomponenttype.headline.name": "Headline",
            "type.thesmarteditcomponenttype.active.name": "Activation",
            "type.thesmarteditcomponenttype.activationDate.name": "Activation date",
            "type.thesmarteditcomponenttype.enabled.name": "Enabled",
            "type.thesmarteditcomponenttype.content.name": "Content",
            "type.thesmarteditcomponenttype.created.name": "Creation date",
            "type.thesmarteditcomponenttype.media.name": "Media",
            "type.thesmarteditcomponenttype.external.name": "External Link",
            "type.thesmarteditcomponenttype.urlLink.name": "Url Link",
            "editor.linkTo.label": "Link to",
            "editor.linkTo.external.label": "External Link",
            "editor.linkTo.internal.label": "Existing Page"
        });


        $httpBackend.whenGET(new RegExp(cmsWebservicesConstants.TYPES)).respond(function(method, url, data, headers) {
            var structure = {
                attributes: [{
                        cmsStructureType: "ShortString",
                        qualifier: "id",
                        i18nKey: 'type.thesmarteditcomponenttype.id.name'
                    }, {
                        cmsStructureType: "LongString",
                        qualifier: "headline",
                        i18nKey: 'type.thesmarteditcomponenttype.headline.name'
                    }, {
                        cmsStructureType: "Boolean",
                        qualifier: "active",
                        i18nKey: 'type.thesmarteditcomponenttype.active.name'
                    }, {
                        cmsStructureType: "Date",
                        qualifier: "activationDate",
                        i18nKey: 'type.thesmarteditcomponenttype.activationDate.name'
                    }, {
                        cmsStructureType: "RichText",
                        qualifier: "content",
                        i18nKey: 'type.thesmarteditcomponenttype.content.name'
                    },
                    /*{
                                        cmsStructureType: "Media",
                                        qualifier: "media",
                                        i18nKey: 'type.thesmarteditcomponenttype.media.name'
                                    },*/
                    {
                        cmsStructureType: "Boolean",
                        qualifier: "external",
                        i18nKey: 'thesmarteditComponentType_external'
                    }, {
                        cmsStructureType: "ShortString",
                        qualifier: "urlLink",
                        i18nKey: 'thesmarteditComponentType_urlLink'
                    }
                ]
            };

            return [200, structure];
        });

        var component = {

            id: 'Component ID',
            pk: '123455667',
            uid: 'Comp_0006456345634',
            name: 'Custom Paragraph Component',
            headline: 'The Headline',
            active: true,
            content: 'the content to edit',
            activationDate: new Date().getTime(),
            creationtime: new Date().getTime(),
            modifiedtime: new Date().getTime(),
            media: '4',
            external: false,
            visible: true
        };

        $httpBackend.whenGET(new RegExp(cmsWebservicesConstants.ITEMS)).respond(component);
        $httpBackend.whenPUT(new RegExp(cmsWebservicesConstants.ITEMS)).respond(function(method, url, data, headers) {
            component = JSON.parse(data);
            return [200, component];
        });

        var medias = [{
            id: '4',
            code: 'clone4',
            description: 'Clone background',
            altText: 'clone alttext',
            realFileName: 'clone_bckg.png',
            url: 'web/webroot/images/clone_bckg.png'
        }];

        $httpBackend.whenGET(/cmswebservices\/cmsxdata\/contentcatalog\/staged\/Media\/(.+)/).respond(function(method, url, data, headers) {
            var identifier = /Media\/(.+)/.exec(url)[1];
            console.info("get media", identifier);
            var filtered = medias.filter(function(media) {
                return media.id == identifier;
            });
            if (filtered.length == 1) {
                return [200, filtered[0]];
            } else {
                return [404];
            }
        });

        $httpBackend.whenGET(/cmswebservices\/cmsxdata\/contentcatalog\/staged\/Media/).respond(function(method, url, data, headers) {
            console.info("query medias");
            var filtered = filterFilter(medias, parseQuery(url).search);
            return [200, {
                medias: filtered
            }];
        });

        $httpBackend.whenGET(new RegExp(LANGUAGE_RESOURCE_URI.replace(/:.*\//g, '.*/'))).respond({
            languages: [{
                nativeName: 'English',
                isocode: 'en',
                name: 'English',
                required: true
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

    });
angular.module('tabsetApp').requires.push('backendMocks');
