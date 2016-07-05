/**
 * @Override the old function in base accelerator
 */
ACC.address.displayCountrySpecificAddressForm = function (options, callback)
{
	options.addressCode = $("input#addressId").val();
	$.ajax({
		url: ACC.config.encodedContextPath + "/my-account/addressform",
		async: true,
		data: options,
		dataType: "html",
		beforeSend: function ()
		{
			$("div#i18nAddressForm").html(ACC.address.spinner);
		}
	}).done(function (data){
		$("div#i18nAddressForm").html($(data).html());
		if (typeof callback == 'function')
		{
			callback.call();
		}
	}).always(function() {
		ACC.address.initChineseAddressForm();
	});
};

/**
 * reload city items after region changed
 */
ACC.address.onRegionChanged = function() {
	$("select#address\\.region").change(function (){
		var url = ACC.config.encodedContextPath + '/cn-address/region/' + $(this).val();
		$.getJSON(url, function(data){
			var $cities = $('select#address\\.townCity'), defaultOption= $('select#address\\.townCity > option:first');
			$cities.empty().append($(defaultOption).removeAttr("disabled").prop("selected", "selected"));
			$.each(data, function(item) {
				$cities.append($("<option />").val(this.code).text(this.name));
		    });
			
			$('#address\\.district > option:first').removeAttr("disabled").attr('selected','selected');			
			$('#address\\.district > option:gt(0)').remove();
		});	
	});	
};

/**
 * reload district items after city changed
 */
ACC.address.onCityChanged = function() {
	$("select#address\\.townCity").change(function (){
		var cityCode = $(this).val();
		if(!cityCode.isEmpty()) {
			var url = ACC.config.encodedContextPath + '/cn-address/city/' + cityCode;
			$.getJSON(url, function (data)	{
				var $districts = $('select#address\\.district'), defaultOption= $('select#address\\.district > option')[0];
				$districts.empty().append($(defaultOption).attr("selected", "selected").removeAttr("disabled"));
				$.each(data, function(item) {
					$districts.append($("<option />").val(this.code).text(this.name));
			    });
			});
			$(this).find("option:first").prop("disabled", true);
		}
	});	
};

/**
 * disable the first option after district changed
 */
ACC.address.onDistrictChanged = function() {
	$("select#address\\.district").change(function (){
		if(!$(this).val().isEmpty()) {
			$(this).find("option:first").prop("disabled", true);
		}
	});	
};

/**
 * cannot set the attribute in Hybris' tag, so use JavaScript
 */
ACC.address.setMaxLengthForCellPhone = function() {
	$("input#address\\.cellphone").attr("maxlength", "16");
}

/**
 * init Chinese address form
 */
ACC.address.initChineseAddressForm = function() {
	ACC.address.onRegionChanged();
	ACC.address.onCityChanged();
	ACC.address.onDistrictChanged();
	ACC.address.setMaxLengthForCellPhone();
}

/**
 * register change event on region/city once DOM (re)loaded
 */
$(function() {
	ACC.address.initChineseAddressForm();
});