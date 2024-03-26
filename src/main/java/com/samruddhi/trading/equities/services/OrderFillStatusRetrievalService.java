package com.samruddhi.trading.equities.services;

import com.samruddhi.trading.equities.domain.getordersbyid.GetOrdersByOrderIdResponse;
import com.samruddhi.trading.equities.domain.getordersbyid.Leg;
import com.samruddhi.trading.equities.domain.getordersbyid.Order;
import com.samruddhi.trading.equities.domain.getordersbyid.OrderFillStatus;
import com.samruddhi.trading.equities.logic.OptionOrderFillStatus;
import com.samruddhi.trading.equities.services.base.GetOrdersByOrderIdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.samruddhi.trading.equities.domain.getordersbyid.OrderFillStatus.ORDER_FILL_STATUS_ACK;
import static com.samruddhi.trading.equities.domain.getordersbyid.OrderFillStatus.ORDER_FILL_STATUS_FAILED;
import static com.samruddhi.trading.equities.logic.OptionOrderFillStatus.ORDER_STATUS_FILLED;
import static com.samruddhi.trading.equities.logic.OptionOrderFillStatus.ORDER_STATUS_OPEN;

public class OrderFillStatusRetrievalService {

    private static final Logger logger = LoggerFactory.getLogger(OrderFillStatusRetrievalService.class);
    private static final int ORDER_FILL_RESPONSE_CHECK_TRIES = 4;
    private static final int ORDER_FILL_RESPONSE_WAIT_TIME = 60; // In millis

    private final GetOrdersByOrderIdService getOrdersByOrderIdService;

    public OrderFillStatusRetrievalService() {
        getOrdersByOrderIdService = new GetOrdersByOrderIdServiceImpl();
    }

    public OrderFillStatus waitForOrderFill(String orderId) throws Exception {

        int i = 0;
        while (i < ORDER_FILL_RESPONSE_CHECK_TRIES) {
            OrderFillStatus orderFillStatus = checkOrderFillStatus(orderId);
            if (orderFillStatus.getStatus() == OptionOrderFillStatus.ORDER_STATUS_FAILED || orderFillStatus.getStatus() == OptionOrderFillStatus.ORDER_STATUS_FILLED)
                return orderFillStatus;
            else if (orderFillStatus.getStatus() == OptionOrderFillStatus.ORDER_STATUS_OPEN) {
                logger.info("Order is still open , checking after a few milliseconds");
                Thread.sleep(ORDER_FILL_RESPONSE_WAIT_TIME);
            }
            i++;
        }
        return new OrderFillStatus("0", "FAILED", 0.0, 0, "");
    }

    public OrderFillStatus checkOrderFillStatus(String orderId) throws Exception {
        GetOrdersByOrderIdResponse getOrdersByOrderIdResponse = getOrdersByOrderIdService.getOrderFillStatus(orderId);
        if (getOrdersByOrderIdResponse.getErrors() != null && getOrdersByOrderIdResponse.getErrors().size() > 0) {
            return ORDER_FILL_STATUS_FAILED;
        } else if (getOrdersByOrderIdResponse.getOrders() != null && getOrdersByOrderIdResponse.getOrders().size() > 0) {
            Order order = getOrdersByOrderIdResponse.getOrders().get(0);
            // TO DO - Need to check and throw exception if no legs
            Leg leg = order.getLegs().get(0);
            if (order.getStatus() == ORDER_STATUS_FILLED) {
                return new OrderFillStatus(orderId, ORDER_STATUS_FILLED, order.getFilledPrice(), leg.getExecQuantity(), leg.getSymbol() + " " + leg.getStrikePrice() + " " + leg.getExpirationDate());
            } else if (order.getStatus().equals(ORDER_STATUS_OPEN)) {
                return new OrderFillStatus(orderId, ORDER_STATUS_OPEN, order.getFilledPrice(), leg.getExecQuantity(), leg.getSymbol() + " " + leg.getStrikePrice() + " " + leg.getExpirationDate());
            }
        }
        return ORDER_FILL_STATUS_ACK;
    }
}
