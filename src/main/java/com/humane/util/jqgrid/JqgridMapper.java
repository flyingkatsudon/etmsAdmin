package com.humane.util.jqgrid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.humane.util.query.QueryBuilder;
import com.humane.util.spring.PageResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Maps a jQgrid JSON query to a {@link Filter} instance
 */
public class JqgridMapper {

    public static Filter parseFilter(String jsonString) {
        if (jsonString != null) {
            ObjectMapper mapper = new ObjectMapper();

            try {
                return mapper.readValue(jsonString, Filter.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public static String[] getSortString(String sidx, String sord){
        List<String> l = new ArrayList<>();
        String s = sidx + " " + sord;

        String[] a = s.split(",");
        for (String string : a) {
            String[] b = string.trim().split(" ");
            l.add(b[0] + "," + b[1]);
        }
        return l.toArray(new String[l.size()]);
    }

    public static JqgridResponse getResponse(InputStream inputStream) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        PageResponse pageResponse = mapper.readValue(inputStream, PageResponse.class);
        JqgridResponse jqgridResponse = new JqgridResponse<>();
        jqgridResponse.setRows(pageResponse.getContent());
        jqgridResponse.setRecords(String.valueOf(pageResponse.getTotalElements()));
        jqgridResponse.setTotal(String.valueOf(pageResponse.getTotalPages()));
        jqgridResponse.setPage(String.valueOf(pageResponse.getNumber() + 1));
        return jqgridResponse;
    }

    public static String getQueryString(String filters) {
        QueryBuilder q = new QueryBuilder();
        JqgridMapper.Filter filter = JqgridMapper.parseFilter(filters);
        for (JqgridMapper.Filter.Rule rule : filter.getRules()) {
            q.add(rule.getField(), rule.getData());
        }
        return q.build();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JqgridResponse<T> {
        private String page;
        private String total;
        private String records;
        private List<T> rows;
    }

    /**
     * A POJO that represents a jQgrid JSON requests {@link String}<br/>
     * A sample filter follows the following format:
     * <pre>
     * {"groupOp":"AND","rules":[{"field":"firstName","op":"eq","data":"John"}]}
     * </pre>
     */
    @Data
    @NoArgsConstructor
    public static class Filter {

        private String source;
        private String groupOp;
        private ArrayList<Rule> rules;

        public Filter(String source) {
            super();
            this.source = source;
        }

        /**
         * Inner class containing field rules
         */
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Rule {
            private String junction;
            private String field;
            private String op;
            private String data;
        }
    }
}