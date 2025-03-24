package com.example.testservice.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Getter
public class JsonMapper<T> {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Class<T> clazz;

    public JsonMapper(Class<T> clazz) {
        mapper.registerModule(new JavaTimeModule());
        this.clazz = clazz;
    }

    public T deserialize(String body) {
        log.info("ResponseBody:\n{}\n", body);
        try {
            return mapper.readValue(body, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize JSON", e);
        }
    }

    public List<T> deserializeList(String body) {
        log.info("ResponseBody:\n{}\n", body);
        try {
            return mapper.readValue(body, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize JSON list", e);
        }
    }

    public String serialize(T obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize JSON", e);
        }
    }
}

