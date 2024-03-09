package com.samruddhi.trading.equities.dummy;

import com.samruddhi.trading.equities.domain.PlaceOrderPayload;

import java.util.HashMap;
import java.util.Map;

public class DummyStorage {

    private static final Map<String, PlaceOrderPayload> placeOrderPayloadMap = new HashMap<>();

    public static void addOrderIdToDummyStorage(String orderId, PlaceOrderPayload placeOrderPayload) {
        placeOrderPayloadMap.put(orderId, placeOrderPayload);
    }
}
