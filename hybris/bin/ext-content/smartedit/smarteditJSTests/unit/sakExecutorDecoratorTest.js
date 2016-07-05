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
describe(
    'Unit integration test of sakExecutorDecorator directive',
    function() {
        var decorators, decoratorFilterService, sakExecutor;
        var $rootScope, parentScope, directiveScope, element, smarteditComponentType, smarteditComponentId;

        beforeEach(module('ui.bootstrap'));
        beforeEach(module('coretemplates'));
        beforeEach(module('PerspectiveModule', function($provide) {

            $provide.value('PersistedConfiguration', {
                getAppModuleNames: function() {
                    return [];
                }
            });

            decoratorFilterService = jasmine.createSpyObj('decoratorFilterService', ['storePrecompiledComponent', 'getDecoratorsForComponent', 'getCurrentPerspective']);
            decorators = ['decorator1', 'decorator2'];
            decoratorFilterService.getDecoratorsForComponent.andReturn(decorators);
            decoratorFilterService.getCurrentPerspective.andReturn({
                name: 'testPerspective'
            });
            $provide.value('decoratorFilterService', decoratorFilterService);
        }));

        beforeEach(module('sakExecutorDecorator'));
        beforeEach(customMatchers);

        // Store references to $rootScope and $compile so they are available to all tests in this describe block
        beforeEach(inject(function($compile, _$rootScope_, $q, _sakExecutor_) {

            jasmine.Clock.useMock();
            sakExecutor = _sakExecutor_;
            spyOn(sakExecutor, 'setPending').andCallThrough();
            spyOn(sakExecutor, 'removePending').andCallThrough();

            smarteditComponentType = "Paragraph";
            smarteditComponentId = "theId";

            $rootScope = _$rootScope_;
            parentScope = $rootScope.$new();
            parentScope.active = false;
            directiveScope = parentScope.$new();

            element = angular.element("<div class=\"smartEditComponent\" data-smartedit-component-id=\"" + smarteditComponentId + "\" data-smartedit-component-type=\"" + smarteditComponentType + "\">initialContent</div>");
            $compile(element)(directiveScope);

            // fire all the watches, so the scope expressions will be evaluated
            $rootScope.$digest();

            expect(element.scope()).toBe(directiveScope);
        }));


        it('sakExecutor stacks decorators in this order : decorator2, decorator1', function() {

            expect(element.find('> div.decorator2').length).toBe(1);
            expect(element.find('> div.decorator2 > div.decorator1').length).toBe(1);

            expect(sakExecutor.setPending).toHaveBeenCalledWith('Paragraph', 'theId');
            jasmine.Clock.tick(0);
            // expect(sakExecutor.removePending).toHaveBeenCalledWith();

        });


    });
