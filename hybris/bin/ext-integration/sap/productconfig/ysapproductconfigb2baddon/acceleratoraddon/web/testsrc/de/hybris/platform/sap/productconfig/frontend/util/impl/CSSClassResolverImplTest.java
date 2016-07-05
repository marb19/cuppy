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
package de.hybris.platform.sap.productconfig.frontend.util.impl;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.CsticStatusType;
import de.hybris.platform.sap.productconfig.facades.GroupStatusType;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;
import de.hybris.platform.sap.productconfig.frontend.constants.Sapproductconfigb2baddonConstants;
import de.hybris.platform.sap.productconfig.frontend.validator.ConflictError;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;


/**

 *
 */
@UnitTest
public class CSSClassResolverImplTest extends CSSClassResolverImplTestBase
{


	@Test
	public void testGetInputStyle_noError()
	{
		final CsticData cstic = new CsticData();
		cstic.setCsticStatus(CsticStatusType.DEFAULT);
		final String inputStyle = classUnderTest.getValueStyleClass(cstic);

		assertContainsStyleClass(inputStyle, CSSClassResolverImpl.STYLE_CLASS_PRODUCT_CONFIG_CSTIC_VALUE);

	}

	@Test
	public void testGetInputStyle_error()
	{
		final CsticData cstic = new CsticData();
		cstic.setCsticStatus(CsticStatusType.ERROR);

		final String inputStyle = classUnderTest.getValueStyleClass(cstic);

		assertContainsStyleClass(inputStyle, CSSClassResolverImpl.STYLE_CLASS_PRODUCT_CONFIG_CSTIC_VALUE_ERROR,
				CSSClassResolverImpl.STYLE_CLASS_PRODUCT_CONFIG_CSTIC_VALUE);
	}

	@Test
	public void testGetLabelStyle_StatusDefault()
	{
		final CsticData cstic = new CsticData();
		cstic.setRequired(false);
		cstic.setCsticStatus(CsticStatusType.DEFAULT);

		final String labelStyle = classUnderTest.getLabelStyleClass(cstic);

		assertContainsStyleClass(labelStyle, CSSClassResolverImpl.STYLE_CLASS_CSTIC_LABEL);

	}

	@Test
	public void testGetLabelStyle_StatusSucces()
	{
		final CsticData cstic = new CsticData();
		cstic.setRequired(false);
		cstic.setCsticStatus(CsticStatusType.FINISHED);

		final String labelStyle = classUnderTest.getLabelStyleClass(cstic);

		assertContainsStyleClass(labelStyle, CSSClassResolverImpl.STYLE_CLASS_CSTIC_LABEL,
				CSSClassResolverImpl.STYLE_CLASS_CSTIC_LABEL_SUCCESS);
	}

	@Test
	public void testGetLabelStyle_StatusWarning()
	{
		final CsticData cstic = new CsticData();
		cstic.setRequired(false);
		cstic.setCsticStatus(CsticStatusType.WARNING);

		final String labelStyle = classUnderTest.getLabelStyleClass(cstic);

		assertContainsStyleClass(labelStyle, CSSClassResolverImpl.STYLE_CLASS_CSTIC_LABEL,
				CSSClassResolverImpl.STYLE_CLASS_CSTIC_LABEL_WARNING);
	}

	@Test
	public void testGetLabelStyle_notRequiredNoError()
	{
		final CsticData cstic = new CsticData();
		cstic.setRequired(false);
		cstic.setCsticStatus(CsticStatusType.DEFAULT);
		final String labelStyle = classUnderTest.getLabelStyleClass(cstic);

		assertContainsStyleClass(labelStyle, CSSClassResolverImpl.STYLE_CLASS_CSTIC_LABEL);
	}

