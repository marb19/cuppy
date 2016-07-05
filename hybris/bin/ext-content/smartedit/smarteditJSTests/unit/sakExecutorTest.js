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
describe('test sakExecutor', function() {

    var sakExecutor, decorators, decoratorFilterService, compiler, compiled;
    var applicationManager = angular.module('ApplicationManager', []);

    beforeEach(customMatchers);

    it('sakExecutor.wrapDecorators fetches eligible decorators for given component type and compiles a stack of those decorators around the clone + the sakDecorator', function() {


        module('sakExecutorDecorator', function($provide) {

            smarteditComponentType = "smarteditComponentType";
            smarteditComponentId = "smarteditComponentId";
            updateCallback = function() {};
            cmsTicketId = "1234";

            decoratorFilterService = jasmine.createSpyObj('decoratorFilterService', ['storePrecompiledComponent', 'getDecoratorsForComponent', 'getCurrentPerspective']);
            decorators = ['decorator1', 'decorator2'];
            decoratorFilterService.getDecoratorsForComponent.andReturn(decorators);
            decoratorFilterService.getCurrentPerspective.andReturn({
                name: 'theperspective'
            });
            $provide.value('decoratorFilterService', decoratorFilterService);

            compiler = jasmine.createSpyObj('compiler', ['compile']);
            compiled = angular.element("<div>compiled</div>").html();
            compiler.compile.andReturn(compiled);
            $provide.value('$compile', function(template, transcludeFn) {
                return compiler.compile(template, transcludeFn);
            });

        });

        inject(function(_sakExecutor_) {
            sakExecutor = _sakExecutor_;
        });


        var pristineClone = angular.element("<div>initialContent</div>").html();
        var transcludeFn = function() {};

        var result = sakExecutor.wrapDecorators(pristineClone, transcludeFn, smarteditComponentId, smarteditComponentType);

        expect(compiled).toBe(result);
        expect(decoratorFilterService.getDecoratorsForComponent).toHaveBeenCalledWith(smarteditComponentType);
        expect(decoratorFilterService.getCurrentPerspective).toHaveBeenCalled();
        expect(compiler.compile).toHaveBeenCalledWith("<div data-perspective='theperspective' class='decorator2' data-active='active' data-smartedit-component-id='{{smarteditComponentId}}' data-smartedit-component-type='{{smarteditComponentType}}'><div data-perspective='theperspective' class='decorator1' data-active='active' data-smartedit-component-id='{{smarteditComponentId}}' data-smartedit-component-type='{{smarteditComponentType}}'><div data-ng-transclude></div></div></div>", transcludeFn);
    });


});
