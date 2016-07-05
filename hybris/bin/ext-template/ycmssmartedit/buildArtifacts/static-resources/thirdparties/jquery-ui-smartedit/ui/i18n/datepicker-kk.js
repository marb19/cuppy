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
/* Written by Dmitriy Karasyov (dmitriy.karasyov@gmail.com). */
(function( factory ) {
	if ( typeof define === "function" && define.amd ) {

		// AMD. Register as an anonymous module.
		define([ "../datepicker" ], factory );
	} else {

		// Browser globals
		factory( jQuery.datepicker );
	}
}(function( datepicker ) {

datepicker.regional['kk'] = {
	closeText: 'Жабу',
	prevText: '&#x3C;Алдыңғы',
	nextText: 'Келесі&#x3E;',
	currentText: 'Бүгін',
	monthNames: ['Қаңтар','Ақпан','Наурыз','Сәуір','Мамыр','Маусым',
	'Шілде','Тамыз','Қыркүйек','Қазан','Қараша','Желтоқсан'],
	monthNamesShort: ['Қаң','Ақп','Нау','Сәу','Мам','Мау',
	'Шіл','Там','Қыр','Қаз','Қар','Жел'],
	dayNames: ['Жексенбі','Дүйсенбі','Сейсенбі','Сәрсенбі','Бейсенбі','Жұма','Сенбі'],
	dayNamesShort: ['жкс','дсн','ссн','срс','бсн','жма','снб'],
	dayNamesMin: ['Жк','Дс','Сс','Ср','Бс','Жм','Сн'],
	weekHeader: 'Не',
	dateFormat: 'dd.mm.yy',
	firstDay: 1,
	isRTL: false,
	showMonthAfterYear: false,
	yearSuffix: ''};
datepicker.setDefaults(datepicker.regional['kk']);

return datepicker.regional['kk'];

}));