	@Test
	public void testGetLabelStyle_requiredNoError()
	{
		final CsticData cstic = new CsticData();
		cstic.setRequired(true);
		cstic.setCsticStatus(CsticStatusType.DEFAULT);
		final String labelStyle = classUnderTest.getLabelStyleClass(cstic);

		assertContainsStyleClass(labelStyle, CSSClassResolverImpl.STYLE_CLASS_CSTIC_LABEL,
				CSSClassResolverImpl.STYLE_CLASS_CSTIC_LABEL_REQUIRED);
	}

	@Test
	public void testGetLabelStyle_requiredNoErrorLongText()
	{
		final CsticData cstic = new CsticData();
		cstic.setRequired(true);
		cstic.setLongText("This is a very long text.");
		cstic.setCsticStatus(CsticStatusType.DEFAULT);
		final String labelStyle = classUnderTest.getLabelStyleClass(cstic);

		assertContainsStyleClass(labelStyle, CSSClassResolverImpl.STYLE_CLASS_CSTIC_LABEL,
				CSSClassResolverImpl.STYLE_CLASS_CSTIC_LABEL_REQUIRED, CSSClassResolverImpl.STYLE_CLASS_CSTIC_LABEL_LONGTEXT);
	}

	@Test
	public void testGetLabelStyle_notRequiredError()
	{
		final CsticData cstic = new CsticData();
		cstic.setRequired(false);
		cstic.setCsticStatus(CsticStatusType.ERROR);

		final String labelStyle = classUnderTest.getLabelStyleClass(cstic);
		assertContainsStyleClass(labelStyle, CSSClassResolverImpl.STYLE_CLASS_CSTIC_LABEL,
				CSSClassResolverImpl.STYLE_CLASS_CSTIC_LABEL_ERROR);
	}

	@Test
	public void testGetLabelStyle_requiredError()
	{
		final CsticData cstic = new CsticData();
		cstic.setRequired(true);
		cstic.setCsticStatus(CsticStatusType.ERROR);

		final String labelStyle = classUnderTest.getLabelStyleClass(cstic);

		assertContainsStyleClass(labelStyle, CSSClassResolverImpl.STYLE_CLASS_CSTIC_LABEL,
				CSSClassResolverImpl.STYLE_CLASS_CSTIC_LABEL_ERROR, CSSClassResolverImpl.STYLE_CLASS_CSTIC_LABEL_REQUIRED);
	}


	@Test
	public void testGetNodeStyle_Default_notFirstLevel()
	{
		final UiGroupData group = new UiGroupData();
		group.setGroupStatus(GroupStatusType.DEFAULT);
		group.setCollapsedInSpecificationTree(false);

		final String nodeStyle = classUnderTest.getNodeStyleClass(group, false, true);

		assertContainsStyleClass(nodeStyle, "");

	}

	@Test
	public void testGetNodeStyle_Default_firstLevel_expanded()
	{
		final UiGroupData group = createUiGroupWithSubGroup();
		group.setGroupStatus(GroupStatusType.DEFAULT);
		group.setCollapsedInSpecificationTree(false);

		final String nodeStyle = classUnderTest.getNodeStyleClass(group, true, true);

		assertContainsStyleClass(nodeStyle, CSSClassResolverImpl.STYLE_CLASS_NODE_MINUS,
				CSSClassResolverImpl.STYLE_CLASS_NODE_EXPANDABLE);

	}

	@Test
	public void testGetNodeStyle_Default_firstLevel_noTitle()
	{
		final UiGroupData group = createUiGroupWithSubGroup();
		group.setGroupStatus(GroupStatusType.DEFAULT);
		group.setCollapsedInSpecificationTree(false);

		final String nodeStyle = classUnderTest.getNodeStyleClass(group, true, false);

		assertContainsStyleClass(nodeStyle, "");

	}

