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

    var STORE_FRONT_CONTEXT_VAR = '/storefront';


    angular.module('resourceLocationsModule', [])

    /**
     * @ngdoc object
     * @name resourceLocationsModule.object:SMARTEDIT_ROOT
     *
     * @description
     * the name of the webapp root context
     */
    .constant('SMARTEDIT_ROOT', 'smartedit')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:SMARTEDIT_RESOURCE_URI_REGEXP
         *
         * @description
         * to calculate platform domain URI, this regular expression will be used
         */
        .constant('SMARTEDIT_RESOURCE_URI_REGEXP', /^(.*)\/smartedit/)
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:CONFIGURATION_URI
         *
         * @description
         * the name of the SmartEdit configuration API root
         */
        .constant('CONFIGURATION_URI', '/smartedit/configuration/:key')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:CMSWEBSERVICES_RESOURCE_URI
         *
         * @description
         * Constant for the cmswebservices API root
         */
        .constant('CMSWEBSERVICES_RESOURCE_URI', '/cmswebservices')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:DEFAULT_AUTHENTICATION_ENTRY_POINT
         *
         * @description
         * When configuration is not available yet to provide authenticationMap, one needs a default authentication entry point to access configuration API itself
         */
        .constant('DEFAULT_AUTHENTICATION_ENTRY_POINT', '/authorizationserver/oauth/token')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:I18N_RESOURCE_URI
         *
         * @description
         * Resource URI to fetch the i18n initialization map for a given locale.
         */
        .constant('I18N_RESOURCE_URI', '/smarteditwebservices/v1/i18n/languages')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:LANGUAGE_RESOURCE_URI
         *
         * @description
         * Resource URI of the languages REST service.
         */
        .constant('LANGUAGE_RESOURCE_URI', '/cmswebservices/v1/sites/:siteUID/languages')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:CATALOG_VERSION_DETAILS_RESOURCE_URI
         *
         * @description
         * Resource URI of the catalog version details REST service.
         */
        .constant('CATALOG_VERSION_DETAILS_RESOURCE_URI', '/cmswebservices/v1/sites/:siteUID/catalogversiondetails')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:SITES_RESOURCE_URI
         *
         * @description
         * Resource URI of the sites REST service.
         */
        .constant('SITES_RESOURCE_URI', '/cmswebservices/v1/sites')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:LANDING_PAGE_PATH
         *
         * @description
         * Path of the landing page
         */
        .constant('LANDING_PAGE_PATH', '/')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:STOREFRONT_PATH
         *
         * @description
         * Path of the storefront
         */
        .constant('STOREFRONT_PATH', STORE_FRONT_CONTEXT_VAR + '/:siteId/:catalogId/:catalogVersion')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:STORE_FRONT_CONTEXT
         *
         * @description
         * to fetch the store front context for inflection points.
         */
        .constant('STORE_FRONT_CONTEXT', STORE_FRONT_CONTEXT_VAR)
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:CATALOGS_PATH
         *
         * @description
         * Path of the catalogs
         */
        .constant('CATALOGS_PATH', '/cmswebservices/v1/catalogs/')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:MEDIA_PATH
         *
         * @description
         * Path of the media
         */
        .constant('MEDIA_PATH', '/cmswebservices/v1/media')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:CMSWEBSERVICES_PATH
         *
         * @description
         * Path of the cmswebservices
         */
        .constant('CMSWEBSERVICES_PATH', '/cmswebservices')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:PREVIEW_RESOURCE_URI
         *
         * @description
         * Path of the preview ticket API
         */
        .constant('PREVIEW_RESOURCE_URI', '/previewwebservices/v1/preview')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:ENUM_RESOURCE_URI
         *
         * @description
         * Path to fetch list of values of a given enum type
         */
        .constant('ENUM_RESOURCE_URI', "/cmswebservices/v1/enums")
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:SYNC_PATH
         *
         * @description
         * Path of the synchronization service
         */
        .constant('SYNC_PATH', '/cmswebservices/v1/catalogs/:catalog/versions/Staged/synchronizations/versions/Online');

})();
