package com.humane.util.query;

import java.util.*;

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
        StringBuilder sb = new StringBuilder();
        map.forEach((String s, Object o) -> {
            if (!Objects.equals(o, ""))
                sb.append(s).append("=").append(o).append(",");
        });
        return sb.toString();
    }
}
