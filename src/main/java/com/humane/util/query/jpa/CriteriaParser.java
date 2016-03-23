package com.humane.util.query.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CriteriaParser {
    protected List<Criteria> criteriaList;

    public CriteriaParser(String q) {

        criteriaList = new LinkedList<>();

        if (q != null) {
            final Pattern pattern = Pattern.compile("(\\w+)(:|=|<|>)([\\w|가-힣|\\s|\\(|\\)|-]+),");
            final Matcher matcher = pattern.matcher(q + ",");
            while (matcher.find()) {
                criteriaList.add(new Criteria(matcher.group(1), matcher.group(2), matcher.group(3)));
            }
        }
    }

    public List<Criteria> getCriteriaList(){
        return criteriaList;
    }

    @Data
    @AllArgsConstructor
    public class Criteria {
        private String key;
        private String operation;
        private String value;
    }
}
