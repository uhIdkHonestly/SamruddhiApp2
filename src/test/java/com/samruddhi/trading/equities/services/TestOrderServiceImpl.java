package com.samruddhi.trading.equities.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.samruddhi.trading.equities.domain.placeorder.PlaceOrderResponse;
import common.JsonParser;
import org.junit.Test;

import static org.junit.Assert.fail;

public class TestOrderServiceImpl {


    @Test
    public void testPlaceOrderResponseParsing() {

        String orderResponse = """
                    {
                      "Orders": [
                        {
                          "Message": "Sent order: Sell 1 MSFT @ Market",
                          "OrderID": "286179829"
                        }
                      ] 
                    }
                """;
        try {
            PlaceOrderResponse placeOrderResponse = JsonParser.getPlaceOrderResponse(orderResponse);
            assertTrue(!placeOrderResponse.getOrders().isEmpty());
            assertEquals(placeOrderResponse.getOrders().get(0).getOrderId(), "286179829");
            assertEquals(placeOrderResponse.getOrders().get(0).getMessage(), "Sent order: Sell 1 MSFT @ Market");
        } catch (Exception e) {
            fail();
        }
    }


    @Test
    public void testPlaceOrderResponseWithErrorsParsing() {

        String orderResponse = """
            {
              "Orders": [
                {
                  "Message": "Sent order: Sell 1 MSFT @ Market",
                  "OrderID": "286179829"
                },
                {
                  "Message": "Sent order: Sell 1 MSFT @ 232.86 Stop Market",
                  "OrderID": "286179830"
                },
                {
                  "Message": "Sent order: Buy 1 MSFT @ Market",
                  "OrderID": "286179831"
                }
              ],
              "Errors": [
                {
                  "Error": "FAILED",
                  "Message": "Order failed. Reason: Type = LMT has invalid Price greater than absolute maximum",
                  "OrderID": "1234567"
                }
              ]
            }
            """;
        try {
            PlaceOrderResponse placeOrderResponse = JsonParser.getPlaceOrderResponse(orderResponse);
            assertTrue(!placeOrderResponse.getErrors().isEmpty());
            assertEquals(placeOrderResponse.getErrors().get(0).getOrderID(), "1234567");
            assertEquals(placeOrderResponse.getErrors().get(0).getMessage(), "Order failed. Reason: Type = LMT has invalid Price greater than absolute maximum");
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
