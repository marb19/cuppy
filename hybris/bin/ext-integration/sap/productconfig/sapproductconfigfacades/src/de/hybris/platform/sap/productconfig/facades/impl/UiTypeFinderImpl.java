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
package de.hybris.platform.sap.productconfig.facades.impl;

import de.hybris.platform.sap.productconfig.facades.UiType;
import de.hybris.platform.sap.productconfig.facades.UiTypeFinder;
import de.hybris.platform.sap.productconfig.facades.UiValidationType;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;


public class UiTypeFinderImpl implements UiTypeFinder
{
	private int dropDownListThreshold = 4;

	private final static Logger LOG = Logger.getLogger(UiTypeFinderImpl.class);

	@Override
	public UiType findUiTypeForCstic(final CsticModel model)
	{
		// This method might be called very often (several thousand times) for large customer models.
		// isDebugEnabled causes some memory allocation internally, which adds up a lot (2 MB for 90.000 calls)
		// so we read it only once per cstic
		final boolean isDebugEnabled = LOG.isDebugEnabled();
		final List<UiType> posibleTypes = collectPossibleTypes(model, isDebugEnabled);
		final UiType uiType = chooseUiType(posibleTypes, model, isDebugEnabled);

		if (isDebugEnabled)
		{
			LOG.debug("UI type found for cstic model [CSTIC_NAME='" + model.getName() + "';CSTIC_TYPE='" + model.getValueType()
					+ "';CSTIC_UI_TYPE='" + uiType + "']");
		}

		return uiType;
	}

	protected List<UiType> addUiTypeToListLowMem(final List<UiType> list, final UiType uiType)
	{
		// list is empty, beside this element no more are added
		List<UiType> newList;
		if (list.isEmpty())
		{
			newList = Collections.singletonList(uiType);
		}
		else
		{
			newList = new ArrayList(list);
			newList.add(uiType);
		}
		return newList;
	}

	protected List<UiType> mergeUiTypeListLowMem(final List<UiType> list1, final List<UiType> list2)
	{
		// 99% case either both list empty, or one list empty, other with one
		// element.
		List<UiType> newList;
		if (list1.isEmpty() && list2.isEmpty())
		{
			newList = list1;
		}
		else if (!list1.isEmpty() && list2.isEmpty())
		{
			newList = list1;
		}
		else if (list1.isEmpty() && !list2.isEmpty())
		{
			newList = list2;
		}
		else
		{
			newList = new ArrayList<>(list1.size() + list2.size());
			newList.addAll(list1);
			newList.addAll(list2);
		}
		return newList;
	}

	protected List<UiType> collectPossibleTypes(final CsticModel model, final boolean isDebugEnabled)
	{
		List<UiType> possibleTypes;
		if (isReadonly(model, isDebugEnabled))
		{
			possibleTypes = Collections.singletonList(UiType.READ_ONLY);
		}
		else
		{
			possibleTypes = checkForSingelValueTypes(model, isDebugEnabled);

			possibleTypes = mergeUiTypeListLowMem(possibleTypes, checkForMultiSelectionTypes(model, isDebugEnabled));

			possibleTypes = mergeUiTypeListLowMem(possibleTypes, checkForSingleSelectionTypes(model, isDebugEnabled));
		}
		return possibleTypes;
	}

	private List<UiType> checkForSingleSelectionTypes(final CsticModel model, final boolean isDebugEnabled)
	{
		List<UiType> possibleTypes = Collections.emptyList();
		if (isRadioButton(model, isDebugEnabled))
		{
			possibleTypes = addUiTypeToListLowMem(possibleTypes, UiType.RADIO_BUTTON);
		}
		if (isRadioButtonAdditionalValue(model, isDebugEnabled))
		{
			possibleTypes = addUiTypeToListLowMem(possibleTypes, UiType.RADIO_BUTTON_ADDITIONAL_INPUT);
		}
		if (isDDLB(model, isDebugEnabled))
		{
			possibleTypes = addUiTypeToListLowMem(possibleTypes, UiType.DROPDOWN);
		}
		if (isDDLBAdditionalValue(model, isDebugEnabled))
		{
			possibleTypes = addUiTypeToListLowMem(possibleTypes, UiType.DROPDOWN_ADDITIONAL_INPUT);
		}

		return possibleTypes;
	}

