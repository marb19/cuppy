<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cssConf" uri="/WEB-INF/tags/addons/ysapproductconfigb2baddon/responsive/configuration/sapproductconfig.tld"%>
<%@ taglib prefix="config" tagdir="/WEB-INF/tags/addons/ysapproductconfigb2baddon/responsive/configuration"%>


<div id="priceSummary"
	class="cpq-price-sum col-xs-12 col-sm-6 col-sm-offset-6">
	<c:if test="${showPriceDetails and !config.priceSummaryCollapsed}">
		<div id="priceSummarySubContent" class="cpq-price-sum-sub">
			<div class="cpq-price-label">
				<spring:theme code="sapproductconfig.pricesummary.label.baseprice" text="Base Price (Default)" />
			</div>
			<div id="basePriceValue" class="cpq-price-value">${config.pricing.basePrice.formattedValue}</div>
			<div class="cpq-price-label">
				<spring:theme code="sapproductconfig.pricesummary.label.selectedoptionsprice" text="Selected Options (Default)" />
			</div>
			<div id="selectedOptionsValue" class="cpq-price-value">${config.pricing.selectedOptions.formattedValue}</div>
		</div>
	</c:if>
	<div class="cpq-price-sum-total">
		<div class="cpq-price-label cpq-price-total">
			<spring:theme code="sapproductconfig.pricesummary.label.totalprice" text="Current Total (Default)" />
		</div>
		<div id="currentTotalValue" class="cpq-price-value cpq-price-total">${config.pricing.currentTotal.formattedValue}</div>

		<div class="cpq-price-label cpq-price-other">
			<spring:theme code="sapproductconfig.pricesummary.label.other" text="" />
		</div>
	</div>
</div>

<div id="addToCartCol" class="cpq-addToCart col-xs-12 col-sm-6 col-sm-offset-6">
		<config:addToCartButton/>
</div>

