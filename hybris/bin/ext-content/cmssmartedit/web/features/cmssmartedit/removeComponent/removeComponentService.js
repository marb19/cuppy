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
angular.module('removeComponentService', ['restServiceFactoryModule', 'renderServiceModule', 'gatewayProxyModule', 'removeComponentServiceInterfaceModule', 'experienceInterceptorModule', 'functionsModule', 'cmsWebservicesConstantsModule'])
    /**
     * @ngdoc service
     * @name removeComponentService.removeComponentService
     * 
     * @description
     * Service to remove a component from a slot
     */
    .factory('removeComponentService', function(restServiceFactory, renderService, extend, gatewayProxy, $q, $log, RemoveComponentServiceInterface, experienceInterceptor, cmsWebservicesConstants) {
        var REMOVE_COMPONENT_CHANNEL_ID = "RemoveComponent";

        var RemoveComponentService = function(gatewayId) {
            this.gatewayId = gatewayId;

            gatewayProxy.initForService(this, ["removeComponent"]);
        };

        RemoveComponentService = extend(RemoveComponentServiceInterface, RemoveComponentService);

        var restServiceForRemoveComponent = restServiceFactory.get(cmsWebservicesConstants.PAGES_CONTENT_SLOT_COMPONENT + '?slotId=:slotId&componentId=:componentId', 'componentId');

        var _payload = {};

        RemoveComponentService.prototype.removeComponent = function(slotId, componentId) {

            var deferred = $q.defer();
            _payload.slotId = slotId;
            _payload.componentId = componentId;


            restServiceForRemoveComponent.remove(_payload).then(
                function(response) {
                    deferred.resolve();
                    renderService.renderRemovalBySlotAndComponent(slotId, componentId);

                },
                function() {
                    deferred.reject();
                }
            );

            return deferred.promise;
        };

        return new RemoveComponentService(REMOVE_COMPONENT_CHANNEL_ID);

    });
