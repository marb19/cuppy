<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="checkout" tagdir="/WEB-INF/tags/responsive/checkout" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/responsive/checkout/multi" %>
<%@ taglib prefix="b2b-multi-checkout" tagdir="/WEB-INF/tags/addons/b2bacceleratoraddon/responsive/checkout/multi" %>
<%@ taglib prefix="addon-multi-checkout" tagdir="/WEB-INF/tags/addons/ysapordermgmtb2baddon/responsive/checkout/multi" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<template:page pageTitle="${pageTitle}" hideHeaderLinks="true">

	<div id="globalMessages">
		<common:globalMessages/>
	</div>

	<multi-checkout:checkoutSteps checkoutSteps="${checkoutSteps}" progressBarId="${progressBarId}">
	<jsp:body>
	<div class="span-14 append-1">
		<div id="checkoutContentPanel" class="clearfix">
			<div class="headline"><spring:theme code="checkout.multi.paymentType.stepHeader"/></div>
			<div class="required right"><spring:theme code="form.required" text="Fields marked * are required"/></div>
			<div class="description"><p><spring:theme code="checkout.multi.paymentType.selectPaymentTypeMessage"/></p></div>
			<addon-multi-checkout:paymentTypeForm/>
		</div>
	</div>
	</jsp:body>
	</multi-checkout:checkoutSteps>
	<addon-multi-checkout:checkoutOrderDetailsB2B cartData="${cartData}" showShipDeliveryEntries="true" showPickupDeliveryEntries="false" showTax="true"/>
	<cms:pageSlot position="SideContent" var="feature" element="div" class="span-24 side-content-slot cms_disp-img_slot">
		<cms:component component="${feature}"/>
	</cms:pageSlot>

</template:page>