	@Test
	public void testGetNodeStyle_Default_firstLevel_collapsed()
	{
		final UiGroupData group = createUiGroupWithSubGroup();

		group.setGroupStatus(GroupStatusType.DEFAULT);
		group.setCollapsedInSpecificationTree(true);

		final String nodeStyle = classUnderTest.getNodeStyleClass(group, true, true);

		assertContainsStyleClass(nodeStyle, CSSClassResolverImpl.STYLE_CLASS_NODE_PLUS,
				CSSClassResolverImpl.STYLE_CLASS_NODE_EXPANDABLE);

	}

	@Test
	public void testGetNodeStyle_firstLevel_collapsed_deeplyNested()
	{
		final UiGroupData group = createUiGroupWithSubGroup();
		final List<UiGroupData> subGroups = new ArrayList<>();
		subGroups.add(group);
		final UiGroupData root = new UiGroupData();
		root.setSubGroups(subGroups);

		root.setGroupStatus(GroupStatusType.DEFAULT);
		root.setCollapsedInSpecificationTree(true);
		root.setOneConfigurableSubGroup(true);
		root.setConfigurable(true);
		group.setOneConfigurableSubGroup(false);
		final String nodeStyle = classUnderTest.getNodeStyleClass(root, true, true);

		assertContainsStyleClass(nodeStyle, CSSClassResolverImpl.STYLE_CLASS_NODE_PLUS,
				CSSClassResolverImpl.STYLE_CLASS_NODE_EXPANDABLE);

	}

	@Test
	public void testGetNodeStyle_firstLevel_collapsed_deeplyNested_onlyNonConfigurable()
	{
		final UiGroupData group = createUiGroupWithSubGroup();
		final List<UiGroupData> subGroups = new ArrayList<>();
		subGroups.add(group);
		final UiGroupData root = new UiGroupData();
		root.setSubGroups(subGroups);

		root.setGroupStatus(GroupStatusType.DEFAULT);
		root.setCollapsedInSpecificationTree(true);
		root.setOneConfigurableSubGroup(false);
		root.setConfigurable(false);
		group.setOneConfigurableSubGroup(false);
		final String nodeStyle = classUnderTest.getNodeStyleClass(root, true, true);

		assertContainsStyleClass(nodeStyle, "");

	}

	@Test
	public void testGetNodeStyle_Warning_firstLevel_expanded()
	{
		final UiGroupData group = createUiGroupWithSubGroup();
		group.setGroupStatus(GroupStatusType.WARNING);
		group.setCollapsedInSpecificationTree(false);

		final String nodeStyle = classUnderTest.getNodeStyleClass(group, true, true);

		assertContainsStyleClass(nodeStyle, CSSClassResolverImpl.STYLE_CLASS_NODE_MINUS,
				CSSClassResolverImpl.STYLE_CLASS_NODE_WARNING, CSSClassResolverImpl.STYLE_CLASS_NODE_EXPANDABLE);

	}

	@Test
	public void testGetNodeStyle_Warning_firstLevel_expanded_noSubGroups()
	{
		final UiGroupData group = new UiGroupData();
		group.setGroupStatus(GroupStatusType.WARNING);
		group.setCollapsedInSpecificationTree(false);

		final String nodeStyle = classUnderTest.getNodeStyleClass(group, true, true);

		assertContainsStyleClass(nodeStyle, CSSClassResolverImpl.STYLE_CLASS_NODE_WARNING);

	}

	@Test
	public void testGetNodeStyle_firstLevel_expanded_onlyOneConfigurabelSubGroup()
	{
		final UiGroupData group = createUiGroupWithSubGroup();
		group.setGroupStatus(GroupStatusType.DEFAULT);
		group.setCollapsedInSpecificationTree(false);
		group.setOneConfigurableSubGroup(true);

		final String nodeStyle = classUnderTest.getNodeStyleClass(group, true, true);

		assertContainsStyleClass(nodeStyle, "");

	}

