<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url value="/checkout/multi/summary/payRightNow" var="payRightNowUrl"/>
<spring:url value="/checkout/multi/summary/checkPaymentResult" var="checkPaymentResultUrl"/>
<template:page pageTitle="${pageTitle}" hideHeaderLinks="true">


	<div class="checkout-headline">
		<spring:theme code="checkout.multi.clicktopay"/>
	</div>
	
	<div class="row">
		<div class="col-sm-3 col-md-4 col-lg-4">
			<div class="checkout-indent">
				<div class="form-group">
					<label class="control-label ">
						<spring:theme code="order.submit.order.code" />
					</label>
					<div class="controls">
						<spring:theme text="${orderData.code}"/>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label ">
						<spring:theme code="order.submit.due.payment" />
					</label>
					<div class="controls">
						<format:price priceData="${orderData.totalPrice}"/>
					</div>
				</div>
				<div class="col-lg-7 col-md-6 col-sm-12">
					<a id="payRightNowButton" class="btn btn-primary btn-block checkout-next" disabled="disabled" href="${payRightNowUrl}/${orderData.code}" target="_blank">
						<spring:theme code="checkout.multi.payrightnow"/>
					</a><br>
				</div>
			</div>
		</div>
	</div>
	<div class="row pay-pop-container">
	    <div class="payPop col-lg-4 col-sm-6 col-md-6 col-xs-12">
	    	<p><spring:theme code="order.payment.remark" /></p>
	        <div class="payPopBtn clearfix">
	        	<a href="${checkPaymentResultUrl}/${orderData.code}"  class="btn btn-primary btn-block checkout-next"><spring:theme code="order.payment.successfully" /></a><br/>
	            <a href="${checkPaymentResultUrl}/${orderData.code}" class="btn btn-primary btn-block checkout-next"><spring:theme code="order.payment.rencontre.problem" /></a>
	            <br><br>
	        </div>	
	    </div>
	</div>
	
	<div class="mask"></div>
    
	<div class="col-sm-12 col-lg-9">
		<br class="hidden-lg">
		<cms:pageSlot position="SideContent" var="feature" element="div" class="checkout-help">
			<cms:component component="${feature}"/>
		</cms:pageSlot>
	</div>
	
</template:page>