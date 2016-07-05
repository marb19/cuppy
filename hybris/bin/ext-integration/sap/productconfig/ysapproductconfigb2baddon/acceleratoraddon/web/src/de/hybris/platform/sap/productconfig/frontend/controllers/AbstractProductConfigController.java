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
package de.hybris.platform.sap.productconfig.frontend.controllers;


import de.hybris.platform.acceleratorservices.data.RequestContextData;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.forms.ReviewForm;
import de.hybris.platform.acceleratorstorefrontcommons.util.MetaSanitizerUtil;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ImageDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.sap.productconfig.facades.ConfigurationCartIntegrationFacade;
import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.facades.ConfigurationFacade;
import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.CsticStatusType;
import de.hybris.platform.sap.productconfig.facades.GroupStatusType;
import de.hybris.platform.sap.productconfig.facades.GroupType;
import de.hybris.platform.sap.productconfig.facades.KBKeyData;
import de.hybris.platform.sap.productconfig.facades.SessionAccessFacade;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;
import de.hybris.platform.sap.productconfig.facades.UiType;
import de.hybris.platform.sap.productconfig.frontend.UiGroupStatus;
import de.hybris.platform.sap.productconfig.frontend.UiStatus;
import de.hybris.platform.sap.productconfig.frontend.constants.Sapproductconfigb2baddonConstants;
import de.hybris.platform.sap.productconfig.frontend.util.impl.UiDataStats;
import de.hybris.platform.sap.productconfig.frontend.util.impl.UiStatusSync;
import de.hybris.platform.sap.productconfig.frontend.validator.ConflictChecker;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;



public abstract class AbstractProductConfigController extends AbstractPageController
{
	static final String PRODUCT_ATTRIBUTE = "product";


	static private final Logger LOG = Logger.getLogger(AbstractProductConfigController.class.getName());

	@Resource(name = "sapProductConfigFacade")
	private ConfigurationFacade configFacade;

	@Resource(name = "sapProductConfigCartIntegrationFacade")
	private ConfigurationCartIntegrationFacade configCartFacade;

	@Resource(name = "sapProductConfigSessionAccessFacade")
	private SessionAccessFacade sessionAccessFacade;

	@Resource(name = "sapProductConfigValidator")
	private Validator productConfigurationValidator;

	@Resource(name = "sapProductConfigConflictChecker")
	private ConflictChecker productConfigurationConflictChecker;

	@Resource(name = "productVariantFacade")
	private ProductFacade productFacade;

	@Resource(name = "productService")
	private ProductService productService;

	public AbstractProductConfigController()
	{
		super();
	}

