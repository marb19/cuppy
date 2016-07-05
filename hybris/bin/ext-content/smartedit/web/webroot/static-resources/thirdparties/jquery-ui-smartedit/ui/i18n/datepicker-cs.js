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
/* Written by Tomas Muller (tomas@tomas-muller.net). */
(function( factory ) {
	if ( typeof define === "function" && define.amd ) {

		// AMD. Register as an anonymous module.
		define([ "../datepicker" ], factory );
	} else {

		// Browser globals
		factory( jQuery.datepicker );
	}
}(function( datepicker ) {

datepicker.regional['cs'] = {
	closeText: 'Zavřít',
	prevText: '&#x3C;Dříve',
	nextText: 'Později&#x3E;',
	currentText: 'Nyní',
	monthNames: ['leden','únor','březen','duben','květen','červen',
	'červenec','srpen','září','říjen','listopad','prosinec'],
	monthNamesShort: ['led','úno','bře','dub','kvě','čer',
	'čvc','srp','zář','říj','lis','pro'],
	dayNames: ['neděle', 'pondělí', 'úterý', 'středa', 'čtvrtek', 'pátek', 'sobota'],
	dayNamesShort: ['ne', 'po', 'út', 'st', 'čt', 'pá', 'so'],
	dayNamesMin: ['ne','po','út','st','čt','pá','so'],
	weekHeader: 'Týd',
	dateFormat: 'dd.mm.yy',
	firstDay: 1,
	isRTL: false,
	showMonthAfterYear: false,
	yearSuffix: ''};
datepicker.setDefaults(datepicker.regional['cs']);

return datepicker.regional['cs'];

}));
