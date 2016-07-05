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

datepicker.regional['lv'] = {
	closeText: 'Aizvērt',
	prevText: 'Iepr.',
	nextText: 'Nāk.',
	currentText: 'Šodien',
	monthNames: ['Janvāris','Februāris','Marts','Aprīlis','Maijs','Jūnijs',
	'Jūlijs','Augusts','Septembris','Oktobris','Novembris','Decembris'],
	monthNamesShort: ['Jan','Feb','Mar','Apr','Mai','Jūn',
	'Jūl','Aug','Sep','Okt','Nov','Dec'],
	dayNames: ['svētdiena','pirmdiena','otrdiena','trešdiena','ceturtdiena','piektdiena','sestdiena'],
	dayNamesShort: ['svt','prm','otr','tre','ctr','pkt','sst'],
	dayNamesMin: ['Sv','Pr','Ot','Tr','Ct','Pk','Ss'],
	weekHeader: 'Ned.',
	dateFormat: 'dd.mm.yy',
	firstDay: 1,
	isRTL: false,
	showMonthAfterYear: false,
	yearSuffix: ''};
datepicker.setDefaults(datepicker.regional['lv']);

return datepicker.regional['lv'];

}));
