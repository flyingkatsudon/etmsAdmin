package com.humane.etms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"examineeCd", "attendCd"}))
@Data
public class AttendMap {
    @Id @GeneratedValue private long attendMapSeq;

    @ManyToOne @JoinColumn(name = "attendCd", nullable = false) private Attend attend;
    @ManyToOne @JoinColumn(name = "examineeCd", nullable = false) private Examinee examinee;

    @ManyToOne @JoinColumn(name = "hallCd", nullable = false) private Hall hall;
    @ManyToOne @JoinColumn(name = "attendHallCd") private Hall attendHall;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date attendDttm;

    private String groupNm;
    @ManyToOne @JoinColumn(name = "deviceNo") private Device device;
    @Column(columnDefinition = "bit" ) private Boolean isMidOut;
    @Column(columnDefinition = "bit") private Boolean isNoIdCard;
    @Column(columnDefinition = "bit") private Boolean isCheck;
    @Column(columnDefinition = "bit") private Boolean isCheat;
    private String memo;
}