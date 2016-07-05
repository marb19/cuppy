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
describe('test outer toolbarService Module', function() {

    var $rootScope, $q, $log, gatewayProxy, toolbarServiceFactory;

    beforeEach(customMatchers);

    beforeEach(module('gatewayFactoryModule', function($provide) {
        gatewayFactory = jasmine.createSpyObj('gatewayFactory', ['initListener']);
        $provide.value('gatewayFactory', gatewayFactory);
    }));

    beforeEach(module('gatewayProxyModule', function($provide) {

        gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['initForService']);
        $provide.value('gatewayProxy', gatewayProxy);
    }));

    beforeEach(module('toolbarModule'));
    beforeEach(inject(function(_$rootScope_, _$q_, _$log_, _toolbarServiceFactory_) {
        $rootScope = _$rootScope_;
        $q = _$q_;
        $log = _$log_;
        toolbarServiceFactory = _toolbarServiceFactory_;
    }));

    it('factory called twice on the same toolbar name returns the same instance', function() {
        expect(toolbarServiceFactory.getToolbarService('toolBar1')).toBe(toolbarServiceFactory.getToolbarService('toolBar1'));
    });

    it('factory called twice on different toolbar names returns different instances', function() {
        expect(toolbarServiceFactory.getToolbarService('toolBar1')).not.toBe(toolbarServiceFactory.getToolbarService('toolBar2'));
    });

    it('on first acquisition of a new ToolbarServiceInstance, it is registered with the gateway proxy', function() {
        var toolbarService = toolbarServiceFactory.getToolbarService('toolBar1');
        expect(gatewayProxy.initForService).toHaveBeenCalledWith(toolbarService, ["addAliases", "triggerActionOnInner"]);
    });

    it('on change of aliases in setAliases, the onAliasChange callback is triggered', function() {
        var toolbarService = toolbarServiceFactory.getToolbarService('toolBar1');
        var onAliasesChange = jasmine.createSpy('onAliasesChange');

        toolbarService.setOnAliasesChange(onAliasesChange);
        toolbarService.addItems([{
            i18nKey: 'i18nKey1',
            callback: function() {}
        }]);

        expect(onAliasesChange).toHaveBeenCalled();
    });

    it('triggerAction triggers the associated action for the given key if it exists on the outer toolbar', function() {
        var toolbarService = toolbarServiceFactory.getToolbarService('toolBar1');
        var someAction = jasmine.createSpy('someAction');
        spyOn(toolbarService, '_getUniqueIdentifier').andReturn('key1');
        spyOn(toolbarService, 'triggerActionOnInner');

        toolbarService.addItems([{
            i8nKey: 'i18nKey1',
            callback: someAction
        }]);

        toolbarService.triggerAction({
            key: 'key1',
            name: 'i18nKey1'
        });

        expect(someAction).toHaveBeenCalled();
        expect(toolbarService.triggerActionOnInner).not.toHaveBeenCalled();
    });

    it('triggerAction dispatches the associated action for the given key on the inner toolbar if it does not exist on the outer toolbar', function() {
        var toolbarService = toolbarServiceFactory.getToolbarService('toolBar1');
        var someAction = jasmine.createSpy('someAction');
        spyOn(toolbarService, '_getUniqueIdentifier').andReturn('key2');
        spyOn(toolbarService, 'triggerActionOnInner');

        toolbarService.addItems([{
            i8nKey: 'i18nKey2',
            callback: someAction
        }]);

        toolbarService.triggerAction({
            key: 'key1',
            name: 'i18nKey1'
        });

        expect(someAction).not.toHaveBeenCalled();
        expect(toolbarService.triggerActionOnInner).toHaveBeenCalledWith({
            key: 'key1',
            name: 'i18nKey1'
        });
    });

    it('triggerActionOnInner is an empty function', function() {
        var toolbarService = toolbarServiceFactory.getToolbarService('toolBar1');
        expect(toolbarService.triggerActionOnInner).toBeEmptyFunction();
    });

});
