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

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.classification.ClassificationSystemService;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.sap.productconfig.facades.ConfigPricing;
import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.CsticStatusType;
import de.hybris.platform.sap.productconfig.facades.CsticTypeMapper;
import de.hybris.platform.sap.productconfig.facades.CsticValueData;
import de.hybris.platform.sap.productconfig.facades.UiType;
import de.hybris.platform.sap.productconfig.facades.UiTypeFinder;
import de.hybris.platform.sap.productconfig.facades.UiValidationType;
import de.hybris.platform.sap.productconfig.facades.ValueFormatTranslator;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.PriceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.PriceModelImpl;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


public class CsticTypeMapperImpl implements CsticTypeMapper
{
	private static final ThreadLocal<StringBuilder> keyBuilder = new ThreadLocal()
	{
		@Override
		protected StringBuilder initialValue()
		{
			return new StringBuilder(128);
		}
	};

	private static final String KEY_SEPARATOR = ".";

	private static final String LOG_SLOW_PERF = "Using deprecated Mapping without cacheMap for hybris classification, mapping for large KBs might be slow.";

	private static final String EMPTY = "";

	private UiTypeFinder uiTypeFinder;

	private ClassificationSystemService classificationService;

	private BaseStoreService baseStoreService;

	private final static Logger LOG = Logger.getLogger(CsticTypeMapperImpl.class);

	@Autowired
	private ValueFormatTranslator valueFormatTranslator;

	@Autowired
	private ConfigPricing pricingFactory;

	private ClassificationSystemVersionModel classificationSystemVersionModel;

	private static Pattern HTML_MATCHING_PATTERN = Pattern.compile(".*\\<.+?\\>.*");

	@SuppressWarnings("deprecation")
	@Deprecated
	@Override
	public CsticData mapCsticModelToData(final CsticModel model, final String prefix)
	{
		return mapCsticModelToData(model, prefix, null);
	}

	@Override
	public CsticData mapCsticModelToData(final CsticModel model, final String prefix,
			final Map<String, HybrisCsticAndValueNames> nameMap)
	{
		// This method might be called very often (several thousand times) for large customer models.
		// LOG.isDebugEnabled() causes some memory allocation internally, which adds up a lot (2 MB for 90.000 calls)
		// so we read it only once per cstic
		final boolean isDebugEnabled = LOG.isDebugEnabled();
		final CsticData data = new CsticData();
		data.setKey(generateUniqueKey(model, prefix));

		final String name = model.getName();
		final HybrisCsticAndValueNames hybrisNames = getHybrisNames(name, nameMap);
		data.setName(name);
		data.setLangdepname(getDisplayName(model, hybrisNames, isDebugEnabled));
		final String longText = getLongText(model, hybrisNames, isDebugEnabled);
		data.setLongText(longText);
		data.setLongTextHTMLFormat(containsHTML(longText, isDebugEnabled));

		data.setInstanceId(model.getInstanceId());
		data.setVisible(model.isVisible());
		data.setRequired(model.isRequired());
		data.setIntervalInDomain(model.isIntervalInDomain());

		data.setMaxlength(model.getTypeLength());
		data.setEntryFieldMask(emptyIfNull(model.getEntryFieldMask()));
		data.setPlaceholder(emptyIfNull(model.getPlaceholder()));
		data.setAdditionalValue(EMPTY);

		final List<CsticValueData> domainValues = createDomainValues(model, hybrisNames, prefix, isDebugEnabled);
		data.setDomainvalues(domainValues);

		data.setConflicts(Collections.emptyList());
		if (CsticModel.AUTHOR_USER.equals(model.getAuthor()))
		{
			data.setCsticStatus(CsticStatusType.FINISHED);
		}
		else
		{
			data.setCsticStatus(CsticStatusType.DEFAULT);
		}

		final UiType uiType = uiTypeFinder.findUiTypeForCstic(model);
		data.setType(uiType);
		final UiValidationType validationType = uiTypeFinder.findUiValidationTypeForCstic(model);
		data.setValidationType(validationType);

		final String singleValue = model.getSingleValue();
		final String formattedValue = valueFormatTranslator.format(uiType, singleValue);
		data.setValue(formattedValue);
		data.setLastValidValue(formattedValue);

		if (UiValidationType.NUMERIC == validationType)
		{
			mapNumericSpecifics(model, data);
		}

		if (isDebugEnabled)
		{
			LOG.debug("Map CsticModel to CsticData [CSTIC_NAME='" + name + "';CSTIC_UI_KEY='" + data.getKey() + "';CSTIC_UI_TYPE='"
					+ data.getType() + "';CSTIC_VALUE='" + data.getValue() + "']");
		}

		return data;
	}

