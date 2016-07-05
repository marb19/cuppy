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
package de.hybris.platform.cmswebservices.catalogversiondetails.facade.populator.model;

import static com.google.common.collect.Lists.newArrayList;
import static de.hybris.platform.cmswebservices.sites.facade.populator.model.MockedCMSSiteModelBuilder.buildSite;
import static de.hybris.platform.cmswebservices.util.models.CMSSiteModelMother.TemplateSite.APPAREL;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmswebservices.catalogversiondetails.facade.CatalogVersionDetailDataCatalogIdComparator;
import de.hybris.platform.cmswebservices.data.CatalogVersionData;
import de.hybris.platform.cmswebservices.data.CatalogVersionDetailData;
import de.hybris.platform.cmswebservices.localization.data.LocalizedValueMap;
import de.hybris.platform.cmswebservices.localization.service.LocalizationService;
import de.hybris.platform.cmswebservices.resolvers.sites.SiteThumbnailResolver;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelInternalContext;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CatalogVersionDataPopulatorTest
{

    public static final String CATALOG_MODEL_ID = "myId";
    public static final String CATALOG_VERSION = "catalogVersion";

    @Mock
    private ItemModelInternalContext mockContext;
    @Mock
    private CMSAdminPageService cmsAdminPageService;
    @Mock
    private CMSAdminSiteService cmsAdminSiteService;
    @Mock
    private LocalizationService localizationService;
    @Mock
    private SiteThumbnailResolver siteThumbnailResolver;
    @InjectMocks
    private CatalogVersionDataPopulator populator;

    @Mock
    private ContentPageModel homepage;
    @Mock
    private MediaModel thumbnail;
    @Mock
    private LocalizedValueMap localizedName;

    private CMSSiteModel sourceSite;

    private static final CatalogVersionData EXPECTED_CATALOG_VERSION_DETAILS_LIST = buildCatalogVersionDetailist();

    private static final Map<String, String> mappedValues = new HashMap<>();
    {
        mappedValues.put(Locale.UK.getLanguage(), APPAREL.getFirstInstanceOfName());
    }

    @Before
    public void setup() {
        sourceSite = buildSite(mockContext);
        when(siteThumbnailResolver.resolveHomepageThumbnailUrl(any(CMSSiteModel.class))).thenReturn(of(APPAREL.getThumbnailUri()));
        when(cmsAdminPageService.getHomepage(sourceSite)).thenReturn(homepage);
        when(homepage.getPreviewImage()).thenReturn(thumbnail);
        when(thumbnail.getDownloadURL()).thenReturn(APPAREL.getThumbnailUri());
        when(localizationService.build(any(Supplier.class), any(Function.class))).thenReturn(localizedName);
        when(localizedName.getValue()).thenReturn(mappedValues);
        populator.setCatalogVersionDetailDataComparator(new CatalogVersionDetailDataCatalogIdComparator());
    }

    @Test
    public void populateWillPopulateADtoWhenGivenAModel() throws Exception {
        CatalogVersionData target = new ComparableCatalogVersionDetailDataList();
        populator.populate(sourceSite, target);
        assertThat(target, is(EXPECTED_CATALOG_VERSION_DETAILS_LIST));
        List<ComparableCatalogVersionDetailData> targetDetails = target.getCatalogVersionDetails().stream().map((catalogVersionDetails) -> new ComparableCatalogVersionDetailData(catalogVersionDetails)).collect(toList());
        assertThat(targetDetails, contains(EXPECTED_CATALOG_VERSION_DETAILS_LIST.getCatalogVersionDetails().toArray()));
    }

    private static CatalogVersionData buildCatalogVersionDetailist() {
        CatalogVersionData catalogVersionDetailsList = new ComparableCatalogVersionDetailDataList();
        catalogVersionDetailsList.setName(APPAREL.getName());
        catalogVersionDetailsList.setUid(APPAREL.getUid());

        CatalogVersionDetailData catalogVersionDetails = new ComparableCatalogVersionDetailData();
        catalogVersionDetails.setRedirectUrl(APPAREL.getBaseUrl());
        catalogVersionDetails.setName(APPAREL.getName());
        catalogVersionDetails.setCatalogId(CATALOG_MODEL_ID);
        catalogVersionDetails.setVersion(CATALOG_VERSION);
        catalogVersionDetails.setThumbnailUrl(APPAREL.getThumbnailUri());

        catalogVersionDetailsList.setCatalogVersionDetails(newArrayList(catalogVersionDetails));

        return catalogVersionDetailsList;
    }

    private static class ComparableCatalogVersionDetailDataList extends CatalogVersionData {

        @Override
        public int hashCode() {
            return reflectionHashCode(this, excludedFields());
        }

        @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
        @Override
        public boolean equals(Object that) {
            return reflectionEquals(this, that, excludedFields());
        }

        @Override
        public String toString() {
            return reflectionToString(this, MULTI_LINE_STYLE);
        }

        private String[] excludedFields() {
            return new String[] {"name", "catalogVersionDetails"};
        }
    }

}