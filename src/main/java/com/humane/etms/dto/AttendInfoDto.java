package com.humane.etms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.humane.util.jackson.TimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AttendInfoDto implements Serializable {
    private String userAdmissions;
    private String admissionCd;
    private String admissionNm;

    private String attendCd;
    private String attendNm;
    private Long attendLen;

    private String typeNm;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date attendDate;

    @DateTimeFormat(pattern = "HH:mm")
    @JsonSerialize(using = TimeSerializer.class)
    private Date attendTime;

    private boolean isPaperChange;
    private boolean isUseGroup;
    private boolean isAssignedGroup;
    private boolean isUseScanner;

    private Long paperCnt;
    private Long paperLen;

    private String paperHeader;
    private String firstAssignPaperCd;
    private String lastAssignPaperCd;

    private boolean isEmptyHall;
    private String groupNmList;

    // 웹으로부터 전달받을 날짜와 시간
    private String atDate;
    private String atTime;
}