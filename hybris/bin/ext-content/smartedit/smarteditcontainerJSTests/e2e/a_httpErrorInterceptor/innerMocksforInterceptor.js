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
angular.module('InterceptorMock', ['ngMockE2E', 'resourceLocationsModule', 'languageServiceModule'])
    .run(function($httpBackend, languageService, $locale, I18N_RESOURCE_URI) {

        $httpBackend.whenGET(/someapi/).respond(function(method, url, data, headers) {
            return [404, {}];
        });

        var locale = $locale.id.split("-");
        locale = locale[0] + "_" + locale[1].toUpperCase();

        $httpBackend.whenGET(I18N_RESOURCE_URI + "/" + languageService.getBrowserLocale()).respond(function(method, url, data, headers) {
            var map = {
                'type.componenttype1.content.name': 'Content',
                'type.componenttype1.name.name': 'Name',
                'type.componenttype1.mediaContainer.name': 'Media Container',
                'componentform.actions.exit': 'Exit',
                'componentform.actions.cancel': 'Cancel',
                'componentform.actions.submit': 'Submit',
                'abanalytics.popover.title': 'ab analytics',
                'type.componenttype1.content.tooltip': 'enter content',
                'unknown.request.error': 'Your request could not be processed! Please try again later!'
            };
            return [200, map];
        });

    });
