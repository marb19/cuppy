<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="product"
	tagdir="/WEB-INF/tags/addons/sapinvoiceaddon/desktop"%>

<div class="invoiceOrderTotalBox order">
	<div class="span-6 last order-totals">

		<table>
			<thead>
				<c:if test="${invoiceData.invoiceItemsData ne null}">
				
					<tr>
						<td><b><spring:theme
									code="text.account.invoice.totalNet"
									text="Net Invoice Value" /></b></td>
						<td><b>:</b></td>
						<td><ycommerce:testId
								code="orderDetails_productItemPrice_label">
								<product:priceInvoice priceData="${invoiceData.invoiceItemsData.netValue}"
									displayFreeForZero="true" />
							</ycommerce:testId></td>
					</tr>
					
					<tr>
						<td><b><spring:theme
									code="text.account.invoice.overallTax"
									text="Overall Tax" /></b></td>
						<td><b>:</b></td>
						<td><ycommerce:testId
								code="orderDetails_productItemPrice_label">
								<product:priceInvoice priceData="${invoiceData.invoiceItemsData.overAllTax}"
									displayFreeForZero="true" />
							</ycommerce:testId></td>
					</tr>
					
					<tr>
						<td><b><spring:theme
									code="text.account.invoice.grandTotal"
									text="Grand Total" /></b></td>
						<td><b>:</b></td>
						<td><ycommerce:testId
								code="orderDetails_productItemPrice_label">
								<product:priceInvoice priceData="${invoiceData.invoiceItemsData.grandTotal}"
									displayFreeForZero="true" />
							</ycommerce:testId></td>
					</tr>
					
				</c:if>
			</thead>
		</table>
	</div>
</div>

