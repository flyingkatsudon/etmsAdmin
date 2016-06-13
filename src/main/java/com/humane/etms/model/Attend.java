package com.humane.etms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Table(name = "attend")
public class Attend {
    @Id private String attendCd;
    private String attendNm;
    @ManyToOne @JoinColumn(name = "admissionCd") private Admission admission;

    @Column(columnDefinition = "int default 9") private int attendLen;
    @Column(columnDefinition = "bit default 0") private boolean isUseScanner;  // 외부 스캐너 사용여부
    @Column(columnDefinition = "int default 0") private int paperCnt;
    @Column(columnDefinition = "int default 5") private int paperLen;  // 답안지 자리수.
    @Column(columnDefinition = "bit default 1") private boolean isPaperChange;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @Temporal(TemporalType.DATE)
    private Date attendDate; // 출결일자

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
    @Temporal(TemporalType.TIME)
    private Date attendTime; // 출결시간
}
