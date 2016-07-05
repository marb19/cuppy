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
describe("sync service  - unit test", function() {
    var $httpProvider, synchronizationService, restServiceFactory, $q, $rootScope, SYNC_PATH;
    beforeEach(function() {
        angular.module('resourceLocationsModule', []);
        angular.module('translationServiceModule', []);
        angular.module('alertServiceModule', []);
        angular.module('confirmationModalServiceModule', []);
    });

    beforeEach(module("synchronizationServiceModule", function($provide, _$httpProvider_) {
        $httpProvider = _$httpProvider_;
        $provide.constant("SYNC_PATH", "the_sync_path");
    }));

    beforeEach(module('restServiceFactoryModule', function($provide) {
        var restServiceFactory = jasmine.createSpyObj('restServiceFactory', ['get']);
        var restServiceForSync = jasmine.createSpyObj('searchRestService', ['get', 'update']);

        restServiceFactory.get.andCallFake(function(uri, catalog) {
            return restServiceForSync;
        });

        restServiceForSync.update.andCallFake(function(value) {
            if (value.catalog == "catalog") {
                return $q.when({
                    "date": "2016-02-12T17:09:29+0000",
                    "status": "FINISHED"
                });
            }
        });

        restServiceForSync.get.andCallFake(function(value) {
            if (value.catalog == "catalog") {
                return $q.when({
                    "date": "2016-02-12T16:08:29+0000",
                    "status": "FINISHED"
                });
            }
        });

        $provide.value('restServiceFactory', restServiceFactory);
    }));

    beforeEach(inject(function(_$rootScope_, _synchronizationService_, _$q_, _SYNC_PATH_) {
        $rootScope = _$rootScope_;
        $q = _$q_;
        synchronizationService = _synchronizationService_;
        SYNC_PATH = _SYNC_PATH_;
    }));

    it('should update sync status ', function() {
        var theCatalog = {
            catalogId: "catalog"
        };
        var result = synchronizationService.updateCatalogSync(theCatalog);

        $rootScope.$digest();

        result.then(
            function(response) {
                console.info(response.date);
                expect(response.date).toEqual("2016-02-12T17:09:29+0000");
                expect(response.status).toEqual("FINISHED");
            }
        );
        $rootScope.$digest();
    });


    it('should get catalog sync status', function() {

        var theCatalog = {
            catalogId: "catalog"
        };
        var result = synchronizationService.getCatalogSyncStatus(theCatalog);

        $rootScope.$digest();

        result.then(
            function(response) {
                expect(response.date).toEqual("2016-02-12T16:08:29+0000");
                expect(response.status).toEqual("FINISHED");
            }
        );
        $rootScope.$digest();
    });

});
