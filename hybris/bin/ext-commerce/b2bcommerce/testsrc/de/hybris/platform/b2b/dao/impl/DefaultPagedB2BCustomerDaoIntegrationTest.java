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
package de.hybris.platform.b2b.dao.impl;


import static de.hybris.platform.b2b.util.B2BCommerceTestUtils.createPageableData;
import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.model.B2BUserGroupModel;
import de.hybris.platform.basecommerce.util.BaseCommerceBaseTest;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.ServicelayerTest;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;


@IntegrationTest
@ContextConfiguration(locations =
{ "classpath:b2bcommerce/test/b2bcommerce-test-spring.xml" })
public class DefaultPagedB2BCustomerDaoIntegrationTest extends BaseCommerceBaseTest
{
	private static final String DUMMY = "dummy";
	private static final String DUMMY2 = "dummy2";
	private static final String EMAIL_END = "@hybris.com";

	@Resource
	private DefaultPagedB2BCustomerDao pagedB2BCustomerDao;

	@Resource
	private B2BDaoTestUtils b2BDaoTestUtils;

	private B2BUnitModel unit;
	private B2BUserGroupModel group1;
	private B2BUserGroupModel group2;

	@Before
	public void setUp() throws Exception
	{
		ServicelayerTest.createCoreData();
		ServicelayerTest.createDefaultCatalog();
		CatalogManager.getInstance().createEssentialData(java.util.Collections.EMPTY_MAP, null);
		importCsv("/impex/essentialdata_1_usergroups.impex", "UTF-8");

		unit = b2BDaoTestUtils.createUnit(DUMMY, DUMMY);
		group1 = b2BDaoTestUtils.createUserGroup(DUMMY, unit);
		group2 = b2BDaoTestUtils.createUserGroup(DUMMY2, unit);
	}

	@Test
	public void testFindPagedCustomersExisting()
	{
		final B2BCustomerModel customerModel = b2BDaoTestUtils.createCustomer(DUMMY + EMAIL_END, DUMMY, unit, group1);

		final PageableData pageableData = createPageableData();
		final SearchPageData<B2BCustomerModel> b2BCustomers = pagedB2BCustomerDao.find(pageableData);
		b2BDaoTestUtils.assertResultsSize(1, b2BCustomers);
		assertEquals(customerModel.getName(), b2BCustomers.getResults().get(0).getName());
	}

	@Test
	public void testFindPagedCustomersIsOrderedByName()
	{
		final B2BCustomerModel customerModel = b2BDaoTestUtils.createCustomer(DUMMY + EMAIL_END, DUMMY, unit, group1);
		final B2BCustomerModel customerModel2 = b2BDaoTestUtils.createCustomer(DUMMY2 + EMAIL_END, DUMMY2, unit, group2);

		final PageableData pageableData = createPageableData();
		final SearchPageData<B2BCustomerModel> b2BCustomers = pagedB2BCustomerDao.find(pageableData);
		b2BDaoTestUtils.assertResultsSize(2, b2BCustomers);
		assertEquals(customerModel.getName(), b2BCustomers.getResults().get(0).getName());
		assertEquals(customerModel2.getName(), b2BCustomers.getResults().get(1).getName());
	}

	@Test
	public void testFindPagedCustomersInexistent()
	{
		final PageableData pageableData = createPageableData();
		final SearchPageData<B2BCustomerModel> b2BCustomers = pagedB2BCustomerDao.find(pageableData);
		b2BDaoTestUtils.assertResultsSize(0, b2BCustomers);
	}

	@Test
	public void testFindPagedCustomersByGroupMembership()
	{
		// create 2 customers, but only one in the desired group
		final B2BCustomerModel customerModel = b2BDaoTestUtils.createCustomer(DUMMY + EMAIL_END, DUMMY, unit, group1);
		b2BDaoTestUtils.createCustomer(DUMMY2 + EMAIL_END, DUMMY2, unit, group2);

		final PageableData pageableData = createPageableData();
		final SearchPageData<B2BCustomerModel> b2BCustomers = pagedB2BCustomerDao.findPagedCustomersByGroupMembership(pageableData,
				group1.getUid());
		b2BDaoTestUtils.assertResultsSize(1, b2BCustomers);
		assertEquals(customerModel.getName(), b2BCustomers.getResults().get(0).getName());
	}

