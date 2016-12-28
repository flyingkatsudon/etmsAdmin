package com.humane.etms.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"admissionCd"}))
@Data
public class AttendDoc {
    @Id @GeneratedValue private Long _id;

    @ManyToOne @JoinColumn(name = "admissionCd", nullable = false) private Admission admission;
    private String title;
    private String content;
    private String sign;
    private String signDate;
}
