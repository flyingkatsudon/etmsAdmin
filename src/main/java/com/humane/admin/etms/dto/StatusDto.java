package com.humane.admin.etms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StatusDto implements Serializable {
    String admissionNm;
    String attendNm;
    String majorNm;
    String deptNm;
    String typeNm;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    Date attendDate;

    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
    Date attendTime;
    String hallCd;
    String headNm;
    String bldgNm;
    String hallNm;
    String examineeCd;
    String examineeNm;
    Long examineeCnt;
    Long attendCnt;
    Long absentCnt;
    Double attendPer;
    Double absentPer;
    Boolean isAttend;
    String groupNm;
    String attendHeadNm;
    String attendBldgNm;
    String attendHallNm;
    Boolean isEtc;
    Boolean isSend;
    Boolean isOtherHall;
}