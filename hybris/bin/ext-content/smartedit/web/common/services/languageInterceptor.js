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
angular.module('languageInterceptorModule', ['functionsModule', 'sharedDataServiceModule', 'resourceLocationsModule'])

/**
 * @ngdoc service
 * @name languageInterceptorModule.languageInterceptor
 *
 * @description
 * An HTTP request interceptor that intercepts all cmswebservices requests that are not directed to the cmswebservices language API and
 * add a hybris-languages header to them. The hybris-language header contains a list of languages that have been
 * returned from the REST Language API.
 *
 * Note: The interceptors are service factories that are registered with the $httpProvider by adding them to the
 * $httpProvider.interceptors array. The factory is called and injected with dependencies and returns the interceptor
 * object, which contains the interceptor methods.
 */
.factory('languageInterceptor', function(sharedDataService, CMSWEBSERVICES_RESOURCE_URI, LANGUAGE_RESOURCE_URI, SITES_RESOURCE_URI, CATALOG_VERSION_DETAILS_RESOURCE_URI, I18N_RESOURCE_URI, $translate, $injector, $q) {

    var _endsWithRegexCache = {};
    var _regexCache = {};

    function _uriEndsWithResource(uri, resourceUri) {
        if (!_endsWithRegexCache[resourceUri]) {
            _endsWithRegexCache[resourceUri] = new RegExp(resourceUri.replace(/\:.*\//g, '.*/') + '$');
        }
        return _endsWithRegexCache[resourceUri].test(uri);
    }

    function _uriMatchesResource(uri, resourceUri) {
        if (!_regexCache[resourceUri]) {
            _regexCache[resourceUri] = new RegExp(resourceUri.replace(/\:.*\//g, '.*/'));
        }
        return _regexCache[resourceUri].test(uri);
    }

    function _isCmswebservicesResource(config) {
        return config &&
            config.url &&
            _uriMatchesResource(config.url, CMSWEBSERVICES_RESOURCE_URI) &&
            !_uriMatchesResource(config.url, I18N_RESOURCE_URI) &&
            !_uriMatchesResource(config.url, LANGUAGE_RESOURCE_URI);
    }

    function _isToolingResource(config) {
        return config &&
            config.url &&
            (_uriEndsWithResource(config.url, SITES_RESOURCE_URI) ||
                _uriMatchesResource(config.url, CATALOG_VERSION_DETAILS_RESOURCE_URI));
    }


    function handleRequestToToolingResource(config) {
        return sharedDataService.get('defaultToolingLanguage').then(function(defaultToolingLanguage) {
            var languageService = $injector.has('languageService') && $injector.get('languageService');
            var browserLanguage = languageService.getBrowserLanguageIsoCode();
            config.headers['hybris-languages'] = defaultToolingLanguage && browserLanguage !== defaultToolingLanguage ? browserLanguage + "," + defaultToolingLanguage : browserLanguage;
            config.headers['Accept-Language'] = "";
            return config;
        });
    }

    function handleRequestToCmsWebServicesResource(config) {
        var deferred = $q.defer();
        config.headers['Accept-Language'] = "";
        sharedDataService.get('experience').then(function(experience) {
            var languageService = $injector.has('languageService') && $injector.get('languageService');
            if (experience && languageService) {
                languageService.getLanguagesForSite(experience.siteDescriptor.uid).then(function(languages) {
                    config.headers['hybris-languages'] = languages.map(function(languageDescriptor) {
                        return languageDescriptor.isocode;
                    }).join(',');

                    deferred.resolve(config);
                }, function() {
                    deferred.resolve(config);
                });
            } else {
                deferred.resolve(config);
            }
        }, function() {
            deferred.resolve(config);
        });
        return deferred.promise;
    }

    /**
     * @ngdoc method
     * @name languageInterceptorModule.languageInterceptor#request
     * @methodOf languageInterceptorModule.languageInterceptor
     *
     * @description
     * The interceptor method that is called with a HTTP configuration object. The method intercepts all cmswebservices
     * requests that are not directed to the languages API and adds a hybris-languages header to them.
     * The hybris-language header contains a list of languages that have been extracted from the REST Language API.
     *
     * @param {Object} config The HTTP configuration object that contains that configuration information.
     *
     * @returns {Promise} Returns a {@link https://docs.angularjs.org/api/ng/service/$q promise} of the passed
     * configuration object.
     */
    var request = function request(config) {
        if (_isToolingResource(config)) {
            return handleRequestToToolingResource(config);
        } else if (_isCmswebservicesResource(config)) {
            return handleRequestToCmsWebServicesResource(config);
        }

        return $q.when(config);
    };

    return {
        request: request
    };
}).config(function($httpProvider) {
    $httpProvider.interceptors.push('languageInterceptor');
});
