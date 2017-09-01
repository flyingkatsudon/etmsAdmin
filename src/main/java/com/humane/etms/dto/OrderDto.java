/**
 * Created by Jeremy on 2017. 8. 29..
 */
package com.humane.etms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OrderDto implements Serializable {

    private String attendCd;
    private String examineeCd;

    private Long attendCnt;
    private Long groupCnt;
    private Long orderCnt;

    private String groupNm;
    private String groupOrder;
    private String debateNm;
    private String debateOrder;

    private List<Map<String, String>> groupList;
}