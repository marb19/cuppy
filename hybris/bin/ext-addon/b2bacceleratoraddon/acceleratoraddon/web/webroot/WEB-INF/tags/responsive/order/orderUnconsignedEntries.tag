<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.OrderData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<div class="well well-tertiary">
    <div class="well-headline">
        <spring:theme code="text.account.order.unconsignedEntry.status.pending" />
    </div>

    <div class="well-content col-md-5 order-ship-to">
        <div class="label-order"><spring:theme code="text.account.order.shipto" /></div>
        <div class="value-order"><order:addressItem address="${order.deliveryAddress}"/></div>
    </div>
    <div class="well-content col-md-5 order-shipping-method">
       <div class="label-order"><spring:theme code="text.shippingMethod" /></div>
       <div class="value-order">${orderData.deliveryMode.name}<br>${orderData.deliveryMode.description}</div>
    </div>
</div>

<ul class="product-list">
	<c:forEach items="${order.unconsignedEntries}" var="entry" varStatus="loop">
        <order:orderEntryDetails orderEntry="${entry}" order="${order}" itemIndex="${loop.index}"/>
	</c:forEach>
</ul>

