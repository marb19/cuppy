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
(function() {
    angular.module('basicTabModule', ['genericEditorModule', 'cmsWebservicesConstantsModule'])
        .directive('basicTab', function($log, $q, cmsWebservicesConstants) {
            return {
                restrict: 'E',
                transclude: false,
                templateUrl: 'web/features/cmssmarteditContainer/editorTabset/tabs/tabInnerTemplate.html',
                scope: {
                    saveTab: '=',
                    resetTab: '=',
                    cancelTab: '=',
                    isDirtyTab: '=',
                    componentId: '=',
                    componentType: '=',
                    tabId: '='
                },
                link: function(scope, elem, attr) {
                    scope.tabStructure = [{
                        cmsStructureType: "ShortString",
                        qualifier: "name",
                        i18nKey: 'type.cmsitem.name.name'
                    }, {
                        cmsStructureType: "Date",
                        qualifier: "creationtime",
                        i18nKey: 'type.item.creationtime.name',
                        editable: false
                    }, {
                        cmsStructureType: "Date",
                        qualifier: "modifiedtime",
                        i18nKey: 'type.item.modifiedtime.name',
                        editable: false
                    }];
                    scope.contentApi = cmsWebservicesConstants.ITEMS;
                }
            };
        });
})();
