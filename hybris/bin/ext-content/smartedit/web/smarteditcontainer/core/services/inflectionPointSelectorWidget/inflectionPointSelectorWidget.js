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
angular.module('inflectionPointSelectorModule', ['sharedDataServiceModule', 'sharedDataServiceModule', 'iFrameManagerModule', 'resourceLocationsModule', 'iframeClickDetectionServiceModule'])
    .directive('inflectionPointSelector', function(sharedDataService, iFrameManager, iframeClickDetectionService, $document, STORE_FRONT_CONTEXT) {
        return {
            templateUrl: 'web/smarteditcontainer/core/services/inflectionPointSelectorWidget/inflectionPointSelectorWidgetTemplate.html',
            restrict: 'E',
            transclude: true,
            $scope: {},
            link: function($scope, $rootScope, elem, attrs, $route) {

                $scope.isLanding = function() {

                    var sitePageURLchk = $route().context.ownerDocument.URL.indexOf(STORE_FRONT_CONTEXT);

                    if (sitePageURLchk > 0) {
                        return false;
                    } else {
                        return true;
                    }

                };
                $scope.selectPoint = function(choice) {

                    $scope.currentPointSelected = choice;
                    $scope.status.isopen = !$scope.status.isopen;

                    if (choice !== undefined) {
                        iFrameManager.apply(choice, $scope.deviceOrientation);
                    }


                };
                var deviceSupportedLst = [];

                deviceSupportedLst = iFrameManager.getDeviceSupports();

                $scope.currentPointSelected = deviceSupportedLst[5];

                $scope.points = deviceSupportedLst;

                $scope.status = {
                    isopen: false
                };

                $scope.toggleDropdown = function($event) {
                    $event.preventDefault();
                    $event.stopPropagation();
                    $scope.status.isopen = !$scope.status.isopen;
                };

                iframeClickDetectionService.registerCallback('inflectionPointClose', function() {
                    $scope.status.isopen = false;
                });

                $document.on('click', function(event) {

                    if ($(event.target).parents('inflection-point-selector').length <= 0 && $scope.status.isopen) {

                        $scope.status.isopen = false;
                        $scope.$apply();
                    }
                });

            }
        };
    });
