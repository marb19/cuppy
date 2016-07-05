/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Jul 5, 2016 1:30:21 PM                      ---
 * ----------------------------------------------------------------
 *  
 * [y] hybris Platform
 *  
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *  
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 *  
 */
package de.hybris.platform.core.model.user;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.model.CockpitFavoriteCategoryModel;
import de.hybris.platform.cockpit.model.CockpitObjectAbstractCollectionModel;
import de.hybris.platform.cockpit.model.CockpitSavedQueryModel;
import de.hybris.platform.cockpit.model.WidgetPreferencesModel;
import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.AbstractContactInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.europe1.enums.UserDiscountGroup;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.europe1.enums.UserTaxGroup;
import de.hybris.platform.europe1.model.GlobalDiscountRowModel;
import de.hybris.platform.hmc.model.UserProfileModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Generated model class for type User first defined at extension core.
 */
@SuppressWarnings("all")
public class UserModel extends PrincipalModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "User";
	
	/**<i>Generated relation code constant for relation <code>AbstractCommentAuthorRelation</code> defining source attribute <code>createdComments</code> in extension <code>comments</code>.</i>*/
	public final static String _ABSTRACTCOMMENTAUTHORRELATION = "AbstractCommentAuthorRelation";
	
	/**<i>Generated relation code constant for relation <code>CommentUserSettingUserRelation</code> defining source attribute <code>commentUserSettings</code> in extension <code>comments</code>.</i>*/
	public final static String _COMMENTUSERSETTINGUSERRELATION = "CommentUserSettingUserRelation";
	
	/**<i>Generated relation code constant for relation <code>CommentAssigneeRelation</code> defining source attribute <code>assignedComments</code> in extension <code>comments</code>.</i>*/
	public final static String _COMMENTASSIGNEERELATION = "CommentAssigneeRelation";
	
	/**<i>Generated relation code constant for relation <code>WidgetPreferencesToUserRelation</code> defining source attribute <code>widgetPreferences</code> in extension <code>cockpit</code>.</i>*/
	public final static String _WIDGETPREFERENCESTOUSERRELATION = "WidgetPreferencesToUserRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.currentTime</code> attribute defined at extension <code>core</code>. */
	public static final String CURRENTTIME = "currentTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.currentDate</code> attribute defined at extension <code>core</code>. */
	public static final String CURRENTDATE = "currentDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.addresses</code> attribute defined at extension <code>core</code>. */
	public static final String ADDRESSES = "addresses";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.carts</code> attribute defined at extension <code>core</code>. */
	public static final String CARTS = "carts";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.defaultPaymentAddress</code> attribute defined at extension <code>core</code>. */
	public static final String DEFAULTPAYMENTADDRESS = "defaultPaymentAddress";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.defaultShipmentAddress</code> attribute defined at extension <code>core</code>. */
	public static final String DEFAULTSHIPMENTADDRESS = "defaultShipmentAddress";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.orders</code> attribute defined at extension <code>core</code>. */
	public static final String ORDERS = "orders";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.passwordEncoding</code> attribute defined at extension <code>core</code>. */
	public static final String PASSWORDENCODING = "passwordEncoding";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.encodedPassword</code> attribute defined at extension <code>core</code>. */
	public static final String ENCODEDPASSWORD = "encodedPassword";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.passwordAnswer</code> attribute defined at extension <code>core</code>. */
	public static final String PASSWORDANSWER = "passwordAnswer";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.passwordQuestion</code> attribute defined at extension <code>core</code>. */
	public static final String PASSWORDQUESTION = "passwordQuestion";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.paymentInfos</code> attribute defined at extension <code>core</code>. */
	public static final String PAYMENTINFOS = "paymentInfos";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.sessionLanguage</code> attribute defined at extension <code>core</code>. */
	public static final String SESSIONLANGUAGE = "sessionLanguage";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.sessionCurrency</code> attribute defined at extension <code>core</code>. */
	public static final String SESSIONCURRENCY = "sessionCurrency";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.loginDisabled</code> attribute defined at extension <code>core</code>. */
	public static final String LOGINDISABLED = "loginDisabled";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.lastLogin</code> attribute defined at extension <code>core</code>. */
	public static final String LASTLOGIN = "lastLogin";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.hmcLoginDisabled</code> attribute defined at extension <code>core</code>. */
	public static final String HMCLOGINDISABLED = "hmcLoginDisabled";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.userprofile</code> attribute defined at extension <code>core</code>. */
	public static final String USERPROFILE = "userprofile";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.contactInfos</code> attribute defined at extension <code>core</code>. */
	public static final String CONTACTINFOS = "contactInfos";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.allWriteableCatalogVersions</code> attribute defined at extension <code>catalog</code>. */
	public static final String ALLWRITEABLECATALOGVERSIONS = "allWriteableCatalogVersions";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.Europe1PriceFactory_UDG</code> attribute defined at extension <code>europe1</code>. */
	public static final String EUROPE1PRICEFACTORY_UDG = "Europe1PriceFactory_UDG";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.Europe1PriceFactory_UPG</code> attribute defined at extension <code>europe1</code>. */
	public static final String EUROPE1PRICEFACTORY_UPG = "Europe1PriceFactory_UPG";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.Europe1PriceFactory_UTG</code> attribute defined at extension <code>europe1</code>. */
	public static final String EUROPE1PRICEFACTORY_UTG = "Europe1PriceFactory_UTG";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.europe1Discounts</code> attribute defined at extension <code>europe1</code>. */
	public static final String EUROPE1DISCOUNTS = "europe1Discounts";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.createdComments</code> attribute defined at extension <code>comments</code>. */
	public static final String CREATEDCOMMENTS = "createdComments";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.commentUserSettings</code> attribute defined at extension <code>comments</code>. */
	public static final String COMMENTUSERSETTINGS = "commentUserSettings";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.assignedComments</code> attribute defined at extension <code>comments</code>. */
	public static final String ASSIGNEDCOMMENTS = "assignedComments";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.ldapaccount</code> attribute defined at extension <code>ldap</code>. */
	public static final String LDAPACCOUNT = "ldapaccount";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.domain</code> attribute defined at extension <code>ldap</code>. */
	public static final String DOMAIN = "domain";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.ldaplogin</code> attribute defined at extension <code>ldap</code>. */
	public static final String LDAPLOGIN = "ldaplogin";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.collections</code> attribute defined at extension <code>cockpit</code>. */
	public static final String COLLECTIONS = "collections";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.cockpitSavedQueries</code> attribute defined at extension <code>cockpit</code>. */
	public static final String COCKPITSAVEDQUERIES = "cockpitSavedQueries";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.cockpitFavoriteCategories</code> attribute defined at extension <code>cockpit</code>. */
	public static final String COCKPITFAVORITECATEGORIES = "cockpitFavoriteCategories";
	
	/** <i>Generated constant</i> - Attribute key of <code>User.widgetPreferences</code> attribute defined at extension <code>cockpit</code>. */
	public static final String WIDGETPREFERENCES = "widgetPreferences";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public UserModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public UserModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _loginDisabled initial attribute declared by type <code>User</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>Principal</code> at extension <code>core</code>
	 */
	@Deprecated
	public UserModel(final boolean _loginDisabled, final String _uid)
	{
		super();
		setLoginDisabled(_loginDisabled);
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _loginDisabled initial attribute declared by type <code>User</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>Principal</code> at extension <code>core</code>
	 */
	@Deprecated
	public UserModel(final boolean _loginDisabled, final ItemModel _owner, final String _uid)
	{
		super();
		setLoginDisabled(_loginDisabled);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.addresses</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the addresses
	 */
	@Accessor(qualifier = "addresses", type = Accessor.Type.GETTER)
	public Collection<AddressModel> getAddresses()
	{
		return getPersistenceContext().getPropertyValue(ADDRESSES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.allWriteableCatalogVersions</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the allWriteableCatalogVersions
	 */
	@Accessor(qualifier = "allWriteableCatalogVersions", type = Accessor.Type.GETTER)
	public Collection<CatalogVersionModel> getAllWriteableCatalogVersions()
	{
		return getPersistenceContext().getPropertyValue(ALLWRITEABLECATALOGVERSIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.assignedComments</code> attribute defined at extension <code>comments</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the assignedComments
	 */
	@Accessor(qualifier = "assignedComments", type = Accessor.Type.GETTER)
	public List<CommentModel> getAssignedComments()
	{
		return getPersistenceContext().getPropertyValue(ASSIGNEDCOMMENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.carts</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the carts
	 */
	@Accessor(qualifier = "carts", type = Accessor.Type.GETTER)
	public Collection<CartModel> getCarts()
	{
		return getPersistenceContext().getPropertyValue(CARTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.cockpitFavoriteCategories</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the cockpitFavoriteCategories
	 */
	@Accessor(qualifier = "cockpitFavoriteCategories", type = Accessor.Type.GETTER)
	public Collection<CockpitFavoriteCategoryModel> getCockpitFavoriteCategories()
	{
		return getPersistenceContext().getPropertyValue(COCKPITFAVORITECATEGORIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.cockpitSavedQueries</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the cockpitSavedQueries
	 */
	@Accessor(qualifier = "cockpitSavedQueries", type = Accessor.Type.GETTER)
	public Collection<CockpitSavedQueryModel> getCockpitSavedQueries()
	{
		return getPersistenceContext().getPropertyValue(COCKPITSAVEDQUERIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.collections</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the collections
	 */
	@Accessor(qualifier = "collections", type = Accessor.Type.GETTER)
	public Collection<CockpitObjectAbstractCollectionModel> getCollections()
	{
		return getPersistenceContext().getPropertyValue(COLLECTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.contactInfos</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the contactInfos
	 */
	@Accessor(qualifier = "contactInfos", type = Accessor.Type.GETTER)
	public Collection<AbstractContactInfoModel> getContactInfos()
	{
		return getPersistenceContext().getPropertyValue(CONTACTINFOS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.createdComments</code> attribute defined at extension <code>comments</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the createdComments
	 */
	@Accessor(qualifier = "createdComments", type = Accessor.Type.GETTER)
	public List<AbstractCommentModel> getCreatedComments()
	{
		return getPersistenceContext().getPropertyValue(CREATEDCOMMENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.currentDate</code> attribute defined at extension <code>core</code>. 
	 * @return the currentDate
	 */
	@Accessor(qualifier = "currentDate", type = Accessor.Type.GETTER)
	public Date getCurrentDate()
	{
		return getPersistenceContext().getPropertyValue(CURRENTDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.currentTime</code> attribute defined at extension <code>core</code>. 
	 * @return the currentTime
	 */
	@Accessor(qualifier = "currentTime", type = Accessor.Type.GETTER)
	public Date getCurrentTime()
	{
		return getPersistenceContext().getPropertyValue(CURRENTTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.defaultPaymentAddress</code> attribute defined at extension <code>core</code>. 
	 * @return the defaultPaymentAddress
	 */
	@Accessor(qualifier = "defaultPaymentAddress", type = Accessor.Type.GETTER)
	public AddressModel getDefaultPaymentAddress()
	{
		return getPersistenceContext().getPropertyValue(DEFAULTPAYMENTADDRESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.defaultShipmentAddress</code> attribute defined at extension <code>core</code>. 
	 * @return the defaultShipmentAddress
	 */
	@Accessor(qualifier = "defaultShipmentAddress", type = Accessor.Type.GETTER)
	public AddressModel getDefaultShipmentAddress()
	{
		return getPersistenceContext().getPropertyValue(DEFAULTSHIPMENTADDRESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.domain</code> attribute defined at extension <code>ldap</code>. 
	 * @return the domain
	 */
	@Accessor(qualifier = "domain", type = Accessor.Type.GETTER)
	public String getDomain()
	{
		return getPersistenceContext().getPropertyValue(DOMAIN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.encodedPassword</code> attribute defined at extension <code>core</code>. 
	 * @return the encodedPassword
	 */
	@Accessor(qualifier = "encodedPassword", type = Accessor.Type.GETTER)
	public String getEncodedPassword()
	{
		return getPersistenceContext().getPropertyValue(ENCODEDPASSWORD);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.europe1Discounts</code> attribute defined at extension <code>europe1</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the europe1Discounts
	 */
	@Accessor(qualifier = "europe1Discounts", type = Accessor.Type.GETTER)
	public Collection<GlobalDiscountRowModel> getEurope1Discounts()
	{
		return getPersistenceContext().getPropertyValue(EUROPE1DISCOUNTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.Europe1PriceFactory_UDG</code> attribute defined at extension <code>europe1</code>. 
	 * @return the Europe1PriceFactory_UDG
	 */
	@Accessor(qualifier = "Europe1PriceFactory_UDG", type = Accessor.Type.GETTER)
	public UserDiscountGroup getEurope1PriceFactory_UDG()
	{
		return getPersistenceContext().getPropertyValue(EUROPE1PRICEFACTORY_UDG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.Europe1PriceFactory_UPG</code> attribute defined at extension <code>europe1</code>. 
	 * @return the Europe1PriceFactory_UPG
	 */
	@Accessor(qualifier = "Europe1PriceFactory_UPG", type = Accessor.Type.GETTER)
	public UserPriceGroup getEurope1PriceFactory_UPG()
	{
		return getPersistenceContext().getPropertyValue(EUROPE1PRICEFACTORY_UPG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.Europe1PriceFactory_UTG</code> attribute defined at extension <code>europe1</code>. 
	 * @return the Europe1PriceFactory_UTG
	 */
	@Accessor(qualifier = "Europe1PriceFactory_UTG", type = Accessor.Type.GETTER)
	public UserTaxGroup getEurope1PriceFactory_UTG()
	{
		return getPersistenceContext().getPropertyValue(EUROPE1PRICEFACTORY_UTG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.hmcLoginDisabled</code> attribute defined at extension <code>core</code>. 
	 * @return the hmcLoginDisabled
	 */
	@Accessor(qualifier = "hmcLoginDisabled", type = Accessor.Type.GETTER)
	public Boolean getHmcLoginDisabled()
	{
		return getPersistenceContext().getPropertyValue(HMCLOGINDISABLED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.lastLogin</code> attribute defined at extension <code>core</code>. 
	 * @return the lastLogin
	 */
	@Accessor(qualifier = "lastLogin", type = Accessor.Type.GETTER)
	public Date getLastLogin()
	{
		return getPersistenceContext().getPropertyValue(LASTLOGIN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.ldapaccount</code> attribute defined at extension <code>ldap</code>. 
	 * @return the ldapaccount
	 */
	@Accessor(qualifier = "ldapaccount", type = Accessor.Type.GETTER)
	public Boolean getLdapaccount()
	{
		return getPersistenceContext().getPropertyValue(LDAPACCOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.ldaplogin</code> attribute defined at extension <code>ldap</code>. 
	 * @return the ldaplogin
	 */
	@Accessor(qualifier = "ldaplogin", type = Accessor.Type.GETTER)
	public String getLdaplogin()
	{
		return getPersistenceContext().getPropertyValue(LDAPLOGIN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.orders</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the orders
	 */
	@Accessor(qualifier = "orders", type = Accessor.Type.GETTER)
	public Collection<OrderModel> getOrders()
	{
		return getPersistenceContext().getPropertyValue(ORDERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.passwordAnswer</code> attribute defined at extension <code>core</code>. 
	 * @return the passwordAnswer
	 */
	@Accessor(qualifier = "passwordAnswer", type = Accessor.Type.GETTER)
	public String getPasswordAnswer()
	{
		return getPersistenceContext().getPropertyValue(PASSWORDANSWER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.passwordEncoding</code> attribute defined at extension <code>core</code>. 
	 * @return the passwordEncoding
	 */
	@Accessor(qualifier = "passwordEncoding", type = Accessor.Type.GETTER)
	public String getPasswordEncoding()
	{
		return getPersistenceContext().getPropertyValue(PASSWORDENCODING);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.passwordQuestion</code> attribute defined at extension <code>core</code>. 
	 * @return the passwordQuestion
	 */
	@Accessor(qualifier = "passwordQuestion", type = Accessor.Type.GETTER)
	public String getPasswordQuestion()
	{
		return getPersistenceContext().getPropertyValue(PASSWORDQUESTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.paymentInfos</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the paymentInfos
	 */
	@Accessor(qualifier = "paymentInfos", type = Accessor.Type.GETTER)
	public Collection<PaymentInfoModel> getPaymentInfos()
	{
		return getPersistenceContext().getPropertyValue(PAYMENTINFOS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.sessionCurrency</code> attribute defined at extension <code>core</code>. 
	 * @return the sessionCurrency
	 */
	@Accessor(qualifier = "sessionCurrency", type = Accessor.Type.GETTER)
	public CurrencyModel getSessionCurrency()
	{
		return getPersistenceContext().getPropertyValue(SESSIONCURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.sessionLanguage</code> attribute defined at extension <code>core</code>. 
	 * @return the sessionLanguage
	 */
	@Accessor(qualifier = "sessionLanguage", type = Accessor.Type.GETTER)
	public LanguageModel getSessionLanguage()
	{
		return getPersistenceContext().getPropertyValue(SESSIONLANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.userprofile</code> attribute defined at extension <code>core</code>. 
	 * @return the userprofile
	 */
	@Accessor(qualifier = "userprofile", type = Accessor.Type.GETTER)
	public UserProfileModel getUserprofile()
	{
		return getPersistenceContext().getPropertyValue(USERPROFILE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.widgetPreferences</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the widgetPreferences
	 */
	@Accessor(qualifier = "widgetPreferences", type = Accessor.Type.GETTER)
	public Collection<WidgetPreferencesModel> getWidgetPreferences()
	{
		return getPersistenceContext().getPropertyValue(WIDGETPREFERENCES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.loginDisabled</code> attribute defined at extension <code>core</code>. 
	 * @return the loginDisabled - Determines whether user is allowed to login to system.
	 */
	@Accessor(qualifier = "loginDisabled", type = Accessor.Type.GETTER)
	public boolean isLoginDisabled()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(LOGINDISABLED));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>User.addresses</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the addresses
	 */
	@Accessor(qualifier = "addresses", type = Accessor.Type.SETTER)
	public void setAddresses(final Collection<AddressModel> value)
	{
		getPersistenceContext().setPropertyValue(ADDRESSES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>User.assignedComments</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the assignedComments
	 */
	@Accessor(qualifier = "assignedComments", type = Accessor.Type.SETTER)
	public void setAssignedComments(final List<CommentModel> value)
	{
		getPersistenceContext().setPropertyValue(ASSIGNEDCOMMENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>User.carts</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the carts
	 */
	@Accessor(qualifier = "carts", type = Accessor.Type.SETTER)
	public void setCarts(final Collection<CartModel> value)
	{
		getPersistenceContext().setPropertyValue(CARTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>User.cockpitFavoriteCategories</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the cockpitFavoriteCategories
	 */
	@Accessor(qualifier = "cockpitFavoriteCategories", type = Accessor.Type.SETTER)
	public void setCockpitFavoriteCategories(final Collection<CockpitFavoriteCategoryModel> value)
	{
		getPersistenceContext().setPropertyValue(COCKPITFAVORITECATEGORIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>User.cockpitSavedQueries</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the cockpitSavedQueries
	 */
	@Accessor(qualifier = "cockpitSavedQueries", type = Accessor.Type.SETTER)
	public void setCockpitSavedQueries(final Collection<CockpitSavedQueryModel> value)
	{
		getPersistenceContext().setPropertyValue(COCKPITSAVEDQUERIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>User.collections</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the collections
	 */
	@Accessor(qualifier = "collections", type = Accessor.Type.SETTER)
	public void setCollections(final Collection<CockpitObjectAbstractCollectionModel> value)
	{
		getPersistenceContext().setPropertyValue(COLLECTIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>User.contactInfos</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the contactInfos
	 */
	@Accessor(qualifier = "contactInfos", type = Accessor.Type.SETTER)
	public void setContactInfos(final Collection<AbstractContactInfoModel> value)
	{
		getPersistenceContext().setPropertyValue(CONTACTINFOS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>User.createdComments</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the createdComments
	 */
	@Accessor(qualifier = "createdComments", type = Accessor.Type.SETTER)
	public void setCreatedComments(final List<AbstractCommentModel> value)
	{
		getPersistenceContext().setPropertyValue(CREATEDCOMMENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>User.defaultPaymentAddress</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the defaultPaymentAddress
	 */
	@Accessor(qualifier = "defaultPaymentAddress", type = Accessor.Type.SETTER)
	public void setDefaultPaymentAddress(final AddressModel value)
	{
		getPersistenceContext().setPropertyValue(DEFAULTPAYMENTADDRESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>User.defaultShipmentAddress</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the defaultShipmentAddress
	 */
	@Accessor(qualifier = "defaultShipmentAddress", type = Accessor.Type.SETTER)
	public void setDefaultShipmentAddress(final AddressModel value)
	{
		getPersistenceContext().setPropertyValue(DEFAULTSHIPMENTADDRESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>User.domain</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the domain
	 */
	@Accessor(qualifier = "domain", type = Accessor.Type.SETTER)
	public void setDomain(final String value)
	{
		getPersistenceContext().setPropertyValue(DOMAIN, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>User.encodedPassword</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the encodedPassword
	 */
	@Accessor(qualifier = "encodedPassword", type = Accessor.Type.SETTER)
	public void setEncodedPassword(final String value)
	{
		getPersistenceContext().setPropertyValue(ENCODEDPASSWORD, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>User.europe1Discounts</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the europe1Discounts
	 */
	@Accessor(qualifier = "europe1Discounts", type = Accessor.Type.SETTER)
	public void setEurope1Discounts(final Collection<GlobalDiscountRowModel> value)
	{
		getPersistenceContext().setPropertyValue(EUROPE1DISCOUNTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>User.Europe1PriceFactory_UDG</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the Europe1PriceFactory_UDG
	 */
	@Accessor(qualifier = "Europe1PriceFactory_UDG", type = Accessor.Type.SETTER)
	public void setEurope1PriceFactory_UDG(final UserDiscountGroup value)
	{
		getPersistenceContext().setPropertyValue(EUROPE1PRICEFACTORY_UDG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>User.Europe1PriceFactory_UPG</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the Europe1PriceFactory_UPG
	 */
	@Accessor(qualifier = "Europe1PriceFactory_UPG", type = Accessor.Type.SETTER)
	public void setEurope1PriceFactory_UPG(final UserPriceGroup value)
	{
		getPersistenceContext().setPropertyValue(EUROPE1PRICEFACTORY_UPG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>User.Europe1PriceFactory_UTG</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the Europe1PriceFactory_UTG
	 */
	@Accessor(qualifier = "Europe1PriceFactory_UTG", type = Accessor.Type.SETTER)
	public void setEurope1PriceFactory_UTG(final UserTaxGroup value)
	{
		getPersistenceContext().setPropertyValue(EUROPE1PRICEFACTORY_UTG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>User.hmcLoginDisabled</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the hmcLoginDisabled
	 */
	@Accessor(qualifier = "hmcLoginDisabled", type = Accessor.Type.SETTER)
	public void setHmcLoginDisabled(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(HMCLOGINDISABLED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>User.lastLogin</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the lastLogin
	 */
	@Accessor(qualifier = "lastLogin", type = Accessor.Type.SETTER)
	public void setLastLogin(final Date value)
	{
		getPersistenceContext().setPropertyValue(LASTLOGIN, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>User.ldapaccount</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the ldapaccount
	 */
	@Accessor(qualifier = "ldapaccount", type = Accessor.Type.SETTER)
	public void setLdapaccount(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(LDAPACCOUNT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>User.ldaplogin</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the ldaplogin
	 */
	@Accessor(qualifier = "ldaplogin", type = Accessor.Type.SETTER)
	public void setLdaplogin(final String value)
	{
		getPersistenceContext().setPropertyValue(LDAPLOGIN, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>User.loginDisabled</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the loginDisabled - Determines whether user is allowed to login to system.
	 */
	@Accessor(qualifier = "loginDisabled", type = Accessor.Type.SETTER)
	public void setLoginDisabled(final boolean value)
	{
		getPersistenceContext().setPropertyValue(LOGINDISABLED, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>User.orders</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the orders
	 */
	@Accessor(qualifier = "orders", type = Accessor.Type.SETTER)
	public void setOrders(final Collection<OrderModel> value)
	{
		getPersistenceContext().setPropertyValue(ORDERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>User.passwordAnswer</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the passwordAnswer
	 */
	@Accessor(qualifier = "passwordAnswer", type = Accessor.Type.SETTER)
	public void setPasswordAnswer(final String value)
	{
		getPersistenceContext().setPropertyValue(PASSWORDANSWER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>User.passwordEncoding</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the passwordEncoding
	 */
	@Accessor(qualifier = "passwordEncoding", type = Accessor.Type.SETTER)
	public void setPasswordEncoding(final String value)
	{
		getPersistenceContext().setPropertyValue(PASSWORDENCODING, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>User.passwordQuestion</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the passwordQuestion
	 */
	@Accessor(qualifier = "passwordQuestion", type = Accessor.Type.SETTER)
	public void setPasswordQuestion(final String value)
	{
		getPersistenceContext().setPropertyValue(PASSWORDQUESTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>User.paymentInfos</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the paymentInfos
	 */
	@Accessor(qualifier = "paymentInfos", type = Accessor.Type.SETTER)
	public void setPaymentInfos(final Collection<PaymentInfoModel> value)
	{
		getPersistenceContext().setPropertyValue(PAYMENTINFOS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>User.sessionCurrency</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the sessionCurrency
	 */
	@Accessor(qualifier = "sessionCurrency", type = Accessor.Type.SETTER)
	public void setSessionCurrency(final CurrencyModel value)
	{
		getPersistenceContext().setPropertyValue(SESSIONCURRENCY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>User.sessionLanguage</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the sessionLanguage
	 */
	@Accessor(qualifier = "sessionLanguage", type = Accessor.Type.SETTER)
	public void setSessionLanguage(final LanguageModel value)
	{
		getPersistenceContext().setPropertyValue(SESSIONLANGUAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>User.userprofile</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the userprofile
	 */
	@Accessor(qualifier = "userprofile", type = Accessor.Type.SETTER)
	public void setUserprofile(final UserProfileModel value)
	{
		getPersistenceContext().setPropertyValue(USERPROFILE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>User.widgetPreferences</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the widgetPreferences
	 */
	@Accessor(qualifier = "widgetPreferences", type = Accessor.Type.SETTER)
	public void setWidgetPreferences(final Collection<WidgetPreferencesModel> value)
	{
		getPersistenceContext().setPropertyValue(WIDGETPREFERENCES, value);
	}
	
}