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
$(document).ready(function(){
	$('.y-buttonClose').click(function() {
		var layer = $(this).parents(".y-on-click-show") 
		layer.slideUp(150, function() {	
			layer.parents(".y-mouseout-view").mouseleave(function() {		
				layer.css("display", "block");
			});
		});
	});
	
	$(".y-thumbnailContainer").click(function() {
		var menuLayer = $(this).siblings(".y-on-click-show")
		menuLayer.fadeIn(150);
	});	
});
