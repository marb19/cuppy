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

import static org.apache.commons.beanutils.BeanUtils.copyProperties;
import static org.apache.commons.collections.SetUtils.isEqualSet;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import de.hybris.platform.cmswebservices.data.CatalogVersionDetailData;
import de.hybris.platform.cmswebservices.localization.data.LocalizedValueMap;
import de.hybris.platform.cmswebservices.localization.data.LocalizedValueString;

import org.apache.commons.codec.binary.StringUtils;

public class ComparableCatalogVersionDetailData extends CatalogVersionDetailData
{

    public ComparableCatalogVersionDetailData(){
    }

    public ComparableCatalogVersionDetailData(CatalogVersionDetailData details){
        try {
            copyProperties(this, details);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this, excludeFields());
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object that) {
        return reflectionEquals(this, that, excludeFields())
                && (
                    (getName() == null)
                    ||
                    (getName() instanceof LocalizedValueMap
                        && namesEqual((LocalizedValueMap) getName(), (LocalizedValueMap) ((CatalogVersionDetailData) that).getName()))
                    || (getName() instanceof LocalizedValueString
                        && namesEqual((LocalizedValueString) getName(), (LocalizedValueString) ((CatalogVersionDetailData) that).getName()))
                );
    }


    public boolean namesEqual(LocalizedValueString thisName, LocalizedValueString thatName) {
        if(thisName == null && thatName == null) {
            return true;
        } else if (thisName == null || thatName == null) {
            return false;
        }
        return StringUtils.equals(thatName.getValue(), thisName.getValue());
    }


    public boolean namesEqual(LocalizedValueMap thisName, LocalizedValueMap thatName) {
        if(thisName == null && thatName == null) {
            return true;
        } else if (thisName == null || thatName == null) {
            return false;
        }
        return isEqualSet(thatName.getValue().entrySet(), thisName.getValue().entrySet());
    }

    @Override
    public String toString() {
        return reflectionToString(this, MULTI_LINE_STYLE);
    }

    public String[] excludeFields()  {
        return new String[] {"name"};
    }
}
