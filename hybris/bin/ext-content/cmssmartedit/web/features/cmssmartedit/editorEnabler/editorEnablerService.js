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
/**
 * @ngdoc overview
 * @name editorEnablerServiceModule
 * @description
 * # The editorEnablerServiceModule
 *
 * The editor enabler service module provides a service that allows enabling the Edit Component contextual menu item,
 * providing a SmartEdit CMS admin the ability to edit various properties of the given component.
 */
angular.module("editorEnablerServiceModule", ["contextualMenuServiceModule", "editorModalServiceModule"])

/**
 * @ngdoc service
 * @name editorEnablerServiceModule.service:editorEnablerService
 *
 * @description
 * Convenience service to attach the open editor modal action to the contextual menu of a given component type, or
 * given regex corresponding to a selection of component types.
 *
 * Example: The Edit button is added to the contextual menu of the CMSParagraphComponent, and all types postfixed
 * with BannerComponent.
 *
 * <pre>
    angular.module('app', ['editorEnablerServiceModule'])
        .run(function(editorEnablerService) {
            editorEnablerService.enableForComponent('CMSParagraphComponent');
            editorEnablerService.enableForComponent('*BannerComponent');
        });
 * </pre>
 */
.factory("editorEnablerService", function(contextualMenuService, editorModalService, $log) {

    // Class Definition
    function EditorEnablerService() {}

    // Private
    EditorEnablerService.prototype._editI18nKey = "contextmenu.title.edit";

    EditorEnablerService.prototype._editDisplayClass = "editbutton";

    EditorEnablerService.prototype._editButtonIconIdle = "/cmssmartedit/images/contextualmenu_edit_off.png";

    EditorEnablerService.prototype._editButtonIconNonIdle = "/cmssmartedit/images/contextualmenu_edit_on.png";

    EditorEnablerService.prototype._editButtonSmallIcon = "/cmssmartedit/images/info.png";

    EditorEnablerService.prototype._editButtonCallback = function(componentType, componentId) {
        editorModalService.open(componentType, componentId);
    };

    // Public
    /**
     * @ngdoc method
     * @name editorEnablerServiceModule.service:editorEnablerService#enableForComponent
     * @methodOf editorEnablerServiceModule.service:editorEnablerService
     *
     * @description
     * Enables the Edit contextual menu item for the given component type.
     *
     * @param {String} componentType The type of component as defined in the platform.
     */
    EditorEnablerService.prototype.enableForComponent = function(componentType) {
        $log.debug("Enabling editor for component " + componentType);
        var contextualMenuConfig = {};
        contextualMenuConfig[componentType] = [];
        contextualMenuConfig[componentType].push({
            displayClass: this._editDisplayClass,
            i18nKey: this._editI18nKey,
            iconIdle: this._editButtonIconIdle,
            iconNonIdle: this._editButtonIconNonIdle,
            smallIcon: this._editButtonSmallIcon,
            callback: this._editButtonCallback
        });

        contextualMenuService.addItems(contextualMenuConfig);
    };

    // Factory Definition
    return new EditorEnablerService();
});
