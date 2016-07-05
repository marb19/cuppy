/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.cmswebservices.items.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2lib.model.components.ProductListComponentModel;
import de.hybris.platform.cmswebservices.constants.CmswebservicesConstants;
import de.hybris.platform.cmswebservices.data.AbstractCMSComponentData;
import de.hybris.platform.cmswebservices.data.CMSParagraphComponentData;
import de.hybris.platform.cmswebservices.data.ComponentItemListData;
import de.hybris.platform.cmswebservices.items.facade.validator.CreateComponentValidator;
import de.hybris.platform.cmswebservices.localization.data.LocalizedValueMap;
import de.hybris.platform.cmswebservices.localization.data.LocalizedValueString;
import de.hybris.platform.cmswebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.cmswebservices.util.apiclient.ApiClient;
import de.hybris.platform.cmswebservices.util.apiclient.Response;
import de.hybris.platform.cmswebservices.util.apiclient.response.ValidationErrorResponse;
import de.hybris.platform.cmswebservices.util.models.BaseStoreModelMother;
import de.hybris.platform.cmswebservices.util.models.CatalogVersionModelMother;
import de.hybris.platform.cmswebservices.util.models.ContentSlotForPageModelMother;
import de.hybris.platform.cmswebservices.util.models.ContentSlotModelMother;
import de.hybris.platform.cmswebservices.util.models.ContentSlotNameModelMother;
import de.hybris.platform.cmswebservices.util.models.PageTemplateModelMother;
import de.hybris.platform.cmswebservices.util.models.ParagraphComponentModelMother;
import de.hybris.platform.cmswebservices.util.models.SimpleBannerComponentModelMother;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Maps;


@IntegrationTest
public class ItemControllerWebServiceTest extends ApiBaseIntegrationTest
{
	private static final String GET_ALL_ENDPOINT = "/v1/sites/electronics/catalogs/{catalogId}/versions/{versionId}/items";
	private static final String GET_ONE_ENDPOINT = "/v1/sites/electronics/catalogs/{catalogId}/versions/{versionId}/items/{uid}";
	private static final String UPDATE_ITEM_ENDPOINT = "/v1/sites/electronics/catalogs/{catalogId}/versions/{versionId}/items/{uid}";
	private static final String CREATE_ITEM_ENDPOINT = "/v1/sites/electronics/catalogs/{catalogId}/versions/{versionId}/items";
	private static final String PAGE_ID = "uid-homepage";
	private static final String INVALID_PAGE_ID = "invalid_uid-homepage";
	private static final Integer SLOT_POSITION = 2;

	private static final String NEW_CONTENT_HEADER_EN = "new-content-header";
	private static final String NEW_CONTENT_HEADER_FR = "new-content-header-fr";
	private static final String NEW_NAME_HEADER = "new-name-header";
	private static final String NEW_UID = "new-uid";
	private static final String CLASS_MAPPING = "class-mapping";

	private static final String TYPE_CODE = "typeCode";
	private static final String UID = "uid";
	private static final String NAME = "name";
	private static final String CONTENT = "content";

	private static final Locale LOCALE_ENGLISH = Locale.ENGLISH;
	private static final Locale LOCALE_FRENCH = Locale.FRENCH;

	@Resource
	private CatalogVersionModelMother catalogVersionModelMother;
	@Resource
	private ContentSlotModelMother contentSlotModelMother;
	@Resource
	private ParagraphComponentModelMother paragraphComponentModelMother;
	@Resource
	private ContentSlotForPageModelMother contentSlotForPageModelMother;
	@Resource
	private PageTemplateModelMother pageTemplateModelMother;
	@Resource
	private ContentSlotNameModelMother contentSlotNameModelMother;
	@Resource
	private BaseStoreModelMother baseStoreModelMother;

	private CatalogVersionModel catalogVersion;

	protected void createEmptyAppleCatalog()
	{
		catalogVersion = catalogVersionModelMother.createAppleStagedCatalogVersionModel();
	}

	protected void createHomeAppleCatalogPageHeaderWithParagraphAndBanner()
	{
		createEmptyAppleCatalog();
		contentSlotModelMother.createHeaderSlotWithParagraphAndBanner(catalogVersion);
	}

