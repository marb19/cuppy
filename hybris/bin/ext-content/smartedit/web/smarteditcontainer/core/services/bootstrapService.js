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
angular.module('bootstrapServiceModule', ['functionsModule', 'configurationExtractorServiceModule', 'sharedDataServiceModule', 'gatewayFactoryModule'])
    .factory('bootstrapService', function(configurationExtractorService, sharedDataService, injectJS, $log, hitch, customTimeout, WHITE_LISTED_STOREFRONTS_CONFIGURATION_KEY) {

        return {

            _getIframe: function() {
                return document.getElementsByTagName('iframe')[0].contentWindow;
            },
            bootstrapSmartEditContainer: function() {
                angular.bootstrap(document, ["smarteditcontainer"]);
            },
            addDependencyToSmartEditContainer: function(app) {
                $log.debug('Adding app: "' + app + '" to smarteditcontainer');
                angular.module('smarteditcontainer').requires.push(app);
            },
            bootstrapContainerModules: function(configurations) {

                var seContainerModules = configurationExtractorService.extractSEContainerModules(configurations);
                $log.debug("outerAppLocations are:", seContainerModules.appLocations);

                sharedDataService.set('authenticationMap', seContainerModules.authenticationMap);
                sharedDataService.set('credentialsMap', configurations['authentication.credentials']);

                angular.module('smarteditcontainer')
                    .constant('domain', configurations.domain)
                    .constant('smarteditroot', configurations.smarteditroot)
                    .constant(WHITE_LISTED_STOREFRONTS_CONFIGURATION_KEY, configurations[WHITE_LISTED_STOREFRONTS_CONFIGURATION_KEY]);

                injectJS.execute({
                    srcs: seContainerModules.appLocations,
                    callback: hitch(this, function() {
                        seContainerModules.applications.forEach(hitch(this, function(app) {
                            this.addDependencyToSmartEditContainer(app);
                        }));
                        this.bootstrapSmartEditContainer();
                    })
                });
            },

            bootstrapSEApp: function(configurations) {

                var seModules = configurationExtractorService.extractSEModules(configurations);

                sharedDataService.set('authenticationMap', seModules.authenticationMap);
                sharedDataService.set('credentialsMap', configurations['authentication.credentials']);

                var resources = {
                    properties: {
                        domain: configurations.domain,
                        smarteditroot: configurations.smarteditroot,
                        applications: seModules.applications
                    },
                    js: [
                        configurations.smarteditroot + '/static-resources/dist/smartedit/js/presmartedit.js'
                    ],
                    css: [
                        configurations.smarteditroot + '/static-resources/thirdparties/bootstrap/dist/css/bootstrap.min.css',
                        configurations.smarteditroot + '/static-resources/thirdparties/ui-select/dist/select.min.css',
                        configurations.smarteditroot + '/static-resources/thirdparties/select2/select2.css',
                        configurations.smarteditroot + '/static-resources/thirdparties/selectize/dist/css/selectize.default.css',
                        configurations.smarteditroot + '/static-resources/techne/css/techne.min.css',
                        configurations.smarteditroot + '/static-resources/dist/smartedit/css/style.css'
                    ]
                };
                resources.properties[WHITE_LISTED_STOREFRONTS_CONFIGURATION_KEY] = configurations.whiteListedStorefronts;
                resources.js = resources.js.concat(seModules.appLocations);
                resources.js.push(configurations.smarteditroot + '/static-resources/dist/smartedit/js/postsmartedit.js');


                this._getIframe().postMessage({
                    eventName: 'smarteditBootstrap',
                    resources: resources
                }, '*');

            }
        };
    });
