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
angular.module('innerapp', ['ui.bootstrap', 'toolbarModule'])
    .run(function(gatewayFactory) {
        gatewayFactory.initListener();
    })
    .controller('defaultController', function($scope, $q, toolbarServiceFactory, customTimeout, systemEventService) {
        var toolbarService = toolbarServiceFactory.getToolbarService('toolbar');
        $scope.sendActions = function() {
            toolbarService.addItems([{
                type: 'ACTION',
                i18nKey: 'toolbar.action.action3',
                callback: function() {
                    console.info('called');
                    $scope.message = 'Action 3 called';
                    setTimeout(function() {
                        delete $scope.message;
                        $scope.$digest();
                    }, 2000);
                },
                icons: {
                    'default': 'icon3.png'
                }
            }, {
                type: 'ACTION',
                i18nKey: 'toolbar.action.action4',
                callback: function() {
                    console.info('called');
                    $scope.message = 'Action 4 called';
                    setTimeout(function() {
                        delete $scope.message;
                        $scope.$digest();
                    }, 2000);
                },
                icons: {
                    'default': 'icon4.png'
                }
            }]);
        };
    });