	protected HybrisCsticAndValueNames getHybrisNames(final String name, final Map<String, HybrisCsticAndValueNames> nameMap)
	{
		HybrisCsticAndValueNames hybrisNames = null;
		if (nameMap != null)
		{
			hybrisNames = nameMap.get(name);
		}
		else
		{
			LOG.debug(LOG_SLOW_PERF);
		}
		if (hybrisNames == null)
		{
			final ClassificationAttributeModel attr = getClassificationAttribute(name);
			hybrisNames = getNameFromAttribute(attr);
			if (nameMap != null)
			{
				nameMap.put(name, hybrisNames);

			}
		}

		return hybrisNames;
	}

	protected HybrisCsticAndValueNames getNameFromAttribute(final ClassificationAttributeModel attr)
	{
		HybrisCsticAndValueNames hybrisNames;
		if (attr == null)
		{
			hybrisNames = HybrisCsticAndValueNames.NULL_OBJ;
		}
		else
		{
			final String code = attr.getCode();
			final String name = attr.getName();
			final String description = attr.getDescription();
			final Map<String, String> valueNames = extractValueNamesFromAttributeModel(attr);
			hybrisNames = new HybrisCsticAndValueNames(code, name, description, valueNames);
		}
		return hybrisNames;
	}

	protected Map<String, String> extractValueNamesFromAttributeModel(final ClassificationAttributeModel attr)
	{

		Map<String, String> hybrisValueNameMap = Collections.emptyMap();
		if (attr != null)
		{
			final List<ClassificationAttributeValueModel> attrValues = attr.getDefaultAttributeValues();
			if (attrValues != null && !attrValues.isEmpty())
			{
				final String attrKeyPrefix = attr.getCode() + "_";
				final int capa = (int) (attrValues.size() / 0.75 + 1);
				hybrisValueNameMap = new HashMap<>(capa);
				for (final ClassificationAttributeValueModel attrValue : attrValues)
				{
					final String attrCode = attrValue.getCode();
					hybrisValueNameMap.put(attrCode, attrValue.getName());
					// backward compatibility, find attrValue with key
					// csticName_csticValueName
					hybrisValueNameMap.put(attrKeyPrefix + attrCode, attrValue.getName());
				}
			}
		}
		return hybrisValueNameMap;
	}

	private String getLongText(final CsticModel model, final HybrisCsticAndValueNames hybrisNames, final boolean isDebugEnabled)
	{
		String longText;

		final String hybrisLongText = hybrisNames.getDescription();
		if (isNeigtherNullNorEmpty(hybrisLongText))
		{
			longText = hybrisLongText;
			logValue("CsticModel", "CSTIC_NAME", model.getName(), "LONG_TEXT", "HYBRIS_NAME", longText, isDebugEnabled);
		}
		else if (isNeigtherNullNorEmpty(model.getLongText()))
		{
			longText = model.getLongText();
			logValue("CsticModel", "CSTIC_NAME", model.getName(), "LONG_TEXT", "MODEL_LONGTEXT", longText, isDebugEnabled);
		}
		else
		{
			longText = null;
			logValue("CsticModel", "CSTIC_NAME", model.getName(), "LONG_TEXT", "MODEL_LONGTEXT", longText, isDebugEnabled);
		}

		return longText;
	}

	private void logValue(final String className, final String nameType, final String name, final String targetType,
			final String sourceType, final String value, final boolean isDebugEnabled)
	{
		if (isDebugEnabled)
		{
			LOG.debug(className + " [" + nameType + "='" + name + "'; " + targetType + "(" + sourceType + ")='" + value + "']");
		}
	}

	protected boolean isNeigtherNullNorEmpty(final String string)
	{
		return string != null && !string.isEmpty();
	}

	private String getDisplayName(final CsticModel csticModel, final HybrisCsticAndValueNames hybrisNames,
			final boolean isDebugEnabled)
	{

		final String langDepName = csticModel.getLanguageDependentName();
		final String hybrisName = hybrisNames.getName();
		String displayName;
		if (isNeigtherNullNorEmpty(hybrisName))
		{
			displayName = hybrisName;
			logValue("CsticModel", "CSTIC_NAME", csticModel.getName(), "DISPLAY_NAME", "HYBRIS_NAME", displayName, isDebugEnabled);
		}
		else if (isNeigtherNullNorEmpty(langDepName))
		{
			displayName = langDepName;
			logValue("CsticModel", "CSTIC_NAME", csticModel.getName(), "DISPLAY_NAME", "MODEL_LANGDEP_NAME", displayName,
					isDebugEnabled);
		}
		else
		{
			displayName = "[" + csticModel.getName() + "]";
			logValue("CsticModel", "CSTIC_NAME", csticModel.getName(), "DISPLAY_NAME", "MODEL_NAME", displayName, isDebugEnabled);
		}

		return displayName;
	}

