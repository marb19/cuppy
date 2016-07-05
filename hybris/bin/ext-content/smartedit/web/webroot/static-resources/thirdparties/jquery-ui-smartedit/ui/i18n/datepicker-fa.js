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
/* Javad Mowlanezhad -- jmowla@gmail.com */
/* Jalali calendar should supported soon! (Its implemented but I have to test it) */
(function( factory ) {
	if ( typeof define === "function" && define.amd ) {

		// AMD. Register as an anonymous module.
		define([ "../datepicker" ], factory );
	} else {

		// Browser globals
		factory( jQuery.datepicker );
	}
}(function( datepicker ) {

datepicker.regional['fa'] = {
	closeText: 'بستن',
	prevText: '&#x3C;قبلی',
	nextText: 'بعدی&#x3E;',
	currentText: 'امروز',
	monthNames: [
		'ژانویه',
		'فوریه',
		'مارس',
		'آوریل',
		'مه',
		'ژوئن',
		'ژوئیه',
		'اوت',
		'سپتامبر',
		'اکتبر',
		'نوامبر',
		'دسامبر'
	],
	monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'],
	dayNames: [
		'يکشنبه',
		'دوشنبه',
		'سه‌شنبه',
		'چهارشنبه',
		'پنجشنبه',
		'جمعه',
		'شنبه'
	],
	dayNamesShort: [
		'ی',
		'د',
		'س',
		'چ',
		'پ',
		'ج',
		'ش'
	],
	dayNamesMin: [
		'ی',
		'د',
		'س',
		'چ',
		'پ',
		'ج',
		'ش'
	],
	weekHeader: 'هف',
	dateFormat: 'yy/mm/dd',
	firstDay: 6,
	isRTL: true,
	showMonthAfterYear: false,
	yearSuffix: ''};
datepicker.setDefaults(datepicker.regional['fa']);

return datepicker.regional['fa'];

}));
