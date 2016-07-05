<%@ tag language="java" pageEncoding="ISO-8859-1" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:theme code="text.addToCart" var="addToCartText" />
<button type="submit" id="cpqAddToCartBtn"
	class="btn cpq-btn-addToCart">
	<spring:theme code="text.addToCart" var="addToCartText" />
	<spring:theme code="basket.add.to.basket" />
</button>



