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
package de.hybris.platform.cmswebservices.sites.facade;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.sort;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import de.hybris.platform.cmswebservices.data.SiteData;
import de.hybris.platform.cmswebservices.sites.facade.populator.model.ComparableSiteData;

import java.util.Comparator;
import java.util.List;

import org.junit.Test;

public class SiteDataUidComparatorTest {

    private static final SiteData FIRST = new ComparableSiteData(); {
        FIRST.setUid("1");
    }

    private static final SiteData SECOND = new ComparableSiteData(); {
        SECOND.setUid("2");
    }

    private static final SiteData[] reverseOrdered = new SiteData[] {SECOND, FIRST};
    private static final SiteData[] ordered = new SiteData[] {FIRST, SECOND};

    private final Comparator comparator = new SiteDataUidComparator();

    @Test
    public void comparatorWillReturnInOrder() throws Exception {
        List<SiteData> collectionToSort = newArrayList(reverseOrdered);
        sort(collectionToSort, comparator);
        assertThat(collectionToSort, contains(ordered));
    }

    @Test
    public void comparatorWillReturnInReverseOrder() throws Exception {
        List<SiteData> collectionToSort = newArrayList(ordered);
        sort(collectionToSort, comparator.reversed());
        assertThat(collectionToSort, contains(reverseOrdered));
    }
}