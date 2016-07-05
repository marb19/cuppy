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

datepicker.regional['eu'] = {
	closeText: 'Egina',
	prevText: '&#x3C;Aur',
	nextText: 'Hur&#x3E;',
	currentText: 'Gaur',
	monthNames: ['urtarrila','otsaila','martxoa','apirila','maiatza','ekaina',
		'uztaila','abuztua','iraila','urria','azaroa','abendua'],
	monthNamesShort: ['urt.','ots.','mar.','api.','mai.','eka.',
		'uzt.','abu.','ira.','urr.','aza.','abe.'],
	dayNames: ['igandea','astelehena','asteartea','asteazkena','osteguna','ostirala','larunbata'],
	dayNamesShort: ['ig.','al.','ar.','az.','og.','ol.','lr.'],
	dayNamesMin: ['ig','al','ar','az','og','ol','lr'],
	weekHeader: 'As',
	dateFormat: 'yy-mm-dd',
	firstDay: 1,
	isRTL: false,
	showMonthAfterYear: false,
	yearSuffix: ''};
datepicker.setDefaults(datepicker.regional['eu']);

return datepicker.regional['eu'];

}));
