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
angular.module('authorizationApp', ['authorizationModule', 'ngMockE2E'])
    .controller('defaultController', function($scope, $httpBackend) {

        $scope.component1 = "componentType1";
        $scope.component2 = "componentType2";

        $scope.toggle = function() {
            $scope.component1 = "componentType2";
            $scope.component2 = "componentType1";
        };

        $httpBackend.whenGET(/configuration/).respond([{
            "id": "8796289666477",
            "value": "\"mockedPermissionsAPI\"",
            "key": "permissionsAPI"
        }]);

        $httpBackend.whenGET(/mockedPermissionsAPI/).respond({
            "typePermissions": [{
                    "type": "componentType1",
                    "operationPermissions": ["CREATE", "READ", "CHANGE", "REMOVE"]
                }, {
                    "type": "componentType2",
                    "operationPermissions": ["READ"]
                }

            ]
        });


    });
