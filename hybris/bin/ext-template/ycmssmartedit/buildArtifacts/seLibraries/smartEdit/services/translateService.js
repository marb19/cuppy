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
angular.module('translationServiceModule', ['pascalprecht.translate', 'i18nInterceptorModule'])
    .config(function($translateProvider, UNDEFINED_LOCALE) {

        /*
         * hard coded url that is always intercepted by i18nInterceptor so as to replace by value from configuration REST call
         */
        $translateProvider.useStaticFilesLoader({
            prefix: '/i18nAPIRoot/',
            suffix: ''
        });

        // Tell the module what language to use by default
        $translateProvider.preferredLanguage(UNDEFINED_LOCALE);

    })
    /* *
     * original angular-translate $translate requires keys to have double curly brackets place holders for parameters the like of "hello{{world}}"
     * but if the placeholder is a number, it seems to be evaluated right away, leaving no room for inteprolation
     * this custom service/filter will as well accept both arrays and object to pass params and will fail only if passed array when message is non positional
     */
    .factory('i18n', function($translate) {

        return {

            translate: function(key, params) {
                params = params || {};
                if (typeof params == 'string') { //a JSON needs be passed as string from html template otherwise conflict with {{}} syntax in filters
                    params = JSON.parse(params.replace(/'/g, "\""));
                }
                if (params instanceof Array) {
                    //transform Array into {}
                    params = params.reduce(function(previous, next, index, array) {
                        previous[index] = next;
                        return previous;
                    }, {});
                }
                var interpolated = $translate.instant(key);
                //need to find out wether message used positional or named parameters
                var isPositional = /\{0\}/.exec(interpolated) ? true : false;

                //iterate over Object.keys(params) instead of params to access index that is needed when message is positional
                return Object.keys(params).reduce(function(interpolated, current, index, array) {
                    var placeholder = isPositional ? index : current;
                    var rg1 = new RegExp("\\{\\{" + placeholder + "\\}\\}", "g"); //not parameterizable through simplified regex syntax //
                    var rg2 = new RegExp("\\{" + placeholder + "\\}", "g");
                    var value = params[current];
                    return interpolated.replace(rg1, value).replace(rg2, value);
                }, interpolated);
            }
        };
    })
    .filter('i18n', function(i18n) {

        return function(key, params) {
            return i18n.translate(key, params);
        };
    });
