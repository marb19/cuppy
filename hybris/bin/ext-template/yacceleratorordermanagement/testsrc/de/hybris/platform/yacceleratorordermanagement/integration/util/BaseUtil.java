/*
 * [y] hybris Platform
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 */
package de.hybris.platform.yacceleratorordermanagement.integration.util;

import de.hybris.platform.acceleratorservices.payment.cybersource.enums.TransactionTypeEnum;
import de.hybris.platform.acceleratorservices.payment.data.OrderInfoData;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.enums.ProductDiscountGroup;
import de.hybris.platform.europe1.enums.ProductPriceGroup;
import de.hybris.platform.europe1.enums.ProductTaxGroup;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.DeliveryModeService;
import de.hybris.platform.ordercancel.OrderCancelService;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.dto.TransactionStatusDetails;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.definition.ProcessDefinitionFactory;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.refund.RefundService;
import de.hybris.platform.returns.ReturnService;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.pos.PointOfServiceService;
import de.hybris.platform.warehousing.allocation.AllocationService;
import de.hybris.platform.warehousing.cancellation.ConsignmentCancellationService;
import de.hybris.platform.warehousing.process.WarehousingBusinessProcessService;
import de.hybris.platform.warehousing.sourcing.SourcingService;
import de.hybris.platform.warehousing.sourcing.util.SourcingConfigurator;
import de.hybris.platform.warehousing.util.models.BaseStores;
import de.hybris.platform.warehousing.util.models.DeliveryModes;
import de.hybris.platform.warehousing.util.models.Orders;
import de.hybris.platform.warehousing.util.models.PaymentInfos;
import de.hybris.platform.warehousing.util.models.PointsOfService;
import de.hybris.platform.warehousing.util.models.Products;
import de.hybris.platform.warehousing.util.models.ReturnRequests;
import de.hybris.platform.warehousing.util.models.StockLevels;
import de.hybris.platform.warehousing.util.models.Users;
import de.hybris.platform.warehousing.util.models.Warehouses;
import de.hybris.platform.yacceleratorordermanagement.actions.order.CheckOrderAction;
import de.hybris.platform.yacceleratorordermanagement.actions.order.SourceOrderAction;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static de.hybris.platform.warehousing.data.sourcing.SourcingFactorIdentifiersEnum.ALLOCATION;
import static de.hybris.platform.warehousing.data.sourcing.SourcingFactorIdentifiersEnum.DISTANCE;
import static de.hybris.platform.warehousing.data.sourcing.SourcingFactorIdentifiersEnum.PRIORITY;
import static org.junit.Assert.assertEquals;



/**
 * this class is mainly to create and modify orders
 */
@Component
public class BaseUtil
{
	@Resource
	protected Orders orders;
	@Resource
	protected WarehousingBusinessProcessService<ConsignmentModel> consignmentBusinessProcessService;
	@Resource
	protected WarehousingBusinessProcessService<OrderModel> orderBusinessProcessService;
	@Resource
	protected WarehousingBusinessProcessService<ReturnRequestModel> returnBusinessProcessService;
	@Resource
	protected BusinessProcessService businessProcessService;
	@Resource
	protected UserService userService;
	@Resource
	protected OrderCancelService orderCancelService;
	@Resource
	protected ReturnService returnService;
	@Resource
	protected RefundService refundService;
	@Resource
	protected SourcingConfigurator sourcingConfigurator;
	@Resource
	protected ProductService productService;
	@Resource
	protected SourcingService sourcingService;
	@Resource
	protected StockService stockService;
	@Resource
	protected ModelService modelService;
	@Resource
	protected AllocationService allocationService;
	@Resource
	protected CalculationService calculationService;
	@Resource
	protected CommonI18NService commonI18NService;
	@Resource
	protected DeliveryModeService deliveryModeService;
	@Resource
	protected CartService cartService;
	@Resource
	protected PointOfServiceService pointOfServiceService;
	@Resource
	protected BaseStoreService baseStoreService;
	@Resource
	protected SourceOrderAction sourceOrderAction;
	@Resource
	protected CheckOrderAction checkOrderAction;
	@Resource
	protected CommerceStockService commerceStockService;
	@Resource
	protected BaseStores baseStores;
	@Resource
	protected PointsOfService pointsOfService;
	@Resource
	protected StockLevels stockLevels;
	@Resource
	protected Products products;
	@Resource
	protected Users users;
	@Resource
	protected PaymentInfos paymentInfos;
	@Resource
	protected ReturnRequests returnRequests;
	@Resource
	protected Warehouses warehouses;
	@Resource
	protected ConsignmentCancellationService consignmentCancellationService;
	@Resource
	protected CatalogVersionService catalogVersionService;
	@Resource
	protected ProcessDefinitionFactory processDefinitionFactory;
	@Resource
	protected DeliveryModes deliveryModes;

