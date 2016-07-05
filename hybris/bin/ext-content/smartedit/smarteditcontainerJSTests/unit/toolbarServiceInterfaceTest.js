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
describe('test outer toolbarServiceInterface Module', function() {

    var $rootScope, ToolbarServiceInterface;

    beforeEach(customMatchers);

    beforeEach(module('toolbarInterfaceModule'));
    beforeEach(inject(function(_$rootScope_, _ToolbarServiceInterface_) {
        $rootScope = _$rootScope_;
        ToolbarServiceInterface = _ToolbarServiceInterface_;
    }));

    it('ToolbarServiceInterface declares the expected set of empty functions', function() {


        expect(ToolbarServiceInterface.prototype.addAliases).toBeEmptyFunction();
        expect(ToolbarServiceInterface.prototype.addItemsStyling).toBeEmptyFunction();
        expect(ToolbarServiceInterface.prototype.triggerActionOnInner).toBeEmptyFunction();
    });

    it('ToolbarServiceInterface.addItems converts actions into aliases (key-callback mapping of actions) before appending them by means of addAliases', function() {

        var toolbarService = new ToolbarServiceInterface();
        toolbarService.actions = {};
        toolbarService.aliases = [];

        var currentKeyNumber = 1;
        spyOn(toolbarService, '_getUniqueIdentifier').andCallFake(function() {
            return "key" + currentKeyNumber++;
        });
        spyOn(toolbarService, 'addAliases').andCallThrough();
        spyOn(toolbarService, 'getAliases').andCallThrough();

        var callback1 = function() {};
        var callback2 = function() {};

        expect(toolbarService.getAliases()).toEqualData([]);

        // Execution
        toolbarService.addItems([{
            i18nKey: 'i18nKey1',
            callback: callback1,
            icons: 'icons1',
            type: 'type1',
            include: 'include1'
        }]);

        var actionsAfterFirstCall = toolbarService.getItems();

        toolbarService.addItems([{
            i18nKey: 'i18nKey2',
            callback: callback2,
            icons: 'icons2',
            type: 'type2',
            include: 'include2'
        }]);

        var actionsAfterSecondCall = toolbarService.getItems();

        // Tests
        expect(toolbarService.addAliases.calls[0].args[0]).toEqualData([{
            key: 'key1',
            name: 'i18nKey1',
            icons: 'icons1',
            type: 'type1',
            include: 'include1'
        }]);


        expect(toolbarService.getItems()).toEqualData({
            'key1': callback1,
        });

        expect(toolbarService.addAliases.calls[1].args[0]).toEqualData([{
            key: 'key2',
            name: 'i18nKey2',
            icons: 'icons2',
            type: 'type2',
            include: 'include2'
        }]);


        expect(toolbarService.getItems()).toEqualData({
            'key1': callback1,
            'key2': callback2
        });
    });

});
