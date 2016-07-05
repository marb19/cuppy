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
 * @name catalogServiceModule
 * @description
 * # The catalogServiceModule
 *
 * The Catalog Service module provides a service that fetches catalogs for a specified site or for all sites registered
 * on the hybris platform.
 */
angular.module('catalogServiceModule', ['restServiceFactoryModule', 'siteServiceModule', 'resourceLocationsModule'])

/**
 * @ngdoc service
 * @name catalogServiceModule.service:catalogService
 *
 * @description
 * The Catalog Service fetches catalogs for a specified site or for all sites registered on the hybris platform using
 * REST calls to the cmswebservices Catalog Version Details API.
 */
.factory('catalogService', function(restServiceFactory, siteService, CATALOG_VERSION_DETAILS_RESOURCE_URI, $q) {
    var cache = {};
    var catalogRestService = restServiceFactory.get(CATALOG_VERSION_DETAILS_RESOURCE_URI);

    function _responseAdapter(catalogsDTO) {
        var isocode = Object.keys(catalogsDTO.name.value)[0];
        catalogsDTO.name = catalogsDTO.name.value[isocode];

        catalogsDTO.catalogVersionDetails = catalogsDTO.catalogVersionDetails.filter(function(catalogVersionDescriptor) {
            return catalogVersionDescriptor.name && catalogVersionDescriptor.name.type && catalogVersionDescriptor.name.value;
        }).map(function(catalogVersionDescriptor) {
            var isocode = Object.keys(catalogVersionDescriptor.name.value)[0];
            catalogVersionDescriptor.name = catalogVersionDescriptor.name.value[isocode];
            return catalogVersionDescriptor;
        });

        return catalogsDTO;
    }

    return {
        /**
         * @ngdoc method
         * @name catalogServiceModule.service:catalogService#getCatalogsForSite
         * @methodOf catalogServiceModule.service:catalogService
         *
         * @description
         * Fetches a list of catalogs for the site that corresponds to the specified site UID. The catalogs are
         * retrieved using REST calls to the cmswebservices catalog ersion details API.
         *
         * @param {String} siteUID The UID of the site that the catalog versions are to be fetched.
         *
         * @returns {Array} An array of catalog descriptors. Each descriptor provides the following catalog properties:
         * catalog (name), catalogId, and catalogVersion.
         */
        getCatalogsForSite: function(siteUID) {
            return cache[siteUID] ? $q.when(cache[siteUID]) : catalogRestService.get({
                siteUID: siteUID
            }).then(function(catalogsDTO) {
                cache[siteUID] = _responseAdapter(catalogsDTO).catalogVersionDetails.reduce(function(acc, catalogVersionDescriptor) {
                    if (catalogVersionDescriptor.name && catalogVersionDescriptor.catalogId && catalogVersionDescriptor.version) {
                        acc.push({
                            name: catalogVersionDescriptor.name,
                            catalogId: catalogVersionDescriptor.catalogId,
                            catalogVersion: catalogVersionDescriptor.version,
                            thumbnailUrl: catalogVersionDescriptor.thumbnailUrl
                        });
                    }
                    return acc;
                }, []).sort(function(catVersion1, catVersion2) {
                    return catVersion1.name.localeCompare(catVersion2.name);
                });
                return cache[siteUID];
            });
        },

        /**
         * @ngdoc method
         * @name catalogServiceModule.service:catalogService#getAllCatalogsGroupedById
         * @methodOf catalogServiceModule.service:catalogService
         *
         * @description
         * Fetches a list of catalog groupings for all sites. The catalogs are retrieved using REST calls to the
         * cmswebservices catalog version details API.
         *
         * @returns {Array} An array of catalog groupings sorted by catalog ID, each of which has a name, a catalog ID, and a list of
         * catalog version descriptors.
         */
        getAllCatalogsGroupedById: function() {
            var catalogGroupings = [];
            var deferred = $q.defer();
            var catalogService = this;

            siteService.getSites().then(function(sites) {
                var promises = [];

                sites.forEach(function(site, index) {
                    promises.push(catalogService.getCatalogsForSite(site.uid).then(function(catalogs) {
                        return catalogs.map(function(catalog) {
                            catalog.siteDescriptor = site;
                            return catalog;
                        });
                    }));
                });

                $q.all(promises).then(function(catalogs) {
                    catalogs = catalogs.reduce(function(allCatalogs, catalogsSubset) {
                        allCatalogs = allCatalogs.concat(catalogsSubset);
                        return allCatalogs;
                    }, []);

                    catalogs.forEach(function(catalog) {
                        var matchedCatalogGrouping = null;
                        catalogGroupings.forEach(function(catalogGrouping) {
                            if (catalogGrouping.catalogId == catalog.catalogId) {
                                matchedCatalogGrouping = catalogGrouping;
                            }
                        });

                        if (!matchedCatalogGrouping) {
                            matchedCatalogGrouping = {
                                name: catalog.name,
                                catalogId: catalog.catalogId,
                                catalogVersions: []
                            };
                            catalogGroupings.push(matchedCatalogGrouping);
                        }

                        matchedCatalogGrouping.catalogVersions.push(catalog);
                    });

                    deferred.resolve(catalogGroupings);

                }, function() {
                    deferred.reject();
                });

            }, function() {
                deferred.reject();
            });

            return deferred.promise;
        }
    };
});
