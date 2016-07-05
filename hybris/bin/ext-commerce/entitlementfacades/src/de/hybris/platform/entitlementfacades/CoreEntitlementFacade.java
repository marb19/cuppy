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
 */
package de.hybris.platform.entitlementfacades;

import de.hybris.platform.entitlementfacades.data.EntitlementData;

import java.util.Collection;

import javax.annotation.Nonnull;

/**
 * Facade which provides functionality to manage entitlements.
 */
public interface CoreEntitlementFacade
{
    /**
     * Returns user grants by userId.
     *
     * @param userId user identifier
     * @return list of found grants.
     * @throws de.hybris.platform.servicelayer.exceptions.ModelNotFoundException if EntitlementModel not found
     * @throws com.hybris.services.entitlements.api.exceptions.ValidationException if user id is invalid
     */
    @Nonnull
    Collection<EntitlementData> getUserGrants(final String userId);
}
