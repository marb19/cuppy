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
/* Written by Leonildo Costa Silva (leocsilva@gmail.com). */
(function( factory ) {
	if ( typeof define === "function" && define.amd ) {

		// AMD. Register as an anonymous module.
		define([ "../datepicker" ], factory );
	} else {

		// Browser globals
		factory( jQuery.datepicker );
	}
}(function( datepicker ) {

datepicker.regional['pt-BR'] = {
	closeText: 'Fechar',
	prevText: '&#x3C;Anterior',
	nextText: 'Próximo&#x3E;',
	currentText: 'Hoje',
	monthNames: ['Janeiro','Fevereiro','Março','Abril','Maio','Junho',
	'Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'],
	monthNamesShort: ['Jan','Fev','Mar','Abr','Mai','Jun',
	'Jul','Ago','Set','Out','Nov','Dez'],
	dayNames: ['Domingo','Segunda-feira','Terça-feira','Quarta-feira','Quinta-feira','Sexta-feira','Sábado'],
	dayNamesShort: ['Dom','Seg','Ter','Qua','Qui','Sex','Sáb'],
	dayNamesMin: ['Dom','Seg','Ter','Qua','Qui','Sex','Sáb'],
	weekHeader: 'Sm',
	dateFormat: 'dd/mm/yy',
	firstDay: 0,
	isRTL: false,
	showMonthAfterYear: false,
	yearSuffix: ''};
datepicker.setDefaults(datepicker.regional['pt-BR']);

return datepicker.regional['pt-BR'];

}));
