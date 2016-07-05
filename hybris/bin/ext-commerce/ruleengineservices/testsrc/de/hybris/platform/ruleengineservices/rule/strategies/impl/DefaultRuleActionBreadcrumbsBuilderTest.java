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
package de.hybris.platform.ruleengineservices.rule.strategies.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ruleengineservices.RuleEngineServiceException;
import de.hybris.platform.ruleengineservices.rule.data.RuleActionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleActionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.util.impl.DefaultMessagePlaceholderReplacementStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultRuleActionBreadcrumbsBuilderTest
{
	private static final String DEFINITION_ID_1 = "actionDefinition1";
	private static final String BREADCRUMB_1 = DEFINITION_ID_1 + " " + "breadcrumb";
	private static final String PARAMETER_ID_1 = "parameterId1";
	private static final String PARAMETER_VALUE_1 = "parameterValue1";

	private static final String DEFINITION_ID_2 = "actionDefinition2";
	private static final String BREADCRUMB_2 = DEFINITION_ID_2 + " " + "breadcrumb";
	private static final String PARAMETER_ID_2 = "parameterId2";
	private static final String PARAMETER_VALUE_2 = "parameterValue2";

	@Rule
	@SuppressWarnings("PMD")
	public final ExpectedException expectedException = ExpectedException.none();

	private RuleActionDefinitionData ruleActionDefinition1;
	private RuleActionDefinitionData ruleActionDefinition2;

	private RuleActionData ruleAction1;
	private RuleActionData ruleAction2;

	private Map<String, RuleActionDefinitionData> ruleActionDefinitions;
	private List<RuleActionData> ruleActions;

	@InjectMocks
	private final DefaultRuleActionBreadcrumbsBuilder ruleActionsMessageBuilder = new DefaultRuleActionBreadcrumbsBuilder();

	private final DefaultMessagePlaceholderReplacementStrategy replacementStrategy = new DefaultMessagePlaceholderReplacementStrategy();

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		ruleActionDefinition1 = new RuleActionDefinitionData();
		ruleActionDefinition1.setBreadcrumb(BREADCRUMB_1 + " " + createProperVariablePlaceholderForBreadcrumb(PARAMETER_ID_1));

		ruleActionDefinition2 = new RuleActionDefinitionData();
		ruleActionDefinition2.setBreadcrumb(BREADCRUMB_2 + " " + createProperVariablePlaceholderForBreadcrumb(PARAMETER_ID_2));

		ruleActionDefinitions = new HashMap<String, RuleActionDefinitionData>();
		ruleActionDefinitions.put(DEFINITION_ID_1, ruleActionDefinition1);
		ruleActionDefinitions.put(DEFINITION_ID_2, ruleActionDefinition2);

		final RuleParameterData parameter1 = new RuleParameterData();
		parameter1.setValue(PARAMETER_VALUE_1);

		ruleAction1 = new RuleActionData();
		ruleAction1.setDefinitionId(DEFINITION_ID_1);
		ruleAction1.setParameters(Collections.singletonMap(PARAMETER_ID_1, parameter1));

		final RuleParameterData parameter2 = new RuleParameterData();
		parameter2.setValue(PARAMETER_VALUE_2);

		ruleAction2 = new RuleActionData();
		ruleAction2.setDefinitionId(DEFINITION_ID_2);
		ruleAction2.setParameters(Collections.singletonMap(PARAMETER_ID_2, parameter2));


		ruleActions = new ArrayList<RuleActionData>();
		ruleActions.add(ruleAction1);
		ruleActions.add(ruleAction2);

		ruleActionsMessageBuilder.setReplacementStrategy(replacementStrategy);
	}

	@Test
	public void buildBreadcrumbsNullParameters() throws Exception
	{
		//expect
		expectedException.expect(IllegalArgumentException.class);

		//when
		ruleActionsMessageBuilder.buildActionBreadcrumbs(null, null);
	}

	@Test
	public void buildBreadcrumbsEmptyParameters() throws Exception
	{
		//when
		final String breadcrumb = ruleActionsMessageBuilder.buildActionBreadcrumbs(Collections.EMPTY_LIST, Collections.EMPTY_MAP);

		//then
		Assert.assertTrue(StringUtils.isEmpty(breadcrumb));
	}

	@Test
	public void buildBreadcrumbsNoDefinitionFound() throws Exception
	{
		//expect
		expectedException.expect(RuleEngineServiceException.class);

		//when
		ruleActionsMessageBuilder.buildActionBreadcrumbs(ruleActions, Collections.EMPTY_MAP);
	}

	@Test
	public void buildBreadcrumbsWithValidActionBreadcrumbs() throws Exception
	{
		//given
		ruleActionDefinition1.setBreadcrumb(BREADCRUMB_1);
		ruleActionDefinition2.setBreadcrumb(BREADCRUMB_2);
		ruleAction1.setParameters(Collections.EMPTY_MAP);
		ruleAction2.setParameters(Collections.EMPTY_MAP);

		//when
		final String breadcrumb = ruleActionsMessageBuilder.buildActionBreadcrumbs(ruleActions, ruleActionDefinitions);

		//then
		Assert.assertTrue(breadcrumb.contains(BREADCRUMB_1));
		Assert.assertTrue(breadcrumb.contains(BREADCRUMB_2));
	}

	@Test
	public void buildBreadcrumbsWithParameters() throws Exception
	{
		//when
		final String breadcrumb = ruleActionsMessageBuilder.buildActionBreadcrumbs(ruleActions, ruleActionDefinitions);

		//then
		Assert.assertFalse(breadcrumb.contains(PARAMETER_ID_1));
		Assert.assertFalse(breadcrumb.contains(PARAMETER_ID_2));
		Assert.assertTrue(breadcrumb.contains(PARAMETER_VALUE_1));
		Assert.assertTrue(breadcrumb.contains(PARAMETER_VALUE_2));
	}

	private String createProperVariablePlaceholderForBreadcrumb(final String parameterName)
	{
		return "{" + parameterName + "}";
	}
}
