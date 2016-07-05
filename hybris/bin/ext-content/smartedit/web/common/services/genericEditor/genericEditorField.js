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
angular.module('genericEditorFieldModule', ['translationServiceModule', 'ui.bootstrap', 'ui.select', 'ui.tinymce', 'ngSanitize'])
    .directive('genericEditorField', function($sanitize) {

        return {
            templateUrl: 'web/common/services/genericEditor/genericEditorFieldTemplate.html',
            restrict: 'E',
            transclude: false,
            replace: false,
            scope: {
                editor: '=',
                field: '=',
                model: '=',
                qualifier: '='
            },

            link: function($scope, element, attrs) {

                $scope.reassignUserCheck = function() {
                    if ($scope.model && $scope.qualifier && $scope.model[$scope.qualifier]) {
                        var content = $scope.model[$scope.qualifier];
                        var sanitizedContent = $sanitize(content);
                        var sanitizedContentMatchesContent = sanitizedContent === content;
                        $scope.field.requiresUserCheck = !sanitizedContentMatchesContent;
                    }
                };
            }
        };
    });
