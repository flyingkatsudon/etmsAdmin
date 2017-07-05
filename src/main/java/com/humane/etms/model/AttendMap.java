package com.humane.etms.model;

import ch.qos.logback.classic.db.names.ColumnName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.humane.etms.listener.AttendMapListener;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@EntityListeners(AttendMapListener.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"examineeCd", "attendCd"}))
@Data
public class AttendMap implements Serializable {
    @Id @GeneratedValue private Long _id;

    @ManyToOne @JoinColumn(name = "attendCd", nullable = false) private Attend attend;
    @ManyToOne @JoinColumn(name = "examineeCd", nullable = false) private Examinee examinee;

    @ManyToOne @JoinColumn(name = "hallCd", nullable = false) private Hall hall;
    @ManyToOne @JoinColumn(name = "attendHallCd") private Hall attendHall;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date attendDttm;

    private String groupNm;
    @Column(columnDefinition = "bit") private Boolean isMidOut;
    @Column(columnDefinition = "bit") private Boolean isCheat;
    private String memo;
    private String subject;

    @Column(columnDefinition = "bit") private Boolean isScanner;
    private String groupOrder;
    private Long deviceId;

    private String debateNm;
    private String debateOrder;
}