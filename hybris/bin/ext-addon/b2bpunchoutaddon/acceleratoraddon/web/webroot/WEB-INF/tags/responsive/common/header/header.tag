<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="hideHeaderLinks" required="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>

<c:set value="site-logo js-site-logo" var="logoClass"></c:set>
<c:if test="${isInspectOperation}">
	<c:set value="site-logo js-site-logo inspect-logo" var="logoClass"></c:set>
</c:if>

<cms:pageSlot position="TopHeaderSlot" var="component" element="div" class="container">
	<cms:component component="${component}" />
</cms:pageSlot>

<header class="main-header main-header-md punchout-header">
	<div class="nav-top hidden-xs hidden-sm">
		<div class="container">
			<div class="row">
				<div class="col-sm-12 col-md-4">
					<div class="${logoClass}">
						<cms:pageSlot position="SiteLogo" var="logo" limit="1">
							<cms:component component="${logo}" />
						</cms:pageSlot>
					</div>
				</div>
				<div class="col-sm-12 col-md-8">
					<div class="md-secondary-navigation">

						<ul>
							<c:if test="${empty hideHeaderLinks}">
								<sec:authorize ifNotGranted="ROLE_ANONYMOUS">
									<c:set var="maxNumberChars" value="25" />
									<c:if test="${fn:length(user.firstName) gt maxNumberChars}">
										<c:set target="${user}" property="firstName"
											value="${fn:substring(user.firstName, 0, maxNumberChars)}..." />
									</c:if>

									<li class="logged_in">
										<ycommerce:testId code="header_LoggedUser">
											<spring:theme code="header.welcome" arguments="${user.firstName},${user.lastName}" htmlEscape="true" />
										</ycommerce:testId>
									</li>
								</sec:authorize>
							</c:if>
						</ul>
						
					</div>

				</div>
			</div>
		</div>
	</div>
	<c:if test="${not isInspectOperation}">
		<div class="nav-middle">
	        <div class="sm-navigation">
				<div class="container">
					<div class="row">
						<div class="col-xs-2 col-sm-1 visible-xs visible-sm mobile-menu">
							<button class="btn js-toggle-sm-navigation"
									type="button">
								<span class="glyphicon glyphicon-align-justify"></span>
							</button>
						</div>
	
						<div class="col-xs-12 col-sm-6 col-lg-4 site-search">
	                        <cms:pageSlot position="SearchBox" var="component">
	                            <cms:component component="${component}" />
	                        </cms:pageSlot>
	                    </div>
	
						<div class="col-xs-2 col-sm-2 visible-xs mobile-search">
							<ycommerce:testId code="header_search_activation_button">
								<button	class="btn js-toggle-xs-search hidden-sm hidden-md hidden-lg" type="button">
									<span class="glyphicon glyphicon-search"></span>
								</button>
							</ycommerce:testId>
						</div>
	
						<cms:pageSlot position="MiniCart" var="cart" element="div" class="miniCartSlot">
							<div class="componentContainer">
								<cms:component component="${cart}" />
							</div>
						</cms:pageSlot>
					</div>
				</div>
	        </div>
		</div>
		<a id="skiptonavigation"></a>
		<nav:topNavigation />
	</c:if>
</header>


<cms:pageSlot position="BottomHeaderSlot" var="component" element="div"	class="container">
	<cms:component component="${component}" />
</cms:pageSlot>
