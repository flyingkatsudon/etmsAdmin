package com.humane.etms.dto;

/**
 * Created by Jeremy on 2017. 8. 7..
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class WaitHallDto{
    private String id;
    private String attendCd;
    private String attendNm;
    private String groupNm;
    private String headNm;
    private String bldgNm;
    private String hallCd;
    private String hallNm;
    private String division;

    // attendHall에 존재
    private String groupNmList;

    // attend_wait_hall에 존재, 시연용 앱에서는 사용하지 않는 테이블
    private List groupList;
}