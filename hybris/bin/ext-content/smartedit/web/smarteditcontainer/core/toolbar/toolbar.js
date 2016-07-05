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
angular.module('toolbarModule', ['gatewayProxyModule', 'toolbarInterfaceModule', 'translationServiceModule'])

/**
 * @ngdoc service
 * @name toolbarModule.toolbarServiceFactory
 *
 * @description
 * The toolbar service factory generates instances of the {@link toolbarModule.ToolbarService ToolbarService} based on
 * the gateway ID (toolbar-name) provided. Only one ToolbarService instance exists for each gateway ID, that is, the
 * instance is a singleton with respect to the gateway ID.
 */
.factory('toolbarServiceFactory', function($q, $log, gatewayProxy, hitch, extend, ToolbarServiceInterface) {
        /////////////////////////////////////
        // OUTER ToolbarService Prototype
        /////////////////////////////////////

        /**
         * @ngdoc service
         * @name toolbarModule.ToolbarService
         *
         * @description
         * The SmartEdit container toolbar service is used to add toolbar items that can perform actions to either
         * the SmartEdit application or the SmartEdit container. Clients can pass items to this service using the
         * addItems() function. The items are then forwarded to the {@link toolbarModule.directive:toolbar toolbar}
         * directive as aliases (a key-name mapping that maps to a specific callback; the alias models the action as it
         * is meant to be displayed in the toolbar), which is responsible for mapping the actions to the view displayed
         * to the user.
         *
         * Uses {@link gatewayProxyModule.gatewayProxy gatewayProxy} for cross iframe communication, using the toolbar
         * name as the gateway ID.
         *
         * <b>Inherited Methods from {@link toolbarInterfaceModule.ToolbarServiceInterface
         * ToolbarServiceInterface}</b>: {@link toolbarInterfaceModule.ToolbarServiceInterface#addItems
         * addItems}
         *
         * @param {String} gatewayId Toolbar name used by the gateway proxy service.
         */
        var ToolbarService = function(gatewayId) {
            this.actions = {};
            this.aliases = [];
            this.onAliasesChange = null;
            this.gatewayId = gatewayId;

            gatewayProxy.initForService(this, ["addAliases", "triggerActionOnInner"]);
        };

        ToolbarService = extend(ToolbarServiceInterface, ToolbarService);

        ToolbarService.prototype.addAliases = function(aliases) {
            this.aliases = this.aliases.concat(aliases);
            if (this.onAliasesChange) {
                this.onAliasesChange(this.aliases);
            }
        };

        ToolbarService.prototype.setOnAliasesChange = function(onAliasesChange) {
            this.onAliasesChange = onAliasesChange;
        };

        ToolbarService.prototype.triggerAction = function(action) {
            if (action && this.actions[action.key]) {
                hitch(action, this.actions[action.key])();
                return;
            }

            this.triggerActionOnInner(action);
        };

        /**
         * @ngdoc method
         * @name toolbarModule.ToolbarService#addItemsStyling
         * @methodOf toolbarModule.ToolbarService
         *
         * @description
         * Adds CSS classes to the items on the toolbar.
         *
         * @param {String} classes Space-separated list of CSS classes
         */
        ToolbarService.prototype.addItemsStyling = function(classes) {
            this.actionsClasses = classes;
        };

        /////////////////////////////////////
        // Factory and Management
        /////////////////////////////////////
        var toolbarServicesByGatewayId = {};

        /**
         * @ngdoc method
         * @name toolbarModule.toolbarServiceFactory#getToolbarService
         * @methodOf toolbarModule.toolbarServiceFactory
         *
         * @description
         * Returns a single instance of the ToolbarService for the given gateway identifier. If one does not exist, an
         * instance is created and cached.
         *
         * @param {string} gatewayId The toolbar name used for cross iframe communication (see {@link
         * gatewayProxyModule.gatewayProxy gatewayProxy})
         * @returns {ToolbarService} Corresponding ToolbarService instance for given gateway ID.
         */
        var getToolbarService = function(gatewayId) {
            if (!toolbarServicesByGatewayId[gatewayId]) {
                toolbarServicesByGatewayId[gatewayId] = new ToolbarService(gatewayId);
            }
            return toolbarServicesByGatewayId[gatewayId];
        };

        return {
            getToolbarService: getToolbarService
        };
    })
    /**
     * @ngdoc directive
     * @name toolbarModule.directive:toolbar
     * @scope
     * @restrict E
     * @element ANY
     *
     * @description
     * Toolbar HTML mark-up that compiles into a configurable toolbar with an assigned {@link
     * toolbarModule.ToolbarService ToolbarService} for functionality.
     *
     * @param {String} imageRoot Root folder path for images
     * @param {String} cssClass Space-separated string of CSS classes for toolbar item styling
     * @param {String} toolbarName Toolbar name used by the gateway proxy service
     */
    .directive('toolbar', function(toolbarServiceFactory) {
        return {
            templateUrl: 'web/smarteditcontainer/core/toolbar/toolbarTemplate.html',
            restrict: 'E',
            transclude: false,
            replace: true,

            scope: {
                cssClass: '@',
                toolbarName: '@',
                imageRoot: '=imageRoot',
            },

            link: function(scope, element, attrs) {
                if (!scope.imageRoot) {
                    scope.imageRoot = "";
                }

                if (!scope.btnStates) {
                    scope.btnStates = {
                        lang: false,
                        add: false,
                        edit: false,
                        pageInfo: false
                    };
                }
                var toolbarService = toolbarServiceFactory.getToolbarService(scope.toolbarName);
                toolbarService.setOnAliasesChange(function(actions) {
                    scope.actions = actions;
                    scope.actionsClasses = toolbarService.actionsClasses;
                });

                scope.actions = toolbarService.getAliases();
                scope.actionsClasses = toolbarService.actionsClasses;
                scope.triggerAction = function(action, $event) {
                    $event.preventDefault();
                    toolbarService.triggerAction(action);
                };

            }
        };
    });
