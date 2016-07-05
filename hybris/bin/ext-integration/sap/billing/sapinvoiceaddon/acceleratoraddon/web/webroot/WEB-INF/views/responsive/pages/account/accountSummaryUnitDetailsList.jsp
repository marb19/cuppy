<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<spring:theme code="text.store.dateformat.datepicker.selection"
	var="dateForForDatePicker" />
<spring:url htmlEscape="false" var="accountSummaryUnitDetailsUrl"
	value="/my-company/organization-management/accountsummary-unit/details" />
<c:set var="searchUrl"
	value="/my-company/organization-management/accountsummary-unit/details" />
<c:set var="searchUrlWithParams"
	value="${searchUrl}?unit=${b2bUnitData.uid}&sort=${searchPageData.pagination.sort}
	&typeCriteria=${typeCriteriaCode}&startRange=${startRange}&endRange=${endRange}
	&documentStatus=${documentStatus}&filterByKey=${filterByKey}" />

<jsp:useBean id="params" class="java.util.HashMap" />
<c:set target="${params}" property="unit" value="${b2bUnitData.uid}" />
<c:set target="${params}" property="sort"
	value="${searchPageData.pagination.sort}" />

<jsp:useBean id="additionalParams" class="java.util.HashMap" />
<c:set target="${additionalParams}" property="unit"
	value="${b2bUnitData.uid}" />
<c:set target="${additionalParams}" property="typeCriteria"
	value="${typeCriteriaCode}" />
<c:set target="${additionalParams}" property="startRange"
	value="${startRange}" />
<c:set target="${additionalParams}" property="endRange"
	value="${endRange}" />
<c:set target="${additionalParams}" property="documentStatus"
	value="${documentStatus}" />
<c:set target="${additionalParams}" property="filterByKey"
	value="${filterByKey}" />

<div class="section-headline">
	<spring:theme code="text.company.accountsummary.documents.label" />
</div>

