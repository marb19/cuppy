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
angular.module('componentSearchModule', []).directive('componentSearch', function(iframeClickDetectionService) {
    return {
        templateUrl: 'web/features/cmssmarteditContainer/componentMenu/componentSearchTemplate.html',
        restrict: 'E',
        transclude: false,
        replace: true,

        scope: false,

        link: function(scope, element, attrs) {

            scope.selectSearch = function(oEvent) {
                oEvent.stopPropagation();
            };
            scope.preventDefault = function(oEvent) {
                oEvent.stopPropagation();
            };
            scope.closeComponentSearch = function(oEvent) {
                if (oEvent) {
                    oEvent.preventDefault();
                    oEvent.stopPropagation();
                }
                scope.status.isopen = false;
            };

            iframeClickDetectionService.registerCallback('closeComponentSearch', function() {
                scope.closeComponentSearch();
            });

        }
    };
});
