package com.humane.etms.form;

import com.blogspot.na5cent.exom.annotation.Column;
import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class FormWaitHall {
    @Column(name = "구분") private String division;
    @Column(name = "조") private String groupNm;
    @Column(name = "고사본부") private String headNm;
    @Column(name = "고사건물") private String bldgNm;
    @Column(name = "고사실") private String hallNm;
}
