package com.humane.etms.form;

import com.blogspot.na5cent.exom.annotation.Column;
import lombok.Data;

@Data
public class FormHallVo {
    @Column(name = "모집") private String recruitNm;
    @Column(name = "전형명") private String admissionNm;
    @Column(name = "전형코드") private String admissionCd;
    @Column(name = "시험코드") private String attendCd;
    @Column(name = "시험명") private String attendNm;
    @Column(name = "시험일자") private String attendDate;
    @Column(name = "시험시간") private String attendTime;

    @Column(name = "고사실코드") private String hallCd;
    @Column(name = "고사본부") private String headNm;
    @Column(name = "고사건물") private String bldgNm;
    @Column(name = "고사실") private String hallNm;
}
