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
(function() {
    /**
     * @ngdoc service
     * @name cmsWebservicesConstantsModule.cmsWebservicesConstants
     *
     * @description
     * Service which holds cmswebservices URI.
     */
    angular.module('cmsWebservicesConstantsModule', ['experienceInterceptorModule'])
        .factory('cmsWebservicesConstants', function(CONTEXT_CATALOG, CONTEXT_CATALOG_VERSION, CONTEXT_SITE_ID) {
            // var CONTEXT_SITE_ID = 'apprel-de';
            var urlPrefix = '/cmswebservices/v1';
            var urlCatalogPrefix = urlPrefix + '/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION;
            var urlSiteCatalogPrefix = urlPrefix + '/sites/' + CONTEXT_SITE_ID + '/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION;
            var endPointConfig = {
                TYPES: urlPrefix + '/types',
                ITEMS: urlSiteCatalogPrefix + '/items',
                PAGES_CONTENT_SLOT_COMPONENT: urlCatalogPrefix + '/pagescontentslotscomponents',
                CONTENT_SLOT_TYPE_RESTRICTION: urlCatalogPrefix + '/pages/:pageUid/contentslots/:slotUid/typerestrictions'
            };

            return endPointConfig;
        });
})();
