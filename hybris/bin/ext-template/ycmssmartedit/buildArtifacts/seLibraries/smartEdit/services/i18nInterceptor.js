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
angular.module('i18nInterceptorModule', ['resourceLocationsModule', 'languageServiceModule']) //loose dependency on loadConfigModule since it exists in smartEditContainer but not smartEdit
    /*
     * MISF-445 : temporary adding of i18n keys that did nto make it to 6.0 feature freeze
     */
    .constant('temporaryKeys', {
        //smartedit
        'ytabset.tabs.more': 'More',
        'configurationform.duplicate.entry.error': 'This is a duplicate key',
        'configurationform.json.parse.error': 'this value should be a valid JSON format',
        'configurationform.save.error': 'Save error',
        'configurationform.absoluteurl.check': 'You have to accept the absolute URL as it can be considered dangerous to the system.',
        'sync.confirmation.title': 'Synchronize Catalog',
        'cataloginfo.sync.initiated': 'Sync Initiated',
        'editor.richtext.check': 'I acknowledge that the content I have entered can result in system errors.',

        //cmssmartedit
        'alert.component.removed.from.slot': 'The component {{componentID}} has been removed from slot {{slotID}}',
        'compoment.confirmation.modal.cancel': 'Cancel',
        'component.confirmation.modal.save': 'Save',
        'simpleresponsivebannercomponent.media.postfix.text': 'Be aware that media cannot be edited using current SmartEdit version (6.0). Media editing will be supported in upcoming version',
        'cmsdraganddrop.error': 'failed to remove the component {{sourceComponentId}} from slot {{targetSlotId}}. {{detailedError}}',
        'external.content.could.not.be.loaded': 'External content could not be loaded.'
    })
    .constant('UNDEFINED_LOCALE', 'UNDEFINED')
    /**
     * @ngdoc service
     * @name i18nInterceptorModule.i18nInterceptor
     *
     * @description
     * A HTTP request interceptor which intercepts all i18n calls and handles them appropriately in the {@link i18nInterceptorModule.i18nInterceptor#request request} method.
     *
     * The interceptors are service factories that are registered with the $httpProvider by adding them to the $httpProvider.interceptors array.
     * The factory is called and injected with dependencies and returns the interceptor object, which contains the interceptor methods.
     */
    .factory('i18nInterceptor', function($q, $injector, I18N_RESOURCE_URI, UNDEFINED_LOCALE, temporaryKeys) {

        return {

            /**
             * @ngdoc method
             * @name i18nInterceptorModule.i18nInterceptor#request
             * @methodOf i18nInterceptorModule.i18nInterceptor
             *
             * @description
             * Interceptor method that is called with an HTTP configuration object. It intercepts all requests that are i18n calls, that is,
             * it intercepts all requests that have an i18nAPIRoot in their calls. It replaces the URL provided in a request with the URL provided by {@link resourceLocationsModule.object:I18N_RESOURCE_URI}.
             * The locale is retrieved using $locale.id if it has not already been defined.
             *
             * @param {Object} config the http config object that holds the configuration information.
             *
             * @returns {Promise} Returns a {@link https://docs.angularjs.org/api/ng/service/$q promise} of the passed configuration object.
             */
            request: function(config) {

                var deferred = $q.defer();

                /*
                 * always intercept i18n calls so as to replace URI by one from configuration (cannot be done at config time of $translateProvider)
                 * regex matching /i18nAPIRoot/<my_locale>
                 */
                var regex = /i18nAPIRoot\/([a-zA-Z_]+)$/;

                if (regex.test(config.url)) {
                    var locale = regex.exec(config.url)[1];

                    //if locale hasn't been defined
                    if (locale === UNDEFINED_LOCALE) {
                        locale = $injector.get('languageService').getBrowserLocale();
                    }
                    config.url = [I18N_RESOURCE_URI, locale].join('/');
                }
                deferred.resolve(config);
                return deferred.promise;
            },
            response: function(response) {
                /*
                 * if it intercepts a call to I18N_RESOURCE_URI the response body will be adapted to
                 * read the value from response.data.value instead.
                 */
                var regex = new RegExp(I18N_RESOURCE_URI + "/([a-zA-Z_]+)$");
                if (response && response.config && response.config.url) {
                    var url = response.config.url;
                    if (regex.test(url) && response.data.value) {
                        response.data = response.data.value;
                    }
                    if (regex.test(url)) {
                        /*
                         * MISF-445 : temporary adding of i18n keys that did nto make it to 6.0 feature freeze
                         */
                        for (var key in temporaryKeys) {
                            if (!response.data[key]) {
                                response.data[key] = temporaryKeys[key];
                            }
                        }
                    }

                }
                $injector.get('languageService').setInitialized(true);

                return response;
            }
        };
    })
    .config(function($httpProvider) {
        $httpProvider.interceptors.push('i18nInterceptor');
    });
