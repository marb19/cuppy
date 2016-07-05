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
package de.hybris.platform.cmswebservices.pagescontentslotscomponents.controller;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cmswebservices.data.PageContentSlotComponentData;
import de.hybris.platform.cmswebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.cmswebservices.util.apiclient.Response;
import de.hybris.platform.cmswebservices.util.apiclient.response.ValidationErrorResponse;
import de.hybris.platform.cmswebservices.util.apiclient.response.ValidationObjectError;
import de.hybris.platform.cmswebservices.util.models.CatalogVersionModelMother;
import de.hybris.platform.cmswebservices.util.models.ContentPageModelMother;
import de.hybris.platform.cmswebservices.util.models.ContentSlotForPageModelMother;
import de.hybris.platform.cmswebservices.util.models.ContentSlotModelMother;
import de.hybris.platform.cmswebservices.util.models.ContentSlotNameModelMother;
import de.hybris.platform.cmswebservices.util.models.PageTemplateModelMother;
import de.hybris.platform.cmswebservices.util.models.ParagraphComponentModelMother;
import de.hybris.platform.cmswebservices.util.models.SimpleBannerComponentModelMother;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.google.common.collect.Maps;


@IntegrationTest
public class PageContentSlotComponentControllerUpdateWebServiceTest extends ApiBaseIntegrationTest
{
	private static final String INVALID_SLOT_ID = "INVALID_SLOT_ID";
	private static final String UPDATE_ENDPOINT = "/v1/catalogs/{catalogId}/versions/{versionId}/pagescontentslotscomponents";
	private static final String SLOT_ID = "slotId";
	private static final String COMPONENT_ID = "componentId";
	private static final String PAGE_ID = "pageId";

	@Resource
	private CatalogVersionModelMother catalogVersionModelMother;
	@Resource
	private ContentSlotForPageModelMother contentSlotForPageModelMother;
	@Resource
	private ContentSlotNameModelMother contentSlotNameModelMother;
	@Resource
	private SimpleBannerComponentModelMother simpleBannerComponentModelMother;
	@Resource
	private ContentPageModelMother contentPageModelMother;
	@Resource
	private ParagraphComponentModelMother paragraphComponentModelMother;
	@Resource
	private ContentSlotModelMother contentSlotModelMother;
	@Resource
	private PageTemplateModelMother pageTemplateModelMother;

	private CatalogVersionModel catalogVersion;

	@Test
	public void shouldMoveComponentFromHeaderSlotToFootSlot() throws Exception
	{
		createParagraphWithinHeaderSlot_appleCatalog_emptyFootSlot();

		final PageContentSlotComponentData entity = new PageContentSlotComponentData();
		entity.setSlotId(ContentSlotModelMother.UID_FOOTER);
		entity.setComponentId(ParagraphComponentModelMother.UID_HEADER);
		entity.setPosition(0);
		entity.setPageId(ContentPageModelMother.UID_HOMEPAGE);

		final Response<Void> response = getApiClientInstance() //
				.request() //
				.endpoint(replaceUriVariablesWithDefaultCatalogAndCatalogVersion(UPDATE_ENDPOINT, Maps.newHashMap())) //
				.parameter(PAGE_ID, ContentPageModelMother.UID_HOMEPAGE) //
				.parameter(SLOT_ID, ContentSlotModelMother.UID_HEADER) //
				.parameter(COMPONENT_ID, ParagraphComponentModelMother.UID_HEADER) //
				.put(entity);
		assertStatusCode(response, HttpStatus.OK.value());
	}

	@Test
	public void shouldNotMoveComponentInvalidSlot() throws Exception
	{
		createParagraphWithinHeaderSlot_appleCatalog_emptyFootSlot();

		final PageContentSlotComponentData entity = new PageContentSlotComponentData();
		//invalid uid
		entity.setSlotId(INVALID_SLOT_ID);

		entity.setComponentId(ParagraphComponentModelMother.UID_FOOTER);
		entity.setPosition(0);
		entity.setPageId(ContentPageModelMother.UID_HOMEPAGE);

		final Response<Void> response = getApiClientInstance() //
				.request() //
				.endpoint(replaceUriVariablesWithDefaultCatalogAndCatalogVersion(UPDATE_ENDPOINT, Maps.newHashMap())) //
				.parameter(PAGE_ID, ContentPageModelMother.UID_HOMEPAGE) //
				.parameter(SLOT_ID, ContentSlotModelMother.UID_HEADER) //
				.parameter(COMPONENT_ID, ParagraphComponentModelMother.UID_HEADER) //
				.put(entity);
		assertStatusCode(response, HttpStatus.BAD_REQUEST.value());
	}

	@Test
	public void shouldNotMoveComponentValidationErrors() throws Exception
	{
		createParagraphWithinHeaderSlot_appleCatalog_emptyFootSlot();

		final PageContentSlotComponentData entity = new PageContentSlotComponentData();

		final Response<ValidationErrorResponse> response = getApiClientInstance() //
				.request() //
				.endpoint(replaceUriVariablesWithDefaultCatalogAndCatalogVersion(UPDATE_ENDPOINT, Maps.newHashMap())) //
				.parameter(PAGE_ID, ContentPageModelMother.UID_HOMEPAGE) //
				.parameter(SLOT_ID, ContentSlotModelMother.UID_HEADER) //
				.parameter(COMPONENT_ID, ParagraphComponentModelMother.UID_HEADER) //
				.put(entity, ValidationErrorResponse.class);

		final List<ValidationObjectError> errors = response.getErrorResponse().getErrors();
		assertEquals(4, errors.size());
		assertStatusCode(response, HttpStatus.BAD_REQUEST.value());
	}

	protected void createParagraphWithinHeaderSlot_appleCatalog_emptyFootSlot()
	{
		// Create catalog & catalog version
		catalogVersion = catalogVersionModelMother.createAppleStagedCatalogVersionModel();

		// Create homepage template
		final PageTemplateModel pageTemplate = pageTemplateModelMother.HomePage_Template(catalogVersion);

		//create home page
		contentPageModelMother.Homepage_Page(catalogVersion);

		// Create homepage page and content slot header with paragraph component
		contentSlotForPageModelMother.HeaderHomepage_ParagraphOnly(catalogVersion);
		contentSlotForPageModelMother.FooterHomepage_Empty(catalogVersion);

		// Create header content slot name with paragraph + banner restrictions
		contentSlotNameModelMother.Header(pageTemplate);

		// Create footer slot
		contentSlotNameModelMother.Footer(pageTemplate);
	}
}
