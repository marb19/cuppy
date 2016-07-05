var ysapproductconfigb2baddonConstants = {};
var scrollListenerSet = false;
var sideBarHeight = 0;

ysapproductconfigb2baddonConstants.init = function(baseUrl, addToCartText,
		updateCartText, addedToCartText, updatedInCartText, noStock) {
	ysapproductconfigb2baddonConstants.baseUrl = baseUrl;
	ysapproductconfigb2baddonConstants.addToCartText = addToCartText;
	ysapproductconfigb2baddonConstants.updateCartText = updateCartText;
	ysapproductconfigb2baddonConstants.addedToCartText = addedToCartText;
	ysapproductconfigb2baddonConstants.updatedInCartText = updatedInCartText;
	ysapproductconfigb2baddonConstants.noStock = noStock;
	ysapproductconfigb2baddonConstants.ajaxRunning = false;
	ysapproductconfigb2baddonConstants.ajaxRunCounter = 0;
	ysapproductconfigb2baddonConstants.lastTarget = undefined;
};

// Solution for IE: 'startsWith' method is not supported in IE
if (!String.prototype.startsWith) {
  String.prototype.startsWith = function(searchString, position) {
    position = position || 0;
    return this.indexOf(searchString, position) === position;
  };
}

function getConfigureUrl() {
	return ysapproductconfigb2baddonConstants.baseUrl + "/config";
}
function getConfigureRedirectUrl() {
	return ysapproductconfigb2baddonConstants.baseUrl + "/redirectconfig";
}
function getAddToCartUrl() {
	return ysapproductconfigb2baddonConstants.baseUrl + "/addToCart";
}
/**
 * In order to escape any white spaces in the 'baseUrl'
 * it is necessary to execute JavaScript 'escape' function
 * because at this place the round trip does not take place.
 */
function getAddToCartPopupUrl() {
	return escape(ysapproductconfigb2baddonConstants.baseUrl + "/addToCartPopup");
}
function getResetUrl() {
	return ysapproductconfigb2baddonConstants.baseUrl + "/reset";
}
function getCopyUrl() {
	return ysapproductconfigb2baddonConstants.baseUrl + "/copy";
}

function getAddToCartText() {
	return ysapproductconfigb2baddonConstants.addToCartText;
}
function getUpdateCartText() {
	return ysapproductconfigb2baddonConstants.updateCartText;
}
function getAddedToCartText() {
	return ysapproductconfigb2baddonConstants.addedToCartText;
}
function getUpdatedInCartText() {
	return ysapproductconfigb2baddonConstants.updatedInCartText;
}

function registerStaticOnClickHandlers() {
	$('.product-details .name').on(
			'click keypress',
			function(e) {
				var element = $('#productName');
				var imageComponent = $('#configImage');
				toggleExpandCollapseStyle(element, imageComponent,
						"product-details-glyphicon-chevron");
				toggleImageGallery(e);
			});
	$('#cpqMenuArea').on('click', function(e) {
		openMenu();
		return false;
	});
}

