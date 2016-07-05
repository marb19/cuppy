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
describe('test Docorator Filter Service Module', function() {

    beforeEach(customMatchers);

    var applicationManagerService;

    var bootstrapAngularApp = function(mockPersistedConfig) {
        module('ApplicationManager', function($provide) {
            $provide.value('PersistedConfiguration', mockPersistedConfig);
        });

        inject(function(_ApplicationManagerService_) {
            applicationManagerService = _ApplicationManagerService_;
        });
    };

});