	private List<UiType> checkForMultiSelectionTypes(final CsticModel model, final boolean isDebugEnabled)
	{
		List<UiType> possibleTypes = Collections.emptyList();
		if (isCheckbox(model, isDebugEnabled))
		{
			possibleTypes = addUiTypeToListLowMem(possibleTypes, UiType.CHECK_BOX);
		}
		if (isCheckboxList(model, isDebugEnabled))
		{
			possibleTypes = addUiTypeToListLowMem(possibleTypes, UiType.CHECK_BOX_LIST);
		}

		return possibleTypes;
	}

	private List<UiType> checkForSingelValueTypes(final CsticModel model, final boolean isDebugEnabled)
	{
		List<UiType> possibleTypes = Collections.emptyList();
		if (isStringInput(model, isDebugEnabled))
		{
			possibleTypes = addUiTypeToListLowMem(possibleTypes, UiType.STRING);
		}
		if (isNumericInput(model, isDebugEnabled))
		{
			possibleTypes = addUiTypeToListLowMem(possibleTypes, UiType.NUMERIC);

		}

		return possibleTypes;
	}

	protected UiType chooseUiType(final List<UiType> posibleTypes, final CsticModel model, final boolean isDebugEnabled)
	{
		UiType uiType;
		if (posibleTypes.isEmpty())
		{
			uiType = UiType.NOT_IMPLEMENTED;
		}
		else if (posibleTypes.size() == 1)
		{
			uiType = posibleTypes.get(0);
		}
		else
		{
			throw new IllegalArgumentException("Cstic: [" + model + "] has an ambigious uiType: [" + posibleTypes + "]");
		}

		return uiType;
	}

	protected List<UiValidationType> collectPossibleValidationTypes(final CsticModel model, final boolean isDebugEnabled)
	{
		List<UiValidationType> possibleTypes;

		if (isReadonly(model, isDebugEnabled))
		{
			possibleTypes = Collections.singletonList(UiValidationType.NONE);
		}
		else if (isSimpleNumber(model, isDebugEnabled)
				&& (isInput(model, isDebugEnabled) || (isSingleSelection(model, isDebugEnabled) && editableWithAdditionalValue(model,
						isDebugEnabled))))
		{
			possibleTypes = Collections.singletonList(UiValidationType.NUMERIC);
		}
		else
		{
			possibleTypes = Collections.emptyList();
		}

		return possibleTypes;
	}

	protected UiValidationType chooseUiValidationType(final List<UiValidationType> posibleTypes, final CsticModel model,
			final boolean isDebugEnabled)
	{
		UiValidationType uiType;
		if (posibleTypes.isEmpty())
		{
			uiType = UiValidationType.NONE;
		}
		else if (posibleTypes.size() == 1)
		{
			uiType = posibleTypes.get(0);
		}
		else
		{
			throw new IllegalArgumentException("Cstic: [" + model + "] has an ambigious uiValidationType: [" + posibleTypes + "]");
		}
		return uiType;
	}

	private boolean isReadonly(final CsticModel model, final boolean isDebugEnabled)
	{
		boolean isReadOnly;
		isReadOnly = model.isReadonly();
		if (model.isConstrained() && (model.getAssignableValues() == null || model.getAssignableValues().size() == 0))
		{
			isReadOnly = true;
		}

		if (isDebugEnabled)
		{
			LOG.debug("CsticModel [CSTIC_NAME='" + model.getName() + "';CSTIC_isReadonly='" + isReadOnly + "']");
		}

		return isReadOnly;
	}

	protected boolean isDDLB(final CsticModel model, final boolean isDebugEnabled)
	{
		boolean isDDLB;
		isDDLB = isSingleSelection(model, isDebugEnabled) && model.getAssignableValues().size() > dropDownListThreshold
				&& editableWithoutAdditionalValue(model, isDebugEnabled);

		if (isDebugEnabled)
		{
			LOG.debug("CsticModel [CSTIC_NAME='" + model.getName() + "';CSTIC_isDDLB='" + isDDLB + "']");
		}

		return isDDLB;
	}

	protected boolean isDDLBAdditionalValue(final CsticModel model, final boolean isDebugEnabled)
	{
		boolean isDDLB;
		isDDLB = isSingleSelection(model, isDebugEnabled) && model.getAssignableValues().size() > dropDownListThreshold
				&& editableWithAdditionalValue(model, isDebugEnabled);

		if (isDebugEnabled)
		{
			LOG.debug("CsticModel [CSTIC_NAME='" + model.getName() + "';CSTIC_isDDLBAdditionalValue='" + isDDLB + "']");
		}

		return isDDLB;
	}

