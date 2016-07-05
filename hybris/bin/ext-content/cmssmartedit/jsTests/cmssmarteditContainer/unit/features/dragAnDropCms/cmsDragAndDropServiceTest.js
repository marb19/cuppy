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
describe('test cmsDragAndDrop module', function() {

    var cmsDragAndDropService, dragAndDropService, systemEventService, renderService, cmsWebservicesConstants;

    var $q, $rootScope, alerts;

    var currentTargetSelector, targetSelector, helperSelector, componentSelector, iframeSelector, iconBckgSelector, slotsSelector, someIterationElementSelector, $translate, CONTEXT_CATALOG, CONTEXT_CATALOG_VERSION, CONTEXT_SITE_ID;

    var someIterationElement = {
        fdfdfg: 'dfgdfgdfg'
    };

    var event = {
        currentTarget: 'currentTarget',
        target: {
            id: 'targetId'
        }
    };

    var ui = {
        item: {
            index: function() {
                return 0;
            }
        },
        helper: 'helper'
    };

    beforeEach(customMatchers);

    beforeEach(function() {
        angular.module('translationServiceModule', []);
        angular.module('gatewayFactoryModule', []);
        angular.module('renderServiceModule', []);
        angular.module('restServiceFactoryModule', []);
        angular.module('gatewayProxyModule', []);
        angular.module('eventServiceModule', []);
        angular.module('dragAndDropServiceModule', []);
        angular.module('alertServiceModule', []);
        angular.module('editorModalServiceModule', []);
        angular.module('experienceInterceptorModule', []).constant("CONTEXT_CATALOG", "CURRENT_CONTEXT_CATALOG").constant("CONTEXT_CATALOG_VERSION", "CURRENT_CONTEXT_CATALOG_VERSION").constant("CONTEXT_SITE_ID", "CURRENT_CONTEXT_SITE_ID");

    });


    beforeEach(module('cmsWebservicesConstantsModule'));

    beforeEach(module('gatewayFactoryModule', function($provide) {
        gatewayFactory = jasmine.createSpyObj('gatewayFactory', ['initListener']);
        $provide.value('gatewayFactory', gatewayFactory);
    }));

    beforeEach(module('renderServiceModule', function($provide) {
        renderService = jasmine.createSpyObj('renderService', ['renderComponent']);
        $provide.value('renderService', renderService);
    }));

    beforeEach(module('componentServiceModule', function($provide) {
        componentService = jasmine.createSpyObj('ComponentService', ['addNewComponent', 'addExistingComponent', 'removeComponent']);
        $provide.value('ComponentService', componentService);
    }));

    beforeEach(module('gatewayProxyModule', function($provide) {
        gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['initForService', 'asdfasdf']);
        $provide.value('gatewayProxy', gatewayProxy);
    }));

    beforeEach(module('editorModalServiceModule', function($provide) {
        editorModal = jasmine.createSpyObj('editorModalService', ['open']);
        $provide.value('editorModalService', editorModal);
    }));

    beforeEach(module('eventServiceModule', function($provide) {
        systemEventService = jasmine.createSpyObj('systemEventService', ['sendSynchEvent']);
        $provide.value('systemEventService', systemEventService);
    }));

    beforeEach(module('cmsDragAndDropServiceModule', function($provide) {
        dragAndDropService = jasmine.createSpyObj('dragAndDropService', ['register', 'getSelectorInIframe']);
        $provide.value('dragAndDropService', dragAndDropService);

        restServiceFactory = jasmine.createSpyObj('restServiceFactory', ['get']);
        restService = jasmine.createSpyObj('restService', ['get', 'save', 'update']);
        restServiceFactory.get.andReturn(restService);

        $provide.value('restServiceFactory', restServiceFactory);

        $translate = jasmine.createSpy('$translate');
        $provide.value('$translate', $translate);

        $provide.value('translateFilter', function(data) {
            return data;
        });
        alertService = jasmine.createSpyObj('alertService', ['pushAlerts']);
        $provide.value('alertService', alertService);

        $provide.value('domain', 'thedomain');

    }));

    beforeEach(inject(function(_$rootScope_, _cmsDragAndDropService_, _$q_, _cmsWebservicesConstants_) {
        $q = _$q_;
        $rootScope = _$rootScope_;
        cmsDragAndDropService = _cmsDragAndDropService_;
        cmsWebservicesConstants = _cmsWebservicesConstants_;
        currentTargetSelector = jasmine.createSpyObj('currentTargetSelector', ['attr']);
        currentTargetSelector.attr.andReturn('sourceSlotId');

        targetSelector = jasmine.createSpyObj('targetSelector', ['attr', 'removeClass', 'addClass', 'hasClass']);
        targetSelector.attr.andReturn('targetSlotId');
        targetSelector.removeClass.andReturn();
        targetSelector.addClass.andReturn();

        componentSelector = jasmine.createSpyObj('componentSelector', ['attr', 'removeClass']);

        componentSelector.attr.andReturn('componentType1');

        helperSelector = jasmine.createSpyObj('helperSelector', ['removeClass']);

        iframeSelector = jasmine.createSpyObj('iframeSelector', ['attr']);
        iframeSelector.attr.andReturn('source');

        someIterationElementSelector = jasmine.createSpyObj('someIterationElementSelector', ['height', 'find', 'addClass']);


        spyOn(cmsDragAndDropService, '_getSelector').andCallFake(function(selectorValue) {
            if (selectorValue === event.currentTarget) {
                return currentTargetSelector;
            } else if (selectorValue === ui.item) {
                return componentSelector;
            } else if (selectorValue === event.target) {
                return targetSelector;
            } else if (selectorValue === ui.helper) {
                return helperSelector;
            } else if (selectorValue === 'iframe') {
                return iframeSelector;
            } else if (selectorValue === someIterationElement) {
                return someIterationElementSelector;
            }
        });

        spyOn(cmsDragAndDropService, '_getElementChild').andCallFake(function(element, childIndex) {
            return element;
        });

        ySEemptySlotSelector = jasmine.createSpyObj('ySEemptySlotSelector', ['removeClass']);
        ySEDirtyHeightSelector = jasmine.createSpyObj('ySEDirtyHeightSelector', ['removeClass', 'height']);
        slotsSelector = jasmine.createSpyObj('slotsSelector', ['switchClass', 'each', 'removeClass']);
        iconBckgSelector = jasmine.createSpyObj('iconBckgSelector', ['switchClass']);
        dragAndDropService.getSelectorInIframe.andCallFake(function(selectorValue) {
            if (selectorValue === '.ySEIconBckg') {
                return iconBckgSelector;
            } else if (selectorValue === '.ySEemptySlot') {
                return ySEemptySlotSelector;
            } else if (selectorValue === '.ySEDirtyHeight') {
                return ySEDirtyHeightSelector;
            } else if (selectorValue === '.smartEditComponent[data-smartedit-component-type=ContentSlot]') {
                return slotsSelector;
            }
        });

        spyOn(cmsDragAndDropService, '_getPageUId').andReturn('pageUid');
        spyOn(cmsDragAndDropService, '_removeDynamicStyles').andCallThrough();
    }));

    it('cmsDragAndDropService.register will call register method of dragAndDropService', function() {

        dragAndDropService.register.andReturn();
        cmsDragAndDropService.register();

        expect(dragAndDropService.register).toHaveBeenCalledWith(jasmine.objectContaining({
            sourceSelector: ".smartEditComponent[data-smartedit-component-type!='ContentSlot']",
            targetSelector: ".smartEditComponent[data-smartedit-component-type=ContentSlot]",
        }));

    });

    describe('test callbacks in the payload', function() {

        beforeEach(inject(function() {

            dragAndDropService.register.andReturn();
            cmsDragAndDropService.register();
            payload = dragAndDropService.register.calls[0].args[0];

        }));

        it('startCallback will set expected sourceSlotId and apply height modification of targeted slots', function() {

            payload.startCallback(event, ui);
            expect(cmsDragAndDropService._getSelector).toHaveBeenCalledWith(event.currentTarget);
            expect(cmsDragAndDropService.sourceSlotId).toEqual('sourceSlotId');
            expect(dragAndDropService.getSelectorInIframe).toHaveBeenCalledWith('.smartEditComponent[data-smartedit-component-type=ContentSlot]');
            var eachCallback = slotsSelector.each.calls[0].args[0];
            expect(slotsSelector.each).toHaveBeenCalledWith(jasmine.any(Function));

        });


        it('startCallback will apply no height modification if slot has height', function() {

            payload.startCallback(event, ui);
            var eachCallback = slotsSelector.each.calls[0].args[0];

            someIterationElementSelector.height.andReturn(2);

            eachCallback.apply(someIterationElement);

            expect(someIterationElementSelector.addClass).not.toHaveBeenCalled();
            expect(someIterationElementSelector.height).not.toHaveBeenCalledWith(jasmine.any(String));

        });

        it('startCallback will apply ySEemptySlot css class to 0-height slots that have no components', function() {

            payload.startCallback(event, ui);
            var eachCallback = slotsSelector.each.calls[0].args[0];

            someIterationElementSelector.height.andReturn(0);

            var componentSelector = jasmine.createSpyObj("componentSelector", ["height"]);
            componentSelector.length = 0;
            someIterationElementSelector.find.andReturn(componentSelector);
            eachCallback.apply(someIterationElement);

            expect(someIterationElementSelector.addClass).toHaveBeenCalledWith('ySEemptySlot');
            expect(someIterationElementSelector.height).not.toHaveBeenCalledWith(jasmine.any(String));

        });

        it('startCallback will apply ySEDirtyHeight css class and component height to 0-height slots that having  components', function() {

            payload.startCallback(event, ui);
            var eachCallback = slotsSelector.each.calls[0].args[0];

            someIterationElementSelector.height.andReturn(0);

            var componentSelector = jasmine.createSpyObj("componentSelector", ["eq"]);
            componentSelector.length = 2;
            var component = jasmine.createSpyObj("component", ["height"]);
            component.height.andReturn("someHeight");
            componentSelector.eq.andReturn(component);
            someIterationElementSelector.find.andReturn(componentSelector);

            eachCallback.apply(someIterationElement);

            expect(someIterationElementSelector.addClass).toHaveBeenCalledWith('ySEDirtyHeight');
            expect(someIterationElementSelector.height).toHaveBeenCalledWith('someHeight');

        });

        it('startCallback will send event to close component menu', function() {
            payload.startCallback(event, ui);
            expect(systemEventService.sendSynchEvent).toHaveBeenCalledWith('ySEComponentMenuClose', {});
        });

        it('overCallback will set enable slot class for valid components and toogle ySEDnDImageRed/ySEDnDImageBlue css classes for slots', function() {

            var data = {
                contentSlotName: "bottomHeaderSlot",
                validComponentTypes: [
                    "componentType1",
                    "componentType2",
                    "componentType3"
                ]
            };

            var deferred = $q.defer();
            deferred.resolve(data);
            restService.get.andReturn(deferred.promise);

            payload.overCallback(event, ui);
            $rootScope.$digest();


            expect(restServiceFactory.get).toHaveBeenCalledWith(cmsWebservicesConstants.CONTENT_SLOT_TYPE_RESTRICTION);
            expect(restService.get).toHaveBeenCalledWith({
                pageUid: 'pageUid',
                slotUid: 'targetSlotId'
            });
            expect(cmsDragAndDropService._getElementChild).toHaveBeenCalledWith(ui.item, 0);
            expect(targetSelector.addClass).toHaveBeenCalledWith("over-slot-enabled");
            expect(iconBckgSelector.switchClass).toHaveBeenCalledWith('ySEDnDImageRed', 'ySEDnDImageBlue');

        });

        it('overCallback will set disable slot class for invalid components and toogle ySEDnDImageBlue/ySEDnDImageRed css classes for slots', function() {

            var data = {
                contentSlotName: "bottomHeaderSlot",
                validComponentTypes: [
                    "componentType2",
                    "componentType3"
                ]
            };

            var deferred = $q.defer();
            deferred.resolve(data);
            restService.get.andReturn(deferred.promise);

            payload.overCallback(event, ui);
            $rootScope.$digest();

            expect(restServiceFactory.get).toHaveBeenCalledWith(cmsWebservicesConstants.CONTENT_SLOT_TYPE_RESTRICTION);
            expect(restService.get).toHaveBeenCalledWith({
                pageUid: 'pageUid',
                slotUid: 'targetSlotId'
            });
            expect(cmsDragAndDropService._getElementChild).toHaveBeenCalledWith(ui.item, 0);
            expect(targetSelector.addClass).toHaveBeenCalledWith("over-slot-disabled");
            expect(iconBckgSelector.switchClass).toHaveBeenCalledWith('ySEDnDImageBlue', 'ySEDnDImageRed');

        });

        it('dropCallback will display an error message (timed alert) if the slot onto which the component is dropped is disabled', function() {

            var deferred = $q.defer();
            deferred.resolve('This item cannot be dropped to slot:targetId');
            $translate.andReturn(deferred.promise);

            var cancel = jasmine.createSpy('cancel');

            targetSelector.hasClass.andCallFake(function(cssSelector) {
                if (cssSelector === 'over-slot-disabled') {
                    return true;
                }

                if (cssSelector === 'over-slot-enabled') {
                    return false;
                }
            });

            payload.dropCallback(event, ui, cancel);
            $rootScope.$digest();

            expect(cancel).toHaveBeenCalled();
            expect(alertService.pushAlerts).toHaveBeenCalledWith([{
                successful: false,
                message: 'This item cannot be dropped to slot:targetId',
                closeable: true
            }]);
            expect(restService.update).not.toHaveBeenCalled();

            expect($translate).toHaveBeenCalledWith("drag.and.drop.not.valid.component.type", {
                componentUID: 'componentType1',
                slotUID: 'targetSlotId'
            });
        });

        it('dropCallback will display a success message (timed alert) if the slot onto which the component is dropped is enabled and API call is successful', function() {

            var deferred = $q.defer();
            deferred.resolve({});
            restService.update.andReturn(deferred.promise);

            var cancel = jasmine.createSpy('cancel');

            targetSelector.hasClass.andCallFake(function(cssSelector) {
                if (cssSelector === 'over-slot-disabled') {
                    return false;
                }

                if (cssSelector === 'over-slot-enabled') {
                    return true;
                }
            });

            cmsDragAndDropService.sourceSlotId = "someSourceSlotId";
            payload.dropCallback(event, ui, cancel);
            $rootScope.$digest();

            expect(restServiceFactory.get).toHaveBeenCalledWith(cmsWebservicesConstants.PAGES_CONTENT_SLOT_COMPONENT + '?pageId=:pageId&slotId=:currentSlotId&componentId=:componentId', 'componentId');

            expect(restService.update).toHaveBeenCalledWith({
                pageId: 'pageUid',
                currentSlotId: 'someSourceSlotId',
                componentId: 'componentType1',
                slotId: 'targetSlotId',
                position: 0
            });

            expect(cancel).not.toHaveBeenCalled();

        });


        it('dropCallback will cancel the move if the move API call is failing', function() {

            var deferred = $q.defer();
            deferred.reject({});
            restService.update.andReturn(deferred.promise);

            var cancel = jasmine.createSpy('cancel');

            targetSelector.hasClass.andCallFake(function(cssSelector) {
                if (cssSelector === 'over-slot-disabled') {
                    return false;
                }

                if (cssSelector === 'over-slot-enabled') {
                    return true;
                }
            });

            cmsDragAndDropService.sourceSlotId = "someSourceSlotId";
            payload.dropCallback(event, ui, cancel);
            $rootScope.$digest();

            expect(restServiceFactory.get).toHaveBeenCalledWith(cmsWebservicesConstants.PAGES_CONTENT_SLOT_COMPONENT + '?pageId=:pageId&slotId=:currentSlotId&componentId=:componentId', 'componentId');

            expect(restService.update).toHaveBeenCalledWith({
                pageId: 'pageUid',
                currentSlotId: 'someSourceSlotId',
                componentId: 'componentType1',
                slotId: 'targetSlotId',
                position: 0
            });

            expect(cancel).toHaveBeenCalled();

        });

        it('outCallback will remove the added classes once dragging of components is stopped', function() {

            payload.outCallback(event, ui);

            expect(helperSelector.removeClass.callCount).toBe(1);
            expect(targetSelector.removeClass.callCount).toBe(2);

            expect(helperSelector.removeClass).toHaveBeenCalledWith("over-active");
            expect(targetSelector.removeClass).toHaveBeenCalledWith("over-slot-enabled");
            expect(targetSelector.removeClass).toHaveBeenCalledWith("over-slot-disabled");

        });



        it('stopCallback will remove height modifications of targeted slots', function() {

            payload.stopCallback(event, ui);

            expect(cmsDragAndDropService._removeDynamicStyles).toHaveBeenCalled();

            expect(slotsSelector.removeClass).toHaveBeenCalledWith("ySEemptySlot");
            expect(ySEDirtyHeightSelector.removeClass).toHaveBeenCalledWith("ySEDirtyHeight");
            expect(ySEDirtyHeightSelector.height).toHaveBeenCalledWith(0);

        });

        it('dropCallback will go through proper steps to successfully add the component item to the page', function() {
            var targetComponent = {
                pageId: 'pageUid',
                componentId: 'id',
                componentType: 'componentType',
                slotId: 'targetSlotId',
                index: 0
            };

            var deferred = $q.defer();
            deferred.resolve({});

            var cancel = jasmine.createSpy('cancel');

            targetSelector.hasClass.andCallFake(function(cssSelector) {
                if (cssSelector === 'over-slot-disabled') {
                    return false;
                }

                if (cssSelector === 'over-slot-enabled') {
                    return true;
                }
            });

            componentService.addExistingComponent.andReturn(deferred.promise);
            componentSelector.attr.andCallFake(function(selector, test) {
                if (selector === 'data-smartedit-component-type') {
                    return targetComponent.componentType;
                } else if (selector === 'data-smartedit-component-id') {
                    return targetComponent.componentId;
                }
            });
            payload.dropCallback(event, ui, cancel);
            $rootScope.$digest();

            expect(componentService.addExistingComponent).toHaveBeenCalledWith(targetComponent.pageId, targetComponent.componentId, targetComponent.slotId, targetComponent.index);
            expect(renderService.renderComponent).toHaveBeenCalledWith(targetComponent.componentId, targetComponent.componentType);

            expect(cancel).not.toHaveBeenCalled();

        });

        it('dropCallback will go through proper steps to successfully add the component type to the page', function() {

            var targetComponent = {
                componentId: 'id',
                componentType: 'componentType',
                slotId: 'targetSlotId',
                index: 0,
                name: "name",
                pageId: 'pageUid'
            };


            var response = {
                "uid": targetComponent.componentId
            };

            var deferred = $q.defer();
            deferred.resolve(response);

            var cancel = jasmine.createSpy('cancel');

            targetSelector.hasClass.andCallFake(function(cssSelector) {
                if (cssSelector === 'over-slot-disabled') {
                    return false;
                }

                if (cssSelector === 'over-slot-enabled') {
                    return true;
                }
            });

            componentService.addNewComponent.andReturn(deferred.promise);

            editorModal.open.andReturn(deferred.promise);
            componentSelector.attr.andCallFake(function(selector, compId) {
                if (selector === 'data-smartedit-component-type') {
                    return targetComponent.componentType;
                } else if (selector === 'data-smartedit-component-id' && compId !== undefined) {
                    return targetComponent.componentId;
                }
            });

            payload.dropCallback(event, ui, cancel);
            $rootScope.$digest();

            expect(componentService.addNewComponent).toHaveBeenCalledWith(targetComponent.name, targetComponent.componentType, targetComponent.pageId, targetComponent.slotId, targetComponent.index);
            expect(editorModal.open).toHaveBeenCalledWith(targetComponent.componentType, targetComponent.componentId);
            expect(renderService.renderComponent).not.toHaveBeenCalled();

            expect(cancel).not.toHaveBeenCalled();

        });

        //TODO fix test
        xit('dropCallback will try to add the component type but modal is cancelled', function() {

            var targetComponent = {
                componentId: 'id',
                componentType: 'componentType',
                slotId: 'targetSlotId',
                index: 0,
                name: "name",
                pageId: 'pageUid'
            };


            var response = {
                "uid": targetComponent.componentId
            };

            var deferredSuccess = $q.defer();
            deferredSuccess.resolve(response);
            componentService.addNewComponent.andReturn(deferredSuccess.promise);

            var cancel = jasmine.createSpy('cancel');

            targetSelector.hasClass.andCallFake(function(cssSelector) {
                if (cssSelector === 'over-slot-disabled') {
                    return false;
                }

                if (cssSelector === 'over-slot-enabled') {
                    return true;
                }
            });

            var deferredReject = $q.defer();
            deferredReject.reject({});

            editorModal.open.andReturn(deferredReject.promise);

            componentSelector.attr.andCallFake(function(selector, compId) {
                if (selector === 'data-smartedit-component-type') {
                    return targetComponent.componentType;
                } else if (selector === 'data-smartedit-component-id' && compId !== undefined) {
                    return targetComponent.componentId;
                }
            });

            componentService.removeComponent.andReturn(deferredSuccess.promise);
            payload.dropCallback(event, ui, cancel);
            $rootScope.$digest();

            expect(componentService.addNewComponent).toHaveBeenCalledWith(targetComponent.name, targetComponent.componentType, targetComponent.pageId, targetComponent.slotId, targetComponent.index);
            expect(editorModal.open).toHaveBeenCalledWith(targetComponent.componentType, targetComponent.componentId);
            expect(componentService.removeComponent).toHaveBeenCalledWith(targetComponent.slotId, targetComponent.componentId);

            expect(cancel).toHaveBeenCalled();
        });

        it('dropCallback will try to add the component type but add new component fails', function() {

            var targetComponent = {
                componentId: 'id',
                componentType: 'componentType',
                slotId: 'targetSlotId',
                index: 0,
                name: "name",
                pageId: 'pageUid'
            };

            var response = {
                "uid": targetComponent.componentId,
                "data": {
                    errors: [{
                        message: 'this is an error'
                    }]
                }
            };

            spyOn(cmsDragAndDropService, '_generateErrorMessage').andCallFake(function(sourceId, targetId, errorObj) {
                return $q.when(errorObj);
            });

            var deferred = $q.defer();
            deferred.reject(response);
            componentService.addNewComponent.andReturn(deferred.promise);

            var cancel = jasmine.createSpy('cancel');

            targetSelector.hasClass.andCallFake(function(cssSelector) {
                if (cssSelector === 'over-slot-disabled') {
                    return false;
                }

                if (cssSelector === 'over-slot-enabled') {
                    return true;
                }
            });

            componentSelector.attr.andCallFake(function(selector, compId) {
                if (selector === 'data-smartedit-component-type') {
                    return targetComponent.componentType;
                }
            });

            payload.dropCallback(event, ui, cancel);
            $rootScope.$digest();

            expect(cmsDragAndDropService._generateErrorMessage).toHaveBeenCalledWith(undefined, targetComponent.slotId, response);
            expect(componentService.addNewComponent).toHaveBeenCalledWith(targetComponent.name, targetComponent.componentType, targetComponent.pageId, targetComponent.slotId, targetComponent.index);

            expect(cancel).toHaveBeenCalled();
        });

        it('dropCallback will try to add the component item but fails', function() {

            var targetComponent = {
                pageId: 'pageUid',
                componentId: 'id',
                componentType: 'componentType',
                slotId: 'targetSlotId',
                index: 0
            };

            var response = {
                "data": {
                    errors: [{
                        message: 'this is an error'
                    }]
                }
            };

            spyOn(cmsDragAndDropService, '_generateErrorMessage').andCallFake(function(sourceId, targetId, errorObj) {
                return $q.when(errorObj);
            });

            var deferred = $q.defer();
            deferred.reject(response);

            var cancel = jasmine.createSpy('cancel');

            targetSelector.hasClass.andCallFake(function(cssSelector) {
                if (cssSelector === 'over-slot-disabled') {
                    return false;
                }

                if (cssSelector === 'over-slot-enabled') {
                    return true;
                }
            });

            componentService.addExistingComponent.andReturn(deferred.promise);
            componentSelector.attr.andCallFake(function(selector, test) {
                if (selector === 'data-smartedit-component-type') {
                    return targetComponent.componentType;
                } else if (selector === 'data-smartedit-component-id') {
                    return targetComponent.componentId;
                }
            });
            payload.dropCallback(event, ui, cancel);
            $rootScope.$digest();

            expect(cmsDragAndDropService._generateErrorMessage).toHaveBeenCalledWith(targetComponent.componentId, targetComponent.slotId, response);
            expect(componentService.addExistingComponent).toHaveBeenCalledWith(targetComponent.pageId, targetComponent.componentId, targetComponent.slotId, targetComponent.index);
            expect(cancel).toHaveBeenCalled();

        });

    });

});
