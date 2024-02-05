package common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

public class JsonParser {

    public static Optional<String> getJsonValue(String jsonString, String key)  {
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
}
