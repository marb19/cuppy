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
/* Written by Mohd Nawawi Mohamad Jamili (nawawi@ronggeng.net). */
(function( factory ) {
	if ( typeof define === "function" && define.amd ) {

		// AMD. Register as an anonymous module.
		define([ "../datepicker" ], factory );
	} else {

		// Browser globals
		factory( jQuery.datepicker );
	}
}(function( datepicker ) {

datepicker.regional['ms'] = {
	closeText: 'Tutup',
	prevText: '&#x3C;Sebelum',
	nextText: 'Selepas&#x3E;',
	currentText: 'hari ini',
	monthNames: ['Januari','Februari','Mac','April','Mei','Jun',
	'Julai','Ogos','September','Oktober','November','Disember'],
	monthNamesShort: ['Jan','Feb','Mac','Apr','Mei','Jun',
	'Jul','Ogo','Sep','Okt','Nov','Dis'],
	dayNames: ['Ahad','Isnin','Selasa','Rabu','Khamis','Jumaat','Sabtu'],
	dayNamesShort: ['Aha','Isn','Sel','Rab','kha','Jum','Sab'],
	dayNamesMin: ['Ah','Is','Se','Ra','Kh','Ju','Sa'],
	weekHeader: 'Mg',
	dateFormat: 'dd/mm/yy',
	firstDay: 0,
	isRTL: false,
	showMonthAfterYear: false,
	yearSuffix: ''};
datepicker.setDefaults(datepicker.regional['ms']);

return datepicker.regional['ms'];

}));
