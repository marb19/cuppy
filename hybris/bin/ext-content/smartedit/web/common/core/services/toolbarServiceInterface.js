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
angular.module('toolbarInterfaceModule', ['functionsModule'])

/**
 * @ngdoc service
 * @name toolbarInterfaceModule.ToolbarServiceInterface
 *
 * @description
 * Provides an abstract extensible toolbar service. Used to manage and perform actions to either the SmartEdit
 * application or the SmartEdit container.
 *
 * This class serves as an interface and should be extended, not instantiated.
 */
.factory('ToolbarServiceInterface', function($q, $log, hitch) {

    /////////////////////////////////////
    // ToolbarServiceInterface Prototype
    /////////////////////////////////////

    function ToolbarServiceInterface() {}

    ToolbarServiceInterface.prototype._getUniqueIdentifier = function() {
        return new Date().getTime() + Math.random().toString();
    };

    ToolbarServiceInterface.prototype.getItems = function() {
        return this.actions;
    };

    ToolbarServiceInterface.prototype.getAliases = function() {
        return this.aliases;
    };

    /**
     * @ngdoc method
     * @name toolbarInterfaceModule.ToolbarServiceInterface#addItems
     * @methodOf toolbarInterfaceModule.ToolbarServiceInterface
     *
     * @description
     * Takes an array of actions and maps them internally for display and trigger through an internal callback key.
     *
     * @param {Object[]} actions - List of actions
     * @param {Function} actions.callback - Callback triggered when this toolbar action item is clicked.
     * @param {String} actions.i18nkey - Description translation key
     * @param {String[]} actions.icons - List of image URLs for the icon images (only for ACTION and HYBRID_ACTION)
     * @param {String} actions.type - TEMPLATE, ACTION, or HYBRID_ACTION
     * @param {String} actions.include - HTML template URL (only for TEMPLATE and HYBRID_ACTION)
     */
    ToolbarServiceInterface.prototype.addItems = function(actions) {
        var aliases = actions.map(function(action) {
            var key = this._getUniqueIdentifier();
            this.actions[key] = action.callback;
            return {
                key: key,
                name: action.i18nKey,
                icons: action.icons,
                type: action.type,
                include: action.include,
                className: action.className
            };
        }, this);
        this.addAliases(aliases);
    };

    /////////////////////////////////////
    // Proxied Functions : these functions will be proxied if left unimplemented
    /////////////////////////////////////

    ToolbarServiceInterface.prototype.addAliases = function() {};

    ToolbarServiceInterface.prototype.addItemsStyling = function() {};

    ToolbarServiceInterface.prototype.triggerActionOnInner = function() {};

    return ToolbarServiceInterface;
});
