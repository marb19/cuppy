<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="productConfig" tagdir="/WEB-INF/tags/addons/ysapproductconfigb2baddon/responsive/product"%>

<productConfig:productAddToCartPanel product="${product}" allowAddToCart="${empty showAddToCart ? true : showAddToCart}"
	isMain="true" />
