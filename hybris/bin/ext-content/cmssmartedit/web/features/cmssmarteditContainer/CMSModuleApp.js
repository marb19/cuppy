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
angular.module('cmssmarteditContainer', [
        'experienceInterceptorModule',
        'cmsWebservicesConstantsModule',
        'cmssmarteditContainerTemplates',
        'toolbarModule',
        'componentMenuModule',
        'editorModalServiceModule',
        'genericEditorModule',
        'eventServiceModule',
        'cmsDragAndDropServiceModule',
        'catalogDetailsModule',
        'synchronizationServiceModule'

    ])
    .run(
        function($log, $rootScope, toolbarServiceFactory, ComponentService, systemEventService, cmsDragAndDropService, catalogDetailsService) {
            var experienceSelectorToolbarService = toolbarServiceFactory.getToolbarService("experienceSelectorToolbar");

            experienceSelectorToolbarService.addItems([{
                    type: 'HYBRID_ACTION',
                    callback: function() {
                        systemEventService.sendSynchEvent('ySEComponentMenuOpen', {});
                    },
                    include: 'web/features/cmssmarteditContainer/componentMenu/componentMenuTemplate.html'
                }

            ]);

            catalogDetailsService.addItems([{
                include: 'web/features/cmssmarteditContainer/synchronize/catalogDetailsSyncTemplate.html'
            }]);

            cmsDragAndDropService.register();
        });
