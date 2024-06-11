package com.adrhol.rate_limiting_service;

import com.adrhol.rate_limiting_service.model.RateBucketDTO;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.time.LocalDateTime;


public class RateBucketDeserializer extends JsonDeserializer<LocalDateTime> {


    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
        LocalDateTime duration = LocalDateTime.parse(jsonNode.get("duration").asText());
        return duration;
    }
}
