package com.samruddhi.trading.equities.services.base;


import com.samruddhi.trading.equities.domain.PlaceOrderPayload;
import com.samruddhi.trading.equities.domain.placeorder.PlaceOrderResponse;

public interface OrderService {

    public PlaceOrderResponse placeOrder(PlaceOrderPayload order) throws Exception;
    //To DO
    public PlaceOrderResponse updateOrder(String orderId, int quantity) throws Exception;

    public void cancelOrder(String orderId) throws Exception;
}
