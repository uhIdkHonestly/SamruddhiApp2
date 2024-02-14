package common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samruddhi.trading.equities.domain.Bar;
import com.samruddhi.trading.equities.domain.OptionData;
import com.samruddhi.trading.equities.domain.placeorder.PlaceOrderResponse;

import java.io.File;
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

    public static List<Bar> getListOfBars(String jsonString, String key) {
        List<Bar> bars = Collections.emptyList();
        try {
            // Create an ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();

            // Read the JSON array and map it to a List of Bar objects
            bars = objectMapper.readValue(jsonString, new TypeReference<List<Bar>>() {
            });
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
}