	protected DeliveryModeModel deliveryMode;

	protected OrderModel order;
	protected OrderModel order2;

	protected static final Long CAMERA_QTY = Long.valueOf(3L);
	protected static final Long MEMORY_CARD_QTY = Long.valueOf(2L);
	protected static final String CAMERA_CODE = "camera";
	protected static final String MEMORY_CARD_CODE = "memorycard";
	protected static final String LENS_CODE = "lens";

	public void saveAll()
	{
		modelService.saveAll();
	}

	public ModelService getModelService()
	{
		return modelService;
	}


	public void setCalculatedStatus(final AbstractOrderModel order)
	{
		order.setCalculated(Boolean.TRUE);
		getModelService().save(order);
		final List<AbstractOrderEntryModel> entries = order.getEntries();
		if (entries != null)
		{
			for (final AbstractOrderEntryModel entry : entries)
			{
				entry.setCalculated(Boolean.TRUE);
			}
			getModelService().saveAll(entries);
		}
	}

	public void setSourcingFactors(final int allocation, final int distance, final int priority)
	{
		sourcingConfigurator.setFactorWeight(ALLOCATION, allocation);
		sourcingConfigurator.setFactorWeight(DISTANCE, distance);
		sourcingConfigurator.setFactorWeight(PRIORITY, priority);
		saveAll();
	}

	public void assertProcessState(final BusinessProcessModel process, final ProcessState state)
	{
		getModelService().refresh(process);
		assertEquals("Process state", state, process.getState());
	}

	public OrderModel createCameraShippedOrder()
	{
		return createOrder(orders.Camera_Shipped(CAMERA_QTY));
	}

	public OrderModel createCameraPickUpOrder()
	{
		return createOrder(orders.Camera_PickupInMontreal(CAMERA_QTY));
	}

	public OrderModel createCameraAndMemoryCardShippingOrder()
	{
		return createOrder(orders.CameraAndMemoryCard_Shipped(CAMERA_QTY, MEMORY_CARD_QTY));
	}

	public OrderModel createOrder(OrderModel orderModel)
	{
		order = orderModel;
		order.setDeliveryMode(deliveryModes.standardShipment());
		modelService.saveAll();
		return order;
	}

	public void refreshOrder(final OrderModel orderModel)
	{
		modelService.refresh(orderModel);
		orderModel.getConsignments().stream().flatMap(cons -> cons.getConsignmentEntries().stream())
				.forEach(conse -> getModelService().refresh(conse));
		orderModel.getEntries().forEach(e -> getModelService().refresh(e));
		getModelService().refresh(orderModel.getStatus());
	}

	/**
	 * set dummy OrderTransaction into order
	 *
	 * @param orderModel
	 * @return
	 */
	public List<PaymentTransactionModel> setDummyOrderTransaction(final OrderModel orderModel)
	{
		OrderInfoData data = new OrderInfoData();
		data.setOrderNumber(orderModel.getCode());
		data.setOrderPageIgnoreAVS(Boolean.TRUE);
		data.setOrderPageIgnoreCVN(Boolean.TRUE);
		data.setOrderPageTransactionType(TransactionTypeEnum.authorization.name());
		PaymentTransactionEntryModel paymentTransactionEntry = new PaymentTransactionEntryModel();
		paymentTransactionEntry.setAmount(BigDecimal.valueOf(120));
		paymentTransactionEntry.setCode(UUID.randomUUID().toString().replaceAll("-", ""));
		paymentTransactionEntry.setType(PaymentTransactionType.AUTHORIZATION);
		paymentTransactionEntry.setTransactionStatus(TransactionStatus.ACCEPTED.name());
		paymentTransactionEntry.setTransactionStatusDetails(TransactionStatusDetails.SUCCESFULL.name());
		paymentTransactionEntry.setCurrency(order.getCurrency());

		List<PaymentTransactionEntryModel> paymentTransactionEntryList = new ArrayList<>();
		List<PaymentTransactionModel> paymentTransactionModelList = new ArrayList<>();
		paymentTransactionEntryList.add(paymentTransactionEntry);

		PaymentTransactionModel paymentTransactionModel = new PaymentTransactionModel();
		paymentTransactionModel.setCurrency(orderModel.getCurrency());
		paymentTransactionModel.setEntries(paymentTransactionEntryList);
		paymentTransactionModel.setPlannedAmount(BigDecimal.valueOf(120));
		paymentTransactionModel.setOrder(orderModel);
		paymentTransactionModel.setRequestId("Mockup");
		paymentTransactionModel.setRequestToken("Mockup");
		paymentTransactionModel.setPaymentProvider("Mockup");
		paymentTransactionModel.setCode(UUID.randomUUID().toString().replaceAll("-", ""));
		paymentTransactionModelList.add(paymentTransactionModel);

		order.getEntries().stream().forEach(p -> {
			p.setBasePrice(Double.valueOf(10));
			p.setCalculated(true);
			getModelService().save(p);
		});

		getModelService().save(paymentTransactionModel);
		return paymentTransactionModelList;
	}

