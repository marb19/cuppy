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
describe('unit test interceptor service', function() {

    var $rootScope, $q, $httpProvider, $injector, i18nInterceptor, languageService;

    beforeEach(customMatchers);
    beforeEach(module('i18nInterceptorModule', function($provide, _$httpProvider_) {

        loadConfigManagerService = jasmine.createSpyObj('loadConfigManagerService', ['loadAsObject']);
        $injector = jasmine.createSpyObj('$injector', ['get']);
        $injector.get.andReturn(loadConfigManagerService);
        $provide.value("$injector", $injector);
        $provide.value("loadConfigManagerService", loadConfigManagerService);
        $httpProvider = _$httpProvider_;

        $provide.constant("I18N_RESOURCE_URI", 'realI18nAPI');

        languageService = jasmine.createSpyObj('languageService', ['getBrowserLocale', 'setInitialized']);
        $provide.constant("languageService", languageService);

    }));

    beforeEach(inject(function(_$q_, _$rootScope_, _$httpBackend_, _i18nInterceptor_) {
        $q = _$q_;
        $rootScope = _$rootScope_;
        i18nInterceptor = _i18nInterceptor_;
    }));


    it('$httpProvider will be loaded with only one interceptor and that will be the i18nInterceptor', function() {

        expect($httpProvider.interceptors).toContain('i18nInterceptor');

    });
    it('will not rewrite url to i18nApiRoot URI from neither configuration nor liveedit namespace when i18n API call not detected', function() {
        var config = {
            url: 'somecall/en_CA',
            headers: {}
        };

        i18nInterceptor.request(config).then(function(response) {
            expect(response).toBe(config);
            expect(config.url).toBe('somecall/en_CA');
        }, function() {
            expect().fail();
        });

        $rootScope.$digest();

        expect(loadConfigManagerService.loadAsObject).not.toHaveBeenCalled();
    });

    it('will rewrite url to i18nApiRoot URI from constants when i18n API call detected', function() {

        var config = {
            url: 'i18nAPIRoot/en_CA',
            headers: {}
        };

        i18nInterceptor.request(config).then(function(response) {
            expect(response).toBe(config);
            expect(config.url).toBe('realI18nAPI/en_CA');
        }, function() {
            expect().fail();
        });

        $rootScope.$digest();

    });

    it('GIVEN request url indicates undefined locale THEN it swaps for the browser locale', function() {

        // WHEN
        languageService.getBrowserLocale.andReturn('xx_YY');
        var promise = requestTranslationForUndefinedLocale();

        //THEN
        promise.then(function(response) {
            expect(response.url).toBe('realI18nAPI/xx_YY');
        }, function() {
            expect().fail();
        });

        $rootScope.$digest();

    });

    it('GIVEN the i18nInterceptor response, when the response is a Map, i18nInterceptor response will return a map and initialise languageService', function() {

        var config = {
            url: 'realI18nAPI/en_CA',
            headers: {}
        };

        var response = i18nInterceptor.response({
            config: config,
            data: {
                key1: 'value1',
                key2: 'value2'
            }
        });
        expect(response.data.key1).toBe('value1');
        expect(languageService.setInitialized).toHaveBeenCalledWith(true);
    });

    it('GIVEN the i18nInterceptor response, when the response is an object that holds a Map, i18nInterceptor response will return a map and initialise languageService', function() {

        var config = {
            url: 'realI18nAPI/en_CA',
            headers: {}
        };

        var response = i18nInterceptor.response({
            config: config,
            data: {
                value: {
                    key1: 'value1',
                    key2: 'value2'
                }
            }
        });
        expect(response.data.key2).toBe('value2');
        expect(languageService.setInitialized).toHaveBeenCalledWith(true);
    });

    //ACTIONS

    function requestTranslationForUndefinedLocale() {
        return i18nInterceptor.request({
            url: 'i18nAPIRoot/UNDEFINED'
        });
    }

});