<div class="row">
	<form:form id="filterByCriteriaForm"
		action="${accountSummaryUnitDetailsUrl}" method="get"
		class="clearfix account-summary-filter">
		<div class="documentStatus col-md-2 form-group">
			<label> <spring:theme
					code="text.company.accountsummary.criteria.document.status.label" />
			</label> <select id="documentStatus" name="documentStatus"
				class="form-control">
				<option id="all" value="ALL"
					${ documentStatusItem eq documentStatus ? 'selected="selected"' : ''}>
					<spring:theme
						code="text.company.accountsummary.document.status.all.label" />
				</option>
				<c:forEach items="${documentStatusList}" var="documentStatusItem">
					<option id="${documentStatusItem}" value="${documentStatusItem}"
						${ documentStatusItem eq documentStatus ? 'selected="selected"' : '' }>
						<spring:theme
							code="text.company.accountsummary.document.status.${documentStatusItem}.label" />
					</option>
				</c:forEach>
			</select>
		</div>

		<div id="filterByKey" class="filterByKey col-md-3 form-group"
			data-filter-by-key="${fn:escapeXml(filterByKey)}">
			<label> <spring:theme
					code="text.company.accountsummary.criteria.filterby.label" />
			</label> <select id="filterByKey" name="filterByKey" class="form-control">
				<c:forEach items="${filterByList}" var="filterByItem">
					<option id="${filterByItem.key}" value="${filterByItem.key}"
						${filterByItem.key eq filterByKey ? 'selected="selected"' : ''}>
						<spring:theme
							code="text.company.accountsummary.${filterByItem.key}.label" />
					</option>
				</c:forEach>
			</select>
		</div>

		<div class="filterByValue col-md-5">
			<div id="rangeCriteria" class="rangeCriteria criterias row"
				style="display: none;"
				data-date-For-Date-Picker="${fn:escapeXml(dateForForDatePicker)}"
				data-start-Range="${fn:escapeXml(startRange)}"
				data-end-Range="${fn:escapeXml(endRange)}">

				<div class="col-sm-6 col-md-4 form-group">
					<label> <spring:theme
							code="text.company.accountsummary.from.label" />
					</label>
					<div id="startRangeCriteria" class="date-input">
						<input id="startRange" class="form-control filterCriteria"
							name="startRange" type="text" value="${startRange}" /> <i
							class=""></i>
					</div>
				</div>
				<div class="col-sm-6 col-md-4 form-group">
					<label> <spring:theme
							code="text.company.accountsummary.to.label" />
					</label>
					<div id="endRangeCriteria" class="date-input">
						<input id="endRange" class="form-control filterCriteria"
							name="endRange" type="text" value="${endRange}" /> <i class=""></i>
					</div>
				</div>
			</div>
			<div id="documentTypeCriteria"
				class="documentTypeCriteria criterias col-md-7 form-group"
				style="display: none;"
				data-type-criteria-code="${fn:escapeXml(typeCriteriaCode)}">
				<div class="row">
					<label> <spring:theme
							code="text.company.accountsummary.documentType.label" />
					</label> <select id="typeCriteria" name="typeCriteria"
						class="form-control filterCriteria">
						<option disabled="disabled" value=""
							${ empty typeCriteriaCode ? 'selected="selected"' : ''}>
							<spring:theme
								code="text.company.accountsummary.document.type.select.label" />
						</option>
						<c:forEach items="${documentTypeList}" var="documentType">
							<option id="${documentType.code}" value="${documentType.code}"
								${documentType.code eq typeCriteriaCode ? 'selected="selected"' : ''}>${documentType.name}</option>
						</c:forEach>
					</select>
				</div>
			</div>
		</div>
		<div class="col-md-2">
			<button id="search" type="submit" value="Search"
				class="btn btn-primary btn-block">
				<spring:theme code="text.company.accountsummary.button.search" />
			</button>
		</div>
		<c:if test="${not empty params}">
			<c:forEach items="${params}" var="entry">
				<input type="hidden" name="${entry.key}" value="${entry.value}" />
			</c:forEach>
		</c:if>
	</form:form>
</div>

