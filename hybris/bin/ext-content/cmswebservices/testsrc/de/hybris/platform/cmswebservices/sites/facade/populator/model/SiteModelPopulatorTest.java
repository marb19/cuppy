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

import static de.hybris.platform.cmswebservices.util.models.CMSSiteModelMother.TemplateSite.APPAREL;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cmswebservices.data.SiteData;
import de.hybris.platform.cmswebservices.localization.data.LocalizedValueMap;
import de.hybris.platform.cmswebservices.localization.service.LocalizationService;
import de.hybris.platform.cmswebservices.resolvers.sites.SiteThumbnailResolver;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelInternalContext;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class SiteModelPopulatorTest
{

    @Mock
    private ItemModelInternalContext mockContext;
    @Mock
    private CMSAdminPageService cmsAdminPageService;
    @Mock
    private LocalizationService localizationService;
    @Mock
    private SiteThumbnailResolver siteThumbnailResolver;

    @InjectMocks
    private SiteModelPopulator populator;

    @Mock
    private ContentPageModel homepage;
    @Mock
    private MediaModel thumbnail;
    @Mock
    private LocalizedValueMap localizedName;

    private CMSSiteModel sourceSite;

    private static final SiteData EXPECTED_SITE_DTO = buildSiteDto();

    private static final Map<String, String> mappedValues = new HashMap<>();
    {
        mappedValues.put(Locale.UK.getLanguage(), APPAREL.getFirstInstanceOfName());
    }

    @Before
    public void setup() {
        sourceSite = MockedCMSSiteModelBuilder.buildSite(mockContext);
        when(siteThumbnailResolver.resolveHomepageThumbnailUrl(any(CMSSiteModel.class))).thenReturn(of(APPAREL.getThumbnailUri()));
        when(cmsAdminPageService.getHomepage(sourceSite)).thenReturn(homepage);
        when(homepage.getPreviewImage()).thenReturn(thumbnail);
        when(thumbnail.getDownloadURL()).thenReturn(APPAREL.getThumbnailUri());
        when(localizationService.build(any(Supplier.class), any(Function.class))).thenReturn(localizedName);
        when(localizedName.getValue()).thenReturn(mappedValues);
    }

    @Test
    public void populateWillPopulateADtoWhenGivenAModel() throws Exception {
        SiteData targetSiteData = new ComparableSiteData();
        populator.populate(sourceSite, targetSiteData);
        assertThat(targetSiteData, is(EXPECTED_SITE_DTO));
    }

    @Test
    public void populateWillPopulateADtoAndNameIsCorrect() throws Exception {
        SiteData targetSiteData = new ComparableSiteData();
        populator.populate(sourceSite, targetSiteData);
        assertThat(
                ((LocalizedValueMap) targetSiteData.getName()).getValue().entrySet(),
                Matchers.equalTo(((LocalizedValueMap)EXPECTED_SITE_DTO.getName()).getValue().entrySet()));
    }

    private static SiteData buildSiteDto() {
        SiteData siteData = new ComparableSiteData();
        siteData.setName(APPAREL.getName());
        siteData.setThumbnailUrl(APPAREL.getThumbnailUri());
        siteData.setUid(APPAREL.getUid());
        siteData.setPreviewUrl(MockedCMSSiteModelBuilder.PREVIEW_URL);
        siteData.setRedirectUrl(APPAREL.getBaseUrl());
        return siteData;
    }
}