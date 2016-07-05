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
angular.module('syncStatusModule', [])
    .directive('syncStatus', function() {
        return {
            templateUrl: 'web/smarteditcontainer/core/services/pageToolMenu/syncStatus/syncStatusTemplate.html',
            restrict: 'E',
            transclude: false,
            link: function(scope, elem, attrs) {
                scope.syncDate = Date.now();
                scope.syncStatus = "sync.status.synced.published";
            }
        };
    });