	@Test
	public void testGetNodeStyle_firstLevel_expanded_oneNoneConfigurabelSubGroup()
	{
		final UiGroupData group = createUiGroupWithSubGroup();
		group.setGroupStatus(GroupStatusType.DEFAULT);
		group.setCollapsedInSpecificationTree(false);
		group.setOneConfigurableSubGroup(false);
		group.setConfigurable(true);
		group.getSubGroups().get(0).setConfigurable(false);

		final String nodeStyle = classUnderTest.getNodeStyleClass(group, true, true);

		assertContainsStyleClass(nodeStyle, CSSClassResolverImpl.STYLE_CLASS_NODE_MINUS,
				CSSClassResolverImpl.STYLE_CLASS_NODE_EXPANDABLE);

	}

	@Test
	public void testGetNodeStyle_Warning_firstLevel_expanded_zeroSubGroups()
	{
		final UiGroupData group = createUiGroupWithNoSubGroup();
		group.setGroupStatus(GroupStatusType.WARNING);
		group.setCollapsedInSpecificationTree(false);

		final String nodeStyle = classUnderTest.getNodeStyleClass(group, true, true);

		assertContainsStyleClass(nodeStyle, CSSClassResolverImpl.STYLE_CLASS_NODE_WARNING);

	}

	@Test
	public void testGetNodeStyle_Error_notFirstLevel_collapsed()
	{
		final UiGroupData group = createUiGroupWithSubGroup();
		group.setGroupStatus(GroupStatusType.ERROR);
		group.setCollapsedInSpecificationTree(true);

		final String nodeStyle = classUnderTest.getNodeStyleClass(group, false, true);

		assertContainsStyleClass(nodeStyle, CSSClassResolverImpl.STYLE_CLASS_NODE_ERROR);

	}

	@Test
	public void testGetNodeStyle_Error_notFirstLevel_noTitle()
	{
		final UiGroupData group = new UiGroupData();
		group.setGroupStatus(GroupStatusType.ERROR);
		group.setCollapsedInSpecificationTree(false);

		final String nodeStyle = classUnderTest.getNodeStyleClass(group, false, false);

		assertContainsStyleClass(nodeStyle, "");

	}

	@Test
	public void testGetSideBarComponentCollapsed()
	{
		final String style = classUnderTest.getStyleClassForSideBarComponent(true, null);

		assertContainsStyleClass(style, CSSClassResolverImpl.STYLE_CLASS_SIDEBAR_COMP_PLUS);
	}

	@Test
	public void testGetSideBarComponentCollapsedWithErrors()
	{
		final BindingResult errors = new BeanPropertyBindingResult(null, Sapproductconfigb2baddonConstants.CONFIG_ATTRIBUTE);
		final ObjectError validationError = new FieldError(Sapproductconfigb2baddonConstants.CONFIG_ATTRIBUTE, "", "ERROR");
		errors.addError(validationError);

		final String style = classUnderTest.getStyleClassForSideBarComponent(true, errors);

		assertContainsStyleClass(style, CSSClassResolverImpl.STYLE_CLASS_SIDEBAR_COMP_PLUS,
				CSSClassResolverImpl.STYLE_CLASS_SPECIFICATION_COMP_ERROR);
	}

	@Test
	public void testGetSideBarComponentCollapsedWithWarnings()
	{
		final BindingResult errors = new BeanPropertyBindingResult(null, Sapproductconfigb2baddonConstants.CONFIG_ATTRIBUTE);
		final ConflictError conflictError = new ConflictError(null, "", null, null, "WARNING");
		errors.addError(conflictError);

		final String style = classUnderTest.getStyleClassForSideBarComponent(true, errors);

		assertContainsStyleClass(style, CSSClassResolverImpl.STYLE_CLASS_SIDEBAR_COMP_PLUS,
				CSSClassResolverImpl.STYLE_CLASS_SPECIFICATION_COMP_WARNING);
	}

