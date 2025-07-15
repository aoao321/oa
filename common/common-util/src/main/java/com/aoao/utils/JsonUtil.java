package com.aoao.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author aoao
 * @create 2025-07-10-15:10
 */
public class JsonUtil {

    private static final ObjectMapper INSTANCE = new ObjectMapper();

    /**
     * 转化成json
     */
    public static String toJson(Object obj) {
        try {
            return INSTANCE.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json转化对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return INSTANCE.readValue(json,clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
