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
package de.hybris.platform.cmswebservices.sites.controller;

import static de.hybris.platform.cmswebservices.sites.controller.ExpectedSiteBuilder.buildApparel;
import static de.hybris.platform.cmswebservices.util.models.CMSSiteModelMother.TemplateSite.APPAREL;
import static de.hybris.platform.cmswebservices.util.models.CMSSiteModelMother.TemplateSite.ELECTRONICS;
import static de.hybris.platform.cmswebservices.util.models.CMSSiteModelMother.TemplateSite.POWER_TOOLS;
import static de.hybris.platform.cmswebservices.util.models.ContentCatalogModelMother.CatalogTemplate.ID_ONLINE;
import static de.hybris.platform.cmswebservices.util.models.ContentCatalogModelMother.CatalogTemplate.ID_STAGED;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.get;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.HttpStatus.OK;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cmswebservices.data.SiteData;
import de.hybris.platform.cmswebservices.data.SiteListData;
import de.hybris.platform.cmswebservices.sites.facade.populator.model.ComparableSiteData;
import de.hybris.platform.cmswebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.cmswebservices.util.apiclient.ApiClient;
import de.hybris.platform.cmswebservices.util.apiclient.Response;
import de.hybris.platform.cmswebservices.util.models.CMSSiteModelMother;
import de.hybris.platform.cmswebservices.util.models.CatalogVersionModelMother;
import de.hybris.platform.cmswebservices.util.models.ContentPageModelMother;

import java.util.Collection;
import java.util.Locale;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class SiteControllerIntegrationTest extends ApiBaseIntegrationTest {

    private static final String SITES_ENDPOINT = "/v1/sites";

    private static final SiteData EXPECTED_APPAREL_SITE = buildApparel();

    private ApiClient apiClient;

    @Resource
    private CMSSiteModelMother cmsSiteModelMother;

    @Resource
    private CatalogVersionModelMother catalogVersionModelMother;

    @Resource
    private ContentPageModelMother contentPageModelMother;

    @Before
    public void setup(){
        apiClient = getApiClientInstance();
    }

    @Test
    public void getOnSiteWillReturnAnEmptyListOfSitesWhenNothingIsAvailable() throws Exception {
        final Response<SiteListData> response = apiClient.request()
                .endpoint(SITES_ENDPOINT)
                .acceptLanguages(null)
                .acceptJson().hybrisLanguages(Locale.ENGLISH)
                .get(SiteListData.class);

        assertStatusCode(response, OK.value());
        assertTrue(CollectionUtils.isEmpty(response.getBody().getSites()));
    }

    @Test
    public void getOnSiteWillReturnAListOfSitesWithApparelWhenApparelIsAvailable() throws Exception {
        CatalogVersionModel[] catalogVersionModels = new CatalogVersionModel[]
                {catalogVersionModelMother.createOnlineCatalogVersionModelWithId(ID_ONLINE.name()),
                        catalogVersionModelMother.createStagedCatalogVersionModelWithId(ID_STAGED.name())};
        contentPageModelMother.Homepage_Page(catalogVersionModels[0]);
        cmsSiteModelMother.createSiteWithTemplate(APPAREL,catalogVersionModels);
        cmsSiteModelMother.createSiteWithTemplate(ELECTRONICS,catalogVersionModels);
        cmsSiteModelMother.createSiteWithTemplate(POWER_TOOLS,catalogVersionModels);
        final Response<SiteListData> response = apiClient.request()
                .endpoint(SITES_ENDPOINT)
                .acceptLanguages(null)
                .acceptJson().hybrisLanguages(Locale.ENGLISH)
                .get(SiteListData.class);

        assertStatusCode(response, OK.value());
        assertThat(response.getBody().getSites(), hasSize(3) );
        Collection<SiteData> sites = makeSiteDataComparable(response.getBody().getSites());
        assertThat(sites, hasItem(EXPECTED_APPAREL_SITE));

    }

    @Test
    public void theApparelSiteItemWillHaveUidAndBaseUrlAndSiteName() throws Exception {
        CatalogVersionModel[] catalogVersionModels = new CatalogVersionModel[]
                {catalogVersionModelMother.createOnlineCatalogVersionModelWithId(ID_ONLINE.name()),
                catalogVersionModelMother.createStagedCatalogVersionModelWithId(ID_STAGED.name())};
        contentPageModelMother.Homepage_Page(catalogVersionModels[0]);
        cmsSiteModelMother.createSiteWithTemplate(APPAREL, catalogVersionModels);

        final Response<SiteListData> response = apiClient.request()
                .endpoint(SITES_ENDPOINT)
                .acceptLanguages(null)
                .acceptJson().hybrisLanguages(Locale.ENGLISH)
                .get(SiteListData.class);

        assertStatusCode(response, OK.value());
        SiteData siteData = new ComparableSiteData(get(response.getBody().getSites(), 0));
        assertThat(siteData, is(EXPECTED_APPAREL_SITE));

    }

    private static Collection<SiteData> makeSiteDataComparable(Collection<SiteData> siteData) throws Exception {
        return siteData.stream().map(ComparableSiteData::new).collect(toList());
    }

}



