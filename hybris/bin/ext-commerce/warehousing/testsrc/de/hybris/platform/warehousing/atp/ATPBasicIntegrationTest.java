/*
 *
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
 */
package de.hybris.platform.warehousing.atp;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.warehousing.atp.services.WarehouseStockService;
import de.hybris.platform.warehousing.atp.strategy.StockLevelSelectionStrategy;
import de.hybris.platform.warehousing.util.BaseWarehousingIntegrationTest;
import de.hybris.platform.warehousing.util.models.AllocationEvents;
import de.hybris.platform.warehousing.util.models.BaseStores;
import de.hybris.platform.warehousing.util.models.PointsOfService;
import de.hybris.platform.warehousing.util.models.Products;
import de.hybris.platform.warehousing.util.models.StockLevels;
import de.hybris.platform.warehousing.util.models.Warehouses;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@IntegrationTest
public class ATPBasicIntegrationTest extends BaseWarehousingIntegrationTest
{
	private static final Long MONTREAL_CAMERA_QTY = Long.valueOf(50);
	private static final Long BOSTON_CAMERA_QTY = Long.valueOf(25);
	private static final Long ALLOCATED_CAMERA_QTY = Long.valueOf(10);

	@Resource
	private CommerceStockService commerceStockService;

	@Resource
	private Products products;
	@Resource
	private BaseStores baseStores;
	@Resource
	private PointsOfService pointsOfService;
	@Resource
	private StockLevels stockLevels;
	@Resource
	private Warehouses warehouses;
	@Resource
	private AllocationEvents allocationEvents;
	@Resource
	private WarehouseStockService warehouseStockService;
	@Resource
	private StockService stockService;
	@Resource
	private ModelService modelService;
	@Resource
	private StockLevelSelectionStrategy stockLevelSelectionStrategy;

	private StockLevelModel stockLevelMontreal;
	private StockLevelModel stockLevelBoston;
	private ConsignmentEntryModel consignmentEntry;


	@Before
	public void setUp()
	{
		final WarehouseModel warehouseMontreal = warehouses.Montreal();
		warehouseMontreal.setPointsOfService(Arrays.asList(pointsOfService.Montreal_Downtown()));
		warehouseMontreal.setBaseStores(Arrays.asList(baseStores.NorthAmerica()));
		final WarehouseModel warehouseBoston = warehouses.Boston();
		warehouseBoston.setPointsOfService(Arrays.asList(pointsOfService.Boston()));
		warehouseBoston.setBaseStores(Arrays.asList(baseStores.NorthAmerica()));
		stockLevels.Camera(warehouseMontreal, MONTREAL_CAMERA_QTY.intValue());
		stockLevels.Camera(warehouseBoston, BOSTON_CAMERA_QTY.intValue());
		stockLevelMontreal = stockLevels.Camera(warehouseMontreal, ALLOCATED_CAMERA_QTY.intValue());
		stockLevelBoston = stockLevels.Camera(warehouseBoston, ALLOCATED_CAMERA_QTY.intValue());
	}

	@Test
	public void getDefault_Atp()
	{
		// When
		final Long globalAtp = commerceStockService.getStockLevelForProductAndBaseStore(products.Camera(),
				baseStores.NorthAmerica());
		final Long posAtp = commerceStockService.getStockLevelForProductAndPointOfService(products.Camera(),
				pointsOfService.Montreal_Downtown());
		final Long warehouseAtp = warehouseStockService.getStockLevelForProductAndWarehouse(
				products.Camera(), warehouses.Montreal());

		// Then
		assertGlobalAtp(globalAtp, Long.valueOf(0));
		assertEquals(MONTREAL_CAMERA_QTY, posAtp);
		assertEquals(MONTREAL_CAMERA_QTY, warehouseAtp);
	}

	@Test
	public void shouldGetATPForBaseStoreWithThreshold()
	{
		// Given
		final StockLevelModel stockLevel = warehouses.Montreal().getStockLevels().iterator().next();
		stockLevel.setReserved(-9);
		modelService.save(stockLevel);

		// When
		final Long globalAtp = commerceStockService.getStockLevelForProductAndBaseStore(products.Camera(),
				baseStores.NorthAmerica());

		// Then
		assertGlobalAtp(globalAtp, Long.valueOf(-9));
	}

	@Test
	public void shouldDecreaseStockLevel_AllocateCameraFrom1Pos1Warehouse()
	{
		// Given
		allocationEvents.Camera_ShippedFromMontrealToMontrealNancyHome(ALLOCATED_CAMERA_QTY, stockLevelMontreal);

		// When
		final Long globalAtp = commerceStockService.getStockLevelForProductAndBaseStore(products.Camera(),
				baseStores.NorthAmerica());
		final Long localAtp = commerceStockService.getStockLevelForProductAndPointOfService(products.Camera(),
				pointsOfService.Montreal_Downtown());

		// Then
		assertGlobalAtp(globalAtp, ALLOCATED_CAMERA_QTY);
		assertEquals(Long.valueOf(MONTREAL_CAMERA_QTY - ALLOCATED_CAMERA_QTY), localAtp);
	}

