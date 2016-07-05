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
angular.module("initializationModule", ['sharedDataServiceModule'])
    .run(function(sharedDataService) {

        // Set an experience and redirect to the storefront to avoid the landing page
        var experience = {
            catalogDescriptor: {
                catalogId: "electronicsContentCatalog",
                catalogVersion: "Online",
                name: "Electronics Content Catalog"
            },
            siteDescriptor: {
                name: "Electronics Site",
                previewUrl: "/smarteditcontainerJSTests/e2e/dummystorefront.html",
                redirectUrl: null,
                uid: "electronics"
            },
            language: 'en',
            time: null
        };

        sharedDataService.set('experience', experience);
    });

angular.module('smarteditcontainer').requires.push('initializationModule');
