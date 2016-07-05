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
 * @ngdoc service
 * @name httpErrorInterceptorModule
 *
 * @description
 * Module that provides a service called {@link httpErrorInterceptorModule.httpErrorInterceptor httpErrorInterceptor}
 * for handling all response errors that are neither 401 errors (Unauthorized access error) nor OCC validation errors.
 */
angular.module('httpErrorInterceptorModule', ['functionsModule', 'translationServiceModule', 'authenticationModule', 'alertServiceModule', 'resourceLocationsModule'])


/**
 * @ngdoc service
 * @name httpErrorInterceptorModule.httpErrorInterceptor
 * 
 * @description
 * Provides a way for global error handling by intercepting the requests before handing them to the server
 * and response before is its given to the application code.
 * 
 * The interceptors are service factories that are registered with the $httpProvider by adding them to the $httpProvider.interceptors array. 
 * The factory is called and injected with dependencies and returns the interceptor object with contains the interceptor methods.
 */
.factory('httpErrorInterceptor', function(LANGUAGE_RESOURCE_URI, $q, $location, $rootScope, $injector, hitch, authenticationService, $log, alertService) {

        var errorRecoverer = {};

        errorRecoverer.removeErrorsByType = function(type, errors) {

            return errors.filter(function(error) {
                return error.type != type;
            });
        };

        errorRecoverer.convertErrorToTimedAlertObject = function(error) {

            var timedAlertObject = {
                successful: false,
                message: error.message,
                closeable: true
            };

            return timedAlertObject;
        };

        /**
         * @ngdoc method
         * @name httpErrorInterceptorModule.httpErrorInterceptor#responseError
         * @methodOf httpErrorInterceptorModule.httpErrorInterceptor
         *
         * @description
         * Interceptor method which intercepts all the failed responses and handles them based on the response code.
         *
         * This method handles all response errors that are neither 401 errors (Unauthorized access error) nor OCC validation errors.
         *
         * @param {Object} response - the response object which contains the status code and other parameters that explain the response.
         *
         * @returns {Promise} Returns a {@link https://docs.angularjs.org/api/ng/service/$q promise} that was already resolved as rejected with the given response.
         */
        errorRecoverer.responseError = function(response) {
            if (response && response.config && response.config.url) {
                $log.debug(["Intercepting response error: ", response.config.url, " status: ", response.status].join(" "));
                var languageResourceRegex = new RegExp(LANGUAGE_RESOURCE_URI.replace(/\:.*\//g, '.*/'));

                if (response.status == 400) {

                    var nonValidationErrors = errorRecoverer.removeErrorsByType("ValidationError", response.data.errors);

                    if (nonValidationErrors.length !== 0) {

                        var timedAlertObjects = [];

                        for (var i in nonValidationErrors) {
                            timedAlertObjects.push(errorRecoverer.convertErrorToTimedAlertObject(nonValidationErrors[i]));
                        }
                        alertService.pushAlerts(timedAlertObjects);
                    }
                } else if (response.status != 401 && !languageResourceRegex.test(response.config.url)) {
                    var errorMessage = response.message === undefined ? "unknown.request.error" : response.message;

                    alertService.pushAlerts([{
                        successful: false,
                        message: errorMessage,
                        closeable: true
                    }]);

                }
            }
            return $q.reject(response);
        };

        return errorRecoverer;
    })
    .config(function($httpProvider) {
        $httpProvider.interceptors.push('httpErrorInterceptor');
    });
