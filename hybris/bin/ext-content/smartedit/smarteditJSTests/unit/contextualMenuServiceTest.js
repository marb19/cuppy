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
describe('test contextualMenuServiceModule', function() {

    var contextualMenuService;

    beforeEach(customMatchers);

    beforeEach(module('contextualMenuServiceModule'));
    beforeEach(inject(function(_contextualMenuService_) {
        contextualMenuService = _contextualMenuService_;
    }));

    it('getContextualMenuByType will return an unique array of contextual menu items when componenttype is given', function() {

        contextualMenuService.addItems({
            'type1': ['contextualMenuItem1', 'contextualMenuItem2'],
            'type2': ['contextualMenuItem3', 'contextualMenuItem4'],
        });

        contextualMenuService.addItems({
            'type1': ['contextualMenuItem2', 'contextualMenuItem3']
        });

        expect(contextualMenuService.getContextualMenuByType('type1')).toEqualData(['contextualMenuItem1', 'contextualMenuItem2', 'contextualMenuItem3']);
        expect(contextualMenuService.getContextualMenuByType('type2')).toEqualData(['contextualMenuItem3', 'contextualMenuItem4']);

    });

    it('getContextualMenuByType will return an unique array of contextual menu items when it matches the regexps', function() {


        contextualMenuService.addItems({
            '*Suffix': ['element1', 'element2'],
            '.*Suffix': ['element2', 'element3'],
            'TypeSuffix': ['element3', 'element4'],
            '^((?!Middle).)*$': ['element4', 'element5'],
            'PrefixType': ['element5', 'element6']
        });


        expect(contextualMenuService.getContextualMenuByType('TypeSuffix')).toEqualData(['element1', 'element2', 'element3', 'element4', 'element5']);

        expect(contextualMenuService.getContextualMenuByType('TypeSuffixes')).toEqualData(['element2', 'element3', 'element4', 'element5']);

        expect(contextualMenuService.getContextualMenuByType('MiddleTypeSuffix')).toEqualData(['element1', 'element2', 'element3']);


    });

    describe('getContextualMenuItems will return an array-of-array of contextual menu items based on condition', function() {

        it('will return those menu items which satisfy the condition or those that have no condition set (default condition to be true)', function() {

            contextualMenuService.addItems({
                'ComponentType1': [{
                    i18nKey: 'ICON1',
                    icon: 'icon1.png'
                }, {
                    i18nKey: 'ICON2',
                    condition: function(componentType, componentId) {
                        return componentId === 'ComponentId2';
                    },
                    icon: 'icon2.png'
                }, {
                    i18nKey: 'ICON3',
                    condition: function(componentType, componentId) {
                        return true;
                    },
                    icon: 'icon3.png'
                }, {
                    i18nKey: 'ICON4',
                    condition: function(componentType, componentId) {
                        return false;
                    },
                    icon: 'icon4.png'
                }, {
                    i18nKey: 'ICON5',
                    condition: function(componentType, componentId) {
                        return componentId === 'ComponentId1';
                    },
                    icon: 'icon5.png'
                }, {
                    i18nKey: 'ICON6',
                    condition: function(componentType, componentId) {
                        return componentType === 'ComponentType1';
                    },
                    icon: 'icon6.png'
                }]
            });


            expect(contextualMenuService.getContextualMenuItems('ComponentType1', 'ComponentId1', 3)).toEqualData({
                'leftMenuItems': [{
                    i18nKey: 'ICON1',
                    icon: 'icon1.png'
                }, {
                    i18nKey: 'ICON3',
                    condition: function(componentType, componentId) {
                        return true;
                    },
                    icon: 'icon3.png'
                }, {
                    i18nKey: 'ICON5',
                    condition: function(componentType, componentId) {
                        return componentId === 'ComponentId1';
                    },
                    icon: 'icon5.png'
                }],
                'moreMenuItems': [{
                    i18nKey: 'ICON6',
                    condition: function(componentType, componentId) {
                        return componentType === 'ComponentType1';
                    },
                    icon: 'icon6.png'
                }]
            });

        });

        it('for iLeftBtns= 3, will set a maximum of 3 menu items to the left (1st element in the array) and the rest to the right (2nd element in the array)', function() {


            contextualMenuService.addItems({
                'ComponentType1': [{
                    i18nKey: 'ICON1',
                    icon: 'icon1.png'
                }, {
                    i18nKey: 'ICON2',
                    condition: function(componentType, componentId) {
                        return componentId === 'ComponentId2';
                    },
                    icon: 'icon2.png'
                }, {
                    i18nKey: 'ICON3',
                    condition: function(componentType, componentId) {
                        return true;
                    },
                    icon: 'icon3.png'
                }, {
                    i18nKey: 'ICON4',
                    condition: function(componentType, componentId) {
                        return false;
                    },
                    icon: 'icon4.png'
                }, {
                    i18nKey: 'ICON5',
                    condition: function(componentType, componentId) {
                        return componentId === 'ComponentId1';
                    },
                    icon: 'icon5.png'
                }, {
                    i18nKey: 'ICON6',
                    condition: function(componentType, componentId) {
                        return componentType === 'ComponentType1';
                    },
                    icon: 'icon6.png'
                }]
            });

            expect(contextualMenuService.getContextualMenuItems('ComponentType1', 'ComponentId1', 3).leftMenuItems).toEqualData([{
                i18nKey: 'ICON1',
                icon: 'icon1.png'
            }, {
                i18nKey: 'ICON3',
                condition: function(componentType, componentId) {
                    return true;
                },
                icon: 'icon3.png'
            }, {
                i18nKey: 'ICON5',
                condition: function(componentType, componentId) {
                    return componentId === 'ComponentId1';
                },
                icon: 'icon5.png'
            }]);

            expect(contextualMenuService.getContextualMenuItems('ComponentType1', 'ComponentId1', 3).moreMenuItems).toEqualData([{
                i18nKey: 'ICON6',
                condition: function(componentType, componentId) {
                    return componentType === 'ComponentType1';
                },
                icon: 'icon6.png'
            }]);


        });

    });


});
