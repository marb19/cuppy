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
//add alert box to the app

angular.element(document).ready(function() {

    // add sanitization of textarea's and input's
    $('textarea, input[type!=password]').each(function() {
        $(this).attr('data-sanitize-html-input', '');
    });

    //systemModule contains some core decorators centralized on CMSXUI side such as contextualMenu
    var apps = window.smartedit.applications;

    angular.module('ApplicationManager').factory('PersistedConfiguration', function() {
        return {
            getAppModuleNames: function() {
                return apps;
            }
        };
    });

    //add the configured apps as dependencies to this app manager
    for (var appIndex in apps) {
        var app = apps[appIndex];
        try {
            var modRef = angular.module(app);
            if (modRef) {
                angular.module('ApplicationManager').requires.push(app);
            }
        } catch (e) {
            console.error('Failed to load app. No module exists called [' + app + ']');
            throw e;
        }
    }

    angular.module('smartedit')
        .constant('domain', window.smartedit.domain)
        .constant('smarteditroot', window.smartedit.smarteditroot);
    angular.bootstrap(document, ["smartedit"]);
});
