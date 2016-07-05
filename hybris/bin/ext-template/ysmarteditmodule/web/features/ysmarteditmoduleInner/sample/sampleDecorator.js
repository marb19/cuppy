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
angular.module('sampleDecorator', ['ysmarteditmoduleInnerTemplates'])
    .directive('sample', function() {
        return {
            templateUrl: 'web/features/ysmarteditmoduleInner/sample/sampleDecoratorTemplate.html',
            restrict: 'C',
            transclude: true,
            replace: false,
            scope: {
                smarteditComponentId: '@',
                smarteditComponentType: '@',
                active: '='
            },

            link: function($scope, element, attrs) {

                $scope.abTitleKey = 'sample.popover.title';
                $scope.abContent = "A : 30%, B : 70%";
            }
        };
    });
