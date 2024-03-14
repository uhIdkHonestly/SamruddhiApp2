package com.samruddhi.trading.equities.services.base;

import com.samruddhi.trading.equities.domain.getordersbyid.GetOrdersByOrderIdResponse;

public interface GetOrdersByOrderIdService {
    // Get status of a single order
    public GetOrdersByOrderIdResponse getOrderFillStatus(String orderId) throws Exception;
}
