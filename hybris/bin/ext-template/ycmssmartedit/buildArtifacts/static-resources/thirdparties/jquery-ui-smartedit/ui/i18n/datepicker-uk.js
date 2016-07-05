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
/* Written by Maxim Drogobitskiy (maxdao@gmail.com). */
/* Corrected by Igor Milla (igor.fsp.milla@gmail.com). */
(function( factory ) {
	if ( typeof define === "function" && define.amd ) {

		// AMD. Register as an anonymous module.
		define([ "../datepicker" ], factory );
	} else {

		// Browser globals
		factory( jQuery.datepicker );
	}
}(function( datepicker ) {

datepicker.regional['uk'] = {
	closeText: 'Закрити',
	prevText: '&#x3C;',
	nextText: '&#x3E;',
	currentText: 'Сьогодні',
	monthNames: ['Січень','Лютий','Березень','Квітень','Травень','Червень',
	'Липень','Серпень','Вересень','Жовтень','Листопад','Грудень'],
	monthNamesShort: ['Січ','Лют','Бер','Кві','Тра','Чер',
	'Лип','Сер','Вер','Жов','Лис','Гру'],
	dayNames: ['неділя','понеділок','вівторок','середа','четвер','п’ятниця','субота'],
	dayNamesShort: ['нед','пнд','вів','срд','чтв','птн','сбт'],
	dayNamesMin: ['Нд','Пн','Вт','Ср','Чт','Пт','Сб'],
	weekHeader: 'Тиж',
	dateFormat: 'dd.mm.yy',
	firstDay: 1,
	isRTL: false,
	showMonthAfterYear: false,
	yearSuffix: ''};
datepicker.setDefaults(datepicker.regional['uk']);

return datepicker.regional['uk'];

}));
