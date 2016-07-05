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
package de.hybris.platform.droolsruleengineservices.commerce.impl;

import static de.hybris.platform.ruleengine.constants.RuleEngineConstants.RULEMETADATA_RULECODE;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.when;

import de.hybris.platform.ruleengineservices.calculation.AbstractRuleEngineTest;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;

import org.drools.core.WorkingMemory;
import org.drools.core.definitions.rule.impl.RuleImpl;
import org.drools.core.spi.KnowledgeHelper;
import org.junit.Before;
import org.kie.api.runtime.ObjectFilter;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class AbstractRAOActionTest extends AbstractRuleEngineTest
{
	private RuleEngineResultRAO result;

	@Mock
	private KnowledgeHelper context;

	@Mock
	private WorkingMemory workingMemory;

	@Mock
	private RuleImpl rule;

	@Mock
	private Map<String, Object> metaData;

	@Before
	public void abstractSetUp()
	{
		MockitoAnnotations.initMocks(this);
		when(context.getRule()).thenReturn(rule);
		when(context.getWorkingMemory()).thenReturn(workingMemory);
		when(workingMemory.getFactHandles((ObjectFilter) notNull())).thenReturn(Collections.emptyList());
		when(rule.getMetaData()).thenReturn(metaData);
		when(metaData.get(RULEMETADATA_RULECODE)).thenReturn("notNullValue");
		when(rule.getName()).thenReturn("ruleName");

		result = new RuleEngineResultRAO();
		result.setActions(new LinkedHashSet<AbstractRuleActionRAO>());
	}

	protected RuleEngineResultRAO getResult()
	{
		return result;
	}

	protected KnowledgeHelper getContext()
	{
		return context;
	}
}
