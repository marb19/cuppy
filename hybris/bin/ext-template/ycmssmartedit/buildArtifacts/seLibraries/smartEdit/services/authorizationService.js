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
 * @name authorizationModule.authorizationService
 * @description
 * The authorization module provides services to fetch CRUD permissions for hybris types existing in the platform.
 * This module makes use of the {@link restServiceFactoryModule restServiceFactoryModule} in order to poll authorization APIs in
 * the backend.
 */
angular.module('authorizationModule', ['restServiceFactoryModule', 'loadConfigModule', 'ngAnimate'])
    .constant('operationPermissionsMap', {
        CREATE: "CREATE",
        READ: "READ",
        UPDATE: "CHANGE",
        REMOVE: "REMOVE"
    })
    .factory('permissionsAdapter', function() {

        var PermissionAdapter = function() {

            this.process = function(serverResponse) {
                return serverResponse;
            };

        };

        return new PermissionAdapter();
    })
    .factory('authorizationService', function($log, restServiceFactory, loadConfigManagerService, permissionsAdapter, operationPermissionsMap, $q, hitch) {


        /**
         * @ngdoc service
         * @name authorizationModule.AuthorizationService
         *
         * @description
         * Service that makes calls to the permissions REST API to expose CRUD permissions for a given
         * hybris type.
         */
        var AuthorizationService = function() {

            this._load = function() {
                return loadConfigManagerService.loadAsObject().then(hitch(this, function(configurations) {
                    this.restService = restServiceFactory.get(configurations.permissionsAPI);
                    return this.restService.get().then(hitch(this, function(response) {
                        this.typePermissions = permissionsAdapter.process(response.typePermissions);
                        return this.typePermissions;
                    }));
                }), function() {
                    $log.error("load configuration failure");
                });
            };

            this._hasPermission = function(type, operationPermission) {
                var entries = this.typePermissions.filter(function(entry) { //cannot use find that is not supported by compliant
                    return entry.type === type;
                });
                return entries.length === 1 ? entries[0].operationPermissions.indexOf(operationPermission) > -1 : false;
            };

            this._can = function(type, operationPermission) {
                var deferred = $q.defer();
                if (this.typePermissions) {
                    deferred.resolve(this._hasPermission(type, operationPermission));
                } else {
                    this._load().then(hitch(this, function() {
                        deferred.resolve(this._hasPermission(type, operationPermission));
                    }, function() {
                        $log.error("_can failure");
                    }));
                }
                return deferred.promise;
            };

            /**
             * @ngdoc method
             * @name authorizationModule.AuthorizationService#canCreate
             * @methodOf authorizationModule.AuthorizationService
             *
             * @description
             * Confirm that a create permission is available on the type
             *
             * @param {String} type the hybris type to check permission
             */
            this.canCreate = function(type) {
                return this._can(type, operationPermissionsMap.CREATE);
            };
            /**
             * @ngdoc method
             * @name authorizationModule.AuthorizationService#canRead
             * @methodOf authorizationModule.AuthorizationService
             *
             * @description
             * Confirm that a read permission is available on the type
             *
             * @param {String} type the hybris type to check permission
             */
            this.canRead = function(type) {
                return this._can(type, operationPermissionsMap.READ);
            };
            /**
             * @ngdoc method
             * @name authorizationModule.AuthorizationService#canUpdate
             * @methodOf authorizationModule.AuthorizationService
             *
             * @description
             * Confirm that a update permission is available on the type
             *
             * @param {String} type the hybris type to check permission
             */
            this.canUpdate = function(type) {
                return this._can(type, operationPermissionsMap.UPDATE);
            };
            /**
             * @ngdoc method
             * @name authorizationModule.AuthorizationService#canDelete
             * @methodOf authorizationModule.AuthorizationService
             *
             * @description
             * Confirm that a delete permission is available on the type
             *
             * @param {String} type the hybris type to check permission
             */
            this.canDelete = function(type) {
                return this._can(type, operationPermissionsMap.REMOVE);
            };
        };

        return new AuthorizationService();

    })
    .factory('permissionDirectiveFactory', function($animate, $log, authorizationService, operationPermissionsMap) {
        return function(operation) {

            var attribute = 'has' + operation.substring(0, 1).toUpperCase() + operation.substring(1).toLowerCase() + 'Permission';
            var scope = {};
            scope[attribute] = '=';

            return {
                transclude: 'element',
                restrict: 'A',
                scope: scope,
                link: function($scope, $element, $attr, ctrl, $transclude) {
                    var childScope, previousElements;
                    $scope.$watch(attribute, function(value) {
                        authorizationService._can(value, operationPermissionsMap[operation]).then(function(hasPermission) {
                            if (hasPermission) {
                                if (!childScope) {
                                    $transclude(function(clone, newScope) {
                                        childScope = newScope;
                                        clone[clone.length++] = document.createComment(' end ' + attribute + ': ' + $attr[attribute] + ' ');
                                        // Note: We only need the first/last node of the cloned nodes.
                                        // However, we need to keep the reference to the jqlite wrapper as it might be changed later
                                        // by a directive with templateUrl when its template arrives.
                                        $animate.enter(clone, $element.parent(), $element);
                                    });
                                }
                            } else {
                                //TODO: doesn't look like previousElements is being used. remove.
                                if (previousElements) {
                                    previousElements.remove();
                                    previousElements = null;
                                }
                                if (childScope) {
                                    childScope.$destroy();
                                    childScope = null;
                                }
                            }
                        }, function() {
                            $log.error("failed to retrieve authorization");
                        });

                    });
                }
            };
        };
    })

