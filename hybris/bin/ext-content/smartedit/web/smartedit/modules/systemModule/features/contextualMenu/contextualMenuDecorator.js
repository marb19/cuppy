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
angular.module('contextualMenuDecorator', ['translationServiceModule', 'contextualMenuServiceModule', 'ui.bootstrap'])
    .directive('contextualMenu', function($timeout, contextualMenuService, smarteditroot) {
        return {
            templateUrl: 'web/smartedit/modules/systemModule/features/contextualMenu/contextualMenuDecoratorTemplate.html',
            restrict: 'C',
            transclude: true,
            replace: false,
            scope: {
                smarteditComponentId: '@',
                smarteditComponentType: '@',
                active: '='
            },

            link: function($scope, element, attrs) {
                var iCtxSize = 50; //fixed for now

                var iBtnMaxCapacity = Math.round(element.width() / iCtxSize) - 1;
                var iLeftBtns = 1;

                if (iBtnMaxCapacity >= 4) {
                    iLeftBtns = 3;
                } else {
                    iLeftBtns = iBtnMaxCapacity - 1;
                }

                $scope.status = {
                    isopen: false
                };

                $scope.toggleDropdown = function($event) {
                    $event.preventDefault();
                    $event.stopPropagation();
                    $scope.status.isopen = !$scope.status.isopen;

                    var oMoreMenuIcon = $($event.target).find('img');

                    if ($scope.status.isopen) {
                        oMoreMenuIcon.attr('src', smarteditroot + '/static-resources/images/more_white.png');
                        oMoreMenuIcon.addClass('bottom-shadow');
                    } else {
                        oMoreMenuIcon.attr('src', smarteditroot + '/static-resources/images/more_bckg.png');
                        oMoreMenuIcon.addClass('bottom-shadow');
                    }
                };

                $scope.isHybrisIcon = function(icon) {
                    var isHybrisIcon = false;
                    if (icon && icon.indexOf("hyicon") != -1) {
                        isHybrisIcon = true;
                    }
                    return isHybrisIcon;
                };

                $scope.getItems = function() {
                    return contextualMenuService.getContextualMenuItems($scope.smarteditComponentType, $scope.smarteditComponentId, iLeftBtns);
                };

                $scope.moreBtn = {
                    condition: function(componentType, componentId) {
                        return true;
                    },
                    callback: function(smarteditComponentType, smarteditComponentId) {
                        $('.dropdown-toggle').dropdown();
                        this.icon = smarteditroot + '/static-resources/images/more_white.png';
                    },
                    displayClass: 'hyicon hyicon-remove',
                    iconIdle: smarteditroot + '/static-resources/images/contextualmenu_more_off.png',
                    iconNonIdle: smarteditroot + '/static-resources/images/contextualmenu_more_on.png'
                };

            }
        };
    });
angular.module('sakExecutorDecorator').requires.push('contextualMenuDecorator');