function registerOnClickHandlers() {
	$('.cpq-group-title-close, .cpq-group-title-open')
			.on(
					'click keypress',
					function(e) {
						var groupTitleId = $(this).attr('id');
						var groupId = getGroupIdFromGroupTitleId(groupTitleId);
						toggleExpandCollapseStyle($(this), $(this).next(),
								"cpq-group-title");
						toggleGroup(e, groupId, false, groupTitleId);
						return false;
					});

	$('.cpq-conflict-link-to-config').on('click', function(e) {
		var csticFieldId = $(this).parent().attr('id');
		var csticId = getCsticIdFromCsticFieldId(csticFieldId);
		$("#autoExpand").val(false);
		var data = getSerializedForm('NAV_TO_CSTIC_IN_GROUP', csticId, true, "");
		firePost(doUpdatePost, [ e, data ]);

		return false;
	});
	
	$('.cpq-conflict-retractValue-button').on('click', function(e) {
		var csticFieldId = $(this).parent().attr('id');
		var csticId = getCsticIdFromCsticFieldId(csticFieldId);
		var targetId = jq("conflict." + csticId + ".retractValue");
		$(targetId).val(true);
		$("#autoExpand").val(false);
		var data = getSerializedForm('RETRACT_VALUE', '', true, "");
		firePost(doUpdatePost, [ e, data ]);

		return false;
	});

	$('.cpq-conflict-link').on('click', function(e) {
		var csticFieldId = $(this).parent().attr('id');
		var csticId = getCsticIdFromViolatedCsticFieldId(csticFieldId);
		$("#autoExpand").val(false);
		var data = getSerializedForm('NAV_TO_CSTIC_IN_CONFLICT', csticId, true, "");
		firePost(doUpdatePost, [ e, data ]);

		return false;
	});	
	
	$('.cpq-menu-node').on('click', function(e) {
		var menuNodeId = $(this).attr('id');
		var nodeId = getGroupIdFromMenuNodeId(menuNodeId);
		toggleNode(e, nodeId);
		return false;
	});


	$('.cpq-menu-conflict-header').on('click', function(e) {
		var menuNodeId = $(this).attr('id');
		var nodeId = getGroupIdFromMenuNodeId(menuNodeId);
		toggleNode(e, nodeId);
		return false;
	});
	
	$('.cpq-menu-leaf').on('click', function(e) {
		var menuNodeId = $(this).attr('id');
		$("#autoExpand").val($(this).hasClass("cpq-menu-warning"));
		
		var nodeId = getGroupIdFromMenuNodeId(menuNodeId);
		toggleGroupToDisplay(e, nodeId);
		removeMenu();
		return false;
	});

	$('.cpq-menu-conflict-node').on('click', function(e) {
		var menuNodeId = $(this).attr('id');
		$("#autoExpand").val($(this).hasClass("cpq-menu-warning"));
		
		var nodeId = getGroupIdFromMenuNodeId(menuNodeId);
		toggleGroupToDisplay(e, nodeId);
		removeMenu();
		return false;
	});	
	
	$('.cpq-previous-button').on('click', function(e) {
		previousButtonClicked(e);
		return false;
	});

	$('.cpq-next-button').on('click', function(e) {
		nextButtonClicked(e);
		return false;
	});

	$('.cpq-menu-icon-remove').on('click', function(e) {
		removeMenu();
		focusOnFirstInput();
		return false;
	});
}

function getCsticIdFromCsticFieldId(csticFieldId) {
	// Id has format "conflict.1-WCEM_MULTILEVEL._GEN.EXP_NO_USERS.conflicts", 
	// remove suffix and prefix 'conflict'
	var csticId = csticFieldId.replace(/conflict.(.*).conflicts/g, "$1");
	return csticId;
}

function getCsticIdFromViolatedCsticFieldId(csticFieldId) {
	// Id has format "1-WCEM_MULTILEVEL._GEN.EXP_NO_USERS.conflicts", 
	// remove suffix 'conflict'
	var csticId = csticFieldId.substring(0, csticFieldId.length - 10);
	return csticId;
}

function getCsticIdFromLableId(csticFieldId) {
	// Id has format "1-WCEM_MULTILEVEL._GEN.EXP_NO_USERS.lable", 
	// remove suffix 'lable'
	var csticId = csticFieldId.substring(0, csticFieldId.length - 6);
	return csticId;
}

function getGroupIdFromGroupTitleId(groupTitleId) {
	// Id has format GROUPID_title, remove suffix
	// '_title'
	var groupId = groupTitleId.substring(0, groupTitleId.length - 6);
	return groupId;
}

function getGroupIdFromMenuNodeId(menuNodeId) {
	// Id has format menuNode_GROUPID,remove prefix
	// 'menuNode_' or "menuLeaf_"
	var groupTitleId = menuNodeId.substring(9, menuNodeId.length);
	return groupTitleId;
}

function getCsticIdFromConflictCstic(csticId) {
	// Id has format conflict.1-WCEM_MULTILEVEL._GEN.EXP_NO_USERS
	// remove prefix "conflict."
	var cstic = csticId;
	if(csticId.startsWith("conflict.")) {
		cstic = csticId.substring(9, csticId.length);		
	}
	return cstic;
}

function toggleExpandCollapseStyle(element, elementToBlind, prefix, noBlind) {
	element.toggleClass(prefix + "-open");
	element.toggleClass(prefix + "-close");
	if (noBlind) {
		elementToBlind.toggle();
	} else {
		elementToBlind.slideToggle(100);
	}
}

function toggleImageGallery(e) {
	var targetElem = $('#hideImageGallery');
	targetElem.val(!(targetElem.val() == 'true'));
	var data = $('#configform').serialize();
	firePost(doUpdatePost, [ e, data ]);
}

