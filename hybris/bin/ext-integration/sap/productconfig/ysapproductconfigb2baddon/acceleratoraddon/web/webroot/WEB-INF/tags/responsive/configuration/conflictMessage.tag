<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="conf" uri="sapproductconfig.tld"%>

<%@ attribute name="group" required="true" type="de.hybris.platform.sap.productconfig.facades.UiGroupData"%>

<c:if test="${group.groupType eq 'CONFLICT'}">
	<div id="${group.id}.conflictMsg" class="cpq-conflict-msg">
		<c:out value="${group.description}" />
	</div>
</c:if>
