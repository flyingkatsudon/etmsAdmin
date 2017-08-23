package com.humane.etms.form;

import com.blogspot.na5cent.exom.annotation.Column;
import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class FormStaffVo {
    @Column(name = "성명") private String staffNm;
    @Column(name = "전화번호") private String phoneNo;
    @Column(name = "전형코드") private String admissionCd;
    @Column(name = "전형명") private String admissionNm;
    @Column(name = "시험코드") private String attendCd;
    @Column(name = "시험일자") private String attendDate;
    @Column(name = "시험시간") private String attendTime;
    @Column(name = "고사건물") private String bldgNm;

    public Date getAttendDate() {
        Date tmp = null;
        try{
            tmp = new SimpleDateFormat("yyyy-MM-dd").parse(attendDate);
        }catch(Exception e){
            e.printStackTrace();
        }
        return tmp;
    }

    public Date getAttendTime() {
        Date tmp = null;
        try{
            tmp = new SimpleDateFormat("HH:mm:ss").parse(attendTime);
        }catch(Exception e){
            e.printStackTrace();
        }
        return tmp;
    }
}
