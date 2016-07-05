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
angular
    .module('LoadingTemplates', ['catalogDetailsModule'])
    .run(function(catalogDetailsService) {
        catalogDetailsService.addItems([{
            include: '../../smarteditcontainerJSTests/e2e/catalogDetails/templateOne.html'
        }]);
        catalogDetailsService.addItems([{
            include: '../../smarteditcontainerJSTests/e2e/catalogDetails/templateTwo.html'
        }]);
    });
angular.module('smarteditcontainer').requires.push('LoadingTemplates');