/**
 * @ngdoc directive
 * @name authorizationModule.directive:hasCreatePermission
 * @scope
 * @restrict A
 * @element ANY
 *
 * @description
 * Authorization HTML mark-up that will remove elements from the DOM if the user does not have authorization.
 * {@link authorizationModule.AuthorizationService AuthorizationService} Is used to check that the CRUD permisions
 * for a type are met.
 *
 * @param {Type} has-create-permission
 *
 * The hybris type to check for given permission
 */
.directive('hasCreatePermission', function(permissionDirectiveFactory) {
    return permissionDirectiveFactory('CREATE');
})

/**
 * @ngdoc directive
 * @name authorizationModule.directive:hasReadPermission
 * @scope
 * @restrict A
 * @element ANY
 *
 * @description
 * Authorization HTML mark-up that will remove elements from the DOM if the user does not have authorization.
 * {@link authorizationModule.AuthorizationService AuthorizationService} Is used to check that the CRUD permisions
 * for a type are met.
 *
 * @param {Type} has-read-permission
 *
 * The hybris type to check for given permission
 */

.directive('hasReadPermission', function(permissionDirectiveFactory) {
    return permissionDirectiveFactory('READ');
})

/**
 * @ngdoc directive
 * @name authorizationModule.directive:hasUpdatePermission
 * @scope
 * @restrict A
 * @element ANY
 *
 * @description
 * Authorization HTML mark-up that will remove elements from the DOM if the user does not have authorization.
 * {@link authorizationModule.AuthorizationService AuthorizationService} Is used to check that the CRUD permisions
 * for a type are met.
 *
 * @param {Type} has-update-permission
 *
 * The hybris type to check for given permission
 */

.directive('hasUpdatePermission', function(permissionDirectiveFactory) {
    return permissionDirectiveFactory('UPDATE');
})

/**
 * @ngdoc directive
 * @name authorizationModule.directive:hasDeletePermission
 * @scope
 * @restrict A
 * @element ANY
 *
 * @description
 * Authorization HTML mark-up that will remove elements from the DOM if the user does not have authorization.
 * {@link authorizationModule.AuthorizationService AuthorizationService} Is used to check that the CRUD permisions
 * for a type are met.
 *
 * @param {Type} has-delete-permission
 *
 * The hybris type to check for given permission
 */
.directive('hasDeletePermission', function(permissionDirectiveFactory) {
    return permissionDirectiveFactory('DELETE');
});
