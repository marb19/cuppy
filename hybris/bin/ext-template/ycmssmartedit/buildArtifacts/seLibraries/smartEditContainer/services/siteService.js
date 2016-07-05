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
 * @name siteServiceModule
 * @description
 * # The siteServiceModule
 *
 * The Site Service module provides a service that fetches all sites that have been configured on the hybris platform.
 */
angular.module('siteServiceModule', ['restServiceFactoryModule', 'resourceLocationsModule'])

/**
 * @ngdoc service
 * @name siteServiceModule.service:siteService
 *
 * @description
 * The Site Service fetches all sites configured on the hybris platform using REST calls to the cmswebservices sites API.
 */
.factory('siteService', function(restServiceFactory, SITES_RESOURCE_URI, $q) {
    var cache = null;
    var siteRestService = restServiceFactory.get(SITES_RESOURCE_URI);

    function _responseAdapter(sitesDTO) {
        sitesDTO.sites = sitesDTO.sites.map(function(site) {
            var isocode = Object.keys(site.name.value)[0];
            site.name = site.name.value[isocode];
            return site;
        });
        return sitesDTO;
    }

    return {
        /**
         * @ngdoc method
         * @name siteServiceModule.service:siteService#getSites
         * @methodOf siteServiceModule.service:siteService
         *
         * @description
         * Fetches a list of sites configured on the hybris platform. The list of sites if fetched using REST calls to
         * the cmswebservices sites API.
         *
         * @returns {Array} An array of site descriptors. Each descriptor provides the following site properties: uid,
         * name, previewUrl, and redirectUrl.
         */
        getSites: function() {
            return cache ? $q.when(cache) : siteRestService.get().then(function(sitesDTO) {
                cache = _responseAdapter(sitesDTO).sites.sort(function(site1, site2) {
                    return site1.name.localeCompare(site2.name);
                });
                return cache;
            });
        },
        /**
         * @ngdoc method
         * @name siteServiceModule.service:siteService#getSiteById
         * @methodOf siteServiceModule.service:siteService
         *
         * @description
         * Fetches a site, configured on the hybris platform, by its uid. The sites if fetched using REST calls to
         * the cmswebservices sites API.
         *
         * @returns {object} a site descriptos. a descriptor provides the following site properties: uid,
         * name, previewUrl, and redirectUrl.
         */
        getSiteById: function(uid) {
            return this.getSites().then(function(sites) {
                return sites.filter(function(site) {
                    return site.uid == uid;
                })[0];
            });
        }
    };
});
