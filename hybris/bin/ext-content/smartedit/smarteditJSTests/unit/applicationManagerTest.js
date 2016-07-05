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
describe('test ApplicationManager Module', function() {

    beforeEach(customMatchers);

    var applicationManagerService;

    var bootstrapAngularApp = function(persistedConfigurationMock) {
        module('ApplicationManager', function($provide) {
            $provide.factory('PersistedConfiguration', persistedConfigurationMock);
        });

        inject(function(_ApplicationManagerService_) {
            applicationManagerService = _ApplicationManagerService_;
        });
    };

    var createPersistedConfigurationMock = function(appsParam) {
        var apps = [];
        if (appsParam !== undefined) {
            apps = appsParam;
        }
        return {
            getAppModuleNames: function() {
                return apps;
            }
        };

    };


    //it('Can load with no apps registered', function() {
    //    bootstrapAngularApp(createPersistedConfigurationMock());
    //    expect(Object.keys(applicationManagerService.getApps()).length).toEqualData(0);
    //});

    //it('Can load with no apps registered', function() {
    //    bootstrapAngularApp(createMockPersistedConfiguration('testApp'));
    //    expect(Object.keys(applicationManagerService.getApps()).length).toEqualData(0);
    //});


    //it('Can load with 1 app registered', function() {
    //
    //
    //    module(function($exceptionHandlerProvider) {
    //        $exceptionHandlerProvider.mode('log');
    //    });
    //
    //    var handler;
    //
    //    inject(function($exceptionHandler) {
    //        handler = $exceptionHandler;
    //    });
    //
    //    var items = [ 'one', 'two' ];
    //    angular.forEach('items', function(item) {
    //        throw 'someErr';
    //    });
    //
    //    //module('ApplicationManager', function($provide) {
    //    //    $provide.value('PersistedConfiguration', { applications: [ 'someApp' ] });
    //    //});
    //
    //
    //    //inject(function(_ApplicationManagerService_) {
    //    //    applicationManagerService = _ApplicationManagerService_;
    //    //});
    //
    //
    //
    //    //bootstrapAngularApp(createMockPersistedConfiguration('someApp'));
    //
    //
    //
    //    console.info("bla");
    //    console.debug(handler.errors.length);
    //
    //});


});
