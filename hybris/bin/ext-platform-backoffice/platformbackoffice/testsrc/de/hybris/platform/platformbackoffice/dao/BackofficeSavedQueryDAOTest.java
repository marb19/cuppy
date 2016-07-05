/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */

package de.hybris.platform.platformbackoffice.dao;

import static org.junit.Assert.*;

import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.platformbackoffice.model.BackofficeSavedQueryModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;


public class BackofficeSavedQueryDAOTest extends ServicelayerTransactionalTest
{
	public static final String TEST_USER_1 = "savedQueriesUser1";
	public static final String TEST_USER_2 = "savedQueriesUser2";
	public static final String TEST_USER_3 = "savedQueriesUser3";
	public static final String TEST_USER_4 = "savedQueriesUser4";
	public static final String TEST_Group_1 = "savedQueriesUserGroup1";
	public static final String TEST_Group_2 = "savedQueriesUserGroup2";
	public static final String TEST_Group_3 = "savedQueriesUserGroup3";
	public static final String TEST_Group_4 = "savedQueriesUserGroup4";
	public static final String TEST_QUERY_1 = "savedQuery1";
	public static final String TEST_QUERY_2 = "savedQuery2";
	public static final String TEST_QUERY_3 = "savedQuery3";
	public static final String TEST_QUERY_4 = "savedQuery4";
	@Resource
	private BackofficeSavedQueryDAO backofficeSavedQueryDAO;

	@Resource
	private ModelService modelService;

	@Resource
	private UserService userService;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultCatalog();
		final UserGroupModel userGroup1 = createUserGroup(TEST_Group_1);
		final UserGroupModel userGroup2 = createUserGroup(TEST_Group_2);
		final UserGroupModel userGroup3 = createUserGroup(TEST_Group_3);
		final UserGroupModel userGroup4 = createUserGroup(TEST_Group_4);
		userGroup3.setGroups(Sets.newHashSet(userGroup1, userGroup2));
		modelService.save(userGroup3);

		final UserModel testUser1 = createTestUser(TEST_USER_1, userGroup1);
		final UserModel testUser2 = createTestUser(TEST_USER_2, userGroup2);
		final UserModel testUser3 = createTestUser(TEST_USER_3, userGroup3);
		final UserModel testUser4 = createTestUser(TEST_USER_4, userGroup4);

		createTestQuery(TEST_QUERY_1, testUser1, userGroup2);
		createTestQuery(TEST_QUERY_2, testUser2, userGroup1);
		createTestQuery(TEST_QUERY_3, testUser2);
		createTestQuery(TEST_QUERY_4,testUser4);
	}

	@Test
	public void testGetSavedQueries() throws Exception
	{
		List<BackofficeSavedQueryModel> savedQueries = backofficeSavedQueryDAO.findSavedQueries(userService
				.getUserForUID(TEST_USER_1));
		assertEquals(2, savedQueries.size());
		assertNotNull(getQueryByName(TEST_QUERY_1, savedQueries));
		assertNotNull(getQueryByName(TEST_QUERY_2, savedQueries));

		savedQueries = backofficeSavedQueryDAO.findSavedQueries(userService.getUserForUID(TEST_USER_2));
		assertEquals(3, savedQueries.size());
		assertNotNull(getQueryByName(TEST_QUERY_1, savedQueries));
		assertNotNull(getQueryByName(TEST_QUERY_2, savedQueries));
		assertNotNull(getQueryByName(TEST_QUERY_3, savedQueries));

		savedQueries = backofficeSavedQueryDAO.findSavedQueries(userService.getUserForUID(TEST_USER_3));
		assertEquals(2, savedQueries.size());
		assertNotNull(getQueryByName(TEST_QUERY_1, savedQueries));
		assertNotNull(getQueryByName(TEST_QUERY_2, savedQueries));

		savedQueries = backofficeSavedQueryDAO.findSavedQueries(userService.getUserForUID(TEST_USER_4));
		assertEquals(1, savedQueries.size());
		assertNotNull(getQueryByName(TEST_QUERY_4, savedQueries));
	}

	private BackofficeSavedQueryModel getQueryByName(final String queryName, final List<BackofficeSavedQueryModel> queries)
	{
		return queries.stream().filter(query -> queryName.equals(query.getName())).findFirst().get();
	}

	private UserModel createTestUser(final String userId, final UserGroupModel... userGroups)
	{
		final UserModel newUser = modelService.create(UserModel.class);
		newUser.setUid(userId);
		if (userGroups != null && userGroups.length > 0)
		{
			newUser.setGroups(Sets.newHashSet(userGroups));
		}
		modelService.save(newUser);
		return newUser;
	}

	private UserGroupModel createUserGroup(final String userGroupName)
	{
		final UserGroupModel userGroup = modelService.create(UserGroupModel.class);
		userGroup.setUid(userGroupName);
		modelService.save(userGroup);
		return userGroup;
	}

	private void createTestQuery(final String queryName, final UserModel owner, final UserGroupModel... userGroups)
	{
		final BackofficeSavedQueryModel savedQuery = modelService.create(BackofficeSavedQueryModel.class);
		savedQuery.setName(queryName);
		savedQuery.setTypeCode("testTypeCode");
		savedQuery.setQueryOwner(owner);
		savedQuery.setUserGroups(Arrays.asList(userGroups));
		modelService.save(savedQuery);
	}
}
