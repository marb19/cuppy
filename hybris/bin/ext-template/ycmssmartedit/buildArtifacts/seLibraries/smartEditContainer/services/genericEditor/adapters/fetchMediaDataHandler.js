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
angular.module('fetchMediaDataHandlerModule', ['FetchDataHandlerInterfaceModule', 'experienceInterceptorModule', 'restServiceFactoryModule', 'resourceLocationsModule'])

.factory('fetchMediaDataHandler', function(FetchDataHandlerInterface, restServiceFactory, hitch, copy, extend, $q, CONTEXT_CATALOG, CONTEXT_CATALOG_VERSION, CATALOGS_PATH, MEDIA_PATH) {

    var FetchMediaDataHandler = function() {
        this.restServiceForMediaSearch = restServiceFactory.get(MEDIA_PATH);
        this.restServiceForMediaFetch = restServiceFactory.get(CATALOGS_PATH + CONTEXT_CATALOG + "/versions/" + CONTEXT_CATALOG_VERSION + "/media");
    };

    FetchMediaDataHandler = extend(FetchDataHandlerInterface, FetchMediaDataHandler);

    FetchMediaDataHandler.prototype.findByMask = function(field, search) {
        var deferred = $q.defer();
        var params = {};
        params.namedQuery = "namedQueryMediaSearchByCodeCatalogVersion";
        params.params = "code:" + search + ",catalogId:" + CONTEXT_CATALOG + ",catalogVersion:" + CONTEXT_CATALOG_VERSION;

        this.restServiceForMediaSearch.get(params).then(
            function(response) {
                deferred.resolve(response.media);
            },
            function() {
                deferred.reject();
            }
        );
        return deferred.promise;
    };

    FetchMediaDataHandler.prototype.getById = function(field, identifier) {
        var deferred = $q.defer();
        this.restServiceForMediaFetch = restServiceFactory.get(CATALOGS_PATH + CONTEXT_CATALOG + "/versions/" + CONTEXT_CATALOG_VERSION + "/media/" + identifier);

        this.restServiceForMediaFetch.get().then(
            function(response) {
                deferred.resolve(response);
            },
            function() {
                deferred.reject();
            }
        );
        return deferred.promise;
    };

    return new FetchMediaDataHandler();
});
