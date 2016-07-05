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
angular.module('smartedit', [
        'sakExecutorDecorator',
        'restServiceFactoryModule',
        'ui.bootstrap',
        'ngResource',
        'PerspectiveModule',
        'alertsBoxModule',
        'ui.select',
        'httpAuthInterceptorModule',
        'httpErrorInterceptorModule',
        'httpSecurityHeadersInterceptorModule',
        'experienceInterceptorModule',
        'languageInterceptorModule',
        'gatewayFactoryModule',
        'renderServiceModule',
        'iframeClickDetectionServiceModule',
        'sanitizeHtmlInputModule'
    ])
    .config(function($logProvider) {
        $logProvider.debugEnabled(false);
    })
    .directive('html', function() {
        return {
            restrict: "E",
            replace: false,
            transclude: false,
            priority: 1000,
            link: function($scope, element) {
                element.addClass('smartedit-html-container');
            }
        };
    })
    .directive('body', function(sakExecutor, gatewayFactory) {
        return {
            restrict: "E",
            replace: false,
            transclude: false,
            priority: 1000,
            link: function($scope, element) {
                var ready = 0;
                var done = false;
                $scope.$watch(sakExecutor.hasPending, function(value) {
                    if (value === false) {
                        ready++;
                    }
                    if (ready === 1 && !done) {
                        done = true;
                        gatewayFactory.createGateway('smartEditBootstrap').publish('smartEditReady');
                        element.attr('data-smartedit-ready', 'true');
                    }
                });
            }
        };
    })

.controller('SmartEditController', ['$scope', function($scope) {}])
    /*
     Do not remove renderService! It is added to the signature of the method to force loading it. Otherwise componentRender calls from
     the container into SmartEdit will fail.
     */
    .run(function($rootScope, $log, $compile, $http, decoratorFilterService, restServiceFactory, hitch, domain, gatewayFactory, renderService) {

        gatewayFactory.initListener();

        restServiceFactory.setDomain(domain);

        decoratorFilterService.registerPerspectiveChangedListener(
            function(currentPerspective) {
                $log.debug("Perspective changed to: " + currentPerspective.name + ", with decorators: " + currentPerspective.decorators);

                var precompiledComponents = decoratorFilterService.getPrecompiledComponents();
                for (var c in precompiledComponents) {
                    var component = precompiledComponents[c];

                    var selector = "[data-smartedit-component-id='" + component.id + "'][data-smartedit-component-type='" + component.type + "']";
                    $(selector).empty();
                    $(selector).append(component.html);
                    $compile($(selector))($rootScope);
                }

            }
        );

    });
