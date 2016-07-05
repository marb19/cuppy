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
angular.module('contextualMenuServiceModule', ['functionsModule'])

/**
 * @ngdoc service
 * @name contextualMenuServiceModule.ContextualMenuService
 *
 * @description
 * The ContextualMenuService is used to add contextual menu items for each component.
 *
 * You add contextual menu items to SmartEdit using the Contextual MenuService of the Contextual Menu Service Module.
 * To add items to the contextual menu, you must call the addItems method of the contextualMenuService and pass a map of the component-type array of contextual menu items mapping.
 * The component type names are the keys in the mapping. The component name can be the full name of the component type, an ant-like wildcard (such as  *middle*Suffix), or a valid regex that includes or excludes a set of component types.
 *
 */
.factory('ContextualMenuService', function(hitch, uniqueArray, regExpFactory) {

    var ContextualMenuService = function() {
        this.contextualMenus = {};
    };

    var contextualMenusCallback = function(map, componentType) {
        this.contextualMenus[componentType] = uniqueArray((this.contextualMenus[componentType] || []), map[componentType]);
    };

    /**
     * @ngdoc method
     * @name contextualMenuServiceModule.ContextualMenuService#addItems
     * @methodOf contextualMenuServiceModule.ContextualMenuService
     *
     * @description
     * The method called to add contextual menu items to component types in the SmartEdit application.
     * The contextual menu items are then retrieved by the contextual menu decorator to wire the set of menu items to the specified component.
     *
     * Sample Usage:
     * <pre>
     * contextualMenuService.addItems({
     * '.*Component': [{
     *  i18nKey: 'CONTEXTUAL_MENU',
     *  condition: function(componentType, componentId) {
     * 	return componentId === 'ComponentType';
     * 	},
     *  callback: function(componentType, componentId) {
     * 	alert('callback for ' + componentType + "_" + componentId);
     * 	},
     *  displayClass: 'democlass',
     *  iconIdle: '.../icons/icon.png',
     *  iconNonIdle: '.../icons/icon.png',
     * }]
     * });
     * </pre>
     *
     * @param {Object} contextualMenuItemsMap An object containing list of componentType to contextual menu items mapping
     *
     * The object contains a list that maps component types to arrays of contextual menu items. The mapping is a key-value pair.
     * The key is the name of the component type, for example, Simple Responsive Banner Component, and the value is an array of contextual menu items, like add, edit, localize, etc.
     *
     * The name of the component type is the key in the mapping. The name can be the full name of the component type, an ant-like wildcard (such as *middle), or a vlide regex that includes or excludes a set of component types.
     * The value in the mapping is an array of contextual menu items to be activated for the component type match.
     *
     * The contextual menu items can have the following properties:
     * @param {String} contextualMenuItemsMap.i18nKey i18nKey Is the message key of the contextual menu item to be translated.
     * @param {Object} contextualMenuItemsMap.condition condition Is an optional entry that holds the condition required to activate the menu item based on component type and the component ID.
     * @param {Object} contextualMenuItemsMap.callback callback Is the action to be performed by clicking on the menu item.
     * @param {String} contextualMenuItemsMap.displayClass Contains the CSS classes used to style the contextual menu item
     * @param {String} contextualMenuItemsMap.iconIdle iconIdle Contains the location of the idle icon of the contextual menu item to be displayed.
     * @param {String} contextualMenuItemsMap.iconNonIdle iconNonIdle Contains the location of the non-idle icon of the contextual menu item to be displayed.
     * @param {String} contextualMenuItemsMap.smallIcon smallIcon Contains the location of the smaller version of the icon to be displayed when the menu item is part of the More... menu options.
     *
     */
    ContextualMenuService.prototype.addItems = function(map) {

        try {
            if (map !== undefined) {
                var componentTypes = Object.keys(map);
                componentTypes.forEach(hitch(this, contextualMenusCallback, map));
            }
        } catch (e) {
            $log.warn('No contextual menu definition found', e);
        }

    };

    /**
     * @ngdoc method
     * @name contextualMenuServiceModule.ContextualMenuService#getContextualMenuByType
     * @methodOf contextualMenuServiceModule.ContextualMenuService
     *
     * @description
     * Will return an array of contextual menu items for a specific component type.
     * For each key in the contextual menus' object, the method converts each component type into a valid regex using the regExpFactory of the function module and then compares it with the input componentType and, if matched, will add it to an array and returns the array.
     *
     * @param {String} componentType The type code of the selected component
     *
     * @returns {Array} An array of contextual menu items assigned to the type.
     *
     */
    ContextualMenuService.prototype.getContextualMenuByType = function(componentType) {
        var contextualMenuArray = [];
        if (this.contextualMenus) {
            for (var regexpKey in this.contextualMenus) {
                if (regExpFactory(regexpKey).test(componentType)) {
                    contextualMenuArray = uniqueArray(contextualMenuArray, this.contextualMenus[regexpKey]);
                }
            }
        }
        return contextualMenuArray;
    };

    /**
     * @ngdoc method
     * @name contextualMenuServiceModule.ContextualMenuService#getContextualMenuItems
     * @methodOf contextualMenuServiceModule.ContextualMenuService
     *
     * @description
     * Will return an object that contains a list of contextual menu items that are visible and those that are to be added to the More... options.
     *
     * For each component and display limit size, two arrays are generated.
     * One array contains the menu items that can be displayed and the other array contains the menu items that are available under the more menu items action.
     *
     * @param {String} componentType The type code of the selected component.
     * @param {String} componentId The ID of the selected component.
     * @param {Number} iLeftBtns The number of visible contextual menu items for a specified component.
     *
     * @returns {Object} An array of contextual menu items assigned to the component type.
     *
     * The returned object contains the following properties
     * - leftMenuItems : An array of menu items that can be displayed on the component.
     * - moreMenuItems : An array of menu items that are available under the more menu items action.
     *
     */
    ContextualMenuService.prototype.getContextualMenuItems = function(componentType, componentId, iLeftBtns) {
        var newMenuItems = [];
        var newMoreItems = [];
        var count = 0;
        var menuItems = this.getContextualMenuByType(componentType);

        for (var i = 0; i < menuItems.length; i++) {
            if (newMenuItems.length < iLeftBtns) {
                if (menuItems[i].condition === undefined || menuItems[i].condition(componentType, componentId) === true) {
                    newMenuItems.push(menuItems[i]);
                }
            } else {
                count = i;
                break;
            }
        }

        //contextualMenuItems.push(newMenuItems);

        if (count > 0) {
            for (var j = 0; j < menuItems.length; j++) {
                if ((j >= count) && (menuItems[j].condition === undefined || menuItems[j].condition(componentType, componentId) === true)) {
                    newMoreItems.push(menuItems[j]);
                }
            }
        }

        //contextualMenuItems.push(newMoreItems);

        return {
            'leftMenuItems': newMenuItems,
            'moreMenuItems': newMoreItems


        };

    };


    return ContextualMenuService;
})

/**
 * @ngdoc service
 * @name contextualMenuServiceModule.contextualMenuService
 *
 * @description
 * The contextual menu service factory creates an instance of the  {@link contextualMenuServiceModule.ContextualMenuService ContextualMenuService}
 * each time it is loaded for a component type and a component ID.
 *
 */
.factory('contextualMenuService', function(ContextualMenuService) {
    return new ContextualMenuService();
});
