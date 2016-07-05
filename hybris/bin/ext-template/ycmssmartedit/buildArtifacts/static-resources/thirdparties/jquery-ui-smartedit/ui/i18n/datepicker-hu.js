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
(function( factory ) {
	if ( typeof define === "function" && define.amd ) {

		// AMD. Register as an anonymous module.
		define([ "../datepicker" ], factory );
	} else {

		// Browser globals
		factory( jQuery.datepicker );
	}
}(function( datepicker ) {

datepicker.regional['hu'] = {
	closeText: 'bezár',
	prevText: 'vissza',
	nextText: 'előre',
	currentText: 'ma',
	monthNames: ['Január', 'Február', 'Március', 'Április', 'Május', 'Június',
	'Július', 'Augusztus', 'Szeptember', 'Október', 'November', 'December'],
	monthNamesShort: ['Jan', 'Feb', 'Már', 'Ápr', 'Máj', 'Jún',
	'Júl', 'Aug', 'Szep', 'Okt', 'Nov', 'Dec'],
	dayNames: ['Vasárnap', 'Hétfő', 'Kedd', 'Szerda', 'Csütörtök', 'Péntek', 'Szombat'],
	dayNamesShort: ['Vas', 'Hét', 'Ked', 'Sze', 'Csü', 'Pén', 'Szo'],
	dayNamesMin: ['V', 'H', 'K', 'Sze', 'Cs', 'P', 'Szo'],
	weekHeader: 'Hét',
	dateFormat: 'yy.mm.dd.',
	firstDay: 1,
	isRTL: false,
	showMonthAfterYear: true,
	yearSuffix: ''};
datepicker.setDefaults(datepicker.regional['hu']);

return datepicker.regional['hu'];

}));
