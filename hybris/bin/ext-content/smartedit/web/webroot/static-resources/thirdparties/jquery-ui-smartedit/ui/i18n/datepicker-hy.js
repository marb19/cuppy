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
/* Written by Levon Zakaryan (levon.zakaryan@gmail.com)*/
(function( factory ) {
	if ( typeof define === "function" && define.amd ) {

		// AMD. Register as an anonymous module.
		define([ "../datepicker" ], factory );
	} else {

		// Browser globals
		factory( jQuery.datepicker );
	}
}(function( datepicker ) {

datepicker.regional['hy'] = {
	closeText: 'Փակել',
	prevText: '&#x3C;Նախ.',
	nextText: 'Հաջ.&#x3E;',
	currentText: 'Այսօր',
	monthNames: ['Հունվար','Փետրվար','Մարտ','Ապրիլ','Մայիս','Հունիս',
	'Հուլիս','Օգոստոս','Սեպտեմբեր','Հոկտեմբեր','Նոյեմբեր','Դեկտեմբեր'],
	monthNamesShort: ['Հունվ','Փետր','Մարտ','Ապր','Մայիս','Հունիս',
	'Հուլ','Օգս','Սեպ','Հոկ','Նոյ','Դեկ'],
	dayNames: ['կիրակի','եկուշաբթի','երեքշաբթի','չորեքշաբթի','հինգշաբթի','ուրբաթ','շաբաթ'],
	dayNamesShort: ['կիր','երկ','երք','չրք','հնգ','ուրբ','շբթ'],
	dayNamesMin: ['կիր','երկ','երք','չրք','հնգ','ուրբ','շբթ'],
	weekHeader: 'ՇԲՏ',
	dateFormat: 'dd.mm.yy',
	firstDay: 1,
	isRTL: false,
	showMonthAfterYear: false,
	yearSuffix: ''};
datepicker.setDefaults(datepicker.regional['hy']);

return datepicker.regional['hy'];

}));
