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
 * @name storageServiceModule
 * @description
 * # The storageServiceModule
 *
 * The Storage Service Module provides a service that allows storing temporary information in the browser.
 *
 */
angular.module('storageServiceModule', ['gatewayProxyModule', 'ngCookies']) //restServiceFactoryModule already requires ngResource and ngRoute
    /**
     * @ngdoc service
     * @name storageServiceModule.service:storageService
     *
     * @description
     * The Storage service is used to store temporary information in the browser. The service keeps track of key/value pairs
     * of authTokens that authenticate the specified user on different URIs.
     *
     */
    .factory("storageService", function($rootScope, $cookies, $window, gatewayProxy) {

        var STORAGE_COOKIE_NAME = "smartedit-sessions";

        var StorageService = function(gatewayId) {
            this.gatewayId = gatewayId;

            gatewayProxy.initForService(this, ['isInitialized', 'storeAuthToken', 'getAuthToken', 'removeAuthToken', 'removeAllAuthTokens']);
        };

        /**
         * @ngdoc method
         * @name storageServiceModule.service:storageService#isInitialized
         * @methodOf storageServiceModule.service:storageService
         *
         * @description
         * This method is used to determine if the storage service has been initialized properly. It
         * makes sure that the smartedit-sessions cookie is available in the browser.
         *
         * @returns {Boolean} Indicates if the storage service was properly initialized.
         */
        StorageService.prototype.isInitialized = function() {
            return $cookies.getObject(STORAGE_COOKIE_NAME) !== undefined;
        };

        /**
         * @ngdoc method
         * @name storageServiceModule.service:storageService#storeAuthToken
         * @methodOf storageServiceModule.service:storageService
         *
         * @description
         * This method creates and stores a new key/value entry. It associates an authentication token with a
         * URI.
         *
         * @param {String} authURI The URI that identifies the resource(s) to be authenticated with the authToken. Will be used as a key.
         * @param {String} auth The token to be used to authenticate the user in the provided URI.
         */
        StorageService.prototype.storeAuthToken = function(authURI, auth) {
            var sessions = this._getFromCookie();
            sessions[authURI] = auth;
            this._setToCookie(sessions);

        };

        /**
         * @ngdoc method
         * @name storageServiceModule.service:storageService#getAuthToken
         * @methodOf storageServiceModule.service:storageService
         *
         * @description
         * This method is used to retrieve the authToken associated with the provided URI.
         *
         * @param {String} authURI The URI for which the associated authToken is to be retrieved.
         * @returns {String} The authToken used to authenticate the current user in the provided URI.
         */
        StorageService.prototype.getAuthToken = function(authURI) {
            var sessions = this._getFromCookie();
            return sessions[authURI];
        };

        /**
         * @ngdoc method
         * @name storageServiceModule.service:storageService#removeAuthToken
         * @methodOf storageServiceModule.service:storageService
         *
         * @description
         * Removes the authToken associated with the provided URI.
         *
         * @param {String} authURI The URI for which its authToken is to be removed.
         */
        StorageService.prototype.removeAuthToken = function(authURI) {
            var sessions = this._getFromCookie();
            delete sessions[authURI];
            this._setToCookie(sessions);
        };

        /**
         * @ngdoc method
         * @name storageServiceModule.service:storageService#removeAllAuthTokens
         * @methodOf storageServiceModule.service:storageService
         *
         * @description
         * This method removes all authURI/authToken key/pairs from the storage service.
         */
        StorageService.prototype.removeAllAuthTokens = function() {
            $cookies.remove(STORAGE_COOKIE_NAME);
        };

        StorageService.prototype._getFromCookie = function() {
            var rawValue = $cookies.getObject(STORAGE_COOKIE_NAME);
            return rawValue ? JSON.parse(atob(rawValue)) : {};
        };
        StorageService.prototype._setToCookie = function(sessions) {
            if (Object.keys(sessions).length > 0) {
                var config = {
                    secure: true
                };
                if ($window.location.protocol.indexOf("https") >= 0) {
                    $cookies.putObject(STORAGE_COOKIE_NAME, btoa(JSON.stringify(sessions)), config);
                } else {
                    $cookies.putObject(STORAGE_COOKIE_NAME, btoa(JSON.stringify(sessions)));
                }

            } else {
                $cookies.remove(STORAGE_COOKIE_NAME);
            }
        };

        return new StorageService("storage");
    });
