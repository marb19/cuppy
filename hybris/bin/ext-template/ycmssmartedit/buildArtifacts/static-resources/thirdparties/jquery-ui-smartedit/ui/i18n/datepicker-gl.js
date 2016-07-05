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
/* Translated by Jorge Barreiro <yortx.barry@gmail.com>. */
(function( factory ) {
	if ( typeof define === "function" && define.amd ) {

		// AMD. Register as an anonymous module.
		define([ "../datepicker" ], factory );
	} else {

		// Browser globals
		factory( jQuery.datepicker );
	}
}(function( datepicker ) {

datepicker.regional['gl'] = {
	closeText: 'Pechar',
	prevText: '&#x3C;Ant',
	nextText: 'Seg&#x3E;',
	currentText: 'Hoxe',
	monthNames: ['Xaneiro','Febreiro','Marzo','Abril','Maio','Xuño',
	'Xullo','Agosto','Setembro','Outubro','Novembro','Decembro'],
	monthNamesShort: ['Xan','Feb','Mar','Abr','Mai','Xuñ',
	'Xul','Ago','Set','Out','Nov','Dec'],
	dayNames: ['Domingo','Luns','Martes','Mércores','Xoves','Venres','Sábado'],
	dayNamesShort: ['Dom','Lun','Mar','Mér','Xov','Ven','Sáb'],
	dayNamesMin: ['Do','Lu','Ma','Mé','Xo','Ve','Sá'],
	weekHeader: 'Sm',
	dateFormat: 'dd/mm/yy',
	firstDay: 1,
	isRTL: false,
	showMonthAfterYear: false,
	yearSuffix: ''};
datepicker.setDefaults(datepicker.regional['gl']);

return datepicker.regional['gl'];

}));
