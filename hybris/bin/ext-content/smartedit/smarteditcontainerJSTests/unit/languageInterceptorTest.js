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
describe('languageInterceptor', function() {

    var $httpProvider, languageInterceptor, sharedDataService, languageService, $q, $rootScope;

    var CMSWEBSERVICES_RESOURCE_URI = 'CMSWEBSERVICES_RESOURCE_URI';
    var LANGUAGE_RESOURCE_URI = 'LANGUAGE_RESOURCE_URI';
    var SITES_RESOURCE_URI = 'SITES_RESOURCE_URI';
    var CATALOG_VERSION_DETAILS_RESOURCE_URI = 'CATALOG_VERSION_DETAILS_RESOURCE_URI';
    var I18N_RESOURCE_URI = 'I18N_RESOURCE_URI';

    var SOME_CMSWEBSERVICES_RESOURCE = CMSWEBSERVICES_RESOURCE_URI + '/something';

    beforeEach(customMatchers);

    beforeEach(module('pascalprecht.translate'));

    beforeEach(module('sharedDataServiceModule', function($provide) {
        sharedDataService = jasmine.createSpyObj('sharedDataService', ['get']);
        $provide.value('sharedDataService', sharedDataService);
    }));

    beforeEach(module('resourceLocationsModule', function($provide) {
        $provide.constant('CMSWEBSERVICES_RESOURCE_URI', CMSWEBSERVICES_RESOURCE_URI);
        $provide.constant('LANGUAGE_RESOURCE_URI', LANGUAGE_RESOURCE_URI);
        $provide.constant('SITES_RESOURCE_URI', SITES_RESOURCE_URI);
        $provide.constant('CATALOG_VERSION_DETAILS_RESOURCE_URI', CATALOG_VERSION_DETAILS_RESOURCE_URI);
        $provide.constant('I18N_RESOURCE_URI', I18N_RESOURCE_URI);
    }));

    beforeEach(module('languageServiceModule', function($provide, _$httpProvider_) {
        $httpProvider = _$httpProvider_;

        languageService = jasmine.createSpyObj('languageService', ['getLanguagesForSite', 'getBrowserLanguageIsoCode']);
        languageService.getLanguagesForSite.andCallFake(function(siteUID) {
            if (siteUID === 'validSiteUid') {
                return $q.when([{
                    isocode: 'en'
                }, {
                    isocode: 'pl'
                }, {
                    isocode: 'it'
                }]);
            }
            return $q.reject();
        });

        $provide.value('languageService', languageService);
    }));

    beforeEach(module('languageInterceptorModule'));

    beforeEach(inject(function(_$rootScope_, _languageInterceptor_, _$q_) {
        $rootScope = _$rootScope_;
        $q = _$q_;
        languageInterceptor = _languageInterceptor_;
    }));

    it('WHEN the language interceptor module is loaded THEN the language interceptor will be part of the $httpProvider interceptors', function() {
        // THEN
        expect($httpProvider.interceptors).toContain('languageInterceptor');
    });

    it('GIVEN the language interceptor module is loaded WHEN the language interceptor intercepts a request to the cmswebservices language resource THEN the request remains unaltered', function() {
        // WHEN
        var promise = languageInterceptor.request({
            url: LANGUAGE_RESOURCE_URI,
            headers: {}
        });

        // THEN
        expect(promise).toBeResolvedWithData({
            url: LANGUAGE_RESOURCE_URI,
            headers: {}
        });
    });

    it('GIVEN the current experience is undefined WHEN the language interceptor intercepts a request to any other cmswebservices resource THEN the request remains unaltered', function() {
        // GIVEN
        sharedDataService.get.andReturn($q.reject());

        // WHEN
        var promise = languageInterceptor.request({
            url: SOME_CMSWEBSERVICES_RESOURCE,
            headers: {}
        });

        // THEN
        expect(promise).toBeResolvedWithData({
            url: SOME_CMSWEBSERVICES_RESOURCE,
            headers: {
                'Accept-Language': ''
            }
        });
    });

    it('GIVEN the current experience is defined AND the language service returns a rejected promise for the given site WHEN the language interceptor intercepts a request to any other cmswebservices THEN the request remains unaltered', function() {
        // GIVEN
        sharedDataService.get.andReturn($q.when({
            siteDescriptor: {
                uid: 'invalidSiteUid'
            }
        }));


        // WHEN
        var promise = languageInterceptor.request({
            url: SOME_CMSWEBSERVICES_RESOURCE,
            headers: {}
        });

        // THEN
        expect(promise).toBeResolvedWithData({
            url: SOME_CMSWEBSERVICES_RESOURCE,
            headers: {
                'Accept-Language': ''
            }
        });
    });

    it('GIVEN the current experience is defined AND the language service returns a promise resolved with the list of languages WHEN the language interceptor intercepts a request to any other cmswebservices THEN the request is augmented with the hybris-languages header with the comma-separated list of languages', function() {
        // GIVEN
        sharedDataService.get.andReturn($q.when({
            siteDescriptor: {
                uid: 'validSiteUid'
            }
        }));

        // WHEN
        var promise = languageInterceptor.request({
            url: SOME_CMSWEBSERVICES_RESOURCE,
            headers: {}
        });

        // THEN
        expect(promise).toBeResolvedWithData({
            url: SOME_CMSWEBSERVICES_RESOURCE,
            headers: {
                'hybris-languages': 'en,pl,it',
                'Accept-Language': ''
            }
        });
    });

    it('GIVEN the tooling language is defined WHEN the language intercepts a request to the sites resource THEN the request is augmented with the hybris-languages with the tooling language + the default tooling language', function() {
        // GIVEN
        languageService.getBrowserLanguageIsoCode.andReturn('ja');
        sharedDataService.get.andReturn($q.when('zz'));

        // WHEN
        var promise = languageInterceptor.request({
            url: SITES_RESOURCE_URI,
            headers: {}
        });

        // THEN
        expect(promise).toBeResolvedWithData({
            url: SITES_RESOURCE_URI,
            headers: {
                'hybris-languages': 'ja,zz',
                'Accept-Language': ''
            }
        });

        expect(sharedDataService.get).toHaveBeenCalledWith('defaultToolingLanguage');
    });

    it('GIVEN the tooling language and default tooling language are the same, WHEN the language intercepts a request to the sites resource THEN the request is augmented with the hybris-languages with the tooling language only', function() {
        // GIVEN
        languageService.getBrowserLanguageIsoCode.andReturn('ja');
        sharedDataService.get.andReturn($q.when('ja'));

        // WHEN
        var promise = languageInterceptor.request({
            url: SITES_RESOURCE_URI,
            headers: {}
        });

        // THEN
        expect(promise).toBeResolvedWithData({
            url: SITES_RESOURCE_URI,
            headers: {
                'hybris-languages': 'ja',
                'Accept-Language': ''
            }
        });

        expect(sharedDataService.get).toHaveBeenCalledWith('defaultToolingLanguage');
    });

    it('GIVEN the tooling language is defined WHEN the language intercepts a request to the catalog version details resource THEN the request is augmented with the hybris-languages with the tooling language', function() {
        // GIVEN
        languageService.getBrowserLanguageIsoCode.andReturn('ja');
        sharedDataService.get.andReturn($q.when('zz'));

        // WHEN
        var promise = languageInterceptor.request({
            url: CATALOG_VERSION_DETAILS_RESOURCE_URI,
            headers: {}
        });

        // THEN
        expect(promise).toBeResolvedWithData({
            url: CATALOG_VERSION_DETAILS_RESOURCE_URI,
            headers: {
                'hybris-languages': 'ja,zz',
                'Accept-Language': ''
            }
        });

        expect(sharedDataService.get).toHaveBeenCalledWith('defaultToolingLanguage');

    });
});
