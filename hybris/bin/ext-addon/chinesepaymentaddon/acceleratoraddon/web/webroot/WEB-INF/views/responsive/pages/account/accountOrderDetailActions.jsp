<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>

<spring:url value="/checkout/multi/summary/payRightNow" var="payRightNowUrl"/>
<spring:url value="/checkout/multi/summary/checkPaymentResult" var="checkPaymentResultUrl"/>
<spring:url value="/my-account/order/cancel/${orderData.code}" var="orderCancelUrl"/>
<spring:url value="/my-account/orders" var="orderHistoryUrl"/>

<div class="row">
    <c:choose>
        <c:when test="${orderData.status.code != 'CANCELLED' and orderData.paymentStatus.code eq 'NOTPAID'}">
            <div class="col-lg-4 col-sm-6 col-md-4 col-xs-12">
                <a class="btn btn-default btn-block" href="${orderCancelUrl}">
                    <spring:theme code="order.detail.button.pay.cancel" text="CancelOrder"/>
                </a>
            </div>
            <div class="col-lg-4 col-sm-6 col-md-4 col-xs-12">
                <a id="payRightNowButton" disabled="disabled" class="btn btn-default btn-block" href="${payRightNowUrl}/${orderData.code}" target="_blank">
                    <spring:theme code="order.detail.button.pay.immediately" text="PayImmediately"/>
                </a>
            </div>
            <div class="col-lg-4 col-sm-6 col-md-4 col-xs-12">
                <a class="btn btn-default btn-block orderBackBtn" href="${orderHistoryUrl}">
                    <spring:theme code="text.account.orderDetails.backToOrderHistory" text="Back To Order History"/>
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="col-lg-4 col-sm-6 col-md-4 col-xs-12 cancel-panel">
                <a class="btn btn-default btn-block orderBackBtn" href="${orderHistoryUrl}">
                    <spring:theme code="text.account.orderDetails.backToOrderHistory" text="Back To Order History"/>
                </a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<div class="mask"></div>
<div class="row pay-pop-container">
    <div class="payPop col-lg-4 col-sm-6 col-md-6 col-xs-12">
    	<p><spring:theme code="order.payment.remark" /></p>
        <div class="payPopBtn clearfix">
        	<a href="${checkPaymentResultUrl}/${orderData.code}" class="btn btn-primary btn-block checkout-next"><spring:theme code="order.payment.successfully" /></a><br/>
            <a href="${checkPaymentResultUrl}/${orderData.code}" class="btn btn-primary btn-block checkout-next"><spring:theme code="order.payment.rencontre.problem" /></a>
            <br><br>
        </div>	
    </div>
</div>