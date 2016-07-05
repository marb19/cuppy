<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="product"
	tagdir="/WEB-INF/tags/addons/sapinvoiceaddon/desktop"%>

<div class="invoiceBox invoice">
	<div class="headline">
		<spring:theme code="text.invoiceInformation"
			text="Invoice Information" />
	</div>
	<div class="span-7">
		<table>

			<thead>
				<c:if test="${invoiceData.documentNumber ne null}">
					<tr>
						<td><b><spring:theme
									code="text.account.invoice.invoiceNumber" text="Status" /></b></td>
						<td><b>:</b></td>
						<td><c:out value="${invoiceData.documentNumber}" /></td>
					</tr>
				</c:if>
<%-- 				<c:if test="${invoiceData.formattedAmount ne null}"> --%>
<!-- 					<tr> -->
<%-- 						<td><b><spring:theme --%>
<%-- 									code="text.account.invoice.invoiceTotal" text="Status" /></b></td> --%>
<!-- 						<td><b>:</b></td> -->
<%-- 						<td><c:out value="${invoiceData.formattedAmount}" /></td> --%>
<!-- 					</tr> -->
<%-- 				</c:if> --%>



				<c:if test="${invoiceData.invoiceAmount ne null}">
					<tr>
						<td><b><spring:theme
									code="text.account.invoice.invoiceTotal" text="Status" /></b></td>
						<td><b>:</b></td>
						<td><ycommerce:testId
								code="orderDetails_productItemPrice_label">
								<product:priceInvoice priceData="${invoiceData.invoiceAmount}"
									displayFreeForZero="true" />
							</ycommerce:testId></td>
					</tr>
				</c:if>
				
				<c:if test="${invoiceData.invoiceDate ne null}">
					<tr>
						<td><b><spring:theme
									code="text.account.invoice.invoiceDate" text="Status" /></b></td>
						<td><b>:</b></td>
						<td><c:out value="${invoiceData.invoiceDate}" /></td>
					</tr>
				</c:if>
				<c:if test="${invoiceData.deliveryNumber ne null}">
					<tr>
						<td><b><spring:theme
									code="text.account.invoice.deliveryNumber" text="Status" /></b></td>
						<td><b>:</b></td>
						<td><c:out value="${invoiceData.deliveryNumber}" /></td>
					</tr>
				</c:if>
				<c:if test="${invoiceData.deliveryDate ne null}">
					<tr>
						<td><b><spring:theme
									code="text.account.order.deliveryDate" text="Status" /></b></td>
						<td><b>:</b></td>
						 <td><c:out value="${invoiceData.deliveryDate}" /></td>
					</tr>
				</c:if>
				<c:if test="${invoiceData.orderNumbre ne null}">
					<tr>
						<td><b><spring:theme
									code="text.account.invoice.orderNumber" text="Status" /></b></td>
						<td><b>:</b></td>
						<td><c:out value="${invoiceData.orderNumbre}" /></td>
					</tr>
				</c:if>
				<c:if test="${invoiceData.orderDate ne null}">
					<tr>
						<td><b><spring:theme
									code="text.account.invoice.orderPlaced" text="Status" /></b></td>
						<td><b>:</b></td>
						<td><c:out value="${invoiceData.orderDate}" /></td>
					</tr>
				</c:if>
				<c:if test="${invoiceData.ourTaxNumber ne null}">
					<tr>
						<td><b><spring:theme
									code="text.account.order.ourTaxNumber" text="Status" /></b></td>
						<td><b>:</b></td>
						<td><c:out value="${invoiceData.ourTaxNumber}" /></td>
					</tr>
				</c:if>
				<c:if test="${invoiceData.taxNumber ne null}">
					<tr>
						<td><b><spring:theme code="text.account.order.taxNumber"
									text="Status" /></b></td>
						<td><b>:</b></td>
						<td><c:out value="${invoiceData.taxNumber}" /></td>
					</tr>
				</c:if>
			</thead>

		</table>
	</div>
</div>


<div class="invoiceCustomerDetailsBox customer">
	<div class="headline">
		<spring:theme code="text.customerInformation"
			text="Customer Information" />
	</div>
	<div class="span-6 last order-totals">

		<table>
			<thead>
				<c:if test="${invoiceData.customerNumber ne null}">
					<tr>
						<td><b><spring:theme
									code="text.account.invoice.customerNumber"
									text="Customer Number" /></b></td>
						<td><b>:</b></td>
						<td><c:out value="${invoiceData.customerNumber}" /></td>
					</tr>
				</c:if>
				<c:if test="${invoiceData.shipToAddress ne null}">
					<tr>
						<td><b><spring:theme
									code="text.account.invoice.shipToAddress"
									text="Shipping Address" /></b></td>

					</tr>
					<tr>
						<td colspan="3">${invoiceData.shipToAddress}</td>
					</tr>
				</c:if>
				<c:if test="${invoiceData.billToAddress ne null}">
					<tr>
						<td><b><spring:theme
									code="text.account.invoice.billToAddress"
									text="Billing Address" /></b></td>

					</tr>
					<tr>
						<td colspan="3">${invoiceData.billToAddress}</td>
					</tr>
				</c:if>
			</thead>
		</table>
	</div>
</div>

