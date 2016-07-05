<%@ page trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="account-orderdetail well well-tertiary">
    <div class="well-headline">
        <spring:theme code="text.paymentDetails" text="Payment Details" />
    </div>
    <ycommerce:testId code="orderDetails_paymentDetails_section">
	    <div class="well-content col-sm-12 col-md-9">
	        <div class="row">
	            <c:if test="${not empty orderData.chinesePaymentInfo}">
	                <div class="col-sm-6 col-md-4 order-payment-data">
	                    <div class="label-order">
	                    	<spring:theme code="account.order.detail.payment.method.title" text="Payment Method" />
						</div>
						<div class="value-order">
	                    	${orderData.chinesePaymentInfo.paymentProviderName}&nbsp;-&nbsp;${orderData.chinesePaymentInfo.serviceType}
	                    </div>
	                 </div>
	                 <div class="col-sm-6 col-md-4 order-payment-data">
	                    <div class="label-order">
	                    	<spring:theme code="account.order.detail.payment.status.title" text="Payment Status" />
	                    </div>
	                    <div class="value-order">
	                    	<spring:theme code="type.PaymentStatus.${orderData.paymentStatus.code}.name" text="${orderData.paymentStatus.code}"/>
	                    </div>
	                </div>
	            </c:if>
	        </div>
	    </div>
    </ycommerce:testId>
</div>

