<%@ taglib prefix="config" tagdir="/WEB-INF/tags/addons/ysapproductconfigb2baddon/responsive/configuration"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:set var="bindResult" value="${requestScope['org.springframework.validation.BindingResult.config']}" />
<form:form id="configform" method="POST" modelAttribute="config">
	<config:addedToCartPopup config="${config}" product="${product}" bindResult="${bindResult}" addMode="${addedToCart}" />
	<form:input type="hidden" value="${config.kbKey.productCode}" path="kbKey.productCode" />
	<form:input type="hidden" value="${config.configId}" path="configId" />
	<form:input type="hidden" value="" path="selectedGroup" />
	<form:input type="hidden" value="false" path="forceExpand" />
	<form:input type="hidden" value="${groupStartLevel}" path="startLevel" />
	<form:input type="hidden" value="${config.cartItemPK}" path="cartItemPK" />
	<form:input type="hidden" value="${autoExpand}" path="autoExpand" />
	<form:input type="hidden" value="${focusId}" path="focusId" />
	<form:input type="hidden" value="" path="groupIdToToggle" />
	<form:input type="hidden" value="" path="groupIdToToggleInSpecTree" />
</form:form>
