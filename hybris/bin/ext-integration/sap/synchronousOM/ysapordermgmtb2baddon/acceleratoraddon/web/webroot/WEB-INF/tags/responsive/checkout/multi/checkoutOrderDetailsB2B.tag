<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ attribute name="showShipDeliveryEntries" required="false" type="java.lang.Boolean" %>
<%@ attribute name="showPickupDeliveryEntries" required="false" type="java.lang.Boolean" %>
<%@ attribute name="showTax" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="checkout" tagdir="/WEB-INF/tags/responsive/checkout" %>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/responsive/checkout/multi" %>
<%@ taglib prefix="multi-checkout-b2b" tagdir="/WEB-INF/tags/addons/ysapordermgmtb2baddon/responsive/checkout/multi" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>


<c:if test="${not empty cartData}">
	<div id="checkoutOrderDetails" class="span-9 last">
			<cart:cartTotals cartData="${cartData}" showTax="${showTax}"/>
			<cart:cartPromotions cartData="${cartData}"/>
			<multi-checkout-b2b:deliveryCartItemsB2B cartData="${cartData}"/>
			<c:forEach items="${cartData.pickupOrderGroups}" var="groupData" varStatus="status">
					<multi-checkout-b2b:pickupCartItems cartData="${cartData}" groupData="${groupData}" index="${status.index}" showHead="true" />
			</c:forEach>	
	</div>
</c:if>
