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
angular.module('componentTypeModule', []).directive('componentType', function($log, domain) {
    return {
        templateUrl: 'web/features/cmssmarteditContainer/componentMenu/componentTypeTemplate.html',
        restrict: 'E',
        transclude: false,
        replace: true,

        link: function(scope, element, attrs) {
            scope.imageRoot = domain + "/cmssmartedit";
        }
    };
});
