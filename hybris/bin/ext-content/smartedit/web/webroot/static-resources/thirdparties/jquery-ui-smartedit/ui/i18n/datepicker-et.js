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
/* Written by Mart Sõmermaa (mrts.pydev at gmail com). */
(function( factory ) {
	if ( typeof define === "function" && define.amd ) {

		// AMD. Register as an anonymous module.
		define([ "../datepicker" ], factory );
	} else {

		// Browser globals
		factory( jQuery.datepicker );
	}
}(function( datepicker ) {

datepicker.regional['et'] = {
	closeText: 'Sulge',
	prevText: 'Eelnev',
	nextText: 'Järgnev',
	currentText: 'Täna',
	monthNames: ['Jaanuar','Veebruar','Märts','Aprill','Mai','Juuni',
	'Juuli','August','September','Oktoober','November','Detsember'],
	monthNamesShort: ['Jaan', 'Veebr', 'Märts', 'Apr', 'Mai', 'Juuni',
	'Juuli', 'Aug', 'Sept', 'Okt', 'Nov', 'Dets'],
	dayNames: ['Pühapäev', 'Esmaspäev', 'Teisipäev', 'Kolmapäev', 'Neljapäev', 'Reede', 'Laupäev'],
	dayNamesShort: ['Pühap', 'Esmasp', 'Teisip', 'Kolmap', 'Neljap', 'Reede', 'Laup'],
	dayNamesMin: ['P','E','T','K','N','R','L'],
	weekHeader: 'näd',
	dateFormat: 'dd.mm.yy',
	firstDay: 1,
	isRTL: false,
	showMonthAfterYear: false,
	yearSuffix: ''};
datepicker.setDefaults(datepicker.regional['et']);

return datepicker.regional['et'];

}));
