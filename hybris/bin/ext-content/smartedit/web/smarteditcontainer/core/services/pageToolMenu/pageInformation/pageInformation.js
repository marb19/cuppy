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
angular.module('pageInformationModule', ['alertServiceModule'])
    .directive('pageInformation', function($log, alertService) {
        return {
            templateUrl: 'web/smarteditcontainer/core/services/pageToolMenu/pageInformation/pageInformationTemplate.html',
            restrict: 'E',
            transclude: true,
            scope: {
                selectedItemCallbacks: "="
            },
            link: function(scope, elem, attrs) {
                scope.selectedItemCallbacks.onClose = function() {
                    $log.debug('Page Information close callback triggered');
                };
            }
        };
    });
