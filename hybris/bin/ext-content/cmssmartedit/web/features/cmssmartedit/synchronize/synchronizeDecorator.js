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
angular.module('synchronizeDecorator', ['cmssmarteditTemplates', 'restServiceFactoryModule', 'translationServiceModule', 'functionsModule'])
    .factory("SynchronizeService", function(restServiceFactory, hitch, $q) {

        var SynchronizeService = function(smarteditComponentType, smarteditComponentId) {
            this.smarteditComponentType = smarteditComponentType;
            this.smarteditComponentId = smarteditComponentId;
            this.restService = restServiceFactory.get("/cmswebservices/:smarteditComponentType/:smarteditComponentId/synchronize");
            this.synced = true;
        };

        SynchronizeService.prototype.load = function() {
            var deferred = $q.defer();
            this.restService.get({
                smarteditComponentType: this.smarteditComponentType,
                smarteditComponentId: this.smarteditComponentId
            }).then(hitch(this, function(response) {
                this.synced = response.status;
                deferred.resolve(this.synced);
            }));
            return deferred.promise;
        };

        SynchronizeService.prototype.synchronize = function() {
            var deferred = $q.defer();
            this.restService.save({
                smarteditComponentType: this.smarteditComponentType,
                smarteditComponentId: this.smarteditComponentId
            }, {}).then(hitch(this, function(response) {
                this.synced = response.status;
                deferred.resolve(this.synced);
            }));
            return deferred.promise;
        };

        return SynchronizeService;
    })
    .directive('synchronize', function($timeout, SynchronizeService) {
        return {
            templateUrl: 'web/features/cmssmartedit/synchronize/synchronizeDecoratorTemplate.html',
            restrict: 'C',
            transclude: true,
            replace: false,
            scope: {
                smarteditComponentId: '@',
                smarteditComponentType: '@',
                active: '='
            },

            link: function($scope, element, attrs) {

                $scope.service = new SynchronizeService($scope.smarteditComponentType, $scope.smarteditComponentId);
                $scope.service.load();

                $scope.isContainer = $scope.smarteditComponentType == 'ContentSlotModel';

                $scope.visible = false;
                $scope.mouseleave = function() {
                    $timeout(function() {
                        $scope.visible = false;
                    }, 1000);
                };
                $scope.mouseenter = function() {
                    $scope.visible = true;
                };
            }
        };
    });
