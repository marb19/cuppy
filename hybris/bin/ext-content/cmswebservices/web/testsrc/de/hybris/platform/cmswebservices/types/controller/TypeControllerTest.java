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
package de.hybris.platform.cmswebservices.types.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmswebservices.data.ComponentTypeData;
import de.hybris.platform.cmswebservices.data.ComponentTypeListData;
import de.hybris.platform.cmswebservices.types.facade.ComponentTypeFacade;
import de.hybris.platform.cmswebservices.types.facade.ComponentTypeNotFoundException;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class TypeControllerTest
{
	private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	private static final String REST_BASE_URI = "/types";
	private static final String BANNER_CODE = "testBannerComponentType";
	private static final String FOOTER_CODE = "testFooterComponentType";

	@Mock
	private ComponentTypeFacade componentTypeFacade;

	@InjectMocks
	private TypeController typeController;

	private ComponentTypeData bannerDto;
	private ComponentTypeData footerDto;

	private MockMvc mockMvc;

	@Before
	public void setUp()
	{
		mockMvc = MockMvcBuilders.standaloneSetup(typeController)
				.setMessageConverters(new MappingJackson2HttpMessageConverter()).build();

		bannerDto = new ComponentTypeData();
		bannerDto.setCode(BANNER_CODE);
		footerDto = new ComponentTypeData();
		footerDto.setCode(FOOTER_CODE);
	}

	@Test
	public void shouldGetNoComponentType()
	{
		when(componentTypeFacade.getAllComponentTypes()).thenReturn(Collections.emptyList());

		final ComponentTypeListData listDto = typeController.getAllComponentTypes();

		assertTrue(listDto.getComponentTypes().isEmpty());
	}

	@Test
	public void shouldGetAllComponentTypes()
	{
		when(componentTypeFacade.getAllComponentTypes()).thenReturn(Arrays.asList(bannerDto, footerDto));

		final ComponentTypeListData listDto = typeController.getAllComponentTypes();

		assertEquals(2, listDto.getComponentTypes().size());
		assertEquals(bannerDto, listDto.getComponentTypes().get(0));
		assertEquals(footerDto, listDto.getComponentTypes().get(1));
	}

	@Test
	public void shouldGetAllComponentTypes_RestCall() throws Exception
	{
		when(componentTypeFacade.getAllComponentTypes()).thenReturn(Arrays.asList(bannerDto, footerDto));

		mockMvc
				.perform(get(REST_BASE_URI) //
				.accept(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andExpect(content().contentType(APPLICATION_JSON_UTF8)) //
				.andExpect(jsonPath("$.componentTypes", hasSize(2))) //
				.andExpect(jsonPath("$.componentTypes.[0].code", is(bannerDto.getCode()))) //
				.andExpect(jsonPath("$.componentTypes.[1].code", is(footerDto.getCode())));

		verify(componentTypeFacade).getAllComponentTypes();
		verifyNoMoreInteractions(componentTypeFacade);
	}

	@Test(expected = ComponentTypeNotFoundException.class)
	public void shouldFailGetComponentByCode_NotFound() throws ComponentTypeNotFoundException
	{
		when(componentTypeFacade.getComponentTypeByCode(BANNER_CODE)).thenThrow(new ComponentTypeNotFoundException("failed"));

		typeController.getComponentTypeByCode(BANNER_CODE);
	}

	@Test
	public void shouldGetComponentByCode() throws Exception
	{
		when(componentTypeFacade.getComponentTypeByCode(BANNER_CODE)).thenReturn(bannerDto);

		mockMvc
				.perform(get(REST_BASE_URI + "/" + BANNER_CODE) //
						.accept(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andExpect(content().contentType(APPLICATION_JSON_UTF8)) //
				.andExpect(jsonPath("$.code", is(bannerDto.getCode()))); //

		verify(componentTypeFacade).getComponentTypeByCode(BANNER_CODE);
		verifyNoMoreInteractions(componentTypeFacade);
	}

}
