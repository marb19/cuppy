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
window.smartEditBootstrapped = {}; //storefront actually loads twice all the JS files, including webApplicationInjector.js, smartEdit must be protected against receiving twice a smartEditBootstrap event
angular.module('smarteditcontainer', [
        'ngRoute',
        'ngResource',
        'ui.bootstrap',
        'coretemplates',
        'loadConfigModule',
        'iFrameManagerModule',
        'alertsBoxModule',
        'httpAuthInterceptorModule',
        'httpSecurityHeadersInterceptorModule',
        'httpErrorInterceptorModule',
        'experienceInterceptorModule',
        'languageInterceptorModule',
        'bootstrapServiceModule',
        'toolbarModule',
        'leftToolbarModule',
        'pageToolMenuModule',
        'modalServiceModule',
        'dragAndDropServiceModule',
        'previewTicketInterceptorModule',
        'previewDataGenericEditorResponseHandlerModule',
        'catalogServiceModule',
        'catalogDetailsModule',
        'experienceSelectorButtonModule',
        'experienceSelectorModule',
        'sharedDataServiceModule',
        'inflectionPointSelectorModule',
        'paginationFilterModule',
        'resourceLocationsModule',
        'fetchMediaDataHandlerModule',
        'experienceServiceModule',
        'eventServiceModule',
        'dragAndDropScrollingModule',
        'urlServiceModule'
    ])
    .config(function(LANDING_PAGE_PATH, STOREFRONT_PATH, $routeProvider, $logProvider) {
        $routeProvider.when(LANDING_PAGE_PATH, {
                templateUrl: 'web/smarteditcontainer/fragments/landingPage.html',
                controller: 'landingPageController',
                controllerAs: 'landingCtl'
            })
            .when(STOREFRONT_PATH, {
                templateUrl: 'web/smarteditcontainer/fragments/mainview.html',
                controller: 'defaultController'
            })
            .otherwise({
                redirectTo: LANDING_PAGE_PATH
            });

        $logProvider.debugEnabled(false);
    })
    .run(
        function($rootScope, $log, $q, toolbarServiceFactory, gatewayFactory, loadConfigManagerService, bootstrapService, iFrameManager, dragAndDropService, restServiceFactory, sharedDataService, iFrameManager, urlService, dragAndDropScrollingService) {
            gatewayFactory.initListener();

            loadConfigManagerService.loadAsObject().then(function(configurations) {
                sharedDataService.set('defaultToolingLanguage', configurations.defaultToolingLanguage);
            });

            var smartEditTitleToolbarService = toolbarServiceFactory.getToolbarService("smartEditTitleToolbar");

            smartEditTitleToolbarService.addItemsStyling('nav nav-pills group pull-right');

            smartEditTitleToolbarService.addItems([{
                type: 'TEMPLATE',
                include: 'web/smarteditcontainer/core/services/topToolbars/leftToolbarWrapperTemplate.html',
            }, {
                type: 'TEMPLATE',
                include: 'web/smarteditcontainer/core/services/topToolbars/logoTemplate.html'
            }, {
                type: 'HYBRID_ACTION',
                include: 'web/smarteditcontainer/core/services/topToolbars/deviceSupportTemplate.html'
            }]);

            var experienceSelectorToolbarService = toolbarServiceFactory.getToolbarService("experienceSelectorToolbar");
            experienceSelectorToolbarService.addItemsStyling('col-md-5 col-lg-4 col-lg-offset-1 yPage');

            experienceSelectorToolbarService.addItems([{
                type: 'TEMPLATE',
                include: 'web/smarteditcontainer/core/services/topToolbars/experienceSelectorWrapperTemplate.html'
            }]);

            function offSetStorefront() {
                // Set the storefront offset
                $('#js_iFrameWrapper').css('padding-top', $('.ySmartEditToolbars').height() + 'px');
            }

            var smartEditBootstrapGateway = gatewayFactory.createGateway('smartEditBootstrap');
            smartEditBootstrapGateway.subscribe("reloadFormerPreviewContext", function(eventId, data) {
                offSetStorefront();
                var deferred = $q.defer();
                iFrameManager.initializeCatalogPreview();
                deferred.resolve();
                return deferred.promise;
            });
            smartEditBootstrapGateway.subscribe("loading", function(eventId, data) {
                var deferred = $q.defer();
                iFrameManager.setCurrentLocation(data.location);
                iFrameManager.hide();
                delete window.smartEditBootstrapped[data.location];
                return deferred.promise;
            });
            smartEditBootstrapGateway.subscribe("bootstrapSmartEdit", function(eventId, data) {
                offSetStorefront();
                var deferred = $q.defer();
                if (!window.smartEditBootstrapped[data.location]) {
                    window.smartEditBootstrapped[data.location] = true;

                    loadConfigManagerService.loadAsObject().then(function(configurations) {
                        bootstrapService.bootstrapSEApp(configurations);
                        deferred.resolve();
                    });
                } else {
                    deferred.resolve();
                }
                return deferred.promise;
            });
            smartEditBootstrapGateway.subscribe("smartEditReady", function(eventId, data) {
                var deferred = $q.defer();
                deferred.resolve();

                dragAndDropScrollingService.initialize();
                dragAndDropService.apply();

                iFrameManager.show();
                return deferred.promise;
            });
        })
    /**
     * @ngdoc controller
     * @name smarteditcontainer.controller:landingPageController
     * @param {Service} loadConfigManagerService The load configuration manager service
     * @param {Service} catalogService The catalog service
     * @param {Service} restServiceFactory  The REST service factory
     *
     * @description
     * When called, it will retrieve the list of catalogs to display on the landing page.
     */
    .controller(
        'landingPageController',
        function(loadConfigManagerService, catalogService, restServiceFactory) {
            var vm = this;
            vm.CATALOGS_PER_PAGE = 4;

            loadConfigManagerService.loadAsObject().then(function(configurations) {
                restServiceFactory.setDomain(configurations.domain);
                return catalogService.getAllCatalogsGroupedById();
            }).then(function(catalogs) {
                vm.catalogs = catalogs;
                vm.totalItems = catalogs.length;
                vm.currentPage = 1;
            });

            var bodyTag = angular.element(document.querySelector('body'));
            if (bodyTag.hasClass('is-storefront')) {
                bodyTag.removeClass('is-storefront');
            }
        })
    .controller(
        'defaultController',
        function($log, $routeParams, $location, LANDING_PAGE_PATH, systemEventService, iFrameManager, experienceService, sharedDataService) {

            experienceService.buildDefaultExperience($routeParams).then(function(experience) {
                sharedDataService.set('experience', experience).then(function(experience) {
                    systemEventService.sendAsynchEvent("experienceUpdate");
                    iFrameManager.applyDefault();
                    iFrameManager.initializeCatalogPreview(experience);
                });
            }, function(buildError) {
                $log.error("the provided path could not be parsed: " + $location.url());
                $log.error(buildError);
                $location.url(LANDING_PAGE_PATH);
            });

            var bodyTag = angular.element(document.querySelector('body'));
            bodyTag.addClass('is-storefront');
        });
