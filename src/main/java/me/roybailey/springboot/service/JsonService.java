package me.roybailey.springboot.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

@Service
public class JsonService {

    private ObjectMapper mapper;

    public JsonService() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public <INTO> INTO decode(String data, TypeReference<INTO> typeReference) {
        try {
            INTO result = mapper.readValue(data, typeReference);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to decode typeReference " + typeReference.getType().getTypeName(), e);
        }
    }

    public <FROM> String encode(FROM data) {
        try {
            return mapper.writeValueAsString(data);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encode typeReference " + data.getClass().getName(), e);
        }
    }
}


