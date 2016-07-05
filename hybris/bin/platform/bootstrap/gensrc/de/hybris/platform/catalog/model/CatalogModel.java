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
package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.AgreementModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.CompanyModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Generated model class for type Catalog first defined at extension catalog.
 */
@SuppressWarnings("all")
public class CatalogModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "Catalog";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.id</code> attribute defined at extension <code>catalog</code>. */
	public static final String ID = "id";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.name</code> attribute defined at extension <code>catalog</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.activeCatalogVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String ACTIVECATALOGVERSION = "activeCatalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.rootCategories</code> attribute defined at extension <code>catalog</code>. */
	public static final String ROOTCATEGORIES = "rootCategories";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.defaultCatalog</code> attribute defined at extension <code>catalog</code>. */
	public static final String DEFAULTCATALOG = "defaultCatalog";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.supplier</code> attribute defined at extension <code>catalog</code>. */
	public static final String SUPPLIER = "supplier";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.buyer</code> attribute defined at extension <code>catalog</code>. */
	public static final String BUYER = "buyer";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.version</code> attribute defined at extension <code>catalog</code>. */
	public static final String VERSION = "version";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.mimeRootDirectory</code> attribute defined at extension <code>catalog</code>. */
	public static final String MIMEROOTDIRECTORY = "mimeRootDirectory";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.generationDate</code> attribute defined at extension <code>catalog</code>. */
	public static final String GENERATIONDATE = "generationDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.defaultCurrency</code> attribute defined at extension <code>catalog</code>. */
	public static final String DEFAULTCURRENCY = "defaultCurrency";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.inclFreight</code> attribute defined at extension <code>catalog</code>. */
	public static final String INCLFREIGHT = "inclFreight";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.inclPacking</code> attribute defined at extension <code>catalog</code>. */
	public static final String INCLPACKING = "inclPacking";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.inclAssurance</code> attribute defined at extension <code>catalog</code>. */
	public static final String INCLASSURANCE = "inclAssurance";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.inclDuty</code> attribute defined at extension <code>catalog</code>. */
	public static final String INCLDUTY = "inclDuty";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.territories</code> attribute defined at extension <code>catalog</code>. */
	public static final String TERRITORIES = "territories";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.languages</code> attribute defined at extension <code>catalog</code>. */
	public static final String LANGUAGES = "languages";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.generatorInfo</code> attribute defined at extension <code>catalog</code>. */
	public static final String GENERATORINFO = "generatorInfo";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.agreements</code> attribute defined at extension <code>catalog</code>. */
	public static final String AGREEMENTS = "agreements";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.previewURLTemplate</code> attribute defined at extension <code>catalog</code>. */
	public static final String PREVIEWURLTEMPLATE = "previewURLTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.urlPatterns</code> attribute defined at extension <code>catalog</code>. */
	public static final String URLPATTERNS = "urlPatterns";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.catalogVersions</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATALOGVERSIONS = "catalogVersions";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CatalogModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CatalogModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>Catalog</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public CatalogModel(final String _id)
	{
		super();
		setId(_id);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>Catalog</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CatalogModel(final String _id, final ItemModel _owner)
	{
		super();
		setId(_id);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.activeCatalogVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the activeCatalogVersion - active CatalogVersion
	 */
	@Accessor(qualifier = "activeCatalogVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getActiveCatalogVersion()
	{
		return getPersistenceContext().getPropertyValue(ACTIVECATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.agreements</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the agreements - Agreement Collection
	 */
	@Accessor(qualifier = "agreements", type = Accessor.Type.GETTER)
	public Collection<AgreementModel> getAgreements()
	{
		return getPersistenceContext().getPropertyValue(AGREEMENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.buyer</code> attribute defined at extension <code>catalog</code>. 
	 * @return the buyer - Buyer
	 */
	@Accessor(qualifier = "buyer", type = Accessor.Type.GETTER)
	public CompanyModel getBuyer()
	{
		return getPersistenceContext().getPropertyValue(BUYER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.catalogVersions</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the catalogVersions
	 */
	@Accessor(qualifier = "catalogVersions", type = Accessor.Type.GETTER)
	public Set<CatalogVersionModel> getCatalogVersions()
	{
		return getPersistenceContext().getPropertyValue(CATALOGVERSIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.defaultCatalog</code> attribute defined at extension <code>catalog</code>. 
	 * @return the defaultCatalog - Default catalog
	 */
	@Accessor(qualifier = "defaultCatalog", type = Accessor.Type.GETTER)
	public Boolean getDefaultCatalog()
	{
		return getPersistenceContext().getPropertyValue(DEFAULTCATALOG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.defaultCurrency</code> attribute defined at extension <code>catalog</code>. 
	 * @return the defaultCurrency - Default Currency
	 */
	@Accessor(qualifier = "defaultCurrency", type = Accessor.Type.GETTER)
	public CurrencyModel getDefaultCurrency()
	{
		return getPersistenceContext().getPropertyValue(DEFAULTCURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.generationDate</code> attribute defined at extension <code>catalog</code>. 
	 * @return the generationDate - Generation Date
	 */
	@Accessor(qualifier = "generationDate", type = Accessor.Type.GETTER)
	public Date getGenerationDate()
	{
		return getPersistenceContext().getPropertyValue(GENERATIONDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.generatorInfo</code> attribute defined at extension <code>catalog</code>. 
	 * @return the generatorInfo - Generator Info
	 */
	@Accessor(qualifier = "generatorInfo", type = Accessor.Type.GETTER)
	public String getGeneratorInfo()
	{
		return getPersistenceContext().getPropertyValue(GENERATORINFO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.id</code> attribute defined at extension <code>catalog</code>. 
	 * @return the id - ID
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.GETTER)
	public String getId()
	{
		return getPersistenceContext().getPropertyValue(ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.inclAssurance</code> attribute defined at extension <code>catalog</code>. 
	 * @return the inclAssurance - incl.Assurance
	 */
	@Accessor(qualifier = "inclAssurance", type = Accessor.Type.GETTER)
	public Boolean getInclAssurance()
	{
		return getPersistenceContext().getPropertyValue(INCLASSURANCE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.inclDuty</code> attribute defined at extension <code>catalog</code>. 
	 * @return the inclDuty - incl.Duty
	 */
	@Accessor(qualifier = "inclDuty", type = Accessor.Type.GETTER)
	public Boolean getInclDuty()
	{
		return getPersistenceContext().getPropertyValue(INCLDUTY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.inclFreight</code> attribute defined at extension <code>catalog</code>. 
	 * @return the inclFreight - incl.Freight
	 */
	@Accessor(qualifier = "inclFreight", type = Accessor.Type.GETTER)
	public Boolean getInclFreight()
	{
		return getPersistenceContext().getPropertyValue(INCLFREIGHT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.inclPacking</code> attribute defined at extension <code>catalog</code>. 
	 * @return the inclPacking - incl.Packing
	 */
	@Accessor(qualifier = "inclPacking", type = Accessor.Type.GETTER)
	public Boolean getInclPacking()
	{
		return getPersistenceContext().getPropertyValue(INCLPACKING);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.languages</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the languages - Language
	 */
	@Accessor(qualifier = "languages", type = Accessor.Type.GETTER)
	public Collection<LanguageModel> getLanguages()
	{
		return getPersistenceContext().getPropertyValue(LANGUAGES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.mimeRootDirectory</code> attribute defined at extension <code>catalog</code>. 
	 * @return the mimeRootDirectory - Mime Root Directory
	 */
	@Accessor(qualifier = "mimeRootDirectory", type = Accessor.Type.GETTER)
	public String getMimeRootDirectory()
	{
		return getPersistenceContext().getPropertyValue(MIMEROOTDIRECTORY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.name</code> attribute defined at extension <code>catalog</code>. 
	 * @return the name - Name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.name</code> attribute defined at extension <code>catalog</code>. 
	 * @param loc the value localization key 
	 * @return the name - Name
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.previewURLTemplate</code> attribute defined at extension <code>catalog</code>. 
	 * @return the previewURLTemplate
	 */
	@Accessor(qualifier = "previewURLTemplate", type = Accessor.Type.GETTER)
	public String getPreviewURLTemplate()
	{
		return getPersistenceContext().getPropertyValue(PREVIEWURLTEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.rootCategories</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the rootCategories - The categories of the active CatalogVersion
	 */
	@Accessor(qualifier = "rootCategories", type = Accessor.Type.GETTER)
	public List<CategoryModel> getRootCategories()
	{
		return getPersistenceContext().getPropertyValue(ROOTCATEGORIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.supplier</code> attribute defined at extension <code>catalog</code>. 
	 * @return the supplier - Supplier
	 */
	@Accessor(qualifier = "supplier", type = Accessor.Type.GETTER)
	public CompanyModel getSupplier()
	{
		return getPersistenceContext().getPropertyValue(SUPPLIER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.territories</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the territories - Territory
	 */
	@Accessor(qualifier = "territories", type = Accessor.Type.GETTER)
	public Collection<CountryModel> getTerritories()
	{
		return getPersistenceContext().getPropertyValue(TERRITORIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.urlPatterns</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the urlPatterns - Collection of URL patterns
	 */
	@Accessor(qualifier = "urlPatterns", type = Accessor.Type.GETTER)
	public Collection<String> getUrlPatterns()
	{
		return getPersistenceContext().getPropertyValue(URLPATTERNS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.version</code> attribute defined at extension <code>catalog</code>. 
	 * @return the version - version
	 */
	@Accessor(qualifier = "version", type = Accessor.Type.GETTER)
	public String getVersion()
	{
		return getPersistenceContext().getPropertyValue(VERSION);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Catalog.activeCatalogVersion</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the activeCatalogVersion - active CatalogVersion
	 */
	@Accessor(qualifier = "activeCatalogVersion", type = Accessor.Type.SETTER)
	public void setActiveCatalogVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(ACTIVECATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Catalog.buyer</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the buyer - Buyer
	 */
	@Accessor(qualifier = "buyer", type = Accessor.Type.SETTER)
	public void setBuyer(final CompanyModel value)
	{
		getPersistenceContext().setPropertyValue(BUYER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Catalog.catalogVersions</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the catalogVersions
	 */
	@Accessor(qualifier = "catalogVersions", type = Accessor.Type.SETTER)
	public void setCatalogVersions(final Set<CatalogVersionModel> value)
	{
		getPersistenceContext().setPropertyValue(CATALOGVERSIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Catalog.defaultCatalog</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the defaultCatalog - Default catalog
	 */
	@Accessor(qualifier = "defaultCatalog", type = Accessor.Type.SETTER)
	public void setDefaultCatalog(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(DEFAULTCATALOG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Catalog.id</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the id - ID
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.SETTER)
	public void setId(final String value)
	{
		getPersistenceContext().setPropertyValue(ID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Catalog.name</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the name - Name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Catalog.name</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the name - Name
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Catalog.previewURLTemplate</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the previewURLTemplate
	 */
	@Accessor(qualifier = "previewURLTemplate", type = Accessor.Type.SETTER)
	public void setPreviewURLTemplate(final String value)
	{
		getPersistenceContext().setPropertyValue(PREVIEWURLTEMPLATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Catalog.supplier</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the supplier - Supplier
	 */
	@Accessor(qualifier = "supplier", type = Accessor.Type.SETTER)
	public void setSupplier(final CompanyModel value)
	{
		getPersistenceContext().setPropertyValue(SUPPLIER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Catalog.urlPatterns</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the urlPatterns - Collection of URL patterns
	 */
	@Accessor(qualifier = "urlPatterns", type = Accessor.Type.SETTER)
	public void setUrlPatterns(final Collection<String> value)
	{
		getPersistenceContext().setPropertyValue(URLPATTERNS, value);
	}
	
}