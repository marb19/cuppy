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
package de.hybris.platform.ruleengineservices.rule.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleTemplateModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultRuleServiceTest extends ServicelayerTransactionalTest
{

	@Resource
	private DefaultRuleService defaultRuleService;

	@Resource
	private ModelService modelService;

	@Before
	public void setUp() throws Exception
	{
		importCsv("/ruleengineservices/test/rule/DefaultRuleServiceTest.impex", "utf-8");
	}

	@Test
	public void testCreateRuleFromTemplate()
	{
		final SourceRuleTemplateModel ruleTemplate = modelService.create(SourceRuleTemplateModel.class);
		ruleTemplate.setCode("testRuleTemplate");
		ruleTemplate.setName("Test Rule Template");
		ruleTemplate.setConditions("[{\"parameters\":{},\"definitionId\":\"group\"}]");
		ruleTemplate.setActions("[{\"parameters\":{\"value\":{\"value\":10}},\"definitionId\":\"y_order_percentage_discount\"}]");

		final SourceRuleModel createdRule = defaultRuleService.createRuleFromTemplate(ruleTemplate);

		assertTrue(createdRule.getCode().startsWith(ruleTemplate.getCode() + "-"));
		assertEquals("Test Rule Template", createdRule.getName());
		assertEquals(ruleTemplate.getConditions(), createdRule.getConditions());
		assertEquals(ruleTemplate.getActions(), createdRule.getActions());
	}

	@Test
	public void testCreateRuleFromTemplateTemplateNull()
	{
		assertNull(defaultRuleService.createRuleFromTemplate(null));
	}
}
