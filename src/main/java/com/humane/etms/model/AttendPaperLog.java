package com.humane.etms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Table
@Data
public class AttendPaperLog implements Serializable {
    @Id @GeneratedValue private Long _id;
    private Date logDttm;

    @PrePersist
    public void prePersist() {
        logDttm = new DateTime().toDate();
    }

    @ManyToOne @JoinColumn(name = "attendCd", nullable = false) private Attend attend;
    @ManyToOne @JoinColumn(name = "examineeCd", nullable = false) private Examinee examinee;
    @Column(nullable = false) private String paperCd;

    @ManyToOne @JoinColumn(name = "hallCd", nullable = false) private Hall hall;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date regDttm;

    @Column(columnDefinition = "bit") private Boolean isScanner;
}
