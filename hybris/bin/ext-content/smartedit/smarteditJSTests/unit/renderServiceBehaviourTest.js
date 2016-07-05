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

    beforeEach(customMatchers);

    beforeEach(module('renderServiceModule'));
    beforeEach(module('gatewayProxyModule', function($provide) {

        gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['initForService']);
        $provide.value('gatewayProxy', gatewayProxy);
    }));
    beforeEach(inject(function(_renderService_, _$q_, _$rootScope_) {
        renderService = _renderService_;
        $q = _$q_;
        $rootScope = _$rootScope_;
    }));

    beforeEach(function() {
        acceleratorService = jasmine.createSpyObj('acceleratorService', ['loadComponent']);

        spyOn(renderService, "_getAcceleratorCMSService").andReturn(acceleratorService);
        spyOn(renderService, "_compile").andCallThrough();
    });
    it('check if RenderService.renderRemovalBySlotAndComponent correctly removes a component from DOM', function() {
        // Arrange
        var originalContent = '<div data-smartedit-component-id="slot1" data-smartedit-component-type="ContentSlot"><div data-smartedit-component-id="component1"></div></div>';
        var componentId = "component1";
        var slotId = "slot1";
        var expectedContent = '<div data-smartedit-component-id="slot1" data-smartedit-component-type="ContentSlot"></div>';
        var testContent = $(originalContent);
        var resultContent = "";

        spyOn(renderService, "_getComponent").andReturn(testContent);

        // Act
        resultContent = renderService.renderRemovalBySlotAndComponent(slotId, componentId);

        // Assert
        expect(renderService._getComponent).toHaveBeenCalled();
        expect(resultContent.end().prop('outerHTML')).toEqual(expectedContent);
    });
});
