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
describe('test service alertService', function() {

    var $q, $rootScope, $httpBackend;

    beforeEach(customMatchers);
    beforeEach(module('alertServiceModule'));

    beforeEach(inject(function(_$q_, _$rootScope_, _$httpBackend_, _alertService_) {

        $q = _$q_;
        $rootScope = _$rootScope_;
        $httpBackend = _$httpBackend_;
        alertService = _alertService_;
        jasmine.Clock.useMock();

    }));

    it('alertService.pushAlert will trigger a digest cycle by calling $apply and bind alerts to $rootScope for 3000ms if digest cycle is not yet started', function() {

        spyOn($rootScope, '$apply').andCallThrough();
        spyOn($rootScope, '$digest').andCallThrough();

        alertService.pushAlerts([{
            successful: false,
            message: 'message',
            closeable: true
        }]);

        jasmine.Clock.tick(2000);
        expect($rootScope.alerts).toEqualData([{
            successful: false,
            message: 'message',
            closeable: true
        }]);

        jasmine.Clock.tick(1000);
        expect($rootScope.alerts).toBeUndefined();

        expect($rootScope.$apply).toHaveBeenCalled();
        expect($rootScope.$digest).toHaveBeenCalled();

    });

    it('alertService.pushAlert will not trigger a digest cycle and bind alerts to $rootScope for 3000ms if digest cycle has already started', function() {

        spyOn($rootScope, '$$phase').andReturn(true);
        spyOn($rootScope, '$apply').andCallThrough();
        spyOn($rootScope, '$digest').andCallThrough();

        alertService.pushAlerts([{
            successful: false,
            message: 'message',
            closeable: true
        }]);

        expect($rootScope.alerts).toEqualData([{
            successful: false,
            message: 'message',
            closeable: true
        }]);

        jasmine.Clock.tick(3000);
        expect($rootScope.alerts).toBeUndefined();

        expect($rootScope.$apply).not.toHaveBeenCalled();
        expect($rootScope.$digest).toHaveBeenCalled();

    });

});
