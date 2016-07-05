<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<div class="back-link">
    <spring:url value="/my-account/approval-dashboard" var="orderApprovalDashboardUrl" htmlEscape="false" />
    <button type="button" class="orderTopBackBtn" data-back-to-orders="${orderApprovalDashboardUrl}">
        <span class="glyphicon glyphicon-chevron-left"></span>
    </button>
    <span class="label">
        <spring:theme code="text.account.orderApprovalDetails.label" />
    </span>
</div>