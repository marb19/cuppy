<!-- ******* The fragment is used by payment addon ******* -->

<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:choose>
	<c:when test="${fn:toUpperCase(address.country.isocode) eq 'CN'}">
	 	<br>${fn:escapeXml(address.line1)}
		<c:if test="${not empty fn:escapeXml(address.line2)}">
			&nbsp;${fn:escapeXml(address.line2)}
		</c:if>
		<br>${fn:escapeXml(address.city.name)}&nbsp;${fn:escapeXml(address.district.name)}
		<br> ${fn:escapeXml(address.country.name)}&nbsp;${fn:escapeXml(address.region.name)}
	    <br>${fn:escapeXml(address.cellphone)}
	</c:when>
	<c:otherwise>
		<br>${fn:escapeXml(address.line1)}
		<c:if test="${not empty fn:escapeXml(address.line2)}">
			<br>${fn:escapeXml(address.line2)}
		</c:if>
		<br>${fn:escapeXml(address.town)}&nbsp;${fn:escapeXml(address.region.name)}
		<br> ${fn:escapeXml(address.country.name)}&nbsp;${fn:escapeXml(address.postalCode)}
	    <br>${fn:escapeXml(address.phone)}
	</c:otherwise>
</c:choose>
