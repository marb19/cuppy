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

import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.GroupType;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;


/**
 *
 */
public class CSSClassResponsiveResolverImpl extends CSSClassResolverImpl
{
	public static final String STYLE_CLASS_MENU_LEVEL = "cpq-menu-level-";
	public static final String STYLE_CLASS_MENU_NODE = "cpq-menu-node";
	public static final String STYLE_CLASS_MENU_LEAF = "cpq-menu-leaf";
	public static final String STYLE_CLASS_MENU_NON_CONF_LEAF = "cpq-menu-nonConfLeaf";

	public static final String STYLE_MENU_CONFLICT_HEADER = "cpq-menu-conflict-header";
	public static final String STYLE_MENU_CONFLICT_NODE = "cpq-menu-conflict-node";

	public static final String STYLE_CLASS_MENU_NODE_CONFLICT = "cpq-menu-conflict";
	public static final String STYLE_CLASS_CSTIC_LABEL_CONFLICT = "cpq-csticlabel-conflict";
	public static final String STYLE_CLASS_GROUP_CONFLICT = "cpq-group-conflict";
	public static final String STYLE_CLASS_CONFLICTGROUP = "cpq-conflictgroup";

	@Override
	public String getGroupStyleClass(final UiGroupData group, final boolean hideExpandCollapse)
	{
		String styleClassString = STYLE_CLASS_GROUP;
		if (GroupType.CONFLICT == group.getGroupType())
		{
			styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_CONFLICTGROUP);
		}
		else
		{
			switch (group.getGroupStatus())
			{
				case ERROR:
					styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_GROUP_ERROR);
					break;
				case WARNING:
					styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_GROUP_WARNING);
					break;
				case CONFLICT:
					styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_GROUP_CONFLICT);
					break;
				case FINISHED:
					styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_GROUP_FINISHED);
					break;
			}
		}
		if (!hideExpandCollapse)
		{
			if (group.isCollapsed())
			{
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_GROUP_CLOSE);
			}
			else
			{
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_GROUP_OPEN);
			}
		}

		return styleClassString;
	}

	@Override
	public String getLabelStyleClass(final CsticData cstic)
	{
		String styleClassString = STYLE_CLASS_CSTIC_LABEL;
		switch (cstic.getCsticStatus())
		{
			case ERROR:
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_CSTIC_LABEL_ERROR);
				break;
			case WARNING:
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_CSTIC_LABEL_WARNING);
				break;
			case CONFLICT:
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_CSTIC_LABEL_CONFLICT);
				break;
			case FINISHED:
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_CSTIC_LABEL_SUCCESS);
				break;
		}

		return styleClassString;
	}

	@Override
	public String getMenuNodeStyleClass(final UiGroupData group, final Integer level)
	{
		String styleClassString = STYLE_CLASS_MENU_LEVEL + level.toString();
		final boolean showAsNode = !isNullorEmptyList(group.getSubGroups());
		final boolean showAsLeaf = GroupType.CSTIC_GROUP == group.getGroupType();

		if (showAsNode)
		{
			styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_MENU_NODE);
			if (group.isCollapsedInSpecificationTree())
			{
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_MENU_NODE_COLLAPSED);
			}
			else
			{
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_MENU_NODE_EXPANDED);
			}
		}
		else if (showAsLeaf)
		{
			styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_MENU_LEAF);
		}
		else
		{
			styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_MENU_NON_CONF_LEAF);
		}

		if (group.isConfigurable())
		{
			switch (group.getGroupStatus())
			{
				case ERROR:
					styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_MENU_NODE_ERROR);
					break;
				case WARNING:
					styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_MENU_NODE_WARNING);
					break;
				case CONFLICT:
					styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_MENU_NODE_CONFLICT);
					break;
				case FINISHED:
					styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_MENU_NODE_COMPLETED);
					break;

			}
		}
		return styleClassString;
	}

	@Override
	public String getMenuConflictStyleClass(final UiGroupData conflict)
	{
		final boolean showAsConflictHeader = GroupType.CONFLICT_HEADER == conflict.getGroupType();
		String styleClassString = "";
		if (showAsConflictHeader)
		{
			styleClassString = appendStyleClass(styleClassString, STYLE_MENU_CONFLICT_HEADER);
			if (conflict.isCollapsedInSpecificationTree())
			{
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_MENU_NODE_COLLAPSED);
			}
			else
			{
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_MENU_NODE_EXPANDED);
			}
		}
		else
		{
			styleClassString = appendStyleClass(styleClassString, STYLE_MENU_CONFLICT_NODE);
		}
		return styleClassString;
	}
}
