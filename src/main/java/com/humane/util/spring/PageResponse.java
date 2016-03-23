package com.humane.util.spring;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
public class PageResponse<T> {
    private List<T> content;
    private boolean last;
    private int totalPages;
    private int totalElements;
    private int number;
    private int size;
    private List<Sort> sort;
    private boolean first;
    private int numberOfElements;

    @Getter
    @Setter
    @ToString
    public static class Sort {
        private String direction;
        private String property;
        private boolean ignoreCase;
        private String nullHandling;
        private boolean ascending;
    }
}
