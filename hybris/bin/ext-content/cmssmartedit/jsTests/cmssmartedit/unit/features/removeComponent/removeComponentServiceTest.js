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
describe('test removeComponentService class', function() {

    var restServiceFactory, removeComponentService, restServiceForRemoveComponent, experienceInterceptor, renderService, sharedDataService;

    beforeEach(function() {
        angular.module('restServiceFactoryModule', []);
        angular.module('renderServiceModule', []);
        angular.module('sharedDataServiceModule', []);
        angular.module('experienceInterceptorModule', []);
        angular.module('gatewayProxyModule', []);
        angular.module('experienceInterceptorModule', []).constant("CONTEXT_CATALOG", "CURRENT_CONTEXT_CATALOG").constant("CONTEXT_CATALOG_VERSION", "CURRENT_CONTEXT_CATALOG_VERSION").constant("CONTEXT_SITE_ID", "CURRENT_CONTEXT_SITE_ID");
    });

    beforeEach(module('removeComponentService'));

    beforeEach(module('cmsWebservicesConstantsModule'));

    beforeEach(module('restServiceFactoryModule', function($provide) {
        restServiceForRemoveComponent = jasmine.createSpyObj('restServiceForRemoveComponent', ['remove']);

        restServiceFactory = jasmine.createSpyObj('restServiceFactory', ['get']);
        restServiceFactory.get.andCallFake(function() {
            return restServiceForRemoveComponent;
        });
        $provide.value('restServiceFactory', restServiceFactory);
        $provide.value('experienceInterceptor', experienceInterceptor);
    }));

    beforeEach(module('renderServiceModule', function($provide) {
        renderService = jasmine.createSpyObj('renderService', ["renderRemovalBySlotAndComponent"]);
        renderService.renderRemovalBySlotAndComponent.andReturn();
        $provide.value("renderService", renderService);
    }));

    beforeEach(module('experienceInterceptorModule', function($provide) {
        sharedDataService = jasmine.createSpyObj('sharedDataService', ['get']);
        sharedDataService.get.andReturn();
        $provide.value('sharedDataService', sharedDataService);
        $provide.value('experienceInterceptor', experienceInterceptor);
    }));

    beforeEach(module('gatewayProxyModule', function($provide) {
        gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['initForService']);
        $provide.value('gatewayProxy', gatewayProxy);
    }));

    beforeEach(customMatchers);
    beforeEach(testSetup); //includes $rootScope and $q
    beforeEach(inject(function(_removeComponentService_) {
        removeComponentService = _removeComponentService_;
    }));

    var payload = {
        'slotId': 'testSlotId',
        'componentId': 'testComponentId'
    };

    it('should remove a component from a slot', function() {

        var deferred = $q.defer();
        deferred.resolve();

        restServiceForRemoveComponent.remove.andReturn(deferred.promise);
        removeComponentService.removeComponent('testSlotId', 'testComponentId');

        $rootScope.$digest();

        expect(restServiceForRemoveComponent.remove).toHaveBeenCalledWith(payload);
        expect(renderService.renderRemovalBySlotAndComponent).toHaveBeenCalledWith('testSlotId', 'testComponentId');
    });

    it('should not remove a component from a slot', function() {

        var deferred = $q.defer();
        deferred.reject();

        restServiceForRemoveComponent.remove.andReturn(deferred.promise);
        removeComponentService.removeComponent('testSlotId', 'testComponentId');

        $rootScope.$digest();

        expect(restServiceForRemoveComponent.remove).toHaveBeenCalledWith(payload);
        expect(renderService.renderRemovalBySlotAndComponent).not.toHaveBeenCalled();
    });
});