	@InitBinder(Sapproductconfigb2baddonConstants.CONFIG_ATTRIBUTE)
	protected void initBinder(final WebDataBinder binder)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("INIT Binder at: " + System.currentTimeMillis());
		}
		binder.setValidator(productConfigurationValidator);
	}



	protected BindingResult getBindingResultForConfig(final ConfigurationData configData, final UiStatus uiStatus)
	{
		resetGroupStatus(configData);
		BindingResult errors = new BeanPropertyBindingResult(configData, Sapproductconfigb2baddonConstants.CONFIG_ATTRIBUTE);

		// UI-Errors
		Map<String, FieldError> userInputToRestore = null;
		if (uiStatus != null)
		{
			userInputToRestore = uiStatus.getUserInputToRestore();
			final Map<String, FieldError> userInputToRemember = uiStatus.getUserInputToRemember();
			userInputToRestore = mergeUiErrors(userInputToRestore, userInputToRemember);
			errors = restoreValidationErrorsOnGetConfig(userInputToRestore, configData, errors);
		}

		productConfigurationConflictChecker.checkConflicts(configData, errors);
		if (configData.getCartItemPK() != null && !configData.getCartItemPK().isEmpty())
		{
			productConfigurationConflictChecker.checkMandatoryFields(configData, errors);
			logConfigurationCheckDeviation(errors, configData);
		}
		getProductConfigurationConflictChecker().checkCompletness(configData);
		countNumberOfUiErrorsPerGroup(configData.getGroups());

		if (userInputToRestore != null)
		{
			final Map<String, FieldError> userInputToRemeber = findCollapsedErrorCstics(userInputToRestore, configData);
			uiStatus.setUserInputToRemember(userInputToRemeber);
			getSessionAccessFacade().setUiStatusForProduct(configData.getKbKey().getProductCode(), uiStatus);
		}

		return errors;
	}

	/**
	 * The ConflictChecker checks only visible characteristics for consistency and completeness as only those
	 * characteristics might be changed by the user.
	 *
	 * If the model is modeled in a way that a conflict appears for an invisible characteristic or an invisible
	 * characteristic is mandatory but not filled this would not be identified by those checks but the overall
	 * configuration status is not consistent/complete. This leads to a situation where the configuration cannot be sent
	 * to the backend system.
	 *
	 * In this case the modeler needs to improve the model to avoid such situations. The user cannot do anything about
	 * this so this is only logged as an error as a hint for the modeler.
	 *
	 * @param errors
	 * @param configData
	 */
	protected void logConfigurationCheckDeviation(final BindingResult errors, final ConfigurationData configData)
	{
		if (!(configData.isComplete() && configData.isConsistent()) && !errors.hasErrors())
		{
			// Configuration is incomplete/inconsistent: check whether this is reflected in the BindingResult
			// BindingResult does not contain errors -> log deviation
			LOG.warn("HINT: Configuration model of product "
					+ configData.getKbKey().getProductCode()
					+ " needs to be improved! Configuration status is [complete="
					+ configData.isComplete()
					+ "; consistent="
					+ configData.isConsistent()
					+ "] but the ConflictChecker signals no errors, i.e. the inconsistency/incompleteness exists at characteristics invisible for the user. Thus the user has no information what went wrong.");
		}

	}

	protected void setCartItemPk(final ConfigurationData configData)
	{
		final String cartItemKey = getSessionAccessFacade().getCartEntryForProduct(configData.getKbKey().getProductCode());
		if (cartItemKey != null)
		{
			final boolean isItemInCart = configCartFacade.isItemInCartByKey(cartItemKey);
			if (!isItemInCart)
			{
				getSessionAccessFacade().removeCartEntryForProduct(configData.getKbKey().getProductCode());

			}
			else
			{
				configData.setCartItemPK(cartItemKey);
			}
		}
	}

	/**
	 * Creates a new configuration session. Either returns a default configuration or creates a configuration from the
	 * external configuration attached to a cart entry. <br>
	 * Stores a new UIStatus based on the new configuration in the session (per product).
	 *
	 * @param kbKey
	 * @param selectedGroup
	 * @param cartItemHandle
	 * @return Null if no configuration could be created
	 */
	protected ConfigurationData loadNewConfiguration(final KBKeyData kbKey, final String selectedGroup, final String cartItemHandle)
	{

		final ConfigurationData configData;
		if (cartItemHandle != null)
		{
			configData = configCartFacade.restoreConfiguration(kbKey, cartItemHandle);
			if (configData == null)
			{
				return null;
			}
		}
		else
		{
			configData = configFacade.getConfiguration(kbKey);
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Load new configuration data with [CONFIG_ID: " + configData.getConfigId() + "]");
		}

		final UiStatusSync uiStatusSync = new UiStatusSync();

		uiStatusSync.setInitialStatus(configData);
		if (selectedGroup != null && !selectedGroup.isEmpty())
		{
			configData.setSelectedGroup(selectedGroup);
		}
		final UiStatus uiStatus = uiStatusSync.extractUiStatusFromConfiguration(configData);
		getSessionAccessFacade().setUiStatusForProduct(configData.getKbKey().getProductCode(), uiStatus);
		return configData;
	}

	protected ConfigurationData reloadConfiguration(final KBKeyData kbKey, final String configId, final String selectedGroup,
			final UiStatus uiStatus)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Reload configuration data with [CONFIG_ID: " + configId + "]");
		}

		final ConfigurationData configData = this.getConfigData(kbKey, configId);


		final UiStatusSync uiStatusSync = new UiStatusSync();
		if (selectedGroup != null && !selectedGroup.isEmpty())
		{
			uiStatusSync.applyUiStatusToConfiguration(configData, uiStatus, selectedGroup);
		}
		else
		{
			uiStatusSync.applyUiStatusToConfiguration(configData, uiStatus);
		}
		return configData;
	}

	protected ConfigurationData getConfigData(final KBKeyData kbKey, final String configId)
	{
		final ConfigurationData configData;
		final ConfigurationData configContent = new ConfigurationData();
		configContent.setConfigId(configId);
		configContent.setKbKey(kbKey);
		configData = configFacade.getConfiguration(configContent);

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Retrieve configuration data with [CONFIG_ID: " + configData.getConfigId() + "]");
		}

		return configData;
	}


	protected ProductModel populateProductModel(final String productCode, final Model model, final HttpServletRequest request)
			throws CMSItemNotFoundException
	{
		final ProductModel productModel = getProductService().getProductForCode(productCode);
		handleRequestContext(request, productModel);
		updatePageTitle(productModel, model);
		populateProductDetailForDisplay(productModel, model);
		model.addAttribute(new ReviewForm());
		model.addAttribute("pageType", "productConfigPage");

		final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(productModel.getKeywords());
		final String metaDescription = MetaSanitizerUtil.sanitizeDescription(productModel.getDescription());
		setUpMetaData(model, metaKeywords, metaDescription);
		return productModel;
	}

	protected void populateProductDetailForDisplay(final ProductModel productModel, final Model model)
			throws CMSItemNotFoundException
	{
		final ProductData productData = getProductFacade().getProductForOptions(
				productModel,
				Arrays.asList(ProductOption.BASIC, ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION,
						ProductOption.GALLERY, ProductOption.CATEGORIES, ProductOption.REVIEW, ProductOption.PROMOTIONS,
						ProductOption.CLASSIFICATION, ProductOption.VARIANT_FULL, ProductOption.STOCK, ProductOption.VOLUME_PRICES,
						ProductOption.PRICE_RANGE, ProductOption.VARIANT_MATRIX));

		final AbstractPageModel configPage = getPageForProduct();

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Using CMS page: '" + configPage.getName() + "' [ '" + configPage.getUid() + "']");
		}
		storeCmsPageInModel(model, configPage);
		populateProductData(productData, model);
	}

	protected void populateProductData(final ProductData productData, final Model model)
	{
		model.addAttribute("galleryImages", getGalleryImages(productData));
		model.addAttribute(PRODUCT_ATTRIBUTE, productData);
	}

	protected List<Map<String, ImageData>> getGalleryImages(final ProductData productData)
	{
		final List<Map<String, ImageData>> galleryImages = new ArrayList<Map<String, ImageData>>();
		if (CollectionUtils.isNotEmpty(productData.getImages()))
		{
			final List<ImageData> images = new ArrayList<ImageData>();
			for (final ImageData image : productData.getImages())
			{
				if (ImageDataType.GALLERY.equals(image.getImageType()))
				{
					images.add(image);
				}
			}
			Collections.sort(images, new Comparator<ImageData>()
			{
				@Override
				public int compare(final ImageData image1, final ImageData image2)
				{
					return image1.getGalleryIndex().compareTo(image2.getGalleryIndex());
				}
			});

			if (CollectionUtils.isNotEmpty(images))
			{
				int currentIndex = images.get(0).getGalleryIndex().intValue();
				Map<String, ImageData> formats = new HashMap<String, ImageData>();
				for (final ImageData image : images)
				{
					if (currentIndex != image.getGalleryIndex().intValue())
					{
						galleryImages.add(formats);
						formats = new HashMap<String, ImageData>();
						currentIndex = image.getGalleryIndex().intValue();
					}
					formats.put(image.getFormat(), image);
				}
				if (!formats.isEmpty())
				{
					galleryImages.add(formats);
				}
			}
		}
		return galleryImages;
	}

	protected AbstractPageModel getPageForProduct() throws CMSItemNotFoundException
	{
		return getCmsPageService().getPageForId("productConfig");
	}

	protected KBKeyData createKBKeyForProduct(final ProductModel productModel)
	{
		final KBKeyData kbKey = new KBKeyData();
		kbKey.setProductCode(productModel.getCode());
		return kbKey;
	}

	protected void removeNullCsticsFromGroup(final List<CsticData> dirtyList)
	{
		if (dirtyList == null)
		{
			return;
		}

		final List<CsticData> cleanList = new ArrayList<>(dirtyList.size());

		for (final CsticData data : dirtyList)
		{
			if (data.getName() != null && (data.getType() != UiType.READ_ONLY || data.isRetractTriggered()))
			{
				cleanList.add(data);
			}
		}

		dirtyList.clear();
		dirtyList.addAll(cleanList);

	}

	protected void removeNullCstics(final List<UiGroupData> groups)
	{
		if (groups == null)
		{
			return;
		}

		for (final UiGroupData group : groups)
		{
			removeNullCsticsFromGroup(group.getCstics());

			final List<UiGroupData> subGroups = group.getSubGroups();
			removeNullCstics(subGroups);
		}
	}

	protected void resetGroupStatus(final ConfigurationData configData)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Reset group status for configuration data with  [CONFIG_ID: '" + configData.getConfigId() + "']");
		}

		final List<UiGroupData> uiGroups = configData.getGroups();
		if (uiGroups != null)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Reset group with subgroups for configuration data with  [CONFIG_ID: '" + configData.getConfigId() + "']");
			}

			resetGroupWithSubGroups(uiGroups);
		}
	}

	private void resetGroupWithSubGroups(final List<UiGroupData> uiGroups)
	{
		for (final UiGroupData group : uiGroups)
		{
			group.setGroupStatus(GroupStatusType.DEFAULT);
			if (group.getSubGroups() != null && !group.getSubGroups().isEmpty())
			{
				resetGroupWithSubGroups(group.getSubGroups());
			}
		}
	}


	protected void handleRequestContext(final HttpServletRequest request, final ProductModel productModel)
	{
		final RequestContextData requestContext = getRequestContextData(request);

		if (requestContext != null)
		{
			requestContext.setProduct(productModel);
		}
	}

	/**
	 * Fetch currently selected group from UI status
	 *
	 * @param uiStatus
	 * @return Selected group. Null, if ui status not present
	 */
	protected String getSelectedGroup(final UiStatus uiStatus)
	{
		String selectedGroup = null;
		if (uiStatus != null)
		{
			final List<UiGroupStatus> uiGroupsStatus = uiStatus.getGroups();
			for (final UiGroupStatus uiGroupStatus : uiGroupsStatus)
			{
				if (!uiGroupStatus.isCollapsed())
				{
					if (selectedGroup != null)
					{
						selectedGroup = null;
						break;
					}
					selectedGroup = uiGroupStatus.getId();
				}
			}
		}
		return selectedGroup;
	}


	protected void logModelmetaData(final ConfigurationData configData)
	{
		if (LOG.isDebugEnabled())
		{
			final UiDataStats numbers = new UiDataStats();
			numbers.countCstics(configData.getGroups());

			LOG.debug("Modelstats of product '" + configData.getKbKey().getProductCode() + "' after Update: '" + numbers + "'");
		}

	}

	protected void logRequestMetaData(final ConfigurationData configData, final HttpServletRequest request)
	{
		if (LOG.isDebugEnabled())
		{
			final NumberFormat decFormat = DecimalFormat.getInstance(Locale.ENGLISH);
			LOG.debug("Update Configuration of product '" + configData.getKbKey().getProductCode() + "'");
			LOG.debug("ContentLength=" + decFormat.format(request.getContentLength()) + " Bytes; numberParams="
					+ decFormat.format(request.getParameterMap().size()));

			final UiDataStats numbers = new UiDataStats();
			numbers.countCstics(configData.getGroups());

			LOG.debug(numbers);
		}
	}

	protected void updatePageTitle(final ProductModel productModel, final Model model)
	{
		storeContentPageTitleInModel(model, getPageTitleResolver().resolveProductPageTitle(productModel));
	}

	protected ConfigurationFacade getConfigFacade()
	{
		return configFacade;
	}

	protected ConfigurationCartIntegrationFacade getConfigCartFacade()
	{
		return configCartFacade;
	}

	protected SessionAccessFacade getSessionAccessFacade()
	{
		return sessionAccessFacade;
	}

	protected void setConfigCartFacade(final ConfigurationCartIntegrationFacade configCartFacade)
	{
		this.configCartFacade = configCartFacade;
	}

	protected void setConfigFacade(final ConfigurationFacade configFacade)
	{
		this.configFacade = configFacade;
	}

	protected Validator getProductConfigurationValidator()
	{
		return productConfigurationValidator;
	}

	protected void setProductConfigurationValidator(final Validator productConfigurationValidator)
	{
		this.productConfigurationValidator = productConfigurationValidator;
	}

	protected ConflictChecker getProductConfigurationConflictChecker()
	{
		return productConfigurationConflictChecker;
	}

	protected void setProductConfigurationConflictChecker(final ConflictChecker productConfigurationConflictChecker)
	{
		this.productConfigurationConflictChecker = productConfigurationConflictChecker;
	}

	protected ProductFacade getProductFacade()
	{
		return productFacade;
	}

	protected void setProductFacade(final ProductFacade productFacade)
	{
		this.productFacade = productFacade;
	}

	protected ProductService getProductService()
	{
		return productService;
	}

	protected void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	protected void expandGroupCloseOthers(final List<UiGroupData> list, final UiGroupData expandedGroup)
	{
		if (list != null && !list.isEmpty())
		{
			for (final UiGroupData uiGroup : list)
			{
				if (uiGroup == expandedGroup)
				{
					uiGroup.setCollapsed(false);
				}
				else
				{
					uiGroup.setCollapsed(true);
				}
			}
		}
		return;

	}

	protected UiGroupData expandFirstGroupWithError(final List<UiGroupData> list)
	{
		final BiPredicate<GroupType, GroupStatusType> csticGroupTypeCsticOnly = (groupType, groupStatus) -> (GroupType.CSTIC_GROUP
				.equals(groupType) && (GroupStatusType.ERROR.equals(groupStatus) || GroupStatusType.WARNING.equals(groupStatus) || GroupStatusType.CONFLICT
				.equals(groupStatus)));

		return expandFirstGroupWithCondition(list, csticGroupTypeCsticOnly);
	}

	protected UiGroupData expandFirstGroupWithErrorOrConflict(final List<UiGroupData> list)
	{

		final BiPredicate<GroupType, GroupStatusType> csticGroupTypeIncludeConflict = (groupType, groupStatus) -> (GroupType.CSTIC_GROUP
				.equals(groupType) || GroupType.CONFLICT.equals(groupType))
				&& (GroupStatusType.ERROR.equals(groupStatus) || GroupStatusType.WARNING.equals(groupStatus) || GroupStatusType.CONFLICT
						.equals(groupStatus));

		return expandFirstGroupWithCondition(list, csticGroupTypeIncludeConflict);
	}

	protected UiGroupData expandFirstGroupWithCondition(final List<UiGroupData> list,
			final BiPredicate<GroupType, GroupStatusType> condition)
	{
		UiGroupData expandedGroup = null;
		if (list != null && !list.isEmpty())
		{
			for (final UiGroupData uiGroup : list)
			{
				final GroupStatusType groupStatus = uiGroup.getGroupStatus();
				final GroupType groupType = uiGroup.getGroupType();
				final boolean isLeafWithError = condition.test(groupType, groupStatus);
				if (isLeafWithError)
				{
					expandedGroup = uiGroup;
				}
				else
				{
					expandedGroup = expandFirstGroupWithCondition(uiGroup.getSubGroups(), condition);
				}
				if (expandedGroup != null)
				{
					uiGroup.setCollapsed(false);
					uiGroup.setCollapsedInSpecificationTree(false);
					break;
				}
			}
		}
		return expandedGroup;

	}


	protected int countNumberOfUiErrorsPerGroup(final List<UiGroupData> uiGroups)
	{
		int allErrors = 0;
		if (uiGroups != null)
		{
			for (final UiGroupData uiGroup : uiGroups)
			{
				if (uiGroup.getGroupType() != null && !uiGroup.getGroupType().toString().contains(GroupType.CONFLICT.toString()))
				{
					int numberErrors = 0;
					if (uiGroup.getCstics() != null)
					{
						numberErrors = (int) uiGroup.getCstics().stream().filter(new CsticStatusErrorWarningPredicate()).count();
					}
					final int subErrors = countNumberOfUiErrorsPerGroup(uiGroup.getSubGroups());
					numberErrors += subErrors;

					uiGroup.setNumberErrorCstics(numberErrors);
					allErrors += numberErrors;
				}
			}
		}
		return allErrors;
	}

	protected BindingResult restoreValidationErrorsAfterUpdate(final Map<String, FieldError> userInputToRestore,
			final ConfigurationData latestConfiguration, BindingResult bindingResult)
	{
		if (!userInputToRestore.isEmpty())
		{
			//discard the old error binding and create a new one instead, that only contains errors for visible cstics
			bindingResult = new BeanPropertyBindingResult(latestConfiguration, Sapproductconfigb2baddonConstants.CONFIG_ATTRIBUTE);
			int groupNumber = 0;
			for (final UiGroupData group : latestConfiguration.getGroups())
			{
				final String prefix = "groups[" + groupNumber + "].";
				restoreValidationErrorsInGroup(prefix, userInputToRestore, bindingResult, group);
				groupNumber++;
			}
		}

		return bindingResult;
	}

	protected BindingResult restoreValidationErrorsOnGetConfig(final Map<String, FieldError> userInputToRestore,
			final ConfigurationData latestConfiguration, final BindingResult bindingResult)
	{
		if (!userInputToRestore.isEmpty())
		{
			int groupNumber = 0;
			for (final UiGroupData group : latestConfiguration.getGroups())
			{
				final String prefix = "groups[" + groupNumber + "].";
				restoreValidationErrorsInGroup(prefix, userInputToRestore, bindingResult, group);
				groupNumber++;
			}
		}

		return bindingResult;
	}

	protected void restoreValidationErrorsInGroup(final String prefix, final Map<String, FieldError> userInputToRestore,
			final BindingResult bindingResult, final UiGroupData group)
	{
		int csticNumber = 0;
		for (final CsticData latestCstic : group.getCstics())
		{
			final UiType uiType = latestCstic.getType();
			final String key = latestCstic.getKey();
			final boolean restoreValidationError = latestCstic.isVisible() && uiType != UiType.READ_ONLY
					&& userInputToRestore.containsKey(key);
			if (restoreValidationError)
			{
				final FieldError fieldError = userInputToRestore.get(key);

				latestCstic.getConflicts().clear();
				latestCstic.setCsticStatus(CsticStatusType.ERROR);
				final String errorValue = fieldError.getRejectedValue().toString();
				if (UiType.DROPDOWN_ADDITIONAL_INPUT == uiType || UiType.RADIO_BUTTON_ADDITIONAL_INPUT == uiType)
				{
					latestCstic.setAdditionalValue(errorValue);
				}
				else
				{
					latestCstic.setValue(errorValue);
				}
				final String path = prefix + "cstics[" + csticNumber + "].value";
				final FieldError newFieldError = new FieldError(fieldError.getObjectName(), path, fieldError.getRejectedValue(),
						fieldError.isBindingFailure(), fieldError.getCodes(), fieldError.getArguments(), fieldError.getDefaultMessage());
				bindingResult.addError(newFieldError);
				group.setGroupStatus(GroupStatusType.ERROR);
			}
			csticNumber++;
		}

		final List<UiGroupData> subGroups = group.getSubGroups();
		if (null == subGroups)
		{
			return;
		}

		int subGroupNumber = 0;
		for (final UiGroupData subGroup : subGroups)
		{
			final String subPrefix = prefix + "subGroups[" + subGroupNumber + "].";
			restoreValidationErrorsInGroup(subPrefix, userInputToRestore, bindingResult, subGroup);
			subGroupNumber++;
		}
	}

	protected void findCollapsedErrorCstics(final Map<String, FieldError> userInputToRestore,
			final Map<String, FieldError> userInputToRemeber, final UiGroupData group, boolean isRootCollapsed)
	{
		if (!isRootCollapsed)
		{
			isRootCollapsed = group.isCollapsed();
		}
		if (isRootCollapsed)
		{
			for (final CsticData cstic : group.getCstics())
			{
				final String key = cstic.getKey();
				if (userInputToRestore.containsKey(key))
				{
					userInputToRemeber.put(key, userInputToRestore.get(key));
				}
			}
		}

		if (group.getSubGroups() != null)
		{
			for (final UiGroupData subGroup : group.getSubGroups())
			{
				findCollapsedErrorCstics(userInputToRestore, userInputToRemeber, subGroup, isRootCollapsed);
			}
		}

	}

	protected Map<String, FieldError> findCollapsedErrorCstics(final Map<String, FieldError> userInputToRestore,
			final ConfigurationData latestConfiguration)
	{
		final Map<String, FieldError> userInputToRemeber;
		if (userInputToRestore == null || userInputToRestore.isEmpty())
		{
			userInputToRemeber = Collections.EMPTY_MAP;
		}
		else
		{
			userInputToRemeber = new HashMap<>();
			for (final UiGroupData group : latestConfiguration.getGroups())
			{
				findCollapsedErrorCstics(userInputToRestore, userInputToRemeber, group, false);
			}
		}
		return userInputToRemeber;

	}

	protected Map<String, FieldError> mergeUiErrors(final Map<String, FieldError> userInputToRestore,
			final Map<String, FieldError> oldUserInputToRember)
	{
		final Map<String, FieldError> mergedUiErrors = new HashMap<>();

		if (oldUserInputToRember != null)
		{
			mergedUiErrors.putAll(oldUserInputToRember);
		}

		if (userInputToRestore != null)
		{
			mergedUiErrors.putAll(userInputToRestore);
		}

		return mergedUiErrors;
	}

	class CsticStatusErrorWarningPredicate implements Predicate<CsticData>
	{
		@Override
		public boolean test(final CsticData csticData)
		{
			return CsticStatusType.ERROR.equals(csticData.getCsticStatus())
					|| CsticStatusType.WARNING.equals(csticData.getCsticStatus());
		}
	}

	class CsticStatusErrorWarningConflictPredicate implements Predicate<CsticData>
	{
		@Override
		public boolean test(final CsticData csticData)
		{
			return CsticStatusType.ERROR.equals(csticData.getCsticStatus())
					|| CsticStatusType.WARNING.equals(csticData.getCsticStatus())
					|| CsticStatusType.CONFLICT.equals(csticData.getCsticStatus());
		}
	}

	class GroupStatusErrorWarningPredicate implements Predicate<UiGroupData>
	{
		@Override
		public boolean test(final UiGroupData uiGroup)
		{
			return GroupStatusType.ERROR.equals(uiGroup.getGroupStatus())
					|| GroupStatusType.WARNING.equals(uiGroup.getGroupStatus());
		}
	}

	protected CsticData getFirstCsticWithErrorInGroup(final UiGroupData group)
	{
		Optional<CsticData> result = group.getCstics().stream().filter(new CsticStatusErrorWarningConflictPredicate()).findFirst();
		if (result.isPresent())
		{
			return result.get();
		}

		if (group.getSubGroups() != null)
		{
			result = group.getSubGroups().stream().filter(new GroupStatusErrorWarningPredicate())
					.map(subGroup -> getFirstCsticWithErrorInGroup(subGroup)).findFirst();
			if (result.isPresent())
			{
				return result.get();
			}
		}
		return null;
	}

	/**
	 * Fetch currently selected group from UI status
	 *
	 * @param uiStatus
	 * @return Selected group. Null, if ui status not present
	 */
	protected String collapsedAllGroups(final UiStatus uiStatus)
	{
		final String selectedGroup = null;
		if (uiStatus != null)
		{
			final List<UiGroupStatus> uiGroupsStatus = uiStatus.getGroups();
			for (final UiGroupStatus uiGroupStatus : uiGroupsStatus)
			{
				if (!uiGroupStatus.isCollapsed())
				{
					uiGroupStatus.setCollapsed(true);
				}
			}
		}
		return selectedGroup;
	}

	protected UiGroupData handleAutoExpand(final ConfigurationData configData)
	{
		UiGroupData expandedGroup = null;
		if (configData.isAutoExpand())
		{
			expandedGroup = expandFirstGroupWithErrorOrConflict(configData.getGroups());
			final boolean tabMode = configData.getStartLevel() > 0;
			if (expandedGroup != null && tabMode)
			{
				expandGroupCloseOthers(configData.getGroups(), expandedGroup);
			}
			if (expandedGroup == null)
			{
				// all ok - quit auto expand mode
				configData.setAutoExpand(false);
			}
			else
			{
				final CsticData errorCstic = getFirstCsticWithErrorInGroup(expandedGroup);
				if (errorCstic != null)
				{
					String errorCsticId = errorCstic.getKey();
					//cstic id for conflict groups is changed in javascript to "conflict." + id
					if (GroupType.CONFLICT.equals(expandedGroup.getGroupType()))
					{
						errorCsticId = "conflict." + errorCsticId;
					}
					configData.setFocusId(errorCsticId);

				}
			}

		}
		return expandedGroup;
	}
}