	@Test
	public void testGetSideBarComponentNotCollapsed()
	{
		final String style = classUnderTest.getStyleClassForSideBarComponent(false, null);

		assertContainsStyleClass(style, CSSClassResolverImpl.STYLE_CLASS_SIDEBAR_COMP_MINUS);
	}

	@Test
	public void testGetGroupStyle_Error()
	{
		final UiGroupData group = new UiGroupData();
		group.setGroupStatus(GroupStatusType.ERROR);
		group.setCollapsed(true);

		final String groupStyle = classUnderTest.getGroupStyleClass(group);

		assertContainsStyleClass(groupStyle, CSSClassResolverImpl.STYLE_CLASS_GROUP, CSSClassResolverImpl.STYLE_CLASS_GROUP_CLOSE,
				CSSClassResolverImpl.STYLE_CLASS_GROUP_ERROR);
	}

	@Test
	public void testGetGroupStyle_Warning()
	{
		final UiGroupData group = new UiGroupData();
		group.setGroupStatus(GroupStatusType.WARNING);
		group.setCollapsed(false);
		final String groupStyle = classUnderTest.getGroupStyleClass(group);

		assertContainsStyleClass(groupStyle, CSSClassResolverImpl.STYLE_CLASS_GROUP, CSSClassResolverImpl.STYLE_CLASS_GROUP_OPEN,
				CSSClassResolverImpl.STYLE_CLASS_GROUP_WARNING);
	}

	@Test
	public void testGetGroupStyle_Conflict()
	{
		final UiGroupData group = new UiGroupData();
		group.setGroupStatus(GroupStatusType.CONFLICT);
		group.setCollapsed(false);
		final String groupStyle = classUnderTest.getGroupStyleClass(group);

		assertContainsStyleClass(groupStyle, CSSClassResolverImpl.STYLE_CLASS_GROUP, CSSClassResolverImpl.STYLE_CLASS_GROUP_OPEN,
				CSSClassResolverImpl.STYLE_CLASS_GROUP_WARNING);
	}

	@Test
	public void testGetGroupStyle_Default()
	{
		final UiGroupData group = new UiGroupData();
		group.setGroupStatus(GroupStatusType.DEFAULT);
		group.setCollapsed(true);
		final String groupStyle = classUnderTest.getGroupStyleClass(group);

		assertContainsStyleClass(groupStyle, CSSClassResolverImpl.STYLE_CLASS_GROUP, CSSClassResolverImpl.STYLE_CLASS_GROUP_CLOSE);

	}

	@Test
	public void testGetGroupStyle_hideExpandCollapse()
	{
		final UiGroupData group = new UiGroupData();
		group.setGroupStatus(GroupStatusType.DEFAULT);
		group.setCollapsed(true);
		final String groupStyle = classUnderTest.getGroupStyleClass(group, true);
		assertEquals(1, getNumberOfStyleClasses(groupStyle));
		assertContainsStyleClass(groupStyle, CSSClassResolverImpl.STYLE_CLASS_GROUP);

	}

	@Test(expected = IllegalAccessError.class)
	public void testMenuNodeStyleClassNotSupported()
	{
		final UiGroupData group = createUiGroupWithSubGroup();
		group.setGroupStatus(GroupStatusType.FINISHED);
		classUnderTest.getMenuNodeStyleClass(group, Integer.valueOf(1));
		fail();
	}

	@Test
	public void testGetGroupStyle_Finished()
	{
		final UiGroupData group = new UiGroupData();
		group.setGroupStatus(GroupStatusType.FINISHED);
		group.setCollapsed(false);
		final String groupStyle = classUnderTest.getGroupStyleClass(group);

		assertContainsStyleClass(groupStyle, CSSClassResolverImpl.STYLE_CLASS_GROUP, CSSClassResolverImpl.STYLE_CLASS_GROUP_OPEN,
				CSSClassResolverImpl.STYLE_CLASS_GROUP_FINISHED);
	}


}
