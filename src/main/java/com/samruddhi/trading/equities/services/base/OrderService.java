package com.samruddhi.trading.equities.services.base;

import com.samruddhi.trading.equities.domain.Order;

public interface OrderService {

    public void placeOrder(Order order) throws Exception;
}