	protected ClassificationAttributeModel getClassificationAttribute(final String name)
	{
		final ClassificationSystemVersionModel systemVersion = getSystemVersion();

		ClassificationAttributeModel attribute = null;
		if (systemVersion != null)
		{
			try
			{

				attribute = classificationService.getAttributeForCode(systemVersion, name);

			}
			catch (final UnknownIdentifierException e)
			{
				LOG.debug("The classification attribute is not found for the name '" + name + "'", e);
			}
		}

		return attribute;
	}

	private String getDisplayValueName(final CsticValueModel valueModel, final HybrisCsticAndValueNames hybrisNames,
			final boolean isDebugEnabled)
	{

		String hybrisValueName = null;
		if (hybrisNames.getValueNames().size() > 0)
		{
			hybrisValueName = hybrisNames.getValueNames().get(valueModel.getName());
		}
		final String langDepName = valueModel.getLanguageDependentName();

		String displayName;
		if (isNeigtherNullNorEmpty(hybrisValueName))
		{
			displayName = hybrisValueName;
			logValue("CsticValueModel", "VALUE_NAME", valueModel.getName(), "DISPLAY_NAME", "HYBRIS_NAME", displayName,
					isDebugEnabled);
		}
		else if (isNeigtherNullNorEmpty(langDepName))
		{
			displayName = langDepName;
			logValue("CsticValueModel", "VALUE_NAME", valueModel.getName(), "DISPLAY_NAME", "MODEL_LANGDEP_NAME", displayName,
					isDebugEnabled);
		}
		else
		{
			displayName = "[" + valueModel.getName() + "]";
			logValue("CsticValueModel", "VALUE_NAME", valueModel.getName(), "DISPLAY_NAME", "MODEL_NAME", displayName,
					isDebugEnabled);
		}

		return displayName;
	}

	/**
	 * @return classification system Version model
	 */
	private ClassificationSystemVersionModel getSystemVersion()
	{
		if (classificationSystemVersionModel == null)
		{
			final BaseStoreModel baseStore = baseStoreService.getCurrentBaseStore();
			if (baseStore == null)
			{
				throw new IllegalStateException("No base store available");
			}

			final List<CatalogModel> catalogs = baseStore.getCatalogs();
			for (final CatalogModel catalog : catalogs)
			{
				try
				{
					return classificationService.getSystemVersion(catalog.getId(), catalog.getVersion());

				}
				catch (final UnknownIdentifierException e)
				{
					LOG.debug("No classification sytem found for Id '" + catalog.getId() + "'", e);
					continue;
				}
			}
		}
		return classificationSystemVersionModel;
	}

	private String emptyIfNull(final String value)
	{
		return (value == null) ? EMPTY : value;
	}

	protected void mapNumericSpecifics(final CsticModel model, final CsticData data)
	{
		final int numFractionDigits = model.getNumberScale();
		final int typeLength = model.getTypeLength();
		data.setNumberScale(numFractionDigits);
		data.setTypeLength(typeLength);

		int maxlength = typeLength;
		if (numFractionDigits > 0)
		{
			maxlength++;
		}
		final int numDigits = typeLength - numFractionDigits;
		final int maxGroupimgSeperators = (numDigits - 1) / 3;
		maxlength += maxGroupimgSeperators;
		data.setMaxlength(maxlength);
	}

	private List<CsticValueData> createDomainValues(final CsticModel model, final HybrisCsticAndValueNames hybrisNames,
			final String prefix, final boolean isDebugEnabled)
	{
		int capa = model.getAssignableValues().size();
		if (model.isConstrained())
		{
			capa += model.getAssignedValues().size();
		}
		final List<CsticValueData> domainValues;
		if (capa == 0)
		{
			domainValues = Collections.emptyList();
		}
		else
		{
			domainValues = new ArrayList<>(capa);
		}

		for (final CsticValueModel csticValue : model.getAssignableValues())
		{
			final CsticValueData domainValue = createDomainValue(model, csticValue, hybrisNames, prefix, isDebugEnabled);
			domainValues.add(domainValue);
		}
		if (model.isConstrained())
		{
			for (final CsticValueModel assignedValue : model.getAssignedValues())
			{
				if (!model.getAssignableValues().contains(assignedValue))
				{
					final CsticValueData domainValue = createDomainValue(model, assignedValue, hybrisNames, prefix, isDebugEnabled);
					domainValues.add(domainValue);
				}
			}
		}

		harmonizeDeltaPricing(domainValues);

		return domainValues;

	}

