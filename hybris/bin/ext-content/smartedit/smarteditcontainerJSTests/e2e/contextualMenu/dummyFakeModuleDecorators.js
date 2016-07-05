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
angular.module('FakeModule', [
        'decoratorServiceModule',
        'contextualMenuServiceModule'
    ])
    .run(function(contextualMenuService, decoratorService, smarteditroot) {

        decoratorService.addMappings({
            '^((?!Slot).)*$': ['contextualMenu']
        });

        contextualMenuService.addItems({
            'componentType1': [{
                i18nKey: 'INFO',
                condition: function(componentType, componentId) {
                    return true;
                },
                callback: function() {
                    alert('whatever');
                },
                displayClass: 'editbutton',
                iconIdle: smarteditroot + '/../../smarteditcontainerJSTests/e2e/contextualMenu/icons/info_small.png',
                iconNonIdle: smarteditroot + '/../../smarteditcontainerJSTests/e2e/contextualMenu/icons/info_small.png',
                smallIcon: smarteditroot + '/../../cmssmartedit/icons/info.png'
            }],
            'componentType2': [{
                i18nKey: 'DELETE',
                condition: function(componentType, componentId) {
                    return true;
                },
                callback: function() {
                    alert('delete for paragraph component');
                },
                displayClass: 'hyicon hyicon-remove',
                iconIdle: smarteditroot + '/../../smarteditcontainerJSTests/e2e/contextualMenu/icons/trash_small.png',
                iconNonIdle: smarteditroot + '/../../smarteditcontainerJSTests/e2e/contextualMenu/icons/trash_small.png',
                smallIcon: smarteditroot + '/../../cmssmartedit/icons/info.png'

            }]
        });
    });
