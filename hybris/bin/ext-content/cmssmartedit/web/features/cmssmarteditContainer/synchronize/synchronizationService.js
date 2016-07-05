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
 * @name synchronizationServiceModule
 * @description
 *
 * The synchronization module contains the service and the directives necessary 
 * to perform catalog synchronization.
 *
 * Use the {@link synchronizationServiceModule.service:synchronizationService synchronizationService} 
 * to call backend API in order to get synchronization status or trigger a catalog synchronaization.
 *
 * Use the {@link synchronizationServiceModule.directive:synchronizeCatalog synchronizeCatalog} directive to display
 * the synchronization area in the landing page for each store.
 *
 */
angular.module('synchronizationServiceModule', ['restServiceFactoryModule', 'resourceLocationsModule', 'translationServiceModule', 'confirmationModalServiceModule', 'alertServiceModule'])
    /**
     * @ngdoc service
     * @name synchronizationServiceModule.service:synchronizationService
     * @description
     *
     * The Synchronization Service will manage the Restful calls to the synchronization 
     * backend API.
     * 
     */
    .factory('synchronizationService', function(restServiceFactory, $q, SYNC_PATH) {

        var restServiceForSync = restServiceFactory.get(SYNC_PATH, 'catalog');

        /**
         * @ngdoc method
         * @name synchronizationServiceModule.service:synchronizationService#updateCatalogSync
         * @methodOf synchronizationServiceModule.service:synchronizationService
         *
         * @description
         * This method allows to synchronize a catalog.
         *
         * Note: Synchronization would happen in one way from staged to online.
         *
         * @param {Object} catalog An object that hold the needed information about the catalog to be synchronized .
         */
        var _updateCatalogSync = function(catalog) {
            return restServiceForSync.update({
                'catalog': catalog.catalogId
            });
        };

        /**
         * @ngdoc method
         * @name synchronizationServiceModule.service:synchronizationService#getCatalogSyncStatus
         * @methodOf synchronizationServiceModule.service:synchronizationService
         *
         * @description
         * This method allows to get the synchronization status of a catalog.
         *
         * @param {Object} catalog An object that hold the needed information about the catalog to be synchronized .
         */
        var _getCatalogSyncStatus = function(catalog) {
            return restServiceForSync.get({
                'catalog': catalog.catalogId
            });
        };

        return {
            getCatalogSyncStatus: _getCatalogSyncStatus,
            updateCatalogSync: _updateCatalogSync
        };

    })

/**
 * @ngdoc directive
 * @name synchronizationServiceModule.directive:synchronizeCatalog
 * @scope
 * @restrict E
 * @element synchronize-catalog
 *
 * @description
 * Directive responsible for displaying the synchronization area in every catalog on the landing page.
 *
 * The directive allows communication with it via one methods. To do so, it exposes the method
 * (syncCatalog). When called, the directive will access the synchronization service to perform synchronization for a catalog.
 *
 * @param {Object} catalog The object that hold the catalog details and allow the directive to load 
 * or update the synchronization data for a specific catalog. 
 * @param {Function} syncCatalog trigger the synchronization of a catalog using the synchronization service.
 * invoke a modal popup to confirm the action before calling the service layer.
 * @param {Object} syncData The modal object that hold the synchronization data for a specific catalog, 
 * directive view elements are bound to this object.
 * 
 */
.directive('synchronizeCatalog', function($timeout, $location, synchronizationService, STOREFRONT_PATH, $q, confirmationModalService, alertService, $translate, catalogDetailsService) {
    return {
        templateUrl: 'web/features/cmssmarteditContainer/synchronize/synchronizationTemplate.html',
        restrict: 'E',
        transclude: true,
        replace: false,
        scope: {
            catalog: '='
        },

        link: function(scope, element, attrs) {
            scope.syncCatalog = function() {
                var deferred = $q.defer();

                $translate('sync.confirm.msg', {
                    catalogName: scope.catalog.name
                }).then(function(translation) {
                    confirmationModalService.confirm({
                        description: translation,
                        title: 'sync.confirmation.title'
                    }).then(function() {

                        scope.syncData.showSyncBtn = false;
                        synchronizationService.updateCatalogSync(scope.catalog).then(
                            function(response) {
                                scope.updateSyncData(response);
                            },
                            function(reason) {
                                $translate('sync.running.error.msg', {
                                    catalogName: scope.catalog.name
                                }).then(function(translationErrorMsg) {
                                    if (reason.statusText == 'Conflict')
                                        alertService.pushAlerts([{
                                            successful: false,
                                            message: translationErrorMsg,
                                            closeable: true
                                        }]);
                                    return false;
                                });
                            }
                        );
                    }, function() {
                        deferred.reject();
                    });
                });
            };

            synchronizationService.getCatalogSyncStatus(scope.catalog).then(
                function(response) {
                    scope.updateSyncData(response);
                });

            scope.updateSyncData = function(response) {

                scope.syncInitiatedFlag = ((response.syncStatus == "RUNNING" || response.syncResult == "UNKNOWN") ? true : false);
                scope.syncData = {
                    showSyncBtn: ((response.syncStatus == "RUNNING" || response.syncResult == "UNKNOWN") ? false : true),
                    lastSyncTS: (response.endDate),
                    startSyncTS: (response.creationDate ? response.creationDate : "NEVER"),
                    showFailure: ((response.syncResult == "ERROR" || response.syncResult == "FAILURE") ? true : false)
                };
            };
        }
    };
});