	/**
	 * @param domainValues
	 */
	protected void harmonizeDeltaPricing(final List<CsticValueData> domainValues)
	{
		boolean atleastOneValueHasADeltaPrice = false;
		PriceData nonZeroDeltaPrice = null;
		for (final CsticValueData domainValue : domainValues)
		{
			if (domainValue.getDeltaPrice() != ConfigPricing.NO_PRICE)
			{
				atleastOneValueHasADeltaPrice = true;
				nonZeroDeltaPrice = domainValue.getDeltaPrice();
				break;
			}
		}
		if (atleastOneValueHasADeltaPrice)
		{
			for (final CsticValueData domainValue : domainValues)
			{
				if (domainValue.getDeltaPrice() == ConfigPricing.NO_PRICE)
				{
					final PriceModel priceModel = new PriceModelImpl();
					priceModel.setCurrency(nonZeroDeltaPrice.getCurrencyIso());
					priceModel.setPriceValue(BigDecimal.ZERO);
					final PriceData priceData = pricingFactory.getPriceData(priceModel);
					domainValue.setDeltaPrice(priceData);
				}
			}
		}

	}

	protected CsticValueData createDomainValue(final CsticModel csticModel, final CsticValueModel csticValueModel,
			final HybrisCsticAndValueNames hybrisNames, final String prefix, final boolean isDebugEnabled)
	{
		final CsticValueData domainValue = new CsticValueData();
		final String name = csticValueModel.getName();
		domainValue.setKey(name);
		String langDepName;
		if (isNumericValueType(csticModel, isDebugEnabled))
		{
			langDepName = valueFormatTranslator.format(UiType.NUMERIC, name);
		}
		else
		{
			langDepName = getDisplayValueName(csticValueModel, hybrisNames, isDebugEnabled);
		}
		domainValue.setLangdepname(langDepName);
		domainValue.setName(name);
		final boolean isAssigned = csticModel.getAssignedValues().contains(csticValueModel);
		domainValue.setSelected(isAssigned);

		final boolean isReadOnly = checkReadonly(csticValueModel, isDebugEnabled);
		domainValue.setReadonly(isReadOnly);

		final PriceData price = pricingFactory.getPriceData(csticValueModel.getDeltaPrice());
		domainValue.setDeltaPrice(price);

		return domainValue;
	}

	protected boolean isNumericValueType(final CsticModel model, final boolean isDebugEnabled)
	{
		final boolean isNumeric = CsticModel.TYPE_INTEGER == model.getValueType() || CsticModel.TYPE_FLOAT == model.getValueType();

		if (isDebugEnabled)
		{
			LOG.debug("CsticModel [CSTIC_NAME='" + model.getName() + "';CSTIC_VALUE='" + model.getValueType()
					+ "';CSTIC_IS_NUMERIC='" + isNumeric + "']");
		}

		return isNumeric;
	}

	protected boolean checkReadonly(final CsticValueModel csticValue, final boolean isDebugEnabled)
	{
		final boolean isSystemValue = csticValue.getAuthor() != null && csticValue.getAuthor().equalsIgnoreCase(READ_ONLY_AUTHOR);

		final boolean isSelectable = csticValue.isSelectable();

		if (isDebugEnabled)
		{
			LOG.debug("CsticValueModel [CSTIC_NAME='" + csticValue.getName() + "';CSTIC_IS_SYSTEM_VALUE='" + isSystemValue
					+ "';CSTIC_IS_SELECTABLE='" + isSelectable + "']");
		}

		return isSystemValue || !isSelectable;
	}

	@Override
	public void setUiTypeFinder(final UiTypeFinder uiTypeFinder)
	{
		this.uiTypeFinder = uiTypeFinder;
	}

