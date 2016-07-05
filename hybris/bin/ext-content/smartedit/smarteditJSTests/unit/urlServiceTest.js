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
describe('test urlService', function() {

    var urlService, gatewayProxy;

    beforeEach(customMatchers);

    beforeEach(module('gatewayProxyModule', function($provide) {

        gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['initForService']);
        $provide.value('gatewayProxy', gatewayProxy);
    }));


    beforeEach(module("urlServiceModule"));

    beforeEach(inject(function(_urlService_) {
        urlService = _urlService_;
    }));

    it('openUrlInPopup function is left empty to enable proxying', function() {
        expect(urlService.openUrlInPopup).toBeEmptyFunction();
    });

    it('url service inits a private gateway', function() {
        expect(gatewayProxy.initForService).toHaveBeenCalledWith(urlService);
    });
});
