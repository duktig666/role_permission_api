package com.test.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

/**
 * json工具包
 *
 * @author RenShiWei
 * Date: 2020/8/3 21:07
 */
public class JsonUtils {

    @Getter
    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(MapperFeature.USE_GETTERS_AS_SETTERS, false);
        mapper.registerModule(new JavaTimeModule());
    }

    static {

    }

    public static <T> T unmarshal(String value, TypeReference<T> typeRef) {
        try {
            return mapper.readValue(value, typeRef);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T unmarshal(InputStream value, TypeReference<T> typeRef) {
        try {
            return mapper.readValue(value, typeRef);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T unmarshal(String value, Class<T> type) {
        try {
            return mapper.readValue(value, type);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String marshal(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }


}