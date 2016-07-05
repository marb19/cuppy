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
describe('test Gateway Module', function() {

    var $rootScope, $q, $window, $log, systemEventService, gatewayFactory, sendDeferred, listener, gateway, getOrigin;

    beforeEach(customMatchers);

    beforeEach(module('eventServiceModule', function($provide) {

        systemEventService = jasmine.createSpyObj('systemEventService', ['sendAsynchEvent', 'registerEventHandler']);

        $provide.value('systemEventService', systemEventService);

        $log = jasmine.createSpyObj("$log", ["error", "debug"]);
        $provide.value('$log', $log);

        $provide.value('whiteListedStorefronts', ['sometrusteddomain', 'someothertrusteddomain']);
    }));

    beforeEach(module('gatewayFactoryModule'));
    beforeEach(inject(function(_$rootScope_, _$q_, _$window_, _gatewayFactory_, _getOrigin_) {
        $rootScope = _$rootScope_;
        $q = _$q_;
        $window = _$window_;
        gatewayFactory = _gatewayFactory_;
        getOrigin = _getOrigin_;
    }));

    it('should attach a W3C postMessage event when addEventListener exists on window', function() {
        $window.addEventListener = function(eventName, callback) {};
        spyOn($window, 'addEventListener').andReturn();

        gatewayFactory.initListener();

        expect($window.addEventListener).toHaveBeenCalledWith('message', jasmine.any(Function), false);
    });

    it('should attach a W3C postMessage event when attachEvent exists on window', function() {
        $window.addEventListener = null;
        $window.attachEvent = function(eventName, callback) {};
        spyOn($window, 'attachEvent').andReturn();

        gatewayFactory.initListener();

        expect($window.attachEvent).toHaveBeenCalledWith('onmessage', jasmine.any(Function), false);
    });

    describe('test Gateway Module', function() {

        beforeEach(function() {
            $window.addEventListener = function(eventName, callback) {};
            spyOn($window, 'addEventListener').andReturn();
            gatewayFactory.initListener();
            gateway = gatewayFactory.createGateway('test');
            spyOn(gateway, '_processEvent').andReturn();
            listener = $window.addEventListener.calls[0].args[1];
        });

        it('GIVEN that parent frame receives message, domain is not white listed and url not same origin, listener callback will log error and not process event', function() {
            spyOn(gatewayFactory, '_isIframe').andReturn(null);
            var e = {
                origin: 'untrusted'
            };
            listener(e);
            expect(gateway._processEvent).not.toHaveBeenCalled();
            expect($log.error).toHaveBeenCalledWith('disallowed storefront is trying to communicate with smarteditcontainer');
        });

        it('GIVEN that parent frame receives message, url is same origin and gatewayId is test, listener callback will process event of gateway only once', function() {
            spyOn(gatewayFactory, '_isIframe').andReturn(null);
            var e = {
                data: {
                    pk: 'somepk',
                    gatewayId: 'test'
                },
                origin: getOrigin()
            };
            listener(e);
            expect(gateway._processEvent).toHaveBeenCalledWith(e.data);
            expect($log.error).not.toHaveBeenCalled();
            listener(e);
            expect(gateway._processEvent.calls.length).toBe(1);
        });

        it('GIVEN that parent frame receives message, url is not same origin but is white listed and gatewayId is test, listener callback will process event of gateway only once', function() {
            spyOn(gatewayFactory, '_isIframe').andReturn(null);
            var e = {
                data: {
                    pk: 'sometrusteddomain',
                    gatewayId: 'test'
                },
                origin: getOrigin()
            };
            listener(e);
            expect(gateway._processEvent).toHaveBeenCalledWith(e.data);
            expect($log.error).not.toHaveBeenCalled();
            listener(e);
            expect(gateway._processEvent.calls.length).toBe(1);
        });


        it('GIVEN that parent frame receives message, url same origin and gatewayId is not test, listener callback will not process event of gateway', function() {
            spyOn(gatewayFactory, '_isIframe').andReturn(null);
            var e = {
                data: {
                    pk: 'sometrusteddomain',
                    gatewayId: 'nottest'
                },
                origin: getOrigin()
            };
            listener(e);
            expect(gateway._processEvent).not.toHaveBeenCalled();
            expect($log.error).not.toHaveBeenCalled();
        });

    });

    it('on subsequent calls to createGateway with the same gateway id, no gateway should be returned', function() {
        var gateway = gatewayFactory.createGateway('TestChannel1');
        var duplicateGateway = gatewayFactory.createGateway('TestChannel1');

        expect(gateway).toBeDefined();
        expect(duplicateGateway).toBeNull();
    });

    it('should subscribe to the system event service with the event id <gateway_id>:<event_id>', function() {
        var CHANNEL_ID = 'TestChannel';
        var EVENT_ID = 'someEvent';
        var SYSTEM_EVENT_ID = CHANNEL_ID + ':' + EVENT_ID;

        var handler = function() {};
        var gateway = gatewayFactory.createGateway(CHANNEL_ID);

        gateway.subscribe(EVENT_ID, handler);

        expect(systemEventService.registerEventHandler).toHaveBeenCalledWith(SYSTEM_EVENT_ID, handler);
    });

    it('publish will post a W3C message to the target frame and return a hanging promise', function() {
        var gateway = gatewayFactory.createGateway('TestChannel');

        targetFrame = jasmine.createSpyObj('targetFrame', ['postMessage']);
        spyOn(gateway, '_getTargetFrame').andReturn(targetFrame);
        var pk = 'sgeydnkuykertvahdr';
        spyOn(gateway, '_generateIdentifier').andReturn(pk);

        gateway.publish("someEvent", {
            key1: 'abc'
        }).then(
            function() {
                expect().fail();
            },
            function() {
                expect().fail();
            }
        );
        $rootScope.$digest();

        expect(targetFrame.postMessage).toHaveBeenCalledWith({
            pk: pk,
            eventId: "someEvent",
            gatewayId: 'TestChannel',
            data: {
                key1: 'abc'
            }
        }, '*');

    });

    it("_processEvent on an event different from 'promiseReturn' and 'promiseAcknowledgement' will call systemEventService.sendAsynchEvent and publish a success promiseReturn event with the last resolved data from subscribers", function() {
        var gateway = gatewayFactory.createGateway('TestChannel');

        var sendDeferred = $q.defer();
        sendDeferred.resolve('someResolvedData');
        systemEventService.sendAsynchEvent.andReturn(sendDeferred.promise);

        spyOn(gateway, 'publish').andReturn($q.defer().promise);

        var event = {
            pk: 'rlktqnvghsliutergwe',
            eventId: 'someEvent',
            data: {
                key1: 'abc'
            }
        };
        gateway._processEvent(event);

        $rootScope.$digest();

        expect(systemEventService.sendAsynchEvent).toHaveBeenCalledWith('TestChannel:someEvent', {
            key1: 'abc'
        });
        expect(gateway.publish).toHaveBeenCalledWith("promiseReturn", {
            pk: 'rlktqnvghsliutergwe',
            type: 'success',
            resolvedDataOfLastSubscriber: 'someResolvedData'
        });
    });

    it("_processEvent on an event different from 'promiseReturn' and 'promiseAcknowldgement' will call systemEventService.sendAsynchEvent and publish a failure promiseReturn event", function() {
        var gateway = gatewayFactory.createGateway('TestChannel');

        var sendDeferred = $q.defer();
        sendDeferred.reject();
        systemEventService.sendAsynchEvent.andReturn(sendDeferred.promise);

        spyOn(gateway, 'publish').andReturn($q.defer().promise);

        var event = {
            pk: 'rlktqnvghsliutergwe',
            eventId: 'someEvent',
            data: {
                key1: 'abc'
            }
        };
        gateway._processEvent(event);

        $rootScope.$digest();

        expect(systemEventService.sendAsynchEvent).toHaveBeenCalledWith('TestChannel:someEvent', {
            key1: 'abc'
        });
        expect(gateway.publish).toHaveBeenCalledWith("promiseReturn", {
            pk: 'rlktqnvghsliutergwe',
            type: 'failure'
        });
    });

    it('promise from publish is resolved to event.data.resolvedDataOfLastSubscriber when incoming success promiseReturn with same pk', function() {
        var gateway = gatewayFactory.createGateway('TestChannel');

        targetFrame = jasmine.createSpyObj('targetFrame', ['postMessage']);
        spyOn(gateway, '_getTargetFrame').andReturn(targetFrame);
        var pk = 'sgeydnkuykertvahdr';
        spyOn(gateway, '_generateIdentifier').andReturn(pk);

        gateway.publish("someEvent", {
            key1: 'abc'
        }).then(
            function(resolvedData) {
                expect(resolvedData).toBe('someData');
            },
            function() {
                expect().fail();
            }
        );

        gateway._processEvent({
            eventId: 'promiseReturn',
            data: {
                pk: pk,
                type: 'success',
                resolvedDataOfLastSubscriber: 'someData'
            }
        });

        $rootScope.$digest();

    });


    it('promise from publish is rejected when incoming failure promiseReturn with same pk', function() {
        var gateway = gatewayFactory.createGateway('TestChannel');

        targetFrame = jasmine.createSpyObj('targetFrame', ['postMessage']);
        spyOn(gateway, '_getTargetFrame').andReturn(targetFrame);
        var pk = 'sgeydnkuykertvahdr';
        spyOn(gateway, '_generateIdentifier').andReturn(pk);

        gateway.publish("someEvent", {
            key1: 'abc'
        }).then(
            function() {
                expect().fail();
            },
            function() {}
        );

        gateway._processEvent({
            eventId: 'promiseReturn',
            data: {
                pk: pk,
                type: 'failure'
            }
        });

        $rootScope.$digest();

    });

    it('promise from publish is still hanging when incoming promiseReturn with different pk', function() {
        var gateway = gatewayFactory.createGateway('TestChannel');

        targetFrame = jasmine.createSpyObj('targetFrame', ['postMessage']);
        spyOn(gateway, '_getTargetFrame').andReturn(targetFrame);
        var pk = 'sgeydnkuykertvahdr';
        spyOn(gateway, '_generateIdentifier').andReturn(pk);

        gateway.publish("someEvent", {
            key1: 'abc'
        }).then(
            function() {
                expect().fail();
            },
            function() {
                expect().fail();
            }
        );

        gateway._processEvent({
            eventId: 'promiseReturn',
            data: {
                pk: 'fgsdfgssf',
                type: 'success'
            }
        });

        $rootScope.$digest();

    });
});
