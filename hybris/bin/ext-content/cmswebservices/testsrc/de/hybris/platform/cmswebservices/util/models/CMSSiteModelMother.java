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
package de.hybris.platform.cmswebservices.util.models;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSContentCatalogDao;
import de.hybris.platform.cms2.servicelayer.daos.CMSSiteDao;
import de.hybris.platform.cmswebservices.data.LocalizedValueData;
import de.hybris.platform.cmswebservices.localization.data.LocalizedValueMap;
import de.hybris.platform.cmswebservices.util.builder.CMSSiteModelBuilder;
import org.springframework.beans.factory.annotation.Required;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static de.hybris.platform.cmswebservices.util.builder.CMSSiteModelBuilder.fromModel;
import static de.hybris.platform.cmswebservices.util.models.CMSSiteModelMother.TemplateSite.APPAREL;
import static java.util.Arrays.asList;
import static java.util.Locale.UK;
import static java.util.stream.Collectors.toList;
import static org.joda.time.DateTime.now;

public class CMSSiteModelMother extends AbstractModelMother<CMSSiteModel> {

    public enum TemplateSite {
        APPAREL(buildName(UK, "Apparel"), "TestApparel", "http://apparel.com", "&attachment=true"),
        POWER_TOOLS(buildName(UK, "Power Tools"), "TestPowerTools", "http://power.com", "&attachment=true"),
        ELECTRONICS(buildName(UK, "Electronics"), "TestElectronics", "http://electric.com", "&attachment=true");

        private final String uid;
        private final LocalizedValueMap name;
        private final String baseUrl;
        private final String thumbnailUri;

        TemplateSite(final LocalizedValueMap name, final String uid, final String baseUrl, final String thumbnailUri) {
            this.name = name;
            this.uid = uid;
            this.baseUrl = baseUrl;
            this.thumbnailUri = thumbnailUri;
        }

        public String getUid() {
            return uid;
        }

        public LocalizedValueMap getName() {
            return name;
        }

        public String getFirstInstanceOfName() {
            Map<String, String> value = getName().getValue();
            String firstKey = value.keySet().stream().findFirst().get();
            return value.get(firstKey);
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public String getThumbnailUri() {
            return thumbnailUri;
        }

        public static LocalizedValueMap buildName(Locale locale, String value) {
            final Map<String, String> map = new HashMap<>();
            map.put(locale.getLanguage(), value);
            return new LocalizedValueMap() {
                @Override
                public Map<String, String> getValue() {
                    return map;
                }

                @Override
                public void setValue(Map<String, String> value) {
                    throw new RuntimeException("Jedi mind trick: This is not the mock you're looking for... move along.");
                }
            };

        }
    }

    private CMSSiteDao cmsSiteDao;

    private CMSContentCatalogDao cmsContentCatalogDao;

    public CMSSiteModel createSiteWithTemplate(final TemplateSite site, final CatalogVersionModel... catalogs) {
        return createSiteWithMinimumParameters(site.getFirstInstanceOfName(), site.getUid(), site.getBaseUrl(), catalogs);
    }

    protected CMSSiteModel createSiteWithMinimumParameters(final String name, final String uid, final String url, final CatalogVersionModel[] catalogs) {

        List<ContentCatalogModel> contentCatalogs = asList(catalogs)
                .stream().map(this::getContentCatalogFromCatalogVersion)
                .collect(toList());
        return getFromCollectionOrSaveAndReturn(
                () -> getCmsSiteDao().findCMSSitesById(uid),
                () -> fromModel(defaultSite())
                        .withEnglishName(name)
                        .withUid(uid)
                        .withRedirectUrl(url)
                        .usingCatalogs(contentCatalogs)
                        .build());
    }

    private ContentCatalogModel getContentCatalogFromCatalogVersion(final CatalogVersionModel catalogVersionModel) {
        return getCmsContentCatalogDao().findContentCatalogById(catalogVersionModel.getCatalog().getId());
    }

    protected CMSSiteModel defaultSite()
    {
        return CMSSiteModelBuilder.aModel()
                .withEnglishName(APPAREL.getFirstInstanceOfName())
                .active()
                .from(now().minusDays(5).withTimeAtStartOfDay().toDate())
                .until(now().plusDays(5).withTimeAtStartOfDay().toDate())
                .withUid(APPAREL.getUid())
                .withRedirectUrl(APPAREL.getBaseUrl())
                .build();
    }

    public CMSSiteDao getCmsSiteDao() {
        return cmsSiteDao;
    }

    @Required
    public void setCmsSiteDao(CMSSiteDao cmsSiteDao) {
        this.cmsSiteDao = cmsSiteDao;
    }

    @Required
    public void setCmsContentCatalogDao(CMSContentCatalogDao cmsContentCatalogDao) {
        this.cmsContentCatalogDao = cmsContentCatalogDao;
    }

    public CMSContentCatalogDao getCmsContentCatalogDao() {
        return cmsContentCatalogDao;
    }
}
