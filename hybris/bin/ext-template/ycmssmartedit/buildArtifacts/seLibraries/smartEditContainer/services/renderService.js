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
    angular.module('renderServiceModule', ['gatewayProxyModule', 'renderServiceInterfaceModule'])
        .factory('renderService', function createRenderService(extend, gatewayProxy, RenderServiceInterface) {
            var RENDERER_CHANNEL_ID = "Renderer";

            var RenderService = function(gatewayId) {
                this.gatewayId = gatewayId;

                gatewayProxy.initForService(this, ["renderComponent", "renderRemovalBySlotAndComponent", "renderRemoval"]);
            };

            RenderService = extend(RenderServiceInterface, RenderService);

            // Methods are delegated to the SmartEdit implementation of the service.

            return new RenderService(RENDERER_CHANNEL_ID);
        });

})();
