package com.samruddhi.trading.equities.services.dummy;

import com.samruddhi.trading.equities.domain.PlaceOrderPayload;
import com.samruddhi.trading.equities.domain.getordersbyid.GetOrdersByOrderIdResponse;
import com.samruddhi.trading.equities.services.base.GetOrdersByOrderIdService;

import java.util.Map;

public class DummyGetOrdersByOrderIdServiceImpl implements GetOrdersByOrderIdService {


    @Override
    public GetOrdersByOrderIdResponse getOrderFillStatus(String orderId) throws Exception {

        // Get map of thread local...

        Map<String, PlaceOrderPayload> placedOrdersMap = DummyOrderServiceImpl.ordersMapThreadLocal.get();
        return null;
    }
}
