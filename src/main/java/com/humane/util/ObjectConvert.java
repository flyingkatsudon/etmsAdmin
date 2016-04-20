package com.humane.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.Iterator;
import java.util.Map;

public class ObjectConvert {

    public static <K, V> Map<K, V> asMap(Object object) {
        return asMap(object, false);
    }

    public static <K, V> Map<K, V> asMap(Object object, boolean useEmpty) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        Map<K, V> map = objectMapper.convertValue(object, new TypeReference<Map>() {
        });

        if (!useEmpty) {
            Iterator<Map.Entry<K, V>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<K, V> entry = it.next();
                Object value = entry.getValue();

                if (value instanceof String && value.equals("")) it.remove();
            }
        }
        return map;
    }
}
