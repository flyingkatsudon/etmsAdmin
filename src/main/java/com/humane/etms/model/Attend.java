package com.humane.etms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
    @Column(columnDefinition = "bit default 0") private boolean isAssignedGroup;  // 조 배정 현황 사용
    @Column(columnDefinition = "bit default 0") private boolean isUseScanner;  // 외부 스캐너 사용여부
    @Column(columnDefinition = "bit default 0") private boolean isUseGroup;  // 조 사용 여부
    @Column(columnDefinition = "int default 0") private int paperCnt; // 답안지 매수
    @Column(columnDefinition = "int default 5") private int paperLen;  // 답안지 자리수
    @Column(columnDefinition = "bit default 1") private boolean isPaperChange; // 답안지 교체 허용여부
    @Column private String paperHeader; // 답안지 헤더값

    private String firstAssignPaperCd; // 시험에 배정된 답안지 중 첫번째 코드
    private String lastAssignPaperCd; // 시험에 배정된 답안지 중 마지막 코드

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date attendDate; // 출결일자

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
    @Temporal(TemporalType.TIME)
    private Date attendTime; // 출결시간
    private String typeNm;
}
