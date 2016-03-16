package com.humane.etms.admin.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * spring data jpa page response
 *
 * @param <T>
 */
@Data
public class PageResponse<T extends Serializable> {
    private List<T> content;
    private boolean last;
    private int totalElements;
    private int totalPages;
    private int size;
    private int number;
    private int numberOfElements;
    private boolean first;
    List<Sort> sort;

    @Data
    public class Sort {
        private boolean ascending;
        private String direction;
        private boolean ignoreCase;
        private String nullHandling;
        private String property;
    }
}