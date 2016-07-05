<%
   response.setHeader( "Pragma", "no-cache" );
   response.setHeader( "Cache-Control", "no-cache" );
   response.setDateHeader( "Expires", 0 );
%>
<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="conf" uri="sapproductconfig.tld"%>

<%@ attribute name="bindResult" required="true" type="org.springframework.validation.BindingResult"%>
<%@ attribute name="addMode" required="true" type="java.lang.Boolean"%>
<%@ attribute name="config" required="true" type="de.hybris.platform.sap.productconfig.facades.ConfigurationData"%>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData"%>


<c:set var="conflicts" value="${conf:conflictErrorWithoutDuplicates(bindResult)}" />
<c:set var="missingMandatoryFields" value="${conf:mandatoryFieldError(bindResult)}" />
<c:set var="conflictCount" value="${fn:length(conflicts)}" />
<c:set var="mandatoryCount" value="${fn:length(missingMandatoryFields)}" />
<c:set var="warningsCount" value="${conflictCount + mandatoryCount}" />
<c:set var="warningsExist" value="${warningsCount gt 0}" />

<c:if test="${warningsExist}">
	<div id="cpqErrorBox">
		<div class="cpq-error-sign">&#xe085;</div>
		<div class="cpq-error-message-cart-popup">
			<span><spring:message code="sapproductconfig.addtocartpopup.entrytext.conflicts.responsive" arguments="${warningsCount}"
					text="{0} issues must be resolved before checkout." />&nbsp;<a
				id="resolveLink"><spring:message code="sapproductconfig.addtocartpopup.resolve.button" text="Resolve Issues Now" /></a></span>
		</div>
	</div>
</c:if>

<div id="addToCartLayer" class="add-to-cart">

	<div class="main">
		<div class="add-to-cart-item">
			<div class="thumb">
				<product:productPrimaryImage product="${product}" format="cartIcon" />
			</div>
			<div class="details">
				<div class="name">${product.name}</div>
				<span class="sku">ID ${product.code}</span>
				<div id="basePriceValue" class="cpq-price-value price">${config.pricing.currentTotal.formattedValue}</div>
			</div>
		</div>

		<div id="addToCartLayer" class="add-to-cart">
			<a id="checkoutLink" class="btn btn-primary btn-block add-to-cart-button cpq-popup-link"><spring:message code="cart.checkout"
					text="Checkout" /></a>
			<a id="resetLink" class="btn btn-default btn-block js-mini-cart-close-button cpq-popup-link"><spring:message
					code="sapproductconfig.addtocartpopup.anotherFromNew" text="Configure Another From New" /></a> <a id="sameConfigLink"
				class="btn btn-default btn-block js-mini-cart-close-button cpq-popup-link"><spring:message
					code="sapproductconfig.addtocartpopup.anotherSaveValue" text="Configure Another Save Values" /></a>
		</div>

	</div>
</div>