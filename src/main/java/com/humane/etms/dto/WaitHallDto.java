package com.humane.etms.dto;

/**
 * Created by Jeremy on 2017. 8. 7..
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class WaitHallDto{
    private String id;
    private String groupNm;
    private String headNm;
    private String bldgNm;
    private String hallCd;
    private String hallNm;
    private String division;
}