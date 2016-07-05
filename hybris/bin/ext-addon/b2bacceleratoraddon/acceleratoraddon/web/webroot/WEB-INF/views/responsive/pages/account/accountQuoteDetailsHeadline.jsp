<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/addons/b2bacceleratoraddon/responsive/order" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<spring:url value="/my-account/my-quotes" var="quotesURL" htmlEscape="false"/>

<div class="back-link">
	<a href="${quotesURL}">
		<button type="button" class="orderTopBackBtn">
			<span class="glyphicon glyphicon-chevron-left"></span>
		</button>
	</a>
	<span class="label"><spring:theme code="text.quotes.orderDetails.label" /></span>
</div>
