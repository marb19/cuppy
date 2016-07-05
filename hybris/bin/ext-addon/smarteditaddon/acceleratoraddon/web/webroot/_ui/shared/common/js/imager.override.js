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
Imager.prototype.replaceImagesBasedOnScreenDimensions = function (image) {
    var src,availableWidths = [],srcARRAY,cwidth;

    var eMedia = $.parseJSON($(image).attr("data-media"));

    $.each(eMedia, function(key, value) {
        availableWidths.push(parseInt(key));
    });

    cwidth = Imager.getClosestValue($(image).parent().width(), $.extend([],availableWidths));

    if(image.src == eMedia[cwidth]){return;}
    image.src = eMedia[cwidth];
};