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
(function() {
    angular.module('renderServiceModule', ['functionsModule', 'gatewayProxyModule', 'renderServiceInterfaceModule', 'alertServiceModule'])
        .factory('renderService', function createRenderService($q, $compile, $rootScope, gatewayProxy, extend, RenderServiceInterface, isBlank, hitch, alertService) {

            var RENDERER_CHANNEL_ID = "Renderer";

            var RenderService = function(gatewayId) {
                this.gatewayId = gatewayId;

                gatewayProxy.initForService(this, ["renderComponent", "renderRemovalBySlotAndComponent", "renderRemoval"]);
            };

            RenderService = extend(RenderServiceInterface, RenderService);

            RenderService.prototype.renderComponent = function(componentId, componentType, customContent) {
                try {
                    var targetSelector = ".smartEditComponent[data-smartedit-component-id='" + componentId + "'][data-smartedit-component-type=" + componentType + "]";

                    var renderService = this;
                    return $q.when(function() {
                        renderService._validateRenderComponentParams(componentId, componentType, targetSelector);

                        if (customContent) {
                            return renderService._replaceCustomContent(customContent, targetSelector);
                        } else {
                            return renderService._renderExternalContent(componentId, componentType, targetSelector);
                        }

                    }()).then(function() {
                            var targetComponent = renderService._getComponent(targetSelector);
                            renderService._compile(targetComponent, $rootScope);
                            return $q.when("");
                        },
                        function(response) {
                            alertService.pushAlerts([{
                                successful: false,
                                message: response.message,
                                closeable: true
                            }]);
                            return $q.reject(response);
                        });
                } catch (error) {
                    return $q.reject(error);
                }
            };

            RenderService.prototype.renderRemovalBySlotAndComponent = function(slotId, componentId) {
                return this._getComponent('[data-smartedit-component-id=' + slotId + ']').find('[data-smartedit-component-id=' + componentId + ']').remove();
            };
            RenderService.prototype.renderRemoval = function(componentId, componentType) {
                return this._getComponent('[data-smartedit-component-type=' + componentType + '][data-smartedit-component-id=' + componentId + ']').remove();
            };

            RenderService.prototype._validateRenderComponentParams = function(componentId, componentType, targetSelector) {
                if (isBlank(componentId)) {
                    throw createRenderError("renderService.renderComponent.invalid.componentId");
                }

                if (isBlank(componentType)) {
                    throw createRenderError("renderService.renderComponent.invalid.componentType");
                }

                var targetComponent = this._getComponent(targetSelector);
                if (!targetComponent || targetComponent.length === 0) {
                    throw createRenderError("renderService.renderComponent.noTargetComponentFound");
                } else if (targetComponent.length > 1) {
                    throw createRenderError("renderService.renderComponent.multipleTargetComponentsFound");
                }
            };

            RenderService.prototype._replaceCustomContent = function(customContent, targetSelector) {
                return $q.when(hitch(this, function() {
                    var targetComponent = this._getComponent(targetSelector);
                    targetComponent.empty();
                    targetComponent.append(customContent);
                })());
            };

            RenderService.prototype._renderExternalContent = function(componentId, componentType, targetSelector) {
                var deferred = $q.defer();
                this._getAcceleratorCMSService()
                    .loadComponent(componentId, componentType, targetSelector, deferred.resolve,
                        function(e) {
                            deferred.reject(createRenderError("external.content.could.not.be.loaded", [e]));
                        });

                return deferred.promise;
            };


            RenderService.prototype._compile = function(component, scope) {
                return $compile(component)(scope);
            };

            RenderService.prototype._getComponent = function(componentSelector) {
                return $(componentSelector);
            };

            RenderService.prototype._getAcceleratorCMSService = function() {
                return ACC.cms;
            };

            return new RenderService(RENDERER_CHANNEL_ID);
        });

    function createRenderError(message, stack) {
        return {
            message: message,
            stack: stack || []
        };
    }
})();
