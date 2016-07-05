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
/**
 * @ngdoc service
 * @name iFrameManagerModule
 *
 * @description
 * Module that provides a service called {@link iFrameManagerModule.iFrameManager iFrameManager} which has set of convenient methods
 * to load the storefront within an iframe.
 *
 */
angular.module('iFrameManagerModule', ['functionsModule', 'translationServiceModule', 'ngResource', 'loadConfigModule', 'restServiceFactoryModule', 'sharedDataServiceModule', 'resourceLocationsModule', 'modalServiceModule'])
    .factory("deviceSupports", function() {
        return [{
            icon: "static-resources/images/icon_res_phone.png",
            selectedIcon: "static-resources/images/icon_res_phone_s.png",
            type: "phone",
            width: 480
        }, {
            icon: "static-resources/images/icon_res_wphone.png",
            selectedIcon: "static-resources/images/icon_res_wphone_s.png",
            type: "wide-phone",
            width: 600
        }, {
            icon: "static-resources/images/icon_res_tablet.png",
            selectedIcon: "static-resources/images/icon_res_tablet_s.png",
            type: "tablet",
            width: 700
        }, {
            icon: "static-resources/images/icon_res_wtablet.png",
            selectedIcon: "static-resources/images/icon_res_wtablet_s.png",
            type: "wide-tablet",
            width: 1024
        }, {
            icon: "static-resources/images/icon_res_desktop.png",
            selectedIcon: "static-resources/images/icon_res_desktop_s.png",
            type: "desktop",
            width: 1200
        }, {
            type: "wide-desktop",
            icon: "static-resources/images/icon_res_wdesktop.png",
            selectedIcon: "static-resources/images/icon_res_wdesktop_s.png",
            width: "100%"
        }];




    })
    .factory("deviceOrientations", function($translate, hitch) {

        var deviceOrientations = [{
            orientation: 'vertical',
            key: 'deviceorientation.vertical.label',
        }, {
            orientation: 'horizontal',
            key: 'deviceorientation.horizontal.label'
        }];

        deviceOrientations.forEach(function(instance) {
            $translate(instance.key).then(hitch(instance, function(translation) {
                this.label = translation;
            }));

        });

        return deviceOrientations;

    })
    /**
     * @ngdoc service
     * @name iFrameManagerModule.iFrameManager
     */
    .factory("iFrameManager", function(deviceSupports, deviceOrientations, getURI, isBlank, $log, $resource, loadConfigManagerService, restServiceFactory, hitch, sharedDataService, LANDING_PAGE_PATH, PREVIEW_RESOURCE_URI, $location, modalService) {

        var DEFAULT_WIDTH = "100%";
        var DEFAULT_HEIGHT = "100%";
        var iframeWrapper = "#js_iFrameWrapper";
        var modalArray = [];
        var currentLocation;

        return {

            setCurrentLocation: function(_currentLocation) {
                currentLocation = _currentLocation;
            },
            _getIframe: function() {
                return $('iframe');
            },
            show: function() {
                //this._getIframe().show();
                this._closeWait();
            },
            hide: function() {
                //this._getIframe().hide();
                this._openWait();
            },
            getDeviceSupports: function() {
                return deviceSupports;
            },

            getDeviceOrientations: function() {
                return deviceOrientations;
            },

            /**
             * @ngdoc method
             * @name iFrameManagerModule.iFrameManager#load
             * @methodOf iFrameManagerModule.iFrameManager
             *
             * @description
             * This method loads the storefront within an iframe by setting the src attribute to the given input url
             *
             * @param {String} url The url of the storefront.
             */
            load: function(url) {
                $log.debug("loading storefront ", url);
                this._getIframe().attr('src', url);

            },

            _appendURISuffix: function(url) {
                var pair = url.split('?');
                return pair[0]
                    .replace(/(.+)([^\/])$/g, "$1$2/previewServlet")
                    .replace(/(.+)\/$/g, "$1/previewServlet") + (pair.length == 2 ? "?" + pair[1] : "");
            },
            /**
             * @ngdoc method
             * @name iFrameManagerModule.iFrameManager#loadPreview
             * @methodOf iFrameManagerModule.iFrameManager
             *
             * @description
             * This method loads the preview of the storefront for a given input homepage url and for a given preview ticket.
             * This method will add '/previewServlet' to the URI and append the preview ticket in the query string.
             * <br/>if it is an initial load  {@link iFrameManagerModule.iFrameManager#load load} will be called with this modified homepage.
             * <br/>if it is a subsequent call, the modified homepage will be called through Ajax to initialize the preview (storefront constraint) and then
             * {@link iFrameManagerModule.iFrameManager#load load} will be called with the current location.
             *
             * @param {String} homePage The url of the storefront homePage for a given experience context.
             * @param {String} previewTicket The preview ticket.
             */
            loadPreview: function(homePage, previewTicket) {
                previewURL = homePage;
                if (!/.+\.html/.test(previewURL)) { //for testing purposes
                    previewURL = this._appendURISuffix(previewURL);
                }
                $log.debug("loading storefront iframe with preview ticket:", previewTicket);
                var homepageInPreviewMode = previewURL + (previewURL.indexOf("?") == -1 ? "?" : "&") + "cmsTicketId=" + previewTicket;

                /*
                 * if currentLocation is not set yet, it only means that this is a first loading and that one is trying to load the homepage
                 */
                if (!currentLocation || getURI(homePage) === getURI(currentLocation)) {
                    this.load(homepageInPreviewMode);
                } else {
                    /*
                     * if location to reload in new experience context is different from homepage, one will have to
                     * first load the home page in preview mode and then access the location without preview mode 
                     */
                    restServiceFactory.get(homepageInPreviewMode).get().then(hitch(this, function(html) {
                        this.load(currentLocation);
                    }));

                }
            },

            /**
             * @ngdoc method
             * @name iFrameManagerModule.iFrameManager#initializeCatalogPreview
             * @methodOf iFrameManagerModule.iFrameManager
             *
             * @description
             * If an experience is set in the shared data service, this method will load the preview for this experience.
             * Otherwise, the user will be redirected to the landing page to select an experience.
             * To load a preview, we need to get a preview ticket from an API
             *
             */
            initializeCatalogPreview: function() {

                sharedDataService.get('experience').then(hitch(this, function(experience) {
                    if (!experience) {
                        $location.url(LANDING_PAGE_PATH);
                        return;
                    }
                    loadConfigManagerService.loadAsObject().then(hitch(this, function(configurations) {
                        var resourcePath = configurations.domain + experience.siteDescriptor.previewUrl;
                        var previewRESTService = restServiceFactory.get(configurations.previewTicketURI || PREVIEW_RESOURCE_URI);
                        previewRESTService.save({
                            catalog: experience.catalogDescriptor.catalogId,
                            catalogVersion: experience.catalogDescriptor.catalogVersion,
                            language: experience.languageDescriptor.isocode,
                            resourcePath: resourcePath
                        }).then(hitch(this, function(response) {
                            window.smartEditBootstrapped = {};
                            this.loadPreview(response.resourcePath, response.ticketId);
                            var preview = {
                                previewTicketId: response.ticketId,
                                resourcePath: response.resourcePath
                            };
                            sharedDataService.set('preview', preview);
                        }));
                    }));
                }));
            },

            apply: function(deviceSupport, deviceOrientation) {

                var width, height;
                var isVertical = true;

                if (!isBlank(deviceOrientation)) {
                    isVertical = deviceOrientation.orientation === 'vertical';
                }

                if (!isBlank(deviceSupport)) {
                    width = (isVertical ? deviceSupport.width : deviceSupport.height);
                    height = (isVertical ? deviceSupport.height : deviceSupport.width);
                }
                if (!width) {
                    width = DEFAULT_WIDTH;
                }
                if (!height) {
                    height = DEFAULT_HEIGHT;
                }
                if (deviceSupport) {
                    //hardcoded the name to default to remove the device skin
                    this._getIframe().removeClass().addClass("device-" + (isVertical ? "vertical" : "horizontal") + " device-" + "default");
                } else {
                    this._getIframe().removeClass();
                }
                this._getIframe().css({
                    "width": width,
                    "height": height,
                    "display": "block",
                    "margin": "auto"
                });
            },


            applyDefault: function() {
                this.apply(this.getDeviceSupports[0], this.getDeviceOrientations()[0]);
            },

            _openWait: function() {
                return modalService.open({
                    templateUrl: 'web/common/core/services/waitDialog.html',
                    cssClasses: "ySEWaitDialog",
                    controller: ['modalManager', 'hitch', '$timeout', function(modalManager, hitch, $timeout) {
                        modalArray.push(modalManager);
                    }]
                });
            },
            _closeWait: function() {
                modalArray.forEach(function(modalManager) {
                    modalManager.close();
                });
                modalArray.length = 0;
            }

        };

    });
