<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="config" tagdir="/WEB-INF/tags/addons/ysapproductconfigb2baddon/responsive/configuration"%>
<%@ taglib prefix="cssConf" uri="sapproductconfig.tld"%>

<%@ attribute name="cstic" required="true" type="de.hybris.platform.sap.productconfig.facades.CsticData"%>
<%@ attribute name="groupType" required="true" type="de.hybris.platform.sap.productconfig.facades.GroupType"%>

<%@ attribute name="csticKey" required="true" type="java.lang.String"%>
<%@ attribute name="pathPrefix" required="true" type="java.lang.String"%>
<%@ attribute name="valueStyle" required="false" type="java.lang.String"%>

<div class="cpq-label-config-link-row">
	<config:label cstic="${cstic}" csticKey="${csticKey}" typeSuffix=".radioGroup" />
	<config:conflictLinkToConfig groupType="${groupType}" key="${csticKey}" />
</div>
<config:longText cstic="${cstic}" csticKey="${csticKey}" pathPrefix="${pathPrefix}" />

<config:csticErrorMessages key="${csticKey}" groupType="${groupType}"
	bindResult="${requestScope['org.springframework.validation.BindingResult.config']}" path="${pathPrefix}value" />
<config:csticErrorMessages key="${csticKey}.additionalValue" groupType="${groupType}"
	bindResult="${requestScope['org.springframework.validation.BindingResult.config']}" path="${pathPrefix}additionalValue" />


<div id="${csticKey}.radioGroup" class="${cssConf:valueStyleClass(cstic)} ${valueStyle}">
	<c:forEach var="value" items="${cstic.domainvalues}" varStatus="status">
		<c:choose>
			<c:when test="${status.index == 0}">
				<c:set value="cpq-csticValueSelect-first" var="cssValueClass" />
				<c:set value="cpq-csticValueLabel-first" var="cssLabelClass" />
			</c:when>
			<c:otherwise>
				<c:set value="" var="cssValueClass" />
				<c:set value="" var="cssLabelClass" />
			</c:otherwise>
		</c:choose>
		<div class="radio">
			<form:radiobutton id="${csticKey}.${value.key}.radioButton" class="cpq-csticValueSelect ${cssValueClass}"
				value="${value.name}" path="${pathPrefix}value" />
			<label id="${csticKey}.${value.key}.label" class="cpq-csticValueLabel ${cssLabelClass}"
				for="${csticKey}.${value.key}.radioButton">${value.langdepname}</label>
			<c:if test="${value.deltaPrice.formattedValue ne '-'}">
				<c:choose>
					<c:when test="${value.deltaPrice.value.unscaledValue() == 0}">
						<spring:message code="sapproductconfig.deltaprcices.included" text="Included" var="formattedPrice" />
					</c:when>
					<c:otherwise>
						<c:set value="${value.deltaPrice.formattedValue}" var="formattedPrice" />
					</c:otherwise>
				</c:choose>
				<div title="${formattedPrice}" id="${csticKey}.${value.key}.deltaPrice"
					class="cpq-csticValueDeltaPrice cpq-csticValueLabel">${formattedPrice}</div>
			</c:if>
		</div>
	</c:forEach>
</div>

<c:if test="${cstic.type == 'RADIO_BUTTON_ADDITIONAL_INPUT'}">
	<config:additionalValue cstic="${cstic}" csticKey="${csticKey}" pathPrefix="${pathPrefix}" />
</c:if>

