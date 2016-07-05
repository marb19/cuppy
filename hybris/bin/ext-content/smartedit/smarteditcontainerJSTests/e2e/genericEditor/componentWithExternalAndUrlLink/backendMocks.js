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
    .constant('URL_FOR_ITEM', /cmswebservices\/v1\/catalogs\/electronics\/versions\/staged\/items\/thesmarteditComponentId/)
    .run(function($httpBackend, filterFilter, parseQuery, URL_FOR_ITEM, I18N_RESOURCE_URI, languageService) {

        $httpBackend.whenGET(I18N_RESOURCE_URI + "/" + languageService.getBrowserLocale()).respond({
            "genericeditor.dropdown.placeholder": "Select an image",
            "componentform.actions.cancel": "Cancel",
            "componentform.actions.submit": "Submit",
            "componentform.actions.replaceImage": "Replace Image",
            "type.thesmarteditcomponenttype.id.name": "id",
            "type.thesmarteditcomponenttype.headline.name": "Headline",
            "type.thesmarteditcomponenttype.active.name": "Activation",
            "type.thesmarteditcomponenttype.enabled.name": "Enabled",
            "type.thesmarteditcomponenttype.content.name": "Content",
            "type.thesmarteditcomponenttype.created.name": "Creation date",
            "type.thesmarteditcomponenttype.media.name": "Media",
            "type.thesmarteditcomponenttype.orientation.name": "Orientation",
            "type.thesmarteditcomponenttype.external.name": "External Link",
            "type.thesmarteditcomponenttype.urlLink.name": "Url Link",
            "editor.linkto.label": "Link to",
            "editor.linkto.external.label": "External Link",
            "editor.linkto.internal.label": "Existing Page"
        });


        $httpBackend.whenGET(/cmswebservices\/v1\/types\/thesmarteditComponentType/).respond(function(method, url, data, headers) {
            var structure = {
                attributes: [{
                    cmsStructureType: "ShortString",
                    qualifier: "id",
                    i18nKey: 'type.thesmarteditcomponenttype.id.name',
                    localized: false,
                    required: true
                }, {
                    cmsStructureType: "LongString",
                    qualifier: "headline",
                    i18nKey: 'type.thesmarteditcomponenttype.headline.name',
                    localized: false
                }, {
                    cmsStructureType: "Boolean",
                    qualifier: "active",
                    i18nKey: 'type.thesmarteditcomponenttype.active.name',
                    localized: false
                }, {
                    cmsStructureType: "Boolean",
                    qualifier: "enabled",
                    i18nKey: 'type.thesmarteditcomponenttype.enabled.name',
                    localized: false
                }, {
                    type: "Date",
                    qualifier: "created",
                    i18nKey: 'type.thesmarteditcomponenttype.created.name',
                    localized: false
                }, {
                    cmsStructureType: "RichText",
                    qualifier: "content",
                    i18nKey: 'type.thesmarteditcomponenttype.content.name',
                    localized: true,
                    required: true
                }, {
                    cmsStructureType: "Media",
                    qualifier: "media",
                    i18nKey: 'type.thesmarteditcomponenttype.media.name',
                    localized: true,
                    required: true
                }, {
                    cmsStructureType: "Enum",
                    cmsStructureEnumType: 'de.mypackage.Orientation',
                    qualifier: "orientation",
                    i18nKey: 'type.thesmarteditcomponenttype.orientation.name',
                    localized: false,
                    required: true
                }, {
                    cmsStructureType: "Boolean",
                    qualifier: "external",
                    i18nKey: 'type.thesmarteditcomponenttype.external.name',
                    localized: false
                }, {
                    cmsStructureType: "ShortString",
                    qualifier: "urlLink",
                    i18nKey: 'type.thesmarteditcomponenttype.urlLink.name',
                    localized: false
                }]
            };

            return [200, structure];
        });


        var component = {

            id: 'Component ID',
            headline: 'The Headline',
            active: true,
            content: {
                'type': 'map',
                'value': {
                    'en': 'the content to edit',
                    'fr': 'le contenu a editer',
                    'pl': 'tresc edytowac',
                    'it': 'il contenuto da modificare',
                    'hi': 'Sampaadit karanee kee liee saamagree'
                }
            },
            created: new Date().getTime(),
            enabled: false,
            media: {
                'type': 'map',
                'value': {
                    'en': 'clone4',
                    'hi': 'dnd5'
                }
            },
            orientation: 'vertical',
            external: false,
            urlLink: "myPageUrl"

        };

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

        $httpBackend.whenGET(URL_FOR_ITEM).respond(component);
        $httpBackend.whenPUT(URL_FOR_ITEM).respond(function(method, url, data, headers) {
            component = JSON.parse(data);
            return [200, component];
        });

        var medias = [{
            id: '1',
            code: 'pencil1',
            description: 'My blue pencil',
            altText: 'pencil alttext',
            realFileName: 'edit_bckg.png',
            url: 'web/webroot/icons/edit_bckg.png'
        }, {
            id: '2',
            code: 'more2',
            description: 'More background',
            altText: 'more alttext',
            realFileName: 'more_bckg.png',
            url: 'web/webroot/icons/more_bckg.png'
        }, {
            id: '3',
            code: 'trash3',
            description: 'Trash background',
            altText: 'trash alttext',
            realFileName: 'trash_bckg.png',
            url: 'web/webroot/icons/trash_bckg.png'
        }, {
            id: '4',
            code: 'clone4',
            description: 'Clone background',
            altText: 'clone alttext',
            realFileName: 'clone_bckg.png',
            url: 'web/webroot/icons/clone_bckg.png'
        }, {
            id: '5',
            code: 'dnd5',
            description: 'Drag and drop background',
            altText: 'dnd alttext',
            realFileName: 'dnd_bckg.png',
            url: 'web/webroot/icons/dnd_bckg.png'
        }, {
            id: '6',
            code: 'roll6',
            description: 'Rollback background',
            altText: 'rollback alttext',
            realFileName: 'rollback_bckg.png',
            url: 'web/webroot/icons/rollback_bckg.png'
        }, {
            id: '7',
            code: 'roll7',
            description: 'Rollback background',
            altText: 'rollback alttext',
            realFileName: 'rollback_bckg.png',
            url: 'web/webroot/icons/rollback_bckg.png'
        }, {
            id: '8',
            code: 'roll8',
            description: 'Rollback background',
            altText: 'rollback alttext',
            realFileName: 'rollback_bckg.png',
            url: 'web/webroot/icons/rollback_bckg.png'
        }, {
            id: '9',
            code: 'roll9',
            description: 'Rollback background',
            altText: 'rollback alttext',
            realFileName: 'rollback_bckg.png',
            url: 'web/webroot/icons/rollback_bckg.png'
        }];

        $httpBackend.whenGET(/cmswebservices\/v1\/catalogs\/electronics\/versions\/staged\/media\/(.+)/).respond(function(method, url, data, headers) {
            var identifier = /media\/(.+)/.exec(url)[1];
            var filtered = medias.filter(function(media) {
                return media.code == identifier;
            });
            if (filtered.length == 1) {
                return [200, filtered[0]];
            } else {
                return [404];
            }
        });

        $httpBackend.whenGET(/cmswebservices\/v1\/media/).respond(function(method, url, data, headers) {

            var params = parseQuery(url).params;
            var search = params.split(",")[0].split(":").pop();
            var filtered = filterFilter(medias, search);
            return [200, {
                media: filtered
            }];
        });


        var orientationEnums = {
            enums: [{
                code: 'vertical',
                label: 'Vertical'
            }, {
                code: 'horizontal',
                label: 'Horizontal'
            }, ]
        };


        $httpBackend.whenGET(/cmswebservices\/v1\/enums/).respond(function(method, url, data, headers) {

            var enumClass = parseQuery(url).enumClass;
            console.info("query enums", enumClass);
            if (enumClass === 'de.mypackage.Orientation') {
                return [200, orientationEnums];
            } else {
                return [404];
            }
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
