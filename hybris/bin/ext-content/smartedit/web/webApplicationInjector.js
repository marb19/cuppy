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
parent.postMessage({
    pk: Math.random(),
    gatewayId: 'smartEditBootstrap',
    eventId: 'loading',
    data: {
        location: document.location.href
    }
}, '*');

$(document).ready(function() {

    parent.postMessage({
        pk: Math.random(),
        gatewayId: 'smartEditBootstrap',
        eventId: 'bootstrapSmartEdit',
        data: {
            location: document.location.href
        }
    }, '*');
});
var injectJS = function(head, srcs, index) {
    if (index < srcs.length) {
        $script(srcs[index], function() {
            injectJS(head, srcs, index + 1);
        });
    }
};

var injectCSS = function(head, cssPaths, index) {

    var link = document.createElement('link');
    link.rel = 'stylesheet';
    link.href = cssPaths[index];
    head.appendChild(link);

    if (index + 1 < cssPaths.length) {
        injectCSS(head, cssPaths, index + 1);
    }
};

// Create IE + others compatible event handler
var eventMethod = window.addEventListener ? "addEventListener" : "attachEvent";
var eventer = window[eventMethod];
var messageEvent = eventMethod == "attachEvent" ? "onmessage" : "message";

// Listen to message from child window
eventer(messageEvent, function(e) {

    //	var originControl = '127.0.0.1:7000';
    //
    //	if(e.origin.indexOf(originControl)==-1){
    //		throw e.origin+" is not allowed to override this storefront";
    //	}
    var event = e.data;
    if (event.eventName == 'smarteditBootstrap') {

        window.smartedit = {};
        if (event.resources && event.resources.properties) {
            for (var i in event.resources.properties) {
                window.smartedit[i] = event.resources.properties[i];
            }
        }

        var head = document.getElementsByTagName("head")[0];

        //JS Files
        if (event.resources && event.resources.js && event.resources.js.length > 0) {
            injectJS(head, event.resources.js, 0);
        }

        //CSS Files
        if (event.resources && event.resources.css && event.resources.css.length > 0) {
            injectCSS(head, event.resources.css, 0);
        }
    }

}, false);
