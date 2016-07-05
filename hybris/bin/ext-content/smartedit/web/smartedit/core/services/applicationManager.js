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
angular.module('ApplicationManager', ['functionsModule'])
    .constant('ApplicationConfiguration', {

        applications: {},

        // set of all unique decorator names as keys
        decorators: {},

    })
    //TODO - make this config, not run
    .run(function(ApplicationConfiguration, PersistedConfiguration, $sce, $log) {

        var PLUGIN_SUFFIX = 'Decorator';
        var MAP_SUFFIX = 'ComponentDecoratorMap';
        var TOOLBAR_SUFFIX = 'ToolbarActions';

        var endsWith = function(str, suffix) {
            if (str.length < suffix.length)
                return false;
            return str.lastIndexOf(suffix) === str.length - suffix.length;
        };

        var applications = ApplicationConfiguration.applications; //reset
        var decorators = ApplicationConfiguration.decorators;
        var appModuleNames = PersistedConfiguration.getAppModuleNames();

        for (var moduleNameIndex in appModuleNames) {
            $log.debug('Processing decorators for app: ' + appModuleNames[moduleNameIndex]);

            var moduleRef = angular.module(appModuleNames[moduleNameIndex]);
            var moduleName = moduleRef.name;

            if (applications[moduleName] === undefined) {
                applications[moduleName] = [];
            } else {
                throw 'Duplicate definition of SmartEdit app: ' + moduleName;
            }

            // --- discover and wire app decorators ---
            for (var appDependency in moduleRef.requires) {
                var dependency = angular.module(moduleRef.requires[appDependency]);
                var dependencyName = dependency.name;
                if (endsWith(dependencyName, PLUGIN_SUFFIX)) {
                    $log.debug('Wiring decorator [' + dependencyName + '] for app: [' + moduleName + ']');
                    applications[moduleName].push(dependencyName);

                    //keep full decorator list populated
                    decorators[dependencyName] = dependency;
                }
            }
            if (applications[moduleName].length <= 0) {
                $log.debug('No decorators found for App: ' + moduleName);
            }

        }
    })
    .factory('ApplicationManagerService',
        function(ApplicationConfiguration) {

            var config = ApplicationConfiguration;

            return {

                getApps: function() {
                    var appsClone = {};
                    for (var app in config.applications) {
                        var decoratorsClone = [];
                        for (var decorator in config.applications[app]) {
                            decoratorsClone.push(config.applications[app][decorator]);
                        }
                        appsClone[app] = decoratorsClone;
                    }
                    return appsClone;
                },

                getAllDecorators: function() {
                    var decoratorArray = [];
                    for (var key in config.decorators) {
                        decoratorArray.push(key);
                    }
                    return decoratorArray;
                },
            };
        }

    );
