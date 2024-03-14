package com.samruddhi.trading.equities.services.dummy;

import com.samruddhi.trading.equities.domain.PlaceOrderPayload;
import com.samruddhi.trading.equities.domain.getordersbyid.GetOrdersByOrderIdResponse;
import com.samruddhi.trading.equities.domain.placeorder.Order;
import com.samruddhi.trading.equities.domain.placeorder.PlaceOrderResponse;
import com.samruddhi.trading.equities.domain.updateorder.UpdateOrderResponse;
import com.samruddhi.trading.equities.dummy.DummyStorage;
import com.samruddhi.trading.equities.services.base.OrderService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DummyOrderServiceImpl implements OrderService {

    private static AtomicInteger orderIdCter = new AtomicInteger(1);

    public static final ThreadLocal<Map<String, PlaceOrderPayload>> ordersMapThreadLocal = new ThreadLocal<Map<String, PlaceOrderPayload>>();

    @Override
    public PlaceOrderResponse placeOrder(PlaceOrderPayload placeOrderPayload) throws Exception {
        PlaceOrderResponse placeOrderResponse = new PlaceOrderResponse();

        String orderId = orderIdCter.getAndIncrement() + "";
        DummyStorage.addOrderIdToDummyStorage(orderId, placeOrderPayload);
        Order orderData = new Order("Order placed", orderId);

        placeOrderResponse.setOrders(List.of(orderData));
        placeOrderResponse.setErrors(Collections.emptyList());
        //we need this later for building GetOrdersByOrderIdResponse
        ordersMapThreadLocal.get().put(orderId, placeOrderPayload);

        return placeOrderResponse;
    }

    @Override
    public UpdateOrderResponse updateOrder(String orderId, double limitPrice) throws Exception {
        return new UpdateOrderResponse("", orderId);
    }

    @Override
    public void cancelOrder(String orderId) throws Exception {

    }
}
