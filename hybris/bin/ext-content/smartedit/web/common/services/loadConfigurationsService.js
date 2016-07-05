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
 * @name loadConfigModule.LoadConfigManager
 * @description
 * The loadConfigModule supplies configuration information to SmartEdit. Configuration is stored in key/value pairs.
 */
angular.module('loadConfigModule', ['functionsModule', 'ngResource', 'sharedDataServiceModule', 'resourceLocationsModule'])
    .factory('LoadConfigManager', function($resource, hitch, copy, $q, convertToArray, sharedDataService, $log, SMARTEDIT_ROOT, SMARTEDIT_RESOURCE_URI_REGEXP, CONFIGURATION_URI) {

        var LoadConfigManager = function() {
            this.editorViewService = $resource(CONFIGURATION_URI);
            this.configuration = [];

            this._convertToObject = function(configuration) {
                var configurations = configuration.reduce(function(previous, current, index, array) {
                    try {
                        previous[current.key] = JSON.parse(current.value);
                    } catch (parseError) {
                        $log.error("item _key_ from configuration contains unparsable JSON data _value_ and was ignored".replace("_key_", current.key).replace("_value_", current.value));
                    }
                    return previous;
                }, {});

                configurations.domain = SMARTEDIT_RESOURCE_URI_REGEXP.exec(this._getLocation())[1];
                configurations.smarteditroot = configurations.domain + '/' + SMARTEDIT_ROOT;
                return configurations;
            };

            this._getLocation = function() {
                return document.location.href;
            };
        };


        LoadConfigManager.prototype._parse = function(configuration) {
            var conf = copy(configuration);
            Object.keys(conf).forEach(function(key) {
                try {
                    conf[key] = JSON.parse(conf[key]);
                } catch (e) {
                    //expected for properties coming form $resource framework such as $promise.... and one wants the configuration editor itself to deal with parsable issues
                }
            });
            return conf;
        };

        /**
         * @ngdoc method
         * @name loadConfigModule.LoadConfigManager#loadAsArray
         * @methodOf loadConfigModule.LoadConfigManager
         *
         * @description
         * Returns configuration values as an array of mapped configuration key/value pairs
         *
         */
        LoadConfigManager.prototype.loadAsArray = function() {
            var deferred = $q.defer();
            this.editorViewService.query().$promise.then(
                hitch(this, function(response) {
                    deferred.resolve(this._parse(response));
                }),
                function(failure) {
                    $log.log("load failed");
                    deferred.reject();
                }
            );
            return deferred.promise;
        };

        /**
         * @ngdoc method
         * @name loadConfigModule.LoadConfigManager#loadAsObject
         * @methodOf loadConfigModule.LoadConfigManager
         *
         * @description
         * Returns configuration values as an object of mapped configuration key/value pairs
         *
         */
        LoadConfigManager.prototype.loadAsObject = function() {
            var deferred = $q.defer();
            this.loadAsArray().then(
                hitch(this, function(response) {
                    var conf = this._convertToObject(response);
                    sharedDataService.set('configuration', conf);
                    deferred.resolve(conf);
                }),
                function(failure) {
                    deferred.reject();
                }
            );
            return deferred.promise;
        };

        return LoadConfigManager;

    })
    .factory('loadConfigManagerService', function(LoadConfigManager) {
        return new LoadConfigManager();
    });
