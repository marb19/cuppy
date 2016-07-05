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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.cmswebservices.data.AbstractCMSComponentData;
import de.hybris.platform.cmswebservices.data.CMSParagraphComponentData;
import de.hybris.platform.cmswebservices.data.ComponentItemListData;
import de.hybris.platform.cmswebservices.items.facade.ComponentItemFacade;
import de.hybris.platform.cmswebservices.items.facade.populator.model.BasicCMSComponentModelPopulator;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;


@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class ItemControllerTest
{
	private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	private static final String REST_BASE_URI = "/sites/electronics/catalogs/electronics/versions/Staged/items";
	private static final String BANNER_CODE = "testBannerComponentType";
	private static final String BANNER_NAME = "Test Banner Component";
	private static final String BANNER_UID = "testBannerComponentId";
	private static final String FOOTER_CODE = "testFooterComponentType";
	private static final String FOOTER_NAME = "Test Footer Component Type";
	private static final String FOOTER_UID = "testFooterComponentId";

	private static final String TYPE_CODE = "typeCode";
	private static final String UID = "uid";
	private static final String NAME = "name";
	private static final String CONTENT = "content";

	@Mock
	private ComponentItemFacade componentFacade;
	@Mock
	private DataMapper dataMapper;
	@Mock
	private Comparator<AbstractCMSComponentModel> cmsItemComparator;
	@Mock
	private Map<Class, String> cmsComponentDtoFactory;
	@Mock
	private CMSParagraphComponentData cmsParagraphComponentData;
	@Mock
	private ObjectMapper objectMapper;

	@InjectMocks
	private ItemController itemController;

	private AbstractCMSComponentModel bannerComponentModel;
	private AbstractCMSComponentModel footerComponentModel;

	@Mock
	private AbstractPopulatingConverter<AbstractCMSComponentModel, AbstractCMSComponentData> basicCMSComponentModelConverter;
	private AbstractCMSComponentData bannerComponentData;
	private AbstractCMSComponentData footerComponentData;

	private MockMvc mockMvc;

	@Before
	public void setUp()
	{
		mockMvc = MockMvcBuilders.standaloneSetup(itemController).setMessageConverters(new MappingJackson2HttpMessageConverter())
				.build();
	}

	@SuppressWarnings("deprecation")
	private void initComponentItems()
	{
		final LocalDateTime bannerModifiedTime = LocalDateTime.of(2015, 1, 1, 10, 30);
		final LocalDateTime footerModifiedTime = LocalDateTime.of(2015, 1, 1, 10, 45);

		bannerComponentModel = spy(new AbstractCMSComponentModel());
		bannerComponentModel.setName(BANNER_NAME);
		bannerComponentModel.setUid(BANNER_UID);
		bannerComponentModel.setModifiedtime(Date.from(bannerModifiedTime.atZone(ZoneId.systemDefault()).toInstant()));
		when(bannerComponentModel.getPk()).thenReturn(PK.BIG_PK);
		doReturn(BANNER_CODE).when(bannerComponentModel).getTypeCode();

		footerComponentModel = spy(new AbstractCMSComponentModel());
		footerComponentModel.setName(FOOTER_NAME);
		footerComponentModel.setUid(FOOTER_UID);
		footerComponentModel.setModifiedtime(Date.from(footerModifiedTime.atZone(ZoneId.systemDefault()).toInstant()));
		when(footerComponentModel.getPk()).thenReturn(PK.BIG_PK);
		doReturn(FOOTER_CODE).when(footerComponentModel).getTypeCode();

		bannerComponentData = new AbstractCMSComponentData();
		bannerComponentData.setName(bannerComponentModel.getName());
		bannerComponentData.setUid(bannerComponentModel.getUid());
		bannerComponentData.setTypeCode(bannerComponentModel.getTypeCode());

		bannerComponentData = new AbstractCMSComponentData();
		bannerComponentData.setName(footerComponentModel.getName());
		bannerComponentData.setUid(footerComponentModel.getUid());
		bannerComponentData.setTypeCode(footerComponentModel.getTypeCode());
	}


	@Test
	public void shouldGetNoComponentItems() throws Exception
	{
		final List<AbstractCMSComponentData> componentItems = Arrays.asList();
		ComponentItemListData dataList = new ComponentItemListData();
		dataList.setComponentItems(componentItems);
		when(componentFacade.getAllComponentItems()).thenReturn(componentItems);
		when(dataMapper.map(any(), Mockito.eq(ComponentItemListData.class), Mockito.eq(ItemController.DEFAULT_FIELD_SET))).thenReturn(dataList);

		final ComponentItemListData allComponentItems = itemController.getAllComponentItems(ItemController.DEFAULT_FIELD_SET);

		assertTrue(allComponentItems.getComponentItems().isEmpty());
	}

	@Test
	public void shouldGetAllComponentItemsOrderByModifiedTimeDesc() throws Exception
	{
		initComponentItems();

		final BasicCMSComponentModelPopulator basicPopulator = new BasicCMSComponentModelPopulator();
		final AbstractCMSComponentData data1 = new AbstractCMSComponentData();
		basicPopulator.populate(bannerComponentModel, data1);
		final AbstractCMSComponentData data2 = new AbstractCMSComponentData();
		basicPopulator.populate(footerComponentModel, data2);


		final List<AbstractCMSComponentData> componentItems = Arrays.asList(data1, data2);
		when(componentFacade.getAllComponentItems()).thenReturn(componentItems);
		ComponentItemListData dataList = new ComponentItemListData();
		dataList.setComponentItems(componentItems);
		when(dataMapper.map(any(), Mockito.eq(ComponentItemListData.class), Mockito.eq(ItemController.DEFAULT_FIELD_SET))).thenReturn(dataList);

		when(cmsItemComparator.compare(any(), any())).thenReturn(-1).thenReturn(1);

		final ComponentItemListData allComponentItems = itemController.getAllComponentItems(ItemController.DEFAULT_FIELD_SET);

		assertEquals(2, allComponentItems.getComponentItems().size());
		assertEquals(data1, allComponentItems.getComponentItems().get(0));
		assertEquals(data2, allComponentItems.getComponentItems().get(1));
	}

	@Test
	public void shouldGetAllComponentItemsOrderByModifiedTimeDesc_RestCall() throws Exception
	{
		initComponentItems();

		final BasicCMSComponentModelPopulator basicPopulator = Mockito.spy(new BasicCMSComponentModelPopulator());
		final AbstractCMSComponentData data1 = new AbstractCMSComponentData();
		basicPopulator.populate(bannerComponentModel, data1);
		final AbstractCMSComponentData data2 = new AbstractCMSComponentData();
		basicPopulator.populate(footerComponentModel, data2);

		final List<AbstractCMSComponentData> componentItems = Arrays.asList(data1, data2);
		ComponentItemListData dataList = new ComponentItemListData();
		dataList.setComponentItems(componentItems);
		when(componentFacade.getAllComponentItems()).thenReturn(componentItems);
		when(dataMapper.map(any(), Mockito.eq(ComponentItemListData.class), Mockito.eq(ItemController.DEFAULT_FIELD_SET))).thenReturn(dataList);

		when(cmsItemComparator.compare(any(), any())).thenReturn(-1).thenReturn(1);

		mockMvc.perform(get(REST_BASE_URI) //
				.accept(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andExpect(content().contentType(APPLICATION_JSON_UTF8)) //
				.andExpect(jsonPath("$.componentItems", hasSize(2))) //
				.andExpect(jsonPath("$.componentItems.[0].name", is(bannerComponentModel.getName()))) //
				.andExpect(jsonPath("$.componentItems.[0].uid", is(bannerComponentModel.getUid()))) //
				.andExpect(jsonPath("$.componentItems.[0].typeCode", is(bannerComponentModel.getItemtype()))) //
				.andExpect(jsonPath("$.componentItems.[1].name", is(footerComponentModel.getName()))) //
				.andExpect(jsonPath("$.componentItems.[1].uid", is(footerComponentModel.getUid()))) //
				.andExpect(jsonPath("$.componentItems.[1].typeCode", is(footerComponentModel.getItemtype())));

		verify(componentFacade).getAllComponentItems();
		verifyNoMoreInteractions(componentFacade);
	}

	@Test
	public void shouldUpdateComponent() throws Exception
	{
		final String componentUid = "componentUid";

		when(cmsComponentDtoFactory.get(cmsParagraphComponentData.getClass())).thenReturn(CMSParagraphComponentModel._TYPECODE);

		itemController.updateComponent(componentUid, cmsParagraphComponentData);

		verify(componentFacade).updateComponentItem(componentUid, cmsParagraphComponentData);

	}

	@Test
	public void shouldGetComponentItem() throws CMSItemNotFoundException, ConversionException
	{
		final AbstractCMSComponentData cmsComponentData = Mockito.mock(AbstractCMSComponentData.class);
		when(componentFacade.getComponentItemByUid(Mockito.anyString())).then(a -> cmsComponentData);
		final AbstractCMSComponentData getComponentItemByUid = itemController.getComponentItemByUid(Mockito.anyString(), ItemController.DEFAULT_FIELD_SET);
		Mockito.verify(componentFacade).getComponentItemByUid(Mockito.anyString());
		org.junit.Assert.assertEquals("", cmsComponentData, getComponentItemByUid);
	}


	@Test(expected = CMSItemNotFoundException.class)
	public void shouldGetComponentItemThrowItemNotFoundException() throws CMSItemNotFoundException, ConversionException
	{
		final AbstractCMSComponentData cmsComponentData = Mockito.mock(AbstractCMSComponentData.class);
		when(componentFacade.getComponentItemByUid(Mockito.anyString())).thenThrow(new CMSItemNotFoundException(""));
		final AbstractCMSComponentData getComponentItemByUid = itemController.getComponentItemByUid(Mockito.anyString(), ItemController.DEFAULT_FIELD_SET);
	}

	@Test(expected = ConversionException.class)
	public void shouldGetComponentItemThrowConversionException() throws CMSItemNotFoundException, ConversionException
	{
		final AbstractCMSComponentData cmsComponentData = Mockito.mock(AbstractCMSComponentData.class);
		when(componentFacade.getComponentItemByUid(Mockito.anyString())).thenThrow(new ConversionException(""));
		final AbstractCMSComponentData getComponentItemByUid = itemController.getComponentItemByUid(Mockito.anyString(), ItemController.DEFAULT_FIELD_SET);
	}

}
