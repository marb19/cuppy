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
package de.hybris.platform.cmswebservices.sites.facade.populator.model;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cmswebservices.catalogversiondetails.facade.populator.model.CatalogVersionDataPopulatorTest;
import de.hybris.platform.servicelayer.model.ItemModelInternalContext;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static de.hybris.platform.basecommerce.model.site.BaseSiteModel.NAME;
import static de.hybris.platform.basecommerce.model.site.BaseSiteModel.UID;
import static de.hybris.platform.catalog.model.CatalogVersionModel.VERSION;
import static de.hybris.platform.cms2.model.site.CMSSiteModel.*;
import static de.hybris.platform.cmswebservices.catalogversiondetails.facade.populator.model.CatalogVersionDataPopulatorTest.CATALOG_VERSION;
import static de.hybris.platform.cmswebservices.util.models.CMSSiteModelMother.TemplateSite.APPAREL;
import static de.hybris.platform.cmswebservices.util.models.ContentCatalogModelMother.CatalogTemplate.ID_ONLINE;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public final class MockedCMSSiteModelBuilder {

    public static final String PREVIEW_URL = "http://preview.url";

    public static CMSSiteModel buildSite(ItemModelInternalContext mockContext) {
        final CMSSiteModel model = new CMSSiteModel(mockContext);
        when(mockContext.getLocalizedValue(eq(NAME), any(Locale.class))).thenReturn(APPAREL.getName());
        when(mockContext.getPropertyValue(eq(UID))).thenReturn(APPAREL.getUid());
        when(mockContext.getPropertyValue(eq(CatalogModel.ID))).thenReturn(CatalogVersionDataPopulatorTest.CATALOG_MODEL_ID);
        when(mockContext.getPropertyValue(eq(REDIRECTURL))).thenReturn(APPAREL.getBaseUrl());
        when(mockContext.getPropertyValue(eq(PREVIEWURL))).thenReturn(PREVIEW_URL);
        when(mockContext.getPropertyValue(eq(CatalogModel.CATALOGVERSIONS))).thenReturn(buildCatalogVersion());
        when(mockContext.getPropertyValue(eq(CONTENTCATALOGS))).thenReturn(buildCatalog(mockContext));
        when(mockContext.getPropertyValue(eq(VERSION))).thenReturn(ID_ONLINE.name());
        return model;
    }

    private static Set<CatalogVersionModel> buildCatalogVersion() {
        return newHashSet(new InternalCatalogVersionModelMock());
    }

    public static List<ContentCatalogModel> buildCatalog(ItemModelInternalContext mockContext) {
        ContentCatalogModel contentCatalogModel = new ContentCatalogModel(mockContext);

        return newArrayList(contentCatalogModel);
    }

    private static class InternalCatalogVersionModelMock extends CatalogVersionModel {
        @Override
        public String getVersion() {
            return CATALOG_VERSION;
        }
    }
}