	protected boolean isRadioButton(final CsticModel model, final boolean isDebugEnabled)
	{
		boolean isRadioButton;
		isRadioButton = isSingleSelection(model, isDebugEnabled) && model.getAssignableValues().size() <= dropDownListThreshold
				&& editableWithoutAdditionalValue(model, isDebugEnabled);

		if (isDebugEnabled)
		{
			LOG.debug("CsticModel [CSTIC_NAME='" + model.getName() + "';CSTIC_isRadioButton='" + isRadioButton + "']");
		}

		return isRadioButton;
	}

	protected boolean isRadioButtonAdditionalValue(final CsticModel model, final boolean isDebugEnabled)
	{
		boolean isRadioButton;
		isRadioButton = isSingleSelection(model, isDebugEnabled) && model.getAssignableValues().size() <= dropDownListThreshold
				&& editableWithAdditionalValue(model, isDebugEnabled);

		if (isDebugEnabled)
		{
			LOG.debug("CsticModel [CSTIC_NAME='" + model.getName() + "';CSTIC_isRadioButtonAdditionalValue='" + isRadioButton + "']");
		}

		return isRadioButton;
	}

	protected boolean isCheckbox(final CsticModel model, final boolean isDebugEnabled)
	{
		boolean isCheckbox;
		isCheckbox = isMultiSelection(model, isDebugEnabled) && model.getStaticDomainLength() == 1;

		if (isDebugEnabled)
		{
			LOG.debug("CsticModel [CSTIC_NAME='" + model.getName() + "';CSTIC_isCheckbox='" + isCheckbox + "']");
		}
		return isCheckbox;
	}

	protected boolean isCheckboxList(final CsticModel model, final boolean isDebugEnabled)
	{
		boolean isCheckboxList;
		isCheckboxList = isMultiSelection(model, isDebugEnabled)
				&& (model.isConstrained() || model.getAssignableValues().size() > 0) && model.getStaticDomainLength() > 1;

		if (isDebugEnabled)
		{
			LOG.debug("CsticModel [CSTIC_NAME='" + model.getName() + "';CSTIC_isCheckboxList='" + isCheckboxList + "']");
		}

		return isCheckboxList;
	}

	protected boolean isStringInput(final CsticModel model, final boolean isDebugEnabled)
	{
		final boolean isStringInput = isInput(model, isDebugEnabled) && CsticModel.TYPE_STRING == model.getValueType();

		if (isDebugEnabled)
		{
			LOG.debug("CsticModel [CSTIC_NAME='" + model.getName() + "';CSTIC_isStringInput='" + isStringInput + "']");
		}

		return isStringInput;
	}

	protected boolean isNumericInput(final CsticModel model, final boolean isDebugEnabled)
	{
		final boolean isNumeric = isInput(model, isDebugEnabled)
				&& (CsticModel.TYPE_INTEGER == model.getValueType() || CsticModel.TYPE_FLOAT == model.getValueType());

		if (isDebugEnabled)
		{
			LOG.debug("CsticModel [CSTIC_NAME='" + model.getName() + "';CSTIC_isNumericInput='" + isNumeric + "']");
		}

		return isNumeric;
	}

	protected boolean isSelection(final CsticModel model, final boolean isDebugEnabled)
	{
		final boolean isSelection = isValueTypeSupported(model, isDebugEnabled)
				&& (model.isConstrained() || model.getAssignableValues().size() > 0) && !model.isIntervalInDomain();

		if (isDebugEnabled)
		{
			LOG.debug("CsticModel [CSTIC_NAME='" + model.getName() + "';CSTIC_isSelection='" + isSelection + "']");
		}

		return isSelection;
	}

	protected boolean isMultiSelection(final CsticModel model, final boolean isDebugEnabled)
	{
		final boolean isMultiSelection = isSelection(model, isDebugEnabled) && model.isMultivalued();

		if (isDebugEnabled)
		{
			LOG.debug("CsticModel [CSTIC_NAME='" + model.getName() + "';CSTIC_isMultiSelection='" + isMultiSelection + "']");
		}

		return isMultiSelection;
	}

	protected boolean isSingleSelection(final CsticModel model, final boolean isDebugEnabled)
	{
		final boolean isSingleSelection = isSelection(model, isDebugEnabled) && !model.isMultivalued();

		if (isDebugEnabled)
		{
			LOG.debug("CsticModel [CSTIC_NAME='" + model.getName() + "';CSTIC_isSingleSelection='" + isSingleSelection + "']");
		}

		return isSingleSelection;
	}

