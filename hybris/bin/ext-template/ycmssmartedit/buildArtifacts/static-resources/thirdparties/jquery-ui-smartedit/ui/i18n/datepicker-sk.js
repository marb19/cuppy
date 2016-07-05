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
/* Written by Vojtech Rinik (vojto@hmm.sk). */
(function( factory ) {
	if ( typeof define === "function" && define.amd ) {

		// AMD. Register as an anonymous module.
		define([ "../datepicker" ], factory );
	} else {

		// Browser globals
		factory( jQuery.datepicker );
	}
}(function( datepicker ) {

datepicker.regional['sk'] = {
	closeText: 'Zavrieť',
	prevText: '&#x3C;Predchádzajúci',
	nextText: 'Nasledujúci&#x3E;',
	currentText: 'Dnes',
	monthNames: ['január','február','marec','apríl','máj','jún',
	'júl','august','september','október','november','december'],
	monthNamesShort: ['Jan','Feb','Mar','Apr','Máj','Jún',
	'Júl','Aug','Sep','Okt','Nov','Dec'],
	dayNames: ['nedeľa','pondelok','utorok','streda','štvrtok','piatok','sobota'],
	dayNamesShort: ['Ned','Pon','Uto','Str','Štv','Pia','Sob'],
	dayNamesMin: ['Ne','Po','Ut','St','Št','Pia','So'],
	weekHeader: 'Ty',
	dateFormat: 'dd.mm.yy',
	firstDay: 1,
	isRTL: false,
	showMonthAfterYear: false,
	yearSuffix: ''};
datepicker.setDefaults(datepicker.regional['sk']);

return datepicker.regional['sk'];

}));
