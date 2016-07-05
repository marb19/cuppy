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
/* Written by Pavel Selitskas <p.selitskas@gmail.com> */
(function( factory ) {
	if ( typeof define === "function" && define.amd ) {

		// AMD. Register as an anonymous module.
		define([ "../datepicker" ], factory );
	} else {

		// Browser globals
		factory( jQuery.datepicker );
	}
}(function( datepicker ) {

datepicker.regional['be'] = {
	closeText: 'Зачыніць',
	prevText: '&larr;Папяр.',
	nextText: 'Наст.&rarr;',
	currentText: 'Сёньня',
	monthNames: ['Студзень','Люты','Сакавік','Красавік','Травень','Чэрвень',
	'Ліпень','Жнівень','Верасень','Кастрычнік','Лістапад','Сьнежань'],
	monthNamesShort: ['Сту','Лют','Сак','Кра','Тра','Чэр',
	'Ліп','Жні','Вер','Кас','Ліс','Сьн'],
	dayNames: ['нядзеля','панядзелак','аўторак','серада','чацьвер','пятніца','субота'],
	dayNamesShort: ['ндз','пнд','аўт','срд','чцв','птн','сбт'],
	dayNamesMin: ['Нд','Пн','Аў','Ср','Чц','Пт','Сб'],
	weekHeader: 'Тд',
	dateFormat: 'dd.mm.yy',
	firstDay: 1,
	isRTL: false,
	showMonthAfterYear: false,
	yearSuffix: ''};
datepicker.setDefaults(datepicker.regional['be']);

return datepicker.regional['be'];

}));
