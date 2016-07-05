ACC.invoice = {

	$errorMessage : $('.acc_summary div#errorMessage'),

	bindAll : function() {
		this.bindInvoicePDF();
	},

	bindInvoicePDF : function() {
		$(document).on("click", '.invoiceClass', function(event) {
			$('#globalMessages').html("");
			ACC.invoice.$errorMessage.hide();
			ACC.invoice.doAjaxCallForInvoice(this, event);

		});
	},

	doAjaxCallForInvoice : function(element, event) {
		event.preventDefault();
		var invoiceCode = $(element).data("invoicenumber");
		var erorrmsg= $(element).data("errormsg");
		var options = {
			url : ACC.config.contextPath
					+ '/my-company/organization-management/invoicedocument/invoicedownload',
			data : {
				invoiceCode : invoiceCode
			},
			type : 'GET',
			contentType : 'application/pdf',
			sync : true,
			success : function(data) {
				window
				.open(
						this.url,
						'_blank',
						"width=1024,height=768,resizable=yes,scrollbars=yes,toolbar=no,location=no,directories=no,status=no,menubar=no,copyhistory=no");
			},
			error : function(data, textStatus, ex) {
				ACC.invoice.$errorMessage.text(erorrmsg+' : '+invoiceCode).removeClass("hidden").css('color','#c90400').show();
				var body = $("html, body");
				body.stop().animate({
					scrollTop : 0
				}, '500', 'swing', function() {

				});

			}

		};
		$(this).ajaxSubmit(options);
		return false;
	}
};

$(document).ready(function() {
	ACC.invoice.bindAll();
});
