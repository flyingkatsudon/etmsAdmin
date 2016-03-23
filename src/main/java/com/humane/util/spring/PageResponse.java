package com.humane.util.spring;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class PageResponse<T> {
    private List<T> content;
    private boolean last;
    private int totalElements;
    private int totalPages;
    private int size;
    private int number;
    private int numberOfElements;
    private boolean first;
    private List<Sort> sort;

    @Getter
    @Setter
    @ToString
    public class Sort {
        private boolean ascending;
        private String direction;
        private boolean ignoreCase;
        private String nullHandling;
        private String property;
    }
}
