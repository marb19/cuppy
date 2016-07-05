<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order" %>

<spring:htmlEscape defaultHtmlEscape="true"/>

<c:if test="${not empty orderData}">
    <div class="account-orderdetail">
        <ycommerce:testId code="replenishment_orderDetail_itemHeader_section">
            <div class="well well-tertiary">
                <div class="well-headline">
                    <spring:theme code="text.account.replenishment.shipToItems"/>
                </div>

                <c:set var="hasShippedItems" value="${orderData.deliveryItemsQuantity > 0}"/>
                <div class="well-content col-sm-12 col-md-9">
                    <c:choose>
                        <c:when test="${hasShippedItems}">
                            <div class="row">
                                <div class="col-sm-6 col-md-4 order-ship-to">
                                    <div class="label-order">
                                        <spring:theme code="text.account.order.shipto"/>
                                    </div>
                                    <div class="value-order">
                                        <order:addressItem address="${orderData.deliveryAddress}"/>
                                    </div>
                                </div>
                                <div class="col-sm-6 col-md-4 order-shipping-method">
                                    <ycommerce:testId code="replenishment_orderDetail_deliveryMethod_section">
                                        <order:deliveryMethodItem order="${orderData}"/>
                                    </ycommerce:testId>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="label-order">
                                <spring:theme code="checkout.pickup.no.delivery.required"/>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </ycommerce:testId>

        <ul class="product-list">
            <ycommerce:testId code="replenishment_orderDetail_itemBody_section">
                <c:forEach items="${orderData.entries}" var="entry" varStatus="loop">
                    <order:orderEntryDetails orderEntry="${entry}" order="${orderData}" itemIndex="${loop.index}" 
                    	targetUrl="${pageContext.request.contextPath}/my-account/my-replenishment/${orderData.jobCode}/getReadOnlyProductVariantMatrix"/>
                </c:forEach>
            </ycommerce:testId>
        </ul>
    </div>
</c:if>
