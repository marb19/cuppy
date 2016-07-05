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
package de.hybris.platform.droolsruleengineservices.impl;

import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsRuleEngineContextModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.util.Config;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests the validate-intercepter logic around the DroolsRule type.
 */
@IntegrationTest
public class DroolsRuleValidationTest extends AbstractRuleEngineServicesTest
{

	private boolean validateRuleName;
	private boolean validateRulePackage;
	private boolean validateRuleCode;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/droolsruleengineservices/test/ruleenginesetup.impex", "utf-8");

		// store current validation parameters
		validateRuleCode = Config.getBoolean("droolsruleengineservices.validate.droolsrule.rulecode", true);
		validateRuleName = Config.getBoolean("droolsruleengineservices.validate.droolsrule.rulename", true);
		validateRulePackage = Config.getBoolean("droolsruleengineservices.validate.droolsrule.rulepackage", true);
	}

	@After
	public void tearDown()
	{
		// reset validation parameters
		Config.setParameter("droolsruleengineservices.validate.droolsrule.rulecode", String.valueOf(validateRuleCode));
		Config.setParameter("droolsruleengineservices.validate.droolsrule.rulename", String.valueOf(validateRuleName));
		Config.setParameter("droolsruleengineservices.validate.droolsrule.rulepackage", String.valueOf(validateRulePackage));
	}

	@Test
	public void testDroolsRuleNameAndPackageValidateInterceptorNotTriggered() throws ImpExException, IOException
	{
		final DroolsRuleEngineContextModel context = (DroolsRuleEngineContextModel) getRuleEngineContextDao()
				.getRuleEngineContextByName(RULE_ENGINGE_CONTEXT_NAME);

		// the kbase to be used for the rules
		final DroolsKIEBaseModel kbase = context.getKieSession().getKieBase();

		// create rule1
		final DroolsRuleModel rule1 = getModelService().create(DroolsRuleModel.class);
		rule1.setCode("DroolsRuleValidationTest1");
		rule1.setUuid("interceptortest-rulename01");
		rule1.setRulePackage("de.hybris.platform.promotionengineservices.test");
		rule1.setRuleContent(readRuleFile("sameRule1.drl", "/droolsruleengineservices/test/rules/interceptors/"));
		rule1.setRuleType(RuleType.DEFAULT);
		getModelService().save(rule1);

		// add rule1 to kbase
		rule1.setKieBase(kbase);
		getModelService().save(rule1);

		// create rule3 with same name and package
		final DroolsRuleModel rule3 = getModelService().create(DroolsRuleModel.class);
		rule3.setCode("DroolsRuleValidationTest3");
		rule3.setUuid("interceptortest-rulename03");
		rule3.setRulePackage("de.hybris.platform.promotionengineservices.test");
		rule3.setRuleContent(readRuleFile("otherRule.drl", "/droolsruleengineservices/test/rules/interceptors/"));
		rule3.setRuleType(RuleType.DEFAULT);
		getModelService().save(rule3);

		// refresh the kbase to make sure rule1 shows up
		getModelService().refresh(kbase);

		// add rule2 to the same kbase should be ok as they have different names/packages
		rule3.setKieBase(kbase);
		getModelService().save(rule3);

	}

	@Test
	public void testDroolsRuleNameAndPackageValidateInterceptorTriggeredAfterCreate() throws ImpExException, IOException
	{
		final DroolsRuleEngineContextModel context = (DroolsRuleEngineContextModel) getRuleEngineContextDao()
				.getRuleEngineContextByName(RULE_ENGINGE_CONTEXT_NAME);

		// the kbase to be used for the rules
		final DroolsKIEBaseModel kbase = context.getKieSession().getKieBase();

		// create rule1
		final DroolsRuleModel rule1 = getModelService().create(DroolsRuleModel.class);
		rule1.setCode("DroolsRuleValidationTest1");
		rule1.setUuid("interceptortest-rulename01");
		rule1.setRulePackage("de.hybris.platform.promotionengineservices.test");
		rule1.setRuleContent(readRuleFile("sameRule1.drl", "/droolsruleengineservices/test/rules/interceptors/"));
		rule1.setRuleType(RuleType.DEFAULT);
		getModelService().save(rule1);

		// add rule1 to kbase
		rule1.setKieBase(kbase);
		getModelService().save(rule1);

		// create rule2 with same name and package
		final DroolsRuleModel rule2 = getModelService().create(DroolsRuleModel.class);
		rule2.setCode("DroolsRuleValidationTest2");
		rule2.setUuid("interceptortest-rulename01");
		rule2.setRulePackage("de.hybris.platform.promotionengineservices.test");
		rule2.setRuleContent(readRuleFile("sameRule2.drl", "/droolsruleengineservices/test/rules/interceptors/"));
		rule2.setRuleType(RuleType.DEFAULT);
		getModelService().save(rule2);

		// refresh the kbase to make sure rule1 shows up
		getModelService().refresh(kbase);

		// add rule2 to the same kbase should fail
		rule2.setKieBase(kbase);
		trySaveAndFailIfSucceeds(rule2);

	}

	@Test
	public void testDroolsRuleNameAndPackageValidateInterceptorTriggeredDuringCreate() throws ImpExException, IOException
	{
		final DroolsRuleEngineContextModel context = (DroolsRuleEngineContextModel) getRuleEngineContextDao()
				.getRuleEngineContextByName(RULE_ENGINGE_CONTEXT_NAME);

		// the kbase to be used for the rules
		final DroolsKIEBaseModel kbase = context.getKieSession().getKieBase();

		// create rule1
		final DroolsRuleModel rule1 = getModelService().create(DroolsRuleModel.class);
		rule1.setCode("DroolsRuleValidationTest1");
		rule1.setUuid("interceptortest-rulename01");
		rule1.setRulePackage("de.hybris.platform.promotionengineservices.test");
		rule1.setRuleContent(readRuleFile("sameRule1.drl", "/droolsruleengineservices/test/rules/interceptors/"));
		rule1.setRuleType(RuleType.DEFAULT);

		// create rule2 with same name and package
		final DroolsRuleModel rule2 = getModelService().create(DroolsRuleModel.class);
		rule2.setCode("DroolsRuleValidationTest2");
		rule2.setUuid("interceptortest-rulename01");
		rule2.setRulePackage("de.hybris.platform.promotionengineservices.test");
		rule2.setRuleContent(readRuleFile("sameRule2.drl", "/droolsruleengineservices/test/rules/interceptors/"));
		rule2.setRuleType(RuleType.DEFAULT);

		// add rule1, rule2 to the same kbase should fail

		final Set<DroolsRuleModel> rules = new HashSet<>();
		rules.add(rule1);
		rules.add(rule2);
		kbase.setRules(rules);
		try
		{
			getModelService().saveAll(rule1, rule2, kbase);
		}
		catch (final ModelSavingException e)
		{
			if (!(e.getCause() instanceof InterceptorException))
			{
				Assert.fail("Save didn't fail due to ModelSavingException with cause InterceptorException.");
			}
			return;
		}

		fail("should have caused interceptor to throw exception!");

	}

	@Test
	public void testDroolsRuleInvalidCharacterInRuleName() throws ImpExException, IOException
	{
		//enable validation
		Config.setParameter("droolsruleengineservices.validate.droolsrule.rulename", "true");
		// create rule
		final DroolsRuleModel rule = createDroolsRuleInvalidCharacterInRuleName();
		trySaveAndFailIfSucceeds(rule);
	}

	@Test
	public void testDroolsRuleInvalidCharacterInRuleNameValidationDisabled() throws ImpExException, IOException
	{
		// disable validation
		Config.setParameter("droolsruleengineservices.validate.droolsrule.rulename", "false");

		// create rule, save should be ok as validation is disabled.
		final DroolsRuleModel rule = createDroolsRuleInvalidCharacterInRuleName();
		getModelService().save(rule);
	}

	private DroolsRuleModel createDroolsRuleInvalidCharacterInRuleName() throws IOException
	{
		final DroolsRuleModel rule = getModelService().create(DroolsRuleModel.class);
		rule.setCode("DroolsRuleValidationTest4");
		rule.setUuid("interceptortest-invalidRuleName\"");
		rule.setRulePackage("de.hybris.platform.promotionengineservices.test");
		rule.setRuleContent(readRuleFile("invalidRuleName.drl", "/droolsruleengineservices/test/rules/interceptors/"));
		rule.setRuleType(RuleType.DEFAULT);
		return rule;
	}

	@Test
	public void testDroolsRuleInvalidCharacterInRuleCode() throws ImpExException, IOException
	{
		// create rule
		final DroolsRuleModel rule = createDroolsRuleInvalidCharacterInRuleCode();
		trySaveAndFailIfSucceeds(rule);
	}

	@Test
	public void testDroolsRuleInvalidCharacterInRuleCodeValidationDisabled() throws ImpExException, IOException
	{
		// disable validation
		Config.setParameter("droolsruleengineservices.validate.droolsrule.rulecode", "false");

		// create rule, save should be ok as validation is disabled.
		final DroolsRuleModel rule = createDroolsRuleInvalidCharacterInRuleCode();
		getModelService().save(rule);
	}

	private DroolsRuleModel createDroolsRuleInvalidCharacterInRuleCode() throws IOException
	{
		final DroolsRuleModel rule = getModelService().create(DroolsRuleModel.class);
		rule.setCode("DroolsRuleValidationTest5\"");
		rule.setUuid("interceptortest-invalidRuleCode");
		rule.setRulePackage("de.hybris.platform.promotionengineservices.test");
		rule.setRuleContent(readRuleFile("invalidRuleCode.drl", "/droolsruleengineservices/test/rules/interceptors/"));
		rule.setRuleType(RuleType.DEFAULT);
		return rule;
	}

	@Test
	public void testDroolsRuleMismatchRuleCode() throws IOException
	{
		// create rule
		final DroolsRuleModel rule = createMismatchRule("mismatchRule.drl", "THE WRONG RULECODE", "interceptortest-mismatchRule",
				"de.hybris.platform.promotionengineservices.test");
		trySaveAndFailIfSucceeds(rule);
	}

	@Test
	public void testDroolsRuleWithWhiteSpacesInRuleCode() throws IOException
	{
		// create rule
		final DroolsRuleModel rule = createMismatchRule("whiteSpacesRuleCodeRule.drl", "DroolsRuleValidationTest7",
				"interceptortest-mismatchRule", "de.hybris.platform.promotionengineservices.test");
		getModelService().save(rule);
	}

	@Test
	public void testDroolsRuleMismatchRuleCodeValidationDisabled() throws IOException
	{
		// disable validation
		Config.setParameter("droolsruleengineservices.validate.droolsrule.rulecode", "false");

		// create rule, save should be ok as validation is disabled.
		final DroolsRuleModel rule = createMismatchRule("mismatchRule.drl", "THE WRONG RULECODE", "interceptortest-mismatchRule",
				"de.hybris.platform.promotionengineservices.test");
		getModelService().save(rule);
	}


	@Test
	public void testDroolsRuleMismatchRuleName() throws IOException
	{
		//enable validation
		Config.setParameter("droolsruleengineservices.validate.droolsrule.rulename", "true");
		//create rule
		final DroolsRuleModel rule = createMismatchRule("mismatchRule.drl", "DroolsRuleValidationTest6", "THE WRONG\" RULENAME",
				"de.hybris.platform.promotionengineservices.test");
		trySaveAndFailIfSucceeds(rule);
	}

	@Test
	public void testDroolsRuleMismatchRuleNameValidationDisabled() throws IOException
	{
		// disable validation
		Config.setParameter("droolsruleengineservices.validate.droolsrule.rulename", "false");

		// create rule, save should be ok as validation is disabled.
		final DroolsRuleModel rule = createMismatchRule("mismatchRule.drl", "DroolsRuleValidationTest6", "THE WRONG\" RULENAME",
				"de.hybris.platform.promotionengineservices.test");
		getModelService().save(rule);
	}

	@Test
	public void testDroolsRuleMismatchRulePackage() throws IOException
	{
		// create rule
		final DroolsRuleModel rule = createMismatchRule("mismatchRule.drl", "DroolsRuleValidationTest6",
				"interceptortest-mismatchRule", "de.hybris.platform.promotionengineservices.WRONG.PACKAGE");
		trySaveAndFailIfSucceeds(rule);
	}

	@Test
	public void testDroolsRuleMismatchRulePackageValidationDisabled() throws IOException
	{
		// disable validation
		Config.setParameter("droolsruleengineservices.validate.droolsrule.rulepackage", "false");

		// create rule, save should be ok as validation is disabled.
		final DroolsRuleModel rule = createMismatchRule("mismatchRule.drl", "DroolsRuleValidationTest6",
				"interceptortest-mismatchRule", "de.hybris.platform.promotionengineservices.WRONG.PACKAGE");
		getModelService().save(rule);
	}

	private DroolsRuleModel createMismatchRule(final String ruleFileName, final String ruleCode, final String uuid,
			final String rulePackage) throws IOException
	{
		final DroolsRuleModel rule = getModelService().create(DroolsRuleModel.class);
		rule.setCode(ruleCode);
		rule.setUuid(uuid);
		rule.setRulePackage(rulePackage);
		rule.setRuleContent(readRuleFile(ruleFileName, "/droolsruleengineservices/test/rules/interceptors/"));
		rule.setRuleType(RuleType.DEFAULT);
		return rule;
	}

	private void trySaveAndFailIfSucceeds(final DroolsRuleModel rule)
	{
		try
		{
			getModelService().save(rule);
		}
		catch (final ModelSavingException e)
		{
			if (!(e.getCause() instanceof InterceptorException))
			{
				fail("Save didn't fail due to ModelSavingException with cause InterceptorException.");
			}
			return;
		}
		fail("should have caused interceptor to throw exception!");
	}
}
