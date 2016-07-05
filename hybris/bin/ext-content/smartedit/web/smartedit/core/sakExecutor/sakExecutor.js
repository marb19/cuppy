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
angular.module('sakExecutorDecorator', ['coretemplates', 'PerspectiveModule', 'eventServiceModule'])
    .factory('sakExecutor', function($compile, decoratorFilterService) {

        _array = [];

        return {
            wrapDecorators: function(pristineClone, transcludeFn, smarteditComponentId, smarteditComponentType) {

                decoratorFilterService.storePrecompiledComponent(smarteditComponentId, smarteditComponentType, pristineClone);

                var decorators = decoratorFilterService.getDecoratorsForComponent(smarteditComponentType);

                var template = "<div data-ng-transclude></div>";
                if (decorators.length > 0) {
                    for (var i in decorators) {
                        var clazz = decorators[i];
                        template = "<div data-perspective='" + decoratorFilterService.getCurrentPerspective().name + "' class='" + clazz + "' data-active='active' data-smartedit-component-id='{{smarteditComponentId}}' data-smartedit-component-type='{{smarteditComponentType}}'>" + template;
                        template += "</div>";
                    }
                }
                return $compile(template, transcludeFn);

            },
            setPending: function(type, id) {
                _array.push(type + "_" + id);
            },
            removePending: function(type, id) {
                var index = _array.indexOf(type + "_" + id);
                _array.splice(index, 1);
            },
            hasPending: function() {
                return _array.length > 0;
            }
        };
    })
    .directive('smartEditComponent', function($rootScope, $q, $timeout, sakExecutor, systemEventService) {
        return {
            restrict: 'C',
            transclude: true,
            replace: false,
            scope: {
                smarteditComponentId: '@',
                smarteditComponentType: '@'
            },
            link: function($scope, element, attrs, controller, transcludeFn) {
                sakExecutor.setPending($scope.smarteditComponentType, $scope.smarteditComponentId);
                var DECORATOR_HOVER_EVENT = 'decorator_event';
                $scope.active = false;

                var pristineClone;
                transcludeFn($scope, function(clone) {
                    pristineClone = angular.element("<div/>").append(clone).html();
                    var compiled = sakExecutor.wrapDecorators(pristineClone, transcludeFn, $scope.smarteditComponentId, $scope.smarteditComponentType);
                    element.append(compiled($scope));
                    $timeout(function() {
                        sakExecutor.removePending($scope.smarteditComponentType, $scope.smarteditComponentId);
                    }, 0);
                    var inactivateDecorator = function() {
                        $scope.active = false;
                    };

                    var onDecoratorEvent = function(event, args) {
                        if (args.componentId != $scope.smarteditComponentId || args.componentType !== $scope.smarteditComponentType) {
                            inactivateDecorator();
                        }

                        return $q.when(true);
                    };

                    // This is necessary to inactivate the decorator when the mouse leaves the component, but hasn't
                    // entered to a new component.
                    if ($scope.smarteditComponentType !== 'ContentSlot') {
                        element.bind("mouseleave", function($event) {
                            $rootScope.$apply(inactivateDecorator);
                        });

                        element.bind("mouseenter", function($event) {
                            $rootScope.$apply(function() {
                                // This is necessary to deactivate all decorators that might be active.
                                systemEventService.sendSynchEvent(DECORATOR_HOVER_EVENT, {
                                    componentId: $scope.smarteditComponentId,
                                    componentType: $scope.smarteditComponentType
                                });
                                $scope.active = true;
                            });
                        });
                    }
                    systemEventService.registerEventHandler(DECORATOR_HOVER_EVENT, onDecoratorEvent);

                    $scope.$on('$destroy', function() {
                        systemEventService.unRegisterEventHandler(DECORATOR_HOVER_EVENT, onDecoratorEvent);
                    });
                });


            }
        };
    });
