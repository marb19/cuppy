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
/* Written by Deden Fathurahman (dedenf@gmail.com). */
(function( factory ) {
	if ( typeof define === "function" && define.amd ) {

		// AMD. Register as an anonymous module.
		define([ "../datepicker" ], factory );
	} else {

		// Browser globals
		factory( jQuery.datepicker );
	}
}(function( datepicker ) {

datepicker.regional['id'] = {
	closeText: 'Tutup',
	prevText: '&#x3C;mundur',
	nextText: 'maju&#x3E;',
	currentText: 'hari ini',
	monthNames: ['Januari','Februari','Maret','April','Mei','Juni',
	'Juli','Agustus','September','Oktober','Nopember','Desember'],
	monthNamesShort: ['Jan','Feb','Mar','Apr','Mei','Jun',
	'Jul','Agus','Sep','Okt','Nop','Des'],
	dayNames: ['Minggu','Senin','Selasa','Rabu','Kamis','Jumat','Sabtu'],
	dayNamesShort: ['Min','Sen','Sel','Rab','kam','Jum','Sab'],
	dayNamesMin: ['Mg','Sn','Sl','Rb','Km','jm','Sb'],
	weekHeader: 'Mg',
	dateFormat: 'dd/mm/yy',
	firstDay: 0,
	isRTL: false,
	showMonthAfterYear: false,
	yearSuffix: ''};
datepicker.setDefaults(datepicker.regional['id']);

return datepicker.regional['id'];

}));
