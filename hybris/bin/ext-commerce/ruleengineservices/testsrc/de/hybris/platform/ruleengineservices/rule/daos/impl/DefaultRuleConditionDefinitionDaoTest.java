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
package de.hybris.platform.ruleengineservices.rule.daos.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.ruleengineservices.model.RuleConditionDefinitionModel;
import de.hybris.platform.ruleengineservices.rule.dao.RuleConditionDefinitionDao;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultRuleConditionDefinitionDaoTest extends ServicelayerTransactionalTest
{
	private static final String TEST_NAME1 = "Def1";

	@Resource
	private RuleConditionDefinitionDao ruleConditionDefinitionDao;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/ruleengineservices/test/rule/defaultRuleConditionDefinitionDaoTest.impex", "utf-8");
	}

	@Test
	public void testFindAll()
	{
		// when
		final List<RuleConditionDefinitionModel> definitions = ruleConditionDefinitionDao.findAllRuleConditionDefinitions();

		// then
		assertNotNull(definitions);
		assertEquals(2, definitions.size());

		final RuleConditionDefinitionModel definition = definitions.get(0);
		assertNotNull(definition);
		assertEquals(TEST_NAME1, definition.getName());
		assertEquals(2, definition.getCategories().size());
		assertEquals(2, definition.getParameters().size());
	}
}