	@Test
	public void testFindPagedApproversForUnitByGroupMembership()
	{
		final B2BCustomerModel customerModel = b2BDaoTestUtils.createCustomer(DUMMY + EMAIL_END, DUMMY, unit, group1);
		final B2BCustomerModel customerModel2 = b2BDaoTestUtils.createCustomer(DUMMY2 + EMAIL_END, DUMMY2, unit, group2);

		final PageableData pageableData = createPageableData();
		final SearchPageData<B2BCustomerModel> b2BCustomers = pagedB2BCustomerDao
				.findPagedApproversForUnitByGroupMembership(pageableData, unit.getUid(), group1.getUid());
		// in this case should bring both, even if the user group only matches the 1st customer,
		// because the unit is the same, and the search uses " OR {b2bunit:uid} = ?unit "
		b2BDaoTestUtils.assertResultsSize(2, b2BCustomers);
		assertEquals(customerModel.getName(), b2BCustomers.getResults().get(0).getName());
		assertEquals(customerModel2.getName(), b2BCustomers.getResults().get(1).getName());
	}

	@Test
	public void testFindPagedCustomersForUnitByGroupMembership()
	{
		// create 2 customers, but only one in the desired group
		final B2BCustomerModel customerModel = b2BDaoTestUtils.createCustomer(DUMMY + EMAIL_END, DUMMY, unit, group1);
		b2BDaoTestUtils.createCustomer(DUMMY2 + EMAIL_END, DUMMY2, unit, group2);

		final PageableData pageableData = createPageableData();
		final SearchPageData<B2BCustomerModel> b2BCustomers = pagedB2BCustomerDao
				.findPagedCustomersForUnitByGroupMembership(pageableData, unit.getUid(), group1.getUid());
		b2BDaoTestUtils.assertResultsSize(1, b2BCustomers);
		assertEquals(customerModel.getName(), b2BCustomers.getResults().get(0).getName());
	}

	@Test
	public void testFindPagedCustomersForUnit()
	{
		final B2BCustomerModel customerModel = b2BDaoTestUtils.createCustomer(DUMMY + EMAIL_END, DUMMY, unit, group1);

		final PageableData pageableData = createPageableData();
		final SearchPageData<B2BCustomerModel> b2BCustomers = pagedB2BCustomerDao.findPagedCustomersForUnit(pageableData,
				unit.getUid());
		b2BDaoTestUtils.assertResultsSize(1, b2BCustomers);
		assertEquals(customerModel.getName(), b2BCustomers.getResults().get(0).getName());
	}

	@Test
	public void testFindPagination()
	{
		final int pageSize = 3;
		final B2BCustomerModel[] models = createCustomerModels(5);
		final PageableData pageableData = createPageableData();
		pageableData.setPageSize(pageSize);

		// get first page of results
		pageableData.setCurrentPage(0);
		final SearchPageData<B2BCustomerModel> results0 = pagedB2BCustomerDao.find(pageableData);
		// check if 1st 3 customers are in the 1st page
		assertPaginationResults(models, results0, pageSize, 0);

		// get second page of results
		pageableData.setCurrentPage(1);
		final SearchPageData<B2BCustomerModel> results1 = pagedB2BCustomerDao.find(pageableData);
		// check if last 2 customers are in the 2nd page
		assertPaginationResults(models, results1, 2, pageSize);
	}

	@Test
	public void testFindPagedCustomersByGroupMembershipPagination()
	{
		final int pageSize = 3;
		final B2BCustomerModel[] models = createCustomerModels(5);
		final PageableData pageableData = createPageableData();
		pageableData.setPageSize(pageSize);

		// get first page of results
		pageableData.setCurrentPage(0);
		final SearchPageData<B2BCustomerModel> results0 = pagedB2BCustomerDao.findPagedCustomersByGroupMembership(pageableData,
				group1.getUid());
		// check if 1st 3 customers are in the 1st page
		assertPaginationResults(models, results0, pageSize, 0);

		// get second page of results
		pageableData.setCurrentPage(1);
		final SearchPageData<B2BCustomerModel> results1 = pagedB2BCustomerDao.findPagedCustomersByGroupMembership(pageableData,
				group1.getUid());
		// check if last 2 customers are in the 2nd page
		assertPaginationResults(models, results1, 2, pageSize);
	}

