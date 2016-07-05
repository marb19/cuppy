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
/**
 * @ngdoc overview
 * @name languageServiceModule
 * @description
 * # The languageServiceModule
 *
 * The Language Service module provides a service that fetches all languages that are supported for specified site.
 */
angular.module('languageServiceModule', ['restServiceFactoryModule', 'resourceLocationsModule'])

/**
 * @ngdoc service
 * @name langaugeServiceModule.service:languageService
 *
 * @description
 * The Language Service fetches all languages for a specified site using REST service calls to the cmswebservices languages API.
 */
.factory('languageService', function(restServiceFactory, LANGUAGE_RESOURCE_URI, $q) {
    var cache = {};
    var languageRestService = restServiceFactory.get(LANGUAGE_RESOURCE_URI);
    var initDeferred = $q.defer();

    return {
        /**
         * @ngdoc method
         * @name langaugeServiceModule.service:languageService#getBrowserLanguageIsoCode
         * @methodOf langaugeServiceModule.service:languageService
         *
         * @description
         * Uses the browser's current locale to determine the selected language ISO code.
         *
         * @returns {String} The language ISO code of the browser's currently selected locale.
         */
        getBrowserLanguageIsoCode: function() {
            return (navigator.language || navigator.userLanguage).split("-")[0];
        },

        setInitialized: function(_initialized) {
            if (_initialized === true) {
                initDeferred.resolve();
            } else {
                initDeferred.reject();
            }
        },
        isInitialized: function() {
            return initDeferred.promise;
        },

        /**
         * @ngdoc method
         * @name langaugeServiceModule.service:languageService#getBrowserLocale
         * @methodOf langaugeServiceModule.service:languageService
         *
         * @description
         * determines the browser locale in the format en_US
         *
         * @returns {string} getBrowserLocale
         */
        getBrowserLocale: function() {
            //locale = $locale.id.split("-");
            locale = (navigator.language || navigator.userLanguage).split("-");
            if (locale.length === 1) {
                return locale[0];
            } else {
                return locale[0] + "_" + locale[1].toUpperCase();
            }
        },

        /**
         * @ngdoc method
         * @name langaugeServiceModule.service:languageService#getLanguagesForSite
         * @methodOf langaugeServiceModule.service:languageService
         *
         * @description
         * Fetches a list of language descriptors for the specified storefront site UID. The list of sites is fetched
         * using REST calls to the cmswebservices languages API.
         *
         * @returns {Array} An array of language descriptors. Each descriptor provides the following language
         * properties: isocode, nativeName, name, active, and required.
         */
        getLanguagesForSite: function(siteUID) {
            return cache[siteUID] ? $q.when(cache[siteUID]) : languageRestService.get({
                siteUID: siteUID
            }).then(function(languagesListDTO) {
                cache[siteUID] = languagesListDTO.languages;
                return cache[siteUID];
            });
        }
    };
});
