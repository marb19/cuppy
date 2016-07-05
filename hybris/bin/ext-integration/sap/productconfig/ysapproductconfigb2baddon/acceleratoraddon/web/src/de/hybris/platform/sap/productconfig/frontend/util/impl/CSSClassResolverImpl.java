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
import de.hybris.platform.sap.productconfig.facades.CsticStatusType;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;
import de.hybris.platform.sap.productconfig.facades.UiType;
import de.hybris.platform.sap.productconfig.frontend.util.CSSClassResolver;

import java.util.List;

import org.springframework.validation.BindingResult;


/**

 *
 */
public class CSSClassResolverImpl implements CSSClassResolver
{
	public static final String STYLE_CLASS_SEPERATOR = " ";
	public static final String STYLE_CLASS_CSTIC_LABEL = "cpq-csticlabel";
	public static final String STYLE_CLASS_CSTIC_LABEL_ERROR = "cpq-csticlabel-error";
	public static final String STYLE_CLASS_CSTIC_LABEL_REQUIRED = "cpq-csticlabel-required-icon";
	public static final String STYLE_CLASS_CSTIC_LABEL_SUCCESS = "cpq-csticlabel-success";
	public static final String STYLE_CLASS_CSTIC_LABEL_WARNING = "cpq-csticlabel-warning";
	public static final String STYLE_CLASS_CSTIC_LABEL_LONGTEXT = "cpq-csticlabel-longtext-icon";

	public static final String STYLE_CLASS_PRODUCT_CONFIG_CSTIC_VALUE = "cpq-csticValue";
	public static final String STYLE_CLASS_PRODUCT_CONFIG_CSTIC_VALUE_ERROR = "cpq-csticValue-error";
	public static final String STYLE_CLASS_PRODUCT_CONFIG_CSTIC_VALUE_MULTI_LIST = "cpq-csticValue-multi";

	public static final String STYLE_CLASS_GROUP = "cpq-group-header";
	public static final String STYLE_CLASS_GROUP_FINISHED = "cpq-group-completed";
	public static final String STYLE_CLASS_GROUP_ERROR = "cpq-group-error";
	public static final String STYLE_CLASS_GROUP_WARNING = "cpq-group-warning";
	public static final String STYLE_CLASS_GROUP_CLOSE = "cpq-group-title-close";
	public static final String STYLE_CLASS_GROUP_OPEN = "cpq-group-title-open";

	public static final String STYLE_CLASS_NODE = "cpq-specification-node";
	public static final String STYLE_CLASS_NODE_ERROR = "cpq-specification-node-error";
	public static final String STYLE_CLASS_NODE_WARNING = "cpq-specification-node-warning";
	public static final String STYLE_CLASS_NODE_PLUS = "cpq-specification-node-plus";
	public static final String STYLE_CLASS_NODE_MINUS = "cpq-specification-node-minus";
	public static final String STYLE_CLASS_NODE_EXPANDABLE = "cpq-specification-node-expandable";

	public static final String STYLE_CLASS_GROUP_TAB = "cpq-tab-title";
	public static final String STYLE_CLASS_GROUP_TAB_ERROR = "cpq-group-tab-error";
	public static final String STYLE_CLASS_GROUP_TAB_WARNING = "cpq-group-tab-warning";

	public static final String STYLE_CLASS_SIDEBAR_COMP_MINUS = "cpq-side-comp-header-minus";
	public static final String STYLE_CLASS_SIDEBAR_COMP_PLUS = "cpq-side-comp-header-plus";
	public static final String STYLE_CLASS_SPECIFICATION_COMP_WARNING = "cpq-specification-comp-warning";
	public static final String STYLE_CLASS_SPECIFICATION_COMP_ERROR = "cpq-specification-comp-error";

	public static final String STYLE_CLASS_MENU_NODE_EXPANDED = "cpq-menu-expanded";
	public static final String STYLE_CLASS_MENU_NODE_COLLAPSED = "cpq-menu-collapsed";
	public static final String STYLE_CLASS_MENU_NODE_ERROR = "cpq-menu-error";
	public static final String STYLE_CLASS_MENU_NODE_WARNING = "cpq-menu-warning";
	public static final String STYLE_CLASS_MENU_NODE_COMPLETED = "cpq-menu-completed";