	@Test
	public void testFindPagedApproversForUnitByGroupMembershipPagination()
	{
		final int pageSize = 3;
		final B2BCustomerModel[] models = createCustomerModels(5);
		final PageableData pageableData = createPageableData();
		pageableData.setPageSize(pageSize);

		// get first page of results
		pageableData.setCurrentPage(0);
		final SearchPageData<B2BCustomerModel> results0 = pagedB2BCustomerDao
				.findPagedApproversForUnitByGroupMembership(pageableData, unit.getUid(), group1.getUid());
		// check if 1st 3 customers are in the 1st page
		assertPaginationResults(models, results0, pageSize, 0);

		// get second page of results
		pageableData.setCurrentPage(1);
		final SearchPageData<B2BCustomerModel> results1 = pagedB2BCustomerDao
				.findPagedApproversForUnitByGroupMembership(pageableData, unit.getUid(), group1.getUid());
		// check if last 2 customers are in the 2nd page
		assertPaginationResults(models, results1, 2, pageSize);
	}

	@Test
	public void testFindPagedCustomersForUnitByGroupMembershipPagination()
	{
		final int pageSize = 3;
		final B2BCustomerModel[] models = createCustomerModels(5);
		final PageableData pageableData = createPageableData();
		pageableData.setPageSize(pageSize);

		// get first page of results
		pageableData.setCurrentPage(0);
		final SearchPageData<B2BCustomerModel> results0 = pagedB2BCustomerDao
				.findPagedCustomersForUnitByGroupMembership(pageableData, unit.getUid(), group1.getUid());
		// check if 1st 3 customers are in the 1st page
		assertPaginationResults(models, results0, pageSize, 0);

		// get second page of results
		pageableData.setCurrentPage(1);
		final SearchPageData<B2BCustomerModel> results1 = pagedB2BCustomerDao
				.findPagedCustomersForUnitByGroupMembership(pageableData, unit.getUid(), group1.getUid());
		// check if last 2 customers are in the 2nd page
		assertPaginationResults(models, results1, 2, pageSize);
	}

	@Test
	public void testFindPagedCustomersForUnitPagination()
	{
		final int pageSize = 3;
		final B2BCustomerModel[] models = createCustomerModels(5);
		final PageableData pageableData = createPageableData();
		pageableData.setPageSize(pageSize);

		// get first page of results
		pageableData.setCurrentPage(0);
		final SearchPageData<B2BCustomerModel> results0 = pagedB2BCustomerDao.findPagedCustomersForUnit(pageableData,
				unit.getUid());
		// check if 1st 3 customers are in the 1st page
		assertPaginationResults(models, results0, pageSize, 0);

		// get second page of results
		pageableData.setCurrentPage(1);
		final SearchPageData<B2BCustomerModel> results1 = pagedB2BCustomerDao.findPagedCustomersForUnit(pageableData,
				unit.getUid());
		// check if last 2 customers are in the 2nd page
		assertPaginationResults(models, results1, 2, pageSize);
	}

	private B2BCustomerModel[] createCustomerModels(final int size)
	{
		final B2BCustomerModel[] models = new B2BCustomerModel[size];
		for (int i = 0; i < models.length; i++)
		{
			models[i] = b2BDaoTestUtils.createCustomer("c" + i + EMAIL_END, "c" + i, unit, group1);
		}
		return models;
	}

	private void assertPaginationResults(final B2BCustomerModel[] models, final SearchPageData<B2BCustomerModel> results,
			final int expectedSize, final int initialIndex)
	{
		b2BDaoTestUtils.assertResultsSize(expectedSize, results);
		for (int i = 0; i < expectedSize; i++)
		{
			assertEquals(models[initialIndex + i].getName(), results.getResults().get(i).getName());
		}
	}
}