<div id="invoiceErrorMessage" style="display: none;"></div>
<c:choose>
	<c:when test="${empty searchPageData.results}">
		<div class="account-section-content	 account-section-content-small">
			<spring:theme code="text.company.accountsummary.nodocuments.info" />
		</div>
	</c:when>
	<c:otherwise>
		<div class="account-section-content	">
			<div class="account-orderhistory">
				<div class="account-orderhistory-pagination">
					<nav:pagination top="true"
						msgKey="text.company.accountsummary.unit.details.page"
						showCurrentPageInfo="true" hideRefineButton="true"
						supportShowPaged="${isShowPageAllowed}"
						supportShowAll="${isShowAllAllowed}"
						searchPageData="${searchPageData}"
						searchUrl="${searchUrlWithParams}"
						numberPagesShown="${numberPagesShown}"
						additionalParams="${additionalParams}" />
				</div>
				<spring:theme code="text.invoice.download.error" var="defaultErrorMsg"/>
				<div class="account-summary-table">
					<table class="responsive-table">
						<tr class="responsive-table-head hidden-xs">
							<th><spring:theme
									code="text.company.accountsummary.documentNumber.label" /></th>
							<th><spring:theme
									code="text.company.accountsummary.documentType.label" /></th>
							<th><spring:theme
									code="text.company.accountsummary.date.label" /></th>
							<th><spring:theme
									code="text.company.accountsummary.dueDate.label" /></th>
							<th><spring:theme
									code="text.company.accountsummary.amount.label" /></th>
							<th><spring:theme
									code="text.company.accountsummary.openAmount.label" /></th>
							<th><spring:theme
									code="text.company.accountsummary.status.label" /></th>
							<th><spring:theme
									code="text.company.accountsummary.document.attachment.label" /></th>
							<th><spring:theme
									code="text.company.accountsummary.invoice.label" /></th>
						</tr>
						<c:forEach items="${searchPageData.results}" var="result">
							<spring:url
								value="/my-company/organization-management/invoicedocument/invoicedetails"
								var="invoiceDetailsURL">
								<spring:param name="invoiceCode"
									value="${result.documentNumber}" />
								<spring:theme code="text.company.accountsummary.invoice.label"
									var="documentTypeName" />
							</spring:url>
							<tr class="responsive-table-item">
								<td class="hidden-sm hidden-md hidden-lg"><spring:theme
										code="text.company.accountsummary.documentNumber.label" /></td>
								<td class="responsive-table-cell"><c:choose>
										<c:when
											test="${result.documentType.name eq fn:trim(documentTypeName)}">
											<a href="${invoiceDetailsURL}">${fn:escapeXml(result.documentNumber)}</a>
										</c:when>
										<c:otherwise>
											${fn:escapeXml(result.documentNumber)}
										</c:otherwise>
									</c:choose></td>
								<td class="hidden-sm hidden-md hidden-lg"><spring:theme
										code="text.company.accountsummary.documentType.label" /></td>
								<td class="responsive-table-cell">
									${fn:escapeXml(result.documentType.name)}</td>
								<td class="hidden-sm hidden-md hidden-lg"><spring:theme
										code="text.company.accountsummary.date.label" /></td>
								<td class="responsive-table-cell"><fmt:formatDate
										value="${result.date}" type="date" /></td>
								<td class="hidden-sm hidden-md hidden-lg"><spring:theme
										code="text.company.accountsummary.dueDate.label" /></td>
								<td class="responsive-table-cell"><fmt:formatDate
										value="${result.dueDate}" type="date" /></td>
								<td class="hidden-sm hidden-md hidden-lg"><spring:theme
										code="text.company.accountsummary.amount.label" /></td>
								<td class="responsive-table-cell">
									${fn:escapeXml(result.formattedAmount)}</td>
								<td class="hidden-sm hidden-md hidden-lg"><spring:theme
										code="text.company.accountsummary.openAmount.label" /></td>
								<td class="responsive-table-cell">
									${fn:escapeXml(result.formattedOpenAmount)}</td>
								<td class="hidden-sm hidden-md hidden-lg"><spring:theme
										code="text.company.accountsummary.status.label" /></td>
								<td class="responsive-table-cell"><spring:theme
										code="text.company.accountsummary.${result.status}.label" /></td>
								<td class="hidden-sm hidden-md hidden-lg"><spring:theme
										code="text.company.accountsummary.document.attachment.label" />
								</td>

								<td class="responsive-table-cell"><c:if
										test="${not empty result.documentMedia.downloadURL}">
										<a class="download-lnk"
											href="${result.documentMedia.downloadURL }" target=_blank>
											<spring:theme
												code="text.company.accountsummary.document.attachment.view.label" />
										</a>
									</c:if></td>

								<td class="hidden-sm hidden-md hidden-lg"><spring:theme
										code="download.view" /></td>
								<td class="responsive-table-cell"><c:if
										test="${result.documentType.name eq fn:trim(documentTypeName)}">
										<a href="#" id="invoicePDF"
											data-invoicenumber="${result.documentNumber}"
											data-errormsg="${defaultErrorMsg}" class="invoiceClass"><spring:theme
												code="download.view" /></a>
									</c:if></td>
							</tr>
						</c:forEach>
					</table>
				</div>
			</div>
			<div class="account-orderhistory-pagination">
				<nav:pagination top="true"
					msgKey="text.company.accountsummary.unit.details.page"
					showCurrentPageInfo="true" hideRefineButton="true"
					supportShowPaged="${isShowPageAllowed}"
					supportShowAll="${isShowAllAllowed}"
					searchPageData="${searchPageData}"
					searchUrl="${searchUrlWithParams}"
					numberPagesShown="${numberPagesShown}"
					additionalParams="${additionalParams}" />
			</div>
		</div>
	</c:otherwise>
</c:choose>