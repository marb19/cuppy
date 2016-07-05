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
describe('outer storage service', function() {

    var $q, $rootScope, $cookies, storageService;

    beforeEach(customMatchers);

    beforeEach(module('gatewayProxyModule', function($provide) {

        gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['initForService']);
        $provide.value('gatewayProxy', gatewayProxy);
    }));

    beforeEach(module('storageServiceModule', function($provide) {

        $cookies = jasmine.createSpyObj('$cookies', ['getObject', 'putObject', 'remove']);
        $provide.value('$cookies', $cookies);
    }));

    beforeEach(inject(function(_$q_, _$rootScope_, _storageService_) {

        $q = _$q_;
        $rootScope = _$rootScope_;
        storageService = _storageService_;

    }));

    it('initialized by gatewayProxy', function() {

        expect(storageService.gatewayId).toBe("storage");
        expect(gatewayProxy.initForService).toHaveBeenCalledWith(storageService, ['isInitialized', 'storeAuthToken', 'getAuthToken', 'removeAuthToken', 'removeAllAuthTokens']);
    });

    it('removeAllAuthTokens will remove from smartedit-sessions cookie', function() {

        storageService.removeAllAuthTokens();

        expect($cookies.remove).toHaveBeenCalledWith("smartedit-sessions");
    });

    it('removeAuthToken for entryPoint1 will remove the entry from smartedit-sessions cookie', function() {

        authTokens = {
            entryPoint1: {
                access_token: 'access_token1',
                token_type: 'bearer'
            },
            entryPoint2: {
                access_token: 'access_token2',
                token_type: 'bearer'
            }
        };
        $cookies.getObject.andReturn(btoa(JSON.stringify(authTokens)));

        storageService.removeAuthToken("entryPoint1");

        expect($cookies.putObject).toHaveBeenCalledWith("smartedit-sessions", btoa(JSON.stringify({
            entryPoint2: {
                access_token: 'access_token2',
                token_type: 'bearer'
            }
        })));
    });

    it('removeAuthToken for entryPoint1 will remove the entire smartedit-sessions cookie', function() {

        authTokens = {
            entryPoint1: {
                access_token: 'access_token1',
                token_type: 'bearer'
            }
        };
        $cookies.getObject.andReturn(btoa(JSON.stringify(authTokens)));

        storageService.removeAuthToken("entryPoint1");

        expect($cookies.remove).toHaveBeenCalledWith("smartedit-sessions");
    });


    it('getAuthToken will get the auth token specific to the given entry point from smartedit-sessions cookie', function() {

        authTokens = {
            entryPoint1: {
                access_token: 'access_token1',
                token_type: 'bearer'
            },
            entryPoint2: {
                access_token: 'access_token2',
                token_type: 'bearer'
            }
        };
        $cookies.getObject.andReturn(btoa(JSON.stringify(authTokens)));

        expect(storageService.getAuthToken("entryPoint2")).toEqual({
            access_token: 'access_token2',
            token_type: 'bearer'
        });

        expect($cookies.getObject).toHaveBeenCalledWith("smartedit-sessions");
    });

    it('storeAuthToken will store the given auth token in a new map with the entryPoint as the key in smartedit-sessions cookie', function() {

        authTokens = {
            entryPoint1: {
                access_token: 'access_token1',
                token_type: 'bearer'
            },
            entryPoint2: {
                access_token: 'access_token2',
                token_type: 'bearer'
            }
        };

        storageService.storeAuthToken("entryPoint1", {
            access_token: 'access_token1',
            token_type: 'bearer'
        });

        expect($cookies.putObject).toHaveBeenCalledWith("smartedit-sessions", btoa(JSON.stringify({
            entryPoint1: {
                access_token: 'access_token1',
                token_type: 'bearer'
            }
        })));
    });

    it('storeAuthToken will store the given auth token in existing map with the entryPoint as the key in pre-existing smartedit-sessions cookie', function() {

        authTokens = {
            entryPoint2: {
                access_token: 'access_token2',
                token_type: 'bearer'
            }
        };
        $cookies.getObject.andReturn(btoa(JSON.stringify(authTokens)));

        storageService.storeAuthToken("entryPoint1", {
            access_token: 'access_token1',
            token_type: 'bearer'
        });

        expect($cookies.putObject).toHaveBeenCalledWith("smartedit-sessions", btoa(JSON.stringify({
            entryPoint2: {
                access_token: 'access_token2',
                token_type: 'bearer'
            },
            entryPoint1: {
                access_token: 'access_token1',
                token_type: 'bearer'
            }
        })));
    });

});