	protected boolean isInput(final CsticModel model, final boolean isDebugEnabled)
	{
		final boolean isInput = isValueTypeSupported(model, isDebugEnabled)
				&& editableWithoutAdditionalValue(model, isDebugEnabled)
				&& !model.isMultivalued()
				&& ((model.getAssignableValues().size() == 0 && !model.isConstrained()) || (model.getAssignableValues().size() > 0 && model
						.isIntervalInDomain()));

		if (isDebugEnabled)
		{
			LOG.debug("CsticModel [CSTIC_NAME='" + model.getName() + "';CSTIC_isInput='" + isInput + "']");
		}

		return isInput;
	}

	protected boolean editableWithoutAdditionalValue(final CsticModel model, final boolean isDebugEnabled)
	{
		final boolean isSupported = !model.isAllowsAdditionalValues() && !model.isReadonly();

		if (isDebugEnabled)
		{
			LOG.debug("CsticModel [CSTIC_NAME='" + model.getName() + "';CSTIC_editableWithoutAdditionalValue='" + isSupported + "']");
		}

		return isSupported;
	}

	protected boolean editableWithAdditionalValue(final CsticModel model, final boolean isDebugEnabled)
	{
		final boolean isSupported = model.isAllowsAdditionalValues() && !model.isReadonly() && !model.isIntervalInDomain();

		if (isDebugEnabled)
		{
			LOG.debug("CsticModel [CSTIC_NAME='" + model.getName() + "';CSTIC_editableWithAdditionalValue='" + isSupported + "']");
		}

		return isSupported;
	}

	protected boolean isValueTypeSupported(final CsticModel model, final boolean isDebugEnabled)
	{
		final boolean isValueTypeSupported = (isSimpleString(model, isDebugEnabled) || isSimpleNumber(model, isDebugEnabled));

		if (isDebugEnabled)
		{
			LOG.debug("CsticModel [CSTIC_NAME='" + model.getName() + "';CSTIC_isValueTypeSupported='" + isValueTypeSupported + "']");
		}

		return isValueTypeSupported;
	}

	protected boolean isSimpleString(final CsticModel model, final boolean isDebugEnabled)
	{
		boolean isSimpleString = CsticModel.TYPE_STRING == model.getValueType();
		if (isSimpleString)
		{
			isSimpleString = model.getEntryFieldMask() == null || model.getEntryFieldMask().isEmpty();
		}

		if (isDebugEnabled)
		{
			LOG.debug("CsticModel [CSTIC_NAME='" + model.getName() + "';CSTIC_isSimpleString='" + isSimpleString + "']");
		}

		return isSimpleString;
	}

	protected boolean isSimpleNumber(final CsticModel model, final boolean isDebugEnabled)
	{
		boolean isNumber = CsticModel.TYPE_INTEGER == model.getValueType() || CsticModel.TYPE_FLOAT == model.getValueType();
		if (isNumber)
		{
			// Scientific format and multi values interval is not supported
			final boolean isScientific = model.getEntryFieldMask() != null && model.getEntryFieldMask().contains("E");

			isNumber = !isScientific;
		}

		if (isDebugEnabled)
		{
			LOG.debug("CsticModel [CSTIC_NAME='" + model.getName() + "';CSTIC_isSimpleNumber='" + isNumber + "']");
		}
		return isNumber;
	}

	/**
	 * @param dropDownListThreshold
	 *           the dropDownListThreshold to set
	 */
	public void setDropDownListThreshold(final int dropDownListThreshold)
	{
		this.dropDownListThreshold = dropDownListThreshold;
	}

	@Override
	public UiValidationType findUiValidationTypeForCstic(final CsticModel model)
	{
		// This method might be called very often (several thousand times) for large customer models.
		// isDebugEnabled causes some memory allocation internally, which adds up a lot (2 MB for 90.000 calls)
		// so we read it only once per cstic
		final boolean isDebugEnabled = LOG.isDebugEnabled();
		final List<UiValidationType> possibleTypes = collectPossibleValidationTypes(model, isDebugEnabled);
		final UiValidationType uiValidationType = chooseUiValidationType(possibleTypes, model, isDebugEnabled);

		if (isDebugEnabled)
		{
			LOG.debug("UI validation type found for cstic model [CSTIC_NAME='" + model.getName() + "';CSTIC_TYPE='"
					+ model.getValueType() + "';CSTIC_UI_VALIDATION_TYPE='" + uiValidationType + "']");
		}

		return uiValidationType;
	}
}