function toggleGroup(e, groupId, forceExpand, focusId) {
	$("#autoExpand").val(false);
	var data = getSerializedForm('', '', forceExpand, groupId);
	firePost(doUpdatePost, [ e, data, focusId ]);
}

function toggleNode(e, nodeId) {
	$("#autoExpand").val(false);
	var data = getSerializedForm('MENU_NAVIGATION', '', false, '', nodeId);
	firePost(doUpdatePost, [ e, data ]);
}

function toggleGroupToDisplay(e, nodeId) {
	$('#groupIdToDisplay').val(nodeId);
	var data = getSerializedForm('MENU_NAVIGATION', '', true, nodeId);
	firePost(doUpdatePost, [ e, data, '##first##' ]);
}

function previousButtonClicked(e) {
	$("#autoExpand").val(false);
	var data = getSerializedForm('PREV_BTN', '', false);
	firePost(doUpdatePost, [ e, data, '##first##' ]);
}

function nextButtonClicked(e) {
	$("#autoExpand").val(false);
	var data = getSerializedForm('NEXT_BTN', '', false);
	firePost(doUpdatePost, [ e, data, '##first##' ]);
}

function firePost(fx, args) {
	ysapproductconfigb2baddonConstants.ajaxRunCounter++;
	waitToFirePost(fx, args);
}

function waitToFirePost(fx, args) {
	if (ysapproductconfigb2baddonConstants.ajaxRunning == true) {
		setTimeout(function() {
			waitToFirePost.call(this, fx, args)
		}, 100);
	} else {
		ysapproductconfigb2baddonConstants.ajaxRunning = true
		// setTimeout(function() {
		fx.apply(this, args);
		// }, 50);
	}
}

function saveFocus() {
	var id = undefined;
	if (document.activeElement) {
		id = document.activeElement.id;
	}
	return id;
}

function secureFocus(focusElement, counter){
	focusElement.focus();
	if(!focusElement.is(':focus') && counter < 20){
		counter++;
		setTimeout(function() {
			counter = counter++;
			secureFocus(focusElement, counter);
		}, 50);
	}
}	

function restoreFocus(focusElementId, srollTo, additionalOffset) {
	if (focusElementId && focusElementId.length > 0) {
		var focusElement = $(jq(focusElementId));
		restoreFocusOnElement(focusElement, srollTo, additionalOffset); 
	}
}

function restoreFocusOnElement(focusElement, srollTo, additionalOffset) {
		if (focusElement[0]) {
			secureFocus(focusElement, 0);
			if (srollTo) {
				var offset = focusElement.offset().top;
				var stickyBanner = $('.stickyBranding');
				var stickyBannerHeight = 0;
				if (stickyBanner.is(":visible")) {
					stickyBannerHeight = stickyBanner.outerHeight();
				}
				if (!additionalOffset) {
					additionalOffset = 0;
				}
				offset = offset - stickyBannerHeight - additionalOffset;
				$('html, body').animate({
					scrollTop : offset
				}, 100);
			}
		}
}

function getSerializedForm(cpqAction, focusId, forceExpand, groupIdToToggle, groupIdToToggleInSpecTree) {
	$("#cpqAction").val(cpqAction);
	$("#focusId").val(focusId);
	$("#forceExpand").val(forceExpand);
	$("#groupIdToToggle").val(groupIdToToggle);
	$("#groupIdToToggleInSpecTree").val(groupIdToToggleInSpecTree);
	var data = $('#configform').serialize();

	$("#cpqAction").val("");
	$("#focusId").val("");
	$("#forceExpand").val(false);
	$("#groupIdToToggle").val("");
	$("#groupIdToToggleInSpecTree").val("");

	return data;
}

function fireValueChangedPost(e) {
	var path = $(e.target).parents(".cpq-cstic").children("input:hidden").attr("name");
	var data = getSerializedForm('VALUE_CHANGED', path, false, "");
	setTimeout(function() {
		firePost(doUpdatePost, [ e, data ]);
	}, 50);

}

function registerAjax() {

	$('#configform').submit(function(e) {
		e.preventDefault();
	});

	// FF and Chrome does fire onChange when enter is pressed in input field and
	// additionally the onKeyPress event
	$('#configform :input').change(function(e) {
		if (duplicateEventProtection(e)) {
			fireValueChangedPost(e);
		}
		e.preventDefault();
		e.stopImmediatePropagation();
	});

	// IE does not fire onChange when enter is pressed in input field, only on
	// focus loss
	$('#configform :input').keypress(function(e) {
		if (e.which === 13) {
			if (duplicateEventProtection(e)) {
				fireValueChangedPost(e);
			}
			e.preventDefault();
			e.stopImmediatePropagation();
		}

	});

	$(document).ajaxError(function(event, xhr, settings, exception) {
		document.write(xhr.responseText);
	});

}

