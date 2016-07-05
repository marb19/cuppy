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
/* Written by Stojce Slavkovski. */
(function( factory ) {
	if ( typeof define === "function" && define.amd ) {

		// AMD. Register as an anonymous module.
		define([ "../datepicker" ], factory );
	} else {

		// Browser globals
		factory( jQuery.datepicker );
	}
}(function( datepicker ) {

datepicker.regional['mk'] = {
	closeText: 'Затвори',
	prevText: '&#x3C;',
	nextText: '&#x3E;',
	currentText: 'Денес',
	monthNames: ['Јануари','Февруари','Март','Април','Мај','Јуни',
	'Јули','Август','Септември','Октомври','Ноември','Декември'],
	monthNamesShort: ['Јан','Фев','Мар','Апр','Мај','Јун',
	'Јул','Авг','Сеп','Окт','Ное','Дек'],
	dayNames: ['Недела','Понеделник','Вторник','Среда','Четврток','Петок','Сабота'],
	dayNamesShort: ['Нед','Пон','Вто','Сре','Чет','Пет','Саб'],
	dayNamesMin: ['Не','По','Вт','Ср','Че','Пе','Са'],
	weekHeader: 'Сед',
	dateFormat: 'dd.mm.yy',
	firstDay: 1,
	isRTL: false,
	showMonthAfterYear: false,
	yearSuffix: ''};
datepicker.setDefaults(datepicker.regional['mk']);

return datepicker.regional['mk'];

}));
