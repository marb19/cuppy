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
angular.module('httpSecurityHeadersInterceptorModule', ['functionsModule'])

/**
 * @ngdoc service
 * @name httpSecurityHeadersInterceptorModule.httpSecurityHeadersInterceptor
 *
 * @description
 * A HTTP request interceptor that adds the security headers to the HTTP header object before a request is made.
 *
 * Interceptors are service factories that are registered with the $httpProvider by adding them to the
 * $httpProvider.interceptors array. The factory is called and injected with dependencies and returns the interceptor
 * object, which contains the interceptor methods.
 */
.factory('httpSecurityHeadersInterceptor', function($q, parseQuery, hitch) {

        /**
         * @ngdoc method
         * @name httpSecurityHeadersInterceptorModule.httpSecurityHeadersInterceptor#request
         * @methodOf httpSecurityHeadersInterceptorModule.httpSecurityHeadersInterceptor
         *
         * @description
         * Interceptor method that is called with a HTTP configuration object. It adds required security headers 
         * (X-FRAME-Options, X-XSS-Protection, X-Content-Type-Options and Strict-Transport-Security) 
         * to the HTTP header object before a request is made to the resource.
         *
         * @param {Object} config The HTTP configuration object that holds the configuration information.
         *
         * @returns {Object} Returns the modified configuration object.
         */
        var request = function request(config) {
            config.headers = config.headers || [];
            config.headers["X-FRAME-Options"] = "DENY";
            config.headers["X-XSS-Protection"] = "1; mode=block";
            config.headers["X-Content-Type-Options"] = "nosniff";
            config.headers["Strict-Transport-Security"] = "max-age=16070400; includeSubDomains";

            return config;
        };
        return {
            request: request
        };
    })
    .config(function($httpProvider) {
        $httpProvider.interceptors.push('httpSecurityHeadersInterceptor');
    });
