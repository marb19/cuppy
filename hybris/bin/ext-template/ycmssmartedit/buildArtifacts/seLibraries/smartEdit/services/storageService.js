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
angular.module('storageServiceModule', ['gatewayProxyModule'])
    /**
     * @ngdoc service
     * @name storageServiceModule.service:storageService
     *
     * @description
     * The Storage service is used to store temporary information in the browser. The service keeps track of key/value pairs
     * of authTokens that authenticate the specified user on different URIs.
     *
     */
    .factory("storageService", function(gatewayProxy) {

        var StorageService = function(gatewayId) {
            this.gatewayId = gatewayId;

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
            this.isInitialized = function() {};

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
            this.storeAuthToken = function(authURI, auth) {};

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
            this.getAuthToken = function(authURI) {};

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
            this.removeAuthToken = function(authURI) {};

            /**
             * @ngdoc method
             * @name storageServiceModule.service:storageService#removeAllAuthTokens
             * @methodOf storageServiceModule.service:storageService
             *
             * @description
             * This method removes all authURI/authToken key/pairs from the storage service.
             */
            this.removeAllAuthTokens = function() {};

            gatewayProxy.initForService(this);
        };

        return new StorageService("storage");
    });
