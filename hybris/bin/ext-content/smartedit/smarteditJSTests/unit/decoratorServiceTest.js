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
describe('test decoratorServiceModule', function() {

    var decoratorService;

    beforeEach(customMatchers);


    beforeEach(module('decoratorServiceModule'));
    beforeEach(inject(function(_decoratorService_) {
        decoratorService = _decoratorService_;
    }));

    it('getDecoratorsForComponent will retain a unique set of decorators for a given type', function() {

        decoratorService.addMappings({
            'type1': ['decorator1', 'decorator2'],
            'type2': ['decorator0'],
        });
        decoratorService.addMappings({
            'type1': ['decorator2', 'decorator3'],
        });

        expect(decoratorService.getDecoratorsForComponent('type1')).toEqualData(['decorator1', 'decorator2', 'decorator3']);
    });


    it('getDecoratorsForComponent will retain a unique set of decorators from all matching regexps', function() {

        decoratorService.addMappings({
            '*Suffix': ['decorator1', 'decorator2'],
            '.*Suffix': ['decorator2', 'decorator3'],
            'TypeSuffix': ['decorator3', 'decorator4'],
            '^((?!Middle).)*$': ['decorator4', 'decorator5'],
            'PrefixType': ['decorator5', 'decorator6'],
        });

        expect(decoratorService.getDecoratorsForComponent('TypeSuffix')).toEqualData(['decorator1', 'decorator2', 'decorator3', 'decorator4', 'decorator5']);

        expect(decoratorService.getDecoratorsForComponent('TypeSuffixes')).toEqualData(['decorator2', 'decorator3', 'decorator4', 'decorator5']);

        expect(decoratorService.getDecoratorsForComponent('MiddleTypeSuffix')).toEqualData(['decorator1', 'decorator2', 'decorator3']);
    });

});
