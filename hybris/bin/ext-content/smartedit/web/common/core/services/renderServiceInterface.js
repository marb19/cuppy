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
    /**
     * @ngdoc overview
     * @name renderServiceInterfaceModule
     * @description
     * # The renderServiceInterfaceModule
     *
     * The render service interface module provides an abstract extensible
     * {@link renderServiceInterfaceModule.service:RenderServiceInterface renderService} . It is designed to
     * re-render components after an update component data operation has been performed, according to how
     * the Accelerator displays the component.
     *
     */
    angular.module('renderServiceInterfaceModule', [])
        /**
         * @ngdoc service
         * @name renderServiceInterfaceModule.service:RenderServiceInterface
         * @description
         * Designed to re-render components after an update component data operation has been performed, according to
         * how the Accelerator displays the component.
         *
         * This class serves as an interface and should be extended, not instantiated.
         *
         */
        .factory('RenderServiceInterface', createRenderServiceInterface);

    function createRenderServiceInterface() {

        function RenderServiceInterface() {

        }

        // Proxied Functions
        /**
         * @ngdoc method
         * @name renderServiceInterfaceModule.service:RenderServiceInterface#renderComponent
         * @methodOf renderServiceInterfaceModule.service:RenderServiceInterface
         *
         * @description
         * Re-renders a component in the page.
         *
         * @param {String} componentId The ID of the component.
         * @param {String} componentType The type of the component.
         * @param {String} customContent The custom content to replace the component content with. If specified, the
         * component content will be rendered with it, instead of the accelerator's. Optional.
         *
         * @returns {Promise} Promise that will resolve on render success or reject if there's an error. When rejected,
         * the promise returns an Object{message, stack}.
         */
        RenderServiceInterface.prototype.renderComponent = function(componentId, componentType, customContent) {};

        /**
         * @ngdoc method
         * @name renderServiceInterfaceModule.service:RenderServiceInterface#renderRemovalBySlotAndComponent
         * @methodOf renderServiceInterfaceModule.service:RenderServiceInterface
         *
         * @description
         * This method removes a component from a slot in the current page. Note that the component is only removed
         * on the frontend; the operation does not propagate to the backend.
         *
         * @param {String} slotId The ID of the slot containing the component to remove.
         * @param {String} componentId The ID of the component.
         *
         * @returns {Object} Object wrapping the removed component.
         */
        RenderServiceInterface.prototype.renderRemovalBySlotAndComponent = function(slotId, componentId) {};

        /**
         * @ngdoc method
         * @name renderServiceInterfaceModule.service:RenderServiceInterface#renderRemoval
         * @methodOf renderServiceInterfaceModule.service:RenderServiceInterface
         *
         * @description
         * This method removes a component from a slot in the current page. Note that the component is only removed
         * on the frontend; the operation does not propagate to the backend.
         *
         * @param {String} componentId The ID of the component to remove.
         * @param {String} componentType The type of the component.
         *
         * @returns {Object} Object wrapping the removed component.
         */
        RenderServiceInterface.prototype.renderRemoval = function(componentId, componentType) {};

        return RenderServiceInterface;
    }
})();
