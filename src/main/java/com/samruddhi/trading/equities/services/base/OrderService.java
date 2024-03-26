package com.samruddhi.trading.equities.services.base;


import com.samruddhi.trading.equities.domain.PlaceOrderPayload;
import com.samruddhi.trading.equities.domain.placeorder.PlaceOrderResponse;
import com.samruddhi.trading.equities.domain.updateorder.UpdateOrderResponse;

public interface OrderService {

    public PlaceOrderResponse placeOrder(PlaceOrderPayload order) throws Exception;

    public UpdateOrderResponse updateOrder(int quantity, String orderId, double limitPrice) throws Exception;

    public void cancelOrder(String orderId) throws Exception;
}
