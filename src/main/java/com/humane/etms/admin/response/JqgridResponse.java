package com.humane.etms.admin.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JqgridResponse<T extends Serializable> {
    private String page;
    private String total;
    private String records;
    private List<T> rows;
}
