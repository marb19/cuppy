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
angular.module('CMSApp', [
    'clickAnalyticsDecorator'
]);

angular.module('CMSApp')
    .run(function(decoratorService) {

        decoratorService.addMappings({
            'componentType1': ['clickAnalytics'],
        });
    });

angular.module('clickAnalyticsDecorator', ['decoratortemplates', 'restServiceFactoryModule'])
    .directive('clickAnalytics', ['restServiceFactory', function(restServiceFactory) {
        return {
            templateUrl: 'web/decorators/clickAnalytics/clickAnalyticsDecoratorTemplate.html',
            restrict: 'C',
            transclude: true,
            replace: false,
            scope: {
                smarteditComponentId: '@',
                smarteditComponentType: '@',
                active: '='
            },

            link: function($scope, element, attrs) {

                $scope.getClickAnalytics = function() {
                    restServiceFactory.get("someapi").get().then(function() {});
                };

            }
        };
    }]);
angular.module('decoratortemplates', []).run(['$templateCache', function($templateCache) {
    'use strict';

    $templateCache.put('web/decorators/clickAnalytics/clickAnalyticsDecoratorTemplate.html',
        "<div>\n" +
        "    <div class=\"row\">\n" +
        "        <div data-ng-transclude></div>\n" +
        "    </div>\n" +
        " <button id='submitButton' data-ng-click='getClickAnalytics()'>click</button>" +
        "</div>"
    );

}]);