	protected void createHomeAppleCatalogPageHeaderWithParagraphWithoutRestriction()
	{
		createEmptyAppleCatalog();
		// Create homepage template
		final PageTemplateModel pageTemplate = pageTemplateModelMother.HomePage_Template(catalogVersion);
		// Create homepage page and content slot header
		contentSlotForPageModelMother.HeaderHomepage_ParagraphOnly(catalogVersion);
		// Create header content slot name with paragraph and banner type restrictions
		contentSlotNameModelMother.Header_without_restriction(pageTemplate);
	}

	protected void createHomeAppleCatalogPageHeaderWithParagraph_WithTypeRestrictions()
	{
		createEmptyAppleCatalog();
		// Create homepage template
		final PageTemplateModel pageTemplate = pageTemplateModelMother.HomePage_Template(catalogVersion);
		// Create homepage page and content slot header
		contentSlotForPageModelMother.HeaderHomepage_ParagraphOnly(catalogVersion);
		// Create header content slot name with paragraph and banner type restrictions
		contentSlotNameModelMother.Header(pageTemplate);
	}

	@Test
	public void shouldGetAllItemsAndAllItemsArePresent() throws Exception
	{
		createHomeAppleCatalogPageHeaderWithParagraphAndBanner();

		final List<AbstractCMSComponentData> entities = executeGetAllItemsWithParagraphAndBannerComponents();
		Assert.assertEquals(2, entities.size());

		final AbstractCMSComponentData headerParagraph = getComponentItemByUid(entities, ParagraphComponentModelMother.UID_HEADER);
		Assert.assertEquals(CMSParagraphComponentModel._TYPECODE, headerParagraph.getTypeCode());
		Assert.assertEquals(ParagraphComponentModelMother.UID_HEADER, headerParagraph.getUid());
		Assert.assertEquals(ParagraphComponentModelMother.NAME_HEADER, headerParagraph.getName());
		Assert.assertNull(headerParagraph.getSlotId());

		final AbstractCMSComponentData headerBanner = getComponentItemByUid(entities,
				SimpleBannerComponentModelMother.UID_HEADER_LOGO);
		Assert.assertEquals(SimpleBannerComponentModel._TYPECODE, headerBanner.getTypeCode());
		Assert.assertEquals(SimpleBannerComponentModelMother.UID_HEADER_LOGO, headerBanner.getUid());
		Assert.assertEquals(SimpleBannerComponentModelMother.NAME_HEADER_LOGO, headerBanner.getName());
		Assert.assertNull(headerBanner.getSlotId());
	}


	@Ignore("Ignoring for now, as the mothers are creating components with the same modified time")
	@Test
	public void shouldGetAllItemsAndAllItemsArePresentOnTheRightOrder() throws Exception
	{
		createHomeAppleCatalogPageHeaderWithParagraphAndBanner();

		final List<AbstractCMSComponentData> entities = executeGetAllItemsWithParagraphAndBannerComponents();
		Assert.assertEquals(2, entities.size());

		final AbstractCMSComponentData headerParagraph = entities.stream().findFirst().get();
		final AbstractCMSComponentData headerBanner = entities.stream().skip(1).findFirst().get();

		Assert.assertTrue("The response should return a list in descending order by modification time. ",
				headerParagraph.getModifiedtime().after(headerBanner.getModifiedtime()));
	}

	protected List<AbstractCMSComponentData> executeGetAllItemsWithParagraphAndBannerComponents() throws Exception
	{
		final ApiClient apiClient = getApiClientInstance();
		final Response<ComponentItemListData> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultCatalogAndCatalogVersion(GET_ALL_ENDPOINT, Maps.newHashMap())).acceptJson()
				.acceptLanguages(Locale.ENGLISH)
				.get(ComponentItemListData.class);

		assertStatusCode(response, 200);

