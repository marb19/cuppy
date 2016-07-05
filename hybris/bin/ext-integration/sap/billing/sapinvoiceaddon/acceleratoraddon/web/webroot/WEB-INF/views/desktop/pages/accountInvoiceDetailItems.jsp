<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="grid" tagdir="/WEB-INF/tags/desktop/grid"%>
<%@ taglib prefix="product"
	tagdir="/WEB-INF/tags/addons/sapinvoiceaddon/desktop"%>

<div>
	<div class="invoiceItemList">
		<div class="headline">
			<div class="invoice_delivery">
				<spring:theme code="text.account.invoice.delivery.items" />
			</div>
			<div class="invoice_currency">
				<spring:theme code="text.account.invoice.currency"
					arguments="${invoiceData.currency.isocode}" />
			</div>

		</div>

		<table class="orderListTable">
			<thead>
				<tr>
					<th id="header2"><spring:theme
							code="text.account.invoice.posno" /></th>
					<th id="header3"><spring:theme
							code="test.account.invoice.itemno" /></th>
					<th id="header4"><spring:theme
							code="text.account.invoice.itemDesc" /></th>
					<th id="header5"><spring:theme code="text.quantity" /></th>
					<th id="header6"><spring:theme
							code="text.account.invoice.grossPrice" /></th>
					<th id="header7"><spring:theme
							code="text.account.invoice.netPrice" /></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${invoiceData.invoiceItemsData.entries}"
					var="entry" varStatus="loop">
					<tr class="item">

						<td headers="header2" class="thumb">${entry.posNo}</td>
						
						<c:choose>
							<c:when test="${entry.product.url ne null}">
								<c:url value="${entry.product.url}" var="productUrl" />
								<td headers="header3" class="details"><ycommerce:testId
										code="orderDetails_productName_link">
										<div class="itemName">
											<a href="${entry.product.purchasable ? productUrl : ''}">${entry.product.code}</a>
										</div>
									</ycommerce:testId>
							</c:when>
							<c:otherwise>
								<td headers="header3" class="details"><ycommerce:testId
										code="orderDetails_productName_link">
										<div class="itemName">${entry.product.code}</div>
									</ycommerce:testId></td>
							</c:otherwise>
						</c:choose>
						<td headers="header4" class="thumb">${entry.itemDesc}</td>


						<td headers="header5" class="quantity"><span class="qty">
								<c:out value="${entry.quantity}" />
						</span></td>

						<td headers="header6"><ycommerce:testId
								code="orderDetails_productItemPrice_label">
								<product:priceInvoice priceData="${entry.grossPrice}"
									displayFreeForZero="true" />
							</ycommerce:testId></td>

						<td headers="header7" class="total"><ycommerce:testId
								code="orderDetails_productTotalPrice_label">
								<product:priceInvoice priceData="${entry.netPrice}"
									displayFreeForZero="true" />
							</ycommerce:testId></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

	</div>
</div>
