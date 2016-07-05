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
/* Written by Dejan Dimić. */
(function( factory ) {
	if ( typeof define === "function" && define.amd ) {

		// AMD. Register as an anonymous module.
		define([ "../datepicker" ], factory );
	} else {

		// Browser globals
		factory( jQuery.datepicker );
	}
}(function( datepicker ) {

datepicker.regional['sr'] = {
	closeText: 'Затвори',
	prevText: '&#x3C;',
	nextText: '&#x3E;',
	currentText: 'Данас',
	monthNames: ['Јануар','Фебруар','Март','Април','Мај','Јун',
	'Јул','Август','Септембар','Октобар','Новембар','Децембар'],
	monthNamesShort: ['Јан','Феб','Мар','Апр','Мај','Јун',
	'Јул','Авг','Сеп','Окт','Нов','Дец'],
	dayNames: ['Недеља','Понедељак','Уторак','Среда','Четвртак','Петак','Субота'],
	dayNamesShort: ['Нед','Пон','Уто','Сре','Чет','Пет','Суб'],
	dayNamesMin: ['Не','По','Ут','Ср','Че','Пе','Су'],
	weekHeader: 'Сед',
	dateFormat: 'dd.mm.yy',
	firstDay: 1,
	isRTL: false,
	showMonthAfterYear: false,
	yearSuffix: ''};
datepicker.setDefaults(datepicker.regional['sr']);

return datepicker.regional['sr'];

}));