		return response.getBody().getComponentItems();
	}

	@Test
	public void shouldGetAllItemsButWithEmptyCollection() throws Exception
	{
		createEmptyAppleCatalog();

		final ApiClient apiClient = getApiClientInstance();


		final Response<ComponentItemListData> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultCatalogAndCatalogVersion(GET_ALL_ENDPOINT, Maps.newHashMap())).acceptJson()
				.acceptLanguages(Locale.ENGLISH)
				.get(ComponentItemListData.class);

		assertStatusCode(response, 200);

		final List<AbstractCMSComponentData> entities = response.getBody().getComponentItems();

		Assert.assertTrue(CollectionUtils.isEmpty(entities));
	}

	@Test
	public void shouldGetOneItem() throws Exception
	{
		createHomeAppleCatalogPageHeaderWithParagraphWithoutRestriction();

		final ApiClient apiClient = getApiClientInstance();

		final Map<String, String> uriVariables = Maps.newHashMap();
		uriVariables.put(UID, ParagraphComponentModelMother.UID_HEADER);

		final String endPoint = replaceUriVariablesWithDefaultCatalogAndCatalogVersion(GET_ONE_ENDPOINT, uriVariables);

		final Response<AbstractCMSComponentData> response = apiClient.request().endpoint(endPoint).acceptJson()
				.acceptLanguages(Locale.ENGLISH).get(AbstractCMSComponentData.class);

		assertStatusCode(response, 200);

		Assert.assertEquals(CMSParagraphComponentModel._TYPECODE, response.getBody().getTypeCode());
		Assert.assertEquals(ParagraphComponentModelMother.UID_HEADER, response.getBody().getUid());
		Assert.assertEquals(ParagraphComponentModelMother.NAME_HEADER, response.getBody().getName());
	}

	@Test
	public void shouldReturnHttpStatus404DueToItemNotFound() throws Exception
	{
		createHomeAppleCatalogPageHeaderWithParagraphWithoutRestriction();

		final ApiClient apiClient = getApiClientInstance();

		final HashMap<String, String> uriVariables = Maps.newHashMap();
		uriVariables.put(UID, SimpleBannerComponentModelMother.UID_HEADER_LOGO);

		final Response<ValidationErrorResponse> response = apiClient
				.request().endpoint(replaceUriVariablesWithDefaultCatalogAndCatalogVersion(GET_ONE_ENDPOINT, uriVariables))
				.acceptJson().acceptLanguages(Locale.ENGLISH)
				.get(AbstractCMSComponentData.class, ValidationErrorResponse.class);

		final ValidationErrorResponse errorResponse = response.getErrorResponse();

		assertEquals(1, errorResponse.getErrors().size());
		assertStatusCode(response, 404);
	}

	@Test
	public void shouldUpdateOneItemExceptUID() throws Exception
	{
		createHomeAppleCatalogPageHeaderWithParagraphWithoutRestriction();

		//send updates to paragraphComponent
		final LocalizedValueString localizedValueString = new LocalizedValueString();
		localizedValueString.setValue(NEW_CONTENT_HEADER_EN);

		final CMSParagraphComponentData componentData = new CMSParagraphComponentData();
		componentData.setTypeCode(CMSParagraphComponentModel._TYPECODE);
		componentData.setUid(ParagraphComponentModelMother.UID_HEADER);
		componentData.setContent(localizedValueString);
		componentData.setName(NEW_NAME_HEADER);

		ApiClient apiClient = getApiClientInstance();

		final Map<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put(UID, ParagraphComponentModelMother.UID_HEADER);


		final Response<Void> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultCatalogAndCatalogVersion(UPDATE_ITEM_ENDPOINT, uriVariables)).acceptJson()
				.acceptLanguages(Locale.ENGLISH)
				.put(componentData);

		assertStatusCode(response, 204);


		apiClient = getApiClientInstance();
		final Response<CMSParagraphComponentData> responseGet = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultCatalogAndCatalogVersion(UPDATE_ITEM_ENDPOINT, uriVariables)).acceptJson()
				.acceptLanguages(Locale.ENGLISH)
				.get(CMSParagraphComponentData.class);

		assertStatusCode(responseGet, 200);
		Assert.assertEquals(NEW_CONTENT_HEADER_EN, ((LocalizedValueString)responseGet.getBody().getContent()).getValue());
		Assert.assertEquals(NEW_NAME_HEADER, responseGet.getBody().getName());
	}

	//TODO: we are not allowing the UID to be edited, fails during validation
	@Ignore
	@Test
	public void shouldUpdateOneItemWithUIDUpdated() throws Exception
	{
		createHomeAppleCatalogPageHeaderWithParagraphWithoutRestriction();

		//updates all fields of the component including UID
		final HashMap<String, String> object = new LinkedHashMap<String, String>();
		object.put(TYPE_CODE, CMSParagraphComponentModel._TYPECODE);
		object.put(UID, NEW_UID);
		object.put(CONTENT, NEW_CONTENT_HEADER_EN);
		object.put(NAME, NEW_NAME_HEADER);

		Map<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put(UID, ParagraphComponentModelMother.UID_HEADER);

		ApiClient apiClient = getApiClientInstance();
		final Response<Void> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultCatalogAndCatalogVersion(UPDATE_ITEM_ENDPOINT, uriVariables)).acceptJson()
				.acceptLanguages(Locale.ENGLISH)
				.put(object);

		assertStatusCode(response, 204);


		uriVariables = new HashMap<String, String>();
		uriVariables.put(UID, NEW_UID);
		apiClient = getApiClientInstance();
		final Response<CMSParagraphComponentData> responseGet = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultCatalogAndCatalogVersion(UPDATE_ITEM_ENDPOINT, uriVariables)).acceptJson()
				.acceptLanguages(Locale.ENGLISH)
				.get(CMSParagraphComponentData.class);

		assertStatusCode(responseGet, 200);
		Assert.assertEquals(NEW_CONTENT_HEADER_EN, responseGet.getBody().getContent());
		Assert.assertEquals(NEW_NAME_HEADER, responseGet.getBody().getName());
	}

	@Test
	public void shouldNotUpdateItemDueToNoTypeCodeProvided() throws Exception
	{
		createHomeAppleCatalogPageHeaderWithParagraphWithoutRestriction();


		final ApiClient apiClient = getApiClientInstance();


		final Map<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put(UID, ParagraphComponentModelMother.UID_HEADER);
		final String endpoint = replaceUriVariablesWithDefaultCatalogAndCatalogVersion(UPDATE_ITEM_ENDPOINT, uriVariables);

		final Response<Void> response = apiClient.request().endpoint(endpoint).acceptJson().acceptLanguages(Locale.ENGLISH).put(
				new CMSParagraphComponentData());

		assertStatusCode(response, 400);
	}

	@Test
	public void shouldCreateNewComponentBasedOnType() throws Exception
	{
		createHomeAppleCatalogPageHeaderWithParagraph_WithTypeRestrictions();

		final String uidNewComponent = "uid-new-component-added";
		final String nameNewComponent = "name-new-component-added";

		final CMSParagraphComponentData component = new CMSParagraphComponentData();

		component.setName(nameNewComponent);
		component.setSlotId(ContentSlotModelMother.UID_HEADER);
		component.setTypeCode(CMSParagraphComponentModel._TYPECODE);
		component.setUid(uidNewComponent);
		component.setPosition(SLOT_POSITION);
		component.setPageId(PAGE_ID);

		final ApiClient apiClient = getApiClientInstance();
		final Response<CMSParagraphComponentData> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultCatalogAndCatalogVersion(CREATE_ITEM_ENDPOINT, Maps.newHashMap())).acceptJson()
				.acceptLanguages(Locale.ENGLISH).post(component, CMSParagraphComponentData.class);
		assertStatusCode(response, 201);

		assertEquals(uidNewComponent, response.getBody().getUid());
		assertEquals(nameNewComponent, response.getBody().getName());
		assertEquals(SLOT_POSITION, response.getBody().getPosition());
		assertEquals(ContentSlotModelMother.UID_HEADER, response.getBody().getSlotId());
		assertEquals(PAGE_ID, response.getBody().getPageId());

		assertTrue(response.getHeaders().get(CmswebservicesConstants.HEADER_LOCATION).contains(uidNewComponent));
	}


	@Test
	public void shouldNotCreateNewComponentBasedOnTypeDueToUnkownSlotId() throws Exception
	{
		createHomeAppleCatalogPageHeaderWithParagraph_WithTypeRestrictions();

		final String uidNewComponent = "uid-new-component-added";
		final String nameNewComponent = "name-new-component-added";
		final String someWrongSlotIDHeader = "unknown-slot-uid";

		final CMSParagraphComponentData component = new CMSParagraphComponentData();

		component.setName(nameNewComponent);
		component.setSlotId(someWrongSlotIDHeader);
		component.setTypeCode(CMSParagraphComponentModel._TYPECODE);
		component.setUid(uidNewComponent);
		component.setPosition(SLOT_POSITION);
		component.setPageId(PAGE_ID);

		final ApiClient apiClient = getApiClientInstance();


		final Response<ValidationErrorResponse> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultCatalogAndCatalogVersion(CREATE_ITEM_ENDPOINT, Maps.newHashMap())).acceptJson()
				.acceptLanguages(Locale.ENGLISH).post(component, ValidationErrorResponse.class);

		final ValidationErrorResponse errorResponse = response.getErrorResponse();

		assertEquals(1, errorResponse.getErrors().size());
		assertEquals(CreateComponentValidator.SLOT_ID, errorResponse.getErrors().get(0).getSubject());

		assertStatusCode(response, 400);
	}

	@Test
	public void shouldNotCreateNewComponentBasedOnType_TypeRestrictionValidation() throws Exception
	{
		createHomeAppleCatalogPageHeaderWithParagraphWithoutRestriction();

		final String uidNewComponent = "uid-new-component-added";
		final String nameNewComponent = "name-new-component-added";

		final CMSParagraphComponentData component = new CMSParagraphComponentData();

		component.setName(nameNewComponent);
		component.setSlotId(ContentSlotModelMother.UID_HEADER);
		component.setTypeCode(ProductListComponentModel._TYPECODE);
		component.setUid(uidNewComponent);
		component.setPosition(SLOT_POSITION);
		component.setPageId(PAGE_ID);
		final ApiClient apiClient = getApiClientInstance();
		final Response<ValidationErrorResponse> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultCatalogAndCatalogVersion(CREATE_ITEM_ENDPOINT, Maps.newHashMap())).acceptJson()
				.acceptLanguages(Locale.ENGLISH).post(component, ValidationErrorResponse.class);

		final ValidationErrorResponse errorResponse = response.getErrorResponse();

		assertEquals(1, errorResponse.getErrors().size());
		assertEquals(CreateComponentValidator.UID, errorResponse.getErrors().get(0).getSubject());

		assertStatusCode(response, 400);
	}


	@Test
	public void shouldNotCreateNewComponent_InvalidSlotPosition() throws Exception
	{
		createHomeAppleCatalogPageHeaderWithParagraph_WithTypeRestrictions();

		final String uidNewComponent = "uid-new-component-added";
		final String nameNewComponent = "name-new-component-added";

		final CMSParagraphComponentData component = new CMSParagraphComponentData();

		component.setName(nameNewComponent);
		component.setSlotId(ContentSlotModelMother.UID_HEADER);
		component.setTypeCode(CMSParagraphComponentModel._TYPECODE);
		component.setUid(uidNewComponent);
		component.setPosition(null);
		component.setPageId(PAGE_ID);

		final ApiClient apiClient = getApiClientInstance();
		final Response<ValidationErrorResponse> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultCatalogAndCatalogVersion(CREATE_ITEM_ENDPOINT, Maps.newHashMap())).acceptJson()
				.acceptLanguages(Locale.ENGLISH).post(component, ValidationErrorResponse.class);

		final ValidationErrorResponse errorResponse = response.getErrorResponse();

		assertEquals(1, errorResponse.getErrors().size());
		assertEquals(CreateComponentValidator.POSITION, errorResponse.getErrors().get(0).getSubject());

		assertStatusCode(response, 400);
	}


	@SuppressWarnings("unchecked")
	@Test
	public void shouldNotCreateNewComponent_InvalidPageId() throws Exception
	{
		createHomeAppleCatalogPageHeaderWithParagraph_WithTypeRestrictions();

		final String uidNewComponent = "uid-new-component-added";
		final String nameNewComponent = "name-new-component-added";

		final CMSParagraphComponentData component = new CMSParagraphComponentData();

		component.setName(nameNewComponent);
		component.setSlotId(ContentSlotModelMother.UID_HEADER);
		component.setTypeCode(CMSParagraphComponentModel._TYPECODE);
		component.setUid(uidNewComponent);
		component.setPosition(SLOT_POSITION);
		component.setPageId(INVALID_PAGE_ID);

		final ApiClient apiClient = getApiClientInstance();
		final Response<ValidationErrorResponse> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultCatalogAndCatalogVersion(CREATE_ITEM_ENDPOINT, Maps.newHashMap())).acceptJson()
				.acceptLanguages(Locale.ENGLISH).post(component, ValidationErrorResponse.class);

		final ValidationErrorResponse errorResponse = response.getErrorResponse();

		assertEquals(1, errorResponse.getErrors().size());
		assertEquals(CreateComponentValidator.PAGE_ID, errorResponse.getErrors().get(0).getSubject());

		assertStatusCode(response, 400);
	}

	@Test
	public void testGetOneItemWithMultipleLanguageHeaders() throws Exception
	{
		createHomeAppleCatalogPageHeaderWithParagraphWithoutRestriction();

		final ApiClient apiClient = getApiClientInstance();

		final Map<String, String> uriVariables = Maps.newHashMap();
		uriVariables.put(UID, ParagraphComponentModelMother.UID_HEADER);

		final String endPoint = replaceUriVariablesWithDefaultCatalogAndCatalogVersion(GET_ONE_ENDPOINT, uriVariables);

		final Response<ValidationErrorResponse> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultCatalogAndCatalogVersion(endPoint, Maps.newHashMap())).acceptJson()
				.acceptLanguages(Locale.ENGLISH).hybrisLanguages(Locale.ENGLISH).get(ValidationErrorResponse.class);

		final ValidationErrorResponse errorResponse = response.getErrorResponse();

		assertEquals(1, errorResponse.getErrors().size());
		assertEquals("LanguageConflictPreConditionFailedError", errorResponse.getErrors().get(0).getType());
		assertStatusCode(response, 412);
	}


	@Test
	public void testPostItemWithMultipleAcceptLanguageHeaders() throws Exception
	{
		createHomeAppleCatalogPageHeaderWithParagraphWithoutRestriction();

		final ApiClient apiClient = getApiClientInstance();

		final Map<String, String> uriVariables = Maps.newHashMap();
		uriVariables.put(UID, ParagraphComponentModelMother.UID_HEADER);

		final String endPoint = replaceUriVariablesWithDefaultCatalogAndCatalogVersion(CREATE_ITEM_ENDPOINT, uriVariables);

		final Response<Void> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultCatalogAndCatalogVersion(endPoint, Maps.newHashMap())).acceptJson()
				.acceptLanguages(Locale.ENGLISH, Locale.FRENCH).post(new CMSParagraphComponentData());

		assertStatusCode(response, 412);
	}


	@Test
	public void shouldUpdateOneItemExceptUIDWithMultipleHybrisLanguages() throws Exception
	{
		createHomeAppleCatalogPageHeaderWithParagraphWithoutRestriction();

		// creates a store with english and french languages
		baseStoreModelMother.createNorthAmerica(catalogVersion);

		final LocalizedValueMap localizedContent = new LocalizedValueMap();
		final Map<String, String> localizedMap = new HashMap<>();
		localizedMap.put(LOCALE_ENGLISH.getLanguage(), NEW_CONTENT_HEADER_EN);
		localizedMap.put(LOCALE_FRENCH.getLanguage(), NEW_CONTENT_HEADER_FR);
		localizedContent.setValue(localizedMap);

		final CMSParagraphComponentData component = new CMSParagraphComponentData();
		component.setTypeCode(CMSParagraphComponentModel._TYPECODE);
		component.setUid(ParagraphComponentModelMother.UID_HEADER);
		component.setContent(localizedContent);
		component.setName(NEW_NAME_HEADER);

		ApiClient apiClient = getApiClientInstance();

		final Map<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put(UID, ParagraphComponentModelMother.UID_HEADER);


		final Response<Void> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultCatalogAndCatalogVersion(UPDATE_ITEM_ENDPOINT, uriVariables)).acceptJson()
				.acceptLanguages(null)
				.hybrisLanguages(LOCALE_ENGLISH, LOCALE_FRENCH)
				.put(component);

		assertStatusCode(response, 204);


		apiClient = getApiClientInstance();
		final Response<CMSParagraphComponentData> responseGet = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultCatalogAndCatalogVersion(GET_ONE_ENDPOINT, uriVariables)).acceptJson()
				.acceptLanguages(null).hybrisLanguages(LOCALE_ENGLISH, LOCALE_FRENCH)
				.get(CMSParagraphComponentData.class);

		assertStatusCode(responseGet, 200);
		assertTrue(responseGet.getBody().getContent() instanceof LocalizedValueMap);
		final LocalizedValueMap responseLocalizedValue = (LocalizedValueMap) responseGet.getBody().getContent();

		Assert.assertTrue(responseLocalizedValue.getValue() instanceof Map);
		Assert.assertEquals(NEW_CONTENT_HEADER_EN, responseLocalizedValue.getValue().get(LOCALE_ENGLISH.getLanguage()));
		Assert.assertEquals(NEW_CONTENT_HEADER_FR, responseLocalizedValue.getValue().get(LOCALE_FRENCH.getLanguage()));
		Assert.assertEquals(NEW_NAME_HEADER, responseGet.getBody().getName());
	}

	@Ignore("Ignore for now. Updating the FR content to null will throw a ValidationError because the field is required. "
			+ "Revisit after implementing validation for required fields/languages")
	@Test
	public void shouldUpdateOneItemExceptUIDWithMultipleHybrisLanguagesOneValueIsNull() throws Exception
	{
		createHomeAppleCatalogPageHeaderWithParagraphWithoutRestriction();

		// creates a store with english and french languages
		baseStoreModelMother.createNorthAmerica(catalogVersion);

		final LocalizedValueMap localizedContent = new LocalizedValueMap();
		final Map<String, String> localizedMap = new HashMap<>();
		localizedMap.put(LOCALE_ENGLISH.getLanguage(), NEW_CONTENT_HEADER_EN);
		localizedContent.setValue(localizedMap);

		final CMSParagraphComponentData component = new CMSParagraphComponentData();
		component.setTypeCode(CMSParagraphComponentModel._TYPECODE);
		component.setUid(ParagraphComponentModelMother.UID_HEADER);
		component.setContent(localizedContent);
		component.setName(NEW_NAME_HEADER);

		ApiClient apiClient = getApiClientInstance();

		final Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put(UID, ParagraphComponentModelMother.UID_HEADER);


		final Response<Void> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultCatalogAndCatalogVersion(UPDATE_ITEM_ENDPOINT, uriVariables)).acceptJson()
				.acceptLanguages(null)
				.hybrisLanguages(LOCALE_ENGLISH, LOCALE_FRENCH)
				.put(component);

		assertStatusCode(response, 204);


		apiClient = getApiClientInstance();
		final Response<CMSParagraphComponentData> responseGet = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultCatalogAndCatalogVersion(GET_ONE_ENDPOINT, uriVariables)).acceptJson()
				.acceptLanguages(null).hybrisLanguages(LOCALE_ENGLISH, LOCALE_FRENCH)
				.get(CMSParagraphComponentData.class);

		assertStatusCode(responseGet, 200);
		assertTrue(responseGet.getBody().getContent() instanceof LocalizedValueMap);
		final LocalizedValueMap responseLocalizedValue = (LocalizedValueMap) responseGet.getBody().getContent();

		Assert.assertTrue(responseLocalizedValue.getValue() instanceof Map);
		Assert.assertEquals(NEW_CONTENT_HEADER_EN, responseLocalizedValue.getValue().get(LOCALE_ENGLISH.getLanguage()));
		Assert.assertNull(responseLocalizedValue.getValue().get(LOCALE_FRENCH.getLanguage()));
		Assert.assertEquals(NEW_NAME_HEADER, responseGet.getBody().getName());
	}

	/**
	 * Get the component with the matching uid.
	 *
	 * @param items
	 *           - list of component items
	 * @param uid
	 *           - the uid to search for
	 * @return the component with the matching uid
	 */
	protected AbstractCMSComponentData getComponentItemByUid(final List<AbstractCMSComponentData> items, final String uid)
	{
		return items.stream().filter(item -> item.getUid().equals(uid)).findAny().get();
	}
}
