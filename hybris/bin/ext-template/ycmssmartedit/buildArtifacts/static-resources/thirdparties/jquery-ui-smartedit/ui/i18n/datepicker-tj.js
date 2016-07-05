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
/* Written by Abdurahmon Saidov (saidovab@gmail.com). */
(function( factory ) {
	if ( typeof define === "function" && define.amd ) {

		// AMD. Register as an anonymous module.
		define([ "../datepicker" ], factory );
	} else {

		// Browser globals
		factory( jQuery.datepicker );
	}
}(function( datepicker ) {

datepicker.regional['tj'] = {
	closeText: 'Идома',
	prevText: '&#x3c;Қафо',
	nextText: 'Пеш&#x3e;',
	currentText: 'Имрӯз',
	monthNames: ['Январ','Феврал','Март','Апрел','Май','Июн',
	'Июл','Август','Сентябр','Октябр','Ноябр','Декабр'],
	monthNamesShort: ['Янв','Фев','Мар','Апр','Май','Июн',
	'Июл','Авг','Сен','Окт','Ноя','Дек'],
	dayNames: ['якшанбе','душанбе','сешанбе','чоршанбе','панҷшанбе','ҷумъа','шанбе'],
	dayNamesShort: ['якш','душ','сеш','чор','пан','ҷум','шан'],
	dayNamesMin: ['Як','Дш','Сш','Чш','Пш','Ҷм','Шн'],
	weekHeader: 'Хф',
	dateFormat: 'dd.mm.yy',
	firstDay: 1,
	isRTL: false,
	showMonthAfterYear: false,
	yearSuffix: ''};
datepicker.setDefaults(datepicker.regional['tj']);

return datepicker.regional['tj'];

}));
