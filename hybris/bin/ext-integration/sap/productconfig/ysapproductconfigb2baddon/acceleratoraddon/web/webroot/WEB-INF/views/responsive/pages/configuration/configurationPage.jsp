<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>

<spring:theme code="text.addToCart" var="addToCartText" text="Add To Cart" />
<spring:theme code="sapproductconfig.addtocartpopup.title" var="addedToCartText" text="Added to Your Shopping Cart" />
<spring:theme code="sapproductconfig.updateCart" var="updateCartText" text="Update Cart" />
<spring:theme code="sapproductconfig.addtocartpopup.update.title" var="updatedInCartText" text="Shopping Cart Updated" />

<template:page pageTitle="${pageTitle}">

	<jsp:body>
		<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">

		<c:if test="${not empty message}">
			<spring:theme code="${message}" />
		</c:if>
		 
		<div id="globalMessages">
			<common:globalMessages />
		</div>		
		
		<div id="product-details-header">
			<c:if test="${!config.singleLevel}"> 
				<div id="cpqMenuArea" class="hidden-md hidden-lg cpq-menu-icon-area">
					<div id="menuIcon" class="cpq-menu-icon"></div>
				</div>
			</c:if>
			<div class="row">
				<div class="cpq-page col-xs-12">
					<cms:pageSlot position="ConfigTitle" var="comp" element="div">
						<cms:component component="${comp}" />	
					</cms:pageSlot>		
				</div>
			</div>
			<cms:pageSlot position="ConfigHeader" var="comp" element="div">
				<cms:component component="${comp}" />
			</cms:pageSlot>
		</div>
		
		<cms:pageSlot position="Section1" var="comp" element="div">
			<cms:component component="${comp}" />
		</cms:pageSlot>
		
		<c:set var="contentColStyle" value="cpq-page col-xs-12 col-lg-8" />
		<c:if test="${!config.singleLevel}"> 
			<c:set var="contentColStyle" value="${contentColStyle} col-sm-12 col-md-8" />
		</c:if>
		
		<div id="dynamicConfigContent">
			
			<div class="row">		
				<div class="${contentColStyle}">				
					<cms:pageSlot id="configContentSlot" position="ConfigContent" var="feature" element="div">
						<cms:component component="${feature}" />
					</cms:pageSlot>
					<cms:pageSlot id="configBottombarSlot" position="ConfigBottombar" var="feature" element="div">
						<cms:component component="${feature}" />
					</cms:pageSlot>
				</div>
				
				<div class="cpq-page hidden-xs hidden-sm col-md-4 col-lg-4">
					<cms:pageSlot id="configSidebarSlot" position="ConfigSidebar" var="feature" element="div">
						<cms:component component="${feature}" />
					</cms:pageSlot>
				</div>		
			</div>
			
		</div>
	

		<cms:pageSlot position="Section2" var="feature" element="div">
			<cms:component component="${feature}" />
		</cms:pageSlot>
		<cms:pageSlot position="Section3" var="feature" element="div">
			<cms:component component="${feature}" />
		</cms:pageSlot>
		
		<cms:pageSlot position="Section4" var="feature" element="div">
			<cms:component component="${feature}" />
		</cms:pageSlot>
	
	
	</jsp:body>
</template:page>
<spring:url value="${product.code}" var="baseUrl" />

<c:set var="noStock">true</c:set>
<c:if test="${product.purchasable and product.stock.stockLevelStatus.code ne 'outOfStock' }">
	<c:set var="noStock">false</c:set>
</c:if>
<c:if
	test="${product.variantType ne null and product.purchasable eq false and product.stock.stockLevelStatus.code ne 'outOfStock'}">
	<c:set var="noStock">false</c:set>
</c:if>

<script type="text/javascript">
	ysapproductconfigb2baddonConstants.init('${baseUrl}', '${addToCartText}',
			'${updateCartText}', '${addedToCartText}', '${updatedInCartText}',
			'${noStock}');
	initConfigPage();
</script>
