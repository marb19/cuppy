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
describe('test renderService', function() {

    var ACC;
    var $q, $rootScope;
    var renderService;
    var acceleratorService;
    var alertService;

    beforeEach(customMatchers);

    beforeEach(module('renderServiceModule'));
    beforeEach(inject(function(_renderService_, _$q_, _$rootScope_, _alertService_) {
        renderService = _renderService_;
        $q = _$q_;
        $rootScope = _$rootScope_;
        alertService = _alertService_;
    }));

    beforeEach(function() {
        acceleratorService = jasmine.createSpyObj('acceleratorService', ['loadComponent']);
        alertService = jasmine.createSpyObj('alertService', ['pushAlerts']);

        spyOn(renderService, "_getAcceleratorCMSService").andReturn(acceleratorService);
        spyOn(renderService, "_compile").andCallThrough();
    });

    it('check if RenderService.renderRemoval correctly goes through necessary steps to remove a component', function() {
        // Arrange
        var componentId = "component1";
        var componentType = "componentType1";
        var jqMockForRemove = jasmine.createSpyObj("Mock object to be used to remove on", ["remove"]);

        spyOn(renderService, "_getComponent").andReturn(jqMockForRemove);
        jqMockForRemove.remove.andReturn();

        // Act
        resultContent = renderService.renderRemoval(componentId, componentType);

        // Assert
        expect(renderService._getComponent).toHaveBeenCalledWith('[data-smartedit-component-type=' + componentType + '][data-smartedit-component-id=' + componentId + ']');
        expect(jqMockForRemove.remove).toHaveBeenCalled();
    });

    it('check if RenderService.renderRemovalBySlotAndComponent correctly goes through necessary steps to remove a component', function() {
        // Arrange
        var componentId = "component1";
        var slotId = "slot1";
        var jqMockForFind = jasmine.createSpyObj("Mock object to be used to find on", ["find"]);
        var jqMockForRemove = jasmine.createSpyObj("Mock object to be used to remove on", ["remove"]);

        spyOn(renderService, "_getComponent").andReturn(jqMockForFind);
        jqMockForFind.find.andReturn(jqMockForRemove);
        jqMockForRemove.remove.andReturn();

        // Act
        resultContent = renderService.renderRemovalBySlotAndComponent(slotId, componentId);

        // Assert
        expect(renderService._getComponent).toHaveBeenCalledWith('[data-smartedit-component-id=' + slotId + ']');
        expect(jqMockForFind.find).toHaveBeenCalledWith('[data-smartedit-component-id=' + componentId + ']');
        expect(jqMockForRemove.remove).toHaveBeenCalled();
    });

    it('RenderService._replaceCustomContent correctly replaces content', function() {
        // Arrange
        var targetSelector = "someSelector";
        var originalContent = "<div>OriginalDiv</div>";
        var customContent = "<div>CustomContent</div>";
        var targetComponent = $(originalContent);

        spyOn(renderService, "_getComponent").andReturn(targetComponent);

        // Act
        renderService._replaceCustomContent(customContent, targetSelector);

        // Assert
        expect(renderService._getComponent).toHaveBeenCalledWith(targetSelector);
        expect(targetComponent.html()).toEqual(customContent);
    });

    it('RenderService correctly calls the accelerator CMS API and resolves the promise if the call was successful', function() {
        // Arrange
        var isResolved;
        var componentId = "someComponentId";
        var componentType = "someComponentType";
        var target = ".smartEditComponent[data-smartedit-component-id='someComponentId'][data-smartedit-component-type=someComponentType]";
        spyOn(renderService, "_getComponent").andReturn($("<div>Somediv</div>"));

        acceleratorService.loadComponent.andCallFake(function(componentId, componentType, target, onSuccess) {
            onSuccess('Any Result!');
        });

        // Act
        var renderPromise = renderService.renderComponent(componentId, componentType);
        renderPromise.then(
            function() {
                isResolved = true;
            },
            function() {
                isResolved = false;
            }
        );
        $rootScope.$digest(); // This is needed. Otherwise the promise is never resolved.

        // Assert
        expect(isResolved).toBe(true);
        expect(acceleratorService.loadComponent).toHaveBeenCalledWith(componentId, componentType, target, jasmine.any(Function), jasmine.any(Function));
        expect(renderService._compile).toHaveBeenCalled();
    });

    it('RenderService correctly calls the accelerator CMS API and rejects the promise if the call was not successful', function() {
        // Arrange
        var error;
        var isResolved;
        var componentId = "someComponentId";
        var componentType = "someComponentType";
        var externalError = "Any Error Result!";
        var target = ".smartEditComponent[data-smartedit-component-id='someComponentId'][data-smartedit-component-type=someComponentType]";
        spyOn(renderService, "_getComponent").andReturn($("<div>Somediv</div>"));

        acceleratorService.loadComponent.andCallFake(function(componentId, componentType, target, onSuccess, onFail) {
            onFail(externalError);
        });

        // Act
        var renderPromise = renderService.renderComponent(componentId, componentType);
        renderPromise.then(
            function() {
                isResolved = true;
            },
            function(e) {
                isResolved = false;
                error = e;
            }
        );
        $rootScope.$digest(); // This is needed. Otherwise the promise is never resolved.

        // Assert
        expect(isResolved).not.toBe(undefined);
        expect(isResolved).toBe(false);
        expect(error.message).toBe("external.content.could.not.be.loaded");
        expect(acceleratorService.loadComponent).toHaveBeenCalledWith(componentId, componentType, target, jasmine.any(Function), jasmine.any(Function));
        expect(renderService._compile).not.toHaveBeenCalled();
    });

    it("RenderService renders custom content when customContent is present", function() {
        // Arrange
        var isResolved;
        var originalContent = "<div>Somediv</div>";
        var customContent = "<div>CustomContent</div>";
        spyOn(renderService, "_renderExternalContent");
        spyOn(renderService, "_replaceCustomContent");
        spyOn(renderService, "_getComponent").andReturn($(originalContent));

        // Act
        var renderPromise = renderService.renderComponent("componentId", "componentType", customContent);
        renderPromise.then(
            function() {
                isResolved = true;
            },
            function(error) {
                isResolved = false;
            }
        );
        $rootScope.$digest(); // This is needed. Otherwise the promise is never resolved.

        // Assert
        expect(isResolved).toBe(true);
        expect(renderService._renderExternalContent).not.toHaveBeenCalled();
        expect(renderService._replaceCustomContent).toHaveBeenCalled();
        expect(renderService._compile).toHaveBeenCalled();
    });

    it('RenderService rejects the promise if no target is found', function() {
        // Arrange
        var isResolved;
        var error;
        spyOn(renderService, "_renderExternalContent");
        spyOn(renderService, "_replaceCustomContent");
        spyOn(renderService, "_getComponent").andReturn(null);

        // Act
        var renderPromise = renderService.renderComponent("componentId", "componentType", "");
        renderPromise.then(
            function() {
                isResolved = true;
            },
            function(e) {
                isResolved = false;
                error = e;
            }
        );
        $rootScope.$digest(); // This is needed. Otherwise the promise is never resolved.

        // Assert
        expect(isResolved).toBe(false);
        expect(error.message).toBe("renderService.renderComponent.noTargetComponentFound");
        expect(acceleratorService.loadComponent).not.toHaveBeenCalled();
        expect(renderService._compile).not.toHaveBeenCalled();
    });

    it('RenderService rejects the promise if multiple targets are found', function() {
        // Arrange
        var isResolved;
        var error;
        spyOn(renderService, "_renderExternalContent");
        spyOn(renderService, "_replaceCustomContent");
        spyOn(renderService, "_getComponent").andReturn(["some item1", "some item2"]);

        // Act
        var renderPromise = renderService.renderComponent("componentId", "componentType", "");
        renderPromise.then(
            function() {
                isResolved = true;
            },
            function(e) {
                isResolved = false;
                error = e;
            }
        );
        $rootScope.$digest(); // This is needed. Otherwise the promise is never resolved.

        // Assert
        expect(isResolved).toBe(false);
        expect(error.message).toBe("renderService.renderComponent.multipleTargetComponentsFound");
        expect(renderService._renderExternalContent).not.toHaveBeenCalled();
        expect(renderService._replaceCustomContent).not.toHaveBeenCalled();
        expect(renderService._compile).not.toHaveBeenCalled();
    });

    it('RenderService rejects the promise if the provided componentId is blank', function() {
        // Arrange
        var isResolved;
        var error;
        spyOn(renderService, "_renderExternalContent");
        spyOn(renderService, "_replaceCustomContent");

        // Act
        var renderPromise = renderService.renderComponent(null, "componentType");
        renderPromise.then(
            function() {
                isResolved = true;
            },
            function(e) {
                isResolved = false;
                error = e;
            }
        );
        $rootScope.$digest(); // This is needed. Otherwise the promise is never resolved.

        // Assert
        expect(isResolved).toBe(false);
        expect(error.message).toBe("renderService.renderComponent.invalid.componentId");
        expect(renderService._renderExternalContent).not.toHaveBeenCalled();
        expect(renderService._replaceCustomContent).not.toHaveBeenCalled();
        expect(renderService._compile).not.toHaveBeenCalled();
    });

    it('RenderService rejects the promise if the provided componentType is blank', function() {
        // Arrange
        var isResolved;
        var error;
        spyOn(renderService, "_renderExternalContent");
        spyOn(renderService, "_replaceCustomContent");

        // Act
        var renderPromise = renderService.renderComponent("componentId", null);
        renderPromise.then(
            function() {
                isResolved = true;
            },
            function(e) {
                isResolved = false;
                error = e;
            }
        );
        $rootScope.$digest(); // This is needed. Otherwise the promise is never resolved.

        // Assert
        // Assert
        expect(isResolved).toBe(false);
        expect(error.message).toBe("renderService.renderComponent.invalid.componentType");
        expect(renderService._renderExternalContent).not.toHaveBeenCalled();
        expect(renderService._replaceCustomContent).not.toHaveBeenCalled();
        expect(renderService._compile).not.toHaveBeenCalled();
    });
});
