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
/* Written by Anders Ekdahl ( anders@nomadiz.se). */
(function( factory ) {
	if ( typeof define === "function" && define.amd ) {

		// AMD. Register as an anonymous module.
		define([ "../datepicker" ], factory );
	} else {

		// Browser globals
		factory( jQuery.datepicker );
	}
}(function( datepicker ) {

datepicker.regional['sv'] = {
	closeText: 'Stäng',
	prevText: '&#xAB;Förra',
	nextText: 'Nästa&#xBB;',
	currentText: 'Idag',
	monthNames: ['Januari','Februari','Mars','April','Maj','Juni',
	'Juli','Augusti','September','Oktober','November','December'],
	monthNamesShort: ['Jan','Feb','Mar','Apr','Maj','Jun',
	'Jul','Aug','Sep','Okt','Nov','Dec'],
	dayNamesShort: ['Sön','Mån','Tis','Ons','Tor','Fre','Lör'],
	dayNames: ['Söndag','Måndag','Tisdag','Onsdag','Torsdag','Fredag','Lördag'],
	dayNamesMin: ['Sö','Må','Ti','On','To','Fr','Lö'],
	weekHeader: 'Ve',
	dateFormat: 'yy-mm-dd',
	firstDay: 1,
	isRTL: false,
	showMonthAfterYear: false,
	yearSuffix: ''};
datepicker.setDefaults(datepicker.regional['sv']);

return datepicker.regional['sv'];

}));
