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
angular.module('alertsBoxModule', []).directive('alertsBox', function() {
    return {
        templateUrl: 'web/common/services/alerts/alertsTemplate.html',
        restrict: 'E', // it kicks in on <alerts-box> elements
        transclude: true,
        replace: false,
        scope: {
            alerts: '=',
        },
        link: function(scope, element, attrs) {
            scope.dismissAlert = function(index) {
                scope.alerts.splice(index, 1);
            };
        }
    };
});
