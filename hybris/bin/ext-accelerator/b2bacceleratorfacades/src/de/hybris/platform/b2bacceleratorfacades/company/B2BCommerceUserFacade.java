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
package de.hybris.platform.b2bacceleratorfacades.company;

import de.hybris.platform.b2bapprovalprocessfacades.company.data.B2BPermissionData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BSelectionData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUserGroupData;
import de.hybris.platform.b2bcommercefacades.company.data.UserData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;


/**
 * A facade for user management within b2b commerce
 *
 * Interface kept for backwards compatibility reasons.
 *
 * @deprecated Use {@link de.hybris.platform.b2bcommercefacades.company.B2BUserFacade} instead.
 *
 * @since 6.0
 */
@Deprecated
public interface B2BCommerceUserFacade extends de.hybris.platform.b2bcommercefacades.company.B2BUserFacade
{

	/**
	 * Gets the list of approvers for the customers
	 *
	 * @param pageableData
	 *           Pagination Data
	 * @param uid
	 *           of customer
	 * @return Get Paginated list found approvers
	 */
	SearchPageData<UserData> getPagedApproversForCustomer(PageableData pageableData, String uid);

	/**
	 * Add an approver to the customer
	 *
	 * @param user
	 *           the uid of the customer
	 * @param approver
	 *           the approver uid
	 * @return Returns the {@link de.hybris.platform.b2bcommercefacades.company.data.B2BSelectionData}
	 */
	B2BSelectionData addApproverForCustomer(String user, String approver);

	/**
	 * Remove an existing approver to the customer
	 *
	 * @param user
	 *           the uid of the customer
	 * @param approver
	 *           the approver uid
	 * @return Returns the {@link de.hybris.platform.b2bcommercefacades.company.data.B2BSelectionData}
	 */
	B2BSelectionData removeApproverFromCustomer(String user, String approver);

	/**
	 * Gets the list of permissions of the customers
	 *
	 * @param pageableData
	 *           Pagination Data
	 * @param uid
	 *           the uid of the customer
	 * @return Returns the {@link de.hybris.platform.commerceservices.search.pagedata.SearchPageData}
	 */
	SearchPageData<B2BPermissionData> getPagedPermissionsForCustomer(PageableData pageableData, String uid);

	/**
	 * Add a permission to a customer
	 *
	 * @param user
	 *           the uid of the customer
	 * @param permission
	 *           the code of the permission
	 * @return Returns the {@link de.hybris.platform.b2bcommercefacades.company.data.B2BSelectionData}
	 */
	B2BSelectionData addPermissionToCustomer(String user, String permission);

	/**
	 * Remove a permission from a customer
	 *
	 * @param user
	 *           the uid of the customer
	 * @param permission
	 *           the code of the permission
	 * @return Returns the {@link de.hybris.platform.b2bcommercefacades.company.data.B2BSelectionData}
	 */
	B2BSelectionData removePermissionFromCustomer(String user, String permission);

	/**
	 * Get Paginated list of B2B User groups the customer belongs to.
	 *
	 * @param pageableData
	 *           Pageable Data
	 * @param user
	 *           the uid of the customer
	 * @return Returns the {@link de.hybris.platform.commerceservices.search.pagedata.SearchPageData}
	 */
	@Override
	SearchPageData<B2BUserGroupData> getPagedB2BUserGroupsForCustomer(PageableData pageableData, String user);

	/**
	 * @Deprecated Use deselectB2BUserGroupFromCustomer(String user, String usergroup) or
	 *             removeB2BUserGroupFromCustomerGroups(String user, String usergroup) instead.
	 *
	 */
	@Deprecated
	B2BSelectionData removeB2BUserGroupFromCustomer(String user, String usergroup);

}
