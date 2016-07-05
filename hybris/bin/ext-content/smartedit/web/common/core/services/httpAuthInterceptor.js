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
angular.module('httpAuthInterceptorModule', ['functionsModule', 'translationServiceModule', 'authenticationModule', 'storageServiceModule'])


/**
 * @ngdoc service
 * @name httpAuthInterceptorModule.httpAuthInterceptor
 * 
 * @description
 * Provides a way for global authentication by intercepting the requests before handing them to the server
 * and response before is its given to the application code.
 * 
 * The interceptors are service factories that are registered with the $httpProvider by adding them to the $httpProvider.interceptors array. 
 * The factory is called and injected with dependencies and returns the interceptor object with contains the interceptor methods.
 */
.factory('httpAuthInterceptor', function($q, $location, $rootScope, $injector, hitch, storageService, authenticationService, $log) {

        /* key : auth entry point, value : array of deferred*/
        var promisesToResolve = {};

        var errorRecoverer = {

            /** 
             * @ngdoc method
             * @name httpAuthInterceptorModule.httpAuthInterceptor#request
             * @methodOf httpAuthInterceptorModule.httpAuthInterceptor
             * 
             * @description
             * Interceptor method which gets called with a http config object, intercepts any request made with $http service.
             * A call to any REST resource will be intercepted by this method, which then adds a authentication token to the request
             * and the forwards it to the REST resource.
             * 
             * @param {Object} config - the http config object that holds the configuration information.
             */
            request: function(config) {
                if (/.+\.html$/.test(config.url)) {
                    return config;
                } else {
                    var deferred = $q.defer();
                    return authenticationService.filterEntryPoints(config.url).then(function(entryPoints) {
                        if (entryPoints && entryPoints.length > 0) {
                            var authURI = entryPoints[0];
                            return storageService.getAuthToken(authURI).then(function(authToken) {
                                if (authToken) {
                                    $log.debug(["Intercepting request adding access token: ", config.url].join(" "));
                                    config.headers.Authorization = authToken.token_type + " " + authToken.access_token;
                                } else {
                                    $log.debug(["Intercepting request no access token found: ", config.url].join(" "));
                                }
                                return config;
                            });
                        }
                        return config;
                    });
                }
            },

            /** 
             * @ngdoc method
             * @name httpAuthInterceptorModule.httpAuthInterceptor#responseError
             * @methodOf httpAuthInterceptorModule.httpAuthInterceptor
             * 
             * @description
             * Interceptor method which intercepts all the failed responses and handles them based on the response code.
             * 
             * This method handles the following response:
             * <ul>
             * 	<li>401 response errors: handles authentication failures.</li>
             * 	<li>Non 400/401 response errors: handles any other errors by displaying an error message.</li>
             * </ul>
             * 
             * @param {Object} response - the response object which contains the status code and other parameters that explain the response.
             */
            responseError: function(response) {
                if (response && response.config && response.config.url) {
                    $log.debug(["Intercepting response error: ", response.config.url, " status: ", response.status].join(" "));
                    var deferred = $q.defer();

                    authenticationService.isAuthEntryPoint(response.config.url).then(function(isAuthEntryPoint) {
                        if (response.status == 401 && !isAuthEntryPoint) {
                            authenticationService.filterEntryPoints(response.config.url).then(function(entryPoints) {
                                var entryPoint = entryPoints[0];
                                $log.debug(["Intercepting and trying Authentication: ", response.config.url, entryPoint].join(" "));
                                promisesToResolve[entryPoint] = promisesToResolve[entryPoint] || [];

                                promisesToResolve[entryPoint].push(deferred);
                                if (!authenticationService.isReAuthInProgress(entryPoint)) {
                                    authenticationService.setReAuthInProgress(entryPoint);
                                    authenticationService.authenticate(response.config.url).then(hitch(entryPoint, function() {
                                        angular.forEach(promisesToResolve[this], function(def) {
                                            def.resolve();
                                        });
                                        promisesToResolve[this] = [];
                                    }), hitch(entryPoint, function(success) {
                                        angular.forEach(promisesToResolve[this], function(def) {
                                            def.reject();
                                        });
                                        promisesToResolve[this] = [];
                                    }));
                                }

                            });
                        } else {
                            deferred.reject(response);
                        }
                    });

                    return deferred.promise.then(function() {
                        return $injector.get('$http')(response.config);
                    });
                }
            }
        };
        return errorRecoverer;
    })
    .config(function($httpProvider) {
        $httpProvider.interceptors.push('httpAuthInterceptor');
    });
