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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.facades.GroupStatusType;
import de.hybris.platform.sap.productconfig.facades.GroupType;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;

import org.junit.Before;
import org.junit.Test;



@UnitTest
public class CSSClassResolverResponsiveImplTest extends CSSClassResolverImplTestBase
{

	@Override
	@Before
	public void setUp()
	{
		classUnderTest = new CSSClassResponsiveResolverImpl();
	}

	@Test
	public void testGetGroupStyle_Conflict()
	{
		final UiGroupData group = new UiGroupData();
		group.setGroupStatus(GroupStatusType.CONFLICT);
		group.setCollapsed(false);
		final String groupStyle = classUnderTest.getGroupStyleClass(group);

		assertContainsStyleClass(groupStyle, CSSClassResolverImpl.STYLE_CLASS_GROUP, CSSClassResolverImpl.STYLE_CLASS_GROUP_OPEN,
				CSSClassResponsiveResolverImpl.STYLE_CLASS_GROUP_CONFLICT);
	}

	@Test
	public void test_MenuNodeStyleClass_Leaf()
	{
		final UiGroupData group = createUiGroupWithNoSubGroup();
		final String style = classUnderTest.getMenuNodeStyleClass(group, Integer.valueOf(2));
		assertEquals(style, 2, getNumberOfStyleClasses(style));
		assertContainsStyleClass(style, CSSClassResponsiveResolverImpl.STYLE_CLASS_MENU_LEVEL + "2",
				CSSClassResponsiveResolverImpl.STYLE_CLASS_MENU_LEAF);
	}

	@Test
	public void test_MenuNodeStyleClass_Node()
	{
		final UiGroupData group = createUiGroupWithSubGroup();
		final String style = classUnderTest.getMenuNodeStyleClass(group, Integer.valueOf(1));
		assertEquals(style, 3, getNumberOfStyleClasses(style));
		assertContainsStyleClass(style, CSSClassResponsiveResolverImpl.STYLE_CLASS_MENU_LEVEL + "1",
				CSSClassResponsiveResolverImpl.STYLE_CLASS_MENU_NODE, CSSClassResolverImpl.STYLE_CLASS_MENU_NODE_EXPANDED);
	}

	@Test
	public void test_MenuNodeStyleClass_NonConfLeaf()
	{
		final UiGroupData group = createUiGroupWithNoSubGroup();
		group.setConfigurable(false);
		group.setGroupType(GroupType.INSTANCE);
		final String style = classUnderTest.getMenuNodeStyleClass(group, Integer.valueOf(1));
		assertEquals(style, 2, getNumberOfStyleClasses(style));
		assertContainsStyleClass(style, CSSClassResponsiveResolverImpl.STYLE_CLASS_MENU_LEVEL + "1",
				CSSClassResponsiveResolverImpl.STYLE_CLASS_MENU_NON_CONF_LEAF);
	}

	@Test
	public void test_MenuNodeStyleClass_Node_Error()
	{
		final UiGroupData group = createUiGroupWithSubGroup();
		group.setGroupStatus(GroupStatusType.ERROR);
		final String style = classUnderTest.getMenuNodeStyleClass(group, Integer.valueOf(1));
		assertEquals(style, 4, getNumberOfStyleClasses(style));
		assertContainsStyleClass(style, CSSClassResponsiveResolverImpl.STYLE_CLASS_MENU_LEVEL + "1",
				CSSClassResponsiveResolverImpl.STYLE_CLASS_MENU_NODE, CSSClassResolverImpl.STYLE_CLASS_MENU_NODE_EXPANDED,
				CSSClassResolverImpl.STYLE_CLASS_MENU_NODE_ERROR);
	}

	@Test
	public void test_MenuNodeStyleClass_Node_Warning()
	{
		final UiGroupData group = createUiGroupWithSubGroup();
		group.setGroupStatus(GroupStatusType.WARNING);
		final String style = classUnderTest.getMenuNodeStyleClass(group, Integer.valueOf(1));
		assertEquals(style, 4, getNumberOfStyleClasses(style));
		assertContainsStyleClass(style, CSSClassResponsiveResolverImpl.STYLE_CLASS_MENU_LEVEL + "1",
				CSSClassResponsiveResolverImpl.STYLE_CLASS_MENU_NODE, CSSClassResolverImpl.STYLE_CLASS_MENU_NODE_EXPANDED,
				CSSClassResolverImpl.STYLE_CLASS_MENU_NODE_WARNING);
	}

