package com.humane.etms.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"attendCd", "hallCd"})})
@Data
public class AttendHall {
    @Id @GeneratedValue private long _id;

    @ManyToOne @JoinColumn(name = "attendCd", nullable = false) private Attend attend;
    @ManyToOne @JoinColumn(name = "hallCd", nullable = false) private Hall hall;
}