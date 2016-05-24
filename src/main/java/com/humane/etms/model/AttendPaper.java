package com.humane.etms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"paperCd"})})
@Data
public class AttendPaper implements Serializable {
    @Id @GeneratedValue private long _id;

    @Column(columnDefinition = "int default 1", nullable = false) private int paperNo;
    @Column(nullable = false) private String paperCd;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "attendMapSeq", nullable = false)
    private AttendMap attendMap;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date regDttm;

    @ManyToOne @JoinColumn(name = "deviceNo") private Device device;
}
