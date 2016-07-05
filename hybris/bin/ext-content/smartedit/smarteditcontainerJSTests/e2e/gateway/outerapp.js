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
angular.module('outerapp', ['ui.bootstrap', 'gatewayFactoryModule'])
    .run(function(gatewayFactory) {
        gatewayFactory.initListener();
    })
    .controller('defaultController', function($rootScope, $scope, $q, gatewayFactory, systemEventService, customTimeout) {

        var gateway1 = gatewayFactory.createGateway('Gateway1');
        var gateway2 = gatewayFactory.createGateway('Gateway2');

        $scope.notifyIframeOnGateway1 = function() {
            this.notifyIframe(gateway1);
        };

        $scope.notifyIframeOnGateway2 = function() {
            this.notifyIframe(gateway2);
        };

        $scope.notifyIframe = function(gateway) {
            gateway.publish("display1", {
                message: 'hello Iframe ! (from parent)'
            }).then(function(returnValue) {
                $scope.acknowledged = "(iframe acknowledged my message and sent back:" + returnValue + ")";
                customTimeout(function() {
                    delete $scope.acknowledged;
                }, 2000);
            }, function() {
                $scope.acknowledged = "(iframe did not acknowledge my message)";
                customTimeout(function() {
                    delete $scope.acknowledged;
                }, 2000);
            });
        };

        gateway1.subscribe("display2", function(eventId, data) {
            var deferred = $q.defer();
            $scope.message = data.message;
            customTimeout(function() {
                delete $scope.message;
            }, 2000);
            deferred.resolve("hello to you iframe");
            return deferred.promise;
        });

    });
