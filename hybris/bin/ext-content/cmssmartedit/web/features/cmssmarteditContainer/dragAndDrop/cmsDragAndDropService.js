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
/**
 * @ngdoc overview
 * @name cmsDragAndDropServiceModule
 * @description
 * # The cmsDragAndDropServiceModule
 *
 * The CMS Drag and Drop Service Module provides a service that extends the base drag and drop implementation to enable CMS specific features.
 */
angular.module('cmsDragAndDropServiceModule', ['translationServiceModule', 'dragAndDropServiceModule', 'restServiceFactoryModule', 'componentServiceModule', 'editorModalServiceModule', 'eventServiceModule', 'renderServiceModule', 'alertServiceModule', 'removeComponentService'])
    /**
     * @ngdoc service
     * @name cmsDragAndDropServiceModule.service:cmsDragAndDropService
     *
     * @description
     * Service that extends the base SmartEdit drag and drop implementation to enable CMS specific features.
     */
    .factory('cmsDragAndDropService', function($rootScope, $log, $translate, dragAndDropService, restServiceFactory, parseQuery, hitch, ComponentService, editorModalService, systemEventService, renderService, alertService, removeComponentService, cmsWebservicesConstants, domain) {

        var slotDetailRestService = restServiceFactory.get(cmsWebservicesConstants.CONTENT_SLOT_TYPE_RESTRICTION);

        var slotUpdateRestService = restServiceFactory.get(cmsWebservicesConstants.PAGES_CONTENT_SLOT_COMPONENT + '?pageId=:pageId&slotId=:currentSlotId&componentId=:componentId', 'componentId');

        var _getPageUId = function() {
            return /page\-([\w]+)/.exec($('iframe').contents().find('body').attr('class'))[1];
        };

        var _getSelector = function(selector) {
            return $(selector);
        };

        var _getElementChild = function(element, childIndex) {
            return element.children()[childIndex];
        };

        var _removeDynamicStyles = function(dndService) {
            var contentSlots = dndService.getSelectorInIframe(targetSelector);
            contentSlots.removeClass("over-slot-enabled");
            contentSlots.removeClass("over-slot-disabled");
            contentSlots.removeClass("ySEemptySlot");

            var aSlots = dndService.getSelectorInIframe('.ySEDirtyHeight');
            aSlots.removeClass('ySEDirtyHeight');
            aSlots.height(0);
        };

        var _generateDragAndDropErrorMessage = function(sourceComponentId, targetSlotId, requestResponse) {
            var detailedError = (requestResponse.data && requestResponse.data.errors && requestResponse.data.errors.length > 0) ?
                requestResponse.data.errors[0].message : "";

            return $translate('cmsdraganddrop.error', {
                sourceComponentId: sourceComponentId,
                targetSlotId: targetSlotId,
                detailedError: detailedError
            });
        };

        var sourceSelector = ".smartEditComponent[data-smartedit-component-type!='ContentSlot']";
        var targetSelector = ".smartEditComponent[data-smartedit-component-type=ContentSlot]";

        /**
         * @ngdoc method
         * @name cmsDragAndDropServiceModule.service:cmsDragAndDropService#register
         * @methodOf cmsDragAndDropServiceModule.service:cmsDragAndDropService
         *
         * @description
         * Configures and enables a CMS specific implementation of the drag and drop service in the current page.
         *
         */
        var register = function() {

            dragAndDropService.register({
                sourceSelector: sourceSelector,
                targetSelector: targetSelector,
                stopCallback: hitch(this, function(dndService, event, ui) {
                    // Remove any slot styling left (due to 'out' events not triggered properly)
                    this._removeDynamicStyles(dndService);
                }, dragAndDropService),
                startCallback: hitch(this, function(dndService, event, ui) {

                    //find all slots that are with 0 height and set the height taken form their components
                    //this is needed as ACC does not provide consistent slots
                    var cmsDnDService = this;
                    dndService.getSelectorInIframe(targetSelector).each(function(index) {
                        var elementSelector = cmsDnDService._getSelector(this);
                        if (elementSelector.height() === 0) {
                            var aComponents = elementSelector.find(sourceSelector);
                            if (aComponents.length > 0) {
                                elementSelector.height(aComponents.eq(0).height());
                                elementSelector.addClass('ySEDirtyHeight');
                            } else {
                                elementSelector.addClass('ySEemptySlot');
                            }
                        }
                    });
                    this.sourceSlotId = this._getSelector(event.currentTarget).attr('data-smartedit-component-id');

                    systemEventService.sendSynchEvent('ySEComponentMenuClose', {});

                }, dragAndDropService),
                overCallback: hitch(this, function(dndService, event, ui) {
                    // The provided ui.item is the helper. Thus, to get the SmartEdit information it's necessary to retrieve
                    // it from the wrapped element.
                    var wrappedElement = this._getSelector(this._getElementChild(ui.item, 0));
                    var sourceComponentType = wrappedElement.attr('data-smartedit-component-type');
                    var targetSlotId = this._getSelector(event.target).attr('data-smartedit-component-id');
                    slotDetailRestService.get({
                        pageUid: this._getPageUId(),
                        slotUid: targetSlotId
                    }).then(hitch(this, function(response) {

                        var slotIsEnabled = response.validComponentTypes.indexOf(sourceComponentType) > -1;

                        this._getSelector(event.target).addClass(slotIsEnabled ? "over-slot-enabled" : "over-slot-disabled");

                        if (slotIsEnabled) {
                            dndService.getSelectorInIframe('.ySEIconBckg').switchClass('ySEDnDImageRed', 'ySEDnDImageBlue');
                        } else {
                            dndService.getSelectorInIframe('.ySEIconBckg').switchClass('ySEDnDImageBlue', 'ySEDnDImageRed');
                        }
                    }), function() {
                        $log.error("failed to retrieve slot details for slot ");
                    });
                }, dragAndDropService),
                dropCallback: hitch(this, function(event, ui, cancel) {
                    // Unlike the overCallback, the ui.item for this callback item is not the helper, but the actual
                    // element. It should directly have the necessary SmartEdit information.
                    var sourceComponentId = this._getSelector(ui.item).attr('data-smartedit-component-id');
                    var targetSlotId = this._getSelector(event.target).attr('data-smartedit-component-id');
                    var sourceComponentType = this._getSelector(ui.item).attr('data-smartedit-component-type');
                    if (this._getSelector(event.target).hasClass("over-slot-disabled")) {
                        cancel();
                        $translate("drag.and.drop.not.valid.component.type", {
                            slotUID: targetSlotId,
                            componentUID: sourceComponentId
                        }).then(function(translation) {
                            alertService.pushAlerts([{
                                successful: false,
                                message: translation,
                                closeable: true
                            }]);
                        });
                    } else if (this._getSelector(event.target).hasClass('over-slot-enabled')) { //after a cancel, dropCallback is called again but nothing must be done and nothing must prevent it

                        var cmsDragAndDropService = this; // Add variable to avoid excessive hitch nesting.

                        if (this.sourceSlotId === undefined) {
                            // Add new component from type
                            if (sourceComponentId === undefined) {
                                ComponentService.addNewComponent("name", sourceComponentType, this._getPageUId(), targetSlotId, ui.item.index()).then(function(response) {
                                    sourceComponentId = response.uid;
                                    $(ui.item).attr('data-smartedit-component-id', sourceComponentId);
                                    editorModalService.open(sourceComponentType, sourceComponentId).then(
                                        function() {},
                                        function(response) {
                                            removeComponentService.removeComponent(targetSlotId, sourceComponentId).then(function() {}, function() {
                                                cmsDragAndDropService._generateErrorMessage(sourceComponentId, targetSlotId, response).then(function(errorMessage) {
                                                    alertService.pushAlerts([{
                                                        successful: false,
                                                        message: errorMessage,
                                                        closeable: true
                                                    }]);
                                                });
                                            });
                                            cancel();
                                        });
                                }, function(response) {
                                    cmsDragAndDropService._generateErrorMessage(sourceComponentId, targetSlotId, response).then(function(errorMessage) {
                                        alertService.pushAlerts([{
                                            successful: false,
                                            message: errorMessage,
                                            closeable: true
                                        }]);
                                    });

                                    cancel();
                                });

                            } else { // Add exisiting component
                                ComponentService.addExistingComponent(
                                    this._getPageUId(),
                                    sourceComponentId,
                                    targetSlotId,
                                    ui.item.index()
                                ).then(function(response) {
                                    renderService.renderComponent(sourceComponentId, sourceComponentType);
                                }, function(response) {
                                    cmsDragAndDropService._generateErrorMessage(sourceComponentId, targetSlotId, response).then(function(errorMessage) {
                                        alertService.pushAlerts([{
                                            successful: false,
                                            message: errorMessage,
                                            closeable: true
                                        }]);
                                    });

                                    cancel();
                                });
                            }
                        } else {

                            slotUpdateRestService.update({
                                pageId: this._getPageUId(),
                                currentSlotId: this.sourceSlotId,
                                componentId: sourceComponentId,
                                slotId: targetSlotId,
                                position: ui.item.index()
                            }).then(function(response) {}, function() {
                                alertService.pushAlerts([{
                                    successful: false,
                                    message: "failed to move component " + sourceComponentId + " to slot " + targetSlotId,
                                    closeable: true
                                }]);
                                cancel();
                            });
                        }

                    } else {
                        // No longer hovering over the content slot
                        cancel();
                    }
                }),
                outCallback: hitch(this, function(event, ui) {
                    this._getSelector(ui.helper).removeClass("over-active");
                    this._getSelector(event.target).removeClass("over-slot-enabled");
                    this._getSelector(event.target).removeClass("over-slot-disabled");
                }),
                helper: function(event, targetSelector, originalItem) {
                    return "<img class='ySEIconBckg' src='" + domain + "/cmssmartedit/images/components/DefaultComponent.png'/>";
                }
            });

        };

        return {
            register: register,
            _getSelector: _getSelector,
            _getPageUId: _getPageUId,
            _getElementChild: _getElementChild,
            _removeDynamicStyles: _removeDynamicStyles,
            _generateErrorMessage: _generateDragAndDropErrorMessage
        };

    });
