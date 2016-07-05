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
describe('test permissions Module', function() {

    var configurations = {
        permissionsAPI: "mockedPermissionsAPI"

    };
    var permissionsResponse = {
        "typePermissions": [{
                "type": "componentType1",
                "operationPermissions": ["CREATE", "READ", "CHANGE", "REMOVE"]
            }, {
                "type": "componentType2",
                "operationPermissions": ["READ"]
            }

        ]
    };

    var $rootScope, $q, permissionsAdapter, authorizationService, loadConfigManagerService, restServiceFactory, restService;

    beforeEach(customMatchers);

    beforeEach(module('authorizationModule', function($provide) {

        restServiceFactory = jasmine.createSpyObj('restServiceFactory', ['get']);
        restService = jasmine.createSpyObj('restService', ['get']);
        restServiceFactory.get.andReturn(restService);
        $provide.value('restServiceFactory', restServiceFactory);

        loadConfigManagerService = jasmine.createSpyObj('loadConfigManagerService', ['loadAsObject']);
        $provide.value('loadConfigManagerService', loadConfigManagerService);

    }));

    beforeEach(inject(function(_$rootScope_, _$q_, _permissionsAdapter_, _authorizationService_) {
        $rootScope = _$rootScope_;
        $q = _$q_;
        permissionsAdapter = _permissionsAdapter_;
        authorizationService = _authorizationService_;
    }));

    it('default permissionsAdapter.process does not transform server response', function() {
        expect(permissionsAdapter.process(permissionsResponse.typePermissions)).toEqualData(permissionsResponse.typePermissions);
    });

    it('any can* method will load configuration and query permission API on the first call only', function() {

        var loadConfigDeferred = $q.defer();
        loadConfigDeferred.resolve(configurations);
        loadConfigManagerService.loadAsObject.andReturn(loadConfigDeferred.promise);

        var restDeferred = $q.defer();
        restDeferred.resolve(permissionsResponse);

        restService.get.andReturn(restDeferred.promise);

        expect(restServiceFactory.get).not.toHaveBeenCalled();

        expect(restService.get).not.toHaveBeenCalled();

        authorizationService.canRead("componentType1");

        $rootScope.$digest();

        authorizationService.canRead("componentType2");

        $rootScope.$digest();

        expect(restServiceFactory.get.calls.length).toBe(1);
        expect(restServiceFactory.get).toHaveBeenCalledWith("mockedPermissionsAPI");

        expect(restService.get.calls.length).toBe(1);
        expect(restService.get).toHaveBeenCalled();

    });



    it('canRead will return true for a type that has granted permission', function() {

        var loadConfigDeferred = $q.defer();
        loadConfigDeferred.resolve(configurations);
        loadConfigManagerService.loadAsObject.andReturn(loadConfigDeferred.promise);


        var restDeferred = $q.defer();
        restDeferred.resolve(permissionsResponse);

        restService.get.andReturn(restDeferred.promise);

        authorizationService.canRead("componentType1").then(function(value) {
            expect(value).toBe(true);
        }, function() {
            expect().fail();
        });

        $rootScope.$digest();

    });


    it('canUpdate will return false for a type that does not have granted permission', function() {

        var loadConfigDeferred = $q.defer();
        loadConfigDeferred.resolve(configurations);
        loadConfigManagerService.loadAsObject.andReturn(loadConfigDeferred.promise);


        var restDeferred = $q.defer();
        restDeferred.resolve(permissionsResponse);

        restService.get.andReturn(restDeferred.promise);

        authorizationService.canUpdate("componentType2").then(function(value) {
            expect(value).toBe(false);
        }, function() {
            expect().fail();
        });

        $rootScope.$digest();

    });

    it('canCreate will return false for a type that does not have granted permission', function() {

        var loadConfigDeferred = $q.defer();
        loadConfigDeferred.resolve(configurations);
        loadConfigManagerService.loadAsObject.andReturn(loadConfigDeferred.promise);


        var restDeferred = $q.defer();
        restDeferred.resolve(permissionsResponse);

        restService.get.andReturn(restDeferred.promise);

        authorizationService.canCreate("componentType2").then(function(value) {
            expect(value).toBe(false);
        }, function() {
            expect().fail();
        });

        $rootScope.$digest();

    });


    it('canDelete will return false for a type that does not have granted permission', function() {

        var loadConfigDeferred = $q.defer();
        loadConfigDeferred.resolve(configurations);
        loadConfigManagerService.loadAsObject.andReturn(loadConfigDeferred.promise);


        var restDeferred = $q.defer();
        restDeferred.resolve(permissionsResponse);

        restService.get.andReturn(restDeferred.promise);

        authorizationService.canUpdate("componentType2").then(function(value) {
            expect(value).toBe(false);
        }, function() {
            expect().fail();
        });

        $rootScope.$digest();

    });

});