	@Test
	public void shouldDecreaseStockLevel_AllocateCameraFrom2Pos2Warehouse()
	{
		// Given
		allocationEvents.Camera_ShippedFromMontrealToMontrealNancyHome(ALLOCATED_CAMERA_QTY, stockLevelMontreal);
		allocationEvents.Camera_ShippedFromBostonToMontrealNancyHome(ALLOCATED_CAMERA_QTY, stockLevelBoston);

		// When
		final Long globalAtp = commerceStockService.getStockLevelForProductAndBaseStore(products.Camera(),
				baseStores.NorthAmerica());
		final Long localMontrealAtp = commerceStockService.getStockLevelForProductAndPointOfService(products.Camera(),
				pointsOfService.Montreal_Downtown());
		final Long localBostonAtp = commerceStockService.getStockLevelForProductAndPointOfService(products.Camera(),
				pointsOfService.Boston());

		// Then
		assertGlobalAtp(globalAtp, ALLOCATED_CAMERA_QTY * 2);
		assertEquals(Long.valueOf(MONTREAL_CAMERA_QTY - ALLOCATED_CAMERA_QTY), localMontrealAtp);
		assertEquals(Long.valueOf(BOSTON_CAMERA_QTY - ALLOCATED_CAMERA_QTY), localBostonAtp);
	}

	@Test
	@Ignore
	//TODO complete after cancellation refactor
	public void shouldGetDifferentATPForBaseStore_NewCancellationEvent()
	{
		// Given
		//createNewCancellationEvt(warehouse, product, 576L);

		// When
		final Long globalAtp = commerceStockService.getStockLevelForProductAndBaseStore(products.Camera(),
				baseStores.NorthAmerica());

		// Then
		assertGlobalAtp(globalAtp, Long.valueOf(0));
	}

	@Test
	public void shouldGetLessATP_StockLevelForcedOutOfStock()
	{
		// When
		final Long localBostonAtp_BeforeForcingOutOfStock = commerceStockService
				.getStockLevelForProductAndPointOfService(products.Camera(), pointsOfService.Boston());
		stockLevelBoston.setInStockStatus(InStockStatus.FORCEOUTOFSTOCK);
		final Long localBostonAtp_AfterForcingOutOfStock = commerceStockService
				.getStockLevelForProductAndPointOfService(products.Camera(), pointsOfService.Boston());

		// Then
		assertEquals(Long.valueOf(25), localBostonAtp_BeforeForcingOutOfStock);
		assertEquals(Long.valueOf(0), localBostonAtp_AfterForcingOutOfStock);
		assertTrue(localBostonAtp_AfterForcingOutOfStock < localBostonAtp_BeforeForcingOutOfStock);
	}

	@Test
	public void shouldGetNewATP_StockLevelForcedOutOfStock_UpdateStockLevel()
	{
		// When
		final Long localBostonAtp_BeforeForcingOutOfStock = commerceStockService
				.getStockLevelForProductAndPointOfService(products.Camera(), pointsOfService.Boston());
		stockLevelBoston.setInStockStatus(InStockStatus.FORCEOUTOFSTOCK);
		stockService.updateActualStockLevel(products.Camera(), warehouses.Boston(), 5, "");
		final Long localBostonAtp_AfterForcingOutOfStock = commerceStockService
				.getStockLevelForProductAndPointOfService(products.Camera(), pointsOfService.Boston());

		// Then
		assertEquals(Long.valueOf(25), localBostonAtp_BeforeForcingOutOfStock);
		//force out of stock is based on the stock level, warehouse can have multi stock level for same product
		assertEquals(Long.valueOf(5), localBostonAtp_AfterForcingOutOfStock);
	}


	@Test
	public void shouldGetATPNull_StockLevelForcedInStock()
	{
		// When
		final Long localBostonAtp_BeforeForcingInOfStock = commerceStockService
				.getStockLevelForProductAndPointOfService(products.Camera(), pointsOfService.Boston());
		stockLevelBoston.setInStockStatus(InStockStatus.FORCEINSTOCK);
		final Long globalBostonAtp_AfterForcingInStock = commerceStockService.getStockLevelForProductAndBaseStore(products.Camera(),
				baseStores.NorthAmerica());
		final Long localBostonWarehouseAtp_AfterForcingInStock = warehouseStockService.getStockLevelForProductAndWarehouse(
				products.Camera(), warehouses.Boston());
		final Long localBostonPosAtp_AfterForcingInStock = commerceStockService
				.getStockLevelForProductAndPointOfService(products.Camera(), pointsOfService.Boston());

		// Then
		assertEquals(Long.valueOf(25), localBostonAtp_BeforeForcingInOfStock);
		assertEquals(null, globalBostonAtp_AfterForcingInStock);
		assertEquals(null, localBostonWarehouseAtp_AfterForcingInStock);
		assertEquals(null, localBostonPosAtp_AfterForcingInStock);
	}

	private void assertGlobalAtp(final Long globalAtp, final Long allocated)
	{
		assertEquals(Long.valueOf(MONTREAL_CAMERA_QTY + BOSTON_CAMERA_QTY - allocated), globalAtp);
	}
}
