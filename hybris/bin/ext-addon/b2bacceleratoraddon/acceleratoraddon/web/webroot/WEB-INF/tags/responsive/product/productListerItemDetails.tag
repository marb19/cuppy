<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ attribute name="skipSummary" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:htmlEscape defaultHtmlEscape="true" />
<spring:url value="${product.url}" var="searchUrl" htmlEscape="false"/>
<div class="row">
    <div class="col-md-12">
        <div class="thumb">
            <c:if test="${not empty product.averageRating}">
                    <span class="stars large" style="display: inherit;">
                    <span style="width: <fmt:formatNumber maxFractionDigits="0"
                                                          value="${product.averageRating * 24}"/>px;"></span>
                </span>
            </c:if>

            <a href="${productUrl}" title="${fn:escapeXml(product.name)}">
                <product:productPrimaryImage product="${product}" format="thumbnail"/>
            </a>

            <c:if test="${not empty product.potentialPromotions and not empty product.potentialPromotions[0].productBanner}">
                <img class="promo" src="${product.potentialPromotions[0].productBanner.url}"
                     alt="${product.potentialPromotions[0].description}"
                     title="${product.potentialPromotions[0].description}"/>
            </c:if>
        </div>

        <div class="details clearfix">
            <div class="row">
                <div class="col-md-6">
                    <ycommerce:testId code="searchPage_productName_link_${product.code}">
                        <a href="${searchUrl}" title="${fn:escapeXml(product.name)}">
                            <div class="name">${fn:escapeXml(product.name)}</div>
                        </a>
                    </ycommerce:testId>
                    <div class="item-sku">${product.code}</div>

                    <div>
                        <spring:theme code="basket.page.itemPrice"/>:
                        <span class="price"><product:productListerItemPrice product="${product}"/></span>
                    </div>
                </div>

                <div class="col-md-6">
                    <product:productListerClassifications product="${product}"/>
                </div>
            </div>
        </div>

        <div class="col-md-12">
            <div class="details with-editable-grid js-show-orderFormGridWrapper">
                <div class="qty">
                    <label><spring:theme code="basket.page.qty"/>:</label>
                    <span class="qtyValue" data-total-qty="0">0</span>
                    <div class="item-price" data-total-price="0">$0</div>
                </div>
            </div>
        </div>
    </div>
</div>