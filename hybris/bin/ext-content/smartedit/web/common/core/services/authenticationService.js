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
 * @name authenticationModule
 *
 * @description
 * # The authenticationModule
 *
 * The authentication module provides a service to authenticate and logout from SmartEdit.
 * It also allows the management of entry points used to authenticate the different resources in the application.
 *
 */
angular.module('authenticationModule', ['functionsModule', 'resourceLocationsModule', 'storageServiceModule', 'sharedDataServiceModule', 'alertServiceModule', 'modalServiceModule', 'ui.bootstrap'])
    /* 1) ngResource and ngRoute pulled transitively
     * 2) translationServiceModule is needed since the templates/modal/loginDialog.html template uses translate filter
     * Not declaring it will make UNIT fail.
     * 3) because of translationServiceModule pulling $http, one cannot wire here $modal, restServiceFactory or profileService
     * otherwise one ends up with cyclic reference. On then needs resort to dynamic 'runtime' injection via $injector.get
     */

/**
 * @ngdoc service
 * @name authenticationModule.object:DEFAULT_AUTH_MAP
 *
 * @description
 * The default authentication map contains the entry points to use before an authentication map
 * can be loaded from the configuration.
 */
.factory('DEFAULT_AUTH_MAP', function(I18N_RESOURCE_URI, DEFAULT_AUTHENTICATION_ENTRY_POINT) {
    var DEFAULT_ENTRY_POINT_MATCHER = "^(?!" + I18N_RESOURCE_URI + '\/.*$).*';
    var DEFAULT_AUTH_MAP = {};
    DEFAULT_AUTH_MAP[DEFAULT_ENTRY_POINT_MATCHER] = DEFAULT_AUTHENTICATION_ENTRY_POINT;

    return DEFAULT_AUTH_MAP;
})

/**
 * @ngdoc service
 * @name authenticationModule.service:authenticationService
 *
 * @description
 * The authenticationService is used to authenticate and logout from SmartEdit.
 * It also allows the management of entry points used to authenticate the different resources in the application.
 *
 */
