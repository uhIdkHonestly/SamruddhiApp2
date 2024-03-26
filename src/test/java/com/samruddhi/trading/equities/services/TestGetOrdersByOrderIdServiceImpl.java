package com.samruddhi.trading.equities.services;

import com.samruddhi.trading.equities.domain.getordersbyid.GetOrdersByOrderIdResponse;
import com.samruddhi.trading.equities.domain.getordersbyid.Order;
import com.samruddhi.trading.equities.domain.placeorder.PlaceOrderResponse;
import com.samruddhi.trading.equities.domain.updateorder.UpdateOrderResponse;
import common.JsonParser;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestGetOrdersByOrderIdServiceImpl {

    @Test
    public void tesGetOrdersByOrderIdResponseParsing() {

        String getOrdersByOrderIdResponse = """
                  {
                       "Orders": [
                         {
                           "AccountID": "123456782",
                           "CommissionFee": "0",
                           "Currency": "USD",
                           "Duration": "GTC",
                           "GoodTillDate": "2021-05-25T00:00:00Z",
                           "Legs": [
                             {
                               "AssetType": "STOCK",
                               "BuyOrSell": "Buy",
                               "ExecQuantity": "0",
                               "ExecutionPrice": "112.28",
                               "ExpirationDate": "2021-05-25T00:00:00Z",
                               "OpenOrClose": "Open",
                               "OptionType": "CALL",
                               "QuantityOrdered": "10",
                               "QuantityRemaining": "10",
                               "StrikePrice": "350",
                               "Symbol": "MSFT",
                               "Underlying": "MSFT"
                             }
                           ],
                           "MarketActivationRules": [
                             {
                               "RuleType": "Price",
                               "Symbol": "EDZ22",
                               "Predicate": "gt",
                               "TriggerKey": "STTN",
                               "Price": "10000.01"
                             }
                           ],
                           "OrderID": "286234131",
                           "OpenedDateTime": "2021-02-24T15:47:45Z",
                           "OrderType": "Market",
                           "PriceUsedForBuyingPower": "230.46",
                           "Routing": "Intelligent",
                           "Status": "OPN",
                           "StatusDescription": "Sent",
                           "AdvancedOptions": "CND=EDZ22>10000.01(STTN);TIM=23:59:59;",
                           "TimeActivationRules": [
                             {
                               "TimeUtc": "0001-01-01T23:59:59Z"
                             }
                           ],
                           "UnbundledRouteFee": "0"
                         },
                         {
                           "AccountID": "123456782",
                           "CommissionFee": "0",
                           "ClosedDateTime": "2020-11-16T16:53:37Z",
                           "Currency": "USD",
                           "Duration": "GTC",
                           "FilledPrice": "216.68",
                           "GoodTillDate": "2021-02-14T00:00:00Z",
                           "Legs": [
                             {
                               "OpenOrClose": "Open",
                               "QuantityOrdered": "10",
                               "ExecQuantity": "10",
                               "QuantityRemaining": "0",
                               "BuyOrSell": "Buy",
                               "Symbol": "MSFT",
                               "AssetType": "STOCK"
                             }
                           ],
                           "OrderID": "123456789",
                           "OpenedDateTime": "2020-11-16T16:53:37Z",
                           "OrderType": "Market",
                           "PriceUsedForBuyingPower": "216.66",
                           "Routing": "Intelligent",
                           "Status": "FLL",
                           "StatusDescription": "Filled",
                           "UnbundledRouteFee": "0"
                         } 
                       ],
                       "Errors": []
                     }
                """;
        try {
            GetOrdersByOrderIdResponse placeOrderResponse = JsonParser.getOrdersByIdResponse(getOrdersByOrderIdResponse);
            assertTrue(!placeOrderResponse.getOrders().isEmpty());
            Order order = placeOrderResponse.getOrders().get(0);
            assertEquals(order.getAccountId(), "123456782");
            assertEquals(order.getStatus(), "OPN");
            assertEquals(order.getLegs().get(0).getUnderlying(), "MSFT");
            assertEquals(order.getLegs().get(0).getStrikePrice(), "350");
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void tesReplaceOrdersByOrderIdResponseParsing() {
        String replaceOrdersByOrderIdResponse = """
                {
                "Message": "Cancel/Replace order sent.",
                "OrderID": "123456789"
                }
                """;
        try {
            UpdateOrderResponse updateOrderResponse = JsonParser.getUpdateOrderResponse(replaceOrdersByOrderIdResponse);

            assertEquals(updateOrderResponse.getOrderID(), "123456789");
            assertEquals(updateOrderResponse.getMessage(), "Cancel/Replace order sent.");
        } catch (Exception e) {
            fail();
        }
    }
}
