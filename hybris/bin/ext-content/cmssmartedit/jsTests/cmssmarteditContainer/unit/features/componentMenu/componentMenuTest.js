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
describe('ComponentMenuService', function() {

    var ComponentService, restServiceFactory, $rootScope, $q;

    function setupRest(rest, $provide) {
        restServiceFactory = jasmine.createSpyObj('restServiceFactory', ['get']);
        restServiceFactory.get.andCallFake(function(uri) {
            return rest;
        });
        $provide.value('restServiceFactory', restServiceFactory);
    }

    beforeEach(function() {
        angular.module('gatewayProxyModule', []);
        angular.module('gatewayFactoryModule', []);
        angular.module('restServiceFactoryModule', []);
        angular.module('renderServiceModule', []);
        angular.module('experienceInterceptorModule', []).constant("CONTEXT_CATALOG", "CURRENT_CONTEXT_CATALOG").constant("CONTEXT_CATALOG_VERSION", "CURRENT_CONTEXT_CATALOG_VERSION").constant("CONTEXT_SITE_ID", "CURRENT_CONTEXT_SITE_ID");
    });

    beforeEach(module('cmsWebservicesConstantsModule'));

    beforeEach(module('gatewayProxyModule', function($provide) {
        gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['initForService', 'asdfasdf']);
        $provide.value('gatewayProxy', gatewayProxy);
    }));

    beforeEach(module('gatewayFactoryModule', function($provide) {
        gatewayFactory = jasmine.createSpyObj('gatewayFactory', ['initListener']);
        $provide.value('gatewayFactory', gatewayFactory);
    }));

    beforeEach(module('renderServiceModule', function($provide) {

        renderService = jasmine.createSpyObj('renderService', ['removeComponent', 'renderRemoval']);
        $provide.value('renderService', renderService);
    }));

    describe("Component Menu - Get All Types Tests", function() {

        var restServiceForTypes;

        beforeEach(module('componentServiceModule', function($provide) {
            restServiceForTypes = jasmine.createSpyObj('restServiceForTypes', ['get']);
            setupRest(restServiceForTypes, $provide);
        }));

        beforeEach(customMatchers);
        beforeEach(inject(function(_ComponentService_, _$rootScope_, _$q_) {
            ComponentService = _ComponentService_;
            $rootScope = _$rootScope_;
            $q = _$q_;
        }));

        it('ComponentService initializes fine', function() {
            expect(ComponentService.listOfComponentTypes).toEqual([]);
        });

        it('ComponentService returns a listOfComponentTypes', function() {
            var response = {
                "componentTypes": [{
                    "code": "AbstractResponsiveBannerComponent",
                    "name": "Abstract Responsive Banner Component"
                }, {
                    "code": "AccountBookmarkComponent",
                    "name": "Account Bookmark Component"
                }, {
                    "code": "AccountControlComponent",
                    "name": "Account Control Component"
                }, {
                    "code": "AccountNavigationCollectionComponent",
                    "name": "Account Navigation Collection Component"
                }]
            };

            var deferred = $q.defer();
            deferred.resolve(response);

            restServiceForTypes.get.andReturn(deferred.promise);
            var promiseResult = ComponentService.loadComponentTypes();

            //checks data inside of the promise itself
            promiseResult.then(function(result) {
                expect(ComponentService.listOfComponentTypes).toEqualData(response);
                expect(ComponentService.listOfComponentTypes.componentTypes).toEqualData(response.componentTypes);
                //expect(result.typesLoaded).toBe(true);
            });

            $rootScope.$digest(); //resolve promises

            expect(restServiceForTypes.get).toHaveBeenCalled();

            //checks that all of the componentTypes are returned 
            //and saved to component service
            expect(ComponentService.listOfComponentTypes).toEqualData(response);
            expect(ComponentService.listOfComponentTypes.componentTypes).toEqualData(response.componentTypes);
        });

        it('ComponentService is unable to load ComponentTypes', function() {

            var deferred = $q.defer();
            deferred.reject();

            restServiceForTypes.get.andReturn(deferred.promise);
            var promiseResult = ComponentService.loadComponentTypes();

            //checks data inside of the promise itself
            promiseResult.then(function(result) {
                //expect to fail
            }, function() {
                //console.log("rejected");
            });

            $rootScope.$digest(); //resolve promises

            expect(restServiceForTypes.get).toHaveBeenCalled();

        });
    });


    describe("Component Menu - Get All Items Tests", function() {
        var restServiceForItems;

        beforeEach(module('componentServiceModule', function($provide) {
            restServiceForItems = jasmine.createSpyObj('restServiceForItems', ['get']);
            setupRest(restServiceForItems, $provide);

        }));

        beforeEach(customMatchers);
        beforeEach(inject(function(_ComponentService_, _$rootScope_, _$q_) {
            ComponentService = _ComponentService_;
            $rootScope = _$rootScope_;
            $q = _$q_;
        }));

        it('ComponentService initializes fine', function() {
            expect(ComponentService.listOfComponentItems).toEqual([]);
        });

        it('ComponentService returns a listOfComonentItems', function() {
            var response = {
                "componentItems": [{
                    "code": "AbstractResponsiveBannerComponent",
                    "name": "Abstract Responsive Banner Component"
                }, {
                    "code": "AccountBookmarkComponent",
                    "name": "Account Bookmark Component"
                }, {
                    "code": "AccountControlComponent",
                    "name": "Account Control Component"
                }, {
                    "code": "AccountNavigationCollectionComponent",
                    "name": "Account Navigation Collection Component"
                }]
            };

            var deferred = $q.defer();
            deferred.resolve(response);

            restServiceForItems.get.andReturn(deferred.promise);
            var promiseResult = ComponentService.loadComponentItems();

            //checks data inside of the promise itself
            promiseResult.then(function(result) {
                expect(ComponentService.listOfComponentItems).toEqualData(response);
                expect(ComponentService.listOfComponentItems.componentTypes).toEqualData(response.componentTypes);
            });

            $rootScope.$digest(); //resolve promises

            expect(restServiceForItems.get).toHaveBeenCalled();

            //checks that all of the componentTypes are returned 
            //and saved to component service
            expect(ComponentService.listOfComponentItems).toEqualData(response);
            expect(ComponentService.listOfComponentItems.componentTypes).toEqualData(response.componentTypes);
        });
    });


    describe('Add New Component Type Tests ', function() {
        var restServiceForAddNewComponent;

        beforeEach(module('componentServiceModule', function($provide) {

            restServiceForAddNewComponent = jasmine.createSpyObj('restServiceForAddNewComponent', ['save']);
            setupRest(restServiceForAddNewComponent, $provide);

        }));

        beforeEach(customMatchers);
        beforeEach(inject(function(_ComponentService_, _$rootScope_, _$q_) {
            ComponentService = _ComponentService_;
            $rootScope = _$rootScope_;
            $q = _$q_;
        }));
        it('Should save a ComponentType', function() {

            var componentType = {
                "code": "AbstractResponsiveBannerComponent",
                "name": "Abstract Responsive Banner Component"
            };

            var deferred = $q.defer();
            deferred.resolve();

            restServiceForAddNewComponent.save.andReturn(deferred.promise);
            var promiseResult = ComponentService.addNewComponent(componentType, "slot-id");

            $rootScope.$digest(); //resolve promises

            expect(restServiceForAddNewComponent.save).toHaveBeenCalled();
        });
    });


    describe('Add New Component Items Tests ', function() {

        beforeEach(module('componentServiceModule', function($provide) {

            restServiceForAddExistingComponent = jasmine.createSpyObj('restServiceForAddExistingComponent', ['save']);
            setupRest(restServiceForAddExistingComponent, $provide);

        }));

        beforeEach(customMatchers);
        beforeEach(inject(function(_ComponentService_, _$rootScope_, _$q_) {
            ComponentService = _ComponentService_;
            $rootScope = _$rootScope_;
            $q = _$q_;
        }));

        it('should add existing component item to slot', function() {
            var componentItem = {
                uId: "component-id"
            };

            var deferred = $q.defer();
            deferred.resolve();

            restServiceForAddExistingComponent.save.andReturn(deferred.promise);
            var promiseResult = ComponentService.addExistingComponent(componentItem, "slot-id", "index");

            $rootScope.$digest(); //resolve promises

            expect(restServiceForAddExistingComponent.save).toHaveBeenCalled();
        });
    });

});
