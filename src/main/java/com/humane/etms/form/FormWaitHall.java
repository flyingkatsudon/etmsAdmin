package com.humane.etms.form;

import com.blogspot.na5cent.exom.annotation.Column;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class FormWaitHall {
    @Column(name = "구분") private String division;
    @Column(name = "전형명") private String admissionNm;
    @Column(name = "시험일자") private String attendDate;
    @Column(name = "시험시간") private String attendTime;
    @Column(name = "고사본부") private String headNm;
    @Column(name = "고사건물") private String bldgNm;
    @Column(name = "고사실") private String hallNm;
    @Column(name = "조") private String groupNm;

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
