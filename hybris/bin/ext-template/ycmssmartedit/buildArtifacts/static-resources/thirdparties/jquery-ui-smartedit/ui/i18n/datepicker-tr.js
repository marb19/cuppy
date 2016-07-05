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
/* Written by Izzet Emre Erkan (kara@karalamalar.net). */
(function( factory ) {
	if ( typeof define === "function" && define.amd ) {

		// AMD. Register as an anonymous module.
		define([ "../datepicker" ], factory );
	} else {

		// Browser globals
		factory( jQuery.datepicker );
	}
}(function( datepicker ) {

datepicker.regional['tr'] = {
	closeText: 'kapat',
	prevText: '&#x3C;geri',
	nextText: 'ileri&#x3e',
	currentText: 'bugün',
	monthNames: ['Ocak','Şubat','Mart','Nisan','Mayıs','Haziran',
	'Temmuz','Ağustos','Eylül','Ekim','Kasım','Aralık'],
	monthNamesShort: ['Oca','Şub','Mar','Nis','May','Haz',
	'Tem','Ağu','Eyl','Eki','Kas','Ara'],
	dayNames: ['Pazar','Pazartesi','Salı','Çarşamba','Perşembe','Cuma','Cumartesi'],
	dayNamesShort: ['Pz','Pt','Sa','Ça','Pe','Cu','Ct'],
	dayNamesMin: ['Pz','Pt','Sa','Ça','Pe','Cu','Ct'],
	weekHeader: 'Hf',
	dateFormat: 'dd.mm.yy',
	firstDay: 1,
	isRTL: false,
	showMonthAfterYear: false,
	yearSuffix: ''};
datepicker.setDefaults(datepicker.regional['tr']);

return datepicker.regional['tr'];

}));
