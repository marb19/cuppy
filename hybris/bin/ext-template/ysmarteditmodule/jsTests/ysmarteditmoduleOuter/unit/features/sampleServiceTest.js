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
describe('Sample Outer Module (sample service) Tests', function() {
    var sampleService;

    beforeEach(module('ysmarteditoutermodule'));

    beforeEach(inject(function(_sampleService_) {
        sampleService = _sampleService_;
    }));

    it('sampleNum returns the sample of the provided input number', function() {
        var inputNum = 5;
        var expectedOutput = 25;
        var result = sampleService.squareNum(inputNum);
        expect(result).toBe(expectedOutput);
    });

});
