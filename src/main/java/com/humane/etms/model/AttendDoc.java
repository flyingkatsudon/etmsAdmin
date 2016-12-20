package com.humane.etms.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"attendCd"}))
@Data
public class AttendDoc {
    @Id @GeneratedValue private Long _id;

    @ManyToOne @JoinColumn(name = "attendCd", nullable = false) private Attend attend;
    private String title;
    private String content;
    private String sign;
    private String signDate;
}