.factory('authenticationService', function($rootScope, $q, $injector, $log, LANDING_PAGE_PATH, MODAL_BUTTON_ACTIONS, DEFAULT_AUTHENTICATION_ENTRY_POINT, DEFAULT_AUTH_MAP, sharedDataService, storageService, alertService, getQueryString, convertToArray, hitch, copy, merge, isBlank) {
    var reauthInProgress = {};

    var launchAuth = function(authURIAndClientCredentials) {
        return $injector.get('languageService').isInitialized().then(function(initialized) {
            return storageService.isInitialized().then(function(initialized) {
                return $injector.get('modalService').open({
                    cssClasses: initialized ? "ySELoginInit" : "ySELoginNotInit",
                    templateUrl: 'web/common/core/services/loginDialog.html',
                    controller: ['modalManager', function(modalManager) {

                        modalManager.setShowHeaderDismiss(false);

                        this.initialized = initialized;
                        this.auth = {
                            username: '',
                            password: ''
                        };
                        this.authURI = authURIAndClientCredentials.authURI;

                        this.authURIKey = btoa(this.authURI).replace(/=/g, "");

                        this.submit = function(loginDialogForm) {
                            var deferred = $q.defer();

                            loginDialogForm.failed = false;
                            loginDialogForm.posted = true;

                            if (loginDialogForm.$valid) {
                                var payload = copy(authURIAndClientCredentials.clientCredentials || {});
                                payload.username = this.auth.username;
                                payload.password = this.auth.password;
                                payload.grant_type = 'password';

                                $injector.get('$http')({
                                    method: 'POST',
                                    url: this.authURI,
                                    headers: {
                                        'Content-Type': 'application/x-www-form-urlencoded'
                                    },
                                    data: getQueryString(payload).replace("?", "")
                                }).
                                success(hitch(this, function(data, status, headers, config) {
                                    storageService.storeAuthToken(this.authURI, data);
                                    $log.debug(["API Authentication Success: ", this.authURI, " status: ", status].join(" "));
                                    modalManager.close();
                                    deferred.resolve();
                                })).
                                error(hitch(this, function(data, status, headers, config) {
                                    storageService.removeAuthToken(this.authURI);
                                    $log.debug(["API Authentication Failure: ", this.authURI, " status: ", status].join(" "));
                                    loginDialogForm.failed = true;
                                    deferred.reject();
                                }));
                            } else {
                                deferred.reject();
                            }
                            return deferred.promise;
                        };

                    }]
                });
            });
        });
    };

    /**
     * @ngdoc method
     * @name authenticationModule.service:authenticationService#filterEntryPoints
     * @methodOf authenticationModule.service:authenticationService
     *
     * @description
     * Will retrieve all relevant authentication entry points for a given resource.
     * A relevant entry point is an entry value of the authenticationMap found in {@link sharedDataServiceModule.service:sharedDataService sharedDataService}.The key used in that map is a regular expression matching the resource.
     * When no entry point is found, the method returns the {@link resourceLocationsModule.object:DEFAULT_AUTHENTICATION_ENTRY_POINT DEFAULT_AUTHENTICATION_ENTRY_POINT}
     * @param {string} resource The URL for which a relevant authentication entry point must be found.
     */
    var filterEntryPoints = function(resource) {
        return sharedDataService.get('authenticationMap').then(function(authenticationMap) {
            authenticationMap = merge(authenticationMap || {}, DEFAULT_AUTH_MAP);
            return convertToArray(authenticationMap).filter(function(entry) {
                return (new RegExp(entry.key, 'g')).test(resource);
            }).map(function(element) {
                return element.value;
            });
        });
    };

    /**
     * @ngdoc method
     * @name authenticationModule.service:authenticationService##isAuthEntryPoint
     * @methodOf authenticationModule.service:authenticationService
     *
     * @description
     * Indicates if the resource URI provided is one of the registered authentication entry points.
     *
     * @param {String} resource The URI to compare
     * @returns {Boolean} Flag that will be true if the resource URI provided is an authentication entry point.
     */
    var isAuthEntryPoint = function(resource) {
        return sharedDataService.get('authenticationMap').then(function(authenticationMap) {
            var authEntryPoints = convertToArray(authenticationMap).map(function(element) {
                return element.value;
            });
            return authEntryPoints.indexOf(resource) > -1 || resource === DEFAULT_AUTHENTICATION_ENTRY_POINT;
        });
    };
    /*
     * will try determine first relevant authentication entry point from authenticationMap and retrieve potential client credentials to be added on top of user credentials
     */
    var findAuthURIAndClientCredentials = function(resource) {
        return filterEntryPoints(resource).then(function(entryPoints) {
            return sharedDataService.get('credentialsMap').then(function(credentialsMap) {
                credentialsMap = credentialsMap || {};
                var authURI = entryPoints[0];
                return {
                    'authURI': authURI,
                    'clientCredentials': credentialsMap[authURI]
                };

            });
        });
    };

    /**
     * @ngdoc method
     * @name authenticationModule.service:authenticationService#authenticate
     * @methodOf authenticationModule.service:authenticationService
     *
     * @description
     * Authenticates the current SmartEdit user against the entry point assigned to the requested resource. If no
     * suitable entry point is found, the resource will be authenticated against the
     * {@link resourceLocationsModule.object:DEFAULT_AUTHENTICATION_ENTRY_POINT DEFAULT_AUTHENTICATION_ENTRY_POINT}
     *
     * @param {String} resource The URI identifying the resource to access.
     * @returns {Promise} A promise that resolves if the authentication is successful.
     */
    var authenticate = function(resource) {
        return findAuthURIAndClientCredentials(resource).then(function(authURIAndClientCredentials) {
            return launchAuth(authURIAndClientCredentials).then(function(success) {
                reauthInProgress[authURIAndClientCredentials.authURI] = false;
            });
        });
    };

    /**
     * @ngdoc method
     * @name authenticationModule.service:authenticationService#logout
     * @methodOf authenticationModule.service:authenticationService
     *
     * @description
     * The logout method removes all stored authentication tokens and redirects to the
     * landing page.
     *
     */
    var logout = function() {
        storageService.removeAllAuthTokens();
        var $location = $injector.get('$location');
        var currentLocation = $location.url();
        if (isBlank(currentLocation) || currentLocation === LANDING_PAGE_PATH) {
            $injector.get('$route').reload();
        } else {
            $location.url(LANDING_PAGE_PATH);
        }
    };

    return {

        authenticate: authenticate,
        logout: logout,
        /**
         * @ngdoc method
         * @name authenticationModule.service:authenticationService#isReAuthInProgress
         * @methodOf authenticationModule.service:authenticationService
         *
         * @description
         * Used to indicate if the user is currently within a re-authentication flow.
         *
         * @param {String} entryPoint The entry point to check
         * @returns {Boolean} Flag will be true if the user must be re-authenticated.
         */
        isReAuthInProgress: function(entryPoint) {
            return reauthInProgress[entryPoint] === true;
        },
        /**
         * @ngdoc method
         * @name authenticationModule.service:authenticationService#setReAuthInProgress
         * @methodOf authenticationModule.service:authenticationService
         *
         * @description
         * Used to indicate that the user is currently within a re-authentication flow for the given entry point.
         * This flow is entered by default through authentication token expiry.
         *
         * @param {String} entryPoint The entry point which the user must be re-authenticated against.
         *
         */
        setReAuthInProgress: function(entryPoint) {
            reauthInProgress[entryPoint] = true;
        },
        filterEntryPoints: filterEntryPoints,
        isAuthEntryPoint: isAuthEntryPoint
    };
});
