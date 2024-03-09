package com.samruddhi.trading.equities.services;

import com.samruddhi.trading.equities.domain.PlaceOrderPayload;
import com.samruddhi.trading.equities.domain.placeorder.Order;
import com.samruddhi.trading.equities.domain.placeorder.PlaceOrderResponse;
import com.samruddhi.trading.equities.domain.updateorder.UpdateOrderResponse;
import com.samruddhi.trading.equities.dummy.DummyStorage;
import com.samruddhi.trading.equities.services.base.OrderService;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DummyOrderServiceImpl implements OrderService {

    private static AtomicInteger orderIdCter = new AtomicInteger(1);

    @Override
    public PlaceOrderResponse placeOrder(PlaceOrderPayload placeOrderPayload) throws Exception {
        PlaceOrderResponse placeOrderResponse = new PlaceOrderResponse();

        String orderId = orderIdCter.getAndIncrement() + "";
        DummyStorage.addOrderIdToDummyStorage(orderId, placeOrderPayload);
        Order orderData = new Order("Order placed", orderId);

        placeOrderResponse.setOrders(List.of(orderData));
        placeOrderResponse.setErrors(Collections.emptyList());

        return placeOrderResponse;
    }

    @Override
    public UpdateOrderResponse updateOrder(String orderId, double limitPrice) throws Exception {
        return new UpdateOrderResponse();
    }

    @Override
    public void cancelOrder(String orderId) throws Exception {

    }
}
