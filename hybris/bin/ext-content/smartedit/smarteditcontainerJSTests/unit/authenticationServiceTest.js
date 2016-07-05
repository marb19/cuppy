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
describe('test service AuthenticationService', function() {
    var authenticationService,
        storageService,
        sharedDataService,
        controllerContext,
        $httpBackend,
        storageServiceMock,
        modalService,
        modalManager,
        languageService,
        loginDialogForm,
        $q,
        $location,
        $route,
        $timeout,
        $rootScope;

    beforeEach(customMatchers);
    beforeEach(module("ngMock"));

    beforeEach(module('gatewayFactoryModule', function($provide) {
        gatewayFactory = jasmine.createSpyObj('gatewayFactory', ['initListener']);
        $provide.value('gatewayFactory', gatewayFactory);
    }));

    beforeEach(module('gatewayProxyModule', function($provide) {

        gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['abc', 'initForService']);
        $provide.value('gatewayProxy', gatewayProxy);
    }));

    beforeEach(module("authenticationModule", function($provide) {

        modalService = jasmine.createSpyObj('modalService', ['open']);
        $provide.value('modalService', modalService);

        languageService = jasmine.createSpyObj('languageService', ['isInitialized']);
        $provide.value('languageService', languageService);

        $provide.constant('DEFAULT_AUTHENTICATION_ENTRY_POINT', 'defaultAuthEntryPoint');
        $provide.constant('DEFAULT_AUTH_MAP', {
            'api3': 'defaultAuthEntryPoint'
        });

        $location = jasmine.createSpyObj('$location', ['url']);
        $provide.value('$location', $location);

        $route = jasmine.createSpyObj('$route', ['reload']);
        $provide.value('$route', $route);

    }));

    beforeEach(inject(function(_authenticationService_, _sharedDataService_, _$httpBackend_, _$q_, _$timeout_, _$rootScope_, _parseQuery_, _storageService_) {
        $timeout = _$timeout_;
        $httpBackend = _$httpBackend_;
        $q = _$q_;
        $rootScope = _$rootScope_;
        storageService = _storageService_;

        spyOn(storageService, 'storeAuthToken').andReturn();
        spyOn(storageService, 'removeAuthToken').andReturn();
        spyOn(storageService, 'removeAllAuthTokens').andReturn();
        spyOn(storageService, 'isInitialized').andReturn($q.when("someState"));

        authenticationService = _authenticationService_;
        sharedDataService = _sharedDataService_;

        spyOn(sharedDataService, 'get').andCallFake(function(key) {
            if (key === 'authenticationMap') {
                return $q.when({
                    "api1": "authEntryPoint1",
                    "api1more": "authEntryPoint2",
                    "api2": "authEntryPoint3",
                });
            } else if (key === 'credentialsMap') {
                return $q.when({
                    authEntryPoint1: {
                        client_id: "client_id_1",
                        client_secret: "client_secret_1"
                    },
                    authEntryPoint2: {
                        client_id: "client_id_2",
                        client_secret: "client_secret_2"
                    }
                });
            } else if (key === 'configuration') {
                return $q.when({
                    domain: 'thedomain'
                });
            }
        });

    }));

    it('isReAuthInProgress reads status set by setReAuthInProgress', function() {

        expect(authenticationService.isReAuthInProgress("someURL")).toBe(false);
        authenticationService.setReAuthInProgress("someURL");
        expect(authenticationService.isReAuthInProgress("someURL")).toBe(true);

    });


    it('WHEN an entry point is filtered using filterEntryPoints AND the entry point matches one in the default auth map THEN the auth entry points returned will include the matched entry point', function() {
        // WHEN
        var promise = authenticationService.filterEntryPoints('api3');

        // THEN
        expect(promise).toBeResolvedWithData(['defaultAuthEntryPoint']);
    });

    it('filterEntryPoints only keeps the values of authenticationMap the regex keys of which match the resource', function() {

        authenticationService.filterEntryPoints("api1moreandmore").then(function(value) {
            expect(value).toEqualData(['authEntryPoint1', 'authEntryPoint2']);
        }, function() {
            expect().fail("failed to resolve to ['authEntryPoint1', 'authEntryPoint2']");
        });

        $rootScope.$digest();

        authenticationService.filterEntryPoints("api2/more").then(function(value) {
            expect(value).toEqualData(['authEntryPoint3']);
        }, function() {
            expect().fail("failed to resolve to ['authEntryPoint3']");
        });

        $rootScope.$digest();

        authenticationService.filterEntryPoints("notfound").then(function(value) {
            expect(value).toEqualData([]);
        }, function() {
            expect().fail("failed to resolve to []");
        });

        $rootScope.$digest();

    });

    it('isAuthEntryPoint returns true only if resource exactly matches at least one of the auth entry points or default auth entry point', function() {

        authenticationService.isAuthEntryPoint("api1moreandmore").then(function(value) {
            expect(value).toBe(false);
        }, function() {
            expect().fail("failed to resolve to false");
        });

        $rootScope.$digest();

        authenticationService.isAuthEntryPoint("authEntryPoint1").then(function(value) {
            expect(value).toBe(true);
        }, function() {
            expect().fail("failed to resolve to true");
        });

        $rootScope.$digest();

        authenticationService.isAuthEntryPoint("authEntryPoint1more").then(function(value) {
            expect(value).toBe(false);
        }, function() {
            expect().fail("failed to resolve to false");
        });

        $rootScope.$digest();


        authenticationService.isAuthEntryPoint("defaultAuthEntryPoint").then(function(value) {
            expect(value).toBe(true);
        }, function() {
            expect().fail("failed to resolve to false");
        });

        $rootScope.$digest();

    });


    it('authenticate will launch modalService and remove authInprogress flag', function() {

        modalService.open.andReturn($q.when("something"));
        languageService.isInitialized.andReturn($q.when());

        authenticationService.setReAuthInProgress("authEntryPoint1");
        authenticationService.authenticate("api1/more").then(function() {}, function() {
            expect().fail("failed to resolve to false");
        });

        $rootScope.$digest();

        expect(modalService.open).toHaveBeenCalledWith({
            cssClasses: "ySELoginInit",
            templateUrl: 'web/common/core/services/loginDialog.html',
            controller: jasmine.any(Array)
        });
        expect(languageService.isInitialized).toHaveBeenCalled();
        expect(authenticationService.isReAuthInProgress("authEntryPoint1")).toBe(false);

    });

    describe('controller of authenticationService', function() {

        beforeEach(function() {

            modalService.open.andReturn($q.when("something"));
            languageService.isInitialized.andReturn($q.when());

            authenticationService.authenticate("api1/more").then(function(data) {
                //expect(data).toBe("something");
            }, function() {
                expect().fail("failed to resolve to false");
            });

            $rootScope.$digest();

            var controller = modalService.open.calls[0].args[0].controller[1];
            expect(controller).toBeDefined();

            modalManager = jasmine.createSpyObj('modalManager', ['setShowHeaderDismiss', 'close']);

            controllerContext = {};
            controller.call(controllerContext, modalManager);


        });

        it('is set with the expected properties and functions', function() {

            expect(controllerContext.authURI).toBe("authEntryPoint1");
            expect(controllerContext.auth).toEqual({
                username: '',
                password: ''
            });

            expect(modalManager.setShowHeaderDismiss).toHaveBeenCalledWith(false);
            expect(controllerContext.initialized).toBe("someState");
            expect(storageService.isInitialized).toHaveBeenCalled();
        });

        it('submit will be rejected is form is invalid', function() {

            controllerContext.auth = {
                username: 'someusername',
                password: 'somepassword'
            };

            var loginDialogForm = {
                $valid: false
            };

            controllerContext.submit(loginDialogForm).then(function() {
                expect().fail("submit should have rejected");
            });

            expect(storageService.storeAuthToken).not.toHaveBeenCalled();
            expect(storageService.removeAuthToken).not.toHaveBeenCalled();
        });

        it('submit will prepare a paylod with optional credentials to auth entry point and resolves and store AuthToken', function() {

            controllerContext.auth = {
                username: 'someusername',
                password: 'somepassword'
            };
            var oauthToken = {
                access_token: 'access-token1',
                token_type: 'bearer'
            };
            $httpBackend.whenPOST("authEntryPoint1").respond(oauthToken);

            var loginDialogForm = {
                $valid: true
            };

            controllerContext.submit(loginDialogForm).then(function() {}, function() {
                expect().fail("submit should have resolved");
            });

            expect(storageService.storeAuthToken).not.toHaveBeenCalled();
            $httpBackend.flush();
            expect(storageService.storeAuthToken).toHaveBeenCalledWith("authEntryPoint1", oauthToken);
            expect(storageService.removeAuthToken).not.toHaveBeenCalled();
            expect(modalManager.close).toHaveBeenCalled();
        });

        it('submit will prepare a paylod with optional credentials to auth entry point and reject and remove AuthToken', function() {

            controllerContext.auth = {
                username: 'someusername',
                password: 'somepassword'
            };
            var oauthToken = {
                access_token: 'access-token1',
                token_type: 'bearer'
            };

            $httpBackend.whenPOST("authEntryPoint1").respond(function() {
                return [401];
            });

            var loginDialogForm = {
                $valid: true
            };

            controllerContext.submit(loginDialogForm).then(function() {
                expect().fail("submit should have rejected");
            }, function() {});


            expect(storageService.removeAuthToken).not.toHaveBeenCalled();
            $httpBackend.flush();
            expect(storageService.removeAuthToken).toHaveBeenCalledWith("authEntryPoint1");
            expect(storageService.storeAuthToken).not.toHaveBeenCalled();
            expect(modalManager.close).not.toHaveBeenCalled();
        });

        it('logout will remove auth tokens from cookie and reload current page if current page is landing page', function() {

            $location.url.andCallFake(function(arg) {
                if (!arg) {
                    return "/";
                }
            });
            authenticationService.logout();

            expect(storageService.removeAllAuthTokens).toHaveBeenCalled();
            expect($location.url).toHaveBeenCalledWith();
            expect($route.reload).toHaveBeenCalled();
        });

        it('logout will remove auth tokens from cookie and reload current page if current page is empty', function() {

            $location.url.andCallFake(function(arg) {
                if (!arg) {
                    return "";
                }
            });
            authenticationService.logout();

            expect(storageService.removeAllAuthTokens).toHaveBeenCalled();
            expect($location.url).toHaveBeenCalledWith();
            expect($route.reload).toHaveBeenCalled();
        });

        it('logout will remove auth tokens from cookie and redirect to landing page if current page is not landing page', function() {

            $location.url.andCallFake(function(arg) {
                if (!arg) {
                    return "/somepage";
                }
            });
            authenticationService.logout();

            expect(storageService.removeAllAuthTokens).toHaveBeenCalled();
            expect($location.url.calls.length).toBe(2);
            expect($location.url.calls[0].args).toEqual([]);
            expect($location.url.calls[1].args).toEqual(["/"]);
        });

    });
});
