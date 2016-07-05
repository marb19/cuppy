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
/* Written by Michael Dawart. */
(function( factory ) {
	if ( typeof define === "function" && define.amd ) {

		// AMD. Register as an anonymous module.
		define([ "../datepicker" ], factory );
	} else {

		// Browser globals
		factory( jQuery.datepicker );
	}
}(function( datepicker ) {

datepicker.regional['hi'] = {
	closeText: 'बंद',
	prevText: 'पिछला',
	nextText: 'अगला',
	currentText: 'आज',
	monthNames: ['जनवरी ','फरवरी','मार्च','अप्रेल','मई','जून',
	'जूलाई','अगस्त ','सितम्बर','अक्टूबर','नवम्बर','दिसम्बर'],
	monthNamesShort: ['जन', 'फर', 'मार्च', 'अप्रेल', 'मई', 'जून',
	'जूलाई', 'अग', 'सित', 'अक्ट', 'नव', 'दि'],
	dayNames: ['रविवार', 'सोमवार', 'मंगलवार', 'बुधवार', 'गुरुवार', 'शुक्रवार', 'शनिवार'],
	dayNamesShort: ['रवि', 'सोम', 'मंगल', 'बुध', 'गुरु', 'शुक्र', 'शनि'],
	dayNamesMin: ['रवि', 'सोम', 'मंगल', 'बुध', 'गुरु', 'शुक्र', 'शनि'],
	weekHeader: 'हफ्ता',
	dateFormat: 'dd/mm/yy',
	firstDay: 1,
	isRTL: false,
	showMonthAfterYear: false,
	yearSuffix: ''};
datepicker.setDefaults(datepicker.regional['hi']);

return datepicker.regional['hi'];

}));
