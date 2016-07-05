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
angular.module('ysmarteditinnermodule', [
        'toolbarModule', // Toolbar API Module from SmartEdit Application
        'decoratorServiceModule', // Decorator API Module from SmartEdit Application
        'sampleDecorator' // Decorators must be added as dependencies to be wired into SmartEdit
    ])
    .run(
        function(toolbarServiceFactory, decoratorService) { // Parameters are injected factory methods

            ////////////////////////////////////////////////////
            // Wiring Decorators to Component Types
            ////////////////////////////////////////////////////
            decoratorService.addMappings({
                'SimpleResponsiveBannerComponent': ['sample'],
                'CMSParagraphComponent': ['sample']
            });

            ////////////////////////////////////////////////////
            // Wiring Toolbar Actions to Toolbar Service
            ////////////////////////////////////////////////////
            var whiteToolbarService = toolbarServiceFactory.getToolbarService("whiteToolbar");
            whiteToolbarService.addItems([{
                type: 'ACTION',
                i18nKey: 'toolbar.action.myModuleAction',
                callback: function() {
                    console.info("Action from ysmarteditmodule.js");
                },
                icons: {
                    default: '/ysmarteditmodule/icons/info.png'
                }
            }]);

        });
