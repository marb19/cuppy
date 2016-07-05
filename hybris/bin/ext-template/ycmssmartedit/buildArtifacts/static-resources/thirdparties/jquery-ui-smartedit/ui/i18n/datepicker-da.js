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
/* Written by Jan Christensen ( deletestuff@gmail.com). */
(function( factory ) {
	if ( typeof define === "function" && define.amd ) {

		// AMD. Register as an anonymous module.
		define([ "../datepicker" ], factory );
	} else {

		// Browser globals
		factory( jQuery.datepicker );
	}
}(function( datepicker ) {

datepicker.regional['da'] = {
	closeText: 'Luk',
	prevText: '&#x3C;Forrige',
	nextText: 'Næste&#x3E;',
	currentText: 'Idag',
	monthNames: ['Januar','Februar','Marts','April','Maj','Juni',
	'Juli','August','September','Oktober','November','December'],
	monthNamesShort: ['Jan','Feb','Mar','Apr','Maj','Jun',
	'Jul','Aug','Sep','Okt','Nov','Dec'],
	dayNames: ['Søndag','Mandag','Tirsdag','Onsdag','Torsdag','Fredag','Lørdag'],
	dayNamesShort: ['Søn','Man','Tir','Ons','Tor','Fre','Lør'],
	dayNamesMin: ['Sø','Ma','Ti','On','To','Fr','Lø'],
	weekHeader: 'Uge',
	dateFormat: 'dd-mm-yy',
	firstDay: 1,
	isRTL: false,
	showMonthAfterYear: false,
	yearSuffix: ''};
datepicker.setDefaults(datepicker.regional['da']);

return datepicker.regional['da'];

}));