function duplicateEventProtection(e) {
	if (!ysapproductconfigb2baddonConstants.lastTarget
			|| e.target.id != ysapproductconfigb2baddonConstants.lastTarget.id
			|| e.target.value != ysapproductconfigb2baddonConstants.lastTarget.value
			|| e.target.checked != ysapproductconfigb2baddonConstants.lastTarget.checked) {
		ysapproductconfigb2baddonConstants.lastTarget = e.target;
		return true;
	}
	return false;

}

function focusOnIputByCsticKey(csticKey, additionalOffset) {

	var csticLabelId = csticKey + ".label";
	var csticKeyId = $(jq(csticKey + '.key'));
	var csticValueName = csticKeyId.attr('name');
	// replace .key with .value
	csticValueName = csticValueName.substring(0, csticValueName.length - 4);
	csticValueName = csticValueName + '.value';

	// foucs on Input if existent
	var nodeList = $("[name='" + csticValueName + "']");
	var length = nodeList.length;

	var focusElem;
	if (length > 0) {
		// length == 1: a simple input field / DDLB ==> focus on it;
		// length > 1: radio buttons ==> focus on first element
		focusElem = $(nodeList[0]);
	} else { // length = 0
		// maybe a check box List?
		var firstCheckboxName = csticValueName.slice(0,
				csticValueName.length - 5);
		firstCheckboxName += "domainvalues[0].selected";
		nodeList = $("[name='" + firstCheckboxName + "']");
		length = nodeList.length;
		// focus on first checkbox
		if (length > 0) {
			// restrict jquery search scope as checkboxValues might not be unique
			focusElem = $(nodeList[0], csticLabelId);
		} else {
			// no input at all, could be a read only field,
			// focus on Label instead
			focusElem = $(jq(csticLabelId));
		}
	}

	// scroll to label
	var label = $(jq(csticLabelId))
	if (!additionalOffset) {
		additionalOffset = 0;
	}
	var offset = focusElem.offset().top - label.offset().top + additionalOffset;
	restoreFocusOnElement(focusElem, true, offset)

}

function doUpdatePost(e, data, focusId) {
	$.post(getConfigureUrl(), data, function(response) {
		if (ysapproductconfigb2baddonConstants.ajaxRunCounter == 1) {
			var redirectTag = '<div id="redirectUrl">';
			var redirectIndex = response.indexOf(redirectTag);
			if (redirectIndex != -1) {
				redirectIndex = redirectIndex + redirectTag.length;

				var endTag = '</div>';
				var endIndex = response.indexOf(endTag);
				if (endIndex != -1) {
					var redirect = response.substring(redirectIndex, endIndex);
					window.location.replace(redirect);
				}
				return;
			}

			var focusElementId;
			var scrollTo;

			if (focusId) {
				focusElementId = focusId;
				scrollTo = true;
			} else {
				focusElementId = saveFocus();
				scrollTo = false;
			}
			updateContent(response);
			if (focusElementId == '##first##') {
				focusOnFirstInput();
			} else {
				restoreFocus(focusElementId, scrollTo);
			}

			doAfterPost();

		}
		ysapproductconfigb2baddonConstants.ajaxRunning = false;
		ysapproductconfigb2baddonConstants.ajaxRunCounter--;
	});

	e.preventDefault();
	e.stopPropagation();
}

function updateContent(response) {
	updateSlotContent(response, 'configContentSlot');
	updateSlotContent(response, 'configSidebarSlot');
	updateSlotContent(response, 'configBottombarSlot');
}

function updateSlotContent(response, slotName) {
	var newSlotContent = getNewSlotContent(response, slotName);
	$('#' + slotName).replaceWith(newSlotContent);
}

function getNewSlotContent(response, slotName) {
	var startTag = '<div id="start:' + slotName + '" />';
	var endTag = '<div id="end:' + slotName + '" />';
	var newContent = "";

	var startIndex = response.indexOf(startTag);
	if (startIndex != -1) {
		startIndex = startIndex + startTag.length;
		var endIndex = response.indexOf(endTag)
		if (endIndex != -1) {
			newContent = response.substring(startIndex, endIndex);
		}
	}

	return newContent;
}