	/**
	 * set dummy priceRowModel
	 *
	 * @param product
	 */
	public void setDummyPriceRowModel(ProductModel product)
	{
		final PriceRowModel lower = buildPriceRow(1.1, product);
		final PriceRowModel higher = buildPriceRow(2.2, product);
		final List<PriceRowModel> europe1 = Arrays.asList(new PriceRowModel[]
				{ higher, lower });
		product.setEurope1Prices(europe1);
		product.setEurope1PriceFactory_PPG(ProductPriceGroup.valueOf("22"));
		product.setEurope1PriceFactory_PTG(ProductTaxGroup.valueOf("22"));
		product.setEurope1PriceFactory_PDG(ProductDiscountGroup.valueOf("22"));
		product.getPriceQuantity();
		getModelService().save(product);
	}

	public OrderEntryModel getOrderEntryModel_Camera(AbstractOrderModel order)
	{
		return ((OrderEntryModel)order.getEntries().stream().filter(entry -> entry.getProduct().getCode().equals(CAMERA_CODE)).findFirst().get());
	}

	public OrderEntryModel getOrderEntryModel_MemoryCard(AbstractOrderModel order)
	{
		return (OrderEntryModel)order.getEntries().stream().filter(entry -> entry.getProduct().getCode().equals(MEMORY_CARD_CODE)).findFirst().get();
	}

	private PriceRowModel buildPriceRow(final double value, final ProductModel product)
	{
		final PriceRowModel price = new PriceRowModel();
		price.setPrice(Double.valueOf(value));
		price.setCurrency(order.getCurrency());
		price.setUnit(product.getUnit());
		modelService.save(price);
		return price;
	}

	public WarehousingBusinessProcessService<OrderModel> getOrderBusinessProcessService()
	{
		return orderBusinessProcessService;
	}

	public void setOrderBusinessProcessService(WarehousingBusinessProcessService<OrderModel> orderBusinessProcessService)
	{
		this.orderBusinessProcessService = orderBusinessProcessService;
	}

	public UserService getUserService()
	{
		return userService;
	}

	public void setUserService(UserService userService)
	{
		this.userService = userService;
	}

	public Orders getOrders()
	{
		return orders;
	}

	public Users getUsers()
	{
		return users;
	}

	public void setUsers(Users users)
	{
		this.users = users;
	}

	public BaseStores getBaseStores()
	{
		return baseStores;
	}

	public void setBaseStores(BaseStores baseStores)
	{
		this.baseStores = baseStores;
	}

	public PointsOfService getPointsOfService()
	{
		return pointsOfService;
	}

	public void setPointsOfService(PointsOfService pointsOfService)
	{
		this.pointsOfService = pointsOfService;
	}

	public SourcingConfigurator getSourcingConfigurator()
	{
		return sourcingConfigurator;
	}

