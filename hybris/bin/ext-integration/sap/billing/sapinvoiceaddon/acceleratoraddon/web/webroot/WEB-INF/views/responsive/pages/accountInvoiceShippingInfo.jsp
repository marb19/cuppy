<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="invoice"
	tagdir="/WEB-INF/tags/addons/sapinvoiceaddon/responsive/invoice"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<spring:htmlEscape defaultHtmlEscape="true" />

<div class="account-orderdetail well well-tertiary">
	<ycommerce:testId code="orderDetails_paymentDetails_section">
		<div class="well-content">
			<c:if test="${invoiceData.billToAddress ne null}">
				<div class="col-sm-6 order-billing-address">
					<div class="label-order">
						<spring:theme code="text.account.invoice.billToAddress" />
					</div>
					<div class="value-order">${invoiceData.billToAddress}</div>
				</div>
			</c:if>
			<c:if test="${invoiceData.shipToAddress ne null}">
				<div class="col-sm-6 order-billing-address">
					<div class="label-order">
						<spring:theme code="text.account.invoice.shipToAddress" />
					</div>
					<div class="value-order">${invoiceData.shipToAddress}</div>
				</div>
			</c:if>
		</div>
	</ycommerce:testId>
</div>

