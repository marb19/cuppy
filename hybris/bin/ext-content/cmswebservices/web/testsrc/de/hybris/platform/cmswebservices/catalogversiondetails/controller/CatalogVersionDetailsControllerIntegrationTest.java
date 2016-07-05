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
package de.hybris.platform.cmswebservices.catalogversiondetails.controller;

import static de.hybris.platform.cmswebservices.util.models.CMSSiteModelMother.TemplateSite.APPAREL;
import static de.hybris.platform.cmswebservices.util.models.CatalogVersionModelMother.CatalogVersion.ONLINE;
import static de.hybris.platform.cmswebservices.util.models.CatalogVersionModelMother.CatalogVersion.STAGED;
import static de.hybris.platform.cmswebservices.util.models.ContentCatalogModelMother.CatalogTemplate.ID_ONLINE;
import static de.hybris.platform.cmswebservices.util.models.ContentCatalogModelMother.CatalogTemplate.ID_STAGED;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.OK;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cmswebservices.catalogversiondetails.facade.populator.model.ComparableCatalogVersionDetailData;
import de.hybris.platform.cmswebservices.data.CatalogVersionData;
import de.hybris.platform.cmswebservices.data.CatalogVersionDetailData;
import de.hybris.platform.cmswebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.cmswebservices.util.apiclient.ApiClient;
import de.hybris.platform.cmswebservices.util.apiclient.Response;
import de.hybris.platform.cmswebservices.util.models.CMSSiteModelMother;
import de.hybris.platform.cmswebservices.util.models.CatalogVersionModelMother;
import de.hybris.platform.cmswebservices.util.models.CatalogVersionModelMother.CatalogVersion;
import de.hybris.platform.cmswebservices.util.models.ContentCatalogModelMother;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class CatalogVersionDetailsControllerIntegrationTest extends ApiBaseIntegrationTest {

    private static final String CATALOG_VERSION_DETAILS_ENDPOINT = "/v1/sites/{uid}/catalogversiondetails";

    private static final Map<String, String> APPAREL_PATH_PARAMETERS = new HashMap<>();
    {
        APPAREL_PATH_PARAMETERS.put("uid", APPAREL.getUid());
    }

    private static final CatalogVersionDetailData[] EXPECTED_CATALOG_DETAILS = new CatalogVersionDetailData[] {
            buildOnline(),
            buildStaged()
    };

    private ApiClient apiClient;

    @Resource
    private CMSSiteModelMother cmsSiteModelMother;

    @Resource
    private CatalogVersionModelMother catalogVersionModelMother;

    @Before
    public void setup(){
        apiClient = getApiClientInstance();
    }

    @Test
    public void postOnCatalogueVersionDetailsWillCauseMETHOD_NOT_ALLOWED() throws Exception {
        final String endPoint = replaceUriVariables(CATALOG_VERSION_DETAILS_ENDPOINT, APPAREL_PATH_PARAMETERS);
        cmsSiteModelMother.createSiteWithTemplate(APPAREL,
                catalogVersionModelMother.createOnlineCatalogVersionModelWithId(ID_ONLINE.name()),
                catalogVersionModelMother.createStagedCatalogVersionModelWithId(ID_STAGED.name()));
        Response<Void> post = apiClient.request()
                .endpoint(endPoint)
                .acceptLanguages(null)
                .acceptJson().hybrisLanguages(Locale.ENGLISH)
                .post(new CatalogVersionDetailData());
        assertStatusCode(post, METHOD_NOT_ALLOWED.value());
    }

    @Test
    public void putOnCatalogueVersionDetailsWillCauseMETHOD_NOT_ALLOWED() throws Exception {
        cmsSiteModelMother.createSiteWithTemplate(APPAREL);
        final String endPoint = replaceUriVariables(CATALOG_VERSION_DETAILS_ENDPOINT, APPAREL_PATH_PARAMETERS);
        Response<Void> post = apiClient.request()
                .endpoint(endPoint)
                .acceptLanguages(null)
                .acceptJson().hybrisLanguages(Locale.ENGLISH)
                .put(new CatalogVersionDetailData());
        assertStatusCode(post, METHOD_NOT_ALLOWED.value());
    }

    @Test
    public void getOnCatalogueVersionDetailsWillReturnAnEmptyList() throws Exception {
        cmsSiteModelMother.createSiteWithTemplate(APPAREL);
        final String endPoint = replaceUriVariables(CATALOG_VERSION_DETAILS_ENDPOINT, APPAREL_PATH_PARAMETERS);
        final Response<CatalogVersionData> response = apiClient.request()
                .endpoint(endPoint)
                .acceptLanguages(null)
                .acceptJson().hybrisLanguages(Locale.ENGLISH)
                .get(CatalogVersionData.class);

        assertStatusCode(response, OK.value());
        assertTrue(CollectionUtils.isEmpty(response.getBody().getCatalogVersionDetails()));
    }

    @Test
    public void getOnCatalogueVersionDetailsWillReturnAListOfOnlineAndStaged() throws Exception {

        cmsSiteModelMother.createSiteWithTemplate(APPAREL,
                catalogVersionModelMother.createOnlineCatalogVersionModelWithId(ID_ONLINE.name()),
                catalogVersionModelMother.createStagedCatalogVersionModelWithId(ID_STAGED.name()));
        final String endPoint = replaceUriVariables(CATALOG_VERSION_DETAILS_ENDPOINT, APPAREL_PATH_PARAMETERS);
        final Response<CatalogVersionData> response = apiClient.request()
                .endpoint(endPoint)
                .acceptLanguages(null)
                .acceptJson().hybrisLanguages(Locale.ENGLISH)
                .get(CatalogVersionData.class);

        assertStatusCode(response, OK.value());
        assertThat(response.getBody().getCatalogVersionDetails(), hasSize(2) );
        Collection<CatalogVersionDetailData> catalogVersions = makeCatalogVersionDetailDataComparable(response.getBody().getCatalogVersionDetails());
        assertThat(catalogVersions, hasItems(EXPECTED_CATALOG_DETAILS));
    }

    @Test
    public void getOnCatalogueVersionDetailsWillReturnAListIncludingOnline() throws Exception {

        cmsSiteModelMother.createSiteWithTemplate(APPAREL,
                catalogVersionModelMother.createOnlineCatalogVersionModelWithId(ID_ONLINE.name()),
                catalogVersionModelMother.createStagedCatalogVersionModelWithId(ID_STAGED.name()));
        final String endPoint = replaceUriVariables(CATALOG_VERSION_DETAILS_ENDPOINT, APPAREL_PATH_PARAMETERS);
        final Response<CatalogVersionData> response = apiClient.request()
                .endpoint(endPoint)
                .acceptLanguages(null)
                .acceptJson().hybrisLanguages(Locale.ENGLISH)
                .get(CatalogVersionData.class);

        assertStatusCode(response, OK.value());
        assertThat(response.getBody().getCatalogVersionDetails(), hasSize(2) );
        Collection<CatalogVersionDetailData> catalogVersions = makeCatalogVersionDetailDataComparable(response.getBody().getCatalogVersionDetails());
        CatalogVersionDetailData online = findCatalogWithVersion(catalogVersions, ONLINE);
        assertThat(online, is(buildOnline()));
    }

    private CatalogVersionDetailData findCatalogWithVersion(Collection<CatalogVersionDetailData> catalogVersions, CatalogVersion version) {
        return catalogVersions.stream()
                .filter(catalogVersionDetailData -> equalsIgnoreCase(version.getVersion(), catalogVersionDetailData.getVersion()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Could not find " + version.getVersion()));
    }


    @Test
    public void getOnCatalogueVersionDetailsWillReturnAListIncludingStaged() throws Exception {

        cmsSiteModelMother.createSiteWithTemplate(APPAREL,
                catalogVersionModelMother.createOnlineCatalogVersionModelWithId(ID_ONLINE.name()),
                catalogVersionModelMother.createStagedCatalogVersionModelWithId(ID_STAGED.name()));
        final String endPoint = replaceUriVariables(CATALOG_VERSION_DETAILS_ENDPOINT, APPAREL_PATH_PARAMETERS);
        final Response<CatalogVersionData> response = apiClient.request()
                .endpoint(endPoint)
                .acceptLanguages(null)
                .acceptJson().hybrisLanguages(Locale.ENGLISH)
                .get(CatalogVersionData.class);

        assertStatusCode(response, OK.value());
        assertThat(response.getBody().getCatalogVersionDetails(), hasSize(2) );
        Collection<CatalogVersionDetailData> catalogVersions = makeCatalogVersionDetailDataComparable(response.getBody().getCatalogVersionDetails());
        CatalogVersionDetailData staged = findCatalogWithVersion(catalogVersions, STAGED);
        assertThat(staged, is(buildStaged()));
    }

    private static CatalogVersionDetailData buildOnline() {
        return new ComparableCatalogVersionDetailData(buildCatalogVersionDetailData(ONLINE.getVersion(), ID_ONLINE.name()));
    }

    private static CatalogVersionDetailData buildStaged() {
        return new ComparableCatalogVersionDetailData(buildCatalogVersionDetailData(STAGED.getVersion(), ID_STAGED.name()));
    }

    private static CatalogVersionDetailData buildCatalogVersionDetailData(final String version, final String id) {
        CatalogVersionDetailData catalogVersionDetails = new ComparableCatalogVersionDetailData();
        catalogVersionDetails.setVersion(version);
        catalogVersionDetails.setCatalogId(id);
        catalogVersionDetails.setRedirectUrl(APPAREL.getBaseUrl());
        catalogVersionDetails.setName(ContentCatalogModelMother.CatalogTemplate.ID_APPLE.getHumanName());
        return catalogVersionDetails;
    }

    private static Collection<CatalogVersionDetailData> makeCatalogVersionDetailDataComparable(Collection<CatalogVersionDetailData> catalogData) throws Exception {
        return catalogData.stream().map(ComparableCatalogVersionDetailData::new).collect(toList());
    }

}



