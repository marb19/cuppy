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
angular.module('CMSApp', [
        'ngMockE2E', 'buttonDisplayDecorator', 'resourceLocationsModule', 'languageServiceModule'
    ])
    .run(function(decoratorService, $httpBackend, I18N_RESOURCE_URI, languageService) {

        $httpBackend.whenGET(I18N_RESOURCE_URI + "/" + languageService.getBrowserLocale()).respond({});

        decoratorService.addMappings({
            'innerContentType': ['buttonDisplay']
        });
    });

angular.module('buttonDisplayDecorator', ['decoratortemplates', 'translationServiceModule', 'renderServiceModule'])
    .directive('buttonDisplay', function($log, renderService) {
        return {
            templateUrl: 'web/features/buttonDisplay/buttonDisplayDecoratorTemplate.html',
            restrict: 'C',
            transclude: true,
            replace: false,
            scope: {
                smarteditComponentId: '@',
                smarteditComponentType: '@',
                active: '='
            },

            link: function($scope) {
                $scope.buttonDisplayContent = "Re-Render Component";
                $scope.renderNewContent = function() {
                    renderService.renderComponent("smartEditComponent2", "innerContentType", "New Content From Inside")
                        .then(
                            function() {
                                $log.info("Worked");
                            },
                            function(result) {
                                $log.error("Didn't work. Error: " + result);
                            });
                };
            }
        };
    });

angular.module('decoratortemplates', []).run(['$templateCache', function($templateCache) {
    'use strict';

    $templateCache.put('web/features/buttonDisplay/buttonDisplayDecoratorTemplate.html',
        "<div>\n" +
        "<div class=\"row\" data-ng-if=\"!active\">\n" +
        "</div>\n" +
        "<button id='renderButtonInner' ng-click='renderNewContent()'>{{buttonDisplayContent}}</button>\n" +
        "<div data-ng-transclude></div>\n" +
        "</div>"
    );
}]);
