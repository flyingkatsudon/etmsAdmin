package com.humane.etms.form;

import com.blogspot.na5cent.exom.annotation.Column;
import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class FormHallVo {
    @Column(name = "전형명") private String admissionNm;
    @Column(name = "전형코드") private String admissionCd;
    @Column(name = "계열") private String typeNm;
    @Column(name = "시험코드") private String attendCd;
    @Column(name = "시험명") private String attendNm;
    @Column(name = "시험일자") private String attendDate;
    @Column(name = "시험시간") private String attendTime;

    public String getAttendTime() {
        try {
            Date date = new SimpleDateFormat("YYYY-mm-dd HH:mm:ss").parse(attendTime);
            return new SimpleDateFormat("HH:mm:ss").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return attendTime;
    }

    @Column(name = "고사실코드") private String hallCd;
    @Column(name = "고사본부") private String headNm;
    @Column(name = "고사건물") private String bldgNm;
    @Column(name = "고사실") private String hallNm;
    @Column(name = "예비고사실여부") private Boolean isEtc;

    @Column(name = "수험번호 자릿수") private String attendLen;
    @Column(name = "답안지 교체여부") private Boolean isPaperChange;
    @Column(name = "답안지 매수") private String paperCnt;
    @Column(name = "답안지 자릿수") private String paperLen;
    @Column(name = "답안지 헤더") private String paperHeader;

}
