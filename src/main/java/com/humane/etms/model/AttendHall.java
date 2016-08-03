package com.humane.etms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"attendCd", "hallCd"})})
@Data
public class AttendHall {
    @Id @GeneratedValue private Long _id;

    @ManyToOne @JoinColumn(name = "attendCd", nullable = false) private Attend attend;
    @ManyToOne @JoinColumn(name = "hallCd", nullable = false) private Hall hall;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date signDttm;
}