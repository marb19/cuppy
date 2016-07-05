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
angular.module('dateTimePickerModule', ['languageServiceModule'])
    .directive('dateTimePicker', function($rootScope, languageService) {
        return {
            templateUrl: 'web/common/services/genericEditor/dateTimePicker/dateTimePickerTemplate.html',
            restrict: 'E',
            transclude: true,
            replace: false,
            scope: {
                name: '=',
                model: '=',
                isEditable: '='
            },
            link: function($scope, elem) {
                $scope.placeholderText = 'componentform.select.date';

                if ($scope.isEditable) {
                    $(elem.children()[0])
                        .datetimepicker({
                            format: 'L LT',
                            showClear: true,
                            showClose: true,
                            useCurrent: false,
                            minDate: 0,
                            keepOpen: true,
                            locale: languageService.getBrowserLocale().split('-')[0]
                        })
                        .on('dp.change', function() {
                            $scope.model = elem.find('input')[0].value;
                            $rootScope.$digest();
                        });
                }
            }
        };
    });
