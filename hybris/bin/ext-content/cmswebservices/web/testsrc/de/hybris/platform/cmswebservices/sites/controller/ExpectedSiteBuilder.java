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

import static de.hybris.platform.cmswebservices.util.models.CMSSiteModelMother.TemplateSite.APPAREL;

import de.hybris.platform.cmswebservices.data.SiteData;
import de.hybris.platform.cmswebservices.sites.facade.populator.model.ComparableSiteData;

public class ExpectedSiteBuilder {

    public static ComparableSiteData buildApparel() {
        SiteData sitedata = new SiteData();
        sitedata.setName(APPAREL.getName());
        sitedata.setUid(APPAREL.getUid());
        sitedata.setThumbnailUrl(APPAREL.getThumbnailUri());
        sitedata.setRedirectUrl(APPAREL.getBaseUrl());
        return new ComparableSiteData(sitedata);
    }
}