function doAddToCartPost(e) {
	var form = $('#configform')[0];
	form.setAttribute("action", getAddToCartUrl());
	form.submit();
}

function doAfterPost() {
	scrollListenerSet = false;
	registerOnClickHandlers();
	checkAddToCartStatus();
	checkUpdateMode();
	registerAjax();
	registerLongTextMore();
	repositionAddToCart();
	if ($("#focusId").attr('value').length > 0) {
		focusOnIputByCsticKey($("#focusId").attr('value'));
		$("#focusId").val("");
	}

}

function repositionAddToCart() {
	// better condition, check screen width?

	var addToCartCol = $('#addToCartCol');
	if (addToCartCol[0]) {
		var width = addToCartCol.width() / addToCartCol.parent().width() * 100;
		var isInOwnCol = width < 40;
		addToCartCol.toggleClass('cpq-addToCart-posBottom', isInOwnCol);
	}
	var priceSummaryCol = $('#priceSummary');
	if (priceSummaryCol[0]) {
		var width = priceSummaryCol.width() / priceSummaryCol.parent().width()
				* 100;
		var isInOwnCol = width < 40
		priceSummaryCol.toggleClass('cpq-price-sum-slim', isInOwnCol);
	}

}

function registerLongTextMore() {
	$(".cpq-csticlabel-longtext-icon").on(
			'click',
			function(e) {
				var labelId = $(this).parent().attr('id');
				var csticId = getCsticIdFromLableId(labelId);
				var targetId = jq(csticId + ".showFullLongText");
				var cpqAction;
				if ($(targetId).val() != "true") {
					$(targetId).val("true");
					cpqAction = 'SHOW_FULL_LONG_TEXT';
					$(this).next().show();
				} else {
					$(targetId).val("false");
					cpqAction = 'HIDE_FULL_LONG_TEXT';
					$(this).next().hide();
				}
				csticId = getCsticIdFromConflictCstic(csticId);

				var data = getSerializedForm(cpqAction, csticId, false);
				firePost(doUpdatePost, [ e, data ]);
			});
}

function initConfigPage() {
	doAfterPost();
	registerStaticOnClickHandlers();
	ysapproductconfigb2baddonConstants.ajaxRunning = false;
	ysapproductconfigb2baddonConstants.ajaxRunCounter = 0;
	registerAddToCartButton();
	openColorboxAddToCartPopUp();

	$(window).resize(function() {
		repositionAddToCart();
	});

}

function registerAddToCartButton() {
	var postFunction = function(e) {
		firePost(doAddToCartPost, [ e ]);
	}
	// use delegated events (selector evaluated on click), as the button itself
	// is dynamically reloaded on POST,
	$(document).on('click', '.cpq-btn-addToCart',
			postFunction);
}

function addToCartPopupSetup() {

	$("#resolveLink").on('click', function(e) {
		ACC.colorbox.close();
		$("#autoExpand").val(true);
		data = $('#configform').serialize()
		firePost(doUpdatePost, [ e, data ]);
	});

	$("#sameConfigLink").on('click', function(e) {
		firePost(copyAndRedirect, [ e, getConfigureUrl() ]);
	});

	$("#resetLink").on('click', function(e) {
		firePost(resetAndRedirect, [ e, getConfigureUrl() ]);
	});

	$("#checkoutLink").on('click', function(e) {
		firePost(resetAndRedirect, [ e, '/cart' ]);
	});
}

function checkUpdateMode() {
	var btnText;
	if ($("#cartItemPK").prop('value') === '') {
		btnText = getAddToCartText();
	} else {
		btnText = getUpdateCartText();
	}

	$(".cpq-btn-addToCart").prop('textContent',
			btnText);
}

function resetAndRedirect(e, url) {
	var input = $('input[name=CSRFToken]');
	var token = null;
	var form = null;

	if (input && input.length != 0) {
		token = input.attr("value");
	}
	if (token) {
		form = $('<form action="' + getResetUrl()
				+ '" method="post" style="display: none;">'
				+ '<input type="text" name="url" value="' + url + '" />'
				+ '<input type="hidden" name="CSRFToken" value="' + token
				+ '" />' + '</form>');
	} else {
		form = $('<form action="' + getResetUrl()
				+ '" method="post" style="display: none;">'
				+ '<input type="text" name="url" value="' + url + '" />'
				+ '" />' + '</form>');
	}

	$('body').append(form);
	$(form).submit();

}

