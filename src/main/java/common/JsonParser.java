package common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samruddhi.trading.equities.domain.Bar;
import com.samruddhi.trading.equities.domain.BarRoot;
import com.samruddhi.trading.equities.domain.OptionData;
import com.samruddhi.trading.equities.domain.getorders.GetOrdersResponse;
import com.samruddhi.trading.equities.domain.getordersbyid.GetOrdersByOrderIdResponse;
import com.samruddhi.trading.equities.domain.placeorder.PlaceOrderResponse;
import com.samruddhi.trading.equities.domain.updateorder.UpdateOrderResponse;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class JsonParser {

    public static Optional<String> getJsonTagValue(String jsonString, String key) {

        Optional<String> tagValue = Optional.empty();
        try {
            // Create an ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            // Parse the JSON string into a JsonNode
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            // Extract the value associated with the "tag" key
            String value = jsonNode.get(key).asText();
            tagValue = Optional.of(value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return tagValue;
    }

    public static List<Bar> getListOfBars(String jsonString) {


        List<Bar> bars = Collections.emptyList();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Create an ObjectMapper
            BarRoot barsWrapper = objectMapper.readValue(jsonString, BarRoot.class);
            bars = barsWrapper.getBars();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return bars;
    }

    public static OptionData getOptionQuote(String optionJsonString) throws JsonProcessingException {
        OptionData optionData = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            optionData = mapper.readValue(optionJsonString, OptionData.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return optionData;
    }


    public static PlaceOrderResponse getPlaceOrderResponse(String jsonString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        PlaceOrderResponse placeOrderResponse = mapper.readValue(jsonString, PlaceOrderResponse.class);
        return placeOrderResponse;
    }

    public static GetOrdersByOrderIdResponse getOrdersByIdResponse(String jsonString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        GetOrdersByOrderIdResponse getOrdersResponse = mapper.readValue(jsonString, GetOrdersByOrderIdResponse.class);
        return getOrdersResponse;
    }

    public static UpdateOrderResponse getUpdateOrderResponse(String jsonString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        UpdateOrderResponse updateOrderResponse = mapper.readValue(jsonString, UpdateOrderResponse.class);
        return updateOrderResponse;
    }

}
