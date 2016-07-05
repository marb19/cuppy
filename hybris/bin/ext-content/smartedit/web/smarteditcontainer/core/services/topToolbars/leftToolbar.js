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
angular.module('leftToolbarModule', ['authenticationModule', 'iframeClickDetectionServiceModule', 'resourceLocationsModule'])
    .directive('leftToolbar', function($location, $timeout, authenticationService, iframeClickDetectionService, LANDING_PAGE_PATH) {
        return {
            templateUrl: 'web/smarteditcontainer/core/services/topToolbars/leftToolbarTemplate.html',
            restrict: 'E',
            transclude: false,
            replace: true,

            scope: {
                imageRoot: '=imageRoot',
            },

            link: function(scope, element, attrs) {

                scope.showToolbar = function($event) {
                    $event.preventDefault();
                    $('body').toggleClass('nav-expanded-left');
                };

                scope.showSites = function() {
                    scope.closeLeftToolbar();
                    // wait for the css closing animation to be completed
                    $timeout(function() {
                        $location.url(LANDING_PAGE_PATH);
                    }, 400);
                };

                scope.showCfgCenter = function($event) {
                    $event.preventDefault();
                    //$('.left-toolbar').toggleClass('hide');

                    $('#hamburger-menu-level1').addClass('ySELeftHideLevel1').removeClass('ySELeftShowLevel1');
                    $('#hamburger-menu-level2').removeClass('ySELeftHideLevel2').addClass('ySELeftShowLevel2');
                };

                scope.goBack = function() {
                    //$('.left-toolbar').toggleClass('hide');

                    $('#hamburger-menu-level1').removeClass('ySELeftHideLevel1').addClass('ySELeftShowLevel1');
                    $('#hamburger-menu-level2').removeClass('ySELeftShowLevel2').addClass('ySELeftHideLevel2');
                };

                scope.signOut = function($event) {
                    authenticationService.logout();
                };

                $(document).bind("click", function(event) {
                    if (!($(event.target).parents('.leftNav').length > 0 || $(event.target).parents('.navbar-header.pull-left.leftNavBtn').length > 0)) {
                        scope.closeLeftToolbar();
                    }
                });

                scope.closeLeftToolbar = function($event) {
                    if ($event) {
                        $event.preventDefault();
                    }

                    // Go back to the "home" of the menu.
                    $('body').removeClass('nav-expanded-left');
                    scope.goBack();
                };


                iframeClickDetectionService.registerCallback('leftToolbarClose', function() {
                    scope.closeLeftToolbar();
                });
            }
        };
    });
