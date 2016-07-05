<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="invoiceBoxes clearfix">
	<div class="invoiceBoxOtherInformation address">
		<div class="headline">
			<spring:theme code="text.invoice.otherInformation" text="Other Information" />
		</div>
		<div class="span-7">
			<table class="invoiceConditions">
			<thead>
				<tr>
					<td colspan="4"><b><spring:theme 
						code="text.account.invoice.order.conditions" text="Status" /></b>
					</td>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><b><spring:theme
								code="text.account.invoice.termsOfPayment" text="Terms of Payment" /></b></td>
					<td colspan="3"><c:out value="${invoiceData.termsOfPayment}" /></td>
				</tr>
				<tr>
					<td><b><spring:theme
								code="text.account.invoice.termsOfDelivery" text="Terms of Delivery" /></b></td>
					<td colspan="3"><c:out value="${invoiceData.termsOfDelivery}" /></td>
				</tr>
			</tbody>
			<thead>
				<tr>
					<td colspan="2" class="invoiceWeightVolume"><b><spring:theme 
						code="text.account.invoice.order.weight" text="Weight And Volume" /></b>
					</td>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><b><spring:theme
								code="text.account.invoice.order.netWeight" text="Net Weight" /></b></td>
					<td><c:out value="${invoiceData.netWeight}" /></td>
				</tr>
				<tr>
					<td><b><spring:theme
								code="text.account.invoice.order.grossWeight" text="Gross Weight" /></b></td>
					<td><c:out value="${invoiceData.grossWeight}" /></td>
				</tr>
			</tbody>
			
		</table>
		</div>
	</div>
</div>