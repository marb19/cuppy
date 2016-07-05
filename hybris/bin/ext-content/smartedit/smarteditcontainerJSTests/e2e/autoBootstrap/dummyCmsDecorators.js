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
angular.module('someModule', [
        'textDisplayDecorator',
    ])
    .run(function(decoratorService) {

        decoratorService.addMappings({
            'componentType1': ['textDisplay'],
        });
    });
angular.module('textDisplayDecorator', ['decoratortemplates', 'translationServiceModule'])
    .directive('textDisplay', function() {
        return {
            templateUrl: 'web/features/textDisplay/textDisplayDecoratorTemplate.html',
            restrict: 'C',
            transclude: true,
            replace: false,
            scope: {
                smarteditComponentId: '@',
                smarteditComponentType: '@',
                active: '='
            },

            link: function($scope) {
                $scope.textDisplayContent = $scope.smarteditComponentId + "_Text_from_dummy_decorator";
            }
        };
    });


angular.module('decoratortemplates', []).run(['$templateCache', function($templateCache) {
    'use strict';

    $templateCache.put('web/features/textDisplay/textDisplayDecoratorTemplate.html',
        "<div >\n" +
        "<div class=\"row\" data-ng-if=\"!active\">\n" +
        "</div>\n" +
        "{{textDisplayContent}}\n" +
        "<div data-ng-transclude></div>\n" +
        "</div>"
    );
}]);