function copyAndRedirect(e, url) {
	var input = $('input[name=CSRFToken]');
	var token = null;
	var form = null;
	if (input && input.length != 0) {
		token = input.attr("value");
	}
	if (token) {
		form = $('<form action="' + getCopyUrl()
				+ '" method="post" style="display: none;">'
				+ '<input type="text" name="url" value="' + url + '" />'
				+ '<input type="hidden" name="CSRFToken" value="' + token
				+ '" />' + '</form>');
	} else {
		form = $('<form action="' + getCopyUrl()
				+ '" method="post" style="display: none;">'
				+ '<input type="text" name="url" value="' + url + '" />'
				+ '</form>');
	}
	$('body').append(form);
	$(form).submit();
}

function checkAddToCartStatus() {
	var addToCartButtons = $("#cpqAddToCartBtn");
	if ( errorExist 
			|| ysapproductconfigb2baddonConstants.noStock == "true") {
		addToCartButtons.attr("disabled", "disabled");
	} else {
		addToCartButtons.removeAttr("disabled");
	}

}

function jq(myid) {
	return "#" + myid.replace(/(:|\.|\[|\$|\])/g, "\\\$1");
}

function openColorboxAddToCartPopUp() {
	if (addToCartPopUpEnabled == true) {
		var url = getAddToCartPopupUrl();
		if (addedToCart == true) {
			cartName = getAddedToCartText();
		} else {
			cartName = getUpdatedInCartText();
		}

		ACC.colorbox.open(cartName, {
			href : url,
			maxWidth : "100%",
			width : "480px",
			initialWidth : "480px",
			onComplete : function() {
				var errorMessages = $("#cpqErrorBox");
				if (errorMessages.length) {
					var errorHeight = errorMessages.outerHeight();
					var cTopCenter = $("#cboxTopCenter");
					cTopCenter.append(errorMessages);
					cTopCenter.height(errorHeight);

					var cboxContent = $("#cboxContent");
					var contentHeight = cboxContent.height() - errorHeight;
					cboxContent.height(contentHeight);
				}
				addToCartPopupSetup();
				ACC.common.refreshScreenReaderBuffer();
			},
			onClosed : function() {
				$.colorbox.remove();
				ACC.common.refreshScreenReaderBuffer();
			}
		});
		var cbutton = $("#cboxClose");
		cbutton.removeAttr('id');
		cbutton.attr('id', 'cpqBoxClose');
	}
}

function openMenu() {
	var sideBar = $('#configSidebarSlot').parent();
	sideBar.removeClass("hidden-xs hidden-sm");

	var menu = $("#cpqMenuArea");
	menu.addClass("hidden-xs hidden-sm");

	var config = $('#configContentSlot').parent();
	config.addClass("hidden-xs hidden-sm");
	config.removeClass("col-xs-12 col-sm-12");

	// show footer header
	$('.main-footer').removeClass("hidden-xs hidden-sm");
	$('.main-header').removeClass("hidden-xs hidden-sm");
	$('.product-details-header').removeClass("hidden-xs hidden-sm");

	// hide footer header
	$('.main-footer').addClass("hidden-xs hidden-sm");
	$('.main-header').addClass("hidden-xs hidden-sm");
	$('#product-details-header').addClass("hidden-xs hidden-sm");

	restoreFocus($('.cpq-menu-leaf-selected').attr('id'), true, $(window)
			.height() / 4);
}

function removeMenu() {
	var sideBar = $('#configSidebarSlot').parent();
	sideBar.addClass("hidden-xs hidden-sm");

	var menu = $("#cpqMenuArea");
	menu.removeClass("hidden-xs hidden-sm");

	var config = $('#configContentSlot').parent();
	config.removeClass("hidden-xs hidden-sm");
	config.addClass("col-xs-12 col-sm-12");

	// show footer header
	$('.main-footer').removeClass("hidden-xs hidden-sm");
	$('.main-header').removeClass("hidden-xs hidden-sm");
	$('#product-details-header').removeClass("hidden-xs hidden-sm");
}

function focusOnFirstInput() {
	var csticKey = $('.cpq-groups label').attr('id');
	if(csticKey){
	csticKey = csticKey.substring(0, csticKey.length - 6);

	var offset = $('.cpq-groups label').offset().top
			- $('.cpq-groups').offset().top;
	focusOnIputByCsticKey(csticKey, offset);
	}
}

