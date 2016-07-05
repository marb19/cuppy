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
angular.module('componentMenuApp', ['ngMockE2E',
        'componentItemModule',
        'componentMenuModule',
        'componentSearchModule',
        'componentsModule',
        'componentTypeModule'
    ])
    .controller('defaultController', function($scope, $httpBackend) {

        $httpBackend.whenGET("somepath/en_US").respond({

        });

        $httpBackend.whenGET(/Template/).passThrough();

        var status = false;

        $httpBackend.whenGET(/cmswebservices\/components\/types/).respond({
            "componentTypes": [{
                "code": "AbstractResponsiveBannerComponent"
            }, {
                "code": "AccountBookmarkComponent"
            }, {
                "code": "AccountControlComponent"
            }, {
                "code": "AccountNavigationCollectionComponent"
            }]
        });

    });