	public Warehouses getWarehouses()
	{
		return warehouses;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public ProductService getProductService()
	{
		return productService;
	}

	protected SourcingService getSourcingService()
	{
		return sourcingService;
	}

	public void setSourcingService(SourcingService sourcingService)
	{
		this.sourcingService = sourcingService;
	}

	public AllocationService getAllocationService()
	{
		return allocationService;
	}

	public void setAllocationService(AllocationService allocationService)
	{
		this.allocationService = allocationService;
	}

	public CalculationService getCalculationService()
	{
		return calculationService;
	}

	public void setCalculationService(CalculationService calculationService)
	{
		this.calculationService = calculationService;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	public void setCommonI18NService(CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	public DeliveryModeService getDeliveryModeService()
	{
		return deliveryModeService;
	}

	public CartService getCartService()
	{
		return cartService;
	}

	public PointOfServiceService getPointOfServiceService()
	{
		return pointOfServiceService;
	}

	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	public CommerceStockService getCommerceStockService()
	{
		return commerceStockService;
	}

	public CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	public ProcessDefinitionFactory getProcessDefinitionFactory()
	{
		return processDefinitionFactory;
	}

	public DeliveryModeModel getDeliveryMode()
	{
		return deliveryMode;
	}

	public DeliveryModes getDeliveryModes()
	{
		return deliveryModes;
	}

	public OrderModel getOrder()
	{
		return order;
	}

	public Products getProducts()
	{
		return products;
	}

	public PaymentInfos getPaymentInfos()
	{
		return paymentInfos;
	}

	public ReturnRequests getReturnRequests()
	{
		return returnRequests;
	}

	public StockLevels getStockLevels()
	{
		return stockLevels;
	}

	public RefundService getRefundService()
	{
		return refundService;
	}

	public ReturnService getReturnService()
	{
		return returnService;
	}

	public OrderCancelService getOrderCancelService()
	{
		return orderCancelService;
	}

	public StockService getStockService()
	{
		return stockService;
	}

	public void setOrders(Orders orders)
	{
		this.orders = orders;
	}

	public void setConsignmentBusinessProcessService(
			WarehousingBusinessProcessService<ConsignmentModel> consignmentBusinessProcessService)
	{
		this.consignmentBusinessProcessService = consignmentBusinessProcessService;
	}

	public void setReturnBusinessProcessService(
			WarehousingBusinessProcessService<ReturnRequestModel> returnBusinessProcessService)
	{
		this.returnBusinessProcessService = returnBusinessProcessService;
	}

	public void setBusinessProcessService(BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

	public void setOrderCancelService(OrderCancelService orderCancelService)
	{
		this.orderCancelService = orderCancelService;
	}

	public void setReturnService(ReturnService returnService)
	{
		this.returnService = returnService;
	}

	public void setRefundService(RefundService refundService)
	{
		this.refundService = refundService;
	}

	public void setSourcingConfigurator(SourcingConfigurator sourcingConfigurator)
	{
		this.sourcingConfigurator = sourcingConfigurator;
	}

	public void setProductService(ProductService productService)
	{
		this.productService = productService;
	}

	public void setStockService(StockService stockService)
	{
		this.stockService = stockService;
	}

	public void setDeliveryModeService(DeliveryModeService deliveryModeService)
	{
		this.deliveryModeService = deliveryModeService;
	}

	public void setCartService(CartService cartService)
	{
		this.cartService = cartService;
	}

	public void setPointOfServiceService(PointOfServiceService pointOfServiceService)
	{
		this.pointOfServiceService = pointOfServiceService;
	}

	public void setBaseStoreService(BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	public void setSourceOrderAction(SourceOrderAction sourceOrderAction)
	{
		this.sourceOrderAction = sourceOrderAction;
	}

	public void setCheckOrderAction(CheckOrderAction checkOrderAction)
	{
		this.checkOrderAction = checkOrderAction;
	}

	public void setCommerceStockService(CommerceStockService commerceStockService)
	{
		this.commerceStockService = commerceStockService;
	}

	public void setStockLevels(StockLevels stockLevels)
	{
		this.stockLevels = stockLevels;
	}

	public void setProducts(Products products)
	{
		this.products = products;
	}

	public void setPaymentInfos(PaymentInfos paymentInfos)
	{
		this.paymentInfos = paymentInfos;
	}

	public void setReturnRequests(ReturnRequests returnRequests)
	{
		this.returnRequests = returnRequests;
	}

	public void setWarehouses(Warehouses warehouses)
	{
		this.warehouses = warehouses;
	}

	public void setConsignmentCancellationService(
			ConsignmentCancellationService consignmentCancellationService)
	{
		this.consignmentCancellationService = consignmentCancellationService;
	}

	public void setCatalogVersionService(CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	public void setProcessDefinitionFactory(
			ProcessDefinitionFactory processDefinitionFactory)
	{
		this.processDefinitionFactory = processDefinitionFactory;
	}

	public void setDeliveryModes(DeliveryModes deliveryModes)
	{
		this.deliveryModes = deliveryModes;
	}

	public void setDeliveryMode(DeliveryModeModel deliveryMode)
	{
		this.deliveryMode = deliveryMode;
	}

	public void setOrder(OrderModel order)
	{
		this.order = order;
	}


}

