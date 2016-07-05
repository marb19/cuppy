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
describe('languageService - ', function() {

    var SITE_UID = 'apparel-de';

    var languageService;
    var languageRestService, restServiceFactory, $q, $rootScope;

    beforeEach(customMatchers);

    beforeEach(module('restServiceFactoryModule', function($provide) {
        languageRestService = jasmine.createSpyObj('languageRestService', ['get']);
        restServiceFactory = jasmine.createSpyObj('restServiceFactory', ['get']);
        restServiceFactory.get.andReturn(languageRestService);
        $provide.value('restServiceFactory', restServiceFactory);
    }));

    beforeEach(module('resourceLocationsModule', function($provide) {
        $provide.value('LANGUAGE_RESOURCE_URI', '/cmswebservices/sites/:siteUID/languages');
    }));

    beforeEach(module('languageServiceModule'));

    beforeEach(inject(function(_languageService_, _$q_, _$rootScope_) {
        languageService = _languageService_;
        $q = _$q_;
        $rootScope = _$rootScope_;
    }));


    it('GIVEN languages REST call fails WHEN I request all languages for a given site THEN I will receive a rejected promise', function() {
        // GIVEN
        languageRESTCallFails();

        // WHEN
        var promise = languageService.getLanguagesForSite(SITE_UID);

        // THEN
        expect(promise).toBeRejected();
    });

    it('GIVEN languages REST call succeeds WHEN I request all languages for a given site THEN I will receive a promise that resolves to the list of language objects', function() {
        // GIVEN
        languagesRESTCallSucceeds();

        // WHEN
        var promise = languageService.getLanguagesForSite(SITE_UID);

        // THEN
        expect(promise).toBeResolvedWithData([{
            "nativeName": "English",
            "isocode": "en",
            "name": "English",
            "active": true,
            "required": true
        }, {
            "nativeName": "Deutsch",
            "isocode": "de",
            "name": "German",
            "active": true,
            "required": false
        }]);
    });

    it('GIVEN languages REST call succeeds at least one WHEN I request all languages for the same site subsequently THEN I will receive a promise that resolves to a cached list of languages AND the rest service will not be called again', function() {
        // GIVEN
        languagesRESTCallSucceeds();

        // WHEN
        languageService.getLanguagesForSite(SITE_UID);
        $rootScope.$digest();
        var promise = languageService.getLanguagesForSite(SITE_UID);
        $rootScope.$digest();

        // THEN
        expect(promise).toBeResolvedWithData([{
            "nativeName": "English",
            "isocode": "en",
            "name": "English",
            "active": true,
            "required": true
        }, {
            "nativeName": "Deutsch",
            "isocode": "de",
            "name": "German",
            "active": true,
            "required": false
        }]);
        expect(languageRestService.get.calls.length).toEqual(1);
    });

    function languagesRESTCallSucceeds() {
        languageRestService.get.andReturn($q.when({
            languages: [{
                "nativeName": "English",
                "isocode": "en",
                "name": "English",
                "active": true,
                "required": true
            }, {
                "nativeName": "Deutsch",
                "isocode": "de",
                "name": "German",
                "active": true,
                "required": false
            }]
        }));
    }

    function languageRESTCallFails() {
        languageRestService.get.andReturn($q.reject());
    }

});
