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
angular.module('previewTicketInterceptorModule', ['functionsModule'])

/**
 * @ngdoc service
 * @name previewTicketInterceptorModule.previewTicketInterceptor
 *
 * @description
 * A HTTP request interceptor that adds the preview ticket to the HTTP header object before a request is made.
 *
 * Interceptors are service factories that are registered with the $httpProvider by adding them to the
 * $httpProvider.interceptors array. The factory is called and injected with dependencies and returns the interceptor
 * object, which contains the interceptor methods.
 */
.factory('previewTicketInterceptor', function($q, parseQuery, hitch) {
        var getLocation = function() {
            var location;

            if (window.frameElement) {
                location = document.location.href;
            } else {
                location = $("iframe").attr("src");
            }
            return location;
        };

        /**
         * @ngdoc method
         * @name previewTicketInterceptorModule.previewTicketInterceptor#request
         * @methodOf previewTicketInterceptorModule.previewTicketInterceptor
         *
         * @description
         * Interceptor method that is called with a HTTP configuration object. It extracts the preview ticket ID (if
         * available) from the URL of the current page and then adds it to the HTTP header object as an
         * "X-Preview-Ticket" property before a request is made to the resource.
         *
         * @param {Object} config The HTTP configuration object that holds the configuration information.
         *
         * @returns {Object} Returns the modified configuration object.
         */
        var request = function request(config) {
            var location = this._getLocation();
            if (location) {
                var previewTicket = parseQuery(location).cmsTicketId;
                if (previewTicket) {
                    config.headers = config.headers || [];
                    config.headers["X-Preview-Ticket"] = previewTicket;
                }
            }

            return config;
        };

        var interceptor = {};
        interceptor.request = hitch(interceptor, request);
        interceptor._getLocation = hitch(interceptor, getLocation);
        return interceptor;
    })
    .config(function($httpProvider) {
        $httpProvider.interceptors.push('previewTicketInterceptor');
    });