	@Override
	public String getLabelStyleClass(final CsticData cstic)
	{
		String styleClassString = STYLE_CLASS_CSTIC_LABEL;
		if (cstic.isRequired())
		{
			styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_CSTIC_LABEL_REQUIRED);
		}
		if (cstic.getLongText() != null && !cstic.getLongText().isEmpty())
		{
			styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_CSTIC_LABEL_LONGTEXT);
		}
		switch (cstic.getCsticStatus())
		{
			case ERROR:
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_CSTIC_LABEL_ERROR);
				break;
			case WARNING:
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_CSTIC_LABEL_WARNING);
				break;
			case CONFLICT:
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_CSTIC_LABEL_WARNING);
				break;
			case FINISHED:
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_CSTIC_LABEL_SUCCESS);
				break;
		}

		return styleClassString;
	}

	@Override
	public String getValueStyleClass(final CsticData cstic)
	{
		String styleClassString = STYLE_CLASS_PRODUCT_CONFIG_CSTIC_VALUE;
		final CsticStatusType csticStatus = cstic.getCsticStatus();
		if (CsticStatusType.ERROR.equals(csticStatus) || CsticStatusType.WARNING.equals(csticStatus)
				|| CsticStatusType.CONFLICT.equals(csticStatus))
		{
			styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_PRODUCT_CONFIG_CSTIC_VALUE_ERROR);
		}
		if (isMulitUiElementsType(cstic))
		{
			styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_PRODUCT_CONFIG_CSTIC_VALUE_MULTI_LIST);
		}

		return styleClassString;
	}

	private boolean isMulitUiElementsType(final CsticData cstic)
	{
		return cstic.getType() == UiType.CHECK_BOX_LIST || cstic.getType() == UiType.RADIO_BUTTON;
	}

	protected String appendStyleClass(String styleClassString, final String styleClass)
	{
		if (!styleClassString.isEmpty())
		{
			styleClassString += STYLE_CLASS_SEPERATOR;
		}
		styleClassString += styleClass;
		return styleClassString;
	}

	@Override
	public String getGroupTabStyleClass(final UiGroupData group)
	{
		String styleClassString = STYLE_CLASS_GROUP_TAB;
		switch (group.getGroupStatus())
		{
			case ERROR:
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_GROUP_TAB_ERROR);
				break;
			case WARNING:
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_GROUP_TAB_WARNING);
				break;
			case CONFLICT:
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_GROUP_TAB_WARNING);
				break;
		}
		return styleClassString;
	}

	@Override
	public String getNodeStyleClass(final UiGroupData group, final boolean isFirstLevelNode, final boolean showNodeTitle)
	{
		String styleClassString = "";
		if (isFirstLevelNode && showNodeTitle && thereIsContentToExpand(group))
		{
			styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_NODE_EXPANDABLE);
			if (group.isCollapsedInSpecificationTree())
			{
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_NODE_PLUS);
			}
			else
			{
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_NODE_MINUS);
			}
		}
		if (showNodeTitle)
		{
			switch (group.getGroupStatus())
			{
				case ERROR:
					styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_NODE_ERROR);
					break;
				case WARNING:
					styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_NODE_WARNING);
					break;
				case CONFLICT:
					styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_NODE_WARNING);
					break;
			}
		}
		return styleClassString;
	}




	protected boolean isNullorEmptyList(final List<?> list)
	{
		final boolean nullOrEmpty = null == list || list.isEmpty();
		return nullOrEmpty;
	}


	protected boolean thereIsContentToExpand(final UiGroupData group)
	{
		boolean thereIsContentToExpand = false;
		// we only show first level of non-configurable components
		if (group.isConfigurable())
		{
			final boolean hasSubGroups = group.getSubGroups() != null && group.getSubGroups().size() > 0;
			// if there is only exactly one configurable  sub instance, it is inlined into the parent instance
			// so there is not instance to show
			thereIsContentToExpand = hasSubGroups && !group.isOneConfigurableSubGroup();
			if (!thereIsContentToExpand && hasSubGroups)
			{
				// however even if teis sub instance is inlined into the parent, there might be content on a depper level
				for (final UiGroupData subGroup : group.getSubGroups())
				{
					if (!subGroup.isConfigurable())
					{
						// first level of non-configurable components always shown
						thereIsContentToExpand = true;
						break;
					}
					else
					{
						// check recursively
						thereIsContentToExpand = thereIsContentToExpand(subGroup);
						if (thereIsContentToExpand)
						{
							// if one branch has content, it is sufficient
							break;
						}
					}
				}
			}
		}

		return thereIsContentToExpand;
	}

	@Override
	public String getStyleClassForSideBarComponent(final boolean collapsed, final BindingResult bindResult)
	{
		String styleClassString = "";

		if (collapsed)
		{
			styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_SIDEBAR_COMP_PLUS);

			if (bindResult != null && bindResult.hasFieldErrors())
			{
				if (ErrorResolver.hasErrorMessages(bindResult))
				{
					styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_SPECIFICATION_COMP_ERROR);
				}
				else
				{
					styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_SPECIFICATION_COMP_WARNING);
				}
			}
		}
		else
		{
			styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_SIDEBAR_COMP_MINUS);
		}

		return styleClassString.toString();
	}


	@Override
	public String getGroupStyleClass(final UiGroupData group)
	{
		return getGroupStyleClass(group, false);
	}

	@Override
	public String getGroupStyleClass(final UiGroupData group, final boolean hideExpandCollapse)
	{
		String styleClassString = STYLE_CLASS_GROUP;
		switch (group.getGroupStatus())
		{
			case ERROR:
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_GROUP_ERROR);
				break;
			case WARNING:
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_GROUP_WARNING);
				break;
			case CONFLICT:
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_GROUP_WARNING);
				break;
			case FINISHED:
				styleClassString = appendStyleClass(styleClassString, STYLE_CLASS_GROUP_FINISHED);
				break;
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
	public String getMenuNodeStyleClass(final UiGroupData group, final Integer level)
	{
		throw new IllegalAccessError("Not supported for desktop UI");
	}

	@Override
	public String getMenuConflictStyleClass(final UiGroupData conflict)
	{
		throw new IllegalAccessError("Not supported for desktop UI");
	}
}
