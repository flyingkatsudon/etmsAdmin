package com.humane.util;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class QueryBuilder {

    private final Map<String, Object> map;

    public QueryBuilder() {
        this.map = new HashMap<>();
    }

    public QueryBuilder add(String key, Object value) {
        map.put(key, value);
        return this;
    }

    public String build() {
        Set<String> keyset = map.keySet();
        StringBuilder sb = new StringBuilder();
        for (String key : keyset) {
            sb.append(key).append("=").append(map.get(key));
        }
        return sb.toString();
    }
}
