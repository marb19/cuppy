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
describe('test bootstrap Service class', function() {

    var bootstrapService, configurationExtractorService, sharedDataService, injectJS, frame;
    var configurations = {
        smarteditroot: 'smarteditroot1',
        domain: 'domain1',
        applications: 'applications1',
        'authentication.credentials': {
            key2: 'value2'
        }

    };

    beforeEach(customMatchers);
    beforeEach(module('bootstrapServiceModule'));

    beforeEach(module('configurationExtractorServiceModule', function($provide) {

        frame = jasmine.createSpyObj('frame', ['postMessage']);

        configurationExtractorService = jasmine.createSpyObj('configurationExtractorService', ['extractSEContainerModules', 'extractSEModules']);
        configurationExtractorService.extractSEContainerModules.andReturn({
            applications: ['AppA', 'AppB'],
            appLocations: ['SEContainerLocationForAppA', 'SEContainerLocationForAppB'],
            authenticationMap: {
                key1: 'value1'
            }
        });

        configurationExtractorService.extractSEModules.andReturn({
            applications: ['AppC', 'AppD'],
            appLocations: ['SELocationForAppC', 'SELocationForAppD'],
            authenticationMap: {
                key1: 'value1'
            }
        });

        $provide.value('configurationExtractorService', configurationExtractorService);

        injectJS = jasmine.createSpyObj('injectJS', ['execute']);
        $provide.value('injectJS', injectJS);
    }));

    beforeEach(inject(function(_bootstrapService_, _sharedDataService_, _configurationExtractorService_) {
        bootstrapService = _bootstrapService_;
        sharedDataService = _sharedDataService_;
        spyOn(bootstrapService, 'bootstrapSmartEditContainer').andReturn();
        spyOn(bootstrapService, 'addDependencyToSmartEditContainer').andReturn();
        spyOn(sharedDataService, 'set').andReturn();
        configurationExtractorService = _configurationExtractorService_;
    }));

    it('calling bootstrapContainerModules will invoke extractSEContainerModules and inject the javascript sources,' +
        ' push the modules to smarteditcontainer module and re-bootstrap smarteditcontainer',
        function() {

            bootstrapService.bootstrapContainerModules(configurations);

            expect(injectJS.execute).toHaveBeenCalledWith(jasmine.objectContaining({
                srcs: ['SEContainerLocationForAppA', 'SEContainerLocationForAppB']
            }));

            expect(Object.keys(injectJS.execute.calls[0].args[0]).length).toBe(2);

            var callback = injectJS.execute.calls[0].args[0].callback;

            expect(bootstrapService.addDependencyToSmartEditContainer).not.toHaveBeenCalled();
            expect(bootstrapService.bootstrapSmartEditContainer).not.toHaveBeenCalled();

            callback();

            expect(bootstrapService.addDependencyToSmartEditContainer).toHaveBeenCalledWith('AppA');
            expect(bootstrapService.addDependencyToSmartEditContainer).toHaveBeenCalledWith('AppB');
            expect(bootstrapService.bootstrapSmartEditContainer).toHaveBeenCalled();

            expect(sharedDataService.set).toHaveBeenCalledWith('authenticationMap', {
                key1: 'value1'
            });
            expect(sharedDataService.set).toHaveBeenCalledWith('credentialsMap', {
                key2: 'value2'
            });

        });

    it('calling bootstrapSEApp will invoke extractSEModules and inject the javascript sources by means of postMessage',
        function() {

            spyOn(bootstrapService, '_getIframe').andReturn(frame);
            jasmine.Clock.useMock();
            bootstrapService.bootstrapSEApp(configurations);
            jasmine.Clock.tick(1000);

            expect(configurationExtractorService.extractSEModules).toHaveBeenCalledWith(configurations);

            expect(frame.postMessage).toHaveBeenCalledWith({
                eventName: 'smarteditBootstrap',
                resources: {
                    properties: {
                        domain: 'domain1',
                        smarteditroot: 'smarteditroot1',
                        applications: ['AppC', 'AppD'],
                    },
                    js: ['smarteditroot1/static-resources/dist/smartedit/js/presmartedit.js', 'SELocationForAppC', 'SELocationForAppD', 'smarteditroot1/static-resources/dist/smartedit/js/postsmartedit.js'],
                    css: ['smarteditroot1/static-resources/thirdparties/bootstrap/dist/css/bootstrap.min.css', 'smarteditroot1/static-resources/thirdparties/ui-select/dist/select.min.css', 'smarteditroot1/static-resources/thirdparties/select2/select2.css', 'smarteditroot1/static-resources/thirdparties/selectize/dist/css/selectize.default.css', 'smarteditroot1/static-resources/techne/css/techne.min.css', 'smarteditroot1/static-resources/dist/smartedit/css/style.css']
                }
            }, '*');


            expect(sharedDataService.set).toHaveBeenCalledWith('authenticationMap', {
                key1: 'value1'
            });
            expect(sharedDataService.set).toHaveBeenCalledWith('credentialsMap', {
                key2: 'value2'
            });
        });

});
