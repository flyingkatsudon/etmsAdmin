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
public class DuplicateDto implements Serializable {
    private String userAdmissions;
    private String admissionCd;
    private String admissionNm;
    private String attendCd;

    private String deviceId;
    private String deviceNo;

    private String bldgNm;
    private String hallNm;

    private String attendNm;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date attendDate;

    @DateTimeFormat(pattern = "HH:mm")
    @JsonSerialize(using = TimeSerializer.class)
    private Date attendTime;

    private String examineeCd;
    private String examineeNm;

    private String duplicateCnt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date logDttm;
    private boolean currentState;
}