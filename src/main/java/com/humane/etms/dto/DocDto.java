package com.humane.etms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.humane.util.jackson.PercentSerializer;
import com.humane.util.jackson.TimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DocDto implements Serializable {
    private String userAdmissions;
    private String admissionNm;

/*    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date attendDate;

    @DateTimeFormat(pattern = "HH:mm")
    @JsonSerialize(using = TimeSerializer.class)
    private Date attendTime;*/

    private String attendDate;
    private String attendTime;

    // examinee
    private BufferedImage examineeImage;
    private String examineeCd;
    private String examineeNm;
    private String deptNm;

    private String title;
    private String content;

    private String signDate;

    private String sign;

    private BufferedImage noIdCardImage;
    private BufferedImage noIdCardSign;
}