	@Test
	public void test_MenuNodeStyleClass_Node_Ok()
	{
		final UiGroupData group = createUiGroupWithSubGroup();
		group.setGroupStatus(GroupStatusType.FINISHED);
		final String style = classUnderTest.getMenuNodeStyleClass(group, Integer.valueOf(1));
		assertEquals(style, 4, getNumberOfStyleClasses(style));
		assertContainsStyleClass(style, CSSClassResponsiveResolverImpl.STYLE_CLASS_MENU_LEVEL + "1",
				CSSClassResponsiveResolverImpl.STYLE_CLASS_MENU_NODE, CSSClassResolverImpl.STYLE_CLASS_MENU_NODE_EXPANDED,
				CSSClassResolverImpl.STYLE_CLASS_MENU_NODE_COMPLETED);
	}

	@Test
	public void test_MenuNodeStyleClass_Node_Collapsed()
	{
		final UiGroupData group = createUiGroupWithSubGroup();
		group.setCollapsedInSpecificationTree(true);
		final String style = classUnderTest.getMenuNodeStyleClass(group, Integer.valueOf(1));
		assertEquals(style, 3, getNumberOfStyleClasses(style));
		assertContainsStyleClass(style, CSSClassResponsiveResolverImpl.STYLE_CLASS_MENU_LEVEL + "1",
				CSSClassResponsiveResolverImpl.STYLE_CLASS_MENU_NODE, CSSClassResolverImpl.STYLE_CLASS_MENU_NODE_COLLAPSED);
	}

	@Test
	public void test_MenuNodeStyleClass_NonConfNode_Collapsed()
	{
		final UiGroupData group = createUiGroupWithSubGroup();
		group.setCollapsedInSpecificationTree(true);
		group.setConfigurable(false);
		final String style = classUnderTest.getMenuNodeStyleClass(group, Integer.valueOf(1));
		assertEquals(style, 3, getNumberOfStyleClasses(style));
		assertContainsStyleClass(style, CSSClassResponsiveResolverImpl.STYLE_CLASS_MENU_LEVEL + "1",
				CSSClassResponsiveResolverImpl.STYLE_CLASS_MENU_NODE, CSSClassResolverImpl.STYLE_CLASS_MENU_NODE_COLLAPSED);
	}

	@Test
	public void test_MenuNodeStyleClass_Leaf_Error()
	{
		final UiGroupData group = createUiGroupWithNoSubGroup();
		group.setGroupStatus(GroupStatusType.ERROR);
		final String style = classUnderTest.getMenuNodeStyleClass(group, Integer.valueOf(2));
		assertEquals(style, 3, getNumberOfStyleClasses(style));
		assertContainsStyleClass(style, CSSClassResponsiveResolverImpl.STYLE_CLASS_MENU_LEVEL + "2",
				CSSClassResponsiveResolverImpl.STYLE_CLASS_MENU_LEAF, CSSClassResolverImpl.STYLE_CLASS_MENU_NODE_ERROR);
	}

	@Test
	public void test_MenuNodeStyleClass_Leaf_Warning()
	{
		final UiGroupData group = createUiGroupWithNoSubGroup();
		group.setGroupStatus(GroupStatusType.WARNING);
		final String style = classUnderTest.getMenuNodeStyleClass(group, Integer.valueOf(2));
		assertEquals(style, 3, getNumberOfStyleClasses(style));
		assertContainsStyleClass(style, CSSClassResponsiveResolverImpl.STYLE_CLASS_MENU_LEVEL + "2",
				CSSClassResponsiveResolverImpl.STYLE_CLASS_MENU_LEAF, CSSClassResolverImpl.STYLE_CLASS_MENU_NODE_WARNING);
	}

	@Test
	public void test_MenuNodeStyleClass_Leaf_Ok()
	{
		final UiGroupData group = createUiGroupWithNoSubGroup();
		group.setGroupStatus(GroupStatusType.FINISHED);
		final String style = classUnderTest.getMenuNodeStyleClass(group, Integer.valueOf(2));
		assertEquals(style, 3, getNumberOfStyleClasses(style));
		assertContainsStyleClass(style, CSSClassResponsiveResolverImpl.STYLE_CLASS_MENU_LEVEL + "2",
				CSSClassResponsiveResolverImpl.STYLE_CLASS_MENU_LEAF, CSSClassResolverImpl.STYLE_CLASS_MENU_NODE_COMPLETED);
	}

	@Test
	public void testGetGroupStyle_ConflicGroup()
	{
		final UiGroupData group = new UiGroupData();
		group.setGroupStatus(GroupStatusType.CONFLICT);
		group.setCollapsed(true);
		group.setGroupType(GroupType.CONFLICT);
		final String groupStyle = classUnderTest.getGroupStyleClass(group, true);
		assertEquals(2, getNumberOfStyleClasses(groupStyle));
		assertContainsStyleClass(groupStyle, CSSClassResolverImpl.STYLE_CLASS_GROUP,
				CSSClassResponsiveResolverImpl.STYLE_CLASS_CONFLICTGROUP);

	}
}
