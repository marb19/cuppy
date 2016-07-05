function retrieveRecommendations(id, productCode, componentId){
	var baseUrl = $("#" + id).data("baseUrl");
    ajaxUrl = baseUrl+ '/action/recommendations/';
	$.get(ajaxUrl,
			{
				id: id,
				productCode: productCode,
				componentId: componentId
			},
			addRecommendation(id));	
};

function registerClickthrough(id, prodId, leadingitemdstype, scenarioId, prodURL, prodImageURL){
	var baseUrl = $("#" + id).parent().data("baseUrl")
    ajaxUrl = baseUrl + '/action/interaction/';
	
	$.post(ajaxUrl, {
		id: prodId,
		leadingitemdstype: leadingitemdstype,
		scenarioId: scenarioId,
		prodURL: window.location.origin + prodURL,
		prodImageURL: prodImageURL 
	}, null);
};