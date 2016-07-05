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
describe('unit test previewTicket interceptor', function() {

    var $q, $httpProvider, interceptor, parseQueryMock;

    beforeEach(customMatchers);

    beforeEach(module('previewTicketInterceptorModule', function($provide, _$httpProvider_) {
        $httpProvider = _$httpProvider_;

        parseQueryMock = jasmine.createSpy('parseQuery');
        $provide.value('parseQuery', parseQueryMock);
    }));

    beforeEach(inject(function(_$q_, _previewTicketInterceptor_) {
        $q = _$q_;
        interceptor = _previewTicketInterceptor_;
    }));

    it('will be loaded with the interceptor', function() {

        expect($httpProvider.interceptors).toContain('previewTicketInterceptor');

    });

    it('will append the preview ticket header if cmsTicketId exists', function() {

        spyOn(interceptor, "_getLocation").andReturn("http://success");
        parseQueryMock.andReturn({
            cmsTicketId: "preview-ticket"
        });

        var config = {};

        expect(interceptor.request(config)).toBe(config);
        expect(interceptor.request(config).headers["X-Preview-Ticket"]).toEqual("preview-ticket");
        expect(parseQueryMock).toHaveBeenCalledWith("http://success");
    });

    it('will NOT append the preview ticket header if cmsTicketId does not exist', function() {

        spyOn(interceptor, "_getLocation").andReturn("http://failure");
        parseQueryMock.andReturn({});

        var config = {};

        expect(interceptor.request(config)).toBe(config);
        expect(config.headers).toBeUndefined();
        expect(parseQueryMock).toHaveBeenCalledWith("http://failure");
    });

});
