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
angular.module('cmssmartedit', [
        'cmsWebservicesConstantsModule',
        'decoratorServiceModule',
        'contextualMenuServiceModule',
        'removeComponentService',
        'experienceInterceptorModule',
        'editorEnablerServiceModule',
        'alertServiceModule',
        'translationServiceModule'
    ])
    .run(
        // Note: only instances can be injected in a run function
        function(decoratorService, contextualMenuService, alertService, $rootScope, removeComponentService, editorEnablerService, $translate) {

            decoratorService.addMappings({
                '^((?!Link|Slot|SearchBox|Grid|Action|AddToCart).)*$': ['contextualMenu']
            });

            editorEnablerService.enableForComponent("CMSParagraphComponent");
            editorEnablerService.enableForComponent("BannerComponent");
            editorEnablerService.enableForComponent("SimpleBannerComponent");
            editorEnablerService.enableForComponent("CMSLinkComponent");
            editorEnablerService.enableForComponent("SimpleResponsiveBannerComponent");

            contextualMenuService.addItems({
                '^((?!Slot).)*$': [{
                    i18nKey: 'contextmenu.title.dragndrop',
                    condition: function(componentType, componentId) {
                        return true;
                    },
                    callback: function() {},
                    displayClass: 'movebutton',
                    iconIdle: '/cmssmartedit/images/contextualmenu_move_off.png',
                    iconNonIdle: '/cmssmartedit/images/contextualmenu_move_on.png',
                }, {
                    i18nKey: 'contextmenu.title.Remove',
                    condition: function(componentType, componentId) {
                        return true;
                    },
                    callback: function(componentType, componentId, $event) {
                        var slotId = $('[data-smartedit-component-id=' + componentId + ']').closest('[data-smartedit-component-type=ContentSlot]').attr('data-smartedit-component-id');
                        removeComponentService.removeComponent(slotId, componentId).then(
                            function() {
                                $translate('alert.component.removed.from.slot', {
                                    componentID: componentId,
                                    slotID: slotId
                                }).then(function(translation) {
                                    alertService.pushAlerts([{
                                        successful: true,
                                        message: translation,
                                        closeable: true
                                    }]);
                                    $event.preventDefault();
                                    $event.stopPropagation();
                                });

                            }
                        );
                    },
                    displayClass: 'hyicon hyicon-remove',
                    iconIdle: '/cmssmartedit/images/contextualmenu_delete_off.png',
                    iconNonIdle: '/cmssmartedit/images/contextualmenu_delete_on.png',
                    smallIcon: 'hyicon hyicon-remove'

                }]
            });

        });
