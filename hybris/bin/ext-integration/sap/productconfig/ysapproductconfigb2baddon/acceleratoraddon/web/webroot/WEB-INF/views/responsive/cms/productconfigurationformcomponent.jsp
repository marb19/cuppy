<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="config" tagdir="/WEB-INF/tags/addons/ysapproductconfigb2baddon/responsive/configuration"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="conf" uri="/WEB-INF/tags/addons/ysapproductconfigb2baddon/responsive/configuration/sapproductconfig.tld"%>

<c:set var="bindResult" value="${requestScope['org.springframework.validation.BindingResult.config']}" />
<c:set var="validationErrors" value="${conf:validationError(bindResult)}" />

<div id="configurationForm" class="cpq-form">
	<form:form id="configform" method="POST" modelAttribute="config">
      <common:globalMessages />
		<script>
			var addToCartPopUpEnabled = false;
			var addedToCart = false;
			var errorExist = false;
		</script>
		<c:choose>
			<c:when test="${not empty addedToCart}">
				<script>
					addToCartPopUpEnabled = true;
				</script>
				<c:choose>
					<c:when test="${addedToCart}">
						<script>
							addedToCart = true;
						</script>
					</c:when>
				</c:choose>
			</c:when>
		</c:choose>
		
		<c:if test="${fn:length(validationErrors) gt 0}" >
		  <script>
		   errorExist = true;
		  </script>
		</c:if>

		<div class="cpq-groups">
			<c:choose>
				<c:when test="${config.singleLevel}">							
					<c:set value="${fn:length(config.groups)}"  var="numberOfCsticGroups"/>
					<!-- first all conflict groups -->
					<c:forEach var="group" items="${config.groups}" varStatus="groupStatus">
						<c:set value="groups[${groupStatus.index}]." var="path" />
						<c:if test="${group.configurable and group.groupType eq 'CONFLICT_HEADER'}">
							<config:group group="${group}" pathPrefix="${path}" hideExpandCollapse="true" hideGroupTitle="true"/>
						   <c:set value="${numberOfCsticGroups-1}"  var="numberOfCsticGroups"/>
						</c:if>
					</c:forEach>	
					<!-- then all ordinary groups -->
					<c:set value="${numberOfCsticGroups == 1}"  var="hideExpandCollapse"/>
					<c:forEach var="group" items="${config.groups}" varStatus="groupStatus">
						<c:set value="groups[${groupStatus.index}]." var="path" />
						<c:if test="${group.configurable and group.groupType ne 'CONFLICT_HEADER'}">
							<config:group group="${group}" pathPrefix="${path}" hideExpandCollapse="${hideExpandCollapse}" />
						</c:if>
					</c:forEach>
						
				</c:when>
				<c:otherwise>
						<config:group group="${config.groupToDisplay.group}" pathPrefix="${config.groupToDisplay.path}" hideExpandCollapse="true" />
				</c:otherwise>
			</c:choose>
		</div>

		<form:input type="hidden" value="${config.kbKey.productCode}" path="kbKey.productCode" />
		<form:input type="hidden" value="${config.configId}" path="configId" />
		<form:input type="hidden" value="" path="selectedGroup" />
		<form:input type="hidden" value="${config.cartItemPK}" path="cartItemPK" />
		<form:input type="hidden" value="${autoExpand}" path="autoExpand" />
		<form:input type="hidden" value="${focusId}" path="focusId" />
		<form:input type="hidden" value="${config.groupIdToDisplay}" path="groupIdToDisplay" />
		<form:input type="hidden" value="${config.groupToDisplay.groupIdPath}" path="groupToDisplay.groupIdPath" />
		<form:input type="hidden" value="${config.groupToDisplay.path}" path="groupToDisplay.path" />
		<form:input type="hidden" value="" path="groupIdToToggle" />
		<form:input type="hidden" value="" path="groupIdToToggleInSpecTree" />
		<form:input type="hidden" value="false" path="forceExpand" />
		<form:input type="hidden" value="${config.hideImageGallery}" path="hideImageGallery" />
		<form:input type="hidden" value="" path="cpqAction" />
		

	</form:form>
</div>

