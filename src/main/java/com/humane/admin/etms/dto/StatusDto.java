package com.humane.admin.etms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatusDto implements Serializable {
    String admissionNm;
    String attendNm;
    String majorNm;
    String deptNm;
    Date attendDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
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
    Long attendPer;
    Long absentPer;
    Boolean isAttend;
    String groupNm;
}