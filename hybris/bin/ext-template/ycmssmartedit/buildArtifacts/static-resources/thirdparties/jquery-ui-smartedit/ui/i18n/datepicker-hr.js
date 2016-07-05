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
/* Written by Vjekoslav Nesek. */
(function( factory ) {
	if ( typeof define === "function" && define.amd ) {

		// AMD. Register as an anonymous module.
		define([ "../datepicker" ], factory );
	} else {

		// Browser globals
		factory( jQuery.datepicker );
	}
}(function( datepicker ) {

datepicker.regional['hr'] = {
	closeText: 'Zatvori',
	prevText: '&#x3C;',
	nextText: '&#x3E;',
	currentText: 'Danas',
	monthNames: ['Siječanj','Veljača','Ožujak','Travanj','Svibanj','Lipanj',
	'Srpanj','Kolovoz','Rujan','Listopad','Studeni','Prosinac'],
	monthNamesShort: ['Sij','Velj','Ožu','Tra','Svi','Lip',
	'Srp','Kol','Ruj','Lis','Stu','Pro'],
	dayNames: ['Nedjelja','Ponedjeljak','Utorak','Srijeda','Četvrtak','Petak','Subota'],
	dayNamesShort: ['Ned','Pon','Uto','Sri','Čet','Pet','Sub'],
	dayNamesMin: ['Ne','Po','Ut','Sr','Če','Pe','Su'],
	weekHeader: 'Tje',
	dateFormat: 'dd.mm.yy.',
	firstDay: 1,
	isRTL: false,
	showMonthAfterYear: false,
	yearSuffix: ''};
datepicker.setDefaults(datepicker.regional['hr']);

return datepicker.regional['hr'];

}));
