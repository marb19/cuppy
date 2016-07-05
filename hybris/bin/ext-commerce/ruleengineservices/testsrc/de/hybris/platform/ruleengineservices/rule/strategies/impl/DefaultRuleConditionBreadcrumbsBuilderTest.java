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
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.util.impl.DefaultMessagePlaceholderReplacementStrategy;
import de.hybris.platform.servicelayer.i18n.L10NService;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultRuleConditionBreadcrumbsBuilderTest
{
	private static final String DEFINITION_ID_1 = "conditionDefinition1";
	private static final String BREADCRUMB_1 = DEFINITION_ID_1 + " " + "breadcrumb";
	private static final String PARAMETER_ID_1 = "parameterId1";
	private static final String PARAMETER_VALUE_1 = "parameterValue1";

	private static final String DEFINITION_ID_2 = "conditionDefinition2";
	private static final String BREADCRUMB_2 = DEFINITION_ID_2 + " " + "breadcrumb";
	private static final String PARAMETER_ID_2 = "parameterId2";
	private static final String PARAMETER_VALUE_2 = "parameterValue2";

	private static final String GROUP_DEFINITION_ID = DefaultRuleConditionBreadcrumbsBuilder.GROUP_CONDITION_DEFINITION_ID;
	private static final String GROUP_OPERATOR_PARAMETER_ID = "groupParameterId";
	private static final String GROUP_PARAMETER_VALUE = "groupParameterValue";

	private static final String GROUP_OPERATOR_AND_KEY = "de.hybris.platform.ruleengineservices.definitions.conditions.rulegroupoperator.and.name";
	private static final String GROUP_OPERATOR_AND_VALUE = "AND";

	private static final String GROUP_OPERATOR_OR_KEY = "de.hybris.platform.ruleengineservices.definitions.conditions.rulegroupoperator.or.name";
	private static final String GROUP_OPERATOR_OR_VALUE = "OR";

	@Rule
	@SuppressWarnings("PMD")
	public final ExpectedException expectedException = ExpectedException.none();

	private RuleConditionDefinitionData groupRuleConditionDefinition;
	private RuleConditionDefinitionData ruleConditionDefinition1;
	private RuleConditionDefinitionData ruleConditionDefinition2;

	private RuleConditionData groupRuleCondition;
	private RuleConditionData ruleCondition1;
	private RuleConditionData ruleCondition2;

	private List<RuleConditionData> ruleConditions;
	private Map<String, RuleConditionDefinitionData> ruleConditionDefinitions;

	@Mock
	private L10NService l10NService;

	private final DefaultMessagePlaceholderReplacementStrategy replacementStrategy = new DefaultMessagePlaceholderReplacementStrategy();

	private DefaultRuleConditionBreadcrumbsBuilder ruleConditionsMessageBuilder;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		ruleConditionDefinition1 = new RuleConditionDefinitionData();
		ruleConditionDefinition1.setBreadcrumb(BREADCRUMB_1 + " " + createProperVariablePlaceholderForBreadcrumb(PARAMETER_ID_1));
		ruleConditionDefinition1.setAllowsChildren(Boolean.FALSE);

		ruleConditionDefinition2 = new RuleConditionDefinitionData();
		ruleConditionDefinition2.setBreadcrumb(BREADCRUMB_2 + " " + createProperVariablePlaceholderForBreadcrumb(PARAMETER_ID_2));
		ruleConditionDefinition2.setAllowsChildren(Boolean.FALSE);

		groupRuleConditionDefinition = new RuleConditionDefinitionData();
		groupRuleConditionDefinition.setAllowsChildren(Boolean.TRUE);

		ruleConditionDefinitions = new HashMap<String, RuleConditionDefinitionData>();
		ruleConditionDefinitions.put(DEFINITION_ID_1, ruleConditionDefinition1);
		ruleConditionDefinitions.put(DEFINITION_ID_2, ruleConditionDefinition2);
		ruleConditionDefinitions.put(GROUP_DEFINITION_ID, groupRuleConditionDefinition);

		final Map<String, Object> groupRuleConditionParameters = new HashMap<>();
		groupRuleConditionParameters.put(GROUP_OPERATOR_PARAMETER_ID, GROUP_PARAMETER_VALUE);

		final RuleParameterData parameter1 = new RuleParameterData();
		parameter1.setValue(PARAMETER_VALUE_1);

		ruleCondition1 = new RuleConditionData();
		ruleCondition1.setDefinitionId(DEFINITION_ID_1);
		ruleCondition1.setParameters(Collections.singletonMap(PARAMETER_ID_1, parameter1));

		final RuleParameterData parameter2 = new RuleParameterData();
		parameter2.setValue(PARAMETER_VALUE_2);

		ruleCondition2 = new RuleConditionData();
		ruleCondition2.setDefinitionId(DEFINITION_ID_2);
		ruleCondition2.setParameters(Collections.singletonMap(PARAMETER_ID_2, parameter2));

		final RuleParameterData groupParameter = new RuleParameterData();
		groupParameter.setValue(GROUP_PARAMETER_VALUE);

		groupRuleCondition = new RuleConditionData();
		groupRuleCondition.setDefinitionId(GROUP_DEFINITION_ID);
		groupRuleCondition.setParameters(Collections.singletonMap(GROUP_OPERATOR_PARAMETER_ID, groupParameter));

		ruleConditions = new ArrayList<RuleConditionData>();
		ruleConditions.add(ruleCondition1);
		ruleConditions.add(ruleCondition2);

		ruleConditionsMessageBuilder = new DefaultRuleConditionBreadcrumbsBuilder();
		ruleConditionsMessageBuilder.setL10NService(l10NService);
		ruleConditionsMessageBuilder.setReplacementStrategy(replacementStrategy);

		Mockito.when(l10NService.getLocalizedString(GROUP_OPERATOR_AND_KEY)).thenReturn(GROUP_OPERATOR_AND_VALUE);
		Mockito.when(l10NService.getLocalizedString(GROUP_OPERATOR_OR_KEY)).thenReturn(GROUP_OPERATOR_OR_VALUE);
	}

	@Test
	public void buildBreadcrumbsNullParameters() throws Exception
	{
		//expect
		expectedException.expect(IllegalArgumentException.class);

		//when
		ruleConditionsMessageBuilder.buildConditionBreadcrumbs(null, null);
	}

	@Test
	public void buildBreadcrumbsEmptyParameters() throws Exception
	{
		//when
		final String breadcrumb = ruleConditionsMessageBuilder.buildConditionBreadcrumbs(Collections.EMPTY_LIST,
				Collections.EMPTY_MAP);

		//then
		Assert.assertTrue(StringUtils.isEmpty(breadcrumb));
	}

	@Test
	public void buildBreadcrumbsNoDefinitionFound() throws Exception
	{
		//expect
		expectedException.expect(RuleEngineServiceException.class);

		//when
		ruleConditionsMessageBuilder.buildConditionBreadcrumbs(ruleConditions, Collections.EMPTY_MAP);
	}

	@Test
	public void buildBreadcrumbsWithValidConditionBreadcrumbs() throws Exception
	{
		//given
		ruleConditionDefinition1.setBreadcrumb(BREADCRUMB_1);
		ruleConditionDefinition2.setBreadcrumb(BREADCRUMB_2);
		ruleCondition1.setParameters(Collections.emptyMap());
		ruleCondition2.setParameters(Collections.emptyMap());

		//when
		final String breadcrumb = ruleConditionsMessageBuilder.buildConditionBreadcrumbs(ruleConditions, ruleConditionDefinitions);

		//then
		Assert.assertTrue(breadcrumb.contains(BREADCRUMB_1));
		Assert.assertTrue(breadcrumb.contains(BREADCRUMB_2));
	}

	@Test
	public void buildBreadcrumbsWithParameters() throws Exception
	{
		//when
		final String breadcrumb = ruleConditionsMessageBuilder.buildConditionBreadcrumbs(ruleConditions, ruleConditionDefinitions);

		//then
		Assert.assertFalse(breadcrumb.contains(PARAMETER_ID_1));
		Assert.assertFalse(breadcrumb.contains(PARAMETER_ID_2));
		Assert.assertTrue(breadcrumb.contains(PARAMETER_VALUE_1));
		Assert.assertTrue(breadcrumb.contains(PARAMETER_VALUE_2));
	}

	@Test
	public void buildBreadcrumbsConditionWithGroupedConditions() throws Exception
	{
		//given
		groupRuleCondition.setChildren(Arrays.asList(ruleCondition1, ruleCondition2));

		final List<RuleConditionData> groupedConditions = new ArrayList<>();
		groupedConditions.add(groupRuleCondition);

		//when
		final String breadcrumb = ruleConditionsMessageBuilder.buildConditionBreadcrumbs(groupedConditions,
				ruleConditionDefinitions);

		//then
		Assert.assertEquals("conditionDefinition1 breadcrumb parameterValue1 AND conditionDefinition2 breadcrumb parameterValue2",
				breadcrumb);
	}

	@Test
	public void buildBreadcrumbsConditionWithChildConditionsParentNoBreadcrumb() throws Exception
	{
		//given
		groupRuleConditionDefinition.setBreadcrumb(null);

		groupRuleCondition.setChildren(Arrays.asList(ruleCondition1, ruleCondition2));

		final List<RuleConditionData> groupedConditions = new ArrayList<>();
		groupedConditions.add(groupRuleCondition);

		//when
		final String breadcrumb = ruleConditionsMessageBuilder.buildConditionBreadcrumbs(groupedConditions,
				ruleConditionDefinitions);

		//then
		Assert.assertTrue(breadcrumb.contains(BREADCRUMB_1));
		Assert.assertTrue(breadcrumb.contains(BREADCRUMB_2));
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
