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
angular.module('componentMenuModule', ['componentsModule', 'componentServiceModule'])
    .controller('ComponentMenuController', function($log, ComponentService) {

        var self = this;
        self.types = {};
        self.visibleItems = {};

        ComponentService.loadComponentTypes().then(function() {
            angular.copy(ComponentService.listOfComponentTypes, self.types);
        });

        self.loadComponentItems = function() {

            ComponentService.loadComponentItems().then(function(response) {
                self.visibleItems.componentItems = response.componentItems.filter(function(item) {
                    return item.visible;
                });
            });
        };

        self.preventDefault = function(oEvent) {
            oEvent.stopPropagation();
        };

    })
    .filter('nameFilter', function() {
        return function(components, criteria) {
            var filterResult = [];
            if (!criteria || criteria.length < 3)
                return components;

            criteria = criteria.toLowerCase();
            criteriaList = criteria.split(" ");

            (components || []).forEach(function(component) {
                var match = true;
                var term = component.name.toLowerCase();

                criteriaList.forEach(function(item) {
                    if (term.indexOf(item) == -1) {
                        match = false;
                        return false;
                    }
                });

                if (match && filterResult.indexOf(component) == -1) {
                    filterResult.push(component);
                }
            });
            return filterResult;
        };
    })
    /**
     * @ngdoc directive
     * @name componentMenuModule.directive:componentMenu
     * @scope
     * @restrict E
     * @element ANY
     *
     * @description
     * Component Menu widget that shows all the component types and customized components.
     */
    .directive('componentMenu', function($rootScope, systemEventService, $q) {
        return {
            templateUrl: 'web/features/cmssmarteditContainer/componentMenu/componentMenuWidgetTemplate.html',
            restrict: 'E',
            transclude: true,
            $scope: {},
            link: function($scope, elem, attrs) {

                $scope.parentBtn = elem.closest('.ySEHybridAction');

                $scope.status = {
                    isopen: false
                };

                $scope.icons = {
                    closed: "../cmssmartedit/images/icon_add.png",
                    open: "../cmssmartedit/images/icon_add_s.png"
                };

                $scope.menuIcon = $scope.icons.closed;

                $scope.updateIcon = function() {
                    if ($scope.status.isopen) {
                        $scope.menuIcon = $scope.icons.open;
                        $scope.parentBtn.addClass("ySEOpenComponent");
                    } else {
                        $scope.menuIcon = $scope.icons.closed;
                        $scope.parentBtn.removeClass("ySEOpenComponent");
                    }
                };

                systemEventService.registerEventHandler('ySEComponentMenuClose', function() {
                    $scope.status.isopen = false;
                    return $q.when();
                });

                $scope.$watch('status.isopen', $scope.updateIcon);

                $scope.preventDefault = function(oEvent) {
                    oEvent.stopPropagation();
                };

            }
        };
    });
