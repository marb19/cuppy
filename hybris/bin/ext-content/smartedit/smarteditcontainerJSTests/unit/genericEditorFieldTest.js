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
describe('directive:genericEditorField', function() {

    var $compile, $templateCache, $sanitize, $sce;
    var scope, compiledElement, elementScope;

    beforeEach(module('ngSanitize'));
    beforeEach(module('genericEditorFieldModule'));
    beforeEach(
        inject(function(_$compile_, _$rootScope_, _$templateCache_, _$sanitize_, _$sce_) {
            $compile = _$compile_;
            $templateCache = _$templateCache_;
            $sanitize = _$sanitize_;
            $sce = _$sce_;
            scope = _$rootScope_.$new();
        })
    );


    beforeEach(function() {
        $templateCache.put('web/common/services/genericEditor/genericEditorFieldTemplate.html', '<div></div>');
        var element = angular.element('<generic-editor-field></generic-editor-field>');
        compiledElement = $compile(element)(scope);
        scope.$digest();
        elementScope = compiledElement.isolateScope();
    });


    it("reassignUserCheck WILL set requiresUserCheck as true on field with javascript snippet WHEN sanitized content does not match unsanitized content", function() {
        elementScope.qualifier = 'en';
        elementScope.model = {
            en: '<div><script>alert(/"I am a snippet/");</script></div>'
        };
        elementScope.field = {};
        elementScope.reassignUserCheck();
        expect(elementScope.field.requiresUserCheck).toBe(true);
    });

    it("reassignUserCheck WILL set requiresUserCheck as true on field WHEN sanitized content does not match unsanitized content", function() {
        elementScope.qualifier = 'en';

        elementScope.model = {
            en: '\"http://\"'
        };
        elementScope.field = {};
        elementScope.reassignUserCheck();
        expect(elementScope.field.requiresUserCheck).toBe(true);
    });

    it("reassignUserCheck WILL not set requiresUserCheck on field WHEN  sanitized content matches unsanitized content.", function() {

        elementScope.qualifier = 'en';
        elementScope.model = {
            en: '<p>Valid Html</p>'
        };
        elementScope.field = {};
        elementScope.reassignUserCheck();
        expect(elementScope.field.requiresUserCheck).toBe(false);
    });

    it("reassignUserCheck WILL not set requiresUserCheck on field WHEN there is no content", function() {
        elementScope.model = {};
        elementScope.field = {};

        elementScope.reassignUserCheck();
        expect(elementScope.field.requiresUserCheck).not.toBeDefined();
    });

    it("reassignUserCheck WILL not set requiresUserCheck on field WHEN the model is not defined", function() {
        elementScope.field = {};

        elementScope.reassignUserCheck();
        expect(elementScope.field.requiresUserCheck).not.toBeDefined();
    });
});
