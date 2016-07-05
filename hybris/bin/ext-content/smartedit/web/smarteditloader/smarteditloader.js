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
angular.module('smarteditloader', [
        'loadConfigModule',
        'bootstrapServiceModule',
        'coretemplates',
        'translationServiceModule',
        'httpAuthInterceptorModule',
        'httpSecurityHeadersInterceptorModule'
    ])
    .config(function($logProvider) {
        $logProvider.debugEnabled(false);
    })
    .run(function(loadConfigManagerService, bootstrapService) {

        loadConfigManagerService.loadAsObject().then(function(configurations) {
            bootstrapService.bootstrapContainerModules(configurations);
        });

    });
