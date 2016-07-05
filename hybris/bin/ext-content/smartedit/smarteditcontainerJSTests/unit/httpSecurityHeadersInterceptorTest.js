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
describe('unit test httpSecurityHeaders interceptor', function() {

    var $q, $httpProvider, interceptor, parseQueryMock;

    beforeEach(customMatchers);

    beforeEach(module('httpSecurityHeadersInterceptorModule', function($provide, _$httpProvider_) {
        $httpProvider = _$httpProvider_;

        parseQueryMock = jasmine.createSpy('parseQuery');
        $provide.value('parseQuery', parseQueryMock);
    }));

    beforeEach(inject(function(_$q_, _httpSecurityHeadersInterceptor_) {
        $q = _$q_;
        interceptor = _httpSecurityHeadersInterceptor_;
    }));

    it('will be loaded with the interceptor', function() {

        expect($httpProvider.interceptors).toContain('httpSecurityHeadersInterceptor');

    });

    it('will append security headers to http request', function() {

        var config = {};

        expect(interceptor.request(config)).toBe(config);
        expect(interceptor.request(config).headers["X-FRAME-Options"]).toEqual("DENY");
        expect(interceptor.request(config).headers["X-XSS-Protection"]).toEqual("1; mode=block");
        expect(interceptor.request(config).headers["X-Content-Type-Options"]).toEqual("nosniff");
        expect(interceptor.request(config).headers["Strict-Transport-Security"]).toEqual("max-age=16070400; includeSubDomains");
    });

});