	@Override
	public void updateCsticModelValuesFromData(final CsticData data, final CsticModel model)
	{
		// This method might be called very often (several thousand times) for large customer models.
		// LOG.isDebugEnabled() causes some memory allocation internally, which adds up a lot (2 MB for 90.000 calls)
		// so we read it only once per cstic
		final boolean isDebugEnabled = LOG.isDebugEnabled();
		handleRetraction(data, model, isDebugEnabled);
		final UiType uiType = data.getType();
		if (UiType.CHECK_BOX_LIST == uiType || UiType.CHECK_BOX == uiType)
		{
			for (final CsticValueData valueData : data.getDomainvalues())
			{
				final String value = valueData.getName();
				final String parsedValue = valueFormatTranslator.parse(uiType, value);
				if (valueData.isSelected())
				{
					model.addValue(parsedValue);
				}
				else
				{
					model.removeValue(parsedValue);
				}
			}
		}
		else
		{
			final String value = getValueFromCstcData(data, isDebugEnabled);
			final String parsedValue = valueFormatTranslator.parse(uiType, value);
			if ((UiType.DROPDOWN == uiType || UiType.DROPDOWN_ADDITIONAL_INPUT == uiType) && "NULL_VALUE".equals(value))
			{
				model.setSingleValue(null);
			}
			else
			{
				model.setSingleValue(parsedValue);
			}
		}

		if (isDebugEnabled)
		{
			LOG.debug("Update CsticData to CsticModel [CSTIC_NAME='" + model.getName() + "';CSTIC_UI_KEY='" + data.getKey()
					+ "';CSTIC_UI_TYPE='" + data.getType() + "';CSTIC_VALUE='" + data.getValue() + "']");
		}

	}

	/**
	 * Handles the retraction of a cstic which means that all user inputs to this cstic are discarded. This is needed for
	 * conflict solving
	 *
	 * @param data
	 * @param model
	 */
	protected void handleRetraction(final CsticData data, final CsticModel model, final boolean isDebugEnabled)
	{
		if (data.isRetractTriggered())
		{
			model.setRetractTriggered(true);
			if (isDebugEnabled)
			{
				LOG.debug("Cstic: " + data.getName() + " is marked as retracted");
			}
		}
	}

	/**
	 * @param data
	 * @return value of characteristic
	 */
	protected String getValueFromCstcData(final CsticData data, final boolean isDebugEnabled)
	{
		String value = data.getValue();
		final UiType uiType = data.getType();
		if (UiType.RADIO_BUTTON_ADDITIONAL_INPUT == uiType || UiType.DROPDOWN_ADDITIONAL_INPUT == uiType)
		{
			final String additionalValue = data.getAdditionalValue();
			if (additionalValue != null && !additionalValue.isEmpty())
			{
				value = additionalValue;
			}
		}

		if (isDebugEnabled)
		{
			LOG.debug("CsticData [CSTIC_NAME='" + data.getName() + "';CSTIC_VALUE='" + value + "']");
		}

		return value;
	}

	@Override
	public void setValueFormatTranslater(final ValueFormatTranslator valueFormatTranslater)
	{
		this.valueFormatTranslator = valueFormatTranslater;
	}

	@Deprecated
	@Override
	public String generateUniqueKey(final CsticModel model, final CsticValueModel value, final String prefix)
	{
		final StringBuilder strBuilder = keyBuilder.get();
		strBuilder.setLength(0);
		if (strBuilder.capacity() > 1024)
		{
			strBuilder.trimToSize();
			strBuilder.ensureCapacity(1028);
		}
		strBuilder.append(prefix);
		strBuilder.append(KEY_SEPARATOR);
		strBuilder.append(model.getName());
		if (value != null)
		{
			strBuilder.append(KEY_SEPARATOR);
			strBuilder.append(value.getName());
		}

		return strBuilder.toString();
	}

	@Override
	public String generateUniqueKey(final CsticModel model, final String prefix)
	{
		final String key = generateUniqueKey(model, null, prefix);

		if (LOG.isDebugEnabled())
		{
			LOG.debug("CsticModel [CSTIC_NAME='" + model.getName() + "';CSTIC_UI_KEY='" + key + "']");
		}

		return key;
	}

	protected ConfigPricing getPricingFactory()
	{
		return pricingFactory;
	}

	@Override
	public void setPricingFactory(final ConfigPricing pricingFactory)
	{
		this.pricingFactory = pricingFactory;
	}

	@Override
	public void setClassificationService(final ClassificationSystemService classificationService)
	{
		this.classificationService = classificationService;
	}

	@Override
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;

	}

	protected boolean containsHTML(final String longText, final boolean isDebugEnabled)
	{
		boolean containsHTML = false;
		if (longText != null)
		{
			containsHTML = HTML_MATCHING_PATTERN.matcher(longText).matches();
		}

		if (isDebugEnabled)
		{
			LOG.debug("Long text contains HTML: '" + containsHTML + "'");
		}

		return containsHTML;
	}

}
