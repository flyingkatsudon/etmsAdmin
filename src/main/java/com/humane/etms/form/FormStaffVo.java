package com.humane.etms.form;

import com.blogspot.na5cent.exom.annotation.Column;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class FormStaffVo {
    @Column(name = "성명") private String staffNm;
    @Column(name = "전화번호") private String phoneNo;
    @Column(name = "고사건물") private String bldgNm;
}
