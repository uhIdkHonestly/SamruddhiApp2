package com.samruddhi.trading.equities.services.dummy;

import com.samruddhi.trading.equities.domain.PlaceOrderPayload;
import com.samruddhi.trading.equities.domain.getordersbyid.GetOrdersByOrderIdResponse;
import com.samruddhi.trading.equities.domain.getordersbyid.Leg;
import com.samruddhi.trading.equities.domain.getordersbyid.Order;
import com.samruddhi.trading.equities.services.base.GetOrdersByOrderIdService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.samruddhi.trading.equities.logic.OptionOrderFillStatus.ORDER_STATUS_FILLED;

public class DummyGetOrdersByOrderIdServiceImpl implements GetOrdersByOrderIdService {

    @Override
    /** Create Dummy Order fillig so that we can validate Uptrend based Call but and seling on Downtrend
     * and vice versa without readlling placing orders at TS to ensure our indicator based BUY and SELL orders
     * could realy be profitable
     */
    public GetOrdersByOrderIdResponse getOrderFillStatus(String orderId) throws Exception {

        // Get map of thread local (orderId, PlaceOrderPayload)
        Map<String, PlaceOrderPayload> placedOrdersMap = DummyOrderServiceImpl.ordersMapThreadLocal.get();

        PlaceOrderPayload payload = placedOrdersMap.get(orderId);
        GetOrdersByOrderIdResponse ordersByOrderIdResponse = new GetOrdersByOrderIdResponse();

        List<Leg> legs = new ArrayList<>();
        Leg leg = new Leg();
        //leg.setExpirationDate();
        leg.setExecutionPrice(payload.getLimitPrice() + "");
        leg.setQuantityOrdered(String.valueOf(2));
        leg.setSymbol(payload.getSymbol());
        legs.add(leg);

        Order order = new Order();
        order.setStatus("FLL");
        order.setOrderId(orderId);
        order.setCurrency("USD");
        order.setFilledPrice(payload.getLimitPrice() + "");
        order.setLegs(legs);


        //Set order into response and no error
        ordersByOrderIdResponse.setOrders(List.of(order));
        ordersByOrderIdResponse.setErrors(Collections.emptyList());
        return ordersByOrderIdResponse;
    }
}
