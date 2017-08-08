package com.humane.etms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;
import java.io.Serializable;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StaffDto implements Serializable {
    private String _id;
    private String staffNm;
    private String phoneNo;
    private String bldgNm;

    // 기존정보
    private String _staffNm;
    private String _phoneNo;
    private String _bldgNm;
}