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
angular.module('contextualMenuDecorator', ['translationServiceModule', 'contextualMenuServiceModule', 'ui.bootstrap'])
    .directive('contextualMenu', ['$timeout', 'contextualMenuService', 'smarteditroot', function($timeout, contextualMenuService, smarteditroot) {
        return {
            templateUrl: 'web/smartedit/modules/systemModule/features/contextualMenu/contextualMenuDecoratorTemplate.html',
            restrict: 'C',
            transclude: true,
            replace: false,
            scope: {
                smarteditComponentId: '@',
                smarteditComponentType: '@',
                active: '='
            },

            link: function($scope, element, attrs) {
                var iCtxSize = 50; //fixed for now

                var iBtnMaxCapacity = Math.round(element.width() / iCtxSize) - 1;
                var iLeftBtns = 1;

                if (iBtnMaxCapacity >= 4) {
                    iLeftBtns = 3;
                } else {
                    iLeftBtns = iBtnMaxCapacity - 1;
                }

                $scope.status = {
                    isopen: false
                };

                $scope.toggleDropdown = function($event) {
                    $event.preventDefault();
                    $event.stopPropagation();
                    $scope.status.isopen = !$scope.status.isopen;

                    var oMoreMenuIcon = $($event.target).find('img');

                    if ($scope.status.isopen) {
                        oMoreMenuIcon.attr('src', smarteditroot + '/static-resources/images/more_white.png');
                        oMoreMenuIcon.addClass('bottom-shadow');
                    } else {
                        oMoreMenuIcon.attr('src', smarteditroot + '/static-resources/images/more_bckg.png');
                        oMoreMenuIcon.addClass('bottom-shadow');
                    }
                };

                $scope.isHybrisIcon = function(icon) {
                    var isHybrisIcon = false;
                    if (icon && icon.indexOf("hyicon") != -1) {
                        isHybrisIcon = true;
                    }
                    return isHybrisIcon;
                };

                $scope.getItems = function() {
                    return contextualMenuService.getContextualMenuItems($scope.smarteditComponentType, $scope.smarteditComponentId, iLeftBtns);
                };

                $scope.moreBtn = {
                    condition: function(componentType, componentId) {
                        return true;
                    },
                    callback: function(smarteditComponentType, smarteditComponentId) {
                        $('.dropdown-toggle').dropdown();
                        this.icon = smarteditroot + '/static-resources/images/more_white.png';
                    },
                    displayClass: 'hyicon hyicon-remove',
                    iconIdle: smarteditroot + '/static-resources/images/contextualmenu_more_off.png',
                    iconNonIdle: smarteditroot + '/static-resources/images/contextualmenu_more_on.png'
                };

            }
        };
    }]);
angular.module('sakExecutorDecorator').requires.push('contextualMenuDecorator');

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
angular.module('toolbarModule', ['gatewayProxyModule', 'toolbarInterfaceModule'])

/**
 * @ngdoc service
 * @name toolbarModule.toolbarServiceFactory
 *
 * @description
 * The toolbar service factory generates instances of the {@link toolbarModule.ToolbarService ToolbarService} based on
 * the gateway ID (toolbar-name) provided. Only one ToolbarService instance exists for each gateway ID, that is, the
 * instance is a singleton with respect to the gateway ID.
 */
.factory('toolbarServiceFactory', ['$log', 'gatewayProxy', 'copy', 'extend', 'ToolbarServiceInterface', function($log, gatewayProxy, copy, extend, ToolbarServiceInterface) {

    /**
     * @ngdoc service
     * @name toolbarModule.ToolbarService
     * @constructor
     *
     * @description
     * The inner toolbar service is used to add toolbar actions that affect the inner application, publish aliases to
     * the outer application through the proxy, and store and manage callbacks made by private key. The service is a
     * managed singleton; the {@link toolbarModule.toolbarServiceFactory toolbarServiceFactory} provides a
     * getToolbarService function that returns a single instance of the ToolbarService for the gateway identifier,
     * that is, the toolbar-name provided by the outer toolbar.
     *
     * Uses {@link gatewayProxyModule.gatewayProxy gatewayProxy} for cross iframe communication, using the toolbar name
     * as the gateway ID.
     *
     * <b>Inherited Methods from {@link toolbarInterfaceModule.ToolbarServiceInterface
     * ToolbarServiceInterface}</b>: {@link toolbarInterfaceModule.ToolbarServiceInterface#addItems
     * addItems}
     *
     * @param {String} gatewayId Toolbar name used by the gateway proxy service.
     */
    var ToolbarService = function(gatewayId) {
        this.gatewayId = gatewayId;
        this.actions = {};

        gatewayProxy.initForService(this, ["addAliases", "triggerActionOnInner"]);
    };

    ToolbarService = extend(ToolbarServiceInterface, ToolbarService);

    ToolbarService.prototype.triggerActionOnInner = function(action) {
        if (!this.actions[action.key]) {
            $log.error('triggerActionByKey() - Failed to find action for key ' + action.key);
            return;
        }
        this.actions[action.key]();
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
}]);

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
angular.module('systemModule', [
    'contextualMenuDecorator',
    'decoratorServiceModule'
]);
