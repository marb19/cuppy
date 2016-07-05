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
describe("Editor Enabler Service: ", function() {

    // Mocks
    var contextualMenuService;
    var systemEventService;
    var editorModalService;
    var $q;
    var $rootScope;

    // Service Under Test
    var editorEnablerService;

    // Test Values
    var COMPONENT_ID = "mycomponent";
    var COMPONENT_TYPE = "mytype";

    beforeEach(function() {
        angular.module("coretemplates", []);
        angular.module('gatewayFactoryModule', []);
        angular.module('contextualMenuServiceModule', []);
        angular.module('eventServiceModule', []);
        angular.module('editorModalServiceModule', []);
    });

    // Set-up Matchers
    beforeEach(customMatchers);

    // Mock Dependencies
    beforeEach(function() {

        module("contextualMenuServiceModule", function($provide) {
            contextualMenuService = jasmine.createSpyObj("contextualMenuService", ["addItems", "getContextualMenuByType", "getContextualMenuItems"]);
            $provide.value("contextualMenuService", contextualMenuService);
        });


        module('eventServiceModule', function($provide) {
            systemEventService = jasmine.createSpyObj('systemEventService', ['sendAsynchEvent', 'registerEventHandler', 'unRegisterEventHandler']);
            $provide.value('systemEventService', systemEventService);
        });


        module('editorModalServiceModule', function($provide) {
            editorModalService = {
                open: function() {}
            };
            $provide.value('editorModalService', editorModalService);
        });
    });

    // Set-up Module Under Test
    beforeEach(function() {
        module("editorEnablerServiceModule");
        inject(function(_editorEnablerService_, _$q_, _$rootScope_) {
            editorEnablerService = _editorEnablerService_;
            $q = _$q_;
            $rootScope = _$rootScope_;
        });
    });

    // Tests
    it("enableForComponent will add the Edit button to the contextual menu for a specified component type", function() {
        // Act
        editorEnablerService.enableForComponent("SimpleResponsiveBannerComponent");

        // Assert
        expect(contextualMenuService.addItems).toHaveBeenCalledWith({
            "SimpleResponsiveBannerComponent": [{
                displayClass: editorEnablerService._editDisplayClass,
                i18nKey: editorEnablerService._editI18nKey,
                iconIdle: editorEnablerService._editButtonIconIdle,
                iconNonIdle: editorEnablerService._editButtonIconNonIdle,
                smallIcon: editorEnablerService._editButtonSmallIcon,
                callback: editorEnablerService._editButtonCallback
            }]
        });
    });

    it('_editButtonCallback delegates to the editor modal service', function() {
        //// Arrange
        spyOn(editorModalService, 'open').andCallFake(function() {
            var deferred = $q.defer();
            deferred.resolve();
            return deferred.promise;
        });

        // Act
        editorEnablerService._editButtonCallback(COMPONENT_TYPE, COMPONENT_ID);
        $rootScope.$digest();

        // Assert
        expect(editorModalService.open).toHaveBeenCalledWith(COMPONENT_TYPE, COMPONENT_ID);
    });

});